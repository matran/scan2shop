package com.alienware.scan2shop.data;

import android.graphics.Bitmap;

/**
 * Created by henry cheruiyot on 2/26/2018.
 */

public class Item {
    String name;
    String description;
    Double price;
    Bitmap image;

    public  Item(){

    }
    public Item(String name, String description, Double price, Bitmap image){

        this.name=name;
        this.description=description;
        this.price=price;
        this.image=image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
