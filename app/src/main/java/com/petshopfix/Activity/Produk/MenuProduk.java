package com.petshopfix.Activity.Produk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petshopfix.Activity.HomeActivity;
import com.petshopfix.R;
import com.petshopfix.View.Produk.ListProduk;

public class MenuProduk extends AppCompatActivity {

    private CardView cvAdd, cvTampil, cvRestoreLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_produk);

        setAtribut();
        init();
    }

    private void setAtribut() {
        cvAdd = (CardView) findViewById(R.id.btnTambahProduk);
        cvTampil = (CardView) findViewById(R.id.btnTampilProduk);
        cvRestoreLog = (CardView) findViewById(R.id.btnRestoreLogProduk);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DASHBOARD PRODUK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateProduk.class));
            }
        });

        cvTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListProduk.class);
                i.putExtra("status", "getAll");
                startActivity(i);
            }
        });

        cvRestoreLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListProduk.class);
                i.putExtra("status", "softDelete");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("status", "dataMaster" );
        startActivity(i);

        return true;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("status", "dataMaster" );
        startActivity(i);
    }
}
