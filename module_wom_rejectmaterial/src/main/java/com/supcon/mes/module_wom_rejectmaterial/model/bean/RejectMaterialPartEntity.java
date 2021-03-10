package com.supcon.mes.module_wom_rejectmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PrepareMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.bean.ProduceTaskEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 退料记录明细实体
 */
public class RejectMaterialPartEntity extends BaseEntity {

    /**
     * batchingPartId : {"batRecordState":{"id":"WOM_batMatState/stop","value":"已中止"},"id":1440,"offerNum":15,"putinNum":null}
     * headId : {"id":1022}
     * id : 1041
     * materialBatchNum : 11
     * materialId : {"code":"P092903","id":1002,"name":"P092903"}
     * outStoreId : null
     * outWareId : {"code":"factory03","id":1002,"name":"原料仓03","storesetState":false}
     * preparePartId : null
     * rejectNum : 15
     * rejectReason : {"id":"WOM_rejectReason/less","value":"少发"}
     * remark : null
     * sort : 0
     * storeId : {"code":"001","id":1003,"name":"工厂2货位1"}
     * taskId : null
     * version : 1
     * wareId : {"code":"factory02","id":1001,"name":"原料仓02","storesetState":true}
     */

    private BatchMaterialPartEntity batchingPartId; // 配料记录id
    private RejectMaterialEntity headId;
    private Long id;
    private String materialBatchNum; // 物料批号
    private MaterialEntity materialId; // 物料
    private StoreSetEntity outStoreId;   // 出库货位
    private WarehouseEntity outWareId; // 出库仓库
    private PrepareMaterialPartEntity preparePartId;  // 备料记录id
    private BigDecimal rejectNum;  // 退料数量
    private SystemCodeEntity rejectReason; // 退料原因
    private String remark;
    private int sort;
    private StoreSetEntity storeId; // 入库货位
    private ProduceTaskEntity taskId; // 制造指令单
//    private int version;
    private WarehouseEntity wareId; // 入库仓库

    public BatchMaterialPartEntity getBatchingPartId() {
        return batchingPartId;
    }

    public void setBatchingPartId(BatchMaterialPartEntity batchingPartId) {
        this.batchingPartId = batchingPartId;
    }

    public RejectMaterialEntity getHeadId() {
        return headId;
    }

    public void setHeadId(RejectMaterialEntity headId) {
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

    public StoreSetEntity getOutStoreId() {
        return outStoreId;
    }

    public void setOutStoreId(StoreSetEntity outStoreId) {
        this.outStoreId = outStoreId;
    }

    public WarehouseEntity getOutWareId() {
        return outWareId;
    }

    public void setOutWareId(WarehouseEntity outWareId) {
        this.outWareId = outWareId;
    }

    public PrepareMaterialPartEntity getPreparePartId() {
        return preparePartId;
    }

    public void setPreparePartId(PrepareMaterialPartEntity preparePartId) {
        this.preparePartId = preparePartId;
    }

    public BigDecimal getRejectNum() {
        return rejectNum;
    }

    public void setRejectNum(BigDecimal rejectNum) {
        this.rejectNum = rejectNum;
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

    public WarehouseEntity getWareId() {
        return wareId;
    }

    public void setWareId(WarehouseEntity wareId) {
        this.wareId = wareId;
    }

    static class BatchingPartIdEntity extends BaseEntity {
        /**
         * batRecordState : {"id":"WOM_batMatState/stop","value":"已中止"}
         * id : 1440
         * offerNum : 15
         * putinNum : null
         */

        private SystemCodeEntity batRecordState;
        private Long id;
        private BigDecimal offerNum;
        private BigDecimal putinNum;

        public SystemCodeEntity getBatRecordState() {
            return batRecordState;
        }

        public void setBatRecordState(SystemCodeEntity batRecordState) {
            this.batRecordState = batRecordState;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public BigDecimal getOfferNum() {
            return offerNum;
        }

        public void setOfferNum(BigDecimal offerNum) {
            this.offerNum = offerNum;
        }

        public BigDecimal getPutinNum() {
            return putinNum;
        }

        public void setPutinNum(BigDecimal putinNum) {
            this.putinNum = putinNum;
        }
    }
}
