package com.petshopfix.Activity.Layanan;

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
import com.petshopfix.API.Interface.ApiLayanan;
import com.petshopfix.API.Interface.ApiUkuran;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.UkuranDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Layanan.ListLayanan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateLayanan extends AppCompatActivity {

    private TextInputLayout txtNamaLayanan, txtHargaLayanan;
    private Button btnSimpan;
    private Spinner dataUkuran;
    private DatabaseHandler db;
    private ProgressDialog progressDialog = null;
    private List<UkuranDAO> listUkuran;
    private int selectUkuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_layanan);

        setAtribut();
        init();
        setDaftarUkuran();
    }

    private void setAtribut() {
        txtNamaLayanan = (TextInputLayout) findViewById(R.id.txtNamaLayanan);
        txtHargaLayanan = (TextInputLayout) findViewById(R.id.txtHargaLayanan);
        dataUkuran = (Spinner) findViewById(R.id.dataUkuran);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        listUkuran = new ArrayList<>();

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(CreateLayanan.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Create Layanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDaftarUkuran()
    {
        ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
        Call<Response> ukurans = apiService.getAll();

        ukurans.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body().getUkuran().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Ukuran Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listUkuran.addAll(response.body().getUkuran());
                    List<String> daftarNamaUkuran = new ArrayList<>();
                    for (int i=0; i<listUkuran.size(); i++)
                    {
                        daftarNamaUkuran.add(listUkuran.get(i).getNama_ukuran());
                        System.out.println(listUkuran.get(i).getNama_ukuran());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, daftarNamaUkuran);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataUkuran.setAdapter(adapter);

                    dataUkuran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i=0; i<listUkuran.size(); i++)
                            {
                                if (dataUkuran.getSelectedItem().toString().equals(listUkuran.get(i).getNama_ukuran()))
                                {
                                    selectUkuran = listUkuran.get(i).getId_ukuran();
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
                String namaL, updateLogIdL;
                double hargaL;
                int id_ukuranL;

                updateLogIdL = db.getUser(1).getNIP();
                namaL = txtNamaLayanan.getEditText().getText().toString();
                hargaL = Double.parseDouble(txtHargaLayanan.getEditText().getText().toString());
                id_ukuranL = selectUkuran;

                if (namaL.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else if (hargaL<1)
                {
                    Toast.makeText(getApplicationContext(), "Harga Tidak boleh kosong atau lebih kecil dari 1", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApiLayanan apiService = ApiClient.getClient().create(ApiLayanan.class);
                    Call<Response> layanan = apiService.createLayanan(namaL, hargaL, id_ukuranL, updateLogIdL);

                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Tambah Data Layanan");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    layanan.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                if (response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Data Layanan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListLayanan.class);
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
        startActivity(new Intent(getApplicationContext(), MenuLayanan.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuLayanan.class));
    }
}
