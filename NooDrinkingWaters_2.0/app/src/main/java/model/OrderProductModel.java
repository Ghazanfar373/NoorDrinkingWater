package model;

public class OrderProductModel {
    private String price;
    private String qty;
    private String name;
    private String amount;

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQty() {
        return qty;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return
                "OrderProductModel{" +
                        "price = '" + price + '\'' +
                        ",qty = '" + qty + '\'' +
                        ",name = '" + name + '\'' +
                        ",amount = '" + amount + '\'' +
                        "}";
    }
}
