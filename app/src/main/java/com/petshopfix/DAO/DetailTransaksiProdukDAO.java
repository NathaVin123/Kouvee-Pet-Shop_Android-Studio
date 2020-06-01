package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class DetailTransaksiProdukDAO {

    @SerializedName("no_transaksi")
    private String no_transaksi;

    @SerializedName("id_produk")
    private String id_produk;

    @SerializedName("nama_produk")
    private String nama_produk;

    @SerializedName("jumlah")
    private int jumlah;

    @SerializedName("stok")
    private int stok;

    @SerializedName("harga_produk")
    private Double harga_produk;

    public DetailTransaksiProdukDAO() {}

    public String getNo_transaksi() {
        return no_transaksi;
    }

    public Double getHarga_produk() {
        return harga_produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getId_produk() {
        return id_produk;
    }

    public int getStok() {
        return stok;
    }

    public void setJumlah(int jumlah){
        this.jumlah = jumlah;
    }
}
