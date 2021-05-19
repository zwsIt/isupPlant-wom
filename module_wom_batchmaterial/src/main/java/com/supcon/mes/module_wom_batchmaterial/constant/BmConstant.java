package com.supcon.mes.module_wom_batchmaterial.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 配料模块常量
 */
public interface BmConstant {

    /**
     * 系统编码
     */
    interface SystemCode {
        // 配料记录状态
        String RECORD_STATE_BATCH = "WOM_batMatState/noBatch";      // 未配料
        String RECORD_STATE_BATCHING = "WOM_batMatState/batching";  // 配料中
        String RECORD_STATE_CANCEL = "WOM_batMatState/cancel";      // 已取消
        String RECORD_STATE_SIGN = "WOM_batMatState/delivered";     // 待签收
        String RECORD_STATE_DELIVER = "WOM_batMatState/finish";     // 待派送
        String RECORD_STATE_REJECT = "WOM_batMatState/reject";      // 已拒签
        String RECORD_STATE_ALLOCATE = "WOM_batMatState/waitAllocat";   // 待放
        String RECORD_STATE_PUTIN = "WOM_batMatState/waitPut";      // 待投料
        String RECORD_STATE_PUTTED = "WOM_batMatState/puted";       // 已投料
        String RECORD_STATE_FINISH = "WOM_batMatState/done";        // 已完成
        String RECORD_STATE_STOOP = "WOM_batMatState/stop";         // 已中止

        // 配料记录签收状态
        String RECEIVE_STATE_RECEIVE = "WOM_receiveState/receive";   // 签收
        String RECEIVE_STATE_REJECT = "WOM_receiveState/reject";    // 拒签

        // 退废状态
        String RETRAIL_STATE_01 = "WOM_retirementState/damaged";    // 已废
        String RETRAIL_STATE_02 = "WOM_retirementState/returned";   // 已退
        String RETRAIL_STATE_03 = "WOM_retirementState/waitDamage"; // 待废
        String RETRAIL_STATE_04 = "WOM_retirementState/waitReturn"; // 待退


    }

    /**
     * 操作编码
     */
    interface PowerCode {
        String BM_INSTRUCTION_EDIT = "TaskEvent_09367jb"; // 配料指令单编辑
    }

    /**
     * url请求
     */
    interface URL {
        String BATCH_MATERIAL_INSTRUCTION_PENDING_LIST_URL = "/msService/WOM/batchMaterial/batchMateril/batchMaterilList-pending"; // 配料指令待办list
        String BATCH_MATERIAL_INSTRUCTION_DG_LIST_URL = "/msService/WOM/batchMaterial/batchMateril/data-"+DG_NAME.DG_BATCH_MATERIAL_EDIT+"?" +
                "datagridCode=WOM_1.0.0_batchMaterial_batchMaterialOrder" +DG_NAME.DG_BATCH_MATERIAL_EDIT; // 配料指令dg_list
        String BATCH_MATERIAL_RECORDS_LIST_URL = "/msService/WOM/batchMaterial/batMaterilPart/materialRecodList-query"; // 配料记录list
        String BATCH_MATERIAL_RECORDS_ABANDON_LIST_URL = "/msService/WOM/batchMaterial/batMaterilPart/baRetireMentPDAList-query"; // 配料记录退废list
        String BATCH_MATERIAL_RECORDS_RECALL_LIST_URL = "/msService/WOM/batchMaterial/batMaterilPart/recallRecordPDAList-query"; // 配料记录撤回list
    }

    /**
     * pt名称
     */
    interface DG_NAME {
        String DG_BATCH_MATERIAL_EDIT = "dg1582113356339"; // 配料指令编辑dg
    }

    /**
     * 视图URL
     */
    interface ViewName {
        String BATCH_MATERIAL_EDIT = "/msService/WOM/batchMaterial/batchMateril/batchMaterialOrder"; // 配料指令编辑
    }

    interface Router {
        String BATCH_MATERIAL_INSTRUCTION_LIST = "BATCH_MATERIAL_INSTRUCTION_LIST"; // 指令列表
    }
}
