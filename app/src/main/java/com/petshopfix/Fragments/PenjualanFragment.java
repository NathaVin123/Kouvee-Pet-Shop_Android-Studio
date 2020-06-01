package com.petshopfix.Fragments;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petshopfix.Activity.Penjualan.Produk.InputPenjualanProduk;
import com.petshopfix.DAO.PegawaiDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.PenjualanProduk.TampilTransaksi.TampilTransaksiProdukShow;

public class PenjualanFragment extends Fragment {

    private CardView cvTampilTransaksiProduk, cvTampilTransaksiLayanan, cvTambahTransaksiProduk, cvTambahTransaksiLayanan;
    private PegawaiDAO Owner;
    private DatabaseHandler db;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_penjualan, container, false);

        setAtribut();
        init();

        return view;
    }

    private void setAtribut() {
        cvTampilTransaksiProduk     = (CardView) view.findViewById(R.id.cv_tampilPenjualanProduk);
        cvTampilTransaksiLayanan    = (CardView) view.findViewById(R.id.cv_tampilPenjualanLayanan);
        cvTambahTransaksiProduk     = (CardView) view.findViewById(R.id.cv_tambahPenjualanProduk);
        cvTambahTransaksiLayanan    = (CardView) view.findViewById(R.id.cv_tambahPenjualanLayanan);

        db = new DatabaseHandler(getContext());
        Owner = new PegawaiDAO(db.getUser(1).getNIP(), db.getUser(1).getNama_pegawai(),
                db.getUser(1).getJabatan());
    }

    private void init() {
        cvTampilTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), TampilTransaksiProdukShow.class));
            }
        });

        cvTampilTransaksiLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvTambahTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), InputPenjualanProduk.class));
            }
        });

        cvTambahTransaksiLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
