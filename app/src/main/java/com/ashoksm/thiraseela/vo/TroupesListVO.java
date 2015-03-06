package com.ashoksm.thiraseela.vo;

public class TroupesListVO {

    private String name;
    private String designation;
    private String location;
    private String url;

    public TroupesListVO(String name, String designation, String location, String url) {
        this.name = name;
        this.designation = designation;
        this.location = location;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}