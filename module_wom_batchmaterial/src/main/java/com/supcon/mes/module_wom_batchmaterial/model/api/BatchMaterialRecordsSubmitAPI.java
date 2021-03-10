package com.supcon.mes.module_wom_batchmaterial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录提交API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface BatchMaterialRecordsSubmitAPI {
    /**
     * @param paramsMap 操作：配料完成/撤回/派送/配放
     * @param dto
     * @param sign      类别：签收/拒签 or 退废
     * @author zhangwenshuai1 2020/4/20
     * @description 配料记录提交
     */
    void submit(Map<String,Object> paramsMap, BatchMaterialRecordsSignSubmitDTO dto,boolean sign);
}
