package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_producetask.model.dto.BatchPutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 投料活动报工API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface PutInReportAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param
     * @param batchPutIn
     * @return
     * @description 报工提交
     *
     */
    void submit(boolean batchPutIn, Long id, String __pc__, PutinDetailDTO putinDetailDTO, BatchPutinDetailDTO batchPutinDetailDTO);
}
