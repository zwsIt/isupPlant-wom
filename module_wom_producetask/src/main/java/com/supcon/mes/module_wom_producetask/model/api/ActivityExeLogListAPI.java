package com.supcon.mes.module_wom_producetask.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogListEntity;


/**
 * Created by wanghaidong on 2020/8/17
 * Email:wanghaidong1@supcon.com
 */
@ContractFactory(entites = {ActiExelogListEntity.class})
public interface ActivityExeLogListAPI {
    /**
     * 根据生产批号获取活动执行记录
     * @param pageIndex
     * @param produceBatchNum
     */
    void listActivityExeLog(int pageIndex, String produceBatchNum);
}
