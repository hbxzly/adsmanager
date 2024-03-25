package com.hbx.adsmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.Bm;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.BmService;
import com.hbx.adsmanager.util.CustomHttpClient;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BmServiceImpl implements BmService {

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AccountCookieService accountCookieService;

    @Override
    public List<Bm> getBmList(String accountSystem,String adAccountId) throws InterruptedException {
        System.out.println("getBmList");
        AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
        String cookie = accountCookieService.queryCookie(system.getAccount());
        String GET_ACCOUNT_BM_LIST_RD = "{\"type\": \"1\",\"channelId\": \"1\",\"adAccountId\": \""+adAccountId+"\"}";
        String s = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_ACCOUNT_BM_LIST_POST, cookie, GET_ACCOUNT_BM_LIST_RD);
        Map<String, String> map = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
        System.out.println("ceshi:"+map);
        if (map.get("code").equals("0")){
            List<Bm> bmList = JSONArray.parseArray(map.get("result"),Bm.class);
            return bmList;
        }
        return null;
    }
}
