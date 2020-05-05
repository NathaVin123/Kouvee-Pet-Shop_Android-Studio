package com.petshopfix.View.Pengadaan.DaftarProdukPengadaan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.ProdukDAO;
import com.petshopfix.R;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu.UpdatePengadaanShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreatePengadaanShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CreatePengadaanAdapter adapter;
    private List<ProdukDAO> listProduk;
    private String nomorPO, cek, nama_supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pengadaan_show);

        setAdapter();
    }

    private void setAdapter() {
        nama_supplier = getIntent().getStringExtra("nama_supplier");
        nomorPO = getIntent().getStringExtra("nomorPO");
        cek = getIntent().getStringExtra("cek");
        listProduk = new ArrayList<ProdukDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CreatePengadaanAdapter(this, listProduk, nomorPO, cek, nama_supplier);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DAFTAR PRODUK TERSEDIA");

        EditText searchView = (EditText) findViewById(R.id.txtSearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });

        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapters = ArrayAdapter
                .createFromResource(this, R.array.sortirPengadaan,
                        android.R.layout.simple_spinner_item);
        dropdown.setAdapter(adapters);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String berdasarkan = null;

                if(position == 0)
                    setProduk();
                else if(position == 1)
                    berdasarkan = "harga_tinggi";
                else if(position == 2)
                    berdasarkan = "harga_rendah";
                else if(position == 3)
                    berdasarkan = "stok_tinggi";
                else if(position == 4)
                    berdasarkan = "stok_rendah";

                System.out.println(berdasarkan);
                if (berdasarkan!=null)
                {
                    ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
                    Call<Response> produks = apiService.sortir(berdasarkan);

                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(CreatePengadaanShow.this);
                    progressDialog.setMessage("loading....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDialog.show();

                    produks.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if(!response.body().getProduk().isEmpty())
                            {
                                listProduk.clear();
                                listProduk.addAll(response.body().getProduk());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setProduk() {
        ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
        Call<Response> produks = apiService.getAll();

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Menampilkan Daftar Produk");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDialog.show();

        produks.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(!response.body().getProduk().isEmpty())
                {
                    listProduk.addAll(response.body().getProduk());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(cek.equals("Ubah Pengadaan"))
        {
            Intent i = new Intent(getApplicationContext(), UpdatePengadaanShow.class);
            i.putExtra("nomorPO", nomorPO);
            i.putExtra("nama_supplier", nama_supplier);
            startActivity(i);
        }
    }
}
