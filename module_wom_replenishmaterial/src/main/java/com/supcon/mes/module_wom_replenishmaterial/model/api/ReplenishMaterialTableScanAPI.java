package com.supcon.mes.module_wom_replenishmaterial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialScanDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialTableDTO;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/13
 * Email zhangwenshuai1@supcon.com
 * Desc 补料编辑API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface ReplenishMaterialTableScanAPI {
    /**
     * @author zhangwenshuai1 2021/5/13
     * @param
     * @param replenishMaterialScanDTO
     * @return
     * @description 提交
     *
     */
    void submit(ReplenishMaterialScanDTO replenishMaterialScanDTO);
}
