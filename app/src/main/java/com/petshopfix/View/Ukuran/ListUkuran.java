package com.petshopfix.View.Ukuran;

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
import com.petshopfix.API.Interface.ApiUkuran;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Ukuran.MenuUkuran;
import com.petshopfix.DAO.UkuranDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ListUkuran extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private UkuranAdapter adapter;
    private List<UkuranDAO> listUkuran;
    private String status;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ukuran);

        setAdapter();
        setUkuran();
    }

    private void setAdapter() {
        status = getIntent().getStringExtra("status");
        listUkuran = new ArrayList<UkuranDAO>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new UkuranAdapter(this, listUkuran, status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul = (TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUkuran()
    {
        if(status.equals("getAll"))
        {
            judul.setText("Daftar Ukuran");
            ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
            Call<Response> ukurans = apiService.getAll();

            response(ukurans);
        }
        else
        {
            judul.setText("Daftar Ukuran Di Hapus");
            ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
            Call<Response> ukurans = apiService.getSoftDelete();

            response(ukurans);
        }
    }

    private void response(Call<Response> ukurans) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");

        if(status.equals("getAll"))
            progressDialog.setTitle("Menampilkan Daftar Ukuran");
        else
            progressDialog.setTitle("Menampilkan Daftar Ukuran Dihapus");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDialog.show();

        ukurans.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                assert response.body() != null;
                if(response.body().getUkuran().isEmpty())
                {
                    if(status.equals("getAll"))
                        Toast.makeText(getApplicationContext(), "Tidak ada ukuran",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Tidak ada ukuran yang dihapus",Toast.LENGTH_SHORT).show();
                }
                listUkuran.addAll(response.body().getUkuran());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu,menu);

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
        startActivity(new Intent(getApplicationContext(), MenuUkuran.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuUkuran.class));
    }

}
