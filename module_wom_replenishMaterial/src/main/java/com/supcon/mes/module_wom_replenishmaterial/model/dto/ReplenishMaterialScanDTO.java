package com.supcon.mes.module_wom_replenishmaterial.model.dto;

import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTablePartEntity;

import java.util.List;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/16
 * @email zhangwenshuai1@supcon.com
 * Desc
 */
public class ReplenishMaterialScanDTO {
    /**
     * state：start  or  complete
     */
    private String state;
    /**
     * fmBill
     */
    private ReplenishMaterialTableEntity fmBill;
    /**
     * fmBillDetais
     */
    private List<ReplenishMaterialTablePartEntity> fmBillDetais;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ReplenishMaterialTableEntity getFmBill() {
        return fmBill;
    }

    public void setFmBill(ReplenishMaterialTableEntity fmBill) {
        this.fmBill = fmBill;
    }

    public List<ReplenishMaterialTablePartEntity> getFmBillDetais() {
        return fmBillDetais;
    }

    public void setFmBillDetais(List<ReplenishMaterialTablePartEntity> fmBillDetais) {
        this.fmBillDetais = fmBillDetais;
    }
}
