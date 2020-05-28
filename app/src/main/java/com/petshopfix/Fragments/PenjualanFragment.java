package com.petshopfix.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.petshopfix.Activity.PenjualanLayanan.MenuPenjualanLayanan;
import com.petshopfix.Activity.PenjualanProduk.MenuPenjualanProduk;
import com.petshopfix.DAO.PegawaiDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;

public class PenjualanFragment extends Fragment {

    private CardView cvTransaksiProduk, cvTransaksiLayanan;
    private PegawaiDAO CS;
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
        cvTransaksiProduk = (CardView) view.findViewById(R.id.cv_TransaksiProduk);
        cvTransaksiLayanan = (CardView) view.findViewById(R.id.cv_TransaksiLayanan);

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
        cvTransaksiProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuPenjualanProduk.class));
            }
        });

        cvTransaksiLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuPenjualanLayanan.class));
            }
        });
    }
}
