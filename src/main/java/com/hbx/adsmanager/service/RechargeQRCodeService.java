package com.hbx.adsmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbx.adsmanager.domain.RechargeQRCode;
import com.hbx.adsmanager.util.PageBean;

import java.io.IOException;


public interface RechargeQRCodeService {

    /**
     * 获取充值二维码列表
     * @param page
     * @param rows
     * @param rechargeQRCode
     * @return
     */
    PageBean<RechargeQRCode> queryRechargeQRCodeList(int page, int rows, RechargeQRCode rechargeQRCode);

    /**
     * 保存充值二维码
     * @param rechargeEqCode
     */
    void saveRechargeQRCode(RechargeQRCode rechargeEqCode);

    /**
     * 检查充值状态
     * @param tradeTid
     * @return
     * @throws InterruptedException
     */
    String checkRechargeStatus(String tradeTid) throws InterruptedException;

    /**
     * 订单号获取cookie
     * @param tradeTid
     * @return
     */
    String getCookieByTradeId(String tradeTid);

    /**
     * 更新充值状态
     * @param tradeTid
     * @throws InterruptedException
     * @throws IOException
     */
    void updateRechargeStatus(String tradeTid) throws InterruptedException, IOException;
}
