package com.example.e_commerce.data;

import android.net.Uri;

public class DataModel {
    private int image1=0;
    private Uri image2=null;
    private String itemName;
    private int noOfItem=1;
    private String itemDescription;
    private double singleItemPrice;

    public DataModel(int image1, String itemName,String itemDescription,double singleItemPrice) {
        this.image1 = image1;
        this.itemName = itemName;
        this.itemDescription=itemDescription;
        this.singleItemPrice=singleItemPrice;

    }

    public DataModel(Uri image2, String itemName,String itemDescription,double singleItemPrice) {
        this.image2 = image2;
        this.itemName = itemName;
        this.itemDescription=itemDescription;
        this.singleItemPrice=singleItemPrice;

    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getSingleItemPrice() {
        return singleItemPrice;
    }

    public void setSingleItemPrice(double singleItemPrice) {
        this.singleItemPrice = singleItemPrice;
    }

    public int getImage1() {
        return image1;
    }

    public void setImage1(int image1) {
        this.image1 = image1;
    }

    public Uri getImage2() {
        return image2;
    }

    public void setImage2(Uri image2) {
        this.image2 = image2;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getNoOfItem() {
        return noOfItem;
    }

    public void setNoOfItem(int noOfItem) {
        this.noOfItem = noOfItem;
    }
}
