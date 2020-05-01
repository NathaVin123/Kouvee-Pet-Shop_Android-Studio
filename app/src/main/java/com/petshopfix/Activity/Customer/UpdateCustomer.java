package com.petshopfix.Activity.Customer;

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
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Customer.ListCustomer;

import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateCustomer extends AppCompatActivity {

    private TextView txtID;
    private TextInputLayout txtNama,txtAlamat, txtTglLahir, txtTelepon;
    private Button btnSimpan;
    private DatabaseHandler db;
    private Bundle customer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);

        try {
            setAtribut();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        init();
    }

    private void setAtribut() throws MalformedURLException{
        txtID = (TextView) findViewById(R.id.dataIDCustomer);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaCustomer);
        txtAlamat = (TextInputLayout) findViewById(R.id.txtAlamatCustomer);
        txtTglLahir = (TextInputLayout) findViewById(R.id.txtTanggalLahirCustomer);
        txtTelepon = (TextInputLayout) findViewById(R.id.txtTelpCustomer);
        btnSimpan = (Button) findViewById(R.id.btnSave);
        customer = getIntent().getExtras();

        txtID.setText(String.valueOf(customer.getInt("id_customer")));
        txtNama.getEditText().setText(customer.getString("nama_customer"));
        txtAlamat.getEditText().setText(customer.getString("alamat_customer"));
        txtTglLahir.getEditText().setText(customer.getString("tglLahir_customer"));
        txtTelepon.getEditText().setText(customer.getString("noTelp_customer"));

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateCustomer.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Ubah Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id_customer;
                String NamaCust, AlamatCust, TglLahirCust, TeleponCust, updateLog_byCust;

                id_customer = Integer.parseInt(txtID.getText().toString());
                NamaCust = txtNama.getEditText().getText().toString();
                AlamatCust = txtAlamat.getEditText().getText().toString();
                TglLahirCust = txtTglLahir.getEditText().getText().toString();
                TeleponCust = txtTelepon.getEditText().getText().toString();
                updateLog_byCust = db.getUser(1).getNIP();

                if (NamaCust.isEmpty() || AlamatCust.isEmpty() || TglLahirCust.isEmpty() || TeleponCust.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody nama_customer =
                            RequestBody.create(MediaType.parse("multipart/form-data"), NamaCust);
                    RequestBody alamat_customer =
                            RequestBody.create(MediaType.parse("multipart/form-data"), AlamatCust);
                    RequestBody tglLahir_customer =
                            RequestBody.create(MediaType.parse("multipart/form-data"), TglLahirCust);
                    RequestBody noTelp_customer =
                            RequestBody.create(MediaType.parse("multipart/form-data"), TeleponCust);
                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLog_byCust);

                    ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
                    Call<Response> customer = apiService.updateCustomer(id_customer, nama_customer, alamat_customer, tglLahir_customer, noTelp_customer, updateLogId);

                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Mengubah Data Customer.");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDialog.show();
                    customer.enqueue(new Callback<Response>() {
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
                                    Toast.makeText(getApplicationContext(), "Update Data Customer Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListCustomer.class);
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
        Intent i = new Intent(UpdateCustomer.this, DetailCustomer.class);
        i.putExtras(customer);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateCustomer.this, DetailCustomer.class);
        i.putExtras(customer);
        startActivity(i);
    }
}
