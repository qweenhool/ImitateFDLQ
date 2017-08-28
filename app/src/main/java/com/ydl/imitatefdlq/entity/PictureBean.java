package com.ydl.imitatefdlq.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by qweenhool on 2017/8/26.
 */
@Entity
public class PictureBean {
    @Id
    private String id;
    private String path;
    private String foreignId;
    private Date orderNumber;
    private int dataUpload;
    private String uploadUrl;
    private int sortNo;
    @Generated(hash = 1789407090)
    public PictureBean(String id, String path, String foreignId, Date orderNumber,
            int dataUpload, String uploadUrl, int sortNo) {
        this.id = id;
        this.path = path;
        this.foreignId = foreignId;
        this.orderNumber = orderNumber;
        this.dataUpload = dataUpload;
        this.uploadUrl = uploadUrl;
        this.sortNo = sortNo;
    }
    @Generated(hash = 356585384)
    public PictureBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getForeignId() {
        return this.foreignId;
    }
    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }
    public Date getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(Date orderNumber) {
        this.orderNumber = orderNumber;
    }
    public int getDataUpload() {
        return this.dataUpload;
    }
    public void setDataUpload(int dataUpload) {
        this.dataUpload = dataUpload;
    }
    public String getUploadUrl() {
        return this.uploadUrl;
    }
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
    public int getSortNo() {
        return this.sortNo;
    }
    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }
}
