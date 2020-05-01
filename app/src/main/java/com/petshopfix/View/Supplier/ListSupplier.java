package com.petshopfix.View.Supplier;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiSupplier;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Supplier.MenuSupplier;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ListSupplier extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private SupplierAdapter adapter;
    private List<SupplierDAO> listSupplier;
    private String status;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_supplier);

        setAdapter();
        setSupplier();
    }

    private void setAdapter() {
        status = getIntent().getStringExtra("status");
        listSupplier = new ArrayList<SupplierDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new SupplierAdapter(this, listSupplier, status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setSupplier() {
        if(status.equals("getAll"))
        {
            judul.setText("Daftar Supplier");
            ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
            Call<Response> suppliers = apiService.getAll();

            response(suppliers);
        }
        else
        {
            judul.setText("Daftar Supplier Di Hapus");
            ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
            Call<Response> suppliers = apiService.getSoftDelete();

            response(suppliers);
        }
    }

    private void response(Call<Response> suppliers) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        if(status.equals("getAll"))
            progressDialog.setTitle("Menampilkan Daftar Supplier");
        else
            progressDialog.setTitle("Menampilkan Daftar Supplier Dihapus");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
        suppliers.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(response.body().getSupplier().isEmpty())
                {
                    if(status.equals("getAll"))
                        Toast.makeText(getApplicationContext(), "Tidak ada supplier",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Tidak ada supplier yang dihapus",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listSupplier.addAll(response.body().getSupplier());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);

        MenuItem menuItem = menu.findItem(R.id.txtSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String userInput = query.toLowerCase();
        adapter.getFilter().filter(userInput);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        adapter.getFilter().filter(userInput);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), MenuSupplier.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuSupplier.class));
    }
}
