package com.petshopfix.View.PenjualanProduk.TambahTransaksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiDetailProduk;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Interface.ApiTransaksiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailTransaksiProdukDAO;
import com.petshopfix.DAO.ProdukDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.PenjualanProduk.CartTransaksi.CartProdukShow;
import com.petshopfix.ZoomImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TambahProdukPenjualanAdapter extends RecyclerView.Adapter<TambahProdukPenjualanAdapter.tambahProdukPenjualanViewHolder> {

    private List<ProdukDAO> produkList;
    private List<ProdukDAO> produkListFiltered;
    private Context context;
    private String status, no_transaksi, cek, nama_customer;

    public TambahProdukPenjualanAdapter(Context context, List<ProdukDAO> produkList, String no_transaksi, String cek, String nama_customer){
        this.context = context;
        this.produkList = produkList;
        this.produkListFiltered = produkList;
        this.no_transaksi = no_transaksi;
        this.cek = cek;
        this.nama_customer = nama_customer;
    }

    @NonNull
    @Override
    public tambahProdukPenjualanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_tambah_produk_penjualan_adapter, parent, false);
        return new tambahProdukPenjualanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tambahProdukPenjualanViewHolder holder, int position) {
        final ProdukDAO produks = produkListFiltered.get(position);
        holder.nama_produk.setText(produks.getNama_produk());
        holder.harga_produk.setText("Harga : Rp "+String.valueOf(produks.getHarga_produk() +"0"));
        holder.stok.setText("Stok "+String.valueOf(produks.getStok()));

        String url = ApiClient.BASE_URL + "produk/" +produks.getId_produk() + "/gambar";
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.gambar);

        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle produk = new Bundle();

                produk.putString("id_produk", produks.getId_produk());
                produk.putString("nama_produk", produks.getNama_produk());
                produk.putString("status", "produk");

                Intent i = new Intent(context, ZoomImage.class);
                i.putExtras(produk);
                context.startActivity(i);
            }
        });

        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
                Call<com.petshopfix.API.Response> dtproduk = apiService.tampilDetailProduk(no_transaksi);

                dtproduk.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        status = "Tidak Ada";
                        DetailTransaksiProdukDAO dtProdukDAO = null;

                        if (!response.body().getDetailProduk().isEmpty() && response.code()==200)
                        {
                            for (DetailTransaksiProdukDAO dtp : response.body().getDetailProduk())
                            {
                                if (dtp.getId_produk().equals(produks.getId_produk()))
                                {
                                    dtProdukDAO = dtp;
                                    status = "Ada";
                                }
                            }
                        }
                        tambahPenjualanProduk(produks, dtProdukDAO);
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return (produkListFiltered != null) ? produkListFiltered.size() : 0;
    }

    public class tambahProdukPenjualanViewHolder extends RecyclerView.ViewHolder {
        private ImageView gambar;
        private TextView nama_produk, harga_produk, stok;
        private Button btnTambah;

        public tambahProdukPenjualanViewHolder(@NonNull View itemView)
        {
            super(itemView);
            nama_produk = (TextView) itemView.findViewById(R.id.txt_namapg);
            harga_produk = (TextView) itemView.findViewById(R.id.txt_hargapg);
            stok = (TextView) itemView.findViewById(R.id.txt_stokpg);
            gambar = (ImageView) itemView.findViewById(R.id.txt_gambarpg);
            btnTambah = (Button) itemView.findViewById(R.id.btn_tambahpp);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userInput = constraint.toString();
                if (userInput.isEmpty()) {
                    produkListFiltered = produkList;
                }
                else
                {
                    List<ProdukDAO> filteredList = new ArrayList<>();
                    for (ProdukDAO produk : produkList) {
                        if (produk.getId_produk().toLowerCase().contains(userInput) || produk.getNama_produk().toLowerCase().contains(userInput))
                        {
                            filteredList.add(produk);
                        }
                    }
                    produkListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = produkListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                produkListFiltered = (ArrayList<ProdukDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }

    public void tambahPenjualanProduk(ProdukDAO produks, DetailTransaksiProdukDAO dtp)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tambah_pengadaan, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        ImageView btnExit = view.findViewById(R.id.btnExit_tp);
        ImageView btnHapus = view.findViewById(R.id.btnHapus_tp);
        ImageView btnMinus = view.findViewById(R.id.min_tp);
        ImageView btnPlus = view.findViewById(R.id.plus_tp);
        Button btnSelesai = view.findViewById(R.id.btnSelesai_tp);
        Button btnTambah = view.findViewById(R.id.btnTambah_tp);
        ImageView txtGambar = view.findViewById(R.id.gambar_tp);
        TextView txtNama = view.findViewById(R.id.nama_tp);
        TextView txtHarga = view.findViewById(R.id.harga_tp);
        TextView txtStok = view.findViewById(R.id.stok_tp);
        TextView txtSubtotal = view.findViewById(R.id.subtotal_tp);
        TextView txtJumlah = view.findViewById(R.id.jumlah_tp);

        txtNama.setText(produks.getNama_produk());
        txtHarga.setText("Rp "+String.valueOf(produks.getHarga_produk() +"0"));

        if (status.equals("Tidak Ada"))
        {
            txtJumlah.setText("1");
            txtSubtotal.setText(produks.getHarga_produk()+"0");
            btnHapus.setVisibility(View.INVISIBLE);
            txtStok.setText("Stok "+String.valueOf(produks.getStok()-1));
        }
        else
        {
            txtJumlah.setText(String.valueOf(dtp.getJumlah()));
            txtSubtotal.setText(String.valueOf(dtp.getHarga_produk()*dtp.getJumlah())+"0");
            txtStok.setText("Stok "+String.valueOf(produks.getStok()));
        }

        String url = ApiClient.BASE_URL + "produk/" + produks.getId_produk() + "/gambar";
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(txtGambar);

        txtJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtJumlah.getText().toString().isEmpty())
                {
                    int jumlah = Integer.parseInt(txtJumlah.getText().toString());

                    if (cek.equals("tambah") && status.equals("Tidak Ada"))
                    {
                        if (jumlah<1)
                        {
                            txtJumlah.setText("1");
                            txtStok.setText("Stok "+produks.getStok());
                        }
                        else if (jumlah>produks.getStok())
                        {
                            txtJumlah.setText(String.valueOf(produks.getStok()));
                            Toast.makeText(context, "Stok yang tersedia hanya " +
                                    String.valueOf(produks.getStok()), Toast.LENGTH_SHORT).show();

                            txtStok.setText("Stok 0");
                        }
                        else
                        {
                            txtStok.setText("Stok "+(produks.getStok()-jumlah));
                        }
                    }
                    else if (cek.equals("detail") || status.equals("Ada"))
                    {
                        int stokAsli = produks.getStok() + dtp.getJumlah();

                        if (jumlah<1)
                        {
                            txtJumlah.setText("1");
                            txtStok.setText("Stok" + stokAsli);
                        }
                        else if (jumlah>stokAsli)
                        {
                            txtJumlah.setText(String.valueOf(stokAsli));
                            Toast.makeText(context, "Stok yang tersedia hanya "
                                    +stokAsli, Toast.LENGTH_SHORT).show();

                            txtStok.setText("Stok 0");
                        }
                        else
                        {
                            txtStok.setText("Stok "+(stokAsli-jumlah));
                        }
                    }

                    int jum = 1;
                    if (!txtJumlah.getText().toString().isEmpty())
                    {
                        jum = Integer.parseInt(txtJumlah.getText().toString());
                    }
                    Double subTotal = jum * produks.getHarga_produk();
                    txtSubtotal.setText(String.valueOf(subTotal)+"0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minus = Integer.parseInt(txtJumlah.getText().toString()) - 1;

                if (cek.equals("tambah") && status.equals("Tidak Ada"))
                {
                    if (minus < 1)
                        txtJumlah.setText("1");
                    else
                        txtJumlah.setText(String.valueOf(minus));

                    txtStok.setText("Stok "+(produks.getStok() - Integer.parseInt(txtJumlah.getText().toString())));
                }
                else if (cek.equals("detail") || status.equals("Ada"))
                {
                    int stokAsli = produks.getStok() + dtp.getJumlah();

                    if (minus < 1)
                    {
                        minus = 1;
                    }

                    txtJumlah.setText(String.valueOf(minus));
                    txtStok.setText("Stok " +(stokAsli-minus));
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus = Integer.parseInt(txtJumlah.getText().toString()) + 1;

                if (cek.equals("tambah") && status.equals("Tidak Ada"))
                {
                    if (plus > produks.getStok())
                    {
                        txtJumlah.setText(String.valueOf(produks.getStok()));
                        Toast.makeText(context, "Stok yang tersedia hanya " +
                                String.valueOf(produks.getStok()), Toast.LENGTH_SHORT).show();

                        txtStok.setText("Stok 0");
                    }
                    else
                    {
                        System.out.println(plus);
                        txtJumlah.setText(String.valueOf(plus));
                        txtStok.setText("Stok "+(produks.getStok()-plus));
                    }
                }
                else if (cek.equals("detail") || status.equals("Ada"))
                {
                    int stokAsli = produks.getStok() + dtp.getJumlah();

                    if (plus > stokAsli)
                    {
                        txtJumlah.setText(String.valueOf(stokAsli));
                        Toast.makeText(context, "Stok yang tersedia hanya "
                                + stokAsli, Toast.LENGTH_SHORT).show();

                        txtStok.setText("Stok 0");
                    }
                    else
                    {
                        txtJumlah.setText(String.valueOf(plus));
                        txtStok.setText("Stok "+ (stokAsli-plus));
                    }
                }
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusDetailPenjualanProduk(no_transaksi, produks.getId_produk(), produks.getStok());

                Intent i = new Intent(context, TambahProdukPenjualanShow.class);
                i.putExtra("no_transaksi", no_transaksi);
                i.putExtra("status", cek);
                i.putExtra("nama_customer", nama_customer);
                context.startActivity(i);
            }
        });

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idP = produks.getId_produk();
                int jumlah = Integer.parseInt(txtJumlah.getText().toString());
                int stok = produks.getStok();

                simpanDetailPenjualanProduk(no_transaksi, idP, jumlah, stok, "Selesai");
                //hitungTotalBiaya(no_transaksi)
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idP = produks.getId_produk();
                int jumlah = Integer.parseInt(txtJumlah.getText().toString());
                int stok = produks.getStok();

                simpanDetailPenjualanProduk(no_transaksi, idP, jumlah, stok, "tambah");
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TambahProdukPenjualanShow.class);
                i.putExtra("no_transaksi", no_transaksi);
                i.putExtra("status", cek);
                i.putExtra("nama_customer", nama_customer);
                context.startActivity(i);
            }
        });

        alertD.setView(view);
        alertD.show();
    }

    private void simpanDetailPenjualanProduk(String no_transaksi, String id_produk, int jumlah, int stok, String set)
    {
        if (status.equals("Tidak Ada"))
        {
            ApiDetailProduk apiService= ApiClient.getClient().create(ApiDetailProduk.class);
            Call<com.petshopfix.API.Response> dtproduk = apiService.createDetailPenjualanProduk(no_transaksi, id_produk, jumlah);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            dtproduk.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    progressDialog.dismiss();
                    if (!response.body().getDetailProduk().isEmpty())
                    {
                        int stokBaru = stok - jumlah;
                        System.out.println(stokBaru);

                        updateStokProduk(id_produk, stokBaru);

                        if (set.equals("tambah"))
                        {
                            Intent i = new Intent(context, TambahProdukPenjualanShow.class);
                            i.putExtra("no_transaksi", no_transaksi);
                            i.putExtra("status", cek);
                            i.putExtra("nama_customer", nama_customer);
                            context.startActivity(i);
                        }
                        else
                        {
                            Intent i = new Intent(context, CartProdukShow.class);
                            i.putExtra("no_transaksi", no_transaksi);
                            i.putExtra("status", "tambah");
                            i.putExtra("nama_customer", nama_customer);
                            context.startActivity(i);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
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
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Loading....");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        dtproduk.enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                progressDialog.dismiss();
                                if (!response.body().getDetailProduk().isEmpty())
                                {
                                    int stokBaru = -1;
                                    System.out.println("sebelum " +jumlahSebelum);
                                    System.out.println("stok " + stok);
                                    System.out.println("jumlah " + jumlah);

                                    if (jumlahSebelum > jumlah)
                                    {
                                        stokBaru = stok + (jumlahSebelum-jumlah);
                                        System.out.println("stok baru " + stokBaru);
                                        updateStokProduk(id_produk, stokBaru);
                                    }
                                    else if (jumlahSebelum < jumlah)
                                    {
                                        stokBaru = stok - (jumlah-jumlahSebelum);
                                        System.out.println("stok baru " + stokBaru);
                                        updateStokProduk(id_produk, stokBaru);
                                    }

                                    if (set.equals("tambah"))
                                    {
                                        Intent i = new Intent(context, TambahProdukPenjualanShow.class);
                                        i.putExtra("no_transaksi", no_transaksi);
                                        i.putExtra("status", cek);
                                        i.putExtra("nama_customer", nama_customer);
                                        context.startActivity(i);
                                    }
                                    else
                                    {
                                        Intent i = new Intent(context, CartProdukShow.class);
                                        i.putExtra("no_transaksi", no_transaksi);
                                        i.putExtra("status", "tambah");
                                        i.putExtra("nama_customer", nama_customer);
                                        context.startActivity(i);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void hitungTotalBiaya(String no_transaksi)
    {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduk = apiService.tampilDetailProduk(no_transaksi);

        dtproduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getDetailProduk().isEmpty())
                {
                    Double totalBiaya = 0.0;
                    for (int i=0; i < response.body().getDetailProduk().size() ; i++)
                    {
                        totalBiaya = totalBiaya + (response.body().getDetailProduk().get(i).getJumlah()*
                                response.body().getDetailProduk().get(i).getHarga_produk());
                    }
                    updateTotalTransaksiProduk(no_transaksi, totalBiaya);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalTransaksiProduk(String no_transaksi, Double totalBiaya)
    {
        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<com.petshopfix.API.Response> transaksiProduk = apiService.updateTotalBiayaProduk(no_transaksi, totalBiaya);

        transaksiProduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200)
                {
                    System.out.println("Success");

                    Intent i = new Intent(context, CartProdukShow.class);
                    i.putExtra("no_transaksi", no_transaksi);
                    i.putExtra("status", "tambah");
                    i.putExtra("nama_customer", nama_customer);
                    context.startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusDetailPenjualanProduk(String no_transaksi, String id_produk, int stok)
    {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduk = apiService.deleteDetailPenjualanProduk(no_transaksi, id_produk);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtproduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (!response.body().getDetailProduk().isEmpty())
                {
                    String id_produk = response.body().getDetailProduk().get(0).getId_produk();
                    int jumlah = response.body().getDetailProduk().get(0).getJumlah();
                    int stokbaru = stok + jumlah;
                    updateStokProduk(id_produk, stokbaru);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStokProduk(String id_produk, int stok)
    {
        DatabaseHandler db = new DatabaseHandler(context);
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
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
