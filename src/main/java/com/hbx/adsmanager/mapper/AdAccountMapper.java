package com.hbx.adsmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbx.adsmanager.domain.AccountSystem;
import com.hbx.adsmanager.domain.AdAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdAccountMapper extends BaseMapper<AdAccount>{

    List<AdAccount> queryAdAccountList(@Param("page") int page, @Param("rows")int rows, @Param("adAccount")AdAccount adAccount);

    List<AdAccount> queryAdAccountListByList(Map<String,Object> paramMap);

    int countAdAccount(@Param("adAccount")AdAccount adAccount);

    int countAdAccountByList(Map<String,Object> paramMap);

}
