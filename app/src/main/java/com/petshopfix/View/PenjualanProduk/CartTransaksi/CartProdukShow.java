package com.petshopfix.View.PenjualanProduk.CartTransaksi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiCustomer;
import com.petshopfix.API.Interface.ApiDetailProduk;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Interface.ApiTransaksiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.HomeActivity;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.DAO.DetailTransaksiProdukDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.PenjualanProduk.TambahTransaksi.TambahProdukPenjualanShow;
import com.petshopfix.View.PenjualanProduk.TampilTransaksi.TampilTransaksiProdukShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CartProdukShow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnBatal, btnSimpan, btnTambah;
    private Spinner spinnerCustomer;
    private TextView tv_totalBiaya, tv_pemberitahuan;
    private CartProdukAdapter adapter;
    private List<DetailTransaksiProdukDAO> listDTP;
    private String no_transaksi, selectedIdCustomer, nama_customer, status;
    private Double BiayaTotal;
    private List<CustomerDAO> listCustomer;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_produk_show);

        init();
        setDetailProduk();
        setAdapter();
        setAtribut();
    }

    private void setAtribut() {
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TambahProdukPenjualanShow.class);
                i.putExtra("no_transaksi", no_transaksi);
                i.putExtra("status", status);
                i.putExtra("nama_customer", nama_customer);
                startActivity(i);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(BiayaTotal);
                updateTransaksiProduk(no_transaksi, selectedIdCustomer, BiayaTotal);
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("tambah"))
                    pembatalanPemesananProduk(no_transaksi);
                else if (status.equals("detail"))
                    startActivity(new Intent(getApplicationContext(), TampilTransaksiProdukShow.class));
            }
        });
    }

    private void init() {
        no_transaksi = getIntent().getStringExtra("no_transaksi");
        status = getIntent().getStringExtra("status");
        nama_customer = getIntent().getStringExtra("nama_customer");
        System.out.println("init "+nama_customer);
        listDTP = new ArrayList<DetailTransaksiProdukDAO>();
        listCustomer = new ArrayList<CustomerDAO>();

        tv_totalBiaya = (TextView) findViewById(R.id.totalBiaya_up3);
        tv_pemberitahuan = (TextView) findViewById(R.id.txtDiskon);
        recyclerView = findViewById(R.id.recycler_view);
        spinnerCustomer = findViewById(R.id.namaCustomer_up3);
        btnTambah = findViewById(R.id.btnTambah_up3);
        btnSimpan = findViewById(R.id.btnSimpan_up3);
        btnBatal = findViewById(R.id.btnBatal_up3);

        tv_pemberitahuan.setVisibility(View.INVISIBLE);

        db = new DatabaseHandler(this);

        if (status.equals("detail"))
            btnBatal.setText("Batal");

        setListCustomer();
    }

    private void setListCustomer() {
        ApiCustomer apiService = ApiClient.getClient().create(ApiCustomer.class);
        Call<Response> customers = apiService.getAll();

        customers.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(!response.body().getCustomer().isEmpty())
                {
                    listCustomer.addAll(response.body().getCustomer());
                    List<String> daftarNamaCustomer = new ArrayList<>();
                    daftarNamaCustomer.add("Tidak Diketahui");
                    for (int i=0; i<listCustomer.size();i++)
                    {
                        daftarNamaCustomer.add(listCustomer.get(i).getNama_customer());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, daftarNamaCustomer);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCustomer.setAdapter(adapter);
                    int spinnerPosition = adapter.getPosition(nama_customer);
                    spinnerCustomer.setSelection(spinnerPosition);

                    spinnerCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            for (int i=0; i<listCustomer.size();i++)
                            {
                                if (spinnerCustomer.getSelectedItem().toString().equals(listCustomer.get(i).getNama_customer()))
                                {
                                    selectedIdCustomer = String.valueOf(listCustomer.get(i).getId_customer());
                                }
                            }

                            if (spinnerCustomer.getSelectedItem().toString().equals("Tidak Diketahui"))
                                selectedIdCustomer = null;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void setDetailProduk() {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<Response> dtp = apiService.tampilDetailProduk(no_transaksi);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtp.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (!response.body().getDetailProduk().isEmpty())
                {
                    listDTP.addAll(response.body().getDetailProduk());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        adapter = new CartProdukAdapter(this, listDTP, no_transaksi, status, nama_customer, new CartProdukAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChange(Double totalBiaya, String nama_customer) {
                if (nama_customer.equals("Tidak Diketahui"))
                {
                    tv_totalBiaya.setText("RP "+ totalBiaya +"0");
                    BiayaTotal = totalBiaya;
                }
                else
                {
                    tv_pemberitahuan.setVisibility(View.VISIBLE);
                    totalBiaya = totalBiaya - (0.1 * totalBiaya);
                    BiayaTotal = totalBiaya;
                    tv_totalBiaya.setText("Rp "+ totalBiaya +"0");
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView judul = (TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        judul.setText("DETAIL PEMESANAN PRODUK");
    }

    private void pembatalanPemesananProduk(String no_transaksi) {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduk = apiService.tampilDetailProduk(no_transaksi);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(CartProdukShow.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtproduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getDetailProduk().isEmpty())
                {
                    for (int i=0; i < response.body().getDetailProduk().size(); i++)
                    {
                        String id_produk = response.body().getDetailProduk().get(i).getId_produk();
                        int stok = response.body().getDetailProduk().get(i).getStok();
                        hapusDetailPenjualanProduk(no_transaksi, id_produk, stok);
                    }
                    hapusTransaksiProduk(no_transaksi);
                    progressDialog.dismiss();
                }
                else
                {
                    hapusTransaksiProduk(no_transaksi);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CartProdukShow.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusTransaksiProduk(String no_transaksi)
    {
        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<Response> transaksiProduk = apiService.batalPenjualanProduk(no_transaksi);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(CartProdukShow.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        transaksiProduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                    Intent i = new Intent(CartProdukShow.this, HomeActivity.class);
                    i.putExtra("status", "penjualan");
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CartProdukShow.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusDetailPenjualanProduk(String no_transaksi, String id_produk, int stok)
    {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduk = apiService.deleteDetailPenjualanProduk(no_transaksi, id_produk);

        dtproduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                System.out.println(response.code());
                if(!response.body().getDetailProduk().isEmpty())
                {
                    String id_produk = response.body().getDetailProduk().get(0).getId_produk();
                    int jumlah = response.body().getDetailProduk().get(0).getJumlah();
                    int stokbaru = stok + jumlah;
                    updateStokProduk(id_produk, stokbaru);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(CartProdukShow.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTransaksiProduk(String no_transaksi, String id_customer, Double totalHarga)
    {
        String idCS = db.getUser(1).getNIP();
        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<Response> transaksiProduk = apiService.updatePenjualanProduk(no_transaksi, totalHarga, id_customer, idCS);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(CartProdukShow.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        transaksiProduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                    for (DetailTransaksiProdukDAO dtp : listDTP)
                    {
                        updateDetailProduk(no_transaksi, dtp.getId_produk(), dtp.getJumlah(), dtp.getStok());
                    }
                    startActivity(new Intent(getApplicationContext(), TampilTransaksiProdukShow.class));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CartProdukShow.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDetailProduk(String no_transaksi, String id_produk, int jumlah, int stok)
    {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduks = apiService.cariDetailProduk(no_transaksi, id_produk);

        dtproduks.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getDetailProduk().isEmpty())
                {
                    int jumlahSebelum = response.body().getDetailProduk().get(0).getJumlah();

                    ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
                    Call<com.petshopfix.API.Response> dtproduk = apiService.updateDetailPenjualanProduk(no_transaksi, id_produk, jumlah);

                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(CartProdukShow.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    dtproduk.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            if (!response.body().getDetailProduk().isEmpty())
                            {
                                int stokbaru = -1;
                                System.out.println("Sebelum " +jumlahSebelum);
                                System.out.println("Stok " + stok);
                                System.out.println("jumlah " +jumlah);

                                if (jumlahSebelum > jumlah)
                                {
                                    stokbaru = stok + (jumlahSebelum-jumlah);
                                    System.out.println("stok baru " + stokbaru);
                                    updateStokProduk(id_produk, stokbaru);
                                }
                                else if (jumlahSebelum < jumlah)
                                {
                                    stokbaru = stok - (jumlah-jumlahSebelum);
                                    System.out.println("stok baru " + stokbaru);
                                    updateStokProduk(id_produk, stokbaru);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(CartProdukShow.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(CartProdukShow.this, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStokProduk(String id_produk, int stok)
    {
        DatabaseHandler db = new DatabaseHandler(CartProdukShow.this);
        String NIP = db.getUser(1).getNIP();
        ApiProduk apiService = ApiClient.getClient().create(ApiProduk.class);
        Call<com.petshopfix.API.Response> produks = apiService.updateStok(id_produk, stok, NIP);

        produks.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getProduk().isEmpty())
                {
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(CartProdukShow.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (status.equals("detail"))
        {
            startActivity(new Intent(getApplicationContext(), TampilTransaksiProdukShow.class));
        }
    }
}
