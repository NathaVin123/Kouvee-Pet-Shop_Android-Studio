package com.petshopfix.View.Ukuran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiUkuran;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Ukuran.UpdateUkuran;
import com.petshopfix.DAO.UkuranDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class UkuranAdapter extends RecyclerView.Adapter<UkuranAdapter.UkuranViewHolder> {

    private List<UkuranDAO> ukuranlist;
    private List<UkuranDAO> ukuranListFiltered;
    private Context context;
    private String status;

    public UkuranAdapter(Context context, List<UkuranDAO> ukuranlist, String status)
    {
        this.context = context;
        this.ukuranlist = ukuranlist;
        this.ukuranListFiltered = ukuranlist;
        this.status = status;
    }

    @NonNull
    @Override
    public UkuranAdapter.UkuranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_ukuran_adapter, parent, false);
        return new UkuranAdapter.UkuranViewHolder(view);
    }

    public void onBindViewHolder(@NonNull UkuranAdapter.UkuranViewHolder holder, int position) {
        final UkuranDAO ukurans = ukuranListFiltered.get(position);
        holder.id_ukuran.setText            ("ID Ukuran : " + String.valueOf(ukurans.getId_ukuran()));
        holder.nama_ukuran.setText          (ukurans.getNama_ukuran());
        holder.createLog_at.setText          ("Tanggal Dibuat       : " + ukurans.getCreateLog_at());
        holder.updateLog_at.setText          ("Tanggal Diubah       : " + ukurans.getUpdateLog_at());
        holder.deleteLog_at.setText          ("Tanggal Dihapus       : " + ukurans.getDeleteLog_at());
        holder.updateLogId.setText          ("NIP LOG ID       : " + ukurans.getUpdateLogId());

        holder.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("getAll"))
                {
                    Bundle ukuran = new Bundle();

                    ukuran.putInt("id_ukuran", ukurans.getId_ukuran());
                    ukuran.putString("nama_ukuran", ukurans.getNama_ukuran());
                    ukuran.putString("createLog_at", ukurans.getCreateLog_at());
                    ukuran.putString("updateLog_at", ukurans.getUpdateLog_at());
                    ukuran.putString("deleteLog_at", ukurans.getDeleteLog_at());
                    ukuran.putString("updateLogId", ukurans.getUpdateLogId());
                    ukuran.putString("status", status);

                    Intent i = new Intent(context, UpdateUkuran.class);
                    i.putExtras(ukuran);
                    context.startActivity(i);
                }
                else
                {
                    ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
                    Call<Response> ukuran = apiService.restoreUkuran(ukurans.getId_ukuran(),ukurans.getUpdateLogId());

                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Memulihkan Data Ukuran");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it
                    progressDialog.show();
                    ukuran.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            System.out.printf(String.valueOf(response.code()));
                            if(response.code() == 200)
                            {
                                Toast.makeText(context,"Data Ukuran Berhasil Dipulihkan", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, ListUkuran.class);
                                i.putExtra("status",status );
                                context.startActivity(i);
                            }
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);

                if (status.equals("getAll"))
                {
                    builder.setMessage("Anda Yakin Ingin Menghapus Ukuran ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
                            Call<Response> ukuran = apiService.deleteUkuran(ukurans.getId_ukuran(),ukurans.getUpdateLogId());

                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading....");
                            progressDialog.setTitle("Menghapus Data Ukuran");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            // show it
                            progressDialog.show();
                            ukuran.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    progressDialog.dismiss();
                                    if(response.code() == 200)
                                    {
                                        Toast.makeText(context,"Data Ukuran Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, ListUkuran.class);
                                        i.putExtra("status",status );
                                        context.startActivity(i);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(context,"Data Ukuran Ini Tidak Dapat Dihapus", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    builder.setMessage("Anda Yakin Ingin Menghapus Ukuran Secara Permanen ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiUkuran apiService = ApiClient.getClient().create(ApiUkuran.class);
                            Call<Response> ukuran = apiService.deleteUkuranPermanen(ukurans.getId_ukuran());

                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading....");
                            progressDialog.setTitle("Menghapus Data Ukuran Permanen");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            // show it
                            progressDialog.show();
                            ukuran.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    progressDialog.dismiss();
                                    if(response.code() == 200)
                                    {
                                        Toast.makeText(context,"Data Ukuran Berhasil Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, ListUkuran.class);
                                        i.putExtra("status",status );
                                        context.startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Data Ukuran Ini Tidak Dapat Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(context,t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return (ukuranListFiltered != null) ? ukuranListFiltered.size() : 0;
    }

    public class UkuranViewHolder extends RecyclerView.ViewHolder {
        private TextView id_ukuran, nama_ukuran, createLog_at, updateLog_at, deleteLog_at, updateLogId;
        private Button btnUbah, btnHapus;

        public UkuranViewHolder(@NonNull View view) {
            super(view);
            id_ukuran = (TextView) view.findViewById(R.id.tv_idUkuran);
            nama_ukuran = (TextView) view.findViewById(R.id.tv_namaUkuran);
            createLog_at = (TextView) view.findViewById(R.id.tv_createAt);
            updateLog_at = (TextView) view.findViewById(R.id.tv_updateAt);
            deleteLog_at = (TextView) view.findViewById(R.id.tv_deleteAt);
            updateLogId = (TextView) view.findViewById(R.id.tv_updateId);

            btnUbah = (Button) view.findViewById(R.id.btnUbah);
            btnHapus = (Button) view.findViewById(R.id.btnHapus);

            if (status.equals("getAll"))
                btnUbah.setText("UPDATE");
            else
                btnUbah.setText("RESTORE");
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userInput = constraint.toString();
                System.out.printf(constraint.toString());
                if (userInput.isEmpty()) {
                    ukuranListFiltered = ukuranlist;
                }else{
                    List<UkuranDAO> filteredList = new ArrayList<>();
                    for (UkuranDAO ukuran : ukuranlist) {
                        if(String.valueOf(ukuran.getId_ukuran()).toLowerCase().contains(userInput) || ukuran.getNama_ukuran().toLowerCase().contains(userInput)){
                            filteredList.add(ukuran);
                            System.out.println(ukuran.getNama_ukuran());
                        }
                    }
                    ukuranListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ukuranListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ukuranListFiltered = (ArrayList<UkuranDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
