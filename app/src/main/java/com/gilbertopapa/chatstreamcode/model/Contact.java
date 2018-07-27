package com.gilbertopapa.chatstreamcode.model;

/**
 * Created by GilbertoPapa on 16/07/2018.
 */

public class Contact {
    private String identfyContact;
    private String name ;
    private String email;

    public Contact() {
    }

    public String getIdentfyContact() {
        return identfyContact;
    }

    public void setIdentfyContact(String identfyContact) {
        this.identfyContact = identfyContact;
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
}
