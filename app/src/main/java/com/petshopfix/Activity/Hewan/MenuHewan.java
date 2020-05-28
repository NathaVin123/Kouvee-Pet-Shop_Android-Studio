package com.petshopfix.Activity.Hewan;

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
import com.petshopfix.View.Hewan.ListHewan;

public class MenuHewan extends AppCompatActivity {

    private CardView cvAdd, cvTampil, cvRestoreLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_hewan);

        setAtribut();
        init();
    }

    private void setAtribut() {
        cvAdd = (CardView) findViewById(R.id.btnTambahHewan);
        cvTampil = (CardView) findViewById(R.id.btnTampilHewan);
        cvRestoreLog = (CardView) findViewById(R.id.btnRestoreLogHewan);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DASHBOARD HEWAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateHewan.class));
            }
        });

        cvTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListHewan.class);
                i.putExtra("status", "getAll");
                startActivity(i);
            }
        });

        cvRestoreLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListHewan.class);
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
