package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 指令单工序操作API
 */
@ContractFactory(entites = {BAP5CommonEntity.class, BAP5CommonEntity.class})
public interface ProcessOperateAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param taskProcessId 指令单工序ID
     * @param submitMap 工序设置工作单元操作
     */
    void updateProcessFactoryModelUnit(Long taskProcessId,String __pc__, Map<String, Object> submitMap);

    /**
     * @author zhangwenshuai1 2020/3/27
     * @param waitPutRecordId 待办记录ID
     * @param isFinish 结束标示
     * @description 指令单工序启停操作
     */
    void operateProcess(Long waitPutRecordId, boolean isFinish);
}
