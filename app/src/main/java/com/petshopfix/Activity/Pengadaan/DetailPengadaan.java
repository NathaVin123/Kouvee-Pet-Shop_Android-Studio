package com.petshopfix.Activity.Pengadaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfWriter;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiDetailPengadaan;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.R;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu.UpdatePengadaanShow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailPengadaan extends AppCompatActivity {

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    private PdfWriter writer;
    Context context;
    private List<DetailPengadaanDAO> listdtProduk;
    private Button cetakPdf, btnUbah;
    private String nomorPO;
    private TableLayout tablePengadaan;
    private Bundle detailPengadaan;
    private TextView tvNamaSupplier, tvAlamatSupplier, tvNoHpSupplier, tvNoPO, tvTanggalPengadaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengadaan);

        setAtribut();
        init();
//        init();
    }

    private void init() {
        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailPengadaan.getString("statusPengadaan").equals("Sudah Datang"))
                {
                    Toast.makeText(DetailPengadaan.this, "Pengadaan produk dengan status sudah datang tidak dapat diubah !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), UpdatePengadaanShow.class);
                    i.putExtra("nomorPO", nomorPO);
                    i.putExtra("nama_supplier", detailPengadaan.getString("nama_supplier"));
                    startActivity(i);
                }
            }
        });
    }

    private void setAtribut() {
        context = this;
//        cetakPdf = (Button) findViewById(R.id.btnCetak);
        btnUbah = (Button) findViewById(R.id.btnUbah);
        detailPengadaan = getIntent().getExtras();
        tvNamaSupplier = (TextView) findViewById(R.id.txtNamaSupplier);
        tvAlamatSupplier = (TextView) findViewById(R.id.txtAlamatSupplier);
        tvNoHpSupplier = (TextView) findViewById(R.id.txtTelpSupplier);
//        tvNoPO = (TextView) findViewById(R.id.txtNoPO);
//        tvTanggalPengadaan = (TextView) findViewById(R.id.txtTanggalPengadaan);

        tvNamaSupplier.setText(detailPengadaan.getString("nama_supplier"));
        tvAlamatSupplier.setText(detailPengadaan.getString("alamat_supplier"));
        tvNoHpSupplier.setText(detailPengadaan.getString("noTelp_supplier"));
        tvNoPO.setText("No : " + detailPengadaan.getString("nomorPO"));
        tvTanggalPengadaan.setText("Tanggal : " + detailPengadaan.getString("tgl_po"));

        listdtProduk = new ArrayList<DetailPengadaanDAO>();
        nomorPO = detailPengadaan.getString("nomorPO");

        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<Response> dtPengadaan = apiService.tampilPengadaan(nomorPO);

        dtPengadaan.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(!response.body().getDetailPengadaan().isEmpty())
                {
                    listdtProduk.addAll(response.body().getDetailPengadaan());
                    detailPengadaan();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void detailPengadaan() {
        tablePengadaan = (TableLayout) findViewById(R.id.tablePengadaan);
        TextView C[] = new TextView[4];

        for (int i = 0; i < listdtProduk.size(); i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lps = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lps);

            for (int j = 0; j < 4; j++) {
                C[j] = new TextView(this);
                C[j].setBackgroundColor(Color.WHITE);
                C[j].setTextColor(Color.DKGRAY);
                C[j].setTextSize(12);
                C[j].setBackgroundResource(R.drawable.row_borders);
            }

            C[0].setGravity(Gravity.CENTER_HORIZONTAL);
            C[1].setPaddingRelative(20, 0, 0, 0);
            C[2].setPaddingRelative(50, 0, 0, 0);
            C[3].setGravity(Gravity.RIGHT);
            C[3].setPaddingRelative(0, 0, 100, 0);

            C[0].setWidth(162);
            C[1].setWidth(374);
            C[2].setWidth(269);
            C[3].setWidth(272);

            C[0].setText(String.valueOf(1 + i));
            C[1].setText(listdtProduk.get(i).getNama_produk());
            C[2].setText(String.valueOf(listdtProduk.get(i).getSatuan()));
            C[3].setText(String.valueOf(listdtProduk.get(i).getJumlah_po()));

            row.addView(C[0]);
            row.addView(C[1]);
            row.addView(C[2]);
            row.addView(C[3]);

            tablePengadaan.addView(row, i);
        }
    }
}
