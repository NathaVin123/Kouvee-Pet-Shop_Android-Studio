package com.petshopfix.Activity.Jenis;

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
import com.petshopfix.API.Interface.ApiJenis;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Jenis.ListJenis;

import retrofit2.Call;
import retrofit2.Callback;

public class CreateJenis extends AppCompatActivity {

    private TextInputLayout txtNama;
    private Button btnSimpan;
    private DatabaseHandler db;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jenis);

        setAtribut();
        init();
    }

    private void setAtribut() {
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaUkuran);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(CreateJenis.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("CREATE JENIS HEWAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaJ, updateLogIdJ;

                updateLogIdJ = db.getUser(1).getNIP();
                namaJ = txtNama.getEditText().getText().toString();

                if (namaJ.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Data Nama tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }else{
                    ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
                    Call<Response> jenis = apiService.createJenis(namaJ,updateLogIdJ);

                    progressDialog.setMessage("Loading...");
                    progressDialog.setTitle("Menambahkan Data Ukuran");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it

                    progressDialog.show();
                    jenis.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200)
                            {
                                if(response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Tambah Data Jenis Berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListJenis.class);
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
        startActivity(new Intent(getApplicationContext(), MenuJenis.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuJenis.class));
    }
}
