package com.annisa.bcs.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data_Traveling implements Serializable {

    @com.google.gson.annotations.SerializedName("ID_TRAVELING")
    private String id_traveling;
    @com.google.gson.annotations.SerializedName("NIK")
    private String nik;
    @com.google.gson.annotations.SerializedName("TRAVELING_DATE")
    private String date;
    @com.google.gson.annotations.SerializedName("NAME")
    private String name;
    @com.google.gson.annotations.SerializedName("DAYS")
    private String days;
    @com.google.gson.annotations.SerializedName("GO_FROM")
    private String from;
    @com.google.gson.annotations.SerializedName("GO_TO")
    private String to;
    @com.google.gson.annotations.SerializedName("RATE")
    private String rate;
    @com.google.gson.annotations.SerializedName("BUDGET")
    private String budget;
    @com.google.gson.annotations.SerializedName("STATUS")
    private String status;
    @com.google.gson.annotations.SerializedName("NOTE")
    private String note;
    @com.google.gson.annotations.SerializedName("PROJECT_CUST")
    private String project;
    @com.google.gson.annotations.SerializedName("CREATED_DT")
    private String created_dt;

    @SerializedName("image")
    private ImageBean imageBean;

    public String getId_traveling() {
        return id_traveling;
    }

    public void setId_traveling(String id_traveling) {
        this.id_traveling = id_traveling;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    public ImageBean getImageBean() { return imageBean; }

    public void setImageBean(ImageBean imageBean) { this.imageBean = imageBean; }

    public static class ImageBean implements Serializable {

        @SerializedName("IMAGE") private String IMAGE;

        public String getIMAGE() { return IMAGE; }
        public void setIMAGE(String IMAGE) { this.IMAGE = IMAGE; }
    }

}