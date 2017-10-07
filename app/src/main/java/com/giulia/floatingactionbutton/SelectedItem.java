package com.giulia.floatingactionbutton;


public class SelectedItem {
    private int imageId;
    private String itemName;

    public SelectedItem(int imageId1, String title) {
        this.imageId = imageId1;
        this.itemName = title;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId1) {
        this.imageId = imageId1;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName1) {
        this.itemName = itemName1;

}}
