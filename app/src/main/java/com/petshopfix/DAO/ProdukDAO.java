package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class ProdukDAO {

    @SerializedName("id_produk")
    private String id_produk;

    @SerializedName("nama_produk")
    private String nama_produk;

    @SerializedName("harga_produk")
    private Double harga_produk;

    @SerializedName("stok")
    private int stok;

    @SerializedName("min_stok")
    private int min_stok;

    @SerializedName("satuan_produk")
    private String satuan_produk;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    public ProdukDAO(String id_produk, String nama_produk, Double harga_produk, int stok, int min_stok, String satuan_produk, String updateLogId)
    {
        this.id_produk = id_produk;
        this.nama_produk = nama_produk;
        this.harga_produk = harga_produk;
        this.stok = stok;
        this.min_stok = min_stok;
        this.satuan_produk = satuan_produk;
        this.updateLogId = updateLogId;
    }

    public String getId_produk() {
        return id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
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

    public String getSatuan_produk() {
        return satuan_produk;
    }

    public String getCreateLog_at() {
        return createLog_at;
    }

    public String getUpdateLog_at() {
        return updateLog_at;
    }

    public String getDeleteLog_at() {
        return deleteLog_at;
    }

    public String getUpdateLogId() {
        return updateLogId;
    }
}
