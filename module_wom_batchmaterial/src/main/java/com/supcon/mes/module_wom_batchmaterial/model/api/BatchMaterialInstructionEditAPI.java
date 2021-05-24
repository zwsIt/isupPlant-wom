package com.supcon.mes.module_wom_batchmaterial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionPartEntity;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令编辑API
 */
@ContractFactory(entites = {CommonBAPListEntity.class,BAP5CommonEntity.class})
public interface BatchMaterialInstructionEditAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param
     * @param id
     * @return
     * @description 获取当前指令下配料记录
     *
     */
    void listBatchParts(Long id);

    /**
     * @author zhangwenshuai1 2020/3/25
     * @param
     * @param batchInstructionPartEntityList
     * @return
     * @description 提交
     *
     */
    void submit(List<BatchInstructionPartEntity> batchInstructionPartEntityList);
}
