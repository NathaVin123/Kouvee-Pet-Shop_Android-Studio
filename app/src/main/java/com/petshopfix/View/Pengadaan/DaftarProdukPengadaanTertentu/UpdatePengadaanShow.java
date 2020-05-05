package com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.R;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaan.CreatePengadaanShow;

import java.util.ArrayList;
import java.util.List;

public class UpdatePengadaanShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnBatal, btnSimpan, btnTambah;
    private Spinner spinnerSupplier;
    private UpdatePengadaanAdapter adapter;
    private List<DetailPengadaanDAO> listDTP;
    private String nomorPO;
    private List<SupplierDAO> listsupplier;
    private int selectedIdSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pengadaan_show);

        init();
//        setDTPengadan();
//        setAdapter();
        setAtribut();
    }

    private void setAtribut() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreatePengadaanShow.class);
                i.putExtra("nomorPO", nomorPO);
                i.putExtra("cek", "Ubah Pengadaan");
                i.putExtra("nama_supplier",getIntent().getStringExtra("nama_supplier"));
                startActivity(i);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double totalHarga = 0.0;
                for(DetailPengadaanDAO dtPengadaan : listDTP)
                {
                    totalHarga = totalHarga + (dtPengadaan.getJumlah_po()*dtPengadaan.getHarga_produk());
                }
                System.out.println(totalHarga);

//                updateTransaksiPengadaan(nomorPO,selectedIdSupplier,totalHarga);
            }
        });
    }

    private void init() {
        nomorPO = getIntent().getStringExtra("noPO");
        listDTP = new ArrayList<DetailPengadaanDAO>();
        listsupplier = new ArrayList<SupplierDAO>();

        recyclerView = findViewById(R.id.recycler_view);
        spinnerSupplier = findViewById(R.id.namaSupplier_up);
        btnTambah = findViewById(R.id.btnTambah_up);
        btnSimpan = findViewById(R.id.btnSimpan_up);
        btnBatal = findViewById(R.id.btnBatal_up);

//        setListSupplier();
    }
}
