package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class TransaksiProdukDAO {

    @SerializedName("no_transaksi")
    private String no_transaksi;

    @SerializedName("tgl_transaksi")
    private String tgl_transaksi;

    @SerializedName("totalBiaya")
    private Double totalBiaya;

    @SerializedName("status_pembayaran")
    private String status_pembayaran;

    @SerializedName("id_customer")
    private int id_customer;

    @SerializedName("id_customerService")
    private String id_customerService;

    @SerializedName("id_kasir")
    private String id_kasir;

    @SerializedName("nama_customer")
    private String nama_customer;

    @SerializedName("noTelp_customer")
    private String noTelp_customer;

    @SerializedName("nama_hewan")
    private String nama_hewan;

    @SerializedName("nama_jenis")
    private String nama_jenis;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    public TransaksiProdukDAO() {}

    public Double getTotalBiaya() {
        return totalBiaya;
    }

    public String getNo_transaksi() {
        return no_transaksi;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public int getId_customer() {
        return id_customer;
    }

    public String getId_customerService() {
        return id_customerService;
    }

    public String getId_kasir() {
        return id_kasir;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public String getNoTelp_customer() {
        return noTelp_customer;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public String getNama_hewan() {
        return nama_hewan;
    }

    public String getCreateLog_at() {
        return createLog_at;
    }

    public String getUpdateLog_at() {
        return updateLog_at;
    }

    public String getNama_jenis() {
        return nama_jenis;
    }
}
