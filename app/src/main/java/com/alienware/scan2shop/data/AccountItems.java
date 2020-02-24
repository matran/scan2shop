package com.alienware.scan2shop.data;

/**
 * Created by Henry cheruiyot on 2/3/2018.
 */

public class AccountItems {
    private String name;
    private String values;

    public AccountItems(String paramString1, String paramString2) {
        this.name = paramString1;
        this.values = paramString2;
    }

    public String getName() {
        return this.name;
    }

    public String getValues() {
        return this.values;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setValues(String paramString) {
        this.values = paramString;
    }
}
