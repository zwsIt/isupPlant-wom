package com.supcon.mes.module_wom_replenishmaterial.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/13
 * @email zhangwenshuai1@supcon.com
 * Desc 补料单明细
 */
public class ReplenishMaterialTablePartEntity extends BaseEntity {

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
     * 补料单
     */
    @Expose(serialize = false)
    private ReplenishMaterialTableEntity fmBill;
    /**
     * 数量
     */
    @Expose
    private BigDecimal fmNumber;
    /**
     * 补料时间
     */
    @Expose
    private Long fmTime;
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

    /**
     *
     * 扫描标识
     */
    @Expose
    private Boolean scanFlag;

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

    public ReplenishMaterialTableEntity getFmBill() {
        return fmBill;
    }

    public void setFmBill(ReplenishMaterialTableEntity fmBill) {
        this.fmBill = fmBill;
    }

    public BigDecimal getFmNumber() {
        return fmNumber;
    }

    public void setFmNumber(BigDecimal fmNumber) {
        this.fmNumber = fmNumber;
    }

    public Long getFmTime() {
        return fmTime;
    }

    public void setFmTime(Long fmTime) {
        this.fmTime = fmTime;
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

    public Boolean getScanFlag() {
        return scanFlag;
    }

    public void setScanFlag(Boolean scanFlag) {
        this.scanFlag = scanFlag;
    }
}
