package com.supcon.mes.module_wom_batchmaterial.model.dto;

import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/20
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录签收/拒收，退废 提交DTO
 */
public class BatchMaterialRecordsSignSubmitDTO {

    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
