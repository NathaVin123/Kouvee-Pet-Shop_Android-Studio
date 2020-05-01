package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class SupplierDAO {

    @SerializedName("id_supplier")
    private int id_supplier;

    @SerializedName("nama_supplier")
    private String nama_supplier;

    @SerializedName("alamat_supplier")
    private String alamat_supplier;

    @SerializedName("noTelp_supplier")
    private String noTelp_supplier;

    @SerializedName("stok")
    private int stok;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    public SupplierDAO(int id_supplier, String nama_supplier, String alamat_supplier, String noTelp_supplier, int stok, String updateLogId)
    {
        this.id_supplier = id_supplier;
        this.nama_supplier = nama_supplier;
        this.alamat_supplier = alamat_supplier;
        this.noTelp_supplier = noTelp_supplier;
        this.stok = stok;
        this.updateLogId = updateLogId;
    }

    public int getId_supplier() {
        return id_supplier;
    }

    public String getNama_supplier() {
        return nama_supplier;
    }

    public String getAlamat_supplier() {
        return alamat_supplier;
    }

    public String getNoTelp_supplier() {
        return noTelp_supplier;
    }

    public int getStok() {
        return stok;
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
