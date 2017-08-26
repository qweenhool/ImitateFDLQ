package com.ydl.imitatefdlq.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by qweenhool on 2017/8/26.
 */
@Entity
public class RoomBean {
    @Id
    private String id;

    private int dataUpload;
    private int orderNumber;
    private String houseId;
    private String roomName;
    private String createTime;
    @Generated(hash = 1278592138)
    public RoomBean(String id, int dataUpload, int orderNumber, String houseId,
            String roomName, String createTime) {
        this.id = id;
        this.dataUpload = dataUpload;
        this.orderNumber = orderNumber;
        this.houseId = houseId;
        this.roomName = roomName;
        this.createTime = createTime;
    }
    @Generated(hash = 2135387174)
    public RoomBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getDataUpload() {
        return this.dataUpload;
    }
    public void setDataUpload(int dataUpload) {
        this.dataUpload = dataUpload;
    }
    public int getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getHouseId() {
        return this.houseId;
    }
    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }
    public String getRoomName() {
        return this.roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
