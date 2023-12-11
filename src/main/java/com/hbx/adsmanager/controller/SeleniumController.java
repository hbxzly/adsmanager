package com.hbx.adsmanager.controller;

import com.hbx.adsmanager.domain.vo.AdAccountRechargeVo;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.SeleniumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/selenium")
public class SeleniumController {

    @Autowired
    SeleniumService seleniumService;

    @Autowired
    AccountSystemService accountSystemService;


    @RequestMapping("loginAccountSystem")
    @ResponseBody
    public String loginAccountSystem(String[] accountSystems) {

        for (String accountSystemStr : accountSystems) {
            seleniumService.loginAccountSystem(accountSystemService.queryAccountSystemByClientName(accountSystemStr));
       }
        return "OK";
    }

    @RequestMapping("adAccountRecharge")
    @ResponseBody
    public String adAccountRecharge(@RequestBody List<AdAccountRechargeVo> adAccountRechargeVoList) throws InterruptedException {
        for (AdAccountRechargeVo rechargeVo :adAccountRechargeVoList) {
            seleniumService.adAccountRecharge(rechargeVo.getAdAccountSystem(),rechargeVo.getId(),rechargeVo.getPayMethod(), rechargeVo.getRechargeAmount());
        }
        return "OK";
    }

    @RequestMapping("adAccountRechargeScreenshot")
    @ResponseBody
    public String adAccountRechargeScreenshot(@RequestBody List<AdAccountRechargeVo> adAccountRechargeVoList) throws InterruptedException {
        for (AdAccountRechargeVo rechargeVo :adAccountRechargeVoList) {
            seleniumService.adAccountRechargeScreenshot(rechargeVo.getAdAccountSystem(),rechargeVo.getId(),rechargeVo.getPayMethod(), rechargeVo.getRechargeAmount());
        }
        return "OK";
    }

    @RequestMapping("adAccountRechargeScreenshot")
    @ResponseBody
    public String adAccountRechargeByWallet(@RequestBody List<AdAccountRechargeVo> adAccountRechargeVoList) throws InterruptedException {
        for (AdAccountRechargeVo rechargeVo :adAccountRechargeVoList) {
            seleniumService.adAccountRechargeScreenshot(rechargeVo.getAdAccountSystem(),rechargeVo.getId(),rechargeVo.getPayMethod(), rechargeVo.getRechargeAmount());
        }
        return "OK";
    }

    @RequestMapping("closeChrome")
    @ResponseBody
    public String closeChrome(){
        seleniumService.closeChrome();
        return "OK";
    }


}
