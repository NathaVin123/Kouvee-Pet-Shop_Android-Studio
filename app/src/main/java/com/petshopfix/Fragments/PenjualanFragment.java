package com.petshopfix.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;

public class PenjualanFragment extends Fragment {

    private ImageView btnPenjualanProduk, btnPenjualanLayanan;
    private String jabatan;
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
        btnPenjualanProduk = (ImageView) view.findViewById(R.id.btnPenjualanProduk);
        btnPenjualanLayanan = (ImageView) view.findViewById(R.id.btnPenjualanProduk);

        db = new DatabaseHandler(getContext());

//        if(db.getUser(1).getJabatan().equals("Owner"))
//        {
//            btnPengadaan.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            btnPenjualanProduk.setVisibility(View.INVISIBLE);
//            btnPenjualanLayanan.setVisibility(View.INVISIBLE);
//        }
    }

    private void init() {
        btnPenjualanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnPenjualanLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
