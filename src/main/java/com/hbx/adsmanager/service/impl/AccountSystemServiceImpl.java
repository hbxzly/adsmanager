package com.hbx.adsmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.mapper.AdAccountMapper;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.AdAccountService;
import com.hbx.adsmanager.util.CustomHttpClient;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountSystemServiceImpl implements AccountSystemService {


    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    AccountCookieService accountCookieService;

    @Autowired
    AdAccountService adAccountService;

    @Autowired
    AdAccountMapper adAccountMapper;


    /**
     * 查询后台分页信息
     * @param page
     * @param rows
     * @param accountSystem
     * @return
     */
    @Override
    public PageBean<AccountSystem> queryAccountSystemList(int page, int rows, AccountSystem accountSystem) {

        PageBean<AccountSystem> pageBean = new PageBean<>(page,rows);


        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        if (accountSystem.getClientName() != null && accountSystem.getClientName() != ""){
            accountSystemQueryWrapper.like("client_name",accountSystem.getClientName())
                    .or().like("client_alias",accountSystem.getClientName());
        }
        if (accountSystem.getAccount() != null && accountSystem.getAccount() != ""){
            accountSystemQueryWrapper.like("account",accountSystem.getAccount());
        }
        if (accountSystem.getEmail() != null && accountSystem.getEmail() != ""){
            accountSystemQueryWrapper.like("email",accountSystem.getEmail());
        }
        if (accountSystem.getBdName() != null && accountSystem.getBdName() != ""){
            accountSystemQueryWrapper.like("bd_name",accountSystem.getBdName());
        }
        if (accountSystem.getClientBalance() != 0){
            accountSystemQueryWrapper.ge("client_balance", accountSystem.getClientBalance());
        }
        if (accountSystem.getSpendAmount() != 0){
            accountSystemQueryWrapper.ge("spend_amount", accountSystem.getSpendAmount());
        }
        if(accountSystem.getNote() != null && accountSystem.getNote() != ""){
            accountSystemQueryWrapper.like("note", accountSystem.getNote());
        }
        if(accountSystem.getClientStatus() != null && accountSystem.getClientStatus() != ""){
            accountSystemQueryWrapper.eq("client_status", accountSystem.getClientStatus());
        }
        accountSystemQueryWrapper.orderByDesc("id");
        Page<AccountSystem> accountSystemPage = new Page<>(page,rows);
        Page<AccountSystem> selectPage = accountSystemMapper.selectPage(accountSystemPage, accountSystemQueryWrapper);

        pageBean.setDatas(selectPage.getRecords());
        pageBean.setTotalsize((int) accountSystemPage.getTotal());

        return pageBean;
    }

    /**
     * 通过后台名称查询后台
     * @param clientName
     * @return
     */
    @Override
    public AccountSystem queryAccountSystemByClientName(String clientName) {

        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.eq("client_name",clientName);
        return accountSystemMapper.selectOne(accountSystemQueryWrapper);
    }

    /**
     * 通过后台账号查询后台
     * @param account
     * @return
     */
    @Override
    public AccountSystem queryAccountSystemByAccount(String account) {
        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.eq("account",account);
        return accountSystemMapper.selectOne(accountSystemQueryWrapper);
    }

    /**
     * 添加一个后台
     * @param accountSystem
     */
    @Override
    public void addAccountSystem(AccountSystem accountSystem) {

        if (queryAccountSystemByAccount(accountSystem.getAccount()) == null){
            accountSystemMapper.insert(accountSystem);
        }
    }


    /**
     * 更新后台名称、邮箱、user_id、商务
     * @param accountSystem
     */
    @Override
    public void updateAccountSystemBasicInfo(AccountSystem accountSystem) {

        String accountCookie = accountCookieService.queryCookie(accountSystem.getAccount());
        String COMMON_RD="{\"domain\": \"sinoclick.com\"}";
        try {
            String clientInfoString = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_UDESK_CLIENT_INFO_POST, accountCookie, COMMON_RD);
            Map<String, String> clientInfoMap = JSON.parseObject(clientInfoString, new TypeReference<HashMap<String, String>>() {});
            AccountSystem accountSystemTemp = JSONObject.parseObject(clientInfoMap.get("result"), AccountSystem.class);
            UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
            accountSystemUpdateWrapper.eq("account", accountSystem.getAccount())
                    .set("client_name", accountSystemTemp.getClientName())
                    .set("email", accountSystemTemp.getEmail())
                    .set("user_id", accountSystemTemp.getUserId())
                    .set("bd_name", accountSystemTemp.getBdName());
            accountSystemMapper.update(null, accountSystemUpdateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新后台账户数量、钱包余额、后台状态
     * @param clientName
     */
    @Override
    public void updateAccountSystemAccountInfo(String clientName) {

        AccountSystem accountSystem = queryAccountSystemByClientName(clientName);
        String cookie = accountCookieService.queryCookie(accountSystem.getAccount());
        String GET_LIST_POST_RD="{\"channelId\": \"1\",\"keywords\": \"\",\"pageSize\": \"50\",\"pageNum\": \"1\"}";
        String CLIENT_FINAL_LEVEL_RD = "{\"channelId\": \"1\"}";
        String WALLET_BALANCE_RD = "{\"refresh\": \"true\"}";
        try {
            String finalLevelIdString = CustomHttpClient.postRequest(SinoClickRequestUrl.CLIENT_FINAL_LEVEL_POST, cookie, CLIENT_FINAL_LEVEL_RD);
            Map<String, String> finalLevelMap = JSON.parseObject(finalLevelIdString, new TypeReference<HashMap<String, String>>() {});
            Map<String, String> result = JSON.parseObject(finalLevelMap.get("result"), new TypeReference<HashMap<String, String>>() {});

            String userAssetsString = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_USER_ASSETS_POST, cookie, WALLET_BALANCE_RD);
            Map<String, String> userAssetsMap = JSON.parseObject(userAssetsString, new TypeReference<HashMap<String, String>>() {});
            Map<String, String> userAssetsResultMap = JSON.parseObject(userAssetsMap.get("result"), new TypeReference<HashMap<String, String>>() {});
            Map<String, String> walletMap = JSON.parseObject(userAssetsResultMap.get("wallet"), new TypeReference<HashMap<String, String>>() {});

            String adAccountListStr = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_LIST_POST, cookie, GET_LIST_POST_RD);
            Map<String,String> adAccountListMap = JSON.parseObject(adAccountListStr, new TypeReference<HashMap<String, String>>() {});
            Map<String,String> mapResult = JSON.parseObject(adAccountListMap.get("result"), new TypeReference<HashMap<String, String>>() {});
            List<AdAccount> adAccountList = JSONObject.parseArray(mapResult.get("result"), AdAccount.class);
            for (AdAccount adAccount : adAccountList) {
                adAccount.setAdAccountId(adAccount.getAdAccountId().replace("act_", ""));
                adAccount.setAdAccountSystem(accountSystem.getClientName());
                adAccount.setAdAccountSystemAlias(accountSystem.getClientAlias());
                adAccount.setAdAccountSystemStatus(accountSystem.getClientStatus());
                adAccount.setAdAccountSystemBdName(accountSystem.getBdName());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String Time=df.format(new Date());// new Date()为获取当前系统时间
                Date date = df.parse(Time);
                adAccount.setUpdateTime(date);
                QueryWrapper<AdAccount> adAccountQueryWrapper = new QueryWrapper<>();
                adAccountQueryWrapper.eq("id",adAccount.getId());
                if (adAccountMapper.selectOne(adAccountQueryWrapper) !=null){
                    adAccountMapper.updateById(adAccount);
                }else {
                    adAccountMapper.insert(adAccount);
                }
            }
            AccountSystem systemAdAccount = countAccountSystemAdAccount(clientName);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(System.currentTimeMillis());
            UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
            accountSystemUpdateWrapper.eq("account", accountSystem.getAccount())
                    .set("client_status", result.get("finalLevelId"))
                    .set("client_balance", walletMap.get("balance"))
                    .set("total_count", systemAdAccount.getTotalCount())
                    .set("active_count", systemAdAccount.getActiveCount())
                    .set("disable_count", systemAdAccount.getDisableCount())
                    .set("last_sync_time", currentTime);
            accountSystemMapper.update(null, accountSystemUpdateWrapper);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccountSystem(AccountSystem accountSystem) {
        UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
        accountSystemUpdateWrapper.eq("client_name",accountSystem.getClientName())
                .set("account",accountSystem.getAccount())
                .set("password",accountSystem.getPassword())
                .set("client_alias",accountSystem.getClientAlias())
                .set("is_able",accountSystem.getIsAble())
                .set("is_sell",accountSystem.getIsSell())
                .set("note",accountSystem.getNote());
        accountSystemMapper.update(null,accountSystemUpdateWrapper);
    }

    @Override
    public void updateAccountSystemForSell(AccountSystem accountSystem) {
        UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
        accountSystemUpdateWrapper.eq("client_name",accountSystem.getClientName())
                .set("is_sell",accountSystem.getIsSell());
        accountSystemMapper.update(null,accountSystemUpdateWrapper);
        List<AdAccount> adAccountList = adAccountService.queryAdAccountListByClentName(accountSystem.getClientName());
        for(AdAccount adAccount:adAccountList){
            adAccountService.updateAdAccount(adAccount,accountSystem.getIsSell());
        }
    }

    @Override
    public AccountSystem countAccountSystemAdAccount(String clientName) {
        return accountSystemMapper.countAccountSystemAdAccount(clientName);
    }

    @Override
    public List<AdAccount> queryAdAccountByAccountSystem(String clientName) {
        QueryWrapper<AdAccount> adAccountQueryWrapper = new QueryWrapper<>();
        adAccountQueryWrapper.eq("ad_account_system",clientName);
        return adAccountMapper.selectList(adAccountQueryWrapper);
    }


}
