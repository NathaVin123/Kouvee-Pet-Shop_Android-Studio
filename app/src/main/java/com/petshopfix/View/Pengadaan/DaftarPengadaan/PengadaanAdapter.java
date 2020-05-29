package com.petshopfix.View.Pengadaan.DaftarPengadaan;

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
import android.widget.TextView;
import android.widget.Toast;

import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiDetailPengadaan;
import com.petshopfix.API.Interface.ApiPengadaan;
import com.petshopfix.API.Response;
import com.petshopfix.Activity.Pengadaan.DetailPengadaan;
import com.petshopfix.DAO.TransaksiPengadaanDAO;
import com.petshopfix.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PengadaanAdapter extends RecyclerView.Adapter<PengadaanAdapter.PengadaanViewHolder> {

    public static final String DEST = "./target/sandbox/stamper/stamp_header2.pdf";
    public static final String SRC = "./src/main/resources/pdfs/Wrong.pdf";
    private List<TransaksiPengadaanDAO> transaksiPengadaanList;
    private List<TransaksiPengadaanDAO> transaksiPengadaanListFiltered;
    private Context context;
    private Bundle detailPengadaan;

    public PengadaanAdapter(Context context, List<TransaksiPengadaanDAO> transaksiPengadaanList) {
        this.context=context;
        this.transaksiPengadaanList = transaksiPengadaanList;
        this.transaksiPengadaanListFiltered = transaksiPengadaanList;
    }

    @NonNull
    @Override
    public PengadaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_pengadaan_adapter, parent, false);

        return new PengadaanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengadaanViewHolder holder, int position) {
        final TransaksiPengadaanDAO pengadaan = transaksiPengadaanListFiltered.get(position);
        holder.tv_noPO.setText("No : " + pengadaan.getNomorPO());

        String sDate1 = pengadaan.getTgl_po();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedCurrentDate = null;
        try {
            convertedCurrentDate = sdf.parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy");

        holder.tv_tglPengadaan.setText("Tanggal : " + dt1.format(convertedCurrentDate));
        holder.tv_namaSupplier.setText(pengadaan.getNama_supplier());
        holder.tv_alamat.setText(pengadaan.getAlamat_supplier());
        holder.tv_noHp.setText(pengadaan.getNoTelp_supplier());
        holder.tv_statusPengadaan.setText("Status Pengadaan : " + pengadaan.getStatus_po());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDate1 = pengadaan.getTgl_po();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedCurrentDate = null;
                try{
                    convertedCurrentDate = sdf.parse(sDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy");

                detailPengadaan = new Bundle();
                detailPengadaan.putString("nomorPO", pengadaan.getNomorPO());
                detailPengadaan.putString("tgl_po", dt1.format(convertedCurrentDate));
                detailPengadaan.putString("nama_supplier", pengadaan.getNama_supplier());
                detailPengadaan.putString("alamat_supplier", pengadaan.getAlamat_supplier());
                detailPengadaan.putString("noTelp_supplier", pengadaan.getNoTelp_supplier());
                detailPengadaan.putString("status_po", pengadaan.getStatus_po());

                Intent i = new Intent(context, DetailPengadaan.class);
                i.putExtras(detailPengadaan);
                context.startActivity(i);
            }
        });

        if (pengadaan.getStatus_po().equals("Sudah Datang"))
            holder.btnBatalPesanan.setVisibility(View.INVISIBLE);

            holder.btnBatalPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pembatalanPengadaan(pengadaan.getNomorPO());
            }
        });
    }


    @Override
    public int getItemCount() {
        return (transaksiPengadaanListFiltered != null) ? transaksiPengadaanListFiltered.size() : 0;
    }

    public class PengadaanViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noPO, tv_tglPengadaan, tv_namaSupplier, tv_statusPengadaan;
        private TextView tv_alamat, tv_noHp;
        private Button btnDetail, btnBatalPesanan;

        public PengadaanViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_noPO = (TextView) itemView.findViewById(R.id.tv_noPO);
            tv_tglPengadaan = (TextView) itemView.findViewById(R.id.tv_tglPengadaan);
            tv_namaSupplier = (TextView) itemView.findViewById(R.id.tv_namaSupplier);
            tv_alamat = (TextView) itemView.findViewById(R.id.tv_alamat);
            tv_noHp = (TextView) itemView.findViewById(R.id.tv_noHp);
            tv_statusPengadaan = (TextView) itemView.findViewById(R.id.tv_statusPengadaan);
            btnDetail = (Button) itemView.findViewById(R.id.btnDetail);
            btnBatalPesanan = (Button) itemView.findViewById(R.id.btnBatalPesan);
        }
    }

    private void hapusDetailPengadaan(String nomorPO, String id_produk)
    {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<Response> detailPengadaan = apiService.batalProdukPengadaan(nomorPO, id_produk);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        detailPengadaan.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pembatalanPengadaan(String nomorPO)
    {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<com.petshopfix.API.Response> detailPengadaan = apiService.tampilPengadaan(nomorPO);

        detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                if(!response.body().getDetailPengadaan().isEmpty())
                {
                    for(int i=0; i < response.body().getDetailPengadaan().size() ; i++)
                    {
                        String id_produk = response.body().getDetailPengadaan().get(i).getId_produk();
                        hapusDetailPengadaan(nomorPO, id_produk);
                    }
                    hapusTransaksiPengadaan(nomorPO);
                }
                else
                {
                    hapusTransaksiPengadaan(nomorPO);
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusTransaksiPengadaan(String nomorPO)
    {
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<com.petshopfix.API.Response> pengadaan = apiService.batalPengadaan(nomorPO);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        pengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<Response> response) {
                System.out.println(response.body());
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                    context.startActivity(new Intent(context, ShowPengadaan.class));
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
