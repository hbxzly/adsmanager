package com.hbx.adsmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbx.adsmanager.domain.AccountCookie;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.mapper.AccountCookieMapper;
import com.hbx.adsmanager.mapper.AccountSystemMapper;
import com.hbx.adsmanager.service.AccountCookieService;
import com.hbx.adsmanager.service.AccountSystemService;
import com.hbx.adsmanager.service.SeleniumService;
import com.hbx.adsmanager.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class AccountCookieServiceImpl implements AccountCookieService {

    @Autowired
    AccountCookieMapper accountCookieMapper;

    @Autowired
    AccountSystemMapper accountSystemMapper;

    @Autowired
    SeleniumService seleniumService;

    @Autowired
    AccountSystemService accountSystemService;

    @Override
    public PageBean<AccountCookie> queryAccountCookie(int page, int rows, AccountCookie accountCookie) {

        PageBean<AccountCookie> pageBean = new PageBean<>(page,rows);

        QueryWrapper<AccountCookie> accountCookieQueryWrapper = new QueryWrapper<>();

        if (accountCookie.getAccount()!=null && accountCookie.getAccount() != ""){
            accountCookieQueryWrapper.eq("account",accountCookie.getAccount());
        }
        accountCookieQueryWrapper.orderByDesc("id");
        Page<AccountCookie> accountCookiePage = new Page<>(page,rows);
        Page<AccountCookie> selectPage = accountCookieMapper.selectPage(accountCookiePage,accountCookieQueryWrapper);

        pageBean.setDatas(selectPage.getRecords());
        pageBean.setTotalsize((int) accountCookiePage.getTotal());

        return pageBean;
    }

    @Override
    public String queryCookie(String account) {

        QueryWrapper<AccountCookie> accountCookieQueryWrapper = new QueryWrapper<>();
        accountCookieQueryWrapper.eq("account",account);
        return accountCookieMapper.selectOne(accountCookieQueryWrapper).getCookie();
    }



    @Override
    public void insertAccountCookie(String account) {

        QueryWrapper<AccountCookie> accountCookieQueryWrapper = new QueryWrapper<>();
        accountCookieQueryWrapper.eq("account",account);
        if (accountCookieMapper.selectOne(accountCookieQueryWrapper) ==null ){
            AccountCookie accountCookie = new AccountCookie();
            accountCookie.setAccount(account);
            accountCookieMapper.insert(accountCookie);
        }
    }

    @Override
    public void updateAccountCookie(String account, String cookie) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = df.format(System.currentTimeMillis());
        UpdateWrapper<AccountCookie> accountCookieUpdateWrapper = new UpdateWrapper<>();
        accountCookieUpdateWrapper.eq("account",account)
                .set("cookie",cookie)
                .set("update_time",currentTime);
        accountCookieMapper.update(null,accountCookieUpdateWrapper);
    }

    @Override
    public void updateAllAccountCookie() {
        QueryWrapper<AccountSystem> accountSystemQueryWrapper = new QueryWrapper<>();
        accountSystemQueryWrapper.orderByAsc("id");
        List<AccountSystem> accountSystemList = accountSystemMapper.selectList(accountSystemQueryWrapper);
        for (AccountSystem accountSystem: accountSystemList){
            String cookie = seleniumService.getAccountSystemCookie(accountSystem);
            updateAccountCookie(accountSystem.getAccount(),cookie);
        }
    }

    @Override
    public String queryCookieByAccountSystem(String accountSystem) {
        AccountSystem system = accountSystemService.queryAccountSystemByClientName(accountSystem);
        return queryCookie(system.getAccount());
    }
}
