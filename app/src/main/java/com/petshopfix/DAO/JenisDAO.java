package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class JenisDAO {
    @SerializedName("id_jenis")
    private int id_jenis;

    @SerializedName("nama_jenis")
    private String nama_jenis;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    @SerializedName("deleteLog_at")
    private String deleteLog_at;

    @SerializedName("updateLogId")
    private String updateLogId;

    public JenisDAO(int id_jenis, String nama_jenis, String createLog_at, String updateLog_at, String deleteLog_at, String updateLogId)
    {
        this.id_jenis = id_jenis;
        this.nama_jenis = nama_jenis;
        this.createLog_at = createLog_at;
        this.updateLog_at = updateLog_at;
        this.deleteLog_at = deleteLog_at;
        this.updateLogId = updateLogId;
    }

    public int getId_jenis() {
        return id_jenis;
    }

    public String getNama_jenis() {
        return nama_jenis;
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
