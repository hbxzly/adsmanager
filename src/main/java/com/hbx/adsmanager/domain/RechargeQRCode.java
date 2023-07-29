package com.hbx.adsmanager.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("recharge_qr_code")
public class RechargeQRCode {

    @TableField("trade_tid")
    private String tradeTid;

    @TableField("trade_order_id")
    private String tradeOrderId;

    @TableField("pay_method")
    private String payMethod;

    @TableField("qr_code")
    private String qrCode;

    @TableField("ad_account_id")
    private String adAccountId;

    @TableField("ad_account_system")
    private String adAccountSystem;

    @TableField("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date createTime;

    @TableField("screenshot_name")
    private String screenshotName;

    @TableField("recharge_usd")
    private String rechargeUSD;

    @TableField("recharge_cny")
    private String rechargeCNY;

    @TableField("recharge_status")
    private String rechargeStatus;

    public String getTradeTid() {
        return tradeTid;
    }

    public void setTradeTid(String tradeTid) {
        this.tradeTid = tradeTid;
    }

    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getAdAccountId() {
        return adAccountId;
    }

    public void setAdAccountId(String adAccountId) {
        this.adAccountId = adAccountId;
    }

    public String getAdAccountSystem() {
        return adAccountSystem;
    }

    public void setAdAccountSystem(String adAccountSystem) {
        this.adAccountSystem = adAccountSystem;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getScreenshotName() {
        return screenshotName;
    }

    public void setScreenshotName(String screenshotName) {
        this.screenshotName = screenshotName;
    }

    public String getRechargeUSD() {
        return rechargeUSD;
    }

    public void setRechargeUSD(String rechargeUSD) {
        this.rechargeUSD = rechargeUSD;
    }

    public String getRechargeCNY() {
        return rechargeCNY;
    }

    public void setRechargeCNY(String rechargeCNY) {
        this.rechargeCNY = rechargeCNY;
    }

    public String getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(String rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public RechargeQRCode(String tradeTid, String tradeOrderId, String payMethod, String qrCode, String adAccountId, String adAccountSystem, Date createTime, String screenshotName, String rechargeUSD, String rechargeCNY, String rechargeStatus) {
        this.tradeTid = tradeTid;
        this.tradeOrderId = tradeOrderId;
        this.payMethod = payMethod;
        this.qrCode = qrCode;
        this.adAccountId = adAccountId;
        this.adAccountSystem = adAccountSystem;
        this.createTime = createTime;
        this.screenshotName = screenshotName;
        this.rechargeUSD = rechargeUSD;
        this.rechargeCNY = rechargeCNY;
        this.rechargeStatus = rechargeStatus;
    }

    @Override
    public String toString() {
        return "RechargeQRCode{" +
                "tradeTid='" + tradeTid + '\'' +
                ", tradeOrderId='" + tradeOrderId + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", adAccountId='" + adAccountId + '\'' +
                ", adAccountSystem='" + adAccountSystem + '\'' +
                ", createTime=" + createTime +
                ", screenshotName='" + screenshotName + '\'' +
                ", rechargeUSD=" + rechargeUSD +
                ", rechargeCNY=" + rechargeCNY +
                ", rechargeStatus='" + rechargeStatus + '\'' +
                '}';
    }
}
