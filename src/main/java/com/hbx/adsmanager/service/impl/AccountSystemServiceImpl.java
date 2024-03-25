package com.hbx.adsmanager.service.impl;

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
import com.hbx.adsmanager.util.JsonParseUtil;
import com.hbx.adsmanager.util.PageBean;
import com.hbx.adsmanager.util.SinoClickRequestUrl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     *
     * @param page
     * @param rows
     * @param accountSystem
     * @return
     */
    @Override
    public PageBean<AccountSystem> queryAccountSystemList(int page, int rows, AccountSystem accountSystem) {

        PageBean<AccountSystem> pageBean = new PageBean<>(page, rows);


        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        if (accountSystem.getClientName() != null && accountSystem.getClientName() != "") {
            accountSystemQueryWrapper.like("client_name", accountSystem.getClientName())
                    .or().like("client_alias", accountSystem.getClientName());
        }
        if (accountSystem.getAccount() != null && accountSystem.getAccount() != "") {
            accountSystemQueryWrapper.like("account", accountSystem.getAccount());
        }
        if (accountSystem.getEmail() != null && accountSystem.getEmail() != "") {
            accountSystemQueryWrapper.like("email", accountSystem.getEmail());
        }
        if (accountSystem.getBdName() != null && accountSystem.getBdName() != "") {
            accountSystemQueryWrapper.like("bd_name", accountSystem.getBdName());
        }
        if (accountSystem.getClientBalance() != 0) {
            accountSystemQueryWrapper.ge("client_balance", accountSystem.getClientBalance());
        }
        if (accountSystem.getSpendAmount() != 0) {
            accountSystemQueryWrapper.ge("spend_amount", accountSystem.getSpendAmount());
        }
        if (accountSystem.getNote() != null && accountSystem.getNote() != "") {
            accountSystemQueryWrapper.like("note", accountSystem.getNote());
        }
        if (accountSystem.getClientStatus() != null && accountSystem.getClientStatus() != "") {
            accountSystemQueryWrapper.eq("client_status", accountSystem.getClientStatus());
        }
        accountSystemQueryWrapper.orderByDesc("id");
        Page<AccountSystem> accountSystemPage = new Page<>(page, rows);
        Page<AccountSystem> selectPage = accountSystemMapper.selectPage(accountSystemPage, accountSystemQueryWrapper);

        pageBean.setDatas(selectPage.getRecords());
        pageBean.setTotalsize((int) accountSystemPage.getTotal());

        return pageBean;
    }

    /**
     * 通过后台名称查询后台
     *
     * @param clientName
     * @return
     */
    @Override
    public AccountSystem queryAccountSystemByClientName(String clientName) {

        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.eq("client_name", clientName);
        return accountSystemMapper.selectOne(accountSystemQueryWrapper);
    }

    /**
     * 通过后台账号查询后台
     *
     * @param account
     * @return
     */
    @Override
    public AccountSystem queryAccountSystemByAccount(String account) {
        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.eq("account", account);
        return accountSystemMapper.selectOne(accountSystemQueryWrapper);
    }

    /**
     * 添加一个后台
     *
     * @param accountSystem
     */
    @Override
    public void addAccountSystem(AccountSystem accountSystem) {

        if (queryAccountSystemByAccount(accountSystem.getAccount()) == null) {
            accountSystemMapper.insert(accountSystem);
        }
    }

    /**
     * 更新后台名称、邮箱、user_id、商务
     *
     * @param accountSystem
     */
    @Override
    public void updateAccountSystemBasicInfo(AccountSystem accountSystem) {

        String accountCookie = accountCookieService.queryCookie(accountSystem.getAccount());
        String requestBody = "{}";
        try {
            String clientInfoString = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_INFO_POST, accountCookie, requestBody);
            String bmInfoString = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_SERVICE_STAFF_INFO_POST, accountCookie, requestBody);
            Map<String, Object> clientInfoMap = JsonParseUtil.parseJSON(clientInfoString);
            Map<String, Object> bmInfoMap = JsonParseUtil.parseJSON(bmInfoString);
            UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
            accountSystemUpdateWrapper.eq("account", accountSystem.getAccount())
                    .set("email", clientInfoMap.get("result.baseInfo.activeEmail"))
                    .set("user_id", clientInfoMap.get("result.baseInfo.userId"))
                    .set("bd_name", bmInfoMap.get("result.bdInfo.name"));
            accountSystemMapper.update(null, accountSystemUpdateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新后台账户,账户数量、钱包余额、后台状态
     *
     * @param clientName
     */
    @Override
    public void updateAccountSystemAccountInfo(String clientName) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = df.format(System.currentTimeMillis());

        try {
            AccountSystem accountSystem = queryAccountSystemByClientName(clientName);
            String cookie = accountCookieService.queryCookie(accountSystem.getAccount());
            String walletBalanceRequestBody = "{\"refresh\":\"false\"}";
            String overviewRequestBody = "{\"refresh\":\"false\"}";

            String userAssetsString = CustomHttpClient.postRequest(SinoClickRequestUrl.GET_DEPARTMENT_ASSETS_POST, cookie, walletBalanceRequestBody);
            String overview = CustomHttpClient.postRequest(SinoClickRequestUrl.OVERVIEW_POST, cookie, overviewRequestBody);
            String totalCount = "";
            if (overview != "") {
                totalCount = JsonParseUtil.parseJSON(overview).get("result.facebook.totalCount").toString();
                String activeCount = JsonParseUtil.parseJSON(overview).get("result.facebook.activeCount").toString();
                String disableCount = JsonParseUtil.parseJSON(overview).get("result.facebook.disableCount").toString();
                String walletBalance = JsonParseUtil.parseJSON(userAssetsString).get("result.wallet.balance").toString();
                UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
                accountSystemUpdateWrapper.eq("account", accountSystem.getAccount())
                        .set("client_balance", walletBalance)
                        .set("total_count", totalCount)
                        .set("active_count", activeCount)
                        .set("disable_count", disableCount)
                        .set("last_sync_time", currentTime);
                accountSystemMapper.update(null, accountSystemUpdateWrapper);
            }


            for (int i = 1; i <= (Math.ceil(Integer.parseInt(totalCount)/50))+1; i++) {
                String accountListRequestBody = "{\"channelId\": \"1\",\"pageSize\": \"50\",\"pageNum\": "+i+"}";
                String adAccountListStr = CustomHttpClient.postRequest(SinoClickRequestUrl.AD_ACCOUNT_LIST_POST, cookie, accountListRequestBody);
                if (adAccountListStr != "") {
                    org.json.JSONArray adAccountList = (org.json.JSONArray) JsonParseUtil.parseJSON(adAccountListStr).get("result.result");
                    for (int j = 0; j < adAccountList.length(); j++) {
                        JSONObject adAccountTemp = adAccountList.getJSONObject(j);
                        AdAccount adAccount = new AdAccount();
                        adAccount.setId((String) adAccountTemp.get("adAccountId"));
                        adAccount.setAdAccountId(((String) adAccountTemp.get("adAccountId")).replace("act_", ""));
                        adAccount.setAdAccountSystemAlias(accountSystem.getClientAlias());
                        adAccount.setAdAccountSystemBdName(accountSystem.getBdName());
                        adAccount.setAdAccountName((String) adAccountTemp.get("adAccountName"));
                        adAccount.setBalance(((BigDecimal) adAccountTemp.get("balance")).doubleValue());
                        adAccount.setAdAccountSystem(clientName);
                        adAccount.setAccountStatus(adAccountTemp.get("adAccountStatus").toString());
                        adAccount.setOpenAccountCompany((String) adAccountTemp.get("openAccountCompany"));
                        adAccount.setUpdateTime(new Date());

                        QueryWrapper<AdAccount> adAccountQueryWrapper = new QueryWrapper<>();
                        adAccountQueryWrapper.eq("id", adAccountTemp.get("adAccountId"));
                        if (adAccountMapper.selectOne(adAccountQueryWrapper) != null) {
                            UpdateWrapper<AdAccount> accountUpdateWrapper = new UpdateWrapper<>();
                            accountUpdateWrapper.eq("id", adAccount.getId())
                                    .set("account_status", adAccount.getAccountStatus())
                                    .set("ad_account_name", adAccount.getAdAccountName())
                                    .set("balance", adAccount.getBalance())
                                    .set("open_account_company", adAccount.getOpenAccountCompany())
                                    .set("ad_account_id", adAccount.getAdAccountId())
                                    .set("ad_account_system", accountSystem.getClientName())
                                    .set("ad_account_system_alias", accountSystem.getClientAlias())
                                    .set("ad_account_system_bd_name", accountSystem.getBdName())
                                    .set("update_time", currentTime);
                            adAccountMapper.update(null, accountUpdateWrapper);
                        } else {
                            adAccountMapper.insert(adAccount);
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccountSystem(AccountSystem accountSystem) {
        UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
        accountSystemUpdateWrapper.eq("client_name", accountSystem.getClientName())
                .set("account", accountSystem.getAccount())
                .set("password", accountSystem.getPassword())
                .set("client_alias", accountSystem.getClientAlias())
                .set("is_able", accountSystem.getIsAble())
                .set("is_sell", accountSystem.getIsSell())
                .set("note", accountSystem.getNote());
        accountSystemMapper.update(null, accountSystemUpdateWrapper);
    }

    @Override
    public void updateAccountSystemForSell(AccountSystem accountSystem) {
        UpdateWrapper<AccountSystem> accountSystemUpdateWrapper = new UpdateWrapper<>();
        accountSystemUpdateWrapper.eq("client_name", accountSystem.getClientName())
                .set("is_sell", accountSystem.getIsSell());
        accountSystemMapper.update(null, accountSystemUpdateWrapper);
        List<AdAccount> adAccountList = adAccountService.queryAdAccountListByClentName(accountSystem.getClientName());
        for (AdAccount adAccount : adAccountList) {
            adAccountService.updateAdAccount(adAccount, accountSystem.getIsSell());
        }
    }

    @Override
    public AccountSystem countAccountSystemAdAccount(String clientName) {
        return accountSystemMapper.countAccountSystemAdAccount(clientName);
    }

    @Override
    public List<AdAccount> queryAdAccountByAccountSystem(String clientName) {
        QueryWrapper<AdAccount> adAccountQueryWrapper = new QueryWrapper<>();
        adAccountQueryWrapper.eq("ad_account_system", clientName);
        return adAccountMapper.selectList(adAccountQueryWrapper);
    }


}
