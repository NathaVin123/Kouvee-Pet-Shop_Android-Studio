package com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu;

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
import com.petshopfix.API.Interface.ApiDetailPengadaan;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UpdatePengadaanAdapter extends RecyclerView.Adapter<UpdatePengadaanAdapter.tambahPengadaanViewHolder> {

    private List<DetailPengadaanDAO> listDTP;
    private Context context;
    private String nomorPO;

    public UpdatePengadaanAdapter(Context context, List<DetailPengadaanDAO> listDTP, String nomorPO) {
        this.context=context;
        this.listDTP = listDTP;
        this.nomorPO = nomorPO;
    }

    @NonNull
    @Override
    public tambahPengadaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_update_pengadaan_adapter, parent, false);
        return new tambahPengadaanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tambahPengadaanViewHolder holder, int position) {
        final DetailPengadaanDAO dtp = listDTP.get(position);

        holder.namaProduk.setText(dtp.getNama_produk());
        holder.harga.setText("Rp "+String.valueOf(dtp.getHarga_produk() +"0"));
        holder.stok.setText("Stok "+String.valueOf(dtp.getStok()) + " | " +String.valueOf(dtp.getMin_stok()));
        holder.satuan.setText(dtp.getSatuan());
        holder.jumlah.setText(String.valueOf(dtp.getJumlah_po()));

//        String url = ApiClient.BASE_URL +  "produk/" + dtp.getIdProduk() + "/gambar";
//        Glide.with(context)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(holder.gambar);
//
//        holder.gambar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle produk = new Bundle();
//
//                produk.putString("idProduk", dtp.getIdProduk());
//                produk.putString("namaProduk",dtp.getNamaProduk());
//                produk.putString("status", "produk");
//
//                Intent i = new Intent(context, ZoomImage.class);
//                i.putExtras(produk);
//                context.startActivity(i);
//            }
//        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusDetailPengadaan(nomorPO, dtp.getId_produk());
            }
        });

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minus = Integer.parseInt(holder.jumlah.getText().toString()) - 1;
                listDTP.get(position).setJumlah_po(minus);
                if(minus < 1)
                    holder.jumlah.setText("1");
                else
                    holder.jumlah.setText(String.valueOf(minus));
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus = Integer.parseInt(holder.jumlah.getText().toString()) + 1;
                listDTP.get(position).setJumlah_po(plus);
                if(plus < 1)
                    holder.jumlah.setText("1");
                else
                    holder.jumlah.setText(String.valueOf(plus));
            }
        });

        holder.satuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listDTP.get(position).setSatuan(dtp.getSatuan());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listDTP != null) ? listDTP.size() : 0;
    }

    public class tambahPengadaanViewHolder extends RecyclerView.ViewHolder{
        private ImageView gambar;
        private TextView namaProduk, harga, stok;
        private EditText satuan, jumlah;
        private ImageView btnHapus, btnPlus, btnMinus;

        public tambahPengadaanViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProduk = (TextView) itemView.findViewById(R.id.nama_up);
            harga = (TextView) itemView.findViewById(R.id.harga_up);
            stok = (TextView) itemView.findViewById(R.id.stok_up);
//            gambar = (ImageView) itemView.findViewById(R.id.gambar_up);
            satuan = (EditText) itemView.findViewById(R.id.satuan_up);
            jumlah = (EditText) itemView.findViewById(R.id.jumlah_up);
            btnHapus = (ImageView) itemView.findViewById(R.id.hapus_up);
            btnMinus = (ImageView) itemView.findViewById(R.id.min_up);
            btnPlus = (ImageView) itemView.findViewById(R.id.plus_up);
        }
    }

    private void hapusDetailPengadaan(String nomorPO, String id_produk)
    {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<com.petshopfix.API.Response> dtpengadaan = apiService.batalProdukPengadaan(nomorPO,id_produk);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtpengadaan.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    Intent i = new Intent(context, UpdatePengadaanShow.class);
                    i.putExtra("nomorPO", nomorPO);
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
}
