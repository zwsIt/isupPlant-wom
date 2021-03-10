package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

import java.math.BigDecimal;

/**
 * Created by wanghaidong on 2020/8/17
 * Email:wanghaidong1@supcon.com
 */
public class ActiExelogEntity extends BaseEntity {
    public Long id;
    public String name;//活动名称
    private Long actEndTime;//活动结束时间
    private Long actStartTime;//活动开始时间
    public SystemCodeEntity activeType;//活动类型
    private BigDecimal actlongTime;//执行时长(分)
    private BigDecimal actualNum;//实际数量
    private BigDecimal beforeNum;//前重
    private BigDecimal afterNum;//后重
    public String checkResult;//检验结果
    public String checkState;//检验状态
    public SystemCodeEntity exeSystem;//执行系统
    public Long execSort;//报工顺序
    public Boolean isPassCheck;
    public String materialBatchNum;//物料批号
    public MaterialEntity materialId;//物料
    public String produceBatchNum;//生产批号
    public String remark;//备注
    public Long tableInfoId;
    public ProduceTaskEntity taskId;//生产指令单
    public TaskProcessEntity taskProcessId;//生产指令单工序
    public Float useNum;//使用量
    public int version;
    public boolean isFinish;
    public TaskActiveEntity taskActiveId;
    private SystemCodeEntity runState; // 活动执行状态

    public SystemCodeEntity getRunState() {
        if (runState == null){
            runState = new SystemCodeEntity();
        }
        return runState;
    }

    public void setRunState(SystemCodeEntity runState) {
        this.runState = runState;
    }

    public Long getId() {
        return id;
    }

    public TaskActiveEntity getTaskActiveId() {
        return taskActiveId;
    }

    public void setTaskActiveId(TaskActiveEntity taskActiveId) {
        this.taskActiveId = taskActiveId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getActlongTime() {
        return actlongTime;
    }

    public void setActlongTime(BigDecimal actlongTime) {
        this.actlongTime = actlongTime;
    }

    public BigDecimal getActualNum() {
        return actualNum;
    }

    public void setActualNum(BigDecimal actualNum) {
        this.actualNum = actualNum;
    }

    public BigDecimal getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(BigDecimal beforeNum) {
        this.beforeNum = beforeNum;
    }

    public BigDecimal getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(BigDecimal afterNum) {
        this.afterNum = afterNum;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public Long getExecSort() {
        return execSort;
    }

    public void setExecSort(Long execSort) {
        this.execSort = execSort;
    }

    public Boolean getPassCheck() {
        return isPassCheck;
    }

    public void setPassCheck(Boolean passCheck) {
        isPassCheck = passCheck;
    }

    public String getMaterialBatchNum() {
        return materialBatchNum;
    }

    public void setMaterialBatchNum(String materialBatchNum) {
        this.materialBatchNum = materialBatchNum;
    }

    public MaterialEntity getMaterialId() {
        return materialId;
    }

    public void setMaterialId(MaterialEntity materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduceBatchNum() {
        return produceBatchNum;
    }

    public void setProduceBatchNum(String produceBatchNum) {
        this.produceBatchNum = produceBatchNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public ProduceTaskEntity getTaskId() {
        if (taskId == null){
            taskId = new ProduceTaskEntity();
        }
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

    public Float getUseNum() {
        return useNum;
    }

    public void setUseNum(Float useNum) {
        this.useNum = useNum;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }
}
