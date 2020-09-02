package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 指令单操作API
 */
@ContractFactory(entites = {BAP5CommonEntity.class,BAP5CommonEntity.class})
public interface ProduceTaskOperateAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param waitPutRecordId：待办记录ID
     * @param operateType：操作类型
     * @param outputDetailEntityList：报工明细
     * @return
     * @description 指令单操作：开始、暂停、恢复、停止
     *
     */
    void operateProduceTask(Long waitPutRecordId, String operateType, List<OutputDetailEntity> outputDetailEntityList);

    /**
     * @author zhangwenshuai1 2020/3/27
     * @param taskId 指令单ID
     * @description 标准指令单提前放料
     */
    void operateDischarge(Long taskId);
}
