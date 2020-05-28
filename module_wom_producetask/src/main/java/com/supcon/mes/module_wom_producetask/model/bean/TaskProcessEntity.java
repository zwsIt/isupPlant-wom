package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.bean.wom.FormulaEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/24
 * Email zhangwenshuai1@supcon.com
 * Desc 生产指令单工序
 */
public class TaskProcessEntity extends BaseEntity {

    /**
     * equipmentId : {"id":1006,"name":"批次生产1单元"}
     * formulaId : {"id":1323}
     * formulaProcessId : {"id":1320}
     * id : 1490
     * longTime : null
     * name : U01
     * procSort : 1
     * processType : null
     * remark : null
     * sort : null
     * taskId : {"id":1262}
     * version : 0
     */

    private FactoryModelEntity equipmentId;         // 工作单元
    private FormulaEntity formulaId;                // 配方Id
    private FormulaProcessEntity formulaProcessId;  // 配方工序Id
    private Long id;
    private BigDecimal longTime;                    // 标准时长(分)
    private String name;
    private Integer procSort;                       // 报工显示顺序
    private ProcessTypeEntity processType;          // 工序类型
    private String remark;
    private int sort;
    private ProduceTaskEntity taskId;               // 工单Id
//    private int version;
    private Integer exeOrder;                       // 报工顺序
    private Long actStartTime;                      // 实际开始时间
    private Long actEndTime;                        // 实际结束时间
    private SystemCodeEntity processRunState;       // 工序执行状态

    public SystemCodeEntity getProcessRunState() {
        if (processRunState == null){
            processRunState = new SystemCodeEntity();
        }
        return processRunState;
    }

    public void setProcessRunState(SystemCodeEntity processRunState) {
        this.processRunState = processRunState;
    }

    public Integer getExeOrder() {
        return exeOrder;
    }

    public void setExeOrder(Integer exeOrder) {
        this.exeOrder = exeOrder;
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

    public FactoryModelEntity getEquipmentId() {
        if (equipmentId == null){
            equipmentId = new FactoryModelEntity();
        }
        return equipmentId;
    }

    public void setEquipmentId(FactoryModelEntity equipmentId) {
        this.equipmentId = equipmentId;
    }

    public FormulaEntity getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(FormulaEntity formulaId) {
        this.formulaId = formulaId;
    }

    public FormulaProcessEntity getFormulaProcessId() {
        if (formulaProcessId == null){
            formulaProcessId = new FormulaProcessEntity();
        }
        return formulaProcessId;
    }

    public void setFormulaProcessId(FormulaProcessEntity formulaProcessId) {
        this.formulaProcessId = formulaProcessId;
    }

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

    public ProcessTypeEntity getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessTypeEntity processType) {
        this.processType = processType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public ProduceTaskEntity getTaskId() {
        return taskId;
    }

    public void setTaskId(ProduceTaskEntity taskId) {
        this.taskId = taskId;
    }

//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }

    /**
     * TaskProcessEntity
     * created by zhangwenshuai1 2020/3/26
     * 工序类型
     */
    public static class ProcessTypeEntity extends BaseEntity{
        /**
         * id : 1006
         * name : 批次生产1单元
         * code : 12
         */

        private Long id;
        private String name;
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
