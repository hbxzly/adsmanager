package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.WalletRechargeRecord;
import com.hbx.adsmanager.util.PageBean;

public interface WalletRechargeRecordService {

    /**
     * 钱包进账记录
     * @param page
     * @param rows
     * @param walletRechargeRecord
     * @return
     */
    PageBean<WalletRechargeRecord> queryWalletRechargeRecordList(int page, int rows, WalletRechargeRecord walletRechargeRecord);

    /**
     * 新增钱包进账记录
     * @param startTime
     * @param accountSystem
     */
    void addRechargeRecordList(String startTime , AccountSystem accountSystem);

}
