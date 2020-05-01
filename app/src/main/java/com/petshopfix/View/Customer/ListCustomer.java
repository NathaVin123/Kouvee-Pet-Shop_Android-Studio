package com.petshopfix.View.Customer;

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
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Customer.MenuCustomer;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ListCustomer extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<CustomerDAO> listCustomer;
    private String status;
    private TextView judul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        setAdapter();
        setCustomer();
    }

    private void setAdapter() {
        status = getIntent().getStringExtra("status");
        listCustomer = new ArrayList<CustomerDAO>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new CustomerAdapter(this, listCustomer, status);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setCustomer() {
        if (status.equals("getAll"))
        {
            judul.setText("Daftar Customer");
            ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
            Call<Response> customers = apiService.getAll();

            response(customers);
        }
        else
        {
            judul.setText("Daftar Customer dihapus");
            ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
            Call<Response> customers = apiService.getSoftDelete();

            response(customers);
        }
    }

    private void response(Call<Response> customers) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        if (status.equals("getAll"))
            progressDialog.setTitle("Menampilkan Daftar Customer");
        else
            progressDialog.setTitle("Menampilkan Daftar Customer Dihapus");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();
        customers.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getCustomer().isEmpty())
                {
                    if (status.equals("getAll"))
                        Toast.makeText(getApplicationContext(), "Tidak ada Customer", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Tidak ada Customer yang dihapus", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listCustomer.addAll(response.body().getCustomer());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
        startActivity(new Intent(getApplicationContext(), MenuCustomer.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuCustomer.class));
    }
}
