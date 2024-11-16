package com.eldroidfri730.extr.data;

public class CategoryModel {
    private String id;
    private String icon;
    private String name;
    private String desc;

    public CategoryModel(String icon, String name, String desc) {
        this.icon = icon;
        this.name = name;
        this.desc = desc;
    }

    public CategoryModel(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public CategoryModel(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return name;
    }
}
