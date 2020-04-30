package com.petshopfix.DAO;

import com.google.gson.annotations.SerializedName;

public class PegawaiDAO {
    @SerializedName("NIP")
    private String  NIP;

    @SerializedName("nama_pegawai")
    private String  nama_pegawai;

    @SerializedName("alamat_pegawai")
    private String  alamat_pegawai;

    @SerializedName("tglLagir_pegawai")
    private String  tglLagir_pegawai;

    @SerializedName("noTelp_pegawai")
    private String  noTelp_pegawai;

    @SerializedName("jabatan")
    private String  jabatan;

    @SerializedName("createLog_at")
    private String  createLog_at;

    @SerializedName("updateLog_at")
    private String  updateLog_at;

    @SerializedName("deleteLog_at")
    private String  deleteLog_at;

    @SerializedName("updateLogId")
    private String  updateLogId;

    public String getNIP() {
        return NIP;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public String getAlamat_pegawai() {
        return alamat_pegawai;
    }

    public String getTglLagir_pegawai() {
        return tglLagir_pegawai;
    }

    public String getNoTelp_pegawai() {
        return noTelp_pegawai;
    }

    public String getJabatan() {
        return jabatan;
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
