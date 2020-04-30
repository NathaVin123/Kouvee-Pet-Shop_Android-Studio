package com.petshopfix.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.petshopfix.API.ApiClient;
import com.petshopfix.Fragments.DataMasterFragment;
import com.petshopfix.Fragments.HomeFragment;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.SQLite.UserDefaults;
import com.petshopfix.ZoomImage;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nv;
    private DatabaseHandler db;
    private ImageView fotoPegawai;
    private TextView txtNama, txtJabatan, txtNip;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setAtribut();
        cek();
        init();
    }

    private void cek() {
        String status = getIntent().getStringExtra("status");
        if (status.equals("dataMaster"))
            loadFragment(new DataMasterFragment());
        else if (status.equals("pengadaan"))
            loadFragment(new DataMasterFragment());
        else if (status.equals("penjualan"))
            loadFragment(new HomeFragment());
    }

    public void setAtribut() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logoapp1);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, new HomeFragment());
        fragmentTransaction.commit();

        nv = (NavigationView) findViewById(R.id.NavigationView);
        View view = nv.getHeaderView(0);

        fotoPegawai = (ImageView) view.findViewById(R.id.navFotoPegawai);
        txtNama = (TextView) view.findViewById(R.id.navNama);
        txtJabatan = (TextView) view.findViewById(R.id.navJabatan);
        txtNip = (TextView) view.findViewById(R.id.navNIP);

        db = new DatabaseHandler(this);

        if (db.getUser(1).getJabatan().equals("Owner")) {
            txtNip.setVisibility(view.INVISIBLE);
            nv.getMenu().findItem(R.id.penjualan).setVisible(false);
        }
        else if (db.getUser(1).getJabatan().equals("Customer Service")) {
            nv.getMenu().findItem(R.id.pengadaan).setVisible(false);
        }
        else {
            nv.getMenu().findItem(R.id.dataMaster).setVisible(false);
            nv.getMenu().findItem(R.id.pengadaan).setVisible(false);
            nv.getMenu().findItem(R.id.penjualan).setVisible(false);
        }
        txtNama.setText(db.getUser(1).getNama_pegawai());
        txtJabatan.setText(db.getUser(1).getJabatan());
        txtNip.setText(db.getUser(1).getNIP());

        Glide.with(this)
                .load(ApiClient.BASE_URL + "pegawai/" + db.getUser(1).getNIP() + "/gambar")
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotoPegawai);
    }

    private void init() {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                closeDrawer();
                switch (id)
                {
                    case R.id.dashboard :
                        loadFragment(new HomeFragment());
                        break;
                    case R.id.dataMaster :
                        loadFragment(new DataMasterFragment());
                        break;
                    case R.id.pengadaan :
                        Toast.makeText(getApplicationContext(), "Pengadaan",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.penjualan :
                        Toast.makeText(getApplicationContext(), "Penjualan",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.keluar :
                        UserDefaults user = new UserDefaults(1,"","","","isLogout");
                        db.updateUser(user);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getApplicationContext(), "Keluar Berhasil",Toast.LENGTH_SHORT).show();
                        break;
                    default :
                        return true;
                }
                return true;
            }
        });

        fotoPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle pegawai = new Bundle();

                pegawai.putString("nip", db.getUser(1).getNIP());
                pegawai.putString("nama_pegawai",db.getUser(1).getNama_pegawai());
                pegawai.putString("status", "pegawai");

                Intent i = new Intent(HomeActivity.this, ZoomImage.class);
                i.putExtras(pegawai);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {

    }
}
