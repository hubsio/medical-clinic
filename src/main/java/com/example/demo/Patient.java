package com.example.demo;

public class Patient {
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String birthday;

    public Patient(String email, String password, String idCardNo, String firstName, String lastName, String phoneNumber, String birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }
}
