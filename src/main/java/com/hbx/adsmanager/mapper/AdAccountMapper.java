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

    /**
     * 查询广告账户列表
     * @param page
     * @param rows
     * @param adAccount
     * @return
     */
    List<AdAccount> queryAdAccountList(@Param("page") int page, @Param("rows")int rows, @Param("adAccount")AdAccount adAccount);

    /**
     * 查询广告账户列表
     * @param paramMap
     * @return
     */
    List<AdAccount> queryAdAccountListByList(Map<String,Object> paramMap);

    /**
     * 统计广告账户数量
     * @param adAccount
     * @return
     */
    int countAdAccount(@Param("adAccount")AdAccount adAccount);

    /**
     * 统计查询计数
     * @param paramMap
     * @return
     */
    int countAdAccountByList(Map<String,Object> paramMap);

}
