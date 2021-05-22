package com.supcon.mes.module_wom_replenishmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;

import java.math.BigDecimal;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/11
 * @email zhangwenshuai1@supcon.com
 * Desc 补料单实体
 */
public class ReplenishMaterialTableEntity extends BaseEntity {

    /**
     * 实际数量
     */
    private BigDecimal actualNumber;
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
     * 节点设备
     */
    private AssociatedEquipmentEntity equipment;
    /**
     * 状态
     */
    private SystemCodeEntity fmState;
    /**
     * 补料通知单
     */
    private ReplenishMaterialNotifyEntity fmnNotice;
    /**
     * id
     */
    private Long id;
    /**
     * 物料
     */
    private Good material;
    /**
     * pending
     */
    private PendingEntity pending;
    /**
     * 计划补料数量
     */
    private BigDecimal planNumber;
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
     * valid
     */
    private Boolean valid = true;
    /**
     * version
     */
    private Integer version;
    /**
     * 容器
     */
    private VesselEntity vessel;
    /**
     * 操作人
     */
    private StaffEntity operator;

    /**
     *节点设备扫描标识
     */
    private boolean eqScanFlag;
    /**
     *容器扫描标识
     */
    private boolean vesselScanFlag;

    public boolean isEqScanFlag() {
        return eqScanFlag;
    }

    public void setEqScanFlag(boolean eqScanFlag) {
        this.eqScanFlag = eqScanFlag;
    }

    public boolean isVesselScanFlag() {
        return vesselScanFlag;
    }

    public void setVesselScanFlag(boolean vesselScanFlag) {
        this.vesselScanFlag = vesselScanFlag;
    }

    public StaffEntity getOperator() {
        return operator;
    }

    public void setOperator(StaffEntity operator) {
        this.operator = operator;
    }

    public BigDecimal getActualNumber() {
        return actualNumber;
    }

    public void setActualNumber(BigDecimal actualNumber) {
        this.actualNumber = actualNumber;
    }

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

    public AssociatedEquipmentEntity getEquipment() {
        return equipment;
    }

    public void setEquipment(AssociatedEquipmentEntity equipment) {
        this.equipment = equipment;
    }

    public SystemCodeEntity getFmState() {
        return fmState;
    }

    public void setFmState(SystemCodeEntity fmState) {
        this.fmState = fmState;
    }

    public ReplenishMaterialNotifyEntity getFmnNotice() {
        return fmnNotice;
    }

    public void setFmnNotice(ReplenishMaterialNotifyEntity fmnNotice) {
        this.fmnNotice = fmnNotice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Good getMaterial() {
        return material;
    }

    public void setMaterial(Good material) {
        this.material = material;
    }

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public BigDecimal getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(BigDecimal planNumber) {
        this.planNumber = planNumber;
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
