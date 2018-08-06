package com.gilbertopapa.chatstreamcode.model;

/**
 * Created by GilbertoPapa on 02/08/2018.
 */

public class Talk {

    private String idUser;
    private String name;

    public Talk() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
}
