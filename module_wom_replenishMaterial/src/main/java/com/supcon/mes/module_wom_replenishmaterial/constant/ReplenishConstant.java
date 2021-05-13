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
    }

    interface SystemCode {
        String NOTIFY_INSTRUCTION = "WOM_fmNoticState/issue"; // 下达
    }

    interface URL {
        String REPLENISH_MATERIAL_PENDING_LIST_URL = "/msService/WOM/fillMaterial/fmBill/fmBillList-pending"; // 补料单待办
        String REPLENISH_MATERIAL_SCAN_LIST_URL = "/msService/WOM/fillMaterial/fmBill/fmBillList-query"; // 补料单查询
    }
}
