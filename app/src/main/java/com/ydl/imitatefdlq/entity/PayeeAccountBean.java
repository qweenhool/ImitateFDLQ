package com.ydl.imitatefdlq.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by qweenhool on 2017/8/30.
 */
@Entity
public class PayeeAccountBean {
    @Id
    private String id;
    private String accountName;//账户名称
    private Date orderNumber;//用来排序
    private int accountType;//账户类型
    private String cardHolder;//持卡人
    private String cardNumber;//卡号
    private String depositBank;//开户行
    private String depositBranch;//开户支行
    private String wechatAccount;//微信号
    private String alipayAccount;//支付宝账号
    private String remark;//备注
    @Generated(hash = 1087120842)
    public PayeeAccountBean(String id, String accountName, Date orderNumber,
            int accountType, String cardHolder, String cardNumber,
            String depositBank, String depositBranch, String wechatAccount,
            String alipayAccount, String remark) {
        this.id = id;
        this.accountName = accountName;
        this.orderNumber = orderNumber;
        this.accountType = accountType;
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.depositBank = depositBank;
        this.depositBranch = depositBranch;
        this.wechatAccount = wechatAccount;
        this.alipayAccount = alipayAccount;
        this.remark = remark;
    }
    @Generated(hash = 997852034)
    public PayeeAccountBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccountName() {
        return this.accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public Date getOrderNumber() {
        return this.orderNumber;
    }
    public void setOrderNumber(Date orderNumber) {
        this.orderNumber = orderNumber;
    }
    public int getAccountType() {
        return this.accountType;
    }
    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
    public String getCardHolder() {
        return this.cardHolder;
    }
    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }
    public String getCardNumber() {
        return this.cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getDepositBank() {
        return this.depositBank;
    }
    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }
    public String getDepositBranch() {
        return this.depositBranch;
    }
    public void setDepositBranch(String depositBranch) {
        this.depositBranch = depositBranch;
    }
    public String getWechatAccount() {
        return this.wechatAccount;
    }
    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }
    public String getAlipayAccount() {
        return this.alipayAccount;
    }
    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
