package com.petshopfix.Activity.Ukuran;

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
import com.petshopfix.View.Ukuran.ListUkuran;

public class MenuUkuran extends AppCompatActivity {

    private CardView cvAdd, cvTampil, cvRestoreLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ukuran);

        setAtribut();
        init();
    }

    private void setAtribut(){
        cvAdd = (CardView) findViewById(R.id.btnTambahUkuran);
        cvTampil = (CardView) findViewById(R.id.btnTampilUkuran);
        cvRestoreLog = (CardView) findViewById(R.id.btnRestoreLogUkuran);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DASHBOARD UKURAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateUkuran.class);
                i.putExtra("status", "getAll");
                startActivity(i);
            }
        });

        cvTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListUkuran.class);
                i.putExtra("status", "getAll");
                startActivity(i);
            }
        });

        cvRestoreLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListUkuran.class);
                i.putExtra("status", "softDelete" );
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
