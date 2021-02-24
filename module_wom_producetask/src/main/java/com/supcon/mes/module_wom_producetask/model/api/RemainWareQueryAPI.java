package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 获取尾料仓库信息API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface RemainWareQueryAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param id
     * @return
     * @description 请求
     *
     */
    void getWareByRemainId(Long id);
}
