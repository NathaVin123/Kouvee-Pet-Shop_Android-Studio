package com.petshopfix.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.petshopfix.DAO.CustomerDAO;
import com.petshopfix.DAO.DetailPengadaanDAO;
import com.petshopfix.DAO.DetailTransaksiProdukDAO;
import com.petshopfix.DAO.HewanDAO;
import com.petshopfix.DAO.JenisDAO;
import com.petshopfix.DAO.LayananDAO;
import com.petshopfix.DAO.PegawaiDAO;
import com.petshopfix.DAO.ProdukDAO;
import com.petshopfix.DAO.SupplierDAO;
import com.petshopfix.DAO.TransaksiPengadaanDAO;
import com.petshopfix.DAO.TransaksiProdukDAO;
import com.petshopfix.DAO.UkuranDAO;

import java.util.List;

public class Response {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("customer")
    @Expose
    private List<CustomerDAO> customer;

    @SerializedName("hewan")
    @Expose
    private List<HewanDAO> hewan;

    @SerializedName("jenis")
    @Expose
    private List<JenisDAO> jenis;

    @SerializedName("ukuran")
    @Expose
    private List<UkuranDAO> ukuran;

    @SerializedName("layanan")
    @Expose
    private List<LayananDAO> layanan;

    @SerializedName("produk")
    @Expose
    private List<ProdukDAO> produk;

    @SerializedName("pegawai")
    @Expose
    private List<PegawaiDAO> pegawai;

    @SerializedName("supplier")
    @Expose
    private List<SupplierDAO> supplier;

    @SerializedName("pengadaan")
    @Expose
    private List<TransaksiPengadaanDAO> pengadaan;

    @SerializedName("detailPengadaan")
    @Expose
    private List<DetailPengadaanDAO> detailPengadaan;

//    @SerializedName("transaksiProduk")
//    @Expose
//    private List<TransaksiProdukDAO> transaksiProduk;
//
//    @SerializedName("detailProduk")
//    @Expose
//    private List<DetailTransaksiProdukDAO> detailProduk;

//    @SerializedName("transaksiLayanan")
//    @Expose
//    private List<TransaksiPengadaanDAO> transaksiLayanan;
//
//    @SerializedName("detailTransaksiLayanan")
//    @Expose
//    private List<DetailPengadaanDAO> detailTransaksiLayanan;

    //Get Data
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<CustomerDAO> getCustomer() {
        return customer;
    }

    public List<HewanDAO> getHewan() {
        return hewan;
    }

    public List<JenisDAO> getJenis() {
        return jenis;
    }

    public List<LayananDAO> getLayanan() {
        return layanan;
    }

    public List<PegawaiDAO> getPegawai() {
        return pegawai;
    }

    public List<ProdukDAO> getProduk() {
        return produk;
    }

    public List<SupplierDAO> getSupplier() {
        return supplier;
    }

    public List<UkuranDAO> getUkuran() {
        return ukuran;
    }

    public List<TransaksiPengadaanDAO> getPengadaan() {
        return pengadaan;
    }

    public List<DetailPengadaanDAO> getDetailPengadaan() {
        return detailPengadaan;
    }

//    public List<TransaksiProdukDAO> getTransaksiProduk() {
//        return transaksiProduk;
//    }
//
//    public List<DetailTransaksiProdukDAO> getDetailProduk() {
//        return detailProduk;
//    }
}
