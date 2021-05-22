package com.supcon.mes.module_wom_batchmaterial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 配料中继位扫描提交API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface BatchTrunkScanAPI {
    /**
     * @param id
     * @author zhangwenshuai1 2020/4/20
     * @description 配料中继位扫描
     */
    void submit(Long id);
}
