package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/23
 * Email zhangwenshuai1@supcon.com
 * Desc 备料记录实体
 */
public class PrepareMaterialPartEntity extends BaseEntity {

    /**
     * attrMap : null
     * cid : 1000
     * createStaff : null
     * createTime : null
     * doneNum : 5
     * exeStaff : {"id":1007,"name":"cuixin"}
     * hasAllocated : false
     * headId : {"id":1219,"prePareDate":1587461523094,"prePareStaff":{"id":1007,"name":"cuixin"},"tableNo":"prepareMaterial_20200421_013"}
     * id : 1276
     * inStoreId : {"code":null,"id":null,"name":null}
     * inWareId : {"code":"factory03","id":1002,"name":"原料仓03","storesetState":false}
     * materialBatchNum : 12
     * materialId : {"code":"A","id":1003,"name":"A"}
     * partState : {"id":"WOM_prePareState/finish","value":"已备料"}
     * receiveNum : 5
     * rejectReason : null
     * remark : null
     * retirementState : null
     * returnNum : null
     * tableInfoId : null
     * valid : true
     * version : 2
     */

    private Object attrMap;
    private Long cid;
    private StaffEntity createStaff;
    private Long createTime;
    private BigDecimal doneNum; // 备料数量
    private StaffEntity exeStaff; // 备料操作人
    private boolean hasAllocated; // 是否已调拨
    private PrepareMaterialEntity headId; // 表头
    private Long id;
    private StoreSetEntity inStoreId; // 备入货位
    private WarehouseEntity inWareId; // 备入仓库
    private String materialBatchNum; // 物料批号
    private MaterialEntity materialId; // 物料
    private SystemCodeEntity partState; // 备料记录状态
    private Long prePareDate; // 备料时间
    private Long receiveDate; // 签收时间
    private StaffEntity receiveStaff; // 签收人
    private SystemCodeEntity receiveState; //签收状态
    private BigDecimal receiveNum; // 复秤数量
    private SystemCodeEntity rejectReason; // 拒签原因
    private String remark;
    private SystemCodeEntity retirementState; // 退废状态
    private BigDecimal returnNum; // 已退数量
    private Long tableInfoId;
    private boolean valid;
    private int version;

    private boolean checked; // 是否选中(参照页面使用)

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

    public BigDecimal getDoneNum() {
        return doneNum;
    }

    public void setDoneNum(BigDecimal doneNum) {
        this.doneNum = doneNum;
    }

    public StaffEntity getExeStaff() {
        return exeStaff;
    }

    public void setExeStaff(StaffEntity exeStaff) {
        this.exeStaff = exeStaff;
    }

    public boolean isHasAllocated() {
        return hasAllocated;
    }

    public void setHasAllocated(boolean hasAllocated) {
        this.hasAllocated = hasAllocated;
    }

    public PrepareMaterialEntity getHeadId() {
        return headId;
    }

    public void setHeadId(PrepareMaterialEntity headId) {
        this.headId = headId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreSetEntity getInStoreId() {
        return inStoreId;
    }

    public void setInStoreId(StoreSetEntity inStoreId) {
        this.inStoreId = inStoreId;
    }

    public WarehouseEntity getInWareId() {
        return inWareId;
    }

    public void setInWareId(WarehouseEntity inWareId) {
        this.inWareId = inWareId;
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

    public SystemCodeEntity getPartState() {
        return partState;
    }

    public void setPartState(SystemCodeEntity partState) {
        this.partState = partState;
    }

    public Long getPrePareDate() {
        return prePareDate;
    }

    public void setPrePareDate(Long prePareDate) {
        this.prePareDate = prePareDate;
    }

    public Long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Long receiveDate) {
        this.receiveDate = receiveDate;
    }

    public StaffEntity getReceiveStaff() {
        return receiveStaff;
    }

    public void setReceiveStaff(StaffEntity receiveStaff) {
        this.receiveStaff = receiveStaff;
    }

    public SystemCodeEntity getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(SystemCodeEntity receiveState) {
        this.receiveState = receiveState;
    }

    public BigDecimal getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(BigDecimal receiveNum) {
        this.receiveNum = receiveNum;
    }

    public SystemCodeEntity getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(SystemCodeEntity rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SystemCodeEntity getRetirementState() {
        return retirementState;
    }

    public void setRetirementState(SystemCodeEntity retirementState) {
        this.retirementState = retirementState;
    }

    public BigDecimal getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(BigDecimal returnNum) {
        this.returnNum = returnNum;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
