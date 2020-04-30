package com.petshopfix.View.Jenis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.petshopfix.API.Interface.ApiJenis;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Jenis.UpdateJenis;
import com.petshopfix.DAO.JenisDAO;
import com.petshopfix.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class JenisAdapter extends RecyclerView.Adapter<JenisAdapter.JenisViewHolder> {

    private List<JenisDAO> jenislist;
    private List<JenisDAO> jenisListFiltered;
    private Context context;
    private String status;

    public JenisAdapter(Context context, List<JenisDAO> jenislist, String status)
    {
        this.context = context;
        this.jenislist = jenislist;
        this.jenisListFiltered = jenislist;
        this.status = status;
    }

    @NonNull
    @Override
    public JenisAdapter.JenisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_jenis_adapter, parent, false);
        return new JenisAdapter.JenisViewHolder(view);
    }

    public void onBindViewHolder(@NonNull JenisAdapter.JenisViewHolder holder, int position) {
        final JenisDAO jeniss = jenisListFiltered.get(position);
        holder.id_jenis.setText            ("ID Jenis : " + String.valueOf(jeniss.getId_jenis()));
        holder.nama_jenis.setText          (jeniss.getNama_jenis());
        holder.createLog_at.setText          ("Tanggal Dibuat       : " + jeniss.getCreateLog_at());
        holder.updateLog_at.setText          ("Tanggal Diubah       : " + jeniss.getUpdateLog_at());
        holder.deleteLog_at.setText          ("Tanggal Dihapus       : " + jeniss.getDeleteLog_at());
        holder.updateLogId.setText          ("NIP LOG ID       : " + jeniss.getUpdateLogId());

        holder.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("getAll"))
                {
                    Bundle jenis = new Bundle();

                    jenis.putInt("id_jenis", jeniss.getId_jenis());
                    jenis.putString("nama_jenis", jeniss.getNama_jenis());
                    jenis.putString("createLog_at", jeniss.getCreateLog_at());
                    jenis.putString("updateLog_at", jeniss.getUpdateLog_at());
                    jenis.putString("deleteLog_at", jeniss.getDeleteLog_at());
                    jenis.putString("updateLogId", jeniss.getUpdateLogId());
                    jenis.putString("status", status);

                    Intent i = new Intent(context, UpdateJenis.class);
                    i.putExtras(jenis);
                    context.startActivity(i);
                }
                else
                {
                    ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
                    Call<Response> jenis = apiService.restoreJenis(jeniss.getId_jenis(),jeniss.getUpdateLogId());

                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Loading....");
                    progressDialog.setTitle("Memulihkan Data Jenis");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    // show it

                    progressDialog.show();
                    jenis.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            progressDialog.dismiss();
                            System.out.printf(String.valueOf(response.code()));
                            if(response.code() == 200)
                            {
                                Toast.makeText(context,"Data Jenis Berhasil Dipulihkan", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, ListJenis.class);
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
                    builder.setMessage("Anda Yakin Ingin Menghapus Jenis ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
                            Call<Response> jenis = apiService.deleteJenis(jeniss.getId_jenis(),jeniss.getUpdateLogId());

                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading....");
                            progressDialog.setTitle("Menghapus Data Jenis");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            // show it
                            progressDialog.show();
                            jenis.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    progressDialog.dismiss();
                                    if(response.code() == 200)
                                    {
                                        Toast.makeText(context,"Data Jenis Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, ListJenis.class);
                                        i.putExtra("status",status );
                                        context.startActivity(i);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(context,"Data Jenis Ini Tidak Dapat Dihapus", Toast.LENGTH_SHORT).show();
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
                    builder.setMessage("Anda Yakin Ingin Menghapus Jenis Secara Permanen ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ApiJenis apiService = ApiClient.getClient().create(ApiJenis.class);
                            Call<Response> jenis = apiService.deleteJenisPermanen(jeniss.getId_jenis());

                            final ProgressDialog progressDialog;
                            progressDialog = new ProgressDialog(context);
                            progressDialog.setMessage("Loading....");
                            progressDialog.setTitle("Menghapus Data Jenis Permanen");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            // show it
                            progressDialog.show();
                            jenis.enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    progressDialog.dismiss();
                                    if(response.code() == 200)
                                    {
                                        Toast.makeText(context,"Data Jenis Berhasil Dihapus Permanen.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context, ListJenis.class);
                                        i.putExtra("status",status );
                                        context.startActivity(i);
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Data Jenis Ini Tidak Dapat Dihapus Permanen.", Toast.LENGTH_SHORT).show();
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
        return (jenisListFiltered != null) ? jenisListFiltered.size() : 0;
    }

    public class JenisViewHolder extends RecyclerView.ViewHolder {
        private TextView id_jenis, nama_jenis, createLog_at, updateLog_at, deleteLog_at, updateLogId;
        private Button btnUbah, btnHapus;

        public JenisViewHolder(@NonNull View view) {
            super(view);
            id_jenis = (TextView) view.findViewById(R.id.tv_idJenis);
            nama_jenis = (TextView) view.findViewById(R.id.tv_namaJenis);
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
                    jenisListFiltered = jenislist;
                }else{
                    List<JenisDAO> filteredList = new ArrayList<>();
                    for (JenisDAO jenis : jenislist) {
                        if(String.valueOf(jenis.getId_jenis()).toLowerCase().contains(userInput) || jenis.getNama_jenis().toLowerCase().contains(userInput)){
                            filteredList.add(jenis);
                            System.out.println(jenis.getNama_jenis());
                        }
                    }
                    jenisListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = jenisListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                jenisListFiltered = (ArrayList<JenisDAO>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
