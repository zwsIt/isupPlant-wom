package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 活动操作API
 */
@ContractFactory(entites = {BAP5CommonEntity.class})
public interface StartQualityAPI {
    /**
     * @author zhangwenshuai1 2020/3/25
     * @param activeId：活动ID
     * @return
     * @description 发起请检
     *
     */
    void startQuality(Long activeId);

    /**
     * @author zhangwenshuai1 2020/3/27
     * @param waitPutRecordId 待办记录ID
     * @param isFinish 结束标示
     * @description 指令单工序操作
     */
//    void operateProcess(Long waitPutRecordId, boolean isFinish);
}
