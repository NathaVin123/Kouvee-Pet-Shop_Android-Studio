package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class LayananDAO {

    @SerializedName("id_layanan")
    private String id_layanan;

    @SerializedName("nama_layanan")
    private String nama_layanan;

    @SerializedName("harga_layanan")
    private Double harga_layanan;

    @SerializedName("id_ukuran")
    private int id_ukuran;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    @SerializedName("nama_ukuran")
    private String nama_ukuran;

    public LayananDAO(String id_layanan, String nama_layanan, Double harga_layanan, int id_ukuran, String updateLogId)
    {
        this.id_layanan = id_layanan;
        this.nama_layanan = nama_layanan;
        this.harga_layanan = harga_layanan;
        this.id_ukuran = id_ukuran;
        this.updateLogId = updateLogId;
    }

    public String getId_layanan() {
        return id_layanan;
    }

    public String getNama_layanan() {
        return nama_layanan;
    }

    public Double getHarga_layanan() {
        return harga_layanan;
    }

    public int getId_ukuran() {
        return id_ukuran;
    }

    public String getCreateLog_at() {
        return createLog_at;
    }

    public String getUpdateLog_at() {
        return updateLog_at;
    }

    public  String getDeleteLog_at() {
        return deleteLog_at;
    }

    public String getUpdateLogId() {
        return updateLogId;
    }

    public String getNama_ukuran() {
        return nama_ukuran;
    }
}
