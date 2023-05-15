package com.example.e_commerce.data;

public class USER_MODEL {
    String Name;
    String Email;
    String Phone;
    String Address;


    public USER_MODEL(String name, String email, String phone, String address) {
        Name = name;
        Phone = phone;
        Address = address;
        Email=email;
    }
    public USER_MODEL(){}

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
