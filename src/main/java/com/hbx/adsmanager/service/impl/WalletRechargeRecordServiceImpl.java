package com.hbx.adsmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String dateFormat = date.format(formatter);
        String GET_RECHARGE_RECORD_LIST_POST_RD = "{\"dataLevel\": \"1\",\"pageSize\": \"50\",\"pageNum\": \"1\",\"endDate\": \"" + dateFormat + "\",\"startDate\": \"" + startTime + "\"}";

        try {

            String s = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_RECHARGE_RECORD_LIST_POST,
                    accountCookieService.queryCookie(accountSystem.getAccount()), GET_RECHARGE_RECORD_LIST_POST_RD);
            List<WalletRechargeRecord> walletRechargeRecordList = JsonParseUtil.parseRechargeRecordList(s);
            for (WalletRechargeRecord walletRechargeRecord : walletRechargeRecordList) {
                QueryWrapper<WalletRechargeRecord> rechargeRecordQueryWrapper = new QueryWrapper<>();
                rechargeRecordQueryWrapper.eq("create_time", walletRechargeRecord.getCreateTime());
                rechargeRecordQueryWrapper.eq("change_desc", walletRechargeRecord.getChangeDesc());
                if (walletRechargeRecordMapper.selectOne(rechargeRecordQueryWrapper) == null) {
                    walletRechargeRecordMapper.insert(walletRechargeRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
