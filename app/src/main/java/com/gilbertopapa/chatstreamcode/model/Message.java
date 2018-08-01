package com.gilbertopapa.chatstreamcode.model;

/**
 * Created by GilbertoPapa on 01/08/2018.
 */

public class Message {
    private String idUserMsg;
    private String msg;

    public Message() {
    }

    public String getIdUserMsg() {
        return idUserMsg;
    }

    public void setIdUserMsg(String idUserMsg) {
        this.idUserMsg = idUserMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
