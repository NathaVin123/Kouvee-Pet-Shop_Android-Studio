package com.petshopfix.View.Produk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.petshopfix.Activity.Produk.DetailProduk;
import com.petshopfix.DAO.ProdukDAO;
import com.petshopfix.R;
import com.petshopfix.ZoomImage;

import java.util.ArrayList;
import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {

    private List<ProdukDAO> produkList;
    private List<ProdukDAO> produkListFiltered;
    private Context context;
    private String status;

    public ProdukAdapter(Context context, List<ProdukDAO> produkList, String status) {
        this.context = context;
        this.produkList = produkList;
        this.produkListFiltered = produkList;
        this.status = status;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_produk_adapter, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukAdapter.ProdukViewHolder holder, int position) {
        final ProdukDAO produks = produkListFiltered.get(position);
        holder.nama_produk.setText          (produks.getNama_produk());
        holder.harga_produk.setText         ("Rp " + String.valueOf(produks.getHarga_produk() + "00"));

        String url = ApiClient.BASE_URL + "produk/" + produks.getId_produk() + "/gambar";
        Glide.with(context)
                .load(url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.gambar);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle produk = new Bundle();

                produk.putString("id_produk", produks.getId_produk());
                produk.putString("nama_produk", produks.getNama_produk());
                produk.putDouble("harga_produk", produks.getHarga_produk());
                produk.putInt("stok", produks.getStok());
                produk.putInt("min_stok", produks.getMin_stok());
                produk.putString("satuan_produk", produks.getSatuan_produk());
                produk.putString("createLog_at", produks.getCreateLog_at());
                produk.putString("updateLog_at", produks.getUpdateLog_at());
                produk.putString("deleteLog_at", produks.getDeleteLog_at());
                produk.putString("updateLogId", produks.getUpdateLogId());
                produk.putString("status", status);

                Intent i = new Intent(context, DetailProduk.class);
                i.putExtras(produk);
                context.startActivity(i);
            }
        });

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
    }


    @Override
    public int getItemCount(){
        return (produkListFiltered != null) ? produkListFiltered.size() : 0;
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        private ImageView gambar;
        private TextView nama_produk, harga_produk, stok;
        private Button btnDetail;

        public ProdukViewHolder(@NonNull View view) {
            super(view);
            nama_produk = (TextView) view.findViewById(R.id.txtNamaProduk);
            harga_produk = (TextView) view.findViewById(R.id.txtHargaProduk);
            gambar = (ImageView) view.findViewById(R.id.ImageViewGambarProduk);
            btnDetail = (Button) view.findViewById(R.id.btnDetail);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userInput = constraint.toString();
                System.out.printf(constraint.toString());
                if (userInput.isEmpty()) {
                    produkListFiltered = produkList;
                }
                else {
                    List<ProdukDAO> filteredList = new ArrayList<>();
                    for (ProdukDAO produk : produkList) {
                        if (produk.getId_produk().toLowerCase().contains(userInput) || produk.getNama_produk().toLowerCase().contains(userInput))
                        {
                            filteredList.add(produk);
                            System.out.println(produk.getNama_produk());
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
}
