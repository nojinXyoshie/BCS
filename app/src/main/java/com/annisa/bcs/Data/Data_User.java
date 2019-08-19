package com.annisa.bcs.Data;

import java.io.Serializable;

public class Data_User implements Serializable {

    @com.google.gson.annotations.SerializedName("NIK")
    private String nik;
    @com.google.gson.annotations.SerializedName("NAME")
    private String name;
    @com.google.gson.annotations.SerializedName("EMAIL")
    private String email;
    @com.google.gson.annotations.SerializedName("PASSWORD")
    private String password;
    @com.google.gson.annotations.SerializedName("IMAGE")
    private String image;

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}