package com.hbx.adsmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbx.adsmanager.domain.RechargeQRCode;
import com.hbx.adsmanager.util.PageBean;

import java.io.IOException;


public interface RechargeQRCodeService {

    PageBean<RechargeQRCode> queryRechargeQRCodeList(int page, int rows, RechargeQRCode rechargeQRCode);

    void saveRechargeQRCode(RechargeQRCode rechargeEqCode);

    IPage<RechargeQRCode> getRechargeQRCodePage(IPage<RechargeQRCode> page);

    String checkRechargeStatus(String tradeTid) throws InterruptedException;

    String getCookieByTradeId(String tradeTid);

    void updateRechargeStatus(String tradeTid) throws InterruptedException, IOException;
}
