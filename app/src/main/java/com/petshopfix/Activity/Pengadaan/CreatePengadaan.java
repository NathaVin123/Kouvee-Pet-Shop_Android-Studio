package com.petshopfix.Activity.Pengadaan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiPengadaan;
import com.petshopfix.API.Interface.ApiSupplier;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.R;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaan.CreatePengadaanShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreatePengadaan extends AppCompatActivity {

    private Button btnTambah;
    private TextView nomorPO, txtAlamat, txtNoTelp;
    private String nomorPemesanan, nama_supplier;
    private Spinner dataSupplier;
    private int selectedIDSupplier;
    private List<SupplierDAO> listSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pengadaan);

        setAtribut();
        init();
    }

    private void setAtribut() {
        btnTambah = (Button) findViewById(R.id.btnTambahPemesanan);
        nomorPO = (TextView) findViewById(R.id.txtNomorPO);
        txtAlamat = (TextView) findViewById(R.id.txtAlamat);
        txtNoTelp = (TextView) findViewById(R.id.txtNoTelp);
        dataSupplier = (Spinner) findViewById(R.id.dataNamaSupplier);
        listSupplier = new ArrayList<>();

        setnomorPO();
        setSupplier();
    }

    private void setSupplier() {
        ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
        Call<com.petshopfix.API.Response> suppliers = apiService.getAll();

        suppliers.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                if (!response.body().getSupplier().isEmpty())
                {
                    listSupplier.addAll(response.body().getSupplier());
                    List<String> daftarNamaSupplier = new ArrayList<>();
                    selectedIDSupplier=listSupplier.get(0).getId_supplier();
                    for (int i=0; i<listSupplier.size(); i++)
                    {
                        daftarNamaSupplier.add(listSupplier.get(i).getNama_supplier());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, daftarNamaSupplier);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataSupplier.setAdapter(adapter);

                    dataSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i=0; i<listSupplier.size(); i++)
                            {
                                if (dataSupplier.getSelectedItem().toString().equals(listSupplier.get(i).getNama_supplier()))
                                {
                                    selectedIDSupplier = listSupplier.get(i).getId_supplier();
                                    txtAlamat.setText(listSupplier.get(i).getAlamat_supplier());
                                    txtNoTelp.setText(listSupplier.get(i).getNoTelp_supplier());
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void setnomorPO() {
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<String> nomor_po = apiService.getNomorPO();

        nomor_po.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (!response.body().isEmpty())
                {
                    nomorPemesanan = response.body();
                    nomorPO.setText(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(t.getCause());
            }
        });
    }

//    private void setnoPO() {
//        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
//        Call<String> no_po = apiService.getNomorPO();
//
//        no_po.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                if (!response.body().isEmpty())
//                {
//                    nomorPemesanan = response.body();
//                    nomorPO.setText(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                System.out.println(t.getCause());
//            }
//        });
//    }

    private void init() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanPengadaan(selectedIDSupplier);

                Intent i = new Intent(getApplicationContext(), CreatePengadaanShow.class);
                i.putExtra("nomorPO", nomorPemesanan);
                i.putExtra("cek", "Tambah Pengadaan");
                startActivity(i);
            }
        });
    }

    private void simpanPengadaan(int id_supplier)
    {
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<com.petshopfix.API.Response> pengadaan = apiService.createPengadaan(id_supplier);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(CreatePengadaan.this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        pengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                if (response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreatePengadaan.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
