package com.supcon.mes.module_wom_preparematerial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialSubmitEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreResultEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2020/6/30
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = PreResultEntity.class)
public interface PreMaterialReceiveSubmitAPI {
    void doSubmitPreMaterial(List<PreMaterialSubmitEntity> preMaterialEntities);
}
