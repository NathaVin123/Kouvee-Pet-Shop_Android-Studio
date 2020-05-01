package com.petshopfix.Activity.Supplier;

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
import com.petshopfix.API.Interface.ApiSupplier;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.View.Supplier.ListSupplier;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailSupplier extends AppCompatActivity {

    private TextView dataID, dataNama, dataAlamat, dataNoTelp, dataStok,  dataDibuat, dataDiedit, dataDihapus, dataNIP;
    private Button btnUbah, btnHapus, btnHapusPermanen, btnRestore;
    private Bundle supplier;
    private String status;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_supplier);

        setAtribut();
        init();
    }

    private void setAtribut(){
        dataID = (TextView) findViewById(R.id.dataIDSupplier);
        dataNama = (TextView) findViewById(R.id.txtNamaSupplier);
        dataAlamat = (TextView) findViewById(R.id.txtAlamatSupplier);
        dataNoTelp = (TextView) findViewById(R.id.txtTelpSupplier);
        dataStok = (TextView) findViewById(R.id.txtStokSupplier);
        dataDibuat = (TextView) findViewById(R.id.dataDibuat);
        dataDiedit = (TextView) findViewById(R.id.dataDiedit);
        dataDihapus = (TextView) findViewById(R.id.dataDihapus);
        dataNIP = (TextView) findViewById(R.id.dataNIP);

        btnUbah = (Button) findViewById(R.id.btnUpdate);
        btnHapus = (Button) findViewById(R.id.btnSoftDelete);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        btnHapusPermanen = (Button) findViewById(R.id.btnHapusPermanen);

        supplier = getIntent().getExtras();
        dataID.setText(String.valueOf(supplier.getInt("id_supplier")));
        dataNama.setText(supplier.getString("nama_supplier"));
        dataAlamat.setText(supplier.getString("alamat_supplier"));
        dataNoTelp.setText(supplier.getString("noTelp_supplier"));
        dataStok.setText(String.valueOf(supplier.getInt("stok")));
        dataDibuat.setText(supplier.getString("createLog_at"));
        dataDiedit.setText(supplier.getString("updateLog_at"));
        dataDihapus.setText(supplier.getString("deleteLog_at"));
        dataNIP.setText(supplier.getString("updateLogId"));
        status = supplier.getString("status");

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
        judul.setText("Detail Supplier");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        int id_supplier = supplier.getInt("id_supplier");
        String NIPLog = supplier.getString("updateLogId");

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateSupplier.class);
                i.putExtras(supplier);
                startActivity(i);
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
                Call<Response> supplier = apiService.restoreSupplier(id_supplier,NIPLog);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(DetailSupplier.this);
                progressDialog.setMessage("Loading....");
                progressDialog.setTitle("Menghapus Data Supplier");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Show it
                progressDialog.show();
                supplier.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Data Supplier Berhasil Dipulihkan", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListSupplier.class);
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
                builder.setMessage("Anda Yakin Ingin Menghapus Supplier ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
                        Call<Response> supplier = apiService.deleteSupplier(id_supplier,NIPLog);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailSupplier.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data Supplier");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        //Show it
                        progressDialog.show();
                        supplier.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(), "Data Supplier Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListSupplier.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Data Supplier ini tidak dapat Dihapus", Toast.LENGTH_SHORT).show();
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
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnHapusPermanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Anda Yakin ingin Menghapus Supplier Secara Permanen ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiSupplier apiService = ApiClient.getClient().create(ApiSupplier.class);
                        Call<Response> supplier = apiService.deleteSupplierPermanen(id_supplier);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailSupplier.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data Supplier Permanen");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        //Show it
                        progressDialog.show();
                        supplier.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(), "Data Supplier Berhasil Dihapus Permanen", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListSupplier.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Data Supplier ini tidak dapat Dihapus Permanen", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(getApplicationContext(), ListSupplier.class);
        i.putExtra("status",status );
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ListSupplier.class);
        i.putExtra("status",status );
        startActivity(i);
    }
}
