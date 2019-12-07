package edu.ncsu.csc.itrust.beans;

import java.io.Serializable;

public class PreregisterBean implements Serializable {
    private long MID = 0;
    private String streetadress1 = "";
    private String streetadress2 = "";
    private String city = "";
    private String state = "AL";
    private String zip = "";
    private String phone = "";
    private String icName = "";
    private String icAddress1 = "";
    private String icAddress2 = "";
    private String icCity = "";
    private String icState = "AK";
    private String icZip = "";
    private String icPhone = "";
    private float height = 0;
    private float weight = 0;
    private boolean smoker = false;

    public String geticPhone(){
        return icPhone;
    }

    public void setIcPhone(String str){
        this.icPhone = str;
    }

    public String getStreetadress1() {
        return streetadress1;
    }

    public void setStreetadress1(String streetadress1) {
        this.streetadress1 = streetadress1;
    }

    public String getStreetadress2() {
        return streetadress2;
    }

    public void setStreetadress2(String streetadress2) {
        this.streetadress2 = streetadress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean getSmoker(){
        return smoker;
    }

    public void setSmoker(boolean tr){
        this.smoker = tr;
    }

    public float getHeight(){
        return height;
    }

    public void setHeight(float fl){
        this.height = fl;
    }

    public float getWeight(){
        return weight;
    }

    public void setWeight(float fl){
        this.weight = fl;
    }

    public String getIcName() {
        return icName;
    }

    public void setIcName(String icName) {
        this.icName = icName;
    }

    public String getIcZip() {
        return icZip;
    }

    public void setIcZip(String icZip) {
        this.icZip = icZip;
    }

    public String getIcState() {
        return icState;
    }

    public void setIcState(String icState) {
        this.icState = icState;
    }

    public long getMID() {
        return MID;
    }

    public void setMID(long mid) {
        MID = mid;
    }

    public String getIcAddress1() {
        return icAddress1;
    }

    public void setIcAddress1(String icAddress1) {
        this.icAddress1 = icAddress1;
    }

    public String getIcAddress2() {
        return icAddress2;
    }

    public void setIcAddress2(String icAddress2) {
        this.icAddress2 = icAddress2;
    }

    public String getIcCity() {
        return icCity;
    }

    // Composition of city, state, and zip
    public String getIcAddress3() {
        return getIcCity() + ", " + getIcState() + " " + getIcZip();
    }



    public void setIcCity(String icCity) {
        this.icCity = icCity;
    }


}


