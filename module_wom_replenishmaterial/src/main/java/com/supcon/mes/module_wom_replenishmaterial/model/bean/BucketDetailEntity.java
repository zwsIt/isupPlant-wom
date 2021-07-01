package com.supcon.mes.module_wom_replenishmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * @author Created by zhangwenshuai1
 * @date on 2021/5/11
 * @email zhangwenshuai1@supcon.com
 * Desc 容器明细
 */
public class BucketDetailEntity extends BaseEntity {
    private Long id;
    private String batch;
    /**
     * 数量
     */
    private BigDecimal materilNumber;
    /**
     * 物料
     */
    private Good material;
    /**
     * 入库时间
     */
    private Long storageTime;
    /**
     * 容器
     */
    private ObjectEntity vessel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public BigDecimal getMaterilNumber() {
        return materilNumber;
    }

    public void setMaterilNumber(BigDecimal materilNumber) {
        this.materilNumber = materilNumber;
    }

    public Good getMaterial() {
        return material;
    }

    public void setMaterial(Good material) {
        this.material = material;
    }

    public Long getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Long storageTime) {
        this.storageTime = storageTime;
    }

    public ObjectEntity getVessel() {
        return vessel;
    }

    public void setVessel(ObjectEntity vessel) {
        this.vessel = vessel;
    }
}
