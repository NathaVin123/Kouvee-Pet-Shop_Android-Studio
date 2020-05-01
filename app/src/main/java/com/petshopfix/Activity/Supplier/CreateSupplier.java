package com.petshopfix.Activity.Supplier;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiSupplier;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Supplier.ListSupplier;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateSupplier extends AppCompatActivity {

    private TextInputLayout txtNama, txtAlamat, txtNoTelp, txtStok;
    private Button btnSimpan;
    private DatabaseHandler db;
    private ProgressDialog progressDialog = null;
    private List<SupplierDAO> listSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_supplier);

        setAtribut();
        init();
    }

    private void setAtribut() {
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaSupplier);
        txtAlamat = (TextInputLayout) findViewById(R.id.txtAlamatSupplier);
        txtNoTelp = (TextInputLayout) findViewById(R.id.txtTelpSupplier);
        txtStok = (TextInputLayout) findViewById(R.id.txtStokSupplier);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(CreateSupplier.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Create Supplier");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaS, alamatS, noTelpS, updateLogIdS;
                int stokS;

                updateLogIdS = db.getUser(1).getNIP();
                namaS = txtNama.getEditText().getText().toString();
                alamatS = txtAlamat.getEditText().getText().toString();
                noTelpS = txtNoTelp.getEditText().getText().toString();
                stokS = Integer.parseInt(txtStok.getEditText().getText().toString());

                if (namaS.isEmpty() || alamatS.isEmpty() || noTelpS.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else if (stokS < 1)
                {
                    Toast.makeText(getApplicationContext(), "Stok Tidak boleh kosong atau lebih kecil dari 1", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
                    Call<Response> supplier = apiService.createSupplier(namaS, alamatS, noTelpS, stokS, updateLogIdS);

                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Tambah Data Supplier");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    supplier.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                if (response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Data Supplier Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListSupplier.class);
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
        startActivity(new Intent(getApplicationContext(), MenuSupplier.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuSupplier.class));
    }
}
