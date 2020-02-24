package com.alienware.scan2shop.data;

public class History {
    private String receiptid;
    private String date;
    private String id;
    private int amount;
    private int items;

    public History( String id,String date,int amount,int items,String receiptid){
        this.date=date;
        this.id=id;
        this.amount=amount;
        this.items=items;
        this.receiptid=receiptid;
    }

    public void setReceiptid(String receiptid) {
        this.receiptid = receiptid;
    }

    public String getReceiptid() {
        return receiptid;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public int getItems() {
        return items;
    }

    public String getId() {
        return id;
    }
}
