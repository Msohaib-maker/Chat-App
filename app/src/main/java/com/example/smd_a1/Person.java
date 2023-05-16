package com.example.smd_a1;

import android.graphics.Bitmap;

public class Person {

    private String Name;
    private String photo;
    private Bitmap photo1;
    private String Phone;
    private int status_online;

    public int getStatus_online() {
        return status_online;
    }

    public void setStatus_online(int status_online) {
        this.status_online = status_online;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    // Constructor



    public Person(String name)
    {
        this.Name = name;
    }

    // 2nd Constructor
    public Person(String name, String phone,int status) {
        Name = name;
        this.Phone = phone;
        this.status_online = status;
    }

    public Person()
    {
        // for firebase
    }


    // for name and image
    public Person(String name, Bitmap photo1) {
        Name = name;
        this.photo1 = photo1;
    }

    public Bitmap getPhoto1() {
        return photo1;
    }

    public void setPhoto1(Bitmap photo1) {
        this.photo1 = photo1;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
