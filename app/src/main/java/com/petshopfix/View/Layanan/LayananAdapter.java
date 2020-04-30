package com.petshopfix.View.Layanan;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.petshopfix.Activity.Layanan.DetailLayanan;
import com.petshopfix.DAO.LayananDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.LayananViewHolder> {

    private List<LayananDAO> layananList;
    private List<LayananDAO> layananListFiltered;
    private Context context;
    private String status;

    public LayananAdapter(Context context, List<LayananDAO> layananList, String status)
    {
        this.context = context;
        this.layananList = layananList;
        this.layananListFiltered = layananList;
        this.status = status;
    }

    @NonNull
    @Override
    public LayananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_layanan_adapter, parent, false);
        return new LayananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LayananAdapter.LayananViewHolder holder, int position) {
        final LayananDAO layanans = layananListFiltered.get(position);
        holder.nama_layanan.setText         (layanans.getNama_layanan());
        holder.harga_layanan.setText        ("Rp "+String.valueOf(layanans.getHarga_layanan() +"0"));
        holder.nama_ukuran.setText          (layanans.getNama_ukuran());

        String url = ApiClient.BASE_URL + "layanan/" + layanans.getId_layanan();
        Glide.with(context)
                .load(url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle layanan = new Bundle();

                layanan.putString("id_layanan", layanans.getId_layanan());
                layanan.putString("nama_layanan", layanans.getNama_layanan());
                layanan.putDouble("harga_layanan", layanans.getHarga_layanan());
                layanan.putInt("id_ukuran", layanans.getId_ukuran());
                layanan.putString("createLog_at", layanans.getCreateLog_at());
                layanan.putString("updateLog_at", layanans.getUpdateLog_at());
                layanan.putString("deleteLog_at", layanans.getDeleteLog_at());
                layanan.putString("updateLogId", layanans.getUpdateLogId());
                layanan.putString("status",status);

                Intent i = new Intent(context, DetailLayanan.class);
                i.putExtras(layanan);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (layananListFiltered != null) ? layananListFiltered.size() : 0;
    }

    public class LayananViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_layanan, harga_layanan, nama_ukuran;
        private Button btnDetail;

        public LayananViewHolder(@NonNull View view)
        {
            super(view);
            nama_layanan = (TextView) view.findViewById(R.id.txtNamaLayanan);
            harga_layanan = (TextView) view.findViewById(R.id.txtHargaLayanan);
            nama_ukuran = (TextView) view.findViewById(R.id.txtNamaUkuran);
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
                    layananListFiltered = layananList;
                }
                else
                {
                    List<LayananDAO> filteredList = new ArrayList<>();
                    for (LayananDAO layanan : layananList) {
                        if (layanan.getId_layanan().toLowerCase().contains(userInput) || layanan.getNama_layanan().toLowerCase().contains(userInput))
                        {
                            filteredList.add(layanan);
                            System.out.println(layanan.getNama_layanan());
                        }
                    }
                    layananListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = layananListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                layananListFiltered = (ArrayList<LayananDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
