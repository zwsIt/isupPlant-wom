package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.CommonSelectListEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
@ContractFactory(entites = {CommonSelectListEntity.class})
public interface CommonList2API {
    /**
     * @author zhangwenshuai1 2020/3/30
     * @param
     * @return
     * @description
     *
     */
    void list(int pageNo, Map<String, Object> customCondition, Map<String, Object> queryParams, String url, String modelAlias);
}
