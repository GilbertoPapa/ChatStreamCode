package com.gilbertopapa.chatstreamcode.model;

import com.gilbertopapa.chatstreamcode.config.ConfigurationFireBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

/**
 * Created by GilbertoPapa on 16/07/2018.
 */

public class User {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String key;


    public User() {

    }

    public void save() {
        DatabaseReference databaseReference = ConfigurationFireBase.getFirebase();
        databaseReference.child("users").child(getId()).setValue(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //@Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
