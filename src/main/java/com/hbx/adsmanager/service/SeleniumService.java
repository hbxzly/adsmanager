package com.hbx.adsmanager.service;


import com.hbx.adsmanager.domain.AccountSystem;

import java.util.HashMap;
import java.util.Map;

public interface SeleniumService {

    /**
     * 正常模式登录后台
     * @param accountSystem 后台账户
     */
    void loginAccountSystem(AccountSystem accountSystem);

    /**
     * 无头模式登录后台
     * @param accountSystem 后台账户
     */
    void loginAccountSystemHeadLess(AccountSystem accountSystem);

    /**
     * 退出浏览器
     */
    void closeChrome();

    /**
     * 账户充值
     * @param accountSystem 账户后台
     * @param id 账户id
     * @param payment 付款方式
     * @param amount 充值金额
     */
    void adAccountRecharge(String accountSystem, String id, String payment, String amount);

    /**
     * 保存充值截图
     * @param accountSystem 账户后台
     * @param id 账户id
     * @param payment 付款方式
     * @param amount 充值金额
     */
    void adAccountRechargeScreenshot(String accountSystem, String id, String payment, String amount);

    /**
     * 用钱包余额充值
     * @param accountSystem 账户后台
     * @param id 账户id
     * @param payment 付款方式
     * @param amount 充值金额
     */
    void adAccountRechargeByWallet(String accountSystem, String id, String payment, String amount);

    /**
     * 获取账户cookie
     * @param accountSystem
     * @return
     */
    String getAccountSystemCookie(AccountSystem accountSystem);






}
