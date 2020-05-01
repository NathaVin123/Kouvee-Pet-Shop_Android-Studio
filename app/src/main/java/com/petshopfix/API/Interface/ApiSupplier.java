package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiSupplier {
    @GET("supplier")
    Call<Response> getAll();

    @GET("supplier/softDelete")
    Call<Response> getSoftDelete();

    @GET("supplier/{cari}")
    Call<Response> getSupplier(@Path("cari") String cari);

    @POST("supplier")
    @FormUrlEncoded
    Call<Response> createSupplier(@Field("nama_suppplier") String nama_supplier,
                                  @Field("alamat_supplier") String alamat_supplier,
                                  @Field("noTelp_supplier") String noTelp_supplier,
                                  @Field("stok") int stok,
                                  @Field("updateLodId") String NIP);

    @POST("supplier/update/{id}")
    @FormUrlEncoded
    Call<Response> updateSupplier(@Path("id") int id_supplier,
                                  @Field("nama_supplier") String nama_supplier,
                                  @Field("alamat_supplier") String alamat_supplier,
                                  @Field("noTelp_supplier") String noTelp_supplier,
                                  @Field("stok") int stok,
                                  @Field("updateLogId") String NIP);

    @FormUrlEncoded
    @POST("supplier/{id}")
    Call<Response> deleteSupplier(@Path("id") int id_supplier,
                                 @Field("updateLogId") String NIP);

    @FormUrlEncoded
    @POST("supplier/{id}/restore")
    Call<Response> restoreSupplier(@Path("id") int id_supplier,
                                  @Field("updateLogId") String NIP);

    @DELETE("supplier/{id}/permanen")
    Call<Response> deleteSupplierPermanen(@Path("id") int id_supplier);
}
