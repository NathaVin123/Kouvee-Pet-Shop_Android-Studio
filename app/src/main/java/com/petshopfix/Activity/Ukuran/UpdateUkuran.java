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

import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateUkuran extends AppCompatActivity {

    private TextView txtID;
    private TextInputLayout txtNama;
    private Button btnSave;
    private DatabaseHandler db;
    private Bundle ukuran;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ukuran);
        try {
            setAtribut();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        init();
    }

    private void setAtribut() throws MalformedURLException {
        txtID = (TextView) findViewById(R.id.dataIDUkuran);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaUkuran);
        btnSave = (Button) findViewById(R.id.btnSave);
        ukuran = getIntent().getExtras();

        txtID.setText(String.valueOf(ukuran.getInt("id_ukuran")));
        txtNama.getEditText().setText(ukuran.getString("nama_ukuran"));

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateUkuran.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Update Ukuran Hewan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_ukuran;
                String namaU, updateLogIdU;

                id_ukuran = Integer.parseInt(txtID.getText().toString());
                namaU = txtNama.getEditText().getText().toString();
                updateLogIdU = db.getUser(1).getNIP();

                if (namaU.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody nama_ukuran =
                            RequestBody.create(MediaType.parse("multipart/form-data"), namaU);
                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLogIdU);

                    ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
                    Call<Response> ukuran = apiService.updateUkuran(id_ukuran, nama_ukuran, updateLogId);

                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Mengubah Data Ukuran.");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDialog.show();

                    ukuran.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            System.out.println(response.code());
                            if(response.code() == 200)
                            {
                                if(response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Update Data Ukuran Berhasil.", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(UpdateUkuran.this, ListUkuran.class);
        i.putExtras(ukuran);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateUkuran.this, ListUkuran.class);
        i.putExtras(ukuran);
        startActivity(i);
    }
}
