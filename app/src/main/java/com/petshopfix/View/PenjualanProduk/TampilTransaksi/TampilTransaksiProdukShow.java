package com.petshopfix.View.PenjualanProduk.TampilTransaksi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiTransaksiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.HomeActivity;
import com.petshopfix.DAO.TransaksiProdukDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TampilTransaksiProdukShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TampilTransaksiProdukAdapter adapter;
    private List<TransaksiProdukDAO> listTransaksiProduk;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_transaksi_produk_show);

        setAdapter();
        setTransaksiProduk();
    }

    private void setAdapter() {
        listTransaksiProduk = new ArrayList<TransaksiProdukDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new TampilTransaksiProdukAdapter(this, listTransaksiProduk);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));

        EditText searchView = (EditText) findViewById(R.id.txtSearch);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setTransaksiProduk() {
        judul.setText("Daftar Transaksi Produk");

        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<Response> transaksiProduk = apiService.getAll();

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Menampilkan Daftar Transaksi Produk");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        transaksiProduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getTransaksiProduk().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Belum ada Transaksi Penjualan Produk", Toast.LENGTH_SHORT).show();
                }
                listTransaksiProduk.addAll(response.body().getTransaksiProduk());
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
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("status", "penjualan");
        startActivity(i);
    }
}
