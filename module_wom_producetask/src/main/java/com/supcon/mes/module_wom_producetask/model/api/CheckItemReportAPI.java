package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_producetask.model.dto.ProCheckDetailDTO;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 检查活动API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface CheckItemReportAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param proCheckDetailDTO：检查明细提交DTO
     * @return
     * @description 报工提交
     *
     */
    void submit(Long id, String __pc__, ProCheckDetailDTO proCheckDetailDTO);
}
