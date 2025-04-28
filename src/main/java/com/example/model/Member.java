package com.example.model;

import java.sql.Date;

public class Member {
    private int id;
    private String name;
    private String email;
    private Date joinDate;
    
    // Default constructor
    public Member() {
    }
    
    // Constructor with all fields except ID (for new members)
    public Member(String name, String email, Date joinDate) {
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
    }
    
    // Constructor with all fields (for existing members)
    public Member(int id, String name, String email, Date joinDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.joinDate = joinDate;
    }
    
    // Getters and setters
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
    
    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }
}
