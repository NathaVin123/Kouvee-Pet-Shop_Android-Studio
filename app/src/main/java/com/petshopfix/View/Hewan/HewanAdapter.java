package com.petshopfix.View.Hewan;

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
import com.petshopfix.Activity.Hewan.DetailHewan;
import com.petshopfix.DAO.HewanDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

public class HewanAdapter extends RecyclerView.Adapter<HewanAdapter.HewanViewHolder> {

    private List<HewanDAO> hewanList;
    private List<HewanDAO> hewanListFiltered;
    private Context context;
    private String status;

    public HewanAdapter(Context context, List<HewanDAO> hewanList, String status)
    {
        this.context = context;
        this.hewanList = hewanList;
        this.hewanListFiltered = hewanList;
        this.status = status;
    }

    @NonNull
    @Override
    public HewanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_hewan_adapter, parent, false);
        return new HewanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HewanAdapter.HewanViewHolder holder, int position)
    {
        final HewanDAO hewans = hewanListFiltered.get(position);
        holder.nama_hewan.setText         (hewans.getNama_hewan());

        String url = ApiClient.BASE_URL + "hewan/" + hewans.getId_hewan();
        Glide.with(context)
                .load(url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle hewan = new Bundle();

                hewan.putInt("id_hewan", hewans.getId_hewan());
                hewan.putString("nama_hewan", hewans.getNama_hewan());
                hewan.putString("tglLahir_hewan", hewans.getTglLahir_hewan());
                hewan.putInt("id_jenis", hewans.getId_jenis());
                hewan.putInt("id_customer", hewans.getId_customer());
                hewan.putString("createLog_at", hewans.getCreateLog_at());
                hewan.putString("updateLog_at", hewans.getUpdateLog_at());
                hewan.putString("deleteLog_at", hewans.getDeleteLog_at());
                hewan.putString("updateLogId", hewans.getUpdateLogId());
                hewan.putString("status",status);

                Intent i = new Intent(context, DetailHewan.class);
                i.putExtras(hewan);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (hewanListFiltered != null) ? hewanListFiltered.size() : 0;
    }

    public class HewanViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_hewan;
        private Button btnDetail;

        public HewanViewHolder(@NonNull View view)
        {
            super(view);
            nama_hewan = (TextView) view.findViewById(R.id.txtNamaHewan);
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
                    hewanListFiltered = hewanList;
                }
                else
                {
                    List<HewanDAO> filteredList = new ArrayList<>();
                    for (HewanDAO hewan : hewanList) {
                        if (String.valueOf(hewan.getId_hewan()).toLowerCase().contains(userInput) || hewan.getNama_hewan().toLowerCase().contains(userInput))
                        {
                            filteredList.add(hewan);
                            System.out.println(hewan.getNama_hewan());
                        }
                    }
                    hewanListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = hewanListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                hewanListFiltered = (ArrayList<HewanDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
