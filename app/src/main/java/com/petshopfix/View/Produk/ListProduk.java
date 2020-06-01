package com.petshopfix.View.Produk;

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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.HomeActivity;
import com.petshopfix.Activity.Produk.MenuProduk;
import com.petshopfix.DAO.ProdukDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ListProduk extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private ProdukAdapter adapter;
    private List<ProdukDAO> listProduk;
    private String status;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        setAdapter();
        setProduk();
    }

    private void setAdapter() {
        status = getIntent().getStringExtra("status");
        listProduk = new ArrayList<ProdukDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new ProdukAdapter(this, listProduk, status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    public void setProduk() {
        if (status.equals("getAll"))
        {
            judul.setText("Daftar Produk");
            ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
            Call<Response> produks = apiService.getAll();

            response(produks);
        }
        else if(status.equals("minimal"))
        {
            judul.setText("Daftar Stok Min Produk");
            ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
            Call<Response> produks = apiService.getMinimal();

            response(produks);
        }
        else
        {
            judul.setText("Daftar Produk Di Hapus");
            ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
            Call<Response> produks = apiService.getSoftDelete();

            response(produks);
        }
    }

    private void response(Call<Response> produks) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading....");
        if(status.equals("getAll"))
            progressDialog.setTitle("Menampilkan Daftar com.petshopfix.Activity.Penjualan.Produk");
        else
            progressDialog.setTitle("Menampilkan Daftar com.petshopfix.Activity.Penjualan.Produk Dihapus");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it

        progressDialog.show();
        produks.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                assert response.body() != null;
                if(response.body().getProduk().isEmpty())
                {
                    if(status.equals("getAll"))
                        Toast.makeText(getApplicationContext(), "Tidak ada produk",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Tidak ada produk yang dihapus",Toast.LENGTH_SHORT).show();
                }
                listProduk.addAll(response.body().getProduk());
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
        if (status.equals("minimal"))
        {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("status", "pengadaan" );
            startActivity(i);
        }
        else
            startActivity(new Intent(getApplicationContext(), MenuProduk.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        if (status.equals("minimal"))
        {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("status", "pengadaan" );
            startActivity(i);
        }
        else
            startActivity(new Intent(getApplicationContext(), MenuProduk.class));
    }
}
