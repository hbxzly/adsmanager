package com.hbx.adsmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemController {

    /**
     * 跳转后台主页
     * @return
     */
    @GetMapping({"/system/index", "/system/","/"})
    public String index(){
        return "system/index";
    }

    @GetMapping("/system/adAccount")
    public String adAccountList(){
        return "adAccount/adAccountList";
    }

    @GetMapping("/system/accountSystem")
    public String accountSystemList(){
        return "accountSystem/accountSystemList";
    }


    @GetMapping("/system/accountCookie")
    public String accountCookieList(){
        return "accountCookie/accountCookieList";
    }


    @GetMapping("/system/walletRechargeRecord")
    public String walletRechargeRecordList(){
        return "walletRechargeRecord/walletRechargeRecordList";
    }

    @GetMapping("/system/adAccountRechargeRecord")
    public String adAccountRechargeRecordList(){
        return "adAccountRechargeRecord/adAccountRechargeRecordList";

    }

    @GetMapping("/system/rechargeQRCode")
    public String QRCodeList(){
        return "rechargeQRCode/rechargeQRCodeList";
    }


}
