package com.example.model;

import java.sql.Date;

public class Member {
    private int id;
    private String name;
    private String email;
    private Date joinDate;
    private String icNumber;
    private String gender;
    private Date dateOfBirth;
    private String postcode;
    private String town;
    
    public Member() {}
    
    // Constructor with all fields except ID (for new members)
    public Member(String name, String email, Date joinDate, String icNumber, String gender, Date dateOfBirth, String postcode, String town) {
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
        this.icNumber = icNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.postcode = postcode;
        this.town = town;
    }

    // Constructor with all fields (for existing members)
    public Member(int id, String name, String email, Date joinDate, String icNumber, String gender, Date dateOfBirth, String postcode, String town) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
        this.icNumber = icNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.postcode = postcode;
        this.town = town;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    public Date getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
    
    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                ", icNumber='" + icNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", postcode='" + postcode + '\'' +
                ", town='" + town + '\'' +
                '}';
    }
}
