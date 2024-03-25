package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountCookie;
import com.hbx.adsmanager.util.PageBean;

public interface AccountCookieService {

    /**
     * 获取cookie列表
     * @param page
     * @param rows
     * @param accountCookie
     * @return
     */
    PageBean<AccountCookie> queryAccountCookie(int page, int rows, AccountCookie accountCookie);

    /**
     * 查询cookie
     * @param account
     * @return
     */
    String queryCookie(String account);

    /**
     * 插入账号cookie
     * @param account
     */
    void insertAccountCookie(String account);

    /**
     * 更新账号cookie
     * @param account
     * @param cookie
     */
    void updateAccountCookie(String account, String cookie);

    /**
     * 更新所有账号cookie
     */
    void updateAllAccountCookie();

    /**
     * 通过后台名称查询cookie
     * @param accountSystem
     * @return
     */
    String queryCookieByAccountSystem(String accountSystem);
}
