package com.hbx.adsmanager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.domain.RechargeQRCode;
import com.hbx.adsmanager.service.RechargeQRCodeService;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/rechargeQRCode")
public class rechargeQRCodeController {

    @Autowired
    private RechargeQRCodeService rechargeQRCodeService;

    @RequestMapping("/getRechargeQRCodeList")
    @ResponseBody
    public Object getRechargeQRCodeList(@RequestParam(value = "page", defaultValue = "1")int page,
                                   @RequestParam(value = "rows", defaultValue = "20")int rows,
                                   RechargeQRCode rechargeQRCode){
//        System.out.println(adAccount);
        PageBean<RechargeQRCode> pageBean = rechargeQRCodeService.queryRechargeQRCodeList(page, rows, rechargeQRCode);
        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("total",pageBean.getTotalsize());
        hashMap.put("rows",pageBean.getDatas());

        return hashMap;
    }



    @RequestMapping("/updateRechargeStatus")
    @ResponseBody
    public void updateRechargeStatus(String tradeTid) throws InterruptedException, IOException {
        rechargeQRCodeService.updateRechargeStatus(tradeTid);
    }

    @RequestMapping("/getUsername")
    @ResponseBody
    public String getUsername() throws InterruptedException, IOException {
        return System.getProperty("user.name");

    }




}
