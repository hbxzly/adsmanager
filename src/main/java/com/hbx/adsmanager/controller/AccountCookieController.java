package com.hbx.adsmanager.controller;

import com.hbx.adsmanager.domain.AccountCookie;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.SeleniumService;
import com.hbx.adsmanager.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/accountCookie")
public class AccountCookieController {

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    SeleniumService seleniumService;

    @Autowired
    AccountSystemService accountSystemService;

    @RequestMapping("getAccountCookieList")
    @ResponseBody
    public Object getAccountCookieList(int page, int rows, AccountCookie accountCookie){

        PageBean<AccountCookie> pageBean = accountCookieService.queryAccountCookie(page, rows, accountCookie);
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("total",pageBean.getTotalsize());
        hashMap.put("rows",pageBean.getDatas());

        return hashMap;
    }

    @RequestMapping("updateAccountCookie")
    @ResponseBody
    public void updateAccountCookie(String[] accounts) throws InterruptedException {

        for (String account:accounts){
            String cookie = seleniumService.getAccountSystemCookie(accountSystemService.queryAccountSystemByAccount(account));
            accountCookieService.updateAccountCookie(account,cookie);
        }

    }

    @RequestMapping("updateAllAccountCookie")
    @ResponseBody
    public void updateAllAccountCookie(String[] accounts) {
        accountCookieService.updateAllAccountCookie();
    }


}
