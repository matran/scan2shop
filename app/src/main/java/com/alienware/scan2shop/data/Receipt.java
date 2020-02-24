package com.alienware.scan2shop.data;

import java.math.BigDecimal;

/**
 * Created by Henry cheruiyot on 3/27/2018.
 */

public class Receipt  {
    public String name;
    public BigDecimal total;
    public  int quantity;

    public Receipt(String name,int quantity,BigDecimal total){

        this.name=name;
        this.quantity=quantity;
        this.total=total;

    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getName() {
        return name;
    }
}

