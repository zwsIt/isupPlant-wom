package com.supcon.mes.module_wom_replenishmaterial.model.api;

import android.util.ArrayMap;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;


/**
 * @desc ReplenishMaterialNotifyListAPI 补料通知API
 * @author created by zhangwenshuai1 
 * @date 2021/5/11
 */
@ContractFactory(entites = {CommonBAP5ListEntity.class})
public interface ReplenishMaterialNotifyListAPI {
    /**
     * 根据生产批号获取活动执行记录
     * @param pageIndex
     * @param queryMap
     */
    void listReplenishMaterialNotifies(int pageIndex, ArrayMap<String,Object> queryMap);
}
