package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 用料清单
 */
public class PutInDetailEntity extends BaseEntity {

    /**
     * headId : {"id":1939}
     * id : 1211
     * materialBatchNum : 123444444
     * materialId : {"code":"Mat001","id":1008,"isBatch":{"id":"BaseSet_isBatch/batch","value":"按批"},"mainUnit":{"name":"1"},"name":"原料001"}
     * putinEndTime : null
     * putinNum : 4
     * putinTime : 1586433529000
     * remark : null
     * sort : 0
     * storeId : {"code":"001","id":1003,"name":"工厂2货位1"}
     * useNum : 4
     * version : 0
     * wareId : {"code":"factory02","id":1001,"name":"原料仓02","storesetState":true}
     */

    private ProcReportEntity headId;    // 工序报工Id
    private Long id;
    private String materialBatchNum;    // 物料批号
    private MaterialEntity materialId;  // 物料
    private Long putinEndTime;          // 投料结束时间
    private BigDecimal putinNum;        // 用料量
    private Long putinTime;             // 投料时间
    private String remark;
    private int sort;
    private StoreSetEntity storeId;     // 货位
    private BigDecimal useNum;          // 物料使用量
    private WarehouseEntity wareId;     // 仓库
    private Long batchingRecordId;      // 配料记录Id
    private BigDecimal availableNum;    // 可用量
    private SystemCodeEntity taskType;  // 工单类型
    private BigDecimal beforeNum;       // 前重
    private BigDecimal afterNum;        // 后重
    private BigDecimal reveiveNum;      // 签收数量
    private String beforeWeight;//前重
    private String afterWeight;//后重
    private boolean isAllPutInto=true;//是否投完 默认为是

    public Long getBatchingRecordId() {
        return batchingRecordId;
    }

    public void setBatchingRecordId(Long batchingRecordId) {
        this.batchingRecordId = batchingRecordId;
    }

    public BigDecimal getAvailableNum() {
        return availableNum!=null?availableNum.setScale(WomConstant.scale):null;
    }

    public void setAvailableNum(BigDecimal availableNum) {
        this.availableNum = availableNum;
    }

    public SystemCodeEntity getTaskType() {
        return taskType;
    }

    public void setTaskType(SystemCodeEntity taskType) {
        this.taskType = taskType;
    }

    public BigDecimal getBeforeNum() {
        return beforeNum!=null?beforeNum.setScale(WomConstant.scale):null;
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

    public BigDecimal getReveiveNum() {
        return reveiveNum!=null?reveiveNum.setScale(WomConstant.scale):null;
    }

    public void setReveiveNum(BigDecimal reveiveNum) {
        this.reveiveNum = reveiveNum;
    }

    public ProcReportEntity getHeadId() {
        return headId;
    }

    public void setHeadId(ProcReportEntity headId) {
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
        if (materialId == null){
            materialId = new MaterialEntity();
        }
        return materialId;
    }

    public void setMaterialId(MaterialEntity materialId) {
        this.materialId = materialId;
    }

    public Long getPutinEndTime() {
        return putinEndTime;
    }

    public void setPutinEndTime(Long putinEndTime) {
        this.putinEndTime = putinEndTime;
    }

    public BigDecimal getPutinNum() {
        return putinNum!=null?putinNum.setScale(WomConstant.scale):null;
    }

    public void setPutinNum(BigDecimal putinNum) {
        this.putinNum = putinNum;
    }

    public Long getPutinTime() {
        return putinTime;
    }

    public void setPutinTime(Long putinTime) {
        this.putinTime = putinTime;
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

    public StoreSetEntity getStoreId() {
        return storeId;
    }

    public void setStoreId(StoreSetEntity storeId) {
        this.storeId = storeId;
    }

    public BigDecimal getUseNum() {
        return useNum;
    }

    public void setUseNum(BigDecimal useNum) {
        this.useNum = useNum;
    }

    public WarehouseEntity getWareId() {
        return wareId;
    }

    public void setWareId(WarehouseEntity wareId) {
        this.wareId = wareId;
    }

    public String getBeforeWeight() {
        return beforeWeight;
    }

    public void setBeforeWeight(String beforeWeight) {
        this.beforeWeight = beforeWeight;
    }

    public String getAfterWeight() {
        return afterWeight;
    }

    public void setAfterWeight(String afterWeight) {
        this.afterWeight = afterWeight;
    }

    public boolean isAllPutInto() {
        return isAllPutInto;
    }

    public void setAllPutInto(boolean allPutInto) {
        isAllPutInto = allPutInto;
    }
}
