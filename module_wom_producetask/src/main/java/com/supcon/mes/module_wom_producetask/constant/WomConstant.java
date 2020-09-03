package com.supcon.mes.module_wom_producetask.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 工单模块常量
 */
public interface WomConstant {

    /**
     * 系统编码
     */
    interface SystemCode {
        // 配方类型
        String RM_TYPE_COMMON  = "RM_formulaType/commonFormula";    // 标准配方
        String RM_TYPE_SIMPLE = "RM_formulaType/simpleFormula";     // 普通配方
        // 待办记录类型
        String RECORD_TYPE_TASK = "WOM_recordType/workOrder";   // 工单
        String RECORD_TYPE_PROCESS = "WOM_recordType/process";  // 工序
        String RECORD_TYPE_ACTIVE = "WOM_recordType/active";    // 活动

        // 待办执行状态
        String EXE_STATE_WAIT = "WOM_runState/waitForRun";      // 待执行
        String EXE_STATE_ING = "WOM_runState/runing";           // 执行中
        String EXE_STATE_PAUSING = "WOM_runState/pausing";      // 暂停中
        String EXE_STATE_PAUSED = "WOM_runState/paused";        // 已暂停
        String EXE_STATE_RESUMING = "WOM_runState/resuming";    // 恢复中
        String EXE_STATE_STOPPING = "WOM_runState/stoping";     // 停止中
        String EXE_STATE_STOPPED = "WOM_runState/stoped";       // 已停止
        String EXE_STATE_ABANDONING = "WOM_runState/abandoning";// 放弃中
        String EXE_STATE_ABANDONED = "WOM_runState/abandoned";  // 已放弃
        String EXE_STATE_END = "WOM_runState/finished";         // 已结束

        // 执行系统
        String EXE_SYSTEM_MES = "RM_exeSystem/mes";     // MES系统
        String EXE_SYSTEM_BATCH = "RM_exeSystem/batch"; // 批控系统

        // 活动类型
        String RM_activeType_COMMON = "RM_activeType/common";           // 常规
        String RM_activeType_CHECK = "RM_activeType/check";             // 检查
        String RM_activeType_PUTIN = "RM_activeType/putin";             // 人工投料
        String RM_activeType_BATCH_PUTIN = "RM_activeType/batchPutin";  // 人工投配料
        String RM_activeType_QUALITY = "RM_activeType/quality";         // 检验
        String RM_activeType_OUTPUT = "RM_activeType/output";           // 人工产出
        String RM_activeType_PIPE_PUTIN = "RM_activeType/pipePutin";         // 管道投料
        String RM_activeType_PIPE_BATCH_PUTIN = "RM_activeType/pipeBatchPutin";// 管道投配料
        String RM_activeType_PIPE_OUTPUT = "RM_activeType/pipeOutput";         // 管道产出

        String WOM_receiveState = "WOM_receiveState";
        String WOM_rejectReason = "WOM_rejectReason";

    }

    /**
     * 操作编码
     */
    interface PowerCode {
        String PRODUCE_TASK_LIST = "WOM_1.0.0_produceTask_makeTaskList_self"; // 制造指令单
//        String PRODUCE_TASK_LIST = "WOM_1.0.0_produceTask_makeTaskList_self"; // 制造指令单
    }

    /**
     * url请求
     */
    interface URL {
        String WAREHOUSE_LIST_REF_URL = "/msService/BaseSet/warehouse/warehouse/warehouseRef-query"; // 仓库参照list
        String STORE_SET_LIST_REF_URL = "/msService/BaseSet/warehouse/storeSet/storeSetRef-query"; // 货位参照list
        String CHECK_ITEM_REPORT_LIST_URL = "/msService/WOM/procReport/procReport/data-"+DG_NAME.DG_CHECK_ACTIVITY_REPORT+"?datagridCode=WOM_1.0.0_procReport_checkFeedBackEdit"+DG_NAME.DG_CHECK_ACTIVITY_REPORT; // 检查活动报工list
        String PUT_IN_REPORT_LIST_URL = "/msService/WOM/procReport/procReport/data-"+DG_NAME.DG_PUT_IN_ACTIVITY_REPORT+"?datagridCode=WOM_1.0.0_procReport_putInFeedBackEdit"+DG_NAME.DG_PUT_IN_ACTIVITY_REPORT; // 投料活动报工list
        String BATCH_PUT_IN_REPORT_LIST_URL = "/msService/WOM/procReport/procReport/data-"+DG_NAME.DG_BATCH_PUT_IN_ACTIVITY_REPORT+"?datagridCode=WOM_1.0.0_procReport_batchFeedBackEdit"+DG_NAME.DG_BATCH_PUT_IN_ACTIVITY_REPORT; // 投配料活动报工list
        String OUTPUT_REPORT_LIST_URL = "/msService/WOM/procReport/procReport/data-"+DG_NAME.DG_OUTPUT_ACTIVITY_REPORT+"?datagridCode=WOM_1.0.0_procReport_outputFeedBackEdit"+DG_NAME.DG_OUTPUT_ACTIVITY_REPORT; // 产出活动报工list

        String BATCH_MATERIAL_LIST_REF_URL = "/msService/WOM/batchMaterial/batMaterilPart/recodRefForReport-query"; // 配料记录参照list
        String BATCH_MATERIAL_RELECT_LIST_REF_URL = "/msService/WOM/batchMaterial/batMaterilPart/recodRefForReject-query"; // 退料配料记录参照list
    }

    /**
     * pt名称
     */
    interface DG_NAME {
        String DG_CHECK_ACTIVITY_REPORT = "dg1576203064378"; // 检查活动报工dg
        String DG_PUT_IN_ACTIVITY_REPORT = "dg1576141500525"; // 投料活动报工dg
        String DG_BATCH_PUT_IN_ACTIVITY_REPORT = "dg1576142566684"; // 投配料活动报工dg
        String DG_OUTPUT_ACTIVITY_REPORT = "dg1576221919090"; // 产出活动报工dg
    }
    int scale=2;

}
