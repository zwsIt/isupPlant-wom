package com.supcon.mes.module_wom_replenishmaterial.constant;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/11
 * @email zhangwenshuai1@supcon.com
 * Desc
 */
public interface ReplenishConstant {
    interface Router {
        String REPLENISH_MATERIAL_EDIT = "REPLENISH_MATERIAL_EDIT"; // 补料编辑
        String ASSOCIATION_EQUIPMENT_LIST_REF = "ASSOCIATION_EQUIPMENT_LIST_REF"; // 关联设备参照
        String REPLENISH_MATERIAL_SCAN = "REPLENISH_MATERIAL_SCAN"; // 补料扫码
    }

    interface SystemCode {
        // 通知单状态
        String NOTIFY_INSTRUCTION = "WOM_fmNoticState/issue"; // 下达

        // 补料单状态
        String TABLE_STATE_NEW = "WOM_fmBillState/new"; // 待补料
        String TABLE_STATE_ING = "WOM_fmBillState/executing"; // 补料中
        String TABLE_STATE_COMPLETE = "WOM_fmBillState/complete"; // 补料完成

        // 补料模式
        String MODEL_MANUAL = "HierarchicalMod_runModelCode1/activeExecution"; // 主动执行
        String MODEL_NOTIFY = "HierarchicalMod_runModelCode1/noticeExecution"; // 通知执行
        // 进料模式
        String MODEL_AIR = "HierarchicalMod_feedStockTypeCode1/feedingAtAirOutlet"; // 风送口补料
        String MODEL_BUCKET_AIR = "HierarchicalMod_feedStockTypeCode1/feedingAtSiloEntrance"; // 转运桶风送口补料
        String MODEL_BUCKET_SILO = "HierarchicalMod_feedStockTypeCode1/feedingPointReplenishment"; // 转运桶料仓补料
        String MODEL_BUCKET_POINT = "HierarchicalMod_feedStockTypeCode1/rackPoint"; // 转运桶上料点补料
//        String MODEL_NOTIFY = "HierarchicalMod_runModelCode1/feedingAtSiloEntrance"; // 转运桶风送口补料

    }

    interface URL {
        String REPLENISH_MATERIAL_PENDING_LIST_URL = "/msService/WOM/fillMaterial/fmBill/fmBillList-pending"; // 补料单待办
        String REPLENISH_MATERIAL_SCAN_LIST_URL = "/msService/WOM/fillMaterial/fmBill/fmBillRef-query"; // 补料单参照（PC特殊处理页面，已生效待补料单）
        String REPLENISH_MATERIAL_TABLE_EDIT_DG_LIST_URL = "/msService/WOM/fillMaterial/fmBill/data-" + DG_NAME.DG_REPLENISH_MATERIAL_EDIT
                + "?datagridCode=WOM_1.0.0_fillMaterial_fmBillEdit"+ DG_NAME.DG_REPLENISH_MATERIAL_EDIT; // 补料单编辑dg
        String ASSOCIATION_EQUIPMENT_LIST_REF_URL = "/msService/HierarchicalMod/factoryModel/ftyEquipment/relationEquRef-query";
    }
    /**
     * pt名称
     */
    interface DG_NAME {
        String DG_REPLENISH_MATERIAL_EDIT = "dg1620710952134"; // 补料单编辑dg
    }

    interface IntentKey {
        String REPLENISH_MATERIAL_TABLE = "REPLENISH_MATERIAL_TABLE"; //补料单
        String WORK_FLOW_BTN_INFO = "WORK_FLOW_BTN_INFO";
    }

    interface ViewCode {
        String REPLENISH_MATERIAL_EDIT = "fmBillEdit";
    }
}
