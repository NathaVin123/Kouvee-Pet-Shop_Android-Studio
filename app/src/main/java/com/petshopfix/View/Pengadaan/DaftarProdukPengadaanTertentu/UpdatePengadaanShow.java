package com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.petshopfix.API.Interface.ApiDetailPengadaan;
import com.petshopfix.API.Interface.ApiPengadaan;
import com.petshopfix.API.Interface.ApiSupplier;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.R;
import com.petshopfix.View.Pengadaan.DaftarPengadaan.ShowPengadaan;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaan.CreatePengadaanShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UpdatePengadaanShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnBatal, btnSimpan, btnTambah;
    private Spinner spinnerSupplier;
    private UpdatePengadaanAdapter adapter;
    private List<DetailPengadaanDAO> listDTP;
    private String nomorPO;
    private List<SupplierDAO> listsupplier;
    private int selectedIdSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pengadaan_show);

        init();
        setDetailPengadan();
        setAdapter();
        setAtribut();
    }

    private void setAtribut() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreatePengadaanShow.class);
                i.putExtra("nomorPO", nomorPO);
                i.putExtra("cek", "Ubah Pengadaan");
                i.putExtra("nama_supplier",getIntent().getStringExtra("nama_supplier"));
                startActivity(i);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double totalHarga_po = 0.0;
                for(DetailPengadaanDAO detailPengdaan : listDTP)
                {
                    totalHarga_po = totalHarga_po + (detailPengdaan.getJumlah_po()*detailPengdaan.getHarga_produk());
                }
                System.out.println(totalHarga_po);

                updateTransaksiPengadaan(nomorPO,selectedIdSupplier,totalHarga_po);
            }
        });
    }

    private void init() {
        nomorPO = getIntent().getStringExtra("nomorPO");
        listDTP = new ArrayList<DetailPengadaanDAO>();
        listsupplier = new ArrayList<SupplierDAO>();

        recyclerView = findViewById(R.id.recycler_view);
        spinnerSupplier = findViewById(R.id.namaSupplier_up);
        btnTambah = findViewById(R.id.btnTambah_up);
        btnSimpan = findViewById(R.id.btnSimpan_up);
        btnBatal = findViewById(R.id.btnBatal_up);

        setListSupplier();
    }

    private void setListSupplier() {
        ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
        Call<com.petshopfix.API.Response> suppliers = apiService.getAll();

        suppliers.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getSupplier().isEmpty())
                {
                    listsupplier.addAll(response.body().getSupplier());
                    List<String> daftarNamaSupplier = new ArrayList<>();
                    selectedIdSupplier=listsupplier.get(0).getId_supplier();
                    for (int i =0; i<listsupplier.size(); i++)
                    {
                        daftarNamaSupplier.add(listsupplier.get(i).getNama_supplier());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, daftarNamaSupplier);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSupplier.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition(getIntent().getStringExtra("nama_supplier"));
                    spinnerSupplier.setSelection(spinnerPosition);

                    spinnerSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i=0; i<listsupplier.size();i++)
                            {
                                if(spinnerSupplier.getSelectedItem().toString().equals(listsupplier.get(i).getNama_supplier()))
                                {
                                    selectedIdSupplier = listsupplier.get(i).getId_supplier();
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

    private void setAdapter() {
        adapter = new UpdatePengadaanAdapter(this, listDTP, nomorPO);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DAFTAR PRODUK");
    }

    private void setDetailPengadan() {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<Response> dtp = apiService.tampilPengadaan(nomorPO);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtp.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(!response.body().getDetailPengadaan().isEmpty())
                {
                    listDTP.addAll(response.body().getDetailPengadaan());
                    adapter.notifyDataSetChanged();
                    if(!listDTP.isEmpty())
                        System.out.println(listDTP.get(0).getJumlah_po());
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTransaksiPengadaan(String nomorPO, int id_supplier, Double totalHarga_po)
    {
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<Response> pengadaan = apiService.updatePengadaan(nomorPO, id_supplier, totalHarga_po, "Belum Datang");

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UpdatePengadaanShow.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        pengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                System.out.println(response.body());
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                    for(DetailPengadaanDAO dtp : listDTP)
                    {
                        updateDetailPengadaan(nomorPO, dtp.getId_produk(), dtp.getSatuan(), dtp.getJumlah_po());
                    }
                    startActivity(new Intent(getApplicationContext(), ShowPengadaan.class));
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdatePengadaanShow.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDetailPengadaan(String nomorPO, String id_produk, String satuan, int jumlah_po)
    {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<com.petshopfix.API.Response> detailPengadaan = apiService.updateDetailPengadaan(nomorPO, id_produk, satuan, jumlah_po);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UpdatePengadaanShow.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdatePengadaanShow.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
