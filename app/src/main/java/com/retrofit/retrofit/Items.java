package com.retrofit.retrofit;

/**
 * Created by tkyh on 9/22/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")

public class Items {

    @SerializedName("NetAutonumber")
    @Expose
    private String netAutonumber;

    @SerializedName("item1")
    @Expose
    private String item1;

    @SerializedName("item2")
    @Expose
    private String item2;


    public String getNetAutonumber() {
        return netAutonumber;
    }

    public String getItem1() {
        return item1;
    }



    public String getItem2() {
        return item2;
    }




}