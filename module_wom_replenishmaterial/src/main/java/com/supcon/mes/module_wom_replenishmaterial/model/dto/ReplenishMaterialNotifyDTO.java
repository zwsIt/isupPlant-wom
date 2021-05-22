package com.supcon.mes.module_wom_replenishmaterial.model.dto;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.AssociatedEquipmentEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;

import java.math.BigDecimal;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/11
 * @email zhangwenshuai1@supcon.com
 * Desc 补料单创建实体
 */
public class ReplenishMaterialNotifyDTO extends BaseEntity {
    /**
     * 补料通知单
     */
    private ReplenishMaterialNotifyEntity fmnNotice;
    /**
     * 计划数量
     */
    private BigDecimal planNumber;

    public ReplenishMaterialNotifyEntity getFmnNotice() {
        return fmnNotice;
    }

    public void setFmnNotice(ReplenishMaterialNotifyEntity fmnNotice) {
        this.fmnNotice = fmnNotice;
    }

    public BigDecimal getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(BigDecimal planNumber) {
        this.planNumber = planNumber;
    }
}
