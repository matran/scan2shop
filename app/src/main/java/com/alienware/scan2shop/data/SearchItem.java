package com.alienware.scan2shop.data;
import java.math.BigDecimal;

/**
 * Created by Henry cheruiyot on 2/3/2018.
 */

public class SearchItem {

    public String pname;
    public  String description;
    public int price;
   public  int quantity;
   public String image;


   public  SearchItem(){

   }
    public SearchItem(String pname,String description,int price,String image){

        this.pname=pname;
        this.description=description;
        this.price=price;
        this.image=image;

    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
