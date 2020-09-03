package com.supcon.mes.module_wom_producetask.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FormulaEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/24
 * Email zhangwenshuai1@supcon.com
 * Desc 指令单活动
 */
public class TaskActiveEntity extends BaseEntity {

    /**
     * actEndTime : null
     * actStartTime : null
     * activeBatchState : null
     * activeType : {"id":"RM_activeType/batchPutin","value":"人工投配料"}
     * applyCheckDepId : null
     * applyCheckStaffId : null
     * batchSite : null
     * chcekTip : null
     * checkDepId : null
     * checkStaffId : null
     * container : A
     * exeSystem : {"id":"RM_exeSystem/mes","value":"MES系统"}
     * execSort : 1
     * finalInspection : false
     * formulaActiveId : {"id":3597}
     * formulaId : {"id":1204}
     * formulaProcessId : {"id":1129}
     * id : 3356
     * isAnaly : false
     * isFixedQuantity : false
     * isReplace : false
     * materialId : null
     * maxQuantity : null
     * minQuantity : null
     * name : 投配料1
     * occurTurn : {"id":"RM_occurTurn/onProcess","value":"工序中"}
     * planQuantity : null
     * putinOrder : null
     * remark : null
     * reportSystem : null
     * runState : {"id":"WOM_runState/waitForRun","value":"待执行"}
     * sort : null
     * standardQuantity : null
     * taskId : {"id":1079}
     * taskProcessId : {"id":1105,"name":"U1"}
     * version : 0
     */

    private Long actEndTime;                    // 活动结束时间
    private Long actStartTime;                  // 活动开始时间
    private Object activeBatchState;            // 活动与批状态
    private SystemCodeEntity activeType;        // 活动类型
    private DepartmentEntity applyCheckDepId;   // 申检部门
    private StaffEntity applyCheckStaffId;      // 申检人
    private SystemCodeEntity batchSite;         // 配料方式
    @SerializedName(value = "chcekTip")
    private String checkTip;                    // 确认提示
    private DepartmentEntity checkDepId;        // 检验部门
    private StaffEntity checkStaffId;           // 检验人
    private String container;                   // 容器
    private SystemCodeEntity exeSystem;         // 执行系统
    private String execSort;                    // 报工顺序
    private Boolean finalInspection;            // 完工检验
    private FormulaActiveIdEntity formulaActiveId;  // 配方活动
    private FormulaEntity formulaId;                // 配方
    private FormulaProcessEntity formulaProcessId;  // 配方工序
    private Long id;
    private Boolean isAnaly;
    private Boolean isFixedQuantity;
    private Boolean isReplace;
    private MaterialEntity materialId;              // 物料
    private String materialBatchNum;                // 物料批号
    private BigDecimal maxQuantity;
    private BigDecimal minQuantity;
    private String name;
    private SystemCodeEntity occurTurn;
    private BigDecimal planQuantity;                // 计划数量
    private String putinOrder;                      // 投料顺序
    private String remark;
    private SystemCodeEntity reportSystem;          // 报工系统
    private SystemCodeEntity runState;              // 活动执行状态
    private Object sort;
    private BigDecimal standardQuantity;            // 理论数量
    private BigDecimal sumNum;                      // 累计数量
    private ProduceTaskEntity taskId;               // 工单
    private TaskProcessEntity taskProcessId;        // 工单工序
    private int version;
    private String batchCode;//物料批号
    public boolean isFinish;


    public String getMaterialBatchNum() {
        return materialBatchNum;
    }

    public void setMaterialBatchNum(String materialBatchNum) {
        this.materialBatchNum = materialBatchNum;
    }

    public Long getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(Long actEndTime) {
        this.actEndTime = actEndTime;
    }

    public Long getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(Long actStartTime) {
        this.actStartTime = actStartTime;
    }

    public Object getActiveBatchState() {
        return activeBatchState;
    }

    public void setActiveBatchState(Object activeBatchState) {
        this.activeBatchState = activeBatchState;
    }

    public SystemCodeEntity getActiveType() {
        if (activeType == null){
            activeType = new SystemCodeEntity();
        }
        return activeType;
    }

    public void setActiveType(SystemCodeEntity activeType) {
        this.activeType = activeType;
    }

    public DepartmentEntity getApplyCheckDepId() {
        return applyCheckDepId;
    }

    public void setApplyCheckDepId(DepartmentEntity applyCheckDepId) {
        this.applyCheckDepId = applyCheckDepId;
    }

