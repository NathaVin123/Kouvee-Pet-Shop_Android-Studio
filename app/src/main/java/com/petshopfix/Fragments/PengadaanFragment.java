package com.petshopfix.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.petshopfix.Activity.Pengadaan.CreatePengadaan;
import com.petshopfix.DAO.PegawaiDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Pengadaan.DaftarPengadaan.ShowPengadaan;
import com.petshopfix.View.Produk.ListProduk;

public class PengadaanFragment extends Fragment {
    private CardView cvTampilPengadaan, cvTampilJumlahPengadaan, cvTambahPengadaan;
    private PegawaiDAO Owner;
    private DatabaseHandler db;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pengadaan, container, false);

        setAtribut();
        init();

        return view;
    }

    private void setAtribut() {
        cvTampilPengadaan = (CardView) view.findViewById(R.id.cv_tampilPengadaan);
        cvTambahPengadaan = (CardView) view.findViewById(R.id.cv_tambahPengadaan);
        cvTampilJumlahPengadaan = (CardView) view.findViewById(R.id.cv_tampilMinimalPengadaan);
//        cvVerifikasiStatusPengadaan = (CardView) view.findViewById(R.id.cv_verifikasiStatusPengadaan);

        db = new DatabaseHandler(getContext());
        Owner = new PegawaiDAO(db.getUser(1).getNIP(), db.getUser(1).getNama_pegawai(), db.getUser(1).getJabatan());
    }

    private void init() {
        cvTambahPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), CreatePengadaan.class));
            }
        });

        cvTampilPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), ShowPengadaan.class));
            }
        });

        cvTampilJumlahPengadaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), ListProduk.class);
                i.putExtra("status", "minimal");
                startActivity(i);
            }
        });

//        cvVerifikasiStatusPengadaan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(view.getContext(), tambahPengadaanShow.class));
//            }
//        });
    }
}
