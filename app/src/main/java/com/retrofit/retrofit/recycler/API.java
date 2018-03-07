package com.retrofit.retrofit.recycler;

/**
 * Created by mircea on 27.05.2017.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {





    @GET("test/comenzi.php")
    Call<List<CustomObject>> getComenziGrupat();

    @GET("test/comenzilemele.php")
    Call<List<CustomObject>> getComenzileMele(@Query("text") String text);

    @GET("test/cautacomenzi.php")
    Call<List<CustomObject>> searchcomenzi(@Query("text") String text);

    @GET("test/comenzipeperioada.php")
    Call<List<CustomObject>> searchcomenziperioada(@Query("start") String start,
                                                   @Query("end") String end);





}
