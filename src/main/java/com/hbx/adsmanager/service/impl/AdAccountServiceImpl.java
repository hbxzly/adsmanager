package com.hbx.adsmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.domain.vo.AdAccountInsightVo;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.mapper.AdAccountMapper;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.AdAccountService;
import com.hbx.adsmanager.util.CustomHttpClient;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import com.hbx.adsmanager.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdAccountServiceImpl implements AdAccountService {

    @Autowired
    AdAccountMapper adAccountMapper;

    @Autowired
    AccountSystemService accountSystemService;

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    AccountSystemMapper accountSystemMapper;


    /**
     * 获取账户分页数据
     * @param page
     * @param rows
     * @param adAccount
     * @return
     */
    @Override
    public PageBean<AdAccount> queryAdAccountList(int page, int rows, AdAccount adAccount) {

        PageBean pageBean =new PageBean<>(page,rows);

        if (adAccount.getId()!="" && adAccount.getId() != null && adAccount.getId().trim().contains(" ")){
            List<AdAccount> adAccountList = new ArrayList<>();
            String[] idStr = StringUtil.strToArray(adAccount.getId());
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("ids",idStr);
            paramMap.put("adAccountSystem",adAccount.getAdAccountSystem());
            paramMap.put("adAccountSystemBdName",adAccount.getAdAccountSystemBdName());
            paramMap.put("balance",adAccount.getBalance());
            paramMap.put("spendAmount",adAccount.getSpendAmount());
            paramMap.put("accountStatus",adAccount.getAccountStatus());
            paramMap.put("adAccountSystemStatus",adAccount.getAdAccountSystemStatus());
            paramMap.put("note",adAccount.getNote());
            paramMap.put("page",pageBean.getStartIndex());
            paramMap.put("rows",rows);
            List<AdAccount> adAccounts = adAccountMapper.queryAdAccountListByList(paramMap);
            pageBean.setDatas(adAccounts);
            pageBean.setTotalsize(adAccountMapper.countAdAccountByList(paramMap));
            return pageBean;
        }
        List<AdAccount> adAccounts = adAccountMapper.queryAdAccountList(pageBean.getStartIndex(), rows, adAccount);
        pageBean.setDatas(adAccounts);
        pageBean.setTotalsize(adAccountMapper.countAdAccount(adAccount));

        return pageBean;
    }


    /**
     * 更新账户消耗
     * @param clientName
     */
    @Override
    public void updateAdAccountSpendAmount(String clientName) {

        AccountSystem accountSystem = accountSystemService.queryAccountSystemByClientName(clientName);
        String cookie = accountCookieService.queryCookie(accountSystem.getAccount());

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.plusDays(-30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endDateFormat = endDate.format(formatter);
        String startDateFormat = startDate.format(formatter);

        String GET_AD_ACCOUNT_INSIGHT_LIST_POST_RD = "{\"pageSize\": \"50\",\"pageNum\": \"1\",\"endDate\": \""+endDateFormat+"\",\"startDate\": \""+startDateFormat+"\"}";

        try {
            String s = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_AD_ACCOUNT_INSIGHT_LIST_POST, cookie, GET_AD_ACCOUNT_INSIGHT_LIST_POST_RD) ;
            Map<String, String> map = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
            System.out.println("ceshiceshi"+map);
            Map<String, String> mapResult = JSON.parseObject(map.get("result"), new TypeReference<HashMap<String, String>>() {});
            List<AdAccountInsightVo> adAccountInsightVoList = JSONObject.parseArray(mapResult.get("result"), AdAccountInsightVo.class);
            BigDecimal bigDecimal = new BigDecimal(0.00);
            for (AdAccountInsightVo adAccountInsightVo : adAccountInsightVoList) {
                String spendAmountStr = adAccountInsightVo.getSpendAmount().replace("$", "").replace(",","");
                double spendAmountDouble = Double.parseDouble(spendAmountStr);
                UpdateWrapper<AdAccount> adAccountUpdateWrapper = new UpdateWrapper<>();
                adAccountUpdateWrapper.eq("id", adAccountInsightVo.getAdAccountId());
                adAccountUpdateWrapper.set("spend_amount", spendAmountDouble);
                adAccountMapper.update(null, adAccountUpdateWrapper);
                BigDecimal spendAmount = new BigDecimal(Double.toString(spendAmountDouble));
                bigDecimal = bigDecimal.add(spendAmount);
            }
            UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
            accountSystemUpdateWrapper.eq("account", accountSystem.getAccount());
            accountSystemUpdateWrapper.set("spend_amount", bigDecimal.doubleValue());
            accountSystemMapper.update(null, accountSystemUpdateWrapper);

            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AdAccount> queryAdAccountListByClentName(String adAccountSystem) {
        QueryWrapper<AdAccount> adAccountQueryWrapper = new QueryWrapper<>();
        adAccountQueryWrapper.eq("ad_account_system",adAccountSystem);
        return adAccountMapper.selectList(adAccountQueryWrapper);
    }

    @Override
    public void updateAdAccount(AdAccount adAccount,String isSell) {
        UpdateWrapper<AdAccount> adAccountUpdateWrapper = new UpdateWrapper<>();
        adAccountUpdateWrapper.eq("id",adAccount.getId())
            .set("is_sell",isSell);
        adAccountMapper.update(null,adAccountUpdateWrapper);
    }

    @Override
    public String bindBm(String accountSystem,String adAccountId, String bmId) throws InterruptedException {

        AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
        String cookie = accountCookieService.queryCookie(system.getAccount());
        String BIND_BM_POST_RD = "{\"bmId\": \""+bmId+"\",\"adAccountId\": \""+adAccountId+"\"}";
        String s = CustomHttpClient.postRequest(SinoClickRequestUrl.BIND_BM_POST, cookie, BIND_BM_POST_RD);
        Map<String, String> map = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
        return map.get("code");
    }

    @Override
    public String unBindBm(String accountSystem,String adAccountId, String bmId) throws InterruptedException {

        AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
        String cookie = accountCookieService.queryCookie(system.getAccount());
        String UNBIND_BM_POST_RD = "{\"bmId\": \""+bmId+"\",\"adAccountId\": \""+adAccountId+"\"}";
        String s = CustomHttpClient.postRequest(SinoClickRequestUrl.UNBIND_BM_POST, cookie, UNBIND_BM_POST_RD);
        Map<String, String> map = JSON.parseObject(s, new TypeReference<HashMap<String, String>>() {});
        return map.get("code");
    }


}
