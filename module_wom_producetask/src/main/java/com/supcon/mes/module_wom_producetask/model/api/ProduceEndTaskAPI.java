package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.dto.ProduceEndTaskDTO;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 指令单操作API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface ProduceEndTaskAPI {
    /**
     * @author zhangwenshuai1 2020/3/27
     * @param id 指令单ID
     * @param produceEndTaskDTO
     * @description 标准指令单提前放料
     */
    void save(Long id, ProduceEndTaskDTO produceEndTaskDTO);
}
