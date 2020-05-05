package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiDetailPengadaan {

    @GET("detailPengadaan")
    Call<Response> getAll();

    @GET("detailPengadaan/tampil/{nomorPO}")
    Call<Response> tampilPengadaan(@Path("nomorPO") String nomorPO);

    @POST("detailPengadaan")
    @FormUrlEncoded
    Call<Response> createDetailPengadaan(@Field("nomorPO") String nomorPO,
                                         @Field("id_produk") String id_produk,
                                         @Field("satuan") String satuan,
                                         @Field("jumlah_po") int jumlah_po);

    @POST("detailPengadaan/update")
    @FormUrlEncoded
    Call<Response> updateDetailPengadaan(@Field("nomorPO") String nomorPO,
                                         @Field("id_produk") String id_produk,
                                         @Field("satuan") String satuan,
                                         @Field("jumlah_po") int jumlah_po);

    @POST("detailPengadaan/delete")
    @FormUrlEncoded
    Call<Response> batalProdukPengadaan(@Field("nomorPO") String nomorPO,
                                        @Field("id_produk") String id_produk);
}
