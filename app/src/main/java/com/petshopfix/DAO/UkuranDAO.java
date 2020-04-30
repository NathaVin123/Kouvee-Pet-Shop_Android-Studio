package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class UkuranDAO {
    @SerializedName("id_ukuran")
    private int id_ukuran;

    @SerializedName("nama_ukuran")
    private String nama_ukuran;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    public UkuranDAO(int id_ukuran, String nama_ukuran, String createLog_at, String updateLog_at, String deleteLog_at, String updateLogId)
    {
        this.id_ukuran = id_ukuran;
        this.nama_ukuran = nama_ukuran;
        this.createLog_at = createLog_at;
        this.updateLog_at = updateLog_at;
        this.deleteLog_at = deleteLog_at;
        this.updateLogId = updateLogId;
    }

    public int getId_ukuran() {
        return id_ukuran;
    }

    public String getNama_ukuran() {
        return nama_ukuran;
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
