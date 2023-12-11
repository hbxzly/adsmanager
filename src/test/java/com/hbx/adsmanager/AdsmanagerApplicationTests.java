package com.hbx.adsmanager;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.domain.WalletRechargeRecord;
import com.hbx.adsmanager.mapper.AccountCookieMapper;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.mapper.AdAccountMapper;
import com.hbx.adsmanager.service.*;
import com.hbx.adsmanager.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
    AdAccountMapper adAccountMapper;

    @Autowired
    SeleniumService seleniumService;

    @Autowired
    AccountCookieService accountCookieService;


    @Autowired
    AdAccountRechargeRecordService adAccountRechargeRecordService;

    @Autowired
    RechargeQRCodeService rechargeQRCodeService;

    @Test
    public void contextLoads() {

//        accountSystemService.updateAccountSystemAccountInfo("通州区文之博纺织品厂");
    }



}
