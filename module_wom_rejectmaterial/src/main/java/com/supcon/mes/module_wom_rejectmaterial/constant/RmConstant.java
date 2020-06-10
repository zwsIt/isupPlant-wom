package com.supcon.mes.module_wom_rejectmaterial.constant;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 退料模块常量
 */
public interface RmConstant {

    /**
     * 系统编码
     */
    interface SystemCode {
        //退料类型
        String REJECT_TYPE_BATCH = "WOM_rejectType/forBatch";      // 配料退料
        String REJECT_TYPE_PREPARE = "WOM_rejectType/forPrepare";  // 备料退料

        // 配料记录签收状态
        String RECEIVE_STATE_RECEIVE = "WOM_receiveState/receive";   // 签收
        String RECEIVE_STATE_REJECT = "WOM_receiveState/reject";    // 拒签

        // 活动类型
        String RM_activeType_COMMON = "RM_activeType/common";           // 常规
        String RM_activeType_CHECK = "RM_activeType/check";             // 检查
        String RM_activeType_PUTIN = "RM_activeType/putin";             // 人工投料
        String RM_activeType_BATCH_PUTIN = "RM_activeType/batchPutin";  // 人工投配料
        String RM_activeType_QUALITY = "RM_activeType/quality";         // 检验
        String RM_activeType_OUTPUT = "RM_activeType/output";           // 人工产出


    }

    /**
     * 操作编码
     */
    interface PowerCode {
//        String BM_INSTRUCTION_EDIT = "TaskEvent_09367jb"; // 配料指令单编辑
    }

    /**
     * url请求
     */
    interface URL {
        String REJECT_BATCH_MATERIAL_PENDING_LIST_URL = "/msService/WOM/rejectMaterilal/rejectMaterial/batchRejectList-pending";    // 配料退料待办list
        String REJECT_PREPARE_MATERIAL_PENDING_LIST_URL = "/msService/WOM/rejectMaterilal/rejectMaterial/prePareRejectList-pending"; // 备料退料待办list
        String REJECT_BATCH_MATERIAL_EDIT_DG_LIST_URL = "/msService/WOM/rejectMaterilal/rejectMaterial/data-"+ DG_NAME.DG_REJECT_BATCH_MATERIAL_EDIT+"?" +
                "datagridCode=WOM_1.0.0_rejectMaterilal_batchRejectEdit" + DG_NAME.DG_REJECT_BATCH_MATERIAL_EDIT; // 配料退料编辑dg_list
        String REJECT_PREPARE_MATERIAL_EDIT_DG_LIST_URL = "/msService/WOM/rejectMaterilal/rejectMaterial/data-"+ DG_NAME.DG_PREPARE_BATCH_MATERIAL_EDIT+"?" +
                "datagridCode=WOM_1.0.0_rejectMaterilal_prePareRejectEdit" + DG_NAME.DG_PREPARE_BATCH_MATERIAL_EDIT; // 备料退料编辑dg_list
        String REJECT_BATCH_MATERIAL_EDIT__URL = "/msService/WOM/rejectMaterilal/rejectMaterial/batchRejectEdit"; // 配料退料编辑提交
        String REJECT_PREPARE_MATERIAL_EDIT_URL = "/msService/WOM/rejectMaterilal/rejectMaterial/prePareRejectEdit"; // 备料退料编辑提交
        String REJECT_BATCH_RECORD_MATERIAL_LIST_URL = "/msService/WOM/rejectMaterilal/rejctMatalPart/batchRejectPrtList-query"; // 配料退料记录
        String REJECT_PREPARE_RECORD_MATERIAL_LIST_URL = "/msService/WOM/rejectMaterilal/rejctMatalPart/prePareRejectPrtList-query"; // 备料退料记录
    }

    /**
     * pt名称
     */
    interface DG_NAME {
        String DG_REJECT_BATCH_MATERIAL_EDIT = "dg1581993495790"; // 配料退料编辑dg
        String DG_PREPARE_BATCH_MATERIAL_EDIT = "dg1581994918681"; // 备料退料编辑dg
    }

}
