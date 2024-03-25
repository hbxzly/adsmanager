package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.util.PageBean;

import java.util.List;

public interface AdAccountService {

    /**
     * 获取广告账户列表
     * @param page
     * @param rows
     * @param adAccount
     * @return
     */
    PageBean<AdAccount> queryAdAccountList(int page, int rows ,AdAccount adAccount);

    /**
     * 更新广告账户消耗
     * @param clientName
     */
    void updateAdAccountSpendAmount(String clientName);

    /**
     * 通过后台名称获取后台
     * @param adAccountSystem
     * @return
     */
    List<AdAccount> queryAdAccountListByClentName(String adAccountSystem);

    /**
     * 更新广告账户
     * @param adAccount
     * @param isSell
     */
    void updateAdAccount(AdAccount adAccount, String isSell);

    /**
     * 绑定BM
     * @param accountSystem
     * @param adAccountId
     * @param bmId
     * @return
     * @throws InterruptedException
     */
    String bindBm(String accountSystem,String adAccountId,String bmId) throws InterruptedException;

    /**
     * 解绑BM
     * @param accountSystem
     * @param adAccountId
     * @param bmId
     * @return
     * @throws InterruptedException
     */
    String unBindBm(String accountSystem,String adAccountId,String bmId) throws InterruptedException;


}
