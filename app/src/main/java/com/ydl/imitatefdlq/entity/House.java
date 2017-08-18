package com.ydl.imitatefdlq.entity;

/**
 * Created by qweenhool on 2017/8/18.
 */

public class House {
    private String name;
    private String type;
    private String account;
    private String photo;
    private String roomNumber;

    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", account='" + account + '\'' +
                ", photo='" + photo + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
