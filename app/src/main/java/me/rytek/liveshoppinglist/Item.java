package me.rytek.liveshoppinglist;

public class Item {
    public String id;
    public String item;
    public int quantity;
    public Boolean bought;
    public String added_by;
    public String comments;

    public Item(String item, int quantity, Boolean bought, String added_by, String comments) {
        this.item = item;
        this.quantity = quantity;
        this.bought = bought;
        this.added_by = added_by;
        this.comments = comments;
    }
}
