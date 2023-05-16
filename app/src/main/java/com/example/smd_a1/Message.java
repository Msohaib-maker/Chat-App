package com.example.smd_a1;

import java.util.Date;

public class Message {
    private String name;
    private String content;
    private String time;
    private String photo;
    private int is_sender;

    // for firebase classification
    private String senderNum;
    private String ReceiverNum;

    // status : online or offline
    private int status;

    public Message()
    {
        is_sender = 0; // sender identity
    }

    public Message(String name, String content, String time, String photo,int type) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.photo = photo;
        this.is_sender = type;
    }

    public Message(String name, String content, String time, String photo,String SenderPhone,String ReceiverPhone) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.photo = photo;
        this.senderNum = SenderPhone;
        this.ReceiverNum = ReceiverPhone;
        this.is_sender = 0;
    }

    public Message(String name, String content, String time, String photo,String SenderPhone,String ReceiverPhone,int s) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.photo = photo;
        this.senderNum = SenderPhone;
        this.ReceiverNum = ReceiverPhone;
        this.is_sender = 0;
        this.status = s;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSenderNum() {
        return senderNum;
    }

    public void setSenderNum(String senderNum) {
        this.senderNum = senderNum;
    }

    public String getReceiverNum() {
        return ReceiverNum;
    }

    public void setReceiverNum(String receiverNum) {
        ReceiverNum = receiverNum;
    }

    public int getIs_sender() {
        return is_sender;
    }

    public void setIs_sender(int is_sender) {
        this.is_sender = is_sender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
