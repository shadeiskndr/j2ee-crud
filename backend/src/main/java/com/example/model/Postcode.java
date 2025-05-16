package com.example.model;

public class Postcode {
    private String postcode;
    private String town;
    private String stateCode;

    public Postcode() {}

    public Postcode(String postcode, String town, String stateCode) {
        this.postcode = postcode;
        this.town = town;
        this.stateCode = stateCode;
    }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }
    public String getTown() { return town; }
    public void setTown(String town) { this.town = town; }
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
}
