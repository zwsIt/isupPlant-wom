package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

/**
 * Created by wanghaidong on 2020/8/17
 * Email:wanghaidong1@supcon.com
 */
public class ActiExelogEntity extends BaseEntity {
    public Long id;
    public String activeName;//活动名称
    public Long actEndTime;//活动结束时间
    public Long actStartTime;//活动开始时间
    public BaseIdValueEntity activeType;//活动类型
    public Long actlongTime;//活动时长
    public Long actualNum;//实际数量
    public String beforeNum;//前重
    public String afterNum;//后重
    public String checkResult;//检验结果
    public String checkState;//检验状态
    public BaseIdValueEntity exeSystem;//执行系统
    public Long execSort;//报工顺序
    public Boolean isPassCheck;
    public String materialBatchNum;//物料批号
    public MaterialEntity materialId;//物料
    public String name;
    public String produceBatchNum;//生产批号
    public String remark;//备注
    public Long tableInfoId;
    public ProduceTaskEntity taskId;//生产指令单
    public TaskProcessEntity taskProcessId;//生产指令单工序
    public Float useNum;//使用量
    public int version;
    public boolean isFinish;
    public TaskActiveEntity taskActiveId;
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

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
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

    public BaseIdValueEntity getActiveType() {
        return activeType;
    }

    public void setActiveType(BaseIdValueEntity activeType) {
        this.activeType = activeType;
    }

    public Long getActlongTime() {
        return actlongTime;
    }

    public void setActlongTime(Long actlongTime) {
        this.actlongTime = actlongTime;
    }

    public Long getActualNum() {
        return actualNum;
    }

    public void setActualNum(Long actualNum) {
        this.actualNum = actualNum;
    }

    public String getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(String beforeNum) {
        this.beforeNum = beforeNum;
    }

    public String getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(String afterNum) {
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

    public BaseIdValueEntity getExeSystem() {
        return exeSystem;
    }

    public void setExeSystem(BaseIdValueEntity exeSystem) {
        this.exeSystem = exeSystem;
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
