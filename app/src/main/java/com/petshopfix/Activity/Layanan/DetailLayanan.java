package com.petshopfix.Activity.Layanan;

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
import com.petshopfix.API.Interface.ApiLayanan;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.View.Layanan.ListLayanan;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailLayanan extends AppCompatActivity {

    private TextView dataId, dataNama, dataHarga, dataUkuran, dataDiBuat, dataDiEdit, dataDiHapus, dataNIP;
    private Button btnUbah, btnHapus, btnHapusPermanen, btnRestore;
    private Bundle layanan;
    private String status;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layanan);
        setAtribut();
        init();
    }

    private void setAtribut() {
        dataId = (TextView) findViewById(R.id.dataIDLayanan);
        dataNama = (TextView) findViewById(R.id.txtNamaLayanan);
        dataHarga = (TextView) findViewById(R.id.txtHargaLayanan);
        dataUkuran = (TextView) findViewById(R.id.txtNamaUkuran);
        dataDiBuat = (TextView) findViewById(R.id.dataDibuat);
        dataDiEdit = (TextView) findViewById(R.id.dataDiedit);
        dataDiHapus = (TextView) findViewById(R.id.dataDihapus);
        dataNIP = (TextView) findViewById(R.id.dataNIP);
        btnUbah = (Button) findViewById(R.id.btnUpdate);
        btnHapus = (Button) findViewById(R.id.btnSoftDelete);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        btnHapusPermanen = (Button) findViewById(R.id.btnHapusPermanen);

        layanan = getIntent().getExtras();
        dataId.setText(layanan.getString("id_layanan"));
        dataNama.setText(layanan.getString("nama_layanan"));
        dataHarga.setText("Rp " + String.valueOf(layanan.getDouble("harga_layanan")) + "0");
        dataUkuran.setText(layanan.getString("nama_ukuran"));
        dataDiBuat.setText(layanan.getString("createLog_at"));
        dataDiEdit.setText(layanan.getString("updateLog_at"));
        dataDiHapus.setText(layanan.getString("deleteLog_at"));
        dataNIP.setText(layanan.getString("updateLogId"));
        status = layanan.getString("status");

        if (status.equals("getAll")) {
            dataDiHapus.setText("-");
            btnHapusPermanen.setVisibility(View.INVISIBLE);
            btnRestore.setVisibility(View.INVISIBLE);
        } else {
            btnHapus.setVisibility(View.INVISIBLE);
            btnUbah.setVisibility(View.INVISIBLE);
        }
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Detail com.petshopfix.Activity.Penjualan.Layanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        String id_layanan = layanan.getString("id_layanan");
        String NIPLog = layanan.getString("updateLogId");

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateLayanan.class);
                i.putExtras(layanan);
                startActivity(i);
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiLayanan apiService = ApiClient.getClient().create(ApiLayanan.class);
                Call<Response> layanan = apiService.restoreLayanan(id_layanan, NIPLog);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(DetailLayanan.this);
                progressDialog.setMessage("Loading....");
                progressDialog.setTitle("Memulihkan Data com.petshopfix.Activity.Penjualan.Layanan");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // show it
                progressDialog.show();
                layanan.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            Toast.makeText(getApplicationContext(), "Data com.petshopfix.Activity.Penjualan.Layanan Berhasil Dipulihkan", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListLayanan.class);
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
                builder.setMessage("Anda Yakin Ingin Menghapus com.petshopfix.Activity.Penjualan.Layanan ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiLayanan apiService = ApiClient.getClient().create(ApiLayanan.class);
                        Call<Response> layanan = apiService.deleteLayanan(id_layanan, NIPLog);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailLayanan.this);
                        progressDialog.setMessage("loading....");
                        progressDialog.setTitle("Menghapus Data com.petshopfix.Activity.Penjualan.Layanan");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // show it
                        progressDialog.show();
                        layanan.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "Data com.petshopfix.Activity.Penjualan.Layanan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListLayanan.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Data com.petshopfix.Activity.Penjualan.Layanan Ini Tidak Dapat Dihapus.", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Anda Yakin Ingin Menghapus com.petshopfix.Activity.Penjualan.Layanan Secara Permanen ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiLayanan apiService = ApiClient.getClient().create(ApiLayanan.class);
                        Call<Response> layanan = apiService.deleteLayananPermanen(id_layanan);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailLayanan.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data com.petshopfix.Activity.Penjualan.Layanan Permanen");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // show it
                        progressDialog.show();
                        layanan.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "Data com.petshopfix.Activity.Penjualan.Layanan Berhasil Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListLayanan.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Data com.petshopfix.Activity.Penjualan.Layanan Ini Tidak Dapat Dihapus Permanen.", Toast.LENGTH_SHORT).show();
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
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(getApplicationContext(), ListLayanan.class);
        i.putExtra("status",status );
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ListLayanan.class);
        i.putExtra("status",status );
        startActivity(i);
    }
}
