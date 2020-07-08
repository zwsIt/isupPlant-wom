package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 待办列表获取API：支持工单、工序、活动
 */
@ContractFactory(entites = {CommonBAPListEntity.class})
public interface WaitPutinRecordsListAPI {
    /**
     * @description 获取待办list
     * @param queryParams:produceBatchNum 生产批号
     * @param queryParams:recordType 待办类型：工单(WOM_recordType/workOrder);工序(WOM_recordType/process);活动(WOM_recordType/active)
     * @param queryParams:like  是否模糊
     * @return
     * @author zhangwenshuai1 2020/3/23
     */
    void listWaitPutinRecords(int pageNo, int pageSize, Map<String,Object> queryParams, boolean like);
}
