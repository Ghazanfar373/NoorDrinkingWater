package model;

/**
 * Created by Oracle on 4/9/2018.
 */

public class DrawerModel {

    private int catID;
    private String title;
    private int logo;
    private boolean isSelected;

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
