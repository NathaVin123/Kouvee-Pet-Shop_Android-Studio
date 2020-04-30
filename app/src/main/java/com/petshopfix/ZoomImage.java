package com.petshopfix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.zolad.zoominimageview.ZoomInImageView;

public class ZoomImage extends AppCompatActivity {

    private ZoomInImageView img;
    private Bundle bundle;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        String idP = getIntent().getStringExtra("idP");
        img = (ZoomInImageView) findViewById(R.id.imageZoom);
        bundle = getIntent().getExtras();
        status = bundle.getString("status");

        if (status.equals("produk"))
        {
            String url = ApiClient.BASE_URL + "produk/" + bundle.getString("id_produk") + "/gambar";
            Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img);
            getSupportActionBar().setTitle(bundle.getString("nama_produk"));
        }
        else
        {
            String url = ApiClient.BASE_URL + "pegawai/" + bundle.getString("nip") + "/gambar";
            Glide.with(this)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img);
            getSupportActionBar().setTitle(bundle.getString("nama_pegawai"));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
