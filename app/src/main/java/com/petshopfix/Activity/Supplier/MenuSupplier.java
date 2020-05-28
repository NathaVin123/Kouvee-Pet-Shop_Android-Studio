package com.petshopfix.Activity.Supplier;

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
import com.petshopfix.View.Supplier.ListSupplier;

public class MenuSupplier extends AppCompatActivity {

    private CardView cvAdd, cvTampil, cvRestoreLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_supplier);

        setAtribut();
        init();
    }

    private void setAtribut() {
        cvAdd = (CardView) findViewById(R.id.btnTambahSupplier);
        cvTampil = (CardView) findViewById(R.id.btnTampilSupplier);
        cvRestoreLog = (CardView) findViewById(R.id.btnRestoreLogSupplier);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DASHBOARD SUPPLIER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateSupplier.class));
            }
        });

        cvTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListSupplier.class);
                i.putExtra("status", "getAll");
                startActivity(i);
            }
        });

        cvRestoreLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListSupplier.class);
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
