package com.ydl.imitatefdlq.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by qweenhool on 2017/8/26.
 */
@Entity
public class HouseBean {
    @Id
    private String id;
    private int dataUpload;
    private String houseName;
    private Date orderNumber;
    private int houseType;
    private int useFeeTemplate;
    @Generated(hash = 947109677)
    public HouseBean(String id, int dataUpload, String houseName, Date orderNumber,
            int houseType, int useFeeTemplate) {
        this.id = id;
        this.dataUpload = dataUpload;
        this.houseName = houseName;
        this.orderNumber = orderNumber;
        this.houseType = houseType;
        this.useFeeTemplate = useFeeTemplate;
    }
    @Generated(hash = 229132509)
    public HouseBean() {
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
    public String getHouseName() {
        return this.houseName;
    }
    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }
    public Date getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(Date orderNumber) {
        this.orderNumber = orderNumber;
    }
    public int getHouseType() {
        return this.houseType;
    }
    public void setHouseType(int houseType) {
        this.houseType = houseType;
    }
    public int getUseFeeTemplate() {
        return this.useFeeTemplate;
    }
    public void setUseFeeTemplate(int useFeeTemplate) {
        this.useFeeTemplate = useFeeTemplate;
    }
}
