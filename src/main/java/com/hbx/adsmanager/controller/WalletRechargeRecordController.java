package com.hbx.adsmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.WalletRechargeRecord;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.WalletRechargeRecordService;
import com.hbx.adsmanager.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/walletRechargeRecord")
public class WalletRechargeRecordController {

    @Autowired
    WalletRechargeRecordService walletRechargeRecordService;

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    AccountSystemService accountSystemService;

    @RequestMapping("getWalletRechargeRecordList")
    @ResponseBody
    public Object getWalletRechargeRecordList(int page, int rows, WalletRechargeRecord walletRechargeRecord){

        PageBean<WalletRechargeRecord> pageBean = walletRechargeRecordService.queryWalletRechargeRecordList(page, rows, walletRechargeRecord);
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("total",pageBean.getTotalsize());
        hashMap.put("rows",pageBean.getDatas());

        return hashMap;

    }

    @RequestMapping("addRechargeRecordList")
    @ResponseBody
    public void addRechargeRecordList(String startTime, String accountSystem) {

        if (accountSystem != "" && accountSystem != null) {
            AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
            walletRechargeRecordService.addRechargeRecordList(startTime, system);
        } else {
            List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
            for (AccountSystem accountSystemTemp : accountSystemList) {
                walletRechargeRecordService.addRechargeRecordList(startTime, accountSystemTemp);
            }

        }
//        System.out.println(startTime);
    }

}
