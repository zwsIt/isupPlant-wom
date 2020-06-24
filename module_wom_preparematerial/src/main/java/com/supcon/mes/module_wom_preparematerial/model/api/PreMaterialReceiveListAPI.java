package com.supcon.mes.module_wom_preparematerial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;

import java.util.Map;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = CommonBAPListEntity.class)
public interface PreMaterialReceiveListAPI {

    void getPreMaterialReceiveList(int pageNum, Map<String, Object> queryParams);

}
