package com.annisa.bcs.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data_Overtime implements Serializable {

    @com.google.gson.annotations.SerializedName("NIK")
    private String nik;
    @com.google.gson.annotations.SerializedName("ATTENDANCE_DATE")
    private String atten_date;
    @com.google.gson.annotations.SerializedName("NAME")
    private String name;
    @com.google.gson.annotations.SerializedName("PROJECT")
    private String projectOt;
    @com.google.gson.annotations.SerializedName("DAYS")
    private String days;
    @com.google.gson.annotations.SerializedName("CUSTOMER")
    private String customer;
    @com.google.gson.annotations.SerializedName("IN")
    private String in;
    @com.google.gson.annotations.SerializedName("OUT")
    private String out;
    @com.google.gson.annotations.SerializedName("BRUTO_OT")
    private String bruto;
    @com.google.gson.annotations.SerializedName("NETT_OT")
    private String nett;
    @com.google.gson.annotations.SerializedName("NOMINAL")
    private String nominal;
    @com.google.gson.annotations.SerializedName("ACTIVITY")
    private String activity;
    @com.google.gson.annotations.SerializedName("CLAIM_STATUS")
    private String claimstatus;
    @com.google.gson.annotations.SerializedName("REASON")
    private String reason;
    @com.google.gson.annotations.SerializedName("CREATED_DT")
    private String createddt;
    @com.google.gson.annotations.SerializedName("CREATED_BY")
    private String createdby;
    @com.google.gson.annotations.SerializedName("UPDATED_DT")
    private String updateddt;
    @com.google.gson.annotations.SerializedName("UPDATED_BY")
    private String updatedy;

    @SerializedName("image")
    private Data_Overtime.ImageBean imageBean;

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getAtten_date() {
        return atten_date;
    }

    public void setAtten_date(String atten_date) {
        this.atten_date = atten_date;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getBruto() {
        return bruto;
    }

    public void setBruto(String bruto) {
        this.bruto = bruto;
    }

    public String getNett() {
        return nett;
    }

    public void setNett(String nett) {
        this.nett = nett;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getClaimstatus() {
        return claimstatus;
    }

    public void setClaimstatus(String claimstatus) {
        this.claimstatus = claimstatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProjectOt() {
        return projectOt;
    }

    public void setProjectOt(String projectOt) {
        this.projectOt = projectOt;
    }

    public String getUpdateddt() {
        return updateddt;
    }

    public void setUpdateddt(String updateddt) {
        this.updateddt = updateddt;
    }

    public String getUpdatedy() {
        return updatedy;
    }

    public void setUpdatedy(String up) {
        this.nett = nett;
    }

    public Data_Overtime.ImageBean getImageBean() { return imageBean; }

    public void setImageBean(Data_Overtime.ImageBean imageBean) { this.imageBean = imageBean; }

    public static class ImageBean implements Serializable {

        @SerializedName("IMAGE")
        private String IMAGE;

        public String getIMAGE() {
            return IMAGE;
        }

        public void setIMAGE(String IMAGE) {
            this.IMAGE = IMAGE;
        }

    }

}