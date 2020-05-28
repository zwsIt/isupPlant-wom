package com.supcon.mes.module_wom_producetask.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/10
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录Entity
 */
public class BatchMaterialPartEntity extends BaseEntity {

    /**
     * attrMap : null
     * batRecordState : {"id":"WOM_batMatState/waitPut","value":"待投料"}
     * batchDate : 1586570967000
     * cid : 1000
     * createStaff : null
     * createTime : null
     * headId : {"batchSite":{"id":"RM_batchSite/putinContainer","value":"配放于容器"},"batchType":{"id":"RM_batchType/sceneBat","value":"现场配"},"id":1215,"taskId":{"id":1291,"produceBatchNum":"202004081328","tableNo":"produceTask_20200408_003"}}
     * id : 1234
     * materialBatchNum : 1111111111
     * materialId : {"code":"P092903","id":1002,"isBatch":{"id":"BaseSet_isBatch/batch","value":"按批"},"mainUnit":{"id":1003,"name":"1"},"name":"P092903"}
     * offerNum : 100
     * offerSystem : {"id":"RM_exeSystem/mes","value":"MES系统"}
     * printCount : null
     * putinNum : null
     * receiveDate : null
     * receiveState : null
     * reciveNum : null
     * reciveStaff : {"name":null}
     * retirementState : null
     * returnNum : null
     * storeId : {"code":null,"id":null,"name":null}
     * tableInfoId : null
     * valid : true
     * version : 3
     * wareId : {"code":"factory03","id":1002,"name":"原料仓03","storesetState":false}
     */

    private Object attrMap;
    private SystemCodeEntity batRecordState; // 配料记录状态
    private Long batchDate; // 配料时间
    private Long cid;
    private StaffEntity createStaff;
    private Long createTime;
    private BatchMaterilEntity headId; // 配料指令Id
    private Long id;
    private String materialBatchNum; // 物料批号
    private MaterialEntity materialId; // 物料
    private BigDecimal offerNum; // 配料数量
    private SystemCodeEntity offerSystem; // 配料系统
    private int printCount; // 打印次数
    private BigDecimal putinNum; // 已投数量
    private Long receiveDate; // 签收时间
    private SystemCodeEntity receiveState; // 签收状态
    @SerializedName("reciveNum")
    private BigDecimal receiveNum; // 复秤数量
    @SerializedName("reciveStaff")
    private StaffEntity receiveStaff; // 签收人
    private SystemCodeEntity rejectReason;// 拒签原因;
    private SystemCodeEntity retirementState; // 退废状态
    private BigDecimal returnNum; // 已退数量
    private StoreSetEntity storeId; // 出库货位
    private TaskActiveEntity taskActive; // 配料活动
    private ProduceTaskEntity taskId; // 制造指令单
    private TaskProcessEntity taskProcId; // 指令单工序
    private Long tableInfoId;
    private Boolean valid;
//    private int version;
    private WarehouseEntity wareId; // 出库仓库
    private StaffEntity exeStaff; // 配料操作人

    @Expose(serialize = false,deserialize = false)
    private boolean checked; // 是否选中(参照页面使用)

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public SystemCodeEntity getBatRecordState() {
        return batRecordState;
    }

    public void setBatRecordState(SystemCodeEntity batRecordState) {
        this.batRecordState = batRecordState;
    }

    public Long getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(Long batchDate) {
        this.batchDate = batchDate;
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

    public BatchMaterilEntity getHeadId() {
        return headId;
    }

    public void setHeadId(BatchMaterilEntity headId) {
        this.headId = headId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getOfferNum() {
        return offerNum;
    }

    public void setOfferNum(BigDecimal offerNum) {
        this.offerNum = offerNum;
    }

    public SystemCodeEntity getOfferSystem() {
        return offerSystem;
    }

    public void setOfferSystem(SystemCodeEntity offerSystem) {
        this.offerSystem = offerSystem;
    }

    public int getPrintCount() {
        return printCount;
    }

    public void setPrintCount(int printCount) {
        this.printCount = printCount;
    }

    public BigDecimal getPutinNum() {
        return putinNum;
    }

    public void setPutinNum(BigDecimal putinNum) {
        this.putinNum = putinNum;
    }

    public Long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Long receiveDate) {
        this.receiveDate = receiveDate;
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

    public StaffEntity getReceiveStaff() {
        return receiveStaff;
    }

    public void setReceiveStaff(StaffEntity receiveStaff) {
        this.receiveStaff = receiveStaff;
    }

    public SystemCodeEntity getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(SystemCodeEntity rejectReason) {
        this.rejectReason = rejectReason;
    }

    public SystemCodeEntity getRetirementState() {
        return retirementState;
    }

    public void setRetirementState(SystemCodeEntity retirementState) {
        this.retirementState = retirementState;
    }

    public StaffEntity getExeStaff() {
        return exeStaff;
    }

    public void setExeStaff(StaffEntity exeStaff) {
        this.exeStaff = exeStaff;
    }

    public BigDecimal getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(BigDecimal returnNum) {
        this.returnNum = returnNum;
    }

    public StoreSetEntity getStoreId() {
        return storeId;
    }

    public void setStoreId(StoreSetEntity storeId) {
        this.storeId = storeId;
    }

    public TaskActiveEntity getTaskActive() {
        return taskActive;
    }

    public void setTaskActive(TaskActiveEntity taskActive) {
        this.taskActive = taskActive;
    }

    public ProduceTaskEntity getTaskId() {
        return taskId;
    }

    public void setTaskId(ProduceTaskEntity taskId) {
        this.taskId = taskId;
    }

    public TaskProcessEntity getTaskProcId() {
        return taskProcId;
    }

    public void setTaskProcId(TaskProcessEntity taskProcId) {
        this.taskProcId = taskProcId;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }

    public WarehouseEntity getWareId() {
        return wareId;
    }

    public void setWareId(WarehouseEntity wareId) {
        this.wareId = wareId;
    }
}
