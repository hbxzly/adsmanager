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

    /**
     * 获取广告账户列表
     * @param page
     * @param rows
     * @param adAccount
     * @return
     */
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

    /**
     * 根据后台名称查找后台
     * @param clientName
     * @return
     */
    @RequestMapping("findAccountSystemByClientName")
    @ResponseBody
    public String findAccountSystemByClientName(String clientName){
        AccountSystem accountSystem = accountSystemService.queryAccountSystemByClientName(clientName);
        return "后台余额："+accountSystem.getClientBalance()+"----------备注："+ StringUtil.isSellStr(accountSystem.getIsSell());
    }


    /**
     * 绑定BM
     * @param accountSystem
     * @param adAccountId
     * @param bmId
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("bindBm")
    @ResponseBody
    public String bindBm(String accountSystem, String adAccountId, String bmId) throws InterruptedException {
        return adAccountService.bindBm(accountSystem, adAccountId, bmId);
    }

    /**
     * 解绑BM
     * @param accountSystem
     * @param adAccountId
     * @param bmId
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("unBindBm")
    @ResponseBody
    public String unBindBm(String accountSystem, String adAccountId, String bmId) throws InterruptedException {
        return adAccountService.unBindBm(accountSystem, adAccountId, bmId);
    }

    /**
     * 根据后台更新账户
     * @param clientName
     */
    @RequestMapping("updateAccountSystemAdAccount")
    @ResponseBody
    public void updateAccountSystemAdAccount(String[] clientName){
        for (String accountSystem :clientName) {
            accountSystemService.updateAccountSystemAccountInfo(accountSystem);
            adAccountService.updateAdAccountSpendAmount(accountSystem);
        }

    }


}
