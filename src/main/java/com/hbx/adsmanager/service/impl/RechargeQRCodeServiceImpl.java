package com.hbx.adsmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.RechargeQRCode;
import com.hbx.adsmanager.mapper.RechargeQRCodeMapper;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.RechargeQRCodeService;
import com.hbx.adsmanager.util.CustomHttpClient;
import com.hbx.adsmanager.util.JsonParseUtil;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RechargeQRCodeServiceImpl implements RechargeQRCodeService {

    @Autowired
    RechargeQRCodeMapper rechargeQRCodeMapper;

    @Autowired
    AccountCookieService accountCookieService;


    @Override
    public PageBean<RechargeQRCode> queryRechargeQRCodeList(int page, int rows, RechargeQRCode rechargeQRCode) {

        PageBean<RechargeQRCode> pageBean = new PageBean<>(page, rows);
        QueryWrapper<RechargeQRCode> rechargeQRCodeQueryWrapper = new QueryWrapper<>();
        rechargeQRCodeQueryWrapper.orderByDesc("create_time");
        Page<RechargeQRCode> rechargeQRCodePage = new Page<>(page, rows);
        Page<RechargeQRCode> selectPage = rechargeQRCodeMapper.selectPage(rechargeQRCodePage, rechargeQRCodeQueryWrapper);

        pageBean.setDatas(selectPage.getRecords());
        pageBean.setTotalsize((int) rechargeQRCodePage.getTotal());

        return pageBean;
    }

    @Override
    public void saveRechargeQRCode(RechargeQRCode rechargeQRCode) {
       rechargeQRCodeMapper.insert(rechargeQRCode);
    }

    @Override
    public IPage<RechargeQRCode> getRechargeQRCodePage(IPage<RechargeQRCode> page) {
        return rechargeQRCodeMapper.selectPage(page, null);
    }

    @Override
    public String checkRechargeStatus(String tradeTid) throws InterruptedException {

        String cookie = getCookieByTradeId(tradeTid);
        String requestData = "{\"tradeTid\": \"" + tradeTid + "\"}";
        CustomHttpClient.postRequest(SinoClickRequestUrl.QUERY_PAY_RESULT,cookie,requestData);
        return CustomHttpClient.postRequest(SinoClickRequestUrl.QUERY_PAY_RESULT,cookie,requestData);
    }

    @Override
    public String getCookieByTradeId(String tradeTid) {

        QueryWrapper<RechargeQRCode> rechargeQRCodeQueryWrapper = new QueryWrapper<>();
        rechargeQRCodeQueryWrapper.eq("trade_tid",tradeTid);
        RechargeQRCode rechargeQRCode = rechargeQRCodeMapper.selectOne(rechargeQRCodeQueryWrapper);

        return accountCookieService.queryCookieByAccountSystem(rechargeQRCode.getAdAccountSystem());
    }

    @Override
    public void updateRechargeStatus(String tradeTid) throws InterruptedException, IOException {
        String status = JsonParseUtil.paresRechargeStatus(checkRechargeStatus(tradeTid));
        UpdateWrapper<RechargeQRCode> rechargeQRCodeUpdateWrapper = new UpdateWrapper<>();
        rechargeQRCodeUpdateWrapper.eq("trade_tid",tradeTid)
                .set("recharge_status",status);
        rechargeQRCodeMapper.update(null,rechargeQRCodeUpdateWrapper);
    }


}
