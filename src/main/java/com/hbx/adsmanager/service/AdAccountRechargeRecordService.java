package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccountRechargeRecord;
import com.hbx.adsmanager.util.PageBean;

public interface AdAccountRechargeRecordService {

    PageBean<AdAccountRechargeRecord> getAdAccountRechargeRecordList(int page, int rows, AdAccountRechargeRecord adAccountRechargeRecord);

    void updateAdAccountRechargeRecord(AdAccountRechargeRecord adAccountRechargeRecord);

    void addAdAccountRechargeRecord(String startTime , AccountSystem accountSystem);

}
