package com.petshopfix.View.PenjualanProduk.DaftarPenjualanProduk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.petshopfix.DAO.TransaksiProdukDAO;
import com.petshopfix.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PenjualanProdukAdapter extends RecyclerView.Adapter<PenjualanProdukAdapter.PenjualanProdukViewHolder> {

    private List<TransaksiProdukDAO> transaksiProdukList;
    private List<TransaksiProdukDAO> transaksiProdukListFiltered;
    private Context context;
    private Bundle detailPenjualanProduk;

    public PenjualanProdukAdapter(Context context, List<TransaksiProdukDAO> transaksiProdukList)
    {
        this.context = context;;
        this.transaksiProdukList = transaksiProdukList;
        this.transaksiProdukListFiltered = transaksiProdukList;
    }

    @NonNull
    @Override
    public PenjualanProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_penjualan_produk_adapter, parent, false);

        return new PenjualanProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenjualanProdukViewHolder holder, int position) {
        final TransaksiProdukDAO produk = transaksiProdukListFiltered.get(position);
        holder.tv_noTransaksi.setText("No : " + produk.getNo_transaksi());

        String sDate1 = produk.getTgl_transaksi();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedCurrentDate = null;
        try{
            convertedCurrentDate = sdf.parse(sDate1);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy");

        holder.tv_tglTransaksi.setText("Tanggal : " + dt1.format(convertedCurrentDate));
        holder.tv_namaCustomer.setText(produk.getNama_customer());
        holder.tv_namaHewan.setText(produk.getNama_hewan());
        holder.tv_noHp.setText(produk.getNoTelp_customer());
        holder.tv_statusPembayaran.setText("Status Pengadaan : " + produk.getStatus_pembayaran());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDate1 = produk.getTgl_transaksi();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date convertedCurrentDate = null;
                try{
                    convertedCurrentDate = sdf.parse(sDate1);
                }catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy");

                detailPenjualanProduk = new Bundle();
                detailPenjualanProduk.putString("no_transaksi", produk.getNo_transaksi());
                detailPenjualanProduk.putString("tgl_transaksi", produk.getTgl_transaksi());
                detailPenjualanProduk.putString("xxxx", produk.getNama_customer());



//                $transaksiProduk = new TransaksiProduk;
//                $transaksiProduk->no_transaksi = $this->penomoranNoTransaksi();
//                $transaksiProduk->tgl_transaksi = Carbon::now();
//                $transaksiProduk->id_customer = $request['idCustomer'];
//                $transaksiProduk->id_customerService = $request['id_customerService'];
//                $transaksiProduk->totalBiaya = $request['totalBiaya'];
//                $transaksiProduk->createLog_at = Carbon::now();
            }
        });
    }




    @Override
    public int getItemCount() {
        return (transaksiProdukListFiltered != null) ? transaksiProdukListFiltered.size() : 0;
    }

    public class PenjualanProdukViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_noTransaksi, tv_tglTransaksi, tv_namaCustomer, tv_statusPembayaran;
        private TextView tv_namaHewan, tv_noHp;
        private Button btnDetail, btnBatalPesanan;

        public PenjualanProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_noTransaksi = (TextView) itemView.findViewById(R.id.tv_noPO);
            tv_tglTransaksi = (TextView) itemView.findViewById(R.id.tv_tglPengadaan);
            tv_namaCustomer = (TextView) itemView.findViewById(R.id.tv_namaSupplier);
            tv_namaHewan = (TextView) itemView.findViewById(R.id.tv_alamat);
            tv_noHp = (TextView) itemView.findViewById(R.id.tv_noHp);
            tv_statusPembayaran = (TextView) itemView.findViewById(R.id.tv_statusPengadaan);
            btnDetail = (Button) itemView.findViewById(R.id.btnDetail);
            btnBatalPesanan = (Button) itemView.findViewById(R.id.btnBatalPesan);
        }
    }
}
