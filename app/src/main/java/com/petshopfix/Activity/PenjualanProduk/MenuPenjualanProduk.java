package com.petshopfix.Activity.PenjualanProduk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.petshopfix.Fragments.PenjualanFragment;
import com.petshopfix.R;

public class MenuPenjualanProduk extends AppCompatActivity {

    private CardView cvTambahPenjualanProduk, cvTampilPenjualanProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan_produk);

        setAtribut();
        init();
    }

    private void setAtribut() {
        cvTambahPenjualanProduk = (CardView) findViewById(R.id.cv_tambahPenjualanProduk);
        cvTampilPenjualanProduk = (CardView) findViewById(R.id.cv_tampilPenjualanProduk);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("PENJUALAN PRODUK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        cvTambahPenjualanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvTampilPenjualanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(getApplicationContext(), PenjualanFragment.class);
        i.putExtra("status", "penjualan" );
        startActivity(i);

        return true;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), PenjualanFragment.class);
        i.putExtra("status", "penjualan" );
        startActivity(i);
    }
}
