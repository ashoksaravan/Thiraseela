package com.ashoksm.thiraseela.vo;

public class ArtistListVO {

    private String name;
    private String designation;
    private String url;
    private String performerImage;
    private String about;
    private String location;
    private String address;
    private String phone;
    private String mobile;
    private String website;

    public ArtistListVO(String name, String designation, String url, String performerImage, String about, String location) {
        this.name = name;
        this.designation = designation;
        this.url = url;
        this.about = about;
        this.performerImage = performerImage;
        this.location = location;
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

    public String getPerformerImage() {
        return performerImage;
    }

    public void setPerformerImage(String performerImage) {
        this.performerImage = performerImage;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}