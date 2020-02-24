package com.alienware.scan2shop.data;

public class Category {
    String name;
    String description;
    String image;

    public Category(String name,String description,String image){

        this.name=name;
        this.description=description;
        this.image=image;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
