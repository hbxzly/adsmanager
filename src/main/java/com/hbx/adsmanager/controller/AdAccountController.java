package com.hbx.adsmanager.controller;


import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.AdAccountService;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/adAccount")
public class AdAccountController {

    @Autowired
    AdAccountService adAccountService;

    @Autowired
    AccountSystemService accountSystemService;

    @RequestMapping("getAdAccountList")
    @ResponseBody
    public Object getAdAccountList(@RequestParam(value = "page", defaultValue = "1")int page,
                                             @RequestParam(value = "rows", defaultValue = "20")int rows,
                                             AdAccount adAccount){
//        System.out.println(adAccount);
        PageBean<AdAccount> pageBean = adAccountService.queryAdAccountList(page, rows, adAccount);
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("total",pageBean.getTotalsize());
        hashMap.put("rows",pageBean.getDatas());

        return hashMap;
    }

    @RequestMapping("findAccountSystemByClientName")
    @ResponseBody
    public String findAccountSystemByClientName(String clientName){
        AccountSystem accountSystem = accountSystemService.queryAccountSystemByClientName(clientName);
        return "后台余额："+accountSystem.getClientBalance()+"----------备注："+ StringUtil.isSellStr(accountSystem.getIsSell());
    }

    @RequestMapping("bindBm")
    @ResponseBody
    public String bindBm(String accountSystem, String adAccountId, String bmId) throws InterruptedException {
        return adAccountService.bindBm(accountSystem, adAccountId, bmId);
    }

    @RequestMapping("unBindBm")
    @ResponseBody
    public String unBindBm(String accountSystem, String adAccountId, String bmId) throws InterruptedException {
        return adAccountService.unBindBm(accountSystem, adAccountId, bmId);
    }

    @RequestMapping("updateAccountSystemAdAccount")
    @ResponseBody
    public void updateAccountSystemAdAccount(String clientName){
        accountSystemService.updateAccountSystemAccountInfo(clientName);
    }


}
