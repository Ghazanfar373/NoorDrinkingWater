package com.pac.ideatech.noordrinkingwater;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String product_id;
    private String qnty;
    private String produt_title;
    private String unit_price;
    private double total_amount;
    private String saleDate;
    private String imgURI;
    Bitmap thumbnail;
    // Order submitting flow processparams
    private static String prepaidCoupon;
    private static String note;
    private static String cartitemslist;
    private static String shipping_address;

    public static String getUser_address() {
        return Order.shipping_address;
    }

    public static void setUser_address(String user_address) {
        Order.shipping_address = user_address;
    }

    public static String getCartitemslist() {
        return Order.cartitemslist;
    }

    public static void setCartitemslist(String cartitemslist) {
        Order.cartitemslist = cartitemslist;
    }

    public static String getPrepaidCoupon() {
        return Order.prepaidCoupon;
    }

    public static void setPrepaidCoupon(String Coupon) {
        Order.prepaidCoupon = Coupon;
    }

    public static String getNote() {
        return Order.note;
    }

    public static void setNote(String not) {
        Order.note = not;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    private static boolean isClearEnabled;
    private static double totalPrice=0;
    public static int num_orders=0;

    private static boolean responceback;

    public static boolean isResponceback() {
        return responceback;
    }

    public static void setResponceback(boolean responceback) {
        Order.responceback = responceback;
    }

    public String getImgURI() {
        return imgURI;
    }

    public void setImgURI(String imgURI) {
        this.imgURI = imgURI;
    }

    public static int getNum_orders() {
        return num_orders;
    }

    public static void setNum_orders(int num_orders) {
        Order.num_orders += num_orders;
    }
    public static void adjustNum_orders(int num_orders) {
        Order.num_orders =num_orders;
    }
    public static double getTotalPrice() {
        return totalPrice;
    }

    public static void setTotalPrice(double totalPrice) {
        Order.totalPrice += totalPrice;
    }
    public  static void adjustTotalPrice(double amount){Order.totalPrice-=amount;}

    public static boolean isClearEnabled() {
        return isClearEnabled;
    }

    public static void setClearEnabled(boolean clearEnabled) {
        isClearEnabled = clearEnabled;
    }

    public static List<Order> orderList=new ArrayList<>();
    public static List<Order> order_historyList=new ArrayList<>();

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getProdut_title() {
        return produt_title;
    }

    public void setProdut_title(String produt_title) {
        this.produt_title = produt_title;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public static List<Order> getOrderList() {
        return orderList;
    }

    public static void setOrderList(List<Order> orderList) {
        Order.orderList = orderList;
    }
}
