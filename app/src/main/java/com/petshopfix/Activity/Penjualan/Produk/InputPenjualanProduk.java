package com.petshopfix.Activity.Penjualan.Produk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiTransaksiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.PenjualanProduk.TambahTransaksi.TambahProdukPenjualanShow;

import retrofit2.Call;
import retrofit2.Callback;

public class InputPenjualanProduk extends AppCompatActivity {

    private CardView cvMember, cvPengunjung;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_penjualan_produk);

        init();
        setAtribut();
    }

    private void setAtribut() {
        cvMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PilihCustomer.class));
            }
        });

        cvPengunjung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idCS = db.getUser(1).getNIP();
                System.out.println(idCS);

                ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
                Call<Response> transaksiProduk = apiService.createPenjualanProduk(null, 0.0, idCS);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(InputPenjualanProduk.this);
                progressDialog.setMessage("Loading....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //show it
                progressDialog.show();

                transaksiProduk.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if (!response.body().getTransaksiProduk().isEmpty())
                        {
                            System.out.println("Success");
                            Intent i = new Intent(getApplicationContext(), TambahProdukPenjualanShow.class);
                            i.putExtra("no_transaksi", response.body().getTransaksiProduk().get(0).getNo_transaksi());
                            i.putExtra("status", "tambah");
                            i.putExtra("nama_customer", "Tidak Diketahui");
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void init() {
        cvMember        = (CardView) findViewById(R.id.cardMember);
        cvPengunjung    = (CardView) findViewById(R.id.cardPengunjung);

        db = new DatabaseHandler(this);
    }
}
