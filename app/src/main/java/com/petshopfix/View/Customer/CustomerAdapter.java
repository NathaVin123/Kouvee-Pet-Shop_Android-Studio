package com.petshopfix.View.Customer;

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
import com.petshopfix.Activity.Customer.DetailCustomer;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<CustomerDAO> customerList;
    private List<CustomerDAO> customerListFiltered;
    private Context context;
    private String status;

    public CustomerAdapter(Context context, List<CustomerDAO> customerList, String status)
    {
        this.context = context;
        this.customerList = customerList;
        this.customerListFiltered = customerList;
        this.status = status;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_customer_adapter, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder holder, int position)
    {
        final CustomerDAO customers = customerListFiltered.get(position);
        holder.nama_customer.setText            (customers.getNama_customer());
        holder.alamat_customer.setText          (customers.getAlamat_customer());
        holder.noTelp_customer.setText          (customers.getNoTelp_customer());

        String url = ApiClient.BASE_URL + "customer/" + customers.getId_customer();
        Glide.with(context)
                .load(url)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle customer = new Bundle();

                customer.putInt("id_customer", customers.getId_customer());
                customer.putString("nama_customer", customers.getNama_customer());
                customer.putString("alamat_customer", customers.getAlamat_customer());
                customer.putString("tglLahir_customer", customers.getTglLahir_customer());
                customer.putString("noTelp_customer", customers.getNoTelp_customer());
                customer.putString("createLog_at", customers.getCreateLog_at());
                customer.putString("updateLog_at", customers.getUpdateLog_at());
                customer.putString("deleteLog_at", customers.getDeleteLog_at());
                customer.putString("updateLogId", customers.getUpdateLogId());
                customer.putString("status",status);

                Intent i = new Intent(context, DetailCustomer.class);
                i.putExtras(customer);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (customerListFiltered != null) ? customerListFiltered.size() : 0;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_customer, alamat_customer, noTelp_customer;
        private Button btnDetail;

        public CustomerViewHolder(@NonNull View view)
        {
            super(view);
            nama_customer = (TextView) view.findViewById(R.id.txtNamaCustomer);
            alamat_customer = (TextView) view.findViewById(R.id.txtAlamatCustomer);
            noTelp_customer = (TextView) view.findViewById(R.id.txtTelpCustomer);
            btnDetail = (Button) view.findViewById(R.id.btnDetail);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userInput = constraint.toString();
                System.out.printf(constraint.toString());
                if (userInput.isEmpty())
                {
                    customerListFiltered = customerList;
                }
                else
                {
                    List<CustomerDAO> filteredList = new ArrayList<>();
                    for (CustomerDAO customer : customerList) {
                        if (String.valueOf(customer.getId_customer()).toLowerCase().contains(userInput) || customer.getNama_customer().toLowerCase().contains(userInput))
                        {
                            filteredList.add(customer);
                            System.out.println(customer.getNama_customer());
                        }
                    }
                    customerListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                customerListFiltered = (ArrayList<CustomerDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
