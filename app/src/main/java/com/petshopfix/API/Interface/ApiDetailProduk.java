package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiDetailProduk {

    @GET("detailProduk")
    Call<Response> getAll();

    @GET("detailProduk/tampil/{id}")
    Call<Response> tampilDTProduk(@Path("id") String no_transaksi);

    @POST("detailProduk")
    @FormUrlEncoded
    Call<Response> createDetailPenjualanProduk(@Field("noTransaksi") String no_transaksi,
                                               @Field("idProduk") String id_produk,
                                               @Field("jumlah") int jumlah);

    @POST("detailProduk/update")
    @FormUrlEncoded
    Call<Response> updateDetailPenjualanProduk(@Field("no_transaksi") String no_transaksi,
                                               @Field("id_produk") String id_produk,
                                               @Field("jumlah") int jumlah);

    @POST("detailProduk/delete")
    @FormUrlEncoded
    Call<Response> batalProdukPenjualanProduk(@Field("no_transaksi") String no_transaksi,
                                              @Field("id_produk") String id_produk);
}
