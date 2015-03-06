package com.ashoksm.thiraseela.vo;

public class ArtistListVO {

    private String name;
    private String designation;
    private String url;

    public ArtistListVO(String name, String designation, String url) {
        this.name = name;
        this.designation = designation;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}