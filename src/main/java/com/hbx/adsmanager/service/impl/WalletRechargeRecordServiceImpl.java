package com.hbx.adsmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.WalletRechargeRecord;
import com.hbx.adsmanager.domain.vo.WalletRechargeRecordVo;
import com.hbx.adsmanager.mapper.AccountCookieMapper;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.mapper.WalletRechargeRecordMapper;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.WalletRechargeRecordService;
import com.hbx.adsmanager.util.CustomHttpClient;
import com.hbx.adsmanager.util.JsonParseUtil;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WalletRechargeRecordServiceImpl implements WalletRechargeRecordService {

    @Autowired
    WalletRechargeRecordMapper walletRechargeRecordMapper;

    @Autowired
    AccountCookieMapper accountCookieMapper;

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    AccountCookieService accountCookieService;

    @Override
    public PageBean<WalletRechargeRecord> queryWalletRechargeRecordList(int page, int rows, WalletRechargeRecord walletRechargeRecord) {

        PageBean<WalletRechargeRecord> pageBean = new PageBean<>(page,rows);

        QueryWrapper<WalletRechargeRecord> walletRechargeRecordQueryWrapper = new QueryWrapper<>();
        if (walletRechargeRecord.getCompanyName() != null && walletRechargeRecord.getCompanyName() !=""){
            walletRechargeRecordQueryWrapper.like("company_name",walletRechargeRecord.getCompanyName());
        }
        if (walletRechargeRecord.getChangeDesc() != null && walletRechargeRecord.getChangeDesc() !=""){
            walletRechargeRecordQueryWrapper.like("change_desc",walletRechargeRecord.getChangeDesc());
        }

        walletRechargeRecordQueryWrapper.orderByDesc("create_time");

        Page<WalletRechargeRecord> walletRechargeRecordPage = new Page<>(page,rows);
        Page<WalletRechargeRecord> selectPage = walletRechargeRecordMapper.selectPage(walletRechargeRecordPage,walletRechargeRecordQueryWrapper);

        pageBean.setDatas(selectPage.getRecords());
        pageBean.setTotalsize((int) walletRechargeRecordPage.getTotal());

        return pageBean;
    }

    @Override
    public void addRechargeRecordList(String startTime , AccountSystem accountSystem) {

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateFormat = date.format(formatter);
        String GET_RECHARGE_RECORD_LIST_POST_RD = "{\"dataLevel\": \"1\",\"pageSize\": \"50\",\"pageNum\": \"1\",\"endDate\": \"" + dateFormat + "\",\"startDate\": \"" + startTime + "\"}";
        String cookie = accountCookieService.queryCookie(accountSystem.getAccount());
        try {

            String rechargeRecord = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_RECHARGE_RECORD_LIST_POST, cookie, GET_RECHARGE_RECORD_LIST_POST_RD);
            Map<String, Object> rechargeRecordMap = JsonParseUtil.parseJSON(rechargeRecord);
            JSONArray rechargeRecordList =  (JSONArray) rechargeRecordMap.get("result.result");
            for (int i = 0; i < rechargeRecordList.length(); i++) {
                Map<String, Object> objectMap = JsonParseUtil.parseJSON(rechargeRecordList.getJSONObject(i).toString());
                WalletRechargeRecord walletRechargeRecord = new WalletRechargeRecord();
                walletRechargeRecord.setId(objectMap.get("operator.id").toString());
                walletRechargeRecord.setActionType(objectMap.get("actionType").toString());
                walletRechargeRecord.setChangeDesc(objectMap.get("changeDesc").toString());
                walletRechargeRecord.setChangeType(objectMap.get("changeType").toString());
                walletRechargeRecord.setCreateTime(formatterDate.parse(objectMap.get("createTime").toString()));
                walletRechargeRecord.setNickName(objectMap.get("operator.userName").toString());
                walletRechargeRecord.setUsdAmount(Double.parseDouble(objectMap.get("changeAmount.usdAmount").toString()));
                walletRechargeRecord.setCompanyName(accountSystem.getClientName());
                walletRechargeRecord.setMobile(accountSystem.getAccount());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
