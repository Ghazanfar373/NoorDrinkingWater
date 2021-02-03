package model;

import java.util.List;

/**
 * Created by Oracle on 4/9/2018.
 */

public class OrderModel {

    private String reason;
    private String delivery;
    private String orderDateTime;
    private String address;
    private String lng;
    private int statusColor;
    private String contact;
    private String orderNum;
    private String customerName;
    private String customerId;
    private String lat;
    private String status;
    private List<OrderProductModel> products;

    //for less adapter calculations
    private String orderTime;
    private String orderDate;

    public void setReason(String reason){
        this.reason = reason;
    }

    public String getReason(){
        return reason;
    }

    public void setDelivery(String delivery){
        this.delivery = delivery;
    }

    public String getDelivery(){
        return delivery;
    }

    public void setOrderDateTime(String orderDateTime){
        this.orderDateTime = orderDateTime;
    }

    public String getOrderDateTime(){
        return orderDateTime;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setLng(String lng){
        this.lng = lng;
    }

    public String getLng(){
        return lng;
    }

    public void setContact(String contact){
        this.contact = contact;
    }

    public String getContact(){
        return contact;
    }

    public void setOrderNum(String orderNum){
        this.orderNum = orderNum;
    }

    public String getOrderNum(){
        return orderNum;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public String getCustomerId(){
        return customerId;
    }

    public void setLat(String lat){
        this.lat = lat;
    }

    public String getLat(){
        return lat;
    }

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setProducts(List<OrderProductModel> products){
        this.products = products;
    }

    public List<OrderProductModel> getProducts(){
        return products;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString(){
        return
                "OrderModel{" +
                        "reason = '" + reason + '\'' +
                        ",delivery = '" + delivery + '\'' +
                        ",order_date_time = '" + orderDateTime + '\'' +
                        ",address = '" + address + '\'' +
                        ",lng = '" + lng + '\'' +
                        ",statusColor = '" + statusColor + '\'' +
                        ",contact = '" + contact + '\'' +
                        ",order_num = '" + orderNum + '\'' +
                        ",customer_name = '" + customerName + '\'' +
                        ",customer_id = '" + customerId + '\'' +
                        ",lat = '" + lat + '\'' +
                        ",status = '" + status + '\'' +
                        ",products = '" + products + '\'' +
                        ",orderTime = '" + orderTime + '\'' +
                        ",orderDate= '" + orderDate + '\'' +
                        "}";
    }
}
