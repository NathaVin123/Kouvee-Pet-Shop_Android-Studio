package com.petshopfix.View.Hewan;

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
import com.petshopfix.API.Interface.ApiHewan;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Hewan.MenuHewan;
import com.petshopfix.DAO.HewanDAO;
import com.petshopfix.R;
import com.petshopfix.View.Ukuran.UkuranAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ListHewan extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private HewanAdapter adapter;
    private List<HewanDAO> listHewan;
    private String status;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hewan);

        setAdapter();
        setHewan();
    }

    private void setAdapter() {
        status = getIntent().getStringExtra("status");
        listHewan = new ArrayList<HewanDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new HewanAdapter(this, listHewan, status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setHewan() {
        if(status.equals("getAll"))
        {
            judul.setText("Daftar Hewan");
            ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
            Call<Response> hewans = apiService.getAll();

            response(hewans);
        }
        else
        {
            judul.setText("Daftar Hewan Di Hapus");
            ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
            Call<Response> hewans = apiService.getSoftDelete();

            response(hewans);
        }
    }

    private void response(Call<Response> hewans)
    {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        if(status.equals("getAll"))
            progressDialog.setTitle("Menampilkan Daftar Hewan");
        else
            progressDialog.setTitle("Menampilkan Daftar Hewan Dihapus");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
        hewans.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(response.body().getHewan().isEmpty())
                {
                    if(status.equals("getAll"))
                        Toast.makeText(getApplicationContext(), "Tidak ada hewan",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Tidak ada hewan yang dihapus",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listHewan.addAll(response.body().getHewan());
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
    public boolean onQueryTextChange(String newText)
    {
        String userInput = newText.toLowerCase();
        adapter.getFilter().filter(userInput);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), MenuHewan.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuHewan.class));
    }
}
