package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiSupplier {
    @GET("supplier")
    Call<Response> getAll();

    @GET("supplier/softDelete")
    Call<Response> getSoftDelete();

    @GET("supplier/{cari}")
    Call<Response> getSupplier(@Path("cari") String cari);

    @Multipart
    @POST("supplier")
    Call<Response> createSupplier(@Part("nama_suppplier") RequestBody nama_supplier,
                                  @Part("alamat_supplier") RequestBody alamat_supplier,
                                  @Part("noTelp_supplier") RequestBody noTelp_supplier,
                                  @Part("stok") RequestBody stok,
                                  @Part("updateLodId") RequestBody updateLogId);

    @Multipart
    @POST("supplier/update/{id}")
    Call<Response> updateSupplier(@Path("id") int id_supplier,
                                  @Part("nama_supplier") RequestBody nama_supplier,
                                  @Part("alamat_supplier") RequestBody alamat_supplier,
                                  @Part("noTelp_supplier") RequestBody noTelp_supplier,
                                  @Part("stok") RequestBody stok,
                                  @Part("updateLogId") RequestBody NIP);

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
