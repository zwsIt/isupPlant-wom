package com.supcon.mes.module_wom_rejectmaterial.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_rejectmaterial.model.dto.RejectMaterialDTO;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 退料提交API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface RejectMaterialSubmitAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param path 视图url
     * @param dto
     * @return
     * @description 提交
     *
     */
    void submit(Long id, String __pc__,String path, RejectMaterialDTO dto);
}
