package com.hbx.adsmanager.util;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 定时任务
 */
@Component
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

    /**
     * 更新账号cookie
     * @throws InterruptedException
     */
   @Scheduled(cron = "0 0 3 * * ?")
    public void updateAccountCookie() throws InterruptedException {
        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
        for (AccountSystem accountSystem:accountSystemList){
            if (accountSystem.getIsAble().equals("1")) {
                String accountSystemCookie = seleniumService.getAccountSystemCookie(accountSystem);
                accountCookieService.updateAccountCookie(accountSystem.getAccount(),accountSystemCookie);
            }
        }
    }

    /**
     * 更细后台信息
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void updateAccountSystemInfo() throws InterruptedException {
        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
        for (AccountSystem accountSystem:accountSystemList){
            if (accountSystem.getIsAble().equals("1")) {
                accountSystemService.updateAccountSystemAccountInfo(accountSystem.getClientName());
                accountSystemService.updateAccountSystemBasicInfo(accountSystem);

            }
        }
    }

    /**
     * 更新充值记录
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void updateRechargeRecord(){
        // 获取当前日期前一天的日期
        LocalDate date = LocalDate.now().minusDays(1);
        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(new QueryWrapper<>());
        for (AccountSystem system :accountSystemList) {
            adAccountRechargeRecordService.addAdAccountRechargeRecord(date.toString() , system);
            walletRechargeRecordService.addRechargeRecordList(date.toString() ,system );
            adAccountService.updateAdAccountSpendAmount(system.getClientName());
        }
    }

}
