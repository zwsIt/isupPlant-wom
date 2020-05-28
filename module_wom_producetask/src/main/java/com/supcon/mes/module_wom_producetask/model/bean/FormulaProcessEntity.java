package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.wom.FormulaEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/26
 * Email zhangwenshuai1@supcon.com
 * Desc 配方工序
 */
public class FormulaProcessEntity extends BaseEntity {
    private Long id;
    private BigDecimal longTime;    // 标准时长(分)
    private String name;
    private Integer procSort;       // 报工显示顺序
    private Integer exeOrder;       // 报工顺序
    private FormulaEntity formula;  // 配方
    private TaskProcessEntity.ProcessTypeEntity processType; // 工序类型

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLongTime() {
        return longTime;
    }

    public void setLongTime(BigDecimal longTime) {
        this.longTime = longTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProcSort() {
        return procSort;
    }

    public void setProcSort(Integer procSort) {
        this.procSort = procSort;
    }

    public Integer getExeOrder() {
        return exeOrder;
    }

    public void setExeOrder(Integer exeOrder) {
        this.exeOrder = exeOrder;
    }

    public FormulaEntity getFormula() {
        return formula;
    }

    public void setFormula(FormulaEntity formula) {
        this.formula = formula;
    }

    public TaskProcessEntity.ProcessTypeEntity getProcessType() {
        return processType;
    }

    public void setProcessType(TaskProcessEntity.ProcessTypeEntity processType) {
        this.processType = processType;
    }
}
