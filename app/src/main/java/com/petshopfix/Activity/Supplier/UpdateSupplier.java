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
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Supplier.ListSupplier;

import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateSupplier extends AppCompatActivity {

    private TextView txtID;
    private TextInputLayout txtNama, txtAlamat, txtTelepon, txtStok;
    private Button btnSimpan;
    private DatabaseHandler db;
    private Bundle supplier;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_supplier);

        try {
            setAtribut();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        init();
    }

    private void setAtribut() throws MalformedURLException {
        txtID = (TextView) findViewById(R.id.dataIDSupplier);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaSupplier);
        txtAlamat = (TextInputLayout) findViewById(R.id.txtAlamatSupplier);
        txtTelepon = (TextInputLayout) findViewById(R.id.txtTelpSupplier);
        txtStok = (TextInputLayout) findViewById(R.id.txtStokSupplier);
        btnSimpan = (Button) findViewById(R.id.btnSave);
        supplier = getIntent().getExtras();

        txtID.setText(String.valueOf(supplier.getInt("id_supplier")));
        txtNama.getEditText().setText(supplier.getString("nama_supplier"));
        txtAlamat.getEditText().setText(supplier.getString("alamat_supplier"));
        txtTelepon.getEditText().setText(supplier.getString("noTelp_supplier"));
        txtStok.getEditText().setText(String.valueOf(supplier.getInt("stok")));

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateSupplier.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Update Supplier");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaS, alamatS, teleponS,  updateLogIdS;
                int id_supplier, stokS;

                id_supplier = Integer.parseInt(txtID.getText().toString());
                namaS = txtNama.getEditText().getText().toString();
                alamatS = txtAlamat.getEditText().getText().toString();
                teleponS = txtTelepon.getEditText().getText().toString();
                stokS = Integer.parseInt(txtStok.getEditText().getText().toString());
                updateLogIdS = db.getUser(1).getNIP();

                if (namaS.isEmpty() || alamatS.isEmpty() || teleponS.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else if (stokS < 1)
                {
                    Toast.makeText(getApplicationContext(), "Data Stok Tidak Boleh Kurang dari 1 atau Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody nama_supplier =
                            RequestBody.create(MediaType.parse("multipart/form-data"), namaS);
                    RequestBody alamat_supplier =
                            RequestBody.create(MediaType.parse("multipart/form-data"), alamatS);
                    RequestBody noTelp_supplier =
                            RequestBody.create(MediaType.parse("multipart/form-data"), teleponS);
                    RequestBody stok =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(stokS));
                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLogIdS);

                    ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
                    Call<Response> supplier = apiService.updateSupplier(id_supplier, nama_supplier, alamat_supplier, noTelp_supplier, stok, updateLogId);

                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Mengubah Data Supplier");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    supplier.enqueue(new Callback<Response>() {
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
                                    Toast.makeText(getApplicationContext(), "Update Data Supplier Berhasil", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(UpdateSupplier.this, DetailSupplier.class);
        i.putExtras(supplier);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateSupplier.this, DetailSupplier.class);
        i.putExtras(supplier);
        startActivity(i);
    }
}
