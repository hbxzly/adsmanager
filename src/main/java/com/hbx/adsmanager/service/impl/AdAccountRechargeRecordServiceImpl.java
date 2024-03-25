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
import com.hbx.adsmanager.util.JsonParseUtil;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String endDateFormat = endDate.format(formatter);
       //请求体
        String requestBody = null;
        try {
            requestBody = "{\"pageSize\": \"50\",\"pageNum\": \"1\",\"endDate\": \"" + endDateFormat + "\",\"startDate\": \"" + startTime + "\"}";
            String cookie = accountCookieService.queryCookie(accountSystem.getAccount());
            //订单列表
            String tradeOrderList = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_TRADE_ORDER_LIST_POST,cookie, requestBody);
            //转化订单列表为Map
            Map<String, Object> tradeOrderMap = JsonParseUtil.parseJSON(tradeOrderList);
            int total = Integer.parseInt(tradeOrderMap.get("result.total").toString());
            int page = total / 50 + (total % 50 != 0 ? 1 : 0);
            for (int i = 1; i < page + 1; i++) {
                requestBody = "{\"pageSize\": \"50\",\"pageNum\": \"" + i + "\",\"endDate\": \"" + endDateFormat + "\",\"startDate\": \"" + startTime + "\"}";
                tradeOrderList = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_TRADE_ORDER_LIST_POST, cookie, requestBody);
                tradeOrderMap = JsonParseUtil.parseJSON(tradeOrderList);
                org.json.JSONArray adAccountRechargeRecordList = (org.json.JSONArray) tradeOrderMap.get("result.result");
                for (int j = 0; j < adAccountRechargeRecordList.length(); j++) {
                    JSONObject adAccountRechargeRecordTemp = adAccountRechargeRecordList.getJSONObject(j);
                    //一个订单多个账户充值处理
                    org.json.JSONArray tradeDetailList = adAccountRechargeRecordTemp.getJSONArray("tradeDetailList");
                    for (int k = 0; k < tradeDetailList.length(); k++) {
                        //单账户充值细节处理
                        Map<String, Object> tradeDetailMap = JsonParseUtil.parseJSON(tradeDetailList.getJSONObject(k).toString());
                        AdAccountRechargeRecord adAccountRechargeRecord = new AdAccountRechargeRecord();
                        adAccountRechargeRecord.setTid(adAccountRechargeRecordTemp.get("tid").toString());
                        adAccountRechargeRecord.setTradeId(adAccountRechargeRecordTemp.get("tradeId").toString());
                        adAccountRechargeRecord.setPayMethod(adAccountRechargeRecordTemp.get("payMethod").toString());
                        adAccountRechargeRecord.setCreateTime(formatterDate.parse(adAccountRechargeRecordTemp.get("createTime").toString()));
                        if (adAccountRechargeRecordTemp.get("tradeStatusName").equals("支付成功")){
                            adAccountRechargeRecord.setPayTime(formatterDate.parse(adAccountRechargeRecordTemp.get("payTime").toString()));
                        }
                        adAccountRechargeRecord.setAccountSystem(accountSystem.getClientName());
                        adAccountRechargeRecord.setAccountSystemIsSell(accountSystem.getIsSell());
                        adAccountRechargeRecord.setChannelAccountId(tradeDetailMap.get("channelAccountId").toString());
                        adAccountRechargeRecord.setTradeStatusName(adAccountRechargeRecordTemp.get("tradeStatusName").toString());
                        adAccountRechargeRecord.setUsdAmount(Double.parseDouble(tradeDetailMap.get("tradeDetailAmount.usdAmount").toString()));
                        adAccountRechargeRecord.setCnyAmount(Double.parseDouble(tradeDetailMap.get("tradeDetailAmount.cnyAmount").toString()));
                        adAccountRechargeRecord.setTradeDetailTypeDesc(tradeDetailMap.get("tradeDetailTypeDesc").toString());
                        adAccountRechargeRecord.setChannelId(tradeDetailMap.get("channelId").toString());
                        if (accountSystem.getIsSell() != "" && accountSystem.getIsSell() != null && accountSystem.getIsSell().equals("1")) {
                            adAccountRechargeRecord.setAccountSystemIsSell("1");
                        }
                        if (!adAccountRechargeRecordTemp.get("tradeStatusName").equals("已取消")) {
                            QueryWrapper<AdAccountRechargeRecord> adAccountRechargeRecordQueryWrapper = new QueryWrapper<>();
                            adAccountRechargeRecordQueryWrapper.eq("tid", adAccountRechargeRecordTemp.get("tid"));
                            AdAccountRechargeRecord accountRechargeRecord = adAccountRechargeRecordMapper.selectOne(adAccountRechargeRecordQueryWrapper);
                            if (accountRechargeRecord == null){
                                adAccountRechargeRecordMapper.insert(adAccountRechargeRecord);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
