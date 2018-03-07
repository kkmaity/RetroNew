package com.retrofit.retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tkyh on 9/22/2017.
 */

public interface MInterface {
    @GET("http://floridaconstruct.eu/comenzi/test/comenzi.php")
    Call<ArrayList<Order>> getUsers();

    @GET("comenzi/test/testgrafic.php")
    Call<ArrayList<Order>> getComenziGrafic();

    @GET("comenzipeperioada.php")
    Call<ArrayList<Order>> getComenziGraficPerioada(@Query("start") String start,
                                                      @Query("end") String end);

}