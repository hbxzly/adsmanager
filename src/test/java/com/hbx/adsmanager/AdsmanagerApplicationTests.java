package com.hbx.adsmanager;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbx.adsmanager.domain.AccountCookie;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.mapper.AccountCookieMapper;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.service.*;
import com.hbx.adsmanager.util.CookieUtil;
import com.hbx.adsmanager.util.StringUtil;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AdsmanagerApplicationTests {

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AdAccountService adAccountService;

    @Autowired
    AccountCookieMapper accountCookieMapper;

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    SeleniumService seleniumService;

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    AdAccountRechargeRecordService adAccountRechargeRecordService;

    @Test
    public void contextLoads() {

    }
/*
    @Test
    public void test1(){
        String strings = StringUtil.cleanBlank("this is    a good idea");
        System.out.println(strings);
    }

    @Test
    public void test2() throws InterruptedException {
        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.eq("account","18050013533");
        AccountSystem accountSystem = accountSystemMapper.selectOne(accountSystemQueryWrapper);
        String systemCookie = seleniumService.getAccountSystemCookie(accountSystem);
        accountCookieService.updateAccountCookie(accountSystem.getAccount(),systemCookie);
    }


    @Test
    public void test3(){
        QueryWrapper<AccountCookie> accountCookieQueryWrapper = new QueryWrapper<>();
        accountCookieQueryWrapper.eq("account","18050013533");
        AccountCookie accountCookie = accountCookieMapper.selectOne(accountCookieQueryWrapper);

        System.out.println(CookieUtil.checkCookie(accountCookie));

    }
      @Test
    public void test5() throws IOException {
//        adAccountService.updateAdAccountSpendAmount("绍兴皕匹进出口有限公司");
        int s=0;
        for (int n=1;n<=10;n++){
            if ( n%3==0){
                s=s+n;
            }
        }
        System.out.println(s);
    }

    @Test
    public void test4() throws InterruptedException {

        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.eq("account","16521633235");
        AccountSystem accountSystem = accountSystemMapper.selectOne(accountSystemQueryWrapper);


    }
*/
   /* @Test
    public void test4() throws InterruptedException {
        AccountSystem accountSystem = accountSystemService.queryAccountSystemByClientName("未阳市广进电子商务有限公司");
        adAccountRechargeRecordService.addAdAccountRechargeRecord("2023-01-14",accountSystem);
    }*/
}
