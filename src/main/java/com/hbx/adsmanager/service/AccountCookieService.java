package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountCookie;
import com.hbx.adsmanager.util.PageBean;

public interface AccountCookieService {

    PageBean<AccountCookie> queryAccountCookie(int page, int rows, AccountCookie accountCookie);

    String queryCookie(String account);

    void insertAccountCookie(String account);

    void updateAccountCookie(String account, String cookie);

    void updateAllAccountCookie();

    String queryCookieByAccountSystem(String accountSystem);
}
