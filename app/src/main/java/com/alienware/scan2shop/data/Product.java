package com.alienware.scan2shop.data;
import android.graphics.Bitmap;

/**
 * Created by Henry cheruiyot on 2/3/2018.
 */

public class Product {
    private String Pname;
    private int Price;
    private int Quantity;
    private int code;
    private String description;
    private String itemImage;
    private int total;
    public Product() {}

    public Product(int paramInt1, String paramString1, String paramString2, int paramDouble, int paramInt2, String paramBitmap,int total)
    {
        this.code = paramInt1;
        this.Pname = paramString1;
        this.description = paramString2;
        this.Price = paramDouble;
        this.Quantity = paramInt2;
        this.itemImage = paramBitmap;
        this.total=total;
    }


    public void setTotal(int total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
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

    public int getPrice()
    {
        return this.Price;
    }

    public String getQuantity()
    {
        return Integer.toString(this.Quantity);
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

    public void setPrice(int paramDouble)
    {
        this.Price = paramDouble;
    }

    public void setQuantity(int paramInt)
    {
        this.Quantity = paramInt;
    }


}
