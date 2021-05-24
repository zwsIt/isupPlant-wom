package com.supcon.mes.module_wom_batchmaterial.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;

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
    private BatchInstructionEntity bmSetDetail;
    /**
     * 配料数量
     */
    @Expose
    private BigDecimal bmNumber;
    /**
     * 配料时间
     */
    @Expose
    private Long operatorTime;
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
     * operator
     */
    @Expose
    private StaffEntity operator;

    public BatchInstructionEntity getBmSetDetail() {
        return bmSetDetail;
    }

    public void setBmSetDetail(BatchInstructionEntity bmSetDetail) {
        this.bmSetDetail = bmSetDetail;
    }

    public Long getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Long operatorTime) {
        this.operatorTime = operatorTime;
    }

    public StaffEntity getOperator() {
        return operator;
    }

    public void setOperator(StaffEntity operator) {
        this.operator = operator;
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
