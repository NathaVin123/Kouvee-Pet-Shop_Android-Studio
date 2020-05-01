package com.petshopfix.Activity.Customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Layanan.DetailLayanan;
import com.petshopfix.R;
import com.petshopfix.View.Customer.ListCustomer;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailCustomer extends AppCompatActivity {

    private TextView dataID, dataNama, dataAlamat, dataTglLahir, dataNoTelp,  dataDibuat, dataDiedit, dataDihapus, dataNIP;
    private Button btnUbah, btnHapus, btnHapusPermanen, btnRestore;
    private Bundle customer;
    private String status;
    private AlertDialog.Builder builder;

//    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);

        setAtribut();
        init();
    }

    private void setAtribut() {
        dataID = (TextView) findViewById(R.id.dataIDCustomer);
        dataNama = (TextView) findViewById(R.id.txtNamaCustomer);
        dataAlamat = (TextView) findViewById(R.id.txtAlamatCustomer);
        dataTglLahir = (TextView) findViewById(R.id.txtTanggalLahirCustomer);
        dataNoTelp = (TextView) findViewById(R.id.txtTelpCustomer);
        dataDibuat = (TextView) findViewById(R.id.dataDibuat);
        dataDiedit = (TextView) findViewById(R.id.dataDiedit);
        dataDihapus = (TextView) findViewById(R.id.dataDihapus);
        dataNIP = (TextView) findViewById(R.id.dataNIP);

        btnUbah = (Button) findViewById(R.id.btnUpdate);
        btnHapus = (Button) findViewById(R.id.btnSoftDelete);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        btnHapusPermanen = (Button) findViewById(R.id.btnHapusPermanen);

        customer = getIntent().getExtras();
        dataID.setText(String.valueOf(customer.getInt("id_customer")));
        dataNama.setText(customer.getString("nama_customer"));
        dataAlamat.setText(customer.getString("alamat_customer"));
        dataTglLahir.setText(customer.getString("tglLahir_customer"));
        dataNoTelp.setText(customer.getString("noTelp_customer"));
        dataDibuat.setText(customer.getString("createLog_at"));
        dataDiedit.setText(customer.getString("updateLog_at"));
        dataDihapus.setText(customer.getString("deleteLog_at"));
        dataNIP.setText(customer.getString("updateLogId"));
        status = customer.getString("status");

        if(status.equals("getAll")){
            dataDihapus.setText("-");
            btnHapusPermanen.setVisibility(View.INVISIBLE);;
            btnRestore.setVisibility(View.INVISIBLE);
        }else{
            btnHapus.setVisibility(View.INVISIBLE);
            btnUbah.setVisibility(View.INVISIBLE);
        }

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Detail Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        int id_customer = customer.getInt("id_customer");
        String NIPLog = customer.getString("updateLogId");

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateCustomer.class);
                i.putExtras(customer);
                startActivity(i);
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
                Call<Response> customer = apiService.restoreCustomer(id_customer,NIPLog);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(DetailCustomer.this);
                progressDialog.setMessage("Loading....");
                progressDialog.setTitle("Menghapus Data Customer");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Show it
                progressDialog.show();
                customer.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Data Customer Berhasil Dipulihkan", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListCustomer.class);
                            i.putExtra("status", status);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Anda Yakin Ingin Menghapus Customer ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
                        Call<Response> customer = apiService.deleteCustomer(id_customer, NIPLog);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailCustomer.this);
                        progressDialog.setMessage("loading....");
                        progressDialog.setTitle("Menghapus Data Customer");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // show it
                        progressDialog.show();
                        customer.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "Data Customer Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListCustomer.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Data Customer Ini Tidak Dapat Dihapus.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnHapusPermanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Anda Yakin ingin Menghapus Customer Secara Permanen ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
                        Call<Response> customer = apiService.deleteCustomerPermanen(id_customer);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailCustomer.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data Customer Permanen");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        //Show it
                        progressDialog.show();
                        customer.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(), "Data Customer Berhasil Dihapus Permanen", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListCustomer.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Data Customer ini tidak dapat Dihapus Permanen", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(), ListCustomer.class);
        i.putExtra("status",status );
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ListCustomer.class);
        i.putExtra("status",status );
        startActivity(i);
    }
}
