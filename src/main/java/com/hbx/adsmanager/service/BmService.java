package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.Bm;

import java.util.List;

public interface BmService {

    /**
     * 获取已绑定BM列表
     * @param accountSystem
     * @param adAccountId
     * @return
     * @throws InterruptedException
     */
    List<Bm> getBmList(String accountSystem,String adAccountId) throws InterruptedException;

}
