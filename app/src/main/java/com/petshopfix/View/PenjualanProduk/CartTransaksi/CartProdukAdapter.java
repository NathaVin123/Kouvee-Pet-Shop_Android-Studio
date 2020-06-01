package com.petshopfix.View.PenjualanProduk.CartTransaksi;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiDetailProduk;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailTransaksiProdukDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.ZoomImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CartProdukAdapter extends RecyclerView.Adapter<CartProdukAdapter.cartProdukViewHolder> {

    private List<DetailTransaksiProdukDAO> listDTP;
    private Context context;
    private String no_transaksi, status, nama_customer;
    private OnQuantityChangeListener mListener;

    public CartProdukAdapter(Context context, List<DetailTransaksiProdukDAO> listDTP, String no_transaksi,
                             String status, String nama_customer, OnQuantityChangeListener listener) {
        this.context = context;
        this.listDTP = listDTP;
        this.no_transaksi = no_transaksi;
        this.status = status;
        this.nama_customer = nama_customer;
        mListener = listener;
    }

    @NonNull
    @Override
    public cartProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_cart_produk_adapter, parent, false);
        cartProdukViewHolder viewHolder = new cartProdukViewHolder(view);

        viewHolder.jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double totalBiaya = 0.0;

                System.out.println(listDTP.size());
                for (int i=0; i<listDTP.size(); i++)
                {
                    totalBiaya = totalBiaya + (listDTP.get(i).getJumlah()*listDTP.get(i).getHarga_produk());
                }

                System.out.println(totalBiaya);
                mListener.onQuantityChange(totalBiaya, nama_customer);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cartProdukViewHolder holder, int position) {
        final DetailTransaksiProdukDAO dtp = listDTP.get(position);

        holder.nama_produk.setText(dtp.getNama_produk());
        holder.harga_produk.setText("Rp "+String.valueOf(dtp.getHarga_produk() +"0"));
        holder.stok.setText("Stok "+String.valueOf(dtp.getStok()));
        holder.jumlah.setText(String.valueOf(dtp.getJumlah()));
        holder.tv_subtotal.setText("Rp " +String.valueOf(dtp.getHarga_produk()*dtp.getJumlah())+"0");

        String url = ApiClient.BASE_URL + "produk/" + dtp.getId_produk() + "/gambar";
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.gambar);

        holder.gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle produk = new Bundle();

                produk.putString("id_produk", dtp.getId_produk());
                produk.putString("nama_produk", dtp.getNama_produk());
                produk.putString("status", "produk");

                Intent i = new Intent(context, ZoomImage.class);
                i.putExtras(produk);
                context.startActivity(i);
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusDetailProduk(no_transaksi, dtp.getId_produk(), dtp.getStok());
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minus = Integer.parseInt(holder.jumlah.getText().toString()) - 1;
                int stokAsli = dtp.getStok() + dtp.getJumlah();

                if (minus < 1)
                {
                    minus = 1;
                }

                listDTP.get(position).setJumlah(minus);
                holder.jumlah.setText(String.valueOf(minus));
                holder.stok.setText("Stok "+(stokAsli-minus));
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus = Integer.parseInt(holder.jumlah.getText().toString()) + 1;
                int stokAsli = dtp.getStok() + dtp.getJumlah();

                if (plus > stokAsli)
                {
                    listDTP.get(position).setJumlah(stokAsli);
                    holder.jumlah.setText(String.valueOf(stokAsli));
                    Toast.makeText(context, "Stok yang tersedia hanya "
                            + stokAsli, Toast.LENGTH_SHORT).show();

                    holder.stok.setText("Stok 0");
                }
                else
                {
                    listDTP.get(position).setJumlah(plus);
                    holder.jumlah.setText(String.valueOf(plus));
                    holder.stok.setText("Stok "+ (stokAsli-plus));
                }
            }
        });

        holder.jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!holder.jumlah.getText().toString().isEmpty())
                {
                    int jumlah = Integer.parseInt(holder.jumlah.getText().toString());

                    int stokAsli = dtp.getStok() + dtp.getJumlah();

                    if (jumlah<1)
                    {
                        listDTP.get(position).setJumlah(1);
                        holder.jumlah.setText("1");
                        holder.stok.setText("Stok " + stokAsli);
                    }
                    else if (jumlah > stokAsli)
                    {
                        listDTP.get(position).setJumlah(stokAsli);
                        holder.jumlah.setText(String.valueOf(stokAsli));
                        Toast.makeText(context, "Stok yang tersedia hanya "
                                    + stokAsli, Toast.LENGTH_SHORT).show();

                        holder.stok.setText("Stok 0");
                    }
                    else
                    {
                        holder.stok.setText("Stok " +(stokAsli-jumlah));
                    }

                    int jum = 1;
                    if (!holder.jumlah.getText().toString().isEmpty())
                    {
                        jum = Integer.parseInt(holder.jumlah.getText().toString());
                    }
                    Double subTotal = jum * dtp.getHarga_produk();
                    holder.tv_subtotal.setText("Rp " +String.valueOf(subTotal)+"0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (listDTP != null) ? listDTP.size() : 0;
    }

    public class cartProdukViewHolder extends RecyclerView.ViewHolder {
        private ImageView gambar;
        private TextView nama_produk, harga_produk, stok, tv_subtotal;
        private EditText jumlah;
        private ImageView btnHapus, btnPlus, btnMinus;

        public cartProdukViewHolder(@NonNull View itemView) {
            super(itemView);

            nama_produk = (TextView) itemView.findViewById(R.id.nama_up3);
            harga_produk = (TextView) itemView.findViewById(R.id.harga_up3);
            stok = (TextView) itemView.findViewById(R.id.stok_up3);
            tv_subtotal = (TextView) itemView.findViewById(R.id.subtotal_up3);
            gambar = (ImageView) itemView.findViewById(R.id.gambar_up3);
            jumlah = (EditText) itemView.findViewById(R.id.jumlah_up3);
            btnHapus = (ImageView) itemView.findViewById(R.id.hapus_up3);
            btnMinus = (ImageView) itemView.findViewById(R.id.min_up3);
            btnPlus = (ImageView) itemView.findViewById(R.id.plus_up3);
        }
    }

    private void hapusDetailProduk(String no_transaksi, String id_produk, int stok)
    {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<Response> dtProduk = apiService.deleteDetailPenjualanProduk(no_transaksi, id_produk);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtProduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200)
                {
                    progressDialog.dismiss();

                    String id_produk = response.body().getDetailProduk().get(0).getId_produk();
                    int jumlah = response.body().getDetailProduk().get(0).getJumlah();
                    int stokbaru = stok + jumlah;
                    updateStokProduk(id_produk, stokbaru);

                    Intent i = new Intent(context, CartProdukShow.class);
                    i.putExtra("no_transaksi", no_transaksi);
                    i.putExtra("status", status);
                    context.startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange(Double totalBiaya, String nama_customer);
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
