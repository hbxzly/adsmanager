package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.WalletRechargeRecord;
import com.hbx.adsmanager.util.PageBean;

public interface WalletRechargeRecordService {

    PageBean<WalletRechargeRecord> queryWalletRechargeRecordList(int page, int rows, WalletRechargeRecord walletRechargeRecord);

    void addRechargeRecordList(String startTime , AccountSystem accountSystem);

}
