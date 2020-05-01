package com.petshopfix.Activity.Hewan;

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
import com.petshopfix.API.Interface.ApiHewan;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.View.Hewan.ListHewan;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailHewan extends AppCompatActivity {

    private TextView dataId, dataNama, dataLahir, dataJenis, dataCustomer, dataDiBuat, dataDiEdit, dataDiHapus, dataNIP;
    private Button btnUbah, btnHapus, btnHapusPermanen, btnRestore;
    private Bundle hewan;
    private String status;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hewan);

        setAtribut();
        init();
    }

    private void setAtribut() {
        dataId = (TextView) findViewById(R.id.dataIDHewan);
        dataNama = (TextView) findViewById(R.id.txtNamaHewan);
        dataLahir = (TextView) findViewById(R.id.txtTanggalLahirHewan);
        dataJenis = (TextView) findViewById(R.id.txtNamaJenis);
        dataCustomer = (TextView) findViewById(R.id.txtNamaCustomer);
        dataDiBuat = (TextView) findViewById(R.id.dataDibuat);
        dataDiEdit = (TextView) findViewById(R.id.dataDiedit);
        dataDiHapus = (TextView) findViewById(R.id.dataDihapus);
        dataNIP = (TextView) findViewById(R.id.dataNIP);
        btnUbah = (Button) findViewById(R.id.btnUpdate);
        btnHapus = (Button) findViewById(R.id.btnSoftDelete);
        btnRestore = (Button) findViewById(R.id.btnRestore);
        btnHapusPermanen = (Button) findViewById(R.id.btnHapusPermanen);

        hewan = getIntent().getExtras();
        dataId.setText(String.valueOf(hewan.getInt("id_hewan")));
        dataNama.setText(hewan.getString("nama_hewan"));
        dataLahir.setText(hewan.getString("tglLahir_hewan"));
        dataJenis.setText(hewan.getString("nama_ukuran"));
        dataCustomer.setText(hewan.getString("nama_customer"));
        dataDiBuat.setText(hewan.getString("createLog_at"));
        dataDiEdit.setText(hewan.getString("updateLog_at"));
        dataDiHapus.setText(hewan.getString("deleteLog_at"));
        dataNIP.setText(hewan.getString("updateLogId"));
        status = hewan.getString("status");

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
        judul.setText("Detail Hewan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        int id_hewan = hewan.getInt("id_hewan");
        String NIPLog = hewan.getString("updateLogId");

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateHewan.class);
                i.putExtras(hewan);
                startActivity(i);
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
                Call<Response> hewan = apiService.restoreHewan(id_hewan, NIPLog);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(DetailHewan.this);
                progressDialog.setMessage("Loading....");
                progressDialog.setTitle("Memulihkan Data Hewan");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // show it
                progressDialog.show();
                hewan.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            Toast.makeText(getApplicationContext(), "Data Hewan Berhasil Dipulihkan", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListHewan.class);
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
                builder.setMessage("Anda Yakin Ingin Menghapus Hewan ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
                        Call<Response> hewan = apiService.deleteHewan(id_hewan, NIPLog);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailHewan.this);
                        progressDialog.setMessage("loading....");
                        progressDialog.setTitle("Menghapus Data Hewan");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // show it
                        progressDialog.show();
                        hewan.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "Data Hewan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListHewan.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Data Hewan Ini Tidak Dapat Dihapus.", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Anda Yakin Ingin Menghapus Hewan Secara Permanen ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
                        Call<Response> hewan = apiService.deleteHewanPermanen(id_hewan);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailHewan.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data Hewan Permanen");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        // show it
                        progressDialog.show();
                        hewan.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(getApplicationContext(), "Data Hewan Berhasil Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListHewan.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Data Hewan Ini Tidak Dapat Dihapus Permanen.", Toast.LENGTH_SHORT).show();
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
        Intent i = new Intent(getApplicationContext(), ListHewan.class);
        i.putExtra("status",status );
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), ListHewan.class);
        i.putExtra("status",status );
        startActivity(i);
    }
}