    public StaffEntity getApplyCheckStaffId() {
        return applyCheckStaffId;
    }

    public void setApplyCheckStaffId(StaffEntity applyCheckStaffId) {
        this.applyCheckStaffId = applyCheckStaffId;
    }

    public SystemCodeEntity getBatchSite() {
        return batchSite;
    }

    public void setBatchSite(SystemCodeEntity batchSite) {
        this.batchSite = batchSite;
    }

    public String getCheckTip() {
        return checkTip;
    }

    public void setCheckTip(String checkTip) {
        this.checkTip = checkTip;
    }

    public DepartmentEntity getCheckDepId() {
        return checkDepId;
    }

    public void setCheckDepId(DepartmentEntity checkDepId) {
        this.checkDepId = checkDepId;
    }

    public StaffEntity getCheckStaffId() {
        return checkStaffId;
    }

    public void setCheckStaffId(StaffEntity checkStaffId) {
        this.checkStaffId = checkStaffId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public SystemCodeEntity getExeSystem() {
        return exeSystem;
    }

    public void setExeSystem(SystemCodeEntity exeSystem) {
        this.exeSystem = exeSystem;
    }

    public String getExecSort() {
        return execSort;
    }

    public void setExecSort(String execSort) {
        this.execSort = execSort;
    }

    public Boolean getFinalInspection() {
        return finalInspection;
    }

    public void setFinalInspection(Boolean finalInspection) {
        this.finalInspection = finalInspection;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public FormulaActiveIdEntity getFormulaActiveId() {
        return formulaActiveId;
    }

    public void setFormulaActiveId(FormulaActiveIdEntity formulaActiveId) {
        this.formulaActiveId = formulaActiveId;
    }

    public FormulaEntity getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(FormulaEntity formulaId) {
        this.formulaId = formulaId;
    }

    public FormulaProcessEntity getFormulaProcessId() {
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

    public Boolean getAnaly() {
        return isAnaly;
    }

    public void setAnaly(Boolean analy) {
        isAnaly = analy;
    }

    public Boolean getFixedQuantity() {
        return isFixedQuantity;
    }

    public void setFixedQuantity(Boolean fixedQuantity) {
        isFixedQuantity = fixedQuantity;
    }

    public Boolean getReplace() {
        return isReplace;
    }

    public void setReplace(Boolean replace) {
        isReplace = replace;
    }

    public MaterialEntity getMaterialId() {
        if (materialId == null){
            materialId = new MaterialEntity();
        }
        return materialId;
    }

    public void setMaterialId(MaterialEntity materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(BigDecimal maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public BigDecimal getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(BigDecimal minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SystemCodeEntity getOccurTurn() {
        return occurTurn;
    }

    public void setOccurTurn(SystemCodeEntity occurTurn) {
        this.occurTurn = occurTurn;
    }

    public BigDecimal getPlanQuantity() {
        return planQuantity!=null?planQuantity.setScale(WomConstant.scale):null;
    }

    public void setPlanQuantity(BigDecimal planQuantity) {
        this.planQuantity = planQuantity;
    }

    public String getPutinOrder() {
        return putinOrder;
    }

    public void setPutinOrder(String putinOrder) {
        this.putinOrder = putinOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SystemCodeEntity getReportSystem() {
        return reportSystem;
    }

    public void setReportSystem(SystemCodeEntity reportSystem) {
        this.reportSystem = reportSystem;
    }

    public SystemCodeEntity getRunState() {
        return runState;
    }

    public void setRunState(SystemCodeEntity runState) {
        this.runState = runState;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public BigDecimal getStandardQuantity() {
        return standardQuantity!=null?standardQuantity.setScale(WomConstant.scale):null;
    }

    public void setStandardQuantity(BigDecimal standardQuantity) {
        this.standardQuantity = standardQuantity;
    }

    public BigDecimal getSumNum() {
        if (sumNum == null){
            sumNum = new BigDecimal(0);
        }
        return sumNum.setScale(WomConstant.scale);
    }

    public void setSumNum(BigDecimal sumNum) {
        this.sumNum = sumNum;
    }

    public ProduceTaskEntity getTaskId() {
        return taskId;
    }

    public void setTaskId(ProduceTaskEntity taskId) {
        this.taskId = taskId;
    }

    public TaskProcessEntity getTaskProcessId() {
        return taskProcessId;
    }

    public void setTaskProcessId(TaskProcessEntity taskProcessId) {
        this.taskProcessId = taskProcessId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    private class FormulaActiveIdEntity extends BaseEntity{
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }
}
