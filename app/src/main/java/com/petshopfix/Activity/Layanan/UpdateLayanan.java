package com.petshopfix.Activity.Layanan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiLayanan;
import com.petshopfix.API.Interface.ApiUkuran;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.UkuranDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Layanan.ListLayanan;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateLayanan extends AppCompatActivity {

    private TextInputLayout txtNama, txtHarga;
    private TextView txtId;
    private Button btnSimpan;
    private Spinner dataUkuran;
    private DatabaseHandler db;
    private Bundle layanan;
    private ProgressDialog progressDialog;
    private List<UkuranDAO> listUkuran;
    private int selectUkuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_layanan);

        try {
            setAtribut();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        init();
        setDaftarUkuran();
    }

    private void setAtribut() throws MalformedURLException {
        txtId = (TextView) findViewById(R.id.dataIDLayanan);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaLayanan);
        txtHarga = (TextInputLayout) findViewById(R.id.txtHargaLayanan);
        dataUkuran = (Spinner) findViewById(R.id.dataUkuran);
        btnSimpan = (Button) findViewById(R.id.btnSave);

        layanan = getIntent().getExtras();
        listUkuran = new ArrayList<>();

        txtId.setText(layanan.getString("id_layanan"));
        txtNama.getEditText().setText(layanan.getString("nama_layanan"));
        txtHarga.getEditText().setText(String.valueOf(layanan.getDouble("harga_layanan")));

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateLayanan.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Update com.petshopfix.Activity.Penjualan.Layanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDaftarUkuran() {
        ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
        Call<Response> ukurans = apiService.getAll();

        ukurans.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body().getUkuran().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data com.petshopfix.Activity.Penjualan.Layanan Kosong",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listUkuran.addAll(response.body().getUkuran());
                    List<String> daftarNamaUkuran = new ArrayList<>();
                    for (int i=0; i<listUkuran.size(); i++)
                    {
                        daftarNamaUkuran.add(listUkuran.get(i).getNama_ukuran());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, daftarNamaUkuran);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataUkuran.setAdapter(adapter);
                    int spinnerPosition = adapter.getPosition(layanan.getString("nama_ukuran"));
                    dataUkuran.setSelection(spinnerPosition);

                    dataUkuran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i=0; i<listUkuran.size();i++)
                            {
                                if(dataUkuran.getSelectedItem().toString().equals(listUkuran.get(i).getNama_ukuran()))
                                {
                                    selectUkuran = listUkuran.get(i).getId_ukuran();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                System.out.println(t.getCause());
            }
        });
    }

    private void init() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_layanan, namaL, updateLodIdL;
                double hargaL;
                int id_ukuranL;

                id_layanan = layanan.getString("id_layanan");
                updateLodIdL = db.getUser(1).getNIP();
                namaL = txtNama.getEditText().getText().toString();
                hargaL = Double.parseDouble(txtHarga.getEditText().getText().toString());
                id_ukuranL = selectUkuran;

                System.out.println(selectUkuran);

                if (namaL.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }
                else if (hargaL < 1)
                {
                    Toast.makeText(getApplicationContext(), "Harga Tidak Boleh Lebih Kecil dari 0",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    RequestBody nama_layanan =
                            RequestBody.create(MediaType.parse("multipart/form-data"), namaL);
                    RequestBody harga_layanan =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(hargaL));
                    RequestBody id_ukuran =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_ukuranL));
                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLodIdL);

                    ApiLayanan apiService = ApiClient.getClient().create(ApiLayanan.class);
                    Call<Response> layanan = apiService.updateLayanan(id_layanan, nama_layanan, harga_layanan, id_ukuran, updateLogId);

                    progressDialog.setMessage("loading....");
                    progressDialog.setTitle("Mengubah Data com.petshopfix.Activity.Penjualan.Layanan");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    layanan.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if(response.code() == 200) {
                                if(response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Update Data com.petshopfix.Activity.Penjualan.Layanan Berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListLayanan.class);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), MenuLayanan.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuLayanan.class));
    }
}
