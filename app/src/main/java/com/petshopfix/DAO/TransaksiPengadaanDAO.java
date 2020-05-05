package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class TransaksiPengadaanDAO {

    @SerializedName("nomorPO")
    private String nomorPO;

    @SerializedName("tgl_po")
    private String tgl_po;

    @SerializedName("status_po")
    private String status_po;

    @SerializedName("totalHarga_po")
    private Double totalHarga_po;

    @SerializedName("id_supplier")
    private int id_supplier;

    @SerializedName("nama_supplier")
    private String nama_supplier;

    @SerializedName("alamat_supplier")
    private String alamat_supplier;

    @SerializedName("noTelp_supplier")
    private String noTelp_supplier;

    @SerializedName("createLog_at")
    private String createLog_at;

    @SerializedName("updateLog_at")
    private String updateLog_at;

    public TransaksiPengadaanDAO(){

    };

    public String getNomorPO() {
        return nomorPO;
    }

    public String getTgl_po() {
        return tgl_po;
    }

    public Double getTotalHarga_po() {
        return totalHarga_po;
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

    public String getStatus_po() {
        return status_po;
    }

    public String getCreateLog_at() {
        return createLog_at;
    }

    public String getUpdateLog_at() {
        return updateLog_at;
    }
}
