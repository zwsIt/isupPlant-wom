package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.bean.wom.FormulaEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/24
 * Email zhangwenshuai1@supcon.com
 * Desc 产出清单
 */
public class OutputDetailEntity extends BaseEntity {

    /**
     * id : 1255
     * materialBatchNum : produceTask_20200323_003
     */

    private ProcReportEntity headId;    // 工序报工Id
    private Long id;
    private String materialBatchNum;    // 物料批号
    private SystemCodeEntity taskType;  // 工单类型
    private MaterialEntity product;     // 产品
    private StoreSetEntity storeId;     // 货位
    private WarehouseEntity wareId;     // 仓库
    private BigDecimal reportNum;       // 报工数量
    private BigDecimal outputNum;       // 产出数量
    private BigDecimal afterNum;        // 后重
    private BigDecimal beforeNum;       // 前重
    private Long putinEndTime;          // 投料结束时间
    private Long putinTime;             // 报工时间
    private BigDecimal remainNum;       // 尾料数量
    private SystemCodeEntity remainOperate; // 尾料处理

    public SystemCodeEntity getRemainOperate() {
        return remainOperate;
    }

    public void setRemainOperate(SystemCodeEntity remainOperate) {
        this.remainOperate = remainOperate;
    }

    public BigDecimal getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(BigDecimal remainNum) {
        this.remainNum = remainNum;
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

    public SystemCodeEntity getTaskType() {
        return taskType;
    }

    public void setTaskType(SystemCodeEntity taskType) {
        this.taskType = taskType;
    }

    public MaterialEntity getProduct() {
        return product;
    }

    public void setProduct(MaterialEntity product) {
        this.product = product;
    }

    public StoreSetEntity getStoreId() {
        return storeId;
    }

    public void setStoreId(StoreSetEntity storeId) {
        this.storeId = storeId;
    }

    public WarehouseEntity getWareId() {
        return wareId;
    }

    public void setWareId(WarehouseEntity wareId) {
        this.wareId = wareId;
    }

    public BigDecimal getReportNum() {
        return reportNum;
    }

    public void setReportNum(BigDecimal reportNum) {
        this.reportNum = reportNum;
    }

    public BigDecimal getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(BigDecimal outputNum) {
        this.outputNum = outputNum;
    }

    public BigDecimal getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(BigDecimal afterNum) {
        this.afterNum = afterNum;
    }

    public BigDecimal getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(BigDecimal beforeNum) {
        this.beforeNum = beforeNum;
    }

    public Long getPutinEndTime() {
        return putinEndTime;
    }

    public void setPutinEndTime(Long putinEndTime) {
        this.putinEndTime = putinEndTime;
    }

    public Long getPutinTime() {
        return putinTime;
    }

    public void setPutinTime(Long putinTime) {
        this.putinTime = putinTime;
    }
}
