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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petshopfix.Activity.Customer.MenuCustomer;
import com.petshopfix.Activity.Hewan.MenuHewan;
import com.petshopfix.Activity.Jenis.MenuJenis;
import com.petshopfix.Activity.Layanan.MenuLayanan;
import com.petshopfix.Activity.Produk.MenuProduk;
import com.petshopfix.Activity.Supplier.MenuSupplier;
import com.petshopfix.Activity.Ukuran.MenuUkuran;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;

public class DataMasterFragment extends Fragment {

    private CardView cvProduk, cvLayanan, cvJenis, cvUkuran, cvSupplier, cvCustomer, cvHewan;
    private LinearLayout llprodukLayanan, llcustomerHewan, lljenisUkuran, llsupplier;
    private String jabatan;
    private DatabaseHandler db;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_datamaster, container, false);
        setAtribut();
        init();

        return view;
    }

    private void setAtribut() {
        cvCustomer = (CardView) view.findViewById(R.id.cv_customerID);
        cvHewan = (CardView) view.findViewById(R.id.cv_hewanID);
        cvJenis = (CardView) view.findViewById(R.id.cv_jenishewanID);
        cvUkuran = (CardView) view.findViewById(R.id.cv_ukuranhewanID);
        cvSupplier = (CardView) view.findViewById(R.id.cv_supplierID);
        cvProduk = (CardView) view.findViewById(R.id.cv_produkID);
        cvLayanan = (CardView) view.findViewById(R.id.cv_layananID);

        llprodukLayanan = (LinearLayout) view.findViewById(R.id.linearLayoutPL);
        llcustomerHewan = (LinearLayout) view.findViewById(R.id.linearLayoutCH);
        lljenisUkuran = (LinearLayout) view.findViewById(R.id.linearLayoutJU);
        llsupplier = (LinearLayout) view.findViewById(R.id.linearLayoutS);

        db = new DatabaseHandler(getContext());

        if(db.getUser(1).getJabatan().equals("Owner"))
        {
            llcustomerHewan.setVisibility(View.INVISIBLE);
        }
        else
        {
            llprodukLayanan.setVisibility(View.INVISIBLE);
            lljenisUkuran.setVisibility(View.INVISIBLE);
            llsupplier.setVisibility(View.INVISIBLE);
        }
    }

    private void init() {
        cvProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuProduk.class));
            }
        });

        cvLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuLayanan.class));
            }
        });

        cvJenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuJenis.class));
            }
        });

        cvUkuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuUkuran.class));
            }
        });

        cvSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuSupplier.class));
            }
        });

        cvCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuCustomer.class));
            }
        });

        cvHewan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), MenuHewan.class));
            }
        });
    }
}
