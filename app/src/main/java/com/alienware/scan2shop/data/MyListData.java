package com.alienware.scan2shop.data;

/**
 * Created by Henry cheruiyot on 2/3/2018.
 */

public class MyListData {
    private int code;
    private String myListName;
    private String myListPrice;
    private String myListQuantity;
    private String total;


    public MyListData(){


    }

    public MyListData(int paramInt, String paramString1, String paramString2, String paramString3,String total)
    {
        this.code = paramInt;
        this.myListName= paramString1;
        this.myListPrice = paramString2;
        this.myListQuantity = paramString3;
        this.total=total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public int getMyListCode() {
        return this.code;
    }

    public String getMyListName() {
        return this.myListName;
    }

    public String getMyListPrice() {
        return this.myListPrice;
    }

    public String getMyListQuantity() {
        return this.myListQuantity;
    }

    public void setMyListCode(int paramInt)
    {
        this.code = paramInt;
    }

    public void setMyListName(String paramString)
    {
        this.myListName = paramString;
    }

    public void setMyListPrice(String paramString)
    {
        this.myListPrice = paramString;
    }

    public void setMyListQuantity(String paramString)
    {
        this.myListQuantity = paramString;
    }
}
