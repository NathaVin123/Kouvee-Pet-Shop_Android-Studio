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

import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateJenis extends AppCompatActivity {

    private TextView txtID;
    private TextInputLayout txtNama;
    private Button btnSimpan;
    private DatabaseHandler db;
    private Bundle jenis;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jenis);

        try {
            setAtribut();
        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }
        init();
    }

    private void setAtribut() throws MalformedURLException {
        txtID = (TextView) findViewById(R.id.dataIDJenis);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaJenis);
        btnSimpan = (Button) findViewById(R.id.btnSave);
        jenis = getIntent().getExtras();

        txtID.setText(String.valueOf(jenis.getInt("id_jenis")));
        txtNama.getEditText().setText(jenis.getString("nama_jenis"));

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateJenis.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Update Jenis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaJ, updateLogIdU;
                int id_hewan;

                id_hewan = Integer.parseInt(txtID.getText().toString());
                namaJ = txtNama.getEditText().getText().toString();
                updateLogIdU = db.getUser(1).getNIP();

                if (namaJ.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody nama_jenis =
                            RequestBody.create(MediaType.parse("multipart/form-data"), namaJ);
                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLogIdU);

                    ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
                    Call<Response> jenis = apiService.updateJenis(id_hewan, nama_jenis, updateLogId);

                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Mengubah Data Jenis");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    jenis.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            System.out.println(response.code());
                            if (response.code() == 200)
                            {
                                if (response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Update Data Jenis Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListJenis.class);
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
        Intent i = new Intent(UpdateJenis.this, ListJenis.class);
        i.putExtras(jenis);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateJenis.this, ListJenis.class);
        i.putExtras(jenis);
        startActivity(i);
    }
}
