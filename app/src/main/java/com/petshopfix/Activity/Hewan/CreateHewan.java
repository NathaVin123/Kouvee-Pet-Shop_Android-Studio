package com.petshopfix.Activity.Hewan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.textfield.TextInputLayout;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Interface.ApiHewan;
import com.petshopfix.API.Interface.ApiJenis;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.DAO.JenisDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Hewan.ListHewan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateHewan extends AppCompatActivity {

    private TextInputLayout txtNamaHewan, txtTglLahirHewan;
    private Button btnSimpan;
    private Spinner dataJenis, dataCustomer;
    private DatabaseHandler db;
    private ProgressDialog progressDialog = null;
    private List<JenisDAO> listJenis;
    private List<CustomerDAO> listCustomer;
    private int selectJenis;
    private int selectCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hewan);

        setAtribut();
        init();
        setDaftarJenis();
        setDaftarCustomer();
    }

    private void setAtribut() {
        txtNamaHewan = (TextInputLayout) findViewById(R.id.txtNamaHewan);
        txtTglLahirHewan = (TextInputLayout) findViewById(R.id.txtTanggalLahirHewan);
        dataJenis = (Spinner) findViewById(R.id.dataJenis);
        dataCustomer = (Spinner) findViewById(R.id.dataCustomer);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        listJenis = new ArrayList<>();
        listCustomer = new ArrayList<>();

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(CreateHewan.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Create Hewan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDaftarJenis() {
        ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
        Call<Response> jeniss = apiService.getAll();

        jeniss.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body().getJenis().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Jenis Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listJenis.addAll(response.body().getJenis());
                    List<String> daftarNamaJenis = new ArrayList<>();
                    for (int i=0; i<listJenis.size(); i++)
                    {
                        daftarNamaJenis.add(listJenis.get(i).getNama_jenis());
                        System.out.println(listJenis.get(i).getNama_jenis());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, daftarNamaJenis);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataJenis.setAdapter(adapter);

                    dataJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i=0; i<listJenis.size(); i++)
                            {
                                if (dataJenis.getSelectedItem().toString().equals(listJenis.get(i).getNama_jenis()))
                                {
                                    selectJenis = listJenis.get(i).getId_jenis();
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
            public void onFailure(Call<Response> call, Throwable t) {
                System.out.println(t.getCause());
            }
        });
    }

    private void setDaftarCustomer() {
        ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
        Call<Response> customers = apiService.getAll();

        customers.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body().getCustomer().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Customer Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listCustomer.addAll(response.body().getCustomer());
                    List<String> daftarNamaCustomer = new ArrayList<>();
                    for (int i=0; i<listCustomer.size(); i++)
                    {
                        daftarNamaCustomer.add(listCustomer.get(i).getNama_customer());
                        System.out.println(listCustomer.get(i).getNama_customer());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, daftarNamaCustomer);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataCustomer.setAdapter(adapter);

                    dataCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i=0; i<listCustomer.size(); i++)
                            {
                                if (dataCustomer.getSelectedItem().toString().equals(listCustomer.get(i).getNama_customer()))
                                {
                                    selectCustomer = listCustomer.get(i).getId_customer();
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
            public void onFailure(Call<Response> call, Throwable t) {
                System.out.println(t.getCause());
            }
        });
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaH, tglLahirH, updateLogIdH;
//                double hargaL;
                int id_jenisH, id_customerH;

                updateLogIdH = db.getUser(1).getNIP();
                namaH = txtNamaHewan.getEditText().getText().toString();
                tglLahirH = txtTglLahirHewan.getEditText().getText().toString();
                id_jenisH = selectJenis;
                id_customerH = selectCustomer;

                if (namaH.isEmpty() || tglLahirH.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
                    Call<Response> hewan = apiService.createHewan(namaH, tglLahirH, id_jenisH, id_customerH, updateLogIdH);

                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Tambah Data Hewan");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    hewan.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                if (response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Data Hewan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListHewan.class);
                                    i.putExtra("status", "getAll");
                                    startActivity(i);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
