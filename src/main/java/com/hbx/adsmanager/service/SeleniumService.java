package com.hbx.adsmanager.service;


import com.hbx.adsmanager.domain.AccountSystem;

import java.util.HashMap;
import java.util.Map;

public interface SeleniumService {

    void loginAccountSystem(AccountSystem accountSystem);

    void closeChrome();

    void adAccountRecharge(String accountSystem, String id, String payment, String amount);

    String getAccountSystemCookie(AccountSystem accountSystem);




}
