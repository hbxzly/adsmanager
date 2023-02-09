package com.hbx.adsmanager.util;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
public class TimedTask {

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AdAccountService adAccountService;

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    SeleniumService seleniumService;

    @Autowired
    WalletRechargeRecordService walletRechargeRecordService;

    @Autowired
    AdAccountRechargeRecordService adAccountRechargeRecordService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void updateAccountCookie() throws InterruptedException {
        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
        for (AccountSystem accountSystem:accountSystemList){
            if (accountSystem.getIsAble().equals("1")) {
                String cookie = seleniumService.getAccountSystemCookie(accountSystem);
                accountCookieService.updateAccountCookie(accountSystem.getAccount(), cookie);
                accountSystemService.updateAccountSystemBasicInfo(accountSystem);
                accountSystemService.updateAccountSystemAccountInfo(accountSystem.getClientName());
                adAccountService.updateAdAccountSpendAmount(accountSystem.getClientName());
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void updateRechargeRecord(){
        // 获取当前日期前一天的日期
        LocalDate date = LocalDate.now().minusDays(1);
        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
        for (AccountSystem system :accountSystemList) {
            adAccountRechargeRecordService.addAdAccountRechargeRecord(date.toString() , system);
            walletRechargeRecordService.addRechargeRecordList(date.toString() ,system );
        }
    }
}
