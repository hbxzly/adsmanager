package com.hbx.adsmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccountRechargeRecord;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.service.AdAccountRechargeRecordService;
import com.hbx.adsmanager.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adAccountRechargeRecord")
public class AdAccountRechargeRecordController {

    @Autowired
    AdAccountRechargeRecordService adAccountRechargeRecordService;

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @RequestMapping("getAdAccountRechargeRecordList")
    @ResponseBody
    public Object getAdAccountRechargeRecordList(int page, int rows, AdAccountRechargeRecord adAccountRechargeRecord){

        PageBean<AdAccountRechargeRecord> pageBean = adAccountRechargeRecordService.getAdAccountRechargeRecordList(page, rows, adAccountRechargeRecord);

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("total",pageBean.getTotalsize());
        hashMap.put("rows",pageBean.getDatas());

        return hashMap;
    }

    @RequestMapping("updateAdAccountRechargeRecord")
    @ResponseBody
    public void updateAdAccountRechargeRecord(@RequestBody List<AdAccountRechargeRecord> adAccountRechargeRecordList){
        for (AdAccountRechargeRecord adRR : adAccountRechargeRecordList  ) {
            adAccountRechargeRecordService.updateAdAccountRechargeRecord(adRR);
        }
    }

    @RequestMapping("addAdAccountRechargeRecord")
    @ResponseBody
    public void addAdAccountRechargeRecord(String startTime){

        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
        for(AccountSystem accountSystem:accountSystemList){
            adAccountRechargeRecordService.addAdAccountRechargeRecord(startTime , accountSystem);
        }
    }

}
