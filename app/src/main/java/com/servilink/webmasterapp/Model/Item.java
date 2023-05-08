package com.servilink.webmasterapp.Model;

public class Item {
    int Id;
    String name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Item(int Id, String name){
        this.Id=Id;
        this.name=name;
    }
    public String toString() {
        return getName();
    }
}
