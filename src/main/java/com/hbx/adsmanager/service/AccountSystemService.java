package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.util.PageBean;

import java.util.List;

public interface AccountSystemService {

    PageBean<AccountSystem> queryAccountSystemList(int page, int rows, AccountSystem accountSystem);

    AccountSystem queryAccountSystemByClientName(String clientName);

    AccountSystem queryAccountSystemByAccount(String account);

    void addAccountSystem(AccountSystem accountSystem);

    void updateAccountSystemBasicInfo(AccountSystem accountSystem);

    void updateAccountSystemAccountInfo(String clientName);

    void updateAccountSystem(AccountSystem accountSystem);

    void updateAccountSystemForSell(AccountSystem accountSystem);

    AccountSystem countAccountSystemAdAccount(String clientName);

    List<AdAccount> queryAdAccountByAccountSystem(String clientName);


}
