package com.hbx.adsmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbx.adsmanager.domain.AccountSystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountSystemMapper extends BaseMapper<AccountSystem>{

    /**
     * 根据后台名称统计数量
     * @param clientName
     * @return
     */
    AccountSystem countAccountSystemAdAccount(@Param("clientName") String clientName);
}
