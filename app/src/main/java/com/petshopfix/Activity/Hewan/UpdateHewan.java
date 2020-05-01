package com.petshopfix.Activity.Hewan;

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
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Interface.ApiHewan;
import com.petshopfix.API.Interface.ApiJenis;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.DAO.JenisDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.Hewan.ListHewan;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateHewan extends AppCompatActivity {

    private TextInputLayout txtNama, txtTglLahir;
    private TextView txtId;
    private Button btnSimpan;
    private Spinner dataJenis, dataCustomer;
    private DatabaseHandler db;
    private Bundle hewan;
    private ProgressDialog progressDialog;
    private List<JenisDAO> listJenis;
    private List<CustomerDAO> listCustomer;
    private int selectJenis;
    private int selectCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hewan);

        try {
            setAtribut();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        init();
        setDaftarJenis();
        setDaftarCustomer();
    }

    private void setAtribut() throws MalformedURLException{
        txtId = (TextView) findViewById(R.id.dataIDHewan);
        txtNama = (TextInputLayout) findViewById(R.id.txtNamaHewan);
        txtTglLahir = (TextInputLayout) findViewById(R.id.txtTanggalLahirHewan);
        dataJenis = (Spinner) findViewById(R.id.dataJenis);
        dataCustomer = (Spinner) findViewById(R.id.dataCustomer);
        btnSimpan = (Button) findViewById(R.id.btnSave);

        hewan = getIntent().getExtras();
        listJenis = new ArrayList<>();
        listCustomer = new ArrayList<>();

        txtId.setText(String.valueOf(hewan.getInt("id_hewan")));
        txtNama.getEditText().setText(hewan.getString("nama_hewan"));
        txtTglLahir.getEditText().setText(hewan.getString("tglLahir_hewan"));

        db = new DatabaseHandler(this);
        progressDialog = new ProgressDialog(UpdateHewan.this);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView) findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("Update Hewan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDaftarJenis() {
        ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
        Call<Response> jeniss = apiService.getAll();

        jeniss.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body().getJenis().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Jenis Kosong",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listJenis.addAll(response.body().getJenis());
                    List<String> daftarNamaJenis = new ArrayList<>();
                    for (int i=0; i<listJenis.size(); i++)
                    {
                        daftarNamaJenis.add(listJenis.get(i).getNama_jenis());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, daftarNamaJenis);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataJenis.setAdapter(adapter);
                    int spinnerPosition = adapter.getPosition(hewan.getString("nama_jenis"));
                    dataJenis.setSelection(spinnerPosition);

                    dataJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i=0; i<listJenis.size();i++)
                            {
                                if(dataJenis.getSelectedItem().toString().equals(listJenis.get(i).getNama_jenis()))
                                {
                                    selectJenis = listJenis.get(i).getId_jenis();
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

    private void setDaftarCustomer() {
        ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
        Call<Response> customers = apiService.getAll();

        customers.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body().getCustomer().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Hewan Kosong",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    listCustomer.addAll(response.body().getCustomer());
                    List<String> daftarNamaCustomer = new ArrayList<>();
                    for (int i=0; i<listCustomer.size(); i++)
                    {
                        daftarNamaCustomer.add(listCustomer.get(i).getNama_customer());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, daftarNamaCustomer);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataCustomer.setAdapter(adapter);
                    int spinnerPosition = adapter.getPosition(hewan.getString("nama_customer"));
                    dataCustomer.setSelection(spinnerPosition);

                    dataCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for(int i=0; i<listCustomer.size();i++)
                            {
                                if(dataCustomer.getSelectedItem().toString().equals(listCustomer.get(i).getNama_customer()))
                                {
                                    selectCustomer = listCustomer.get(i).getId_customer();
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
                int id_hewan;
                int id_jenisH, id_customerH;
                String namaH, tglLahirH, updateLodIdH;

                id_hewan = Integer.parseInt(txtId.getText().toString());
                namaH = txtNama.getEditText().getText().toString();
                tglLahirH = txtTglLahir.getEditText().getText().toString();
                id_jenisH = selectJenis;
                id_customerH = selectCustomer;
                updateLodIdH = db.getUser(1).getNIP();

                System.out.println(selectJenis);
                System.out.println(selectCustomer);

                if (namaH.isEmpty() || tglLahirH.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boleh Kosong",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody nama_hewan =
                            RequestBody.create(MediaType.parse("multipart/form-data"), namaH);
                    RequestBody tglLahir_hewan =
                            RequestBody.create(MediaType.parse("multipart/form-data"), tglLahirH);
                    RequestBody id_jenis =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_jenisH));
                    RequestBody id_customer =
                            RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(id_customerH));
                    RequestBody updateLogId =
                            RequestBody.create(MediaType.parse("multipart/form-data"), updateLodIdH);

                    ApiHewan apiService = ApiClient.getClient().create(ApiHewan.class);
                    Call<Response> hewan = apiService.updateHewan(id_hewan, nama_hewan, tglLahir_hewan, id_jenis, id_customer, updateLogId);

                    progressDialog.setMessage("loading....");
                    progressDialog.setTitle("Mengubah Data Hewan");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    progressDialog.show();
                    hewan.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if(response.code() == 200) {
                                if(response.body().getStatus().equals("Error"))
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Update Data Hewan Berhasil.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), ListHewan.class);
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
        startActivity(new Intent(getApplicationContext(), MenuHewan.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MenuHewan.class));
    }
}
