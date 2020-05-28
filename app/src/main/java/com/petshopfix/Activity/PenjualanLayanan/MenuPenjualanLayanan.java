package com.petshopfix.Activity.PenjualanLayanan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.petshopfix.Fragments.PenjualanFragment;
import com.petshopfix.R;

public class MenuPenjualanLayanan extends AppCompatActivity {

    private CardView cvTambahPenjualanLayanan, cvTampilPenjualanLayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_penjualan_layanan);

        setAtribut();
        init();
    }

    private void setAtribut() {
        cvTambahPenjualanLayanan = (CardView) findViewById(R.id.cv_tambahPenjualanLayanan);
        cvTampilPenjualanLayanan = (CardView) findViewById(R.id.cv_tampilPenjualanLayanan);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("PENJUALAN LAYANAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        cvTambahPenjualanLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvTampilPenjualanLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(getApplicationContext(), PenjualanFragment.class);
        i.putExtra("status", "dataMaster" );
        startActivity(i);

        return true;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), PenjualanFragment.class);
        i.putExtra("status", "dataMaster");
        startActivity(i);
    }
}
