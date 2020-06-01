package com.petshopfix.Activity.Penjualan.Produk;

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
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Interface.ApiTransaksiProduk;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.PenjualanProduk.TambahTransaksi.TambahProdukPenjualanShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PilihCustomer extends AppCompatActivity {

    private Button btn_tambah;
    private TextView no_transaksi, txtAlamat, txtNoTelp;
    private String nomorTransaksi, nama_customer;
    private Spinner dataCustomer;
    private int selectedIdCustomer;
    private DatabaseHandler db;
    private List<CustomerDAO> listCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_customer);

        init();
        setAtribut();
    }

    private void init() {
        btn_tambah = (Button) findViewById(R.id.btnTambah_customer);
        no_transaksi = (TextView) findViewById(R.id.txtNoTransaksi);
        txtAlamat = (TextView) findViewById(R.id.txtAlamat);
        txtNoTelp = (TextView) findViewById(R.id.txtNoTelp);
        dataCustomer = (Spinner) findViewById(R.id.dataCustomer);
        listCustomer = new ArrayList<>();

        db = new DatabaseHandler(this);

        setNoTransaksi();
        setCustomer();
    }

    private void setAtribut() {
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanTransaksiPenjualanProduk(String.valueOf(selectedIdCustomer));
            }
        });
    }

    private void setNoTransaksi()
    {
        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<String> penjualanProduk = apiService.getNoTransaksi();

        penjualanProduk.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                nomorTransaksi = response.body();
                no_transaksi.setText(nomorTransaksi);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void setCustomer() {
        ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
        Call<com.petshopfix.API.Response> suppliers = apiService.getAll();

        suppliers.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, Response<com.petshopfix.API.Response> response) {
                if (!response.body().getCustomer().isEmpty())
                {
                    listCustomer.addAll(response.body().getCustomer());
                    List<String> daftarNamaSupplier = new ArrayList<>();
                    selectedIdCustomer=listCustomer.get(0).getId_customer();

                    for (int i=0; i<listCustomer.size();i++)
                    {
                        daftarNamaSupplier.add(listCustomer.get(i).getNama_customer());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, daftarNamaSupplier);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataCustomer.setAdapter(adapter);

                    dataCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i=0; i<listCustomer.size();i++)
                            {
                                if (dataCustomer.getSelectedItem().toString().equals(listCustomer.get(i).getNama_customer()))
                                {
                                    selectedIdCustomer = listCustomer.get(i).getId_customer();
                                    nama_customer = listCustomer.get(i).getNama_customer();
                                    txtAlamat.setText(listCustomer.get(i).getAlamat_customer());
                                    txtNoTelp.setText(listCustomer.get(i).getNoTelp_customer());
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
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {

            }
        });
    }

    private void simpanTransaksiPenjualanProduk(String id_customer)
    {
        String idCS = db.getUser(1).getNIP();

        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<com.petshopfix.API.Response> transaksiProduk = apiService.createPenjualanProduk(id_customer, 0.0, idCS);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(PilihCustomer.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //show it
        progressDialog.show();

        transaksiProduk.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, Response<com.petshopfix.API.Response> response) {
                progressDialog.dismiss();
                if (!response.body().getTransaksiProduk().isEmpty())
                {
                    System.out.println("Success");
                    Intent i = new Intent(getApplicationContext(), TambahProdukPenjualanShow.class);
                    i.putExtra("no_transaksi", response.body().getTransaksiProduk().get(0).getNo_transaksi());
                    i.putExtra("status", "tambah");
                    i.putExtra("nama_customer", nama_customer);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
