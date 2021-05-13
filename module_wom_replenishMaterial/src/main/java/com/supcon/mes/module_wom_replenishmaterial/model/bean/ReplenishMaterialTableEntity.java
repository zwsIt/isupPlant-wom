package com.supcon.mes.module_wom_replenishmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

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
    private Long id;
    /**
     * 物料
     */
    private Good material;
    /**
     * 实际数量
     */
    private BigDecimal actualNumber;
    /**
     * 单据编号
     */
    private String code;
    /**
     * 节点设备
     */
    private AssociatedEquipmentEntity equipment;
    /**
     * 状态
     */
    private SystemCodeEntity noticeState;
    /**
     * 计划数量
     */
    private BigDecimal planNumber;
    /**
     * 创建时间
     */
    private Long createTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
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

    public BigDecimal getActualNumber() {
        return actualNumber;
    }

    public void setActualNumber(BigDecimal actualNumber) {
        this.actualNumber = actualNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AssociatedEquipmentEntity getEquipment() {
        return equipment;
    }

    public void setEquipment(AssociatedEquipmentEntity equipment) {
        this.equipment = equipment;
    }

    public SystemCodeEntity getNoticeState() {
        return noticeState;
    }

    public void setNoticeState(SystemCodeEntity noticeState) {
        this.noticeState = noticeState;
    }

    public BigDecimal getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(BigDecimal planNumber) {
        this.planNumber = planNumber;
    }
}
