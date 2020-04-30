package com.petshopfix.Activity.Ukuran;

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
import com.petshopfix.API.Interface.ApiUkuran;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Ukuran.ListUkuran;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateUkuran extends AppCompatActivity {

    private TextInputLayout txtNama;
    private Button btnSimpan;
    private DatabaseHandler db;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ukuran);

        setAtribut();
        init();
    }

    private void setAtribut() {
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaUkuran);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(CreateUkuran.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("CREATE UKURAN HEWAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaU, updateLogIdU;

                updateLogIdU = db.getUser(1).getNIP();
                namaU = txtNama.getEditText().getText().toString();

                if (namaU.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Data Nama tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }else {
                    ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
                    Call<Response> ukuran = apiService.createUkuran(namaU,updateLogIdU);

                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Menambahkan Data Ukuran");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it

                    progressDialog.show();
                    ukuran.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200)
                            {
                                if(response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Tambah Data Ukuran Berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListUkuran.class);
                                    i.putExtra("status","getAll" );
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
        startActivity(new Intent(getApplicationContext(), MenuUkuran.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuUkuran.class));
    }
}
