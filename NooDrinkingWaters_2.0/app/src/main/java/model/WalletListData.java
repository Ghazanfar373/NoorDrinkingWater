package model;
public class WalletListData {
    private String description;
    private int imgId;
    private String amountCash;
    private String day;
    private String time;
    private String typeOrder;
    public WalletListData(String description, int imgId,String day,String time,String amount, String typeOrder) {
        this.description = description;
        this.imgId = imgId;
        this.day = day;
        this.time= time;
        this.amountCash=amount;
        this.typeOrder=typeOrder;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getAmountCash() {
        return amountCash;
    }

    public void setAmountCash(String amountCash) {
        this.amountCash = amountCash;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(String typeOrder) {
        this.typeOrder = typeOrder;
    }
}
