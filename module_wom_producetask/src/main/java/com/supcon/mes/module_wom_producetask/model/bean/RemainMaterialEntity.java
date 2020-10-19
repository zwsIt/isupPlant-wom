package com.supcon.mes.module_wom_producetask.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/10/15
 * Email zhangwenshuai1@supcon.com
 * Desc 尾料记录实体
 */
public class RemainMaterialEntity extends BaseEntity {

    /**
     * attrMap : null
     * batchText : 10141433
     * cid : 1000
     * createStaff : null
     * createTime : null
     * id : 1063
     * initNum : 4.9
     * material : {"attrMap":null,"code":"c2oh4","id":1025,"mainUnit":{"attrMap":null,"id":1006,"name":"吨"},"name":"甲醇","produceUnit":{"attrMap":null,"id":1006,"name":"吨"},"specifications":"20kg/桶"}
     * remainNum : 1
     * remainType : {"id":"WOM_remainType/outPutRemain","value":"产出尾料"}
     * remark : null
     * status : null
     * storeId : {"attrMap":null,"code":null,"id":null,"name":null}
     * tableInfoId : null
     * tableNo : null
     * totalUseNum : 3.9
     * valid : true
     * version : 1
     * wareId : {"attrMap":null,"code":"factory03","id":1002,"name":"原料仓03","storesetState":false}
     * workAreaId : {"attrMap":null,"id":1002,"name":"16层"}
     */

    private Map attrMap;
    private String batchText; // 物料批号
    private Long cid;
    private StaffEntity createStaff;
    private Long createTime;
    private Long id;
    private BigDecimal initNum;  // 初始数量
    private MaterialEntity material;
    private BigDecimal remainNum; // 尾料可用数量
    private SystemCodeEntity remainType; // 来源
    private String remark;
    private Long status;
    private StoreSetEntity storeId; // 货位
    private Long tableInfoId;
    private String tableNo;
    private BigDecimal totalUseNum; // 累计使用量
    private boolean valid;
    private int version;
    private WarehouseEntity wareId; // 仓库
    private FactoryModelEntity workAreaId; // 生产区域

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
        this.attrMap = attrMap;
    }

    public String getBatchText() {
        return batchText;
    }

    public void setBatchText(String batchText) {
        this.batchText = batchText;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInitNum() {
        return initNum;
    }

    public void setInitNum(BigDecimal initNum) {
        this.initNum = initNum;
    }

    public MaterialEntity getMaterial() {
        return material;
    }

    public void setMaterial(MaterialEntity material) {
        this.material = material;
    }

    public BigDecimal getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(BigDecimal remainNum) {
        this.remainNum = remainNum;
    }

    public SystemCodeEntity getRemainType() {
        return remainType;
    }

    public void setRemainType(SystemCodeEntity remainType) {
        this.remainType = remainType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public StoreSetEntity getStoreId() {
        return storeId;
    }

    public void setStoreId(StoreSetEntity storeId) {
        this.storeId = storeId;
    }

    public Long getTableInfoId() {
        return tableInfoId;
    }

    public void setTableInfoId(Long tableInfoId) {
        this.tableInfoId = tableInfoId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public BigDecimal getTotalUseNum() {
        return totalUseNum;
    }

    public void setTotalUseNum(BigDecimal totalUseNum) {
        this.totalUseNum = totalUseNum;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public WarehouseEntity getWareId() {
        return wareId;
    }

    public void setWareId(WarehouseEntity wareId) {
        this.wareId = wareId;
    }

    public FactoryModelEntity getWorkAreaId() {
        return workAreaId;
    }

    public void setWorkAreaId(FactoryModelEntity workAreaId) {
        this.workAreaId = workAreaId;
    }
}
