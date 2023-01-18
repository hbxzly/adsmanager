package com.hbx.adsmanager.domain.vo;

public class AdAccountRechargeVo {

    private String adAccountSystem;
    private String id;
    private String rechargeAmount;
    private String payMethod;

    public String getAdAccountSystem() {
        return adAccountSystem;
    }

    public void setAdAccountSystem(String adAccountSystem) {
        this.adAccountSystem = adAccountSystem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public String toString() {
        return "AdAccountRechargeVo{" +
                "adAccountSystem='" + adAccountSystem + '\'' +
                ", id='" + id + '\'' +
                ", rechargeAmount='" + rechargeAmount + '\'' +
                ", payMethod='" + payMethod + '\'' +
                '}';
    }
}
