package com.retrofit.retrofit;

/**
 * Created by tkyh on 9/22/2017.
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")

public class Order {

    @SerializedName("NetAutonumber")
    @Expose
    private String netAutonumber;

    @SerializedName("item1")
    @Expose
    private String item1;

    @SerializedName("item3")
    @Expose
    private String item3;

    @SerializedName("NetData")
    @Expose
    private String netData;

    @SerializedName("item2")
    @Expose
    private String item2;

    @SerializedName("Grup")
    @Expose
    private Integer grup;

    @SerializedName("Note")
    @Expose
    private String note;

    @SerializedName("SubGrup")
    @Expose
    private Integer subGrup;

    @SerializedName("item4")
    @Expose
    private String item4;

    public String getNetAutonumber() {
        return netAutonumber;
    }

    public String getItem1() {
        return item1;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3){
        this.item3=item3;
    }

    public String getNetData() {
        return netData;
    }

    public String getItem2() {
        return item2;
    }

    public Integer getGrup() {
        return grup;
    }

    public String getNote() {
        return note;
    }

    public Integer getSubGrup() {
        return subGrup;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4){
        this.item4=item4;
    }

}