package com.petshopfix.View.PenjualanProduk.TambahTransaksi;

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
import com.petshopfix.View.PenjualanProduk.CartTransaksi.CartProdukShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TambahProdukPenjualanShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TambahProdukPenjualanAdapter adapter;
    private List<ProdukDAO> listproduk;
    private String no_transaksi, status, nama_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk_penjualan_show);

        setAdapter();
    }

    private void setAdapter() {
        no_transaksi = getIntent().getStringExtra("no_transaksi");
        status = getIntent().getStringExtra("status");
        nama_customer = getIntent().getStringExtra("nama_customer");

        listproduk = new ArrayList<ProdukDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new TambahProdukPenjualanAdapter(this, listproduk, no_transaksi, status, nama_customer);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(TambahProdukPenjualanShow.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DAFTAR PRODUK");

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

                if (berdasarkan!=null)
                {
                    ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
                    Call<Response> produks = apiService.sortir(berdasarkan);

                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(TambahProdukPenjualanShow.this);
                    progressDialog.setMessage("loading....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDialog.show();

                    produks.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (!response.body().getProduk().isEmpty())
                            {
                                listproduk.clear();
                                listproduk.addAll(response.body().getProduk());
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

    public void setProduk()
    {
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
                if (!response.body().getProduk().isEmpty())
                {
                    listproduk.addAll(response.body().getProduk());
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
        if (status.equals("tambah") || status.equals("detail"))
        {
            Intent i = new Intent(TambahProdukPenjualanShow.this, CartProdukShow.class);
            i.putExtra("no_transaksi", no_transaksi);
            i.putExtra("status", status);
            i.putExtra("nama_customer", nama_customer);
            startActivity(i);
        }
    }
}
