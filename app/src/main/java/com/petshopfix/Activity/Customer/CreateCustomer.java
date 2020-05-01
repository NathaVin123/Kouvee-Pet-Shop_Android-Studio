package com.petshopfix.Activity.Customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Customer.ListCustomer;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateCustomer extends AppCompatActivity {

    private TextInputLayout txtNama, txtAlamat, txtTglLahir, txtTelp;
//    DatePickerDialog.OnDateSetListener setListener;
    private Button btnSimpan;
    private DatabaseHandler db;
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);

        setAtribut() ;
        init() ;
    }

    private void setAtribut() {
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaCustomer);
        txtAlamat = (TextInputLayout) findViewById(R.id.txtAlamatCustomer);
        txtTglLahir = (TextInputLayout) findViewById(R.id.txtTanggalLahirCustomer);
        txtTelp = (TextInputLayout) findViewById(R.id.txtTelpCustomer);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(CreateCustomer.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Create Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaC, alamatC, tglLahirC, noTelpC, updateLogIdC;

                updateLogIdC = db.getUser(1).getNIP();
                namaC = txtNama.getEditText().getText().toString();
                alamatC = txtAlamat.getEditText().getText().toString();
                tglLahirC = txtTglLahir.getEditText().getText().toString();
                noTelpC = txtTelp.getEditText().getText().toString();

                if (namaC.isEmpty() || alamatC.isEmpty() || tglLahirC.isEmpty() || noTelpC.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
                    Call<Response> customer = apiService.createCustomer(namaC, alamatC, tglLahirC, noTelpC, updateLogIdC);

                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Tambah Data Customer");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    customer.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                if (response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Data Customer Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListCustomer.class);
                                    i.putExtra("status", "getAll");
                                    startActivity(i);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
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
