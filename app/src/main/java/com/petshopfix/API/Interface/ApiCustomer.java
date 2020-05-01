package com.petshopfix.API.Interface;

import com.petshopfix.API.Response;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiCustomer {

    @GET("customer")
    Call<Response> getAll();

    @GET("customer/softDelete")
    Call<Response> getSoftDelete();

    @GET("customer/{cari}")
    Call<Response> getCustomer(@Path("cari") String cari);

    @POST("customer")
    @FormUrlEncoded
    Call<Response> createCustomer(@Field("nama_customer") String nama_csutomer,
                                  @Field("alamat_customer") String alamat_customer,
                                  @Field("tglLahir_customer") String tglLahir_customer,
                                  @Field("noTelp_customer") String noTelp_customer,
                                  @Field("updateLogId") String updateLogId);

//    @POST("supplier/update/{id}")
//    @FormUrlEncoded
//    Call<Response> updateSupplier(@Path("id") int id_supplier,
//                                  @Field("nama_supplier") String nama_supplier,
//                                  @Field("alamat_supplier") String alamat_supplier,
//                                  @Field("noTelp_supplier") String noTelp_supplier,
//                                  @Field("stok") int stok,
//                                  @Field("updateLogId") String NIP);

    @FormUrlEncoded
    @POST("customer/{id}")
    Call<Response> deleteCustomer(@Path("id") int id_customer,
                                  @Field("updateLogId") String NIP);

    @FormUrlEncoded
    @POST("customer/{id}/restore")
    Call<Response> restoreCustomer(@Path("id") int ic_customer,
                                   @Field("updateLogId") String NIP);

    @DELETE("customer/{id}/permanen")
    Call<Response> deleteCustomerPermanen(@Path("id") int id_customer);
}
