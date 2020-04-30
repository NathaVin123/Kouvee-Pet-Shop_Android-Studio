package com.petshopfix.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiPegawai;
import com.petshopfix.API.Response;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.SQLite.UserDefaults;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout txtNIP, txtPass;
    private Button btnMasuk;
    public static Bundle pgw;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        setAtribut();
        cekLogin();
        init();
    }

    private void cekLogin() {
        if(db.isEmpty()==false)
        {
            String status = db.getUser(1).getStatus();
            if(status.equals("isLogin")){
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("status","home" );
                startActivity(i);
            }
        }
    }

    public void setAtribut() {
        txtNIP = (TextInputLayout) findViewById(R.id.txtNIP);
        txtPass = (TextInputLayout) findViewById(R.id.txtPassword);

        btnMasuk = (Button) findViewById(R.id.btnMasuk);
        db = new DatabaseHandler(this);
    }

    private void init() {
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NIP = txtNIP.getEditText().getText().toString();
                String Pass = txtPass.getEditText().getText().toString();

                ApiPegawai apiService = ApiClient.getClient().create(ApiPegawai.class);
                Call<Response> pegawai = apiService.Login(NIP, Pass);

                final ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("loading....");
                progressDialog.setTitle("Izin Masuk");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // show it
                progressDialog.show();

                pegawai.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        progressDialog.dismiss();
                        if(response.code()==200)
                        {
                            String nip, nama, jabatan;
                            nip = response.body().getPegawai().get(0).getNIP();
                            nama = response.body().getPegawai().get(0).getNama_pegawai();
                            jabatan = response.body().getPegawai().get(0).getJabatan();

                            UserDefaults user = new UserDefaults(1,nip,nama,jabatan,"isLogin");

                            if(db.isEmpty()==true){
                                db.addUser(user);
                            }
                            else{
                                db.updateUser(user);
                            }

                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            i.putExtra("status","home" );
                            startActivity(i);
                        }
                        else if(response.code()==404)
                        {
                            Toast.makeText(getApplicationContext(), "NIP anda tidak terdaftar", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Password anda Salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
