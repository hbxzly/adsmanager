package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.util.PageBean;

import java.util.List;

public interface AccountSystemService {

    /**
     * 获取后台系统列表
     * @param page
     * @param rows
     * @param accountSystem
     * @return
     */
    PageBean<AccountSystem> queryAccountSystemList(int page, int rows, AccountSystem accountSystem);

    /**
     * 通过后台名称获取后台
     * @param clientName
     * @return
     */
    AccountSystem queryAccountSystemByClientName(String clientName);

    /**
     * 通过账号（电话）获取后台
     * @param account
     * @return
     */
    AccountSystem queryAccountSystemByAccount(String account);

    /**
     * 新增后台
     * @param accountSystem
     */
    void addAccountSystem(AccountSystem accountSystem);

    /**
     * 更新后台基础信息
     * @param accountSystem
     */
    void updateAccountSystemBasicInfo(AccountSystem accountSystem);

    /**
     * 更新后台账号信息
     * @param clientName
     */
    void updateAccountSystemAccountInfo(String clientName);

    /**
     * 更新后台
     * @param accountSystem
     */
    void updateAccountSystem(AccountSystem accountSystem);

    /**
     * 更新后台是否被卖
     * @param accountSystem
     */
    void updateAccountSystemForSell(AccountSystem accountSystem);

    /**
     * 统计后台广告账户
     * @param clientName
     * @return
     */
    AccountSystem countAccountSystemAdAccount(String clientName);

    /**
     * 通过后台名称查询后台名下广告账户
     * @param clientName
     * @return
     */
    List<AdAccount> queryAdAccountByAccountSystem(String clientName);


}
