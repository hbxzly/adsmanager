package com.hbx.adsmanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.SeleniumService;
import com.hbx.adsmanager.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/accountSystem")
public class AccountSystemController {

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    SeleniumService seleniumService;

    @RequestMapping("getAccountSystemList")
    @ResponseBody
    public Object getAccountSystemList(@RequestParam(value = "page", defaultValue = "1")int page,
                                                    @RequestParam(value = "rows", defaultValue = "20")int rows,
                                                    AccountSystem accountSystem){

        PageBean<AccountSystem> pageBean = accountSystemService.queryAccountSystemList(page, rows, accountSystem);
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("total",pageBean.getTotalsize());
        hashMap.put("rows",pageBean.getDatas());

        return hashMap;
    }


    @RequestMapping("/addAccountSystem")
    @ResponseBody
    public String addAccountSystem(AccountSystem accountSystem){
        accountSystemService.addAccountSystem(accountSystem);
        accountCookieService.insertAccountCookie(accountSystem.getAccount());
        return "OK";
    }

    @RequestMapping("updateAccountSystemBasicInfo")
    @ResponseBody
    public String updateAccountSystemBasicInfo(String[] accountSystems) {
        for (String accountSystemStr : accountSystems) {
            accountSystemService.updateAccountSystemBasicInfo(accountSystemService.queryAccountSystemByClientName(accountSystemStr));
        }
        return "OK";
    }

    @RequestMapping("updateAccountSystemAccountInfo")
    @ResponseBody
    public String updateAccountSystemAccountInfo(String[] accountSystems) {
        for (String accountSystem : accountSystems) {
            accountSystemService.updateAccountSystemAccountInfo(accountSystem);
         }
        return "OK";
    }

    @RequestMapping("queryAdAccountByAccountSystem")
    @ResponseBody
    public List<AdAccount> queryAdAccountByAccountSystem(String clientName){
        return accountSystemService.queryAdAccountByAccountSystem(clientName);
    }

    @RequestMapping("updateAccountSystem")
    @ResponseBody
    public String updateAccountSystem(AccountSystem accountSystem){
       accountSystemService.updateAccountSystem(accountSystem);
       return "OK";
    }

    @RequestMapping("updateAccountSystemForSell")
    @ResponseBody
    public String updateAccountSystemForSell(AccountSystem accountSystem){
       accountSystemService.updateAccountSystemForSell(accountSystem);
        return "OK";
    }

    @RequestMapping("updateCookie")
    @ResponseBody
    public String updateCookie(@RequestBody AccountSystem accountSystem){
        String accountSystemCookie = seleniumService.getAccountSystemCookie(accountSystem);
        accountCookieService.updateAccountCookie(accountSystem.getAccount(),accountSystemCookie);
        return "OK";
    }




}
