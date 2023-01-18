package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.util.PageBean;

import java.util.List;

public interface AdAccountService {

    PageBean<AdAccount> queryAdAccountList(int page, int rows ,AdAccount adAccount);


    void updateAdAccountSpendAmount(String clientName);

    List<AdAccount> queryAdAccountListByClentName(String adAccountSystem);

    void updateAdAccount(AdAccount adAccount, String isSell);

    String bindBm(String accountSystem,String adAccountId,String bmId) throws InterruptedException;

    String unBindBm(String accountSystem,String adAccountId,String bmId) throws InterruptedException;


}
