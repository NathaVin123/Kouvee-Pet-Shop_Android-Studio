package com.petshopfix.View.PenjualanProduk.TampilTransaksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.petshopfix.API.Interface.ApiDetailProduk;
import com.petshopfix.API.Interface.ApiProduk;
import com.petshopfix.API.Interface.ApiTransaksiProduk;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.TransaksiProdukDAO;
import com.petshopfix.R;
import com.petshopfix.SQLite.DatabaseHandler;
import com.petshopfix.View.PenjualanProduk.CartTransaksi.CartProdukShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TampilTransaksiProdukAdapter extends RecyclerView.Adapter<TampilTransaksiProdukAdapter.tampilTransaksiProdukViewHolder> {

    private List<TransaksiProdukDAO> penjualanProdukList;
    private List<TransaksiProdukDAO> penjualanProdukListFiltered;
    private Context context;

    public TampilTransaksiProdukAdapter(Context context, List<TransaksiProdukDAO> transaksiProdukList) {
        this.context = context;
        this.penjualanProdukList = transaksiProdukList;
        this.penjualanProdukListFiltered = transaksiProdukList;
    }

    @NonNull
    @Override
    public tampilTransaksiProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_tampil_transaksi_produk_adapter, parent, false);
        return new tampilTransaksiProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tampilTransaksiProdukViewHolder holder, int position) {
        final TransaksiProdukDAO transaksiProduk = penjualanProdukListFiltered.get(position);
        holder.tv_noTransaksi.setText(transaksiProduk.getNo_transaksi());

        if(transaksiProduk.getId_customer() != null) {
            holder.tv_status.setText("Member");
            holder.tv_namaCustomer.setText(transaksiProduk.getNama_customer());
            holder.tv_alamat.setText(transaksiProduk.getAlamat_customer());
            holder.tv_noTelp.setText(transaksiProduk.getNoTelp_customer());
        }
        else
        {
            holder.tv_status.setText("Pengunjung");
            holder.tv_namaCustomer.setText("");
            holder.tv_alamat.setText("Tidak Diketahui");
            holder.tv_noTelp.setText("");
        }

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CartProdukShow.class);
                i.putExtra("no_transaksi", transaksiProduk.getNo_transaksi());
                i.putExtra("status", "detail");
                i.putExtra("nama_customer", holder.tv_namaCustomer.getText().toString());
            }
        });

        holder.btnBatalPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pembatalanPemesananProduk(transaksiProduk.getNo_transaksi());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (penjualanProdukListFiltered != null) ? penjualanProdukListFiltered.size() : 0;
    }

    public class tampilTransaksiProdukViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_status, tv_namaCustomer, tv_alamat, tv_noTelp;
        private Button btnDetail, btnBatalPesanan;

        public tampilTransaksiProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_noTransaksi = (TextView) itemView.findViewById(R.id.noTransaksi_dpa);
            tv_status = (TextView) itemView.findViewById(R.id.status_dpa);
            tv_namaCustomer = (TextView) itemView.findViewById(R.id.namaCustomer_dpa);
            tv_alamat = (TextView) itemView.findViewById(R.id.alamat_dpa);
            tv_noTelp = (TextView) itemView.findViewById(R.id.noTelp_dpa);
            btnDetail = (Button) itemView.findViewById(R.id.btnDetail_dpa);
            btnBatalPesanan = (Button) itemView.findViewById(R.id.btnBatalPesan_dpa);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userInput = constraint.toString();
                if (userInput.isEmpty())
                {
                    penjualanProdukListFiltered = penjualanProdukList;
                }
                else
                {
                    String nama;
                    List<TransaksiProdukDAO> filteredList = new ArrayList<>();
                    for (TransaksiProdukDAO dtproduk : penjualanProdukList) {
                        if (dtproduk.getNama_customer() == null)
                            nama = "Tidak Diketahui";
                        else
                            nama = dtproduk.getNama_customer();

                        if (dtproduk.getNo_transaksi().toLowerCase().contains(userInput) || nama.toLowerCase().contains(userInput))
                        {
                            filteredList.add(dtproduk);
                        }
                    }
                    penjualanProdukListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = penjualanProdukListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                penjualanProdukListFiltered = (ArrayList<TransaksiProdukDAO>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void pembatalanPemesananProduk(String no_transaksi) {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduk = apiService.tampilDetailProduk(no_transaksi);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        dtproduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (!response.body().getDetailProduk().isEmpty())
                {
                    for (int i=0; i < response.body().getDetailProduk().size(); i++)
                    {
                        String id_produk = response.body().getDetailProduk().get(i).getId_produk();
                        int stok = response.body().getDetailProduk().get(i).getStok();
                        hapusDetailPenjualanProduk(no_transaksi, id_produk, stok);
                    }
                    hapusTransaksiProduk(no_transaksi);
                    progressDialog.dismiss();
                }
                else
                {
                    hapusTransaksiProduk(no_transaksi);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusTransaksiProduk(String no_transaksi)
    {
        ApiTransaksiProduk apiService = ApiClient.getClient().create(ApiTransaksiProduk.class);
        Call<Response> transaksiProduk = apiService.batalPenjualanProduk(no_transaksi);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        transaksiProduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                    context.startActivity(new Intent(context, TampilTransaksiProdukShow.class));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusDetailPenjualanProduk(String no_transaksi, String id_produk, int stok)
    {
        ApiDetailProduk apiService = ApiClient.getClient().create(ApiDetailProduk.class);
        Call<com.petshopfix.API.Response> dtproduk = apiService.deleteDetailPenjualanProduk(no_transaksi, id_produk);

        dtproduk.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                System.out.println(response.code());
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
