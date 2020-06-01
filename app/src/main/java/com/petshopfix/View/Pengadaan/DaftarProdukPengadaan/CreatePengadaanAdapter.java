package com.petshopfix.View.Pengadaan.DaftarProdukPengadaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.petshopfix.API.ApiClient;
import com.petshopfix.API.Interface.ApiDetailPengadaan;
import com.petshopfix.API.Interface.ApiPengadaan;
import com.petshopfix.API.Response;
import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.DAO.ProdukDAO;
import com.petshopfix.R;
import com.petshopfix.View.Pengadaan.DaftarPengadaan.ShowPengadaan;
import com.petshopfix.View.Pengadaan.DaftarProdukPengadaanTertentu.UpdatePengadaanShow;
import com.petshopfix.ZoomImage;

import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CreatePengadaanAdapter extends RecyclerView.Adapter<CreatePengadaanAdapter.CreatePengadaanViewHolder> {

    private List<ProdukDAO> produkList;
    private List<ProdukDAO> produkListFiltered;
    private Context context;
    private String nomorPO;
    private Double totalBiaya;
    private String status, cek, nama_supplier;

    public CreatePengadaanAdapter(Context context, List<ProdukDAO> produkList, String nomorPO,
                                  String cek, String nama_supplier)
    {
        this.context = context;
        this.produkList = produkList;
        this.produkListFiltered = produkList;
        this.nomorPO = nomorPO;
        this.cek = cek;
        this.nama_supplier = nama_supplier;
    }

    @NonNull
    @Override
    public CreatePengadaanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_create_pengadaan_adapter, parent, false);
        return new CreatePengadaanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatePengadaanViewHolder holder, int position)
    {
        final ProdukDAO produks = produkListFiltered.get(position);
        holder.nama_produk.setText(produks.getNama_produk());
        holder.harga_produk.setText("Harga : Rp "+String.valueOf(produks.getHarga_produk() +"0"));
        holder.stok.setText("Stok "+String.valueOf(produks.getStok()) + " | " +String.valueOf(produks.getMin_stok()));

        String url = ApiClient.BASE_URL +  "produk/" + produks.getId_produk() + "/gambar";
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.gambar);

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

        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
                Call<com.petshopfix.API.Response> detailPengadaan = apiService.tampilPengadaan(nomorPO);

                detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
                    @Override
                    public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                        status = "Tidak ada";
                        DetailPengadaanDAO detailPengadaan = null;


                        if (!response.body().getDetailPengadaan().isEmpty())
                        {
                            System.out.println("Iki "+ response.body().getDetailPengadaan().get(0).getNama_produk());
                            System.out.println("jumlah"+response.body().getDetailPengadaan().size());
                            for (DetailPengadaanDAO dtp : response.body().getDetailPengadaan())
                            {

                                if (dtp.getId_produk().equals(produks.getId_produk()))
                                {
                                    detailPengadaan = dtp;
                                    status = "Ada";
                                }
                            }
                        }
                        createPengadaan(produks,detailPengadaan);
                    }

                    @Override
                    public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                        Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return (produkListFiltered != null) ? produkListFiltered.size() : 0;
    }

    public class CreatePengadaanViewHolder extends RecyclerView.ViewHolder{
        private ImageView gambar;
        private TextView nama_produk, harga_produk, stok;
        private Button btnTambah;

        public CreatePengadaanViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_produk = (TextView) itemView.findViewById(R.id.txt_nama);
            harga_produk = (TextView) itemView.findViewById(R.id.txt_harga);
            stok = (TextView) itemView.findViewById(R.id.txt_stok);
            gambar = (ImageView) itemView.findViewById(R.id.txt_gambarpg);
            btnTambah = (Button) itemView.findViewById(R.id.btn_tambah);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String userInput = constraint.toString();
                if (userInput.isEmpty())
                {
                    produkListFiltered = produkList;
                }
                else
                {
                    List<ProdukDAO> filteredList = new ArrayList<>();
                    for (ProdukDAO produk : produkList) {
                        if (produk.getId_produk().toLowerCase().contains(userInput) || produk.getNama_produk().toLowerCase().contains(userInput))
                        {
                            filteredList.add(produk);
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

    private void updateTotalTransaksiPengadaan(String nomorPO, Double totalBiaya)
    {
        ApiPengadaan apiService = ApiClient.getClient().create(ApiPengadaan.class);
        Call<com.petshopfix.API.Response> pengadaan = apiService.updateBiayaPengadaan(nomorPO, totalBiaya);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        pengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                System.out.println(response.body());
                if (response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");

                    if (cek.equals("Ubah Pengadaan"))
                    {
                        Intent i = new Intent(context, UpdatePengadaanShow.class);
                        i.putExtra("nomorPO", nomorPO);
                        i.putExtra("nama_supplier", nama_supplier);
                        context.startActivity(i);
                    }
                    else
                        context.startActivity(new Intent(context, ShowPengadaan.class));
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
}

    private void simpanDetailPengadaan(String nomorPO, String id_produk, String satuan, int jumlah_po)
    {
        /*if (status.equals("Tidak Ada"))
        {*/
        System.out.println("NoPO"+nomorPO);
        System.out.println("idProduk" +id_produk);
        System.out.println("satuan"+satuan);
        System.out.println("jumlah"+jumlah_po);
            ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
            Call<com.petshopfix.API.Response> detailPengadaan = apiService.createDetailPengadaan(nomorPO, id_produk, satuan, jumlah_po);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
                @Override
                public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                    System.out.println(response.code());

                    if(response.code() == 200)
                    {
                        progressDialog.dismiss();
                        System.out.println("Success");
                    }
                }

                @Override
                public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        /*}
        else
        {
            ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
            Call<com.petshopfix.API.Response> detailPengadaan = apiService.updateDetailPengadaan(nomorPO, id_produk, satuan, jumlah_po);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
                @Override
                public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                    progressDialog.dismiss();
                    System.out.println(response.code());
                    if(response.code() == 200)
                    {
                        progressDialog.dismiss();
                        System.out.println("Success");
                    }
                }

                @Override
                public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    private void hapusDetailPengadaan(String nomorPO, String id_produk)
    {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<com.petshopfix.API.Response> detailPengadaan = apiService.batalProdukPengadaan(nomorPO, id_produk);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                if(response.code() == 200)
                {
                    progressDialog.dismiss();
                    System.out.println("Success");
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitungTotalBiaya(String nomorPO)
    {
        ApiDetailPengadaan apiService = ApiClient.getClient().create(ApiDetailPengadaan.class);
        Call<com.petshopfix.API.Response> detailPengadaan = apiService.tampilPengadaan(nomorPO);

        detailPengadaan.enqueue(new Callback<com.petshopfix.API.Response>() {
            @Override
            public void onResponse(Call<com.petshopfix.API.Response> call, retrofit2.Response<com.petshopfix.API.Response> response) {
                if(!response.body().getDetailPengadaan().isEmpty())
                {
                    totalBiaya = 0.0;
                    for(int i=0; i < response.body().getDetailPengadaan().size() ; i++)
                    {
                        totalBiaya = totalBiaya + (response.body().getDetailPengadaan().get(i).getJumlah_po()*
                                response.body().getDetailPengadaan().get(i).getHarga_produk());
                    }
                    System.out.println(totalBiaya);
                    updateTotalTransaksiPengadaan(nomorPO,totalBiaya);
                }
            }

            @Override
            public void onFailure(Call<com.petshopfix.API.Response> call, Throwable t) {
                Toast.makeText(context, t.getCause().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createPengadaan(ProdukDAO produks, DetailPengadaanDAO dtp)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tambah_pengadaan, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        ImageView btnExit = view.findViewById(R.id.btnExit_tp);
        ImageButton btnHapus = view.findViewById(R.id.btnHapus_tp);
        ImageView btnMinus = view.findViewById(R.id.min_tp);
        ImageView btnPlus = view.findViewById(R.id.plus_tp);
        Button btnSelesai = view.findViewById(R.id.btnSelesai_tp);
        Button btnTambah = view.findViewById(R.id.btnTambah_tp);
        ImageView txtGambar = view.findViewById(R.id.gambar_tp);
        TextView txtNama = view.findViewById(R.id.nama_tp);
        TextView txtHarga = view.findViewById(R.id.harga_tp);
        TextView txtStok = view.findViewById(R.id.stok_tp);
        TextView txtSubtotal = view.findViewById(R.id.subtotal_tp);
        EditText txtJumlah = view.findViewById(R.id.jumlah_tp);
        EditText txtSatuan = view.findViewById(R.id.satuan_tp);

        if (cek.equals("Ubah Pengadaan"))
            btnSelesai.setText("Pengadaan Tersimpan");

        txtNama.setText(produks.getNama_produk());
        txtHarga.setText("Rp "+String.valueOf(produks.getHarga_produk() +"0"));
        txtStok.setText("Stok : "+ produks.getStok() + " / " + produks.getMin_stok());

       /* if (status.equals("Tidak Ada"))
        {
            txtSatuan.setText("Box");
            txtJumlah.setText("1");
            txtSubtotal.setText(produks.getHarga_produk()+"0");
            btnHapus.setVisibility(View.INVISIBLE);
        }
        else
        {
//            txtSatuan.setText(dtp.getSatuan());
//            txtJumlah.setText(String.valueOf(dtp.getJumlah_po()));
//            txtSubtotal.setText(String.valueOf(dtp.getHarga_produk()*dtp.getJumlah_po())+"0");
        }*/

        String url = ApiClient.BASE_URL + "produk/" + produks.getId_produk() + "/gambar";
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(txtGambar);

        txtJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int jum = 1;
                if(!txtJumlah.getText().toString().isEmpty())
                {
                    jum = Integer.parseInt(txtJumlah.getText().toString());
                }
                Double subTotal = jum * produks.getHarga_produk();
                txtSubtotal.setText(String.valueOf(subTotal)+"0");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minus = Integer.parseInt(txtJumlah.getText().toString()) - 1;
                if(minus < 1)
                    txtJumlah.setText("1");
                else
                    txtJumlah.setText(String.valueOf(minus));
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus = Integer.parseInt(txtJumlah.getText().toString()) + 1;
                if(plus < 1)
                    txtJumlah.setText("1");
                else
                    txtJumlah.setText(String.valueOf(plus));
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusDetailPengadaan(nomorPO,produks.getId_produk());

                Intent i = new Intent(context, CreatePengadaanShow.class);
                i.putExtra("nomorPO", nomorPO);
                i.putExtra("cek", cek);
                context.startActivity(i);
            }
        });

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idP, satuan;
                int jumlah_po;

                idP = produks.getId_produk();
                satuan = txtSatuan.getText().toString();
                jumlah_po = Integer.parseInt(txtJumlah.getText().toString());

                simpanDetailPengadaan(nomorPO, idP, satuan, jumlah_po);
                hitungTotalBiaya(nomorPO);
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idP, satuan;
                int jumlah_po;

                idP = produks.getId_produk();
                satuan = txtSatuan.getText().toString();
                jumlah_po = Integer.parseInt(txtJumlah.getText().toString());

                simpanDetailPengadaan(nomorPO, idP, satuan, jumlah_po);

                Intent i = new Intent(context, CreatePengadaanShow.class);
                i.putExtra("nomorPO", nomorPO);
                i.putExtra("cek", cek);
                context.startActivity(i);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CreatePengadaanShow.class);
                i.putExtra("nomorPO", nomorPO);
                i.putExtra("cek", cek);
                context.startActivity(i);
            }
        });
        alertD.setView(view);
        alertD.show();
    }
}
