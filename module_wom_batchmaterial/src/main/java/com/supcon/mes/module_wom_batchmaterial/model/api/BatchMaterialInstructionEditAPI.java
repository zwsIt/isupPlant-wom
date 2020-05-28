package com.supcon.mes.module_wom_batchmaterial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;
import com.supcon.mes.module_wom_producetask.model.dto.OutputDetailDTO;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令编辑API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface BatchMaterialInstructionEditAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param
     * @param batchMaterialInstructionDTO
     * @return
     * @description 提交
     *
     */
    void submit(Long id, String __pc__, BatchMaterialInstructionDTO batchMaterialInstructionDTO);
}
