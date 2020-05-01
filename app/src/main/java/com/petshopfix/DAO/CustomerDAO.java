package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CustomerDAO {
    @SerializedName("id_customer")
    private int id_customer;

    @SerializedName("nama_customer")
    private String nama_customer;

    @SerializedName("alamat_customer")
    private String alamat_customer;

    @SerializedName("tglLahir_customer")
    private String tglLahir_customer;

    @SerializedName("noTelp_customer")
    private String noTelp_customer;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    public CustomerDAO(int id_customer, String nama_customer, String alamat_customer, String tglLahir_customer, String noTelp_customer, String updateLogId)
    {
        this.id_customer = id_customer;
        this.nama_customer = nama_customer;
        this.alamat_customer = alamat_customer;
        this.tglLahir_customer = tglLahir_customer;
        this.noTelp_customer = noTelp_customer;
        this.updateLogId = updateLogId;
    }

    public int getId_customer() {
        return id_customer;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public String getAlamat_customer() {
        return alamat_customer;
    }

    public String getTglLahir_customer() {
        return tglLahir_customer;
    }

    public String getNoTelp_customer() {
        return noTelp_customer;
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
