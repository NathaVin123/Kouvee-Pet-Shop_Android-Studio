package com.petshopfix.View.Pengadaan.DaftarPengadaan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiPengadaan;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.HomeActivity;
import com.petshopfix.DAO.TransaksiPengadaanDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ShowPengadaan extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PengadaanAdapter adapter;
    private List<TransaksiPengadaanDAO> listPengadaaan;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pengadaan);

        setAdapter();
        setPengadaan();
    }

    private void setAdapter() {
        listPengadaaan = new ArrayList<TransaksiPengadaanDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new PengadaanAdapter(this, listPengadaaan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setPengadaan() {
        judul.setText("Daftar Pengadaan Produk");
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<Response> pengadaan = apiService.getAll();

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan Daftar Pengadaan Produk");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDialog.show();

        pengadaan.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(response.body().getPengadaan().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Belum ada transaksi pengadaan produk",Toast.LENGTH_SHORT).show();
                }
                listPengadaaan.addAll(response.body().getPengadaan());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("status","pengadaan" );
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("status","pengadaan" );
        startActivity(i);
    }
}
