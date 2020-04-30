package com.petshopfix.SQLite;

public class UserDefaults {
    private int id;
    private String NIP, nama_pegawai, jabatan, status;

    public UserDefaults(){}

    public UserDefaults(int id, String NIP, String nama_pegawai, String jabatan, String status)
    {
        this.id = id;
        this.NIP = NIP;
        this.nama_pegawai = nama_pegawai;
        this.jabatan = jabatan;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getNIP() {
        return NIP;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public String getJabatan() {
        return jabatan;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
