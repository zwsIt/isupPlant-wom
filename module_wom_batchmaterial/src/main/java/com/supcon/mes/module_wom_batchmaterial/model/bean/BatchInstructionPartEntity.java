package com.supcon.mes.module_wom_batchmaterial.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/13
 * @email zhangwenshuai1@supcon.com
 * Desc 配料指令明细
 */
public class BatchInstructionPartEntity extends BaseEntity {

    /**
     * attrMap
     */
    @Expose
    private Object attrMap;
    /**
     * 批次
     */
    @Expose
    private String batch;
    /**
     * 配料指令
     */
    @Expose(serialize = false)
    private BatchInstructionEntity batchInstructionEntity;
    /**
     * 配料数量
     */
    @Expose
    private BigDecimal bmNumber;
    /**
     * 配料时间
     */
    @Expose
    private Long bmTime;
    /**
     * id
     */
    @Expose
    private Long id;
    /**
     * remark
     */
    @Expose
    private String remark;
    /**
     * sort
     */
    @Expose
    private int sort;
    /**
     * version
     */
    @Expose
    private int version;

    public BatchInstructionEntity getBatchInstructionEntity() {
        return batchInstructionEntity;
    }

    public void setBatchInstructionEntity(BatchInstructionEntity batchInstructionEntity) {
        this.batchInstructionEntity = batchInstructionEntity;
    }

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public BigDecimal getBmNumber() {
        return bmNumber;
    }

    public void setBmNumber(BigDecimal bmNumber) {
        this.bmNumber = bmNumber;
    }

    public Long getBmTime() {
        return bmTime;
    }

    public void setBmTime(Long bmTime) {
        this.bmTime = bmTime;
    }

    public BigDecimal getFmNumber() {
        return bmNumber;
    }

    public void setFmNumber(BigDecimal fmNumber) {
        this.bmNumber = bmNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
