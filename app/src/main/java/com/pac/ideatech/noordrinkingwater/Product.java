package com.pac.ideatech.noordrinkingwater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghazanfar_Ali on 4/30/2018.
 */

public class Product {
    private String title;
    private String product_id;
    private String price;
    private String imgURL;
    public static  List<Product> ITEMS = new ArrayList<Product>();
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
