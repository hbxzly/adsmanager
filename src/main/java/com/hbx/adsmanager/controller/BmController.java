package com.hbx.adsmanager.controller;

import com.hbx.adsmanager.domain.Bm;
import com.hbx.adsmanager.service.BmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/bm")
public class BmController {

    @Autowired
    BmService bmService;

    @RequestMapping("getAccountBmList")
    @ResponseBody
    public List<Bm> getAccountBmList(String accountSystem,String adAccountId) throws InterruptedException {
        return bmService.getBmList(accountSystem,adAccountId);
    }
}
