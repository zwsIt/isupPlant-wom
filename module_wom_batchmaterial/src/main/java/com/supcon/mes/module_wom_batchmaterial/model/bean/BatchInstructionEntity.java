package com.supcon.mes.module_wom_batchmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.wom.BatchAreaEntity;
import com.supcon.mes.middleware.model.bean.wom.BatchLineEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

import java.math.BigDecimal;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/21
 * @email zhangwenshuai1@supcon.com
 * Desc 配料指令实体
 */
public class BatchInstructionEntity extends BaseEntity {

    /**
     * 实际配料数量
     */
    private BigDecimal actualNumber;
    /**
     * 配料区
     */
    private BatchAreaEntity areaMange;
    /**
     * attrMap
     */
    private Object attrMap;
    /**
     * 配料指令集
     */
    private BatchMaterialSetEntity bmSet;
    /**
     * 配料线
     */
    private BatchLineEntity burendManage;
    /**
     * completeTime
     */
    private Long completeTime;
    /**
     * id
     */
    private Long id;
    /**
     * material
     */
    private MaterialEntity material;
    /**
     * 配料顺序
     */
    private Integer plOrder;
    /**
     * 需求数量
     */
    private BigDecimal planNumber;
    /**
     * remark
     */
    private String remark;
    /**
     * sort
     */
    private Object sort;
    /**
     * version
     */
    private Integer version;

    public BigDecimal getActualNumber() {
        return actualNumber;
    }

    public void setActualNumber(BigDecimal actualNumber) {
        this.actualNumber = actualNumber;
    }

    public BatchAreaEntity getAreaMange() {
        return areaMange;
    }

    public void setAreaMange(BatchAreaEntity areaMange) {
        this.areaMange = areaMange;
    }

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public BatchMaterialSetEntity getBmSet() {
        return bmSet;
    }

    public void setBmSet(BatchMaterialSetEntity bmSet) {
        this.bmSet = bmSet;
    }

    public BatchLineEntity getBurendManage() {
        return burendManage;
    }

    public void setBurendManage(BatchLineEntity burendManage) {
        this.burendManage = burendManage;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaterialEntity getMaterial() {
        return material;
    }

    public void setMaterial(MaterialEntity material) {
        this.material = material;
    }

    public Integer getPlOrder() {
        return plOrder;
    }

    public void setPlOrder(Integer plOrder) {
        this.plOrder = plOrder;
    }

    public BigDecimal getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(BigDecimal planNumber) {
        this.planNumber = planNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
