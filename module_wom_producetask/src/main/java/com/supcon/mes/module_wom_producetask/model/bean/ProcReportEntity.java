package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/24
 * Email zhangwenshuai1@supcon.com
 * Desc 过程报工/工序报工实体
 */
public class ProcReportEntity extends BaseEntity {
    private Long id;
    private ProduceTaskEntity taskId;           // 工单
    private TaskProcessEntity taskProcessId;    // 工单工序
    private TaskActiveEntity taskActiveId;      // 工单活动
    private SystemCodeEntity procReportType;    // 过程报工类型
    private MaterialEntity materialId;          // 物料
    private Boolean isFinish;                   // 是否完成
    private Boolean detailsIsNull;              // 检查表体是否为空

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TaskActiveEntity getTaskActiveId() {
        return taskActiveId;
    }

    public void setTaskActiveId(TaskActiveEntity taskActiveId) {
        this.taskActiveId = taskActiveId;
    }

    public SystemCodeEntity getProcReportType() {
        return procReportType;
    }

    public void setProcReportType(SystemCodeEntity procReportType) {
        this.procReportType = procReportType;
    }

    public MaterialEntity getMaterialId() {
        return materialId;
    }

    public void setMaterialId(MaterialEntity materialId) {
        this.materialId = materialId;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public Boolean getDetailsIsNull() {
        return detailsIsNull;
    }

    public void setDetailsIsNull(Boolean detailsIsNull) {
        this.detailsIsNull = detailsIsNull;
    }
}
