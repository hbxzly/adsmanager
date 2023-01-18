package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.Bm;

import java.util.List;

public interface BmService {

    List<Bm> getBmList(String accountSystem,String adAccountId) throws InterruptedException;

}
