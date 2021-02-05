package com.presidev.maos.location.rest;

import com.presidev.maos.location.response.Districts;
import com.presidev.maos.location.response.Provinces;
import com.presidev.maos.location.response.Regencies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("provinsi")
    Call<Provinces> getProvinces();

    @GET("kota")
    Call<Regencies> getRegencies(
            @Query("id_provinsi") int ID_PROVINCE
    );

    @GET("kecamatan")
    Call<Districts> getDistricts(
            @Query("id_kota") int ID_REGENCY
    );
}

