package com.petshopfix.Activity.Produk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Produk.ListProduk;
import com.petshopfix.ZoomImage;

import java.io.File;
import java.net.MalformedURLException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateProduk extends AppCompatActivity {

    private TextView txtIDProduk;
    private ImageView txtImage;
    private TextInputLayout txtNama, txtHarga, txtStok, txtMinStok, txtSatuan;
    private Button btnUpload, btnSave;
    private DatabaseHandler db;
    private Bundle produk;
    private Uri image_uri = null;
    private File file;
    private ProgressDialog progressDialog;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produk);

        try {
            setAtribut();
        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }
        init();
    }

    private void setAtribut() throws MalformedURLException {
        txtIDProduk = (TextView) findViewById(R.id.dataIDProduk);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaProduk);
        txtHarga = (TextInputLayout) findViewById(R.id.txtHargaProduk);
        txtStok = (TextInputLayout) findViewById(R.id.txtStokProduk);
        txtMinStok = (TextInputLayout) findViewById(R.id.txtMinStokProduk);
        txtSatuan = (TextInputLayout) findViewById(R.id.txtSatuanProduk);

        txtImage = (ImageView) findViewById(R.id.dataGambarProduk);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnSave = (Button) findViewById(R.id.btnSave);
        produk = getIntent().getExtras();

        txtIDProduk.setText(produk.getString("id_produk"));
        txtNama.getEditText().setText(produk.getString("nama_produk"));
        txtHarga.getEditText().setText(String.valueOf(produk.getDouble("harga_produk")));
        txtStok.getEditText().setText(String.valueOf(produk.getInt("stok")));
        txtMinStok.getEditText().setText(String.valueOf(produk.getInt("min_stok")));
        txtSatuan.getEditText().setText(produk.getString("satuan_produk"));

        Glide.with(this)
                .load(ApiClient.BASE_URL + "produk/" + produk.getString("id_produk") + "/gambar")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(txtImage);

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateProduk.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul =(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("UPDATE PRODUK");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(UpdateProduk.this);
                View view = inflater.inflate(R.layout.choose_media, null);

                final AlertDialog alertD = new AlertDialog.Builder(UpdateProduk.this).create();
                Button btnKamera = (Button) view.findViewById(R.id.btnKamera);
                Button btnGaleri = (Button) view.findViewById(R.id.btnGaleri);

                btnKamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                    PackageManager.PERMISSION_DENIED ||
                                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                            PackageManager.PERMISSION_DENIED){
                                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission, PERMISSION_CODE);
                            }else {
                                openCamera();
                            }
                        }else {
                            openCamera();
                        }
                        alertD.dismiss();
                    }
                });

                btnGaleri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permission, PERMISSION_CODE);
                            } else {
                                FileChooser();
                            }
                        }else {
                            FileChooser();
                        }
                        alertD.dismiss();
                    }
                });
                alertD.setView(view);
                alertD.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_produk, namaP, satuanP, updateLog_byP;
                Double hargaP;
                int stokP, minStokP;

                id_produk = txtIDProduk.getText().toString();
                namaP = txtNama.getEditText().getText().toString();
                hargaP = Double.parseDouble(txtHarga.getEditText().getText().toString());
                stokP = Integer.parseInt(txtStok.getEditText().getText().toString());
                minStokP = Integer.parseInt(txtMinStok.getEditText().getText().toString());
                satuanP = txtSatuan.getEditText().getText().toString();
                updateLog_byP = db.getUser(1).getNIP();

                if(namaP.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nama Anda Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }else if(hargaP < 1 || stokP < 1 || minStokP < 1){
                    Toast.makeText(getApplicationContext(),"Data harga, stok dan jumlah minimal tidak boleh lebih kecil atau sama dengan 0 !",Toast.LENGTH_SHORT).show();
                }else if(satuanP.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Satuan Anda Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }else {
                    RequestBody nama_produk =
                            RequestBody.create(MediaType.parse("multipart/form-data"), namaP);
                    RequestBody harga_produk =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(hargaP));
                    RequestBody stok =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(stokP));
                    RequestBody min_stok =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(minStokP));
                    RequestBody satuan_produk =
                            RequestBody.create(MediaType.parse("multipart/form-data"), satuanP);

                    MultipartBody.Part body = null;
                    if(image_uri != null){
                        file = new File(getRealPathFromUri(getApplicationContext(), image_uri));
                        RequestBody requestFile = RequestBody.create(MediaType.parse("images/*"), file);
                        body = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);
                    }else{
                        RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("images/*"),"");
                        body = MultipartBody.Part.createFormData("gambar", "", attachmentEmpty);
                    }

                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLog_byP);

                    ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
                    Call<Response> produk = apiService.updateProduk(id_produk, nama_produk, harga_produk, stok,
                            min_stok, satuan_produk, body, updateLogId);

                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Updating Data Produk.");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDialog.show();

                    produk.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            System.out.println(response.code());
                            if(response.code() == 200){
                                if(response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(getApplicationContext(),"Update Data Produk Berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListProduk.class);
                                    i.putExtra("status","getAll" );
                                    startActivity(i);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        txtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdateProduk.this, ZoomImage.class);
                i.putExtras(produk);
                startActivity(i);
            }
        });
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else{
                    Toast.makeText(this,"Permision denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            image_uri = data.getData();
        }
        txtImage.setImageURI(image_uri);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri =  getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        System.out.println("From Camera : " + image_uri.toString());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri){
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent i = new Intent(UpdateProduk.this, ListProduk.class);
        i.putExtras(produk);
        startActivity(i);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateProduk.this, ListProduk.class);
        i.putExtras(produk);
        startActivity(i);
    }
}
