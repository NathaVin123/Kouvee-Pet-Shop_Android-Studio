package com.petshopfix.View.Supplier;

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
import com.petshopfix.Activity.Supplier.DetailSupplier;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {

    private List<SupplierDAO> supplierList;
    private List<SupplierDAO> supplierListFiltered;
    private Context context;
    private String status;

    public SupplierAdapter(Context context, List<SupplierDAO> supplierList, String status)
    {
        this.context = context;
        this.supplierList = supplierList;
        this.supplierListFiltered = supplierList;
        this.status = status;
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_supplier_adapter, parent, false);
        return new SupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.SupplierViewHolder holder, int position) {
        final SupplierDAO suppliers = supplierListFiltered.get(position);
        holder.nama_supplier.setText            (suppliers.getNama_supplier());
        holder.alamat_supplier.setText          (suppliers.getAlamat_supplier());
        holder.stok.setText                     (String.valueOf(suppliers.getStok()));

        String url = ApiClient.BASE_URL + "supplier/" + suppliers.getId_supplier();
        Glide.with(context)
                .load(url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle supplier = new Bundle();

                supplier.putInt("id_supplier", suppliers.getId_supplier());
                supplier.putString("nama_supplier", suppliers.getNama_supplier());
                supplier.putString("alamat_supplier", suppliers.getAlamat_supplier());
                supplier.putString("noTelp_supplier", suppliers.getNoTelp_supplier());
                supplier.putInt("stok", suppliers.getStok());
                supplier.putString("createLog_at", suppliers.getCreateLog_at());
                supplier.putString("updateLog_at", suppliers.getUpdateLog_at());
                supplier.putString("deleteLog_at", suppliers.getDeleteLog_at());
                supplier.putString("updateLogId", suppliers.getUpdateLogId());
                supplier.putString("status",status);

                Intent i = new Intent(context, DetailSupplier.class);
                i.putExtras(supplier);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (supplierListFiltered != null) ? supplierListFiltered.size() : 0;
    }

    public class SupplierViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_supplier, alamat_supplier, stok;
        private Button btnDetail;

        public SupplierViewHolder(@NonNull View view)
        {
            super(view);
            nama_supplier = (TextView) view.findViewById(R.id.txtNamaSupplier);
            alamat_supplier = (TextView) view.findViewById(R.id.txtAlamatSupplier);
            stok = (TextView) view.findViewById(R.id.txtStokSupplier);
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
                    supplierListFiltered = supplierList;
                }
                else
                {
                    List<SupplierDAO> filteredList = new ArrayList<>();
                    for (SupplierDAO supplier : supplierList) {
                        if (String.valueOf(supplier.getId_supplier()).toLowerCase().contains(userInput) || supplier.getNama_supplier().toLowerCase().contains(userInput))
                        {
                            filteredList.add(supplier);
                            System.out.println(supplier.getNama_supplier());
                        }
                    }
                    supplierListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = supplierListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                supplierListFiltered = (ArrayList<SupplierDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
