package com.supcon.mes.module_wom_replenishmaterial.model.api;

import android.util.ArrayMap;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;


/**
 * @desc ReplenishMaterialNotifyListAPI 补料单PI
 * @author created by zhangwenshuai1 
 * @date 2021/5/11
 */
@ContractFactory(entites = {CommonBAP5ListEntity.class})
public interface ReplenishMaterialTableListAPI {
    /**
     * 获取补料单
     * @param pageIndex
     * @param queryMap
     */
    void listReplenishMaterialTables(int pageIndex,String url, boolean pending, ArrayMap<String,Object> queryMap);

}
