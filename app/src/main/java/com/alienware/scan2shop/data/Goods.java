package com.alienware.scan2shop.data;

import java.math.BigDecimal;

public class Goods {
    private String Pname;
    private String Price;
    private int code;
    private String description;
    private String itemImage;
    public Goods() {}

    public Goods(int code, String pname, String description, String price,String image)
    {
        this.code = code;
        this.Pname =pname;
        this.description = description;
        this.Price = price;
        this.itemImage = image;

    }

    public String getDescription()
    {
        return this.description;
    }

    public int getItemCode()
    {
        return this.code;
    }

    public String getItemImage()
    {
        return this.itemImage;
    }

    public String getName()
    {
        return this.Pname;
    }

    public String getPrice() {
        return Price;
    }

    public void setCode(int paramInt)
    {
        this.code = paramInt;
    }

    public void setDescription(String paramString)
    {
        this.description = paramString;
    }

    public void setItemImage(String paramBitmap)
    {
        this.itemImage = paramBitmap;
    }

    public void setName(String paramString)
    {
        this.Pname = paramString;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
