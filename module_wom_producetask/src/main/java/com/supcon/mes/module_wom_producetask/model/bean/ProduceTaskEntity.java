package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.bean.wom.FormulaEntity;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/24
 * Email zhangwenshuai1@supcon.com
 * Desc 生产指令单
 */
public class ProduceTaskEntity extends BaseEntity {

    /**
     * id : 1255
     * tableNo : produceTask_20200323_003
     */

    private Long id;
    private String tableNo;
    private FormulaEntity formulaId;    // 配方
    private FactoryModelEntity lineId;  // 生产线
    private BigDecimal planNum;         // 计划产量
    private Long planStartTime;         // 计划开始时间
    private Long planEndTime;           // 计划结束时间
    private Long actStartTime;          // 实际开始时间
    private Long actEndTime;            // 实际结束时间
    private Boolean batchContral;       //是否批控

    public void setBatchContral(Boolean batchContral) {
        this.batchContral = batchContral;
    }

    public Boolean getBatchContral() {
        return batchContral;
    }

    public Long getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Long planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Long getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Long planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Long getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(Long actStartTime) {
        this.actStartTime = actStartTime;
    }

    public Long getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(Long actEndTime) {
        this.actEndTime = actEndTime;
    }

    public BigDecimal getPlanNum() {
        return planNum!=null?planNum.setScale(WomConstant.scale,BigDecimal.ROUND_HALF_UP):null;
    }

    public void setPlanNum(BigDecimal planNum) {
        this.planNum = planNum;
    }

    public FactoryModelEntity getLineId() {
        return lineId;
    }

    public void setLineId(FactoryModelEntity lineId) {
        this.lineId = lineId;
    }

    public FormulaEntity getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(FormulaEntity formulaId) {
        this.formulaId = formulaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }
}
