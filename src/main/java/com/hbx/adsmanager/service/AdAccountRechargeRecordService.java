package com.hbx.adsmanager.service;

import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccountRechargeRecord;
import com.hbx.adsmanager.util.PageBean;

public interface AdAccountRechargeRecordService {

    /**
     * 获取账号充值列表
     * @param page
     * @param rows
     * @param adAccountRechargeRecord
     * @return
     */
    PageBean<AdAccountRechargeRecord> getAdAccountRechargeRecordList(int page, int rows, AdAccountRechargeRecord adAccountRechargeRecord);

    /**
     * 修改账号充值记录
     * @param adAccountRechargeRecord
     */
    void updateAdAccountRechargeRecord(AdAccountRechargeRecord adAccountRechargeRecord);

    /**
     * 新增广告账户充值记录
     * @param startTime
     * @param accountSystem
     */
    void addAdAccountRechargeRecord(String startTime , AccountSystem accountSystem);

}
