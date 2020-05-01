package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class HewanDAO {

    @SerializedName("id_hewan")
    private int id_hewan;

    @SerializedName("nama_hewan")
    private String nama_hewan;

    @SerializedName("tglLahir_hewan")
    private String tglLahir_hewan;

    @SerializedName("id_jenis")
    private int id_jenis;

    @SerializedName("id_customer")
    private int id_customer;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    @SerializedName("nama_jenis")
    private String nama_jenis;

    @SerializedName("nama_customer")
    private String nama_customer;

    public HewanDAO(int id_hewan, String nama_hewan, String tglLahir_hewan, int id_jenis, int id_customer, String updateLogId)
    {
        this.id_hewan = id_hewan;
        this.nama_hewan = nama_hewan;
        this.tglLahir_hewan = tglLahir_hewan;
        this.id_jenis = id_jenis;
        this.id_customer = id_customer;
        this.updateLogId = updateLogId;
    }

    public int getId_hewan() {
        return id_hewan;
    }

    public String getNama_hewan() {
        return nama_hewan;
    }

    public String getTglLahir_hewan() {
        return tglLahir_hewan;
    }

    public int getId_jenis() {
        return id_jenis;
    }

    public int getId_customer() {
        return id_customer;
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

    public String getNama_jenis() {
        return nama_jenis;
    }

    public String getNama_customer() {
        return nama_customer;
    }
}
