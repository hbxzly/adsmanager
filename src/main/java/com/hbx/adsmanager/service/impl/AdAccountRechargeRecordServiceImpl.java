package com.hbx.adsmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccountRechargeRecord;
import com.hbx.adsmanager.domain.vo.AdAccountRechargeRecordVo;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.mapper.AdAccountRechargeRecordMapper;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AdAccountRechargeRecordService;
import com.hbx.adsmanager.util.CustomHttpClient;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdAccountRechargeRecordServiceImpl implements AdAccountRechargeRecordService {

    @Autowired
    AdAccountRechargeRecordMapper adAccountRechargeRecordMapper;

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    AccountCookieService accountCookieService;

    @Override
    public PageBean<AdAccountRechargeRecord> getAdAccountRechargeRecordList(int page, int rows, AdAccountRechargeRecord adAccountRechargeRecord) {

        PageBean<AdAccountRechargeRecord> pageBean = new PageBean<>(page,rows);

        QueryWrapper<AdAccountRechargeRecord> adAccountRechargeRecordQueryWrapper = new QueryWrapper<>();
        if (adAccountRechargeRecord.getAccountSystem() != null && adAccountRechargeRecord.getAccountSystem() != ""){
            adAccountRechargeRecordQueryWrapper.like("account_system",adAccountRechargeRecord.getAccountSystem());
        }
        if (adAccountRechargeRecord.getChannelAccountId() != null && adAccountRechargeRecord.getChannelAccountId() != ""){
            adAccountRechargeRecordQueryWrapper.like("channel_account_id",adAccountRechargeRecord.getChannelAccountId());
        }
        adAccountRechargeRecordQueryWrapper.orderByDesc("pay_time");

        Page<AdAccountRechargeRecord> adAccountRechargeRecordPage = new Page<>(page,rows);
        Page<AdAccountRechargeRecord> selectPage = adAccountRechargeRecordMapper.selectPage(adAccountRechargeRecordPage, adAccountRechargeRecordQueryWrapper);

        pageBean.setDatas(selectPage.getRecords());
        pageBean.setTotalsize((int) adAccountRechargeRecordPage.getTotal());


        return pageBean;
    }

    @Override
    public void updateAdAccountRechargeRecord(AdAccountRechargeRecord adAccountRechargeRecord) {

        UpdateWrapper<AdAccountRechargeRecord> adAccountRechargeRecordUpdateWrapper = new UpdateWrapper<>();
        adAccountRechargeRecordUpdateWrapper.eq("tid",adAccountRechargeRecord.getTid())
                .set("pay_card",adAccountRechargeRecord.getPayCard())
                .set("receiver",adAccountRechargeRecord.getReceiver())
                .set("note",adAccountRechargeRecord.getNote());
        adAccountRechargeRecordMapper.update(null,adAccountRechargeRecordUpdateWrapper);
    }

    @Override
    public void addAdAccountRechargeRecord(String startTime, AccountSystem accountSystem) {
        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endDateFormat = endDate.format(formatter);
        String requestData = null;
        try {
            requestData = "{\"pageSize\": \"50\",\"pageNum\": \"1\",\"endDate\": \"" + endDateFormat + "\",\"startDate\": \"" + startTime + "\"}";
            String tradeOrderStr = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_TRADE_ORDER_LIST_POST,
                    accountCookieService.queryCookie(accountSystem.getAccount()), requestData);
            Map<String, String> tradeOrderMap = JSON.parseObject(tradeOrderStr, new TypeReference<HashMap<String, String>>(){});
            Map<String, String> tradeOrderMapResult = JSON.parseObject(tradeOrderMap.get("result"), new TypeReference<HashMap<String, String>>() {});
            int total = Integer.parseInt(tradeOrderMapResult.get("total"));
            int page = total / 50 + (total % 50 != 0 ? 1 : 0);
            for (int i = 1; i < page + 1; i++) {
                requestData = "{\"pageSize\": \"50\",\"pageNum\": \"" + i + "\",\"endDate\": \"" + endDateFormat + "\",\"startDate\": \"" + startTime + "\"}";
                String pageTradeOrderStr = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_TRADE_ORDER_LIST_POST,
                        accountCookieService.queryCookie(accountSystem.getAccount()), requestData);
                Map<String, String> pageTradeOrderMap = JSON.parseObject(pageTradeOrderStr, new TypeReference<HashMap<String, String>>() {});
                Map<String, String> pageTradeOrderMapResult = JSON.parseObject(pageTradeOrderMap.get("result"), new TypeReference<HashMap<String, String>>() {});
                List<AdAccountRechargeRecordVo> adAccountRechargeRecordVoList = JSONArray.parseArray(pageTradeOrderMapResult.get("result"), AdAccountRechargeRecordVo.class);
                for (AdAccountRechargeRecordVo adAccountRechargeRecordVo : adAccountRechargeRecordVoList) {
                    requestData = "{\"tradeId\": \"" + adAccountRechargeRecordVo.getTradeId() + "\"}";
                    String queryTradeOrderStr = CustomHttpClient.postRequest(SinoClickRequestUrl.QUERY_TRADE_ORDER_POST,
                            accountCookieService.queryCookie(accountSystem.getAccount()), requestData);
                    Map<String, String> queryTradeOrderMap = JSON.parseObject(queryTradeOrderStr, new TypeReference<HashMap<String, String>>() {});
                    Map<String, String> queryTradeOrderMapResult = JSON.parseObject(queryTradeOrderMap.get("result"), new TypeReference<HashMap<String, String>>() {});
                    Map<String, String> walletAmountMap = JSON.parseObject(queryTradeOrderMapResult.get("walletAmount"), new TypeReference<HashMap<String, String>>() {});
                    Map<String, String> tradeSubAmountMap = JSON.parseObject(queryTradeOrderMapResult.get("tradeSubAmount"), new TypeReference<HashMap<String, String>>() {});
                    AdAccountRechargeRecord adAccountRechargeRecord = new AdAccountRechargeRecord();
                    adAccountRechargeRecord.setTradeId(adAccountRechargeRecordVo.getTradeId());
                    adAccountRechargeRecord.setTid(adAccountRechargeRecordVo.getTid());
                    adAccountRechargeRecord.setPayMethod(adAccountRechargeRecordVo.getPayMethod());
                    adAccountRechargeRecord.setCreateTime(adAccountRechargeRecordVo.getCreateTime());
                    if (adAccountRechargeRecordVo.getTradeStatusName().equals("支付成功")) {
                        adAccountRechargeRecord.setPayTime(adAccountRechargeRecordVo.getPayTime());
                    }
                    adAccountRechargeRecord.setTradeStatusName(adAccountRechargeRecordVo.getTradeStatusName());
                    adAccountRechargeRecord.setUsdAmount(adAccountRechargeRecordVo.getTradeAmount().getUsdAmount());
                    adAccountRechargeRecord.setCnyAmount(adAccountRechargeRecordVo.getTradeAmount().getCnyAmount());
                    adAccountRechargeRecord.setExchangeRate(queryTradeOrderMapResult.get("exchangeRate"));
                    adAccountRechargeRecord.setWalletUsdAmount(Double.parseDouble(walletAmountMap.get("usdAmount")));
                    adAccountRechargeRecord.setWalletCnyAmount(Double.parseDouble(walletAmountMap.get("cnyAmount")));
                    adAccountRechargeRecord.setTradeSubUsdAmount(Double.parseDouble(tradeSubAmountMap.get("usdAmount")));
                    adAccountRechargeRecord.setTradeSubCnyAmount(Double.parseDouble(tradeSubAmountMap.get("cnyAmount")));
                    adAccountRechargeRecord.setTradeDetailTypeDesc(adAccountRechargeRecordVo.getTradeDetailList().get(0).getTradeDetailTypeDesc());
                    adAccountRechargeRecord.setChannelId(adAccountRechargeRecordVo.getTradeDetailList().get(0).getChannelId());
                    adAccountRechargeRecord.setChannelAccountId(adAccountRechargeRecordVo.getTradeDetailList().get(0).getChannelAccountId());
                    adAccountRechargeRecord.setAccountSystem(accountSystem.getClientName());
                    if (accountSystem.getIsSell() != "" && accountSystem.getIsSell() != null && accountSystem.getIsSell().equals("1")) {
                        adAccountRechargeRecord.setAccountSystemIsSell("1");
                    }
                    if (!adAccountRechargeRecordVo.getTradeStatusName().equals("已取消")) {
                        adAccountRechargeRecordMapper.insert(adAccountRechargeRecord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
