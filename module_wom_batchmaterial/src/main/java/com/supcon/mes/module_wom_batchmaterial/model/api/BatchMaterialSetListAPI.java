package com.supcon.mes.module_wom_batchmaterial.model.api;

import android.util.ArrayMap;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;


/**
 * @desc ReplenishMaterialNotifyListAPI 补料单PI
 * @author created by zhangwenshuai1 
 * @date 2021/5/11
 */
@ContractFactory(entites = {CommonBAP5ListEntity.class})
public interface BatchMaterialSetListAPI {
    /**
     * 获取配料指令集
     * @param pageIndex
     * @param queryMap
     */
    void listBatchMaterialSets(int pageIndex,String url, boolean pending, ArrayMap<String,Object> queryMap);

}
