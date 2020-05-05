package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class DetailPengadaanDAO {

    @SerializedName("nomorPO")
    private String nomorPO;

    @SerializedName("id_produk")
    private String id_produk;

    @SerializedName("nama_produk")
    private String nama_produk;

    @SerializedName("jumlah_po")
    private int jumlah_po;

    @SerializedName("stok")
    private int stok;

    @SerializedName("harga_produk")
    private Double harga_produk;

    @SerializedName("satuan")
    private String satuan;

    @SerializedName("min_stok")
    private int min_stok;

    public DetailPengadaanDAO() {

    };

    public String getNomorPO() {
        return nomorPO;
    }

    public String getId_produk() {
        return id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public int getJumlah_po() {
        return jumlah_po;
    }

    public String getSatuan() {
        return satuan;
    }

    public Double getHarga_produk() {
        return harga_produk;
    }

    public int getStok() {
        return stok;
    }

    public int getMin_stok() {
        return min_stok;
    }

    public void setJumlah_po(int jumlah_po) {
        this.jumlah_po = jumlah_po;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
