package com.supcon.mes.module_wom_batchmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.BatchLineEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;
import com.supcon.mes.module_wom_producetask.model.bean.ProduceTaskEntity;
import com.supcon.mes.module_wom_producetask.model.bean.TaskActiveEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/19
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令集实体
 */
public class BatchMaterialSetEntity extends BaseEntity {

    /**
     * attrMap
     */
    private Object attrMap;
    /**
     * cid
     */
    private Long cid;
    /**
     * createStaff
     */
    private StaffEntity createStaff;
    /**
     * createTime
     */
    private Long createTime;
    /**
     * 当前配料线
     */
    private BatchLineEntity currentBurendManage;
    /**
     * endTime
     */
    private Long endTime;
    /**
     * 配料顺序
     */
    private Integer fmOrder;
    /**
     * 任务状态:初始化、执行中、完成
     */
    private SystemCodeEntity fmState;
    /**
     * 当前任务:配料、中转、转运至下游
     */
    private SystemCodeEntity fmTask;
    /**
     * formulaActiveId
     */
    private TaskActiveEntity.FormulaActiveIdEntity formulaActiveId;
    /**
     * id
     */
    private Long id;
    /**
     * startTime
     */
    private Long startTime;
    /**
     * status
     */
    private Integer status;
    /**
     * tableInfoId
     */
    private Long tableInfoId;
    /**
     * tableNo
     */
    private String tableNo;
    /**
     * task
     */
    private ProduceTaskEntity task;
    /**
     * valid
     */
    private Boolean valid;
    /**
     * version
     */
    private Integer version;
    /**
     * 容器
     */
    private VesselEntity vessel;

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public StaffEntity getCreateStaff() {
        return createStaff;
    }

    public void setCreateStaff(StaffEntity createStaff) {
        this.createStaff = createStaff;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public BatchLineEntity getCurrentBurendManage() {
        return currentBurendManage;
    }

    public void setCurrentBurendManage(BatchLineEntity currentBurendManage) {
        this.currentBurendManage = currentBurendManage;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getFmOrder() {
        return fmOrder;
    }

    public void setFmOrder(Integer fmOrder) {
        this.fmOrder = fmOrder;
    }

    public SystemCodeEntity getFmState() {
        return fmState;
    }

    public void setFmState(SystemCodeEntity fmState) {
        this.fmState = fmState;
    }

    public SystemCodeEntity getFmTask() {
        return fmTask;
    }

    public void setFmTask(SystemCodeEntity fmTask) {
        this.fmTask = fmTask;
    }

    public TaskActiveEntity.FormulaActiveIdEntity getFormulaActiveId() {
        return formulaActiveId;
    }

    public void setFormulaActiveId(TaskActiveEntity.FormulaActiveIdEntity formulaActiveId) {
        this.formulaActiveId = formulaActiveId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public ProduceTaskEntity getTask() {
        return task;
    }

    public void setTask(ProduceTaskEntity task) {
        this.task = task;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public VesselEntity getVessel() {
        return vessel;
    }

    public void setVessel(VesselEntity vessel) {
        this.vessel = vessel;
    }
}
