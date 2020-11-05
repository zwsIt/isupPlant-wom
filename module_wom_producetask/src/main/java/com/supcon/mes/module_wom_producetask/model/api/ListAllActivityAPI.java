package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface ListAllActivityAPI {
    /**
     * @author zhangwenshuai1 2020/3/30
     * @param
     * @return
     * @description 根据工序获取活动
     *
     */
    void listActivities(Map<String, Object> customCondition, Map<String, Object> queryParams);
}
