package com.suncart.grocerysuncart.model.content;


public class ContentItems {

    int id;
    String productName;
    String productMrp;
    String productSp;
    String totalStock;
    String productWeight;
    String productPics;
    String productDiscount;
    String unitType;

    public String getUnitType() {
        return unitType;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public int getId() {
       return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductMrp() {
        return productMrp;
    }

    public String getProductSp() {
        return productSp;
    }

    public String getTotalStock() {
        return totalStock;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public String getProductPics() {
        return productPics;
    }
}
