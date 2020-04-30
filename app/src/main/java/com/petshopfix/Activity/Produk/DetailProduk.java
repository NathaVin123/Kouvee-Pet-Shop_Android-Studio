package com.petshopfix.Activity.Produk;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.View.Produk.ListProduk;
import com.petshopfix.ZoomImage;
import com.zolad.zoominimageview.ZoomInImageView;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailProduk extends AppCompatActivity {

    private TextView dataID, dataNama, dataHarga, dataStok, dataMinStok, dataSatuan, dataDibuat, dataDiedit, dataDihapus, dataNIP;
    private ZoomInImageView dataImage;
    private Button btnUpdate, btnDelete, btnDeletePermanen, btnRestore;
    private Bundle produk;
    private String status;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        setAtribut();
        init();
    }

    private void setAtribut() {
        dataID = (TextView) findViewById(R.id.dataIDProduk);
        dataNama = (TextView) findViewById(R.id.txtNamaProduk);
        dataHarga = (TextView) findViewById(R.id.txtHargaProduk);
        dataStok = (TextView) findViewById(R.id.txtStokProduk);
        dataMinStok = (TextView) findViewById(R.id.txtMinStokProduk);
        dataSatuan = (TextView) findViewById(R.id.txtSatuanProduk);
        dataDibuat = (TextView) findViewById(R.id.dataDibuat);
        dataDiedit = (TextView) findViewById(R.id.dataDiedit);
        dataDihapus = (TextView) findViewById(R.id.dataDihapus);
        dataNIP = (TextView) findViewById(R.id.dataNIP);

        dataImage = (ZoomInImageView) findViewById(R.id.dataGambarProduk);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnSoftDelete);
        btnDeletePermanen = (Button) findViewById(R.id.btnHapusPermanen);
        btnRestore = (Button) findViewById(R.id.btnRestore);

        produk = getIntent().getExtras();
        dataID.setText(produk.getString("id_produk"));
        dataNama.setText(produk.getString("nama_produk"));
        dataHarga.setText("Rp " + String.valueOf(produk.getDouble("harga_produk")) + "00");
        dataStok.setText(String.valueOf(produk.getInt("stok")));
        dataMinStok.setText(String.valueOf(produk.getInt("min_stok")));
        dataSatuan.setText(produk.getString("satuan_produk"));
        dataDibuat.setText(produk.getString("createLog_at"));
        dataDiedit.setText(produk.getString("updateLog_at"));
        dataDihapus.setText(produk.getString("deleteLog_at"));
        dataNIP.setText(produk.getString("updateLogId"));
        status = produk.getString("status");

        Glide.with(this)
                .load(ApiClient.BASE_URL + "produk/" + produk.getString("id_produk")+"/gambar")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(dataImage);

        if(status.equals("getAll")){
            dataDihapus.setText("-");
            btnDeletePermanen.setVisibility(View.INVISIBLE);
            btnRestore.setVisibility(View.INVISIBLE);
        }
        else{
            btnDelete.setVisibility(View.INVISIBLE);
            btnUpdate.setVisibility(View.INVISIBLE);
        }

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DETAIL PRODUK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        String id_produk = produk.getString("id_produk");
        String NIPLog = produk.getString("updateLogId");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UpdateProduk.class);
                i.putExtras(produk);
                startActivity(i);
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
                Call<Response> produk = apiService.restoreProduk(id_produk, NIPLog);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(DetailProduk.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Menghapus Data Produk");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Show it

                progressDialog.show();
                produk.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Data Produk Berhasil Direstore", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListProduk.class);
                            i.putExtra("status", status );
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

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Apakah Anda Yakin Menghapus Produk ini ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
                        Call<Response> produk = apiService.deleteProduk(id_produk, NIPLog);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailProduk.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data Produk");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        //Show it

                        progressDialog.show();
                        produk.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(),"Data Produk Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListProduk.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                }else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Data Produk Ini Tidak Dapat Dihapus.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
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
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnDeletePermanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Apakah Anda Ingin Menghapus Produk Secara Permanen ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
                        Call<Response> produk = apiService.deleteProdukPermanen(id_produk);

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(DetailProduk.this);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setTitle("Menghapus Data Produk Permanen");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        //Show it

                        progressDialog.show();
                        produk.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(),"Data Produk Berhasil Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListProduk.class);
                                    i.putExtra("status", status);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Data Produk Ini Tidak Dapat Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
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

        dataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailProduk.this, ZoomImage.class);
                i.putExtras(produk);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(getApplicationContext(), ListProduk.class);
        i.putExtra("status", status);
        startActivity(i);

        return true;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), ListProduk.class);
        i.putExtra("status", status);
        startActivity(i);
    }
}
