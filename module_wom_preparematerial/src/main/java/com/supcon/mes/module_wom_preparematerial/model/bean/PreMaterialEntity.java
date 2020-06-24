package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

import java.math.BigDecimal;

import retrofit2.http.PUT;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/23
 * Email zhangwenshuai1@supcon.com
 * Desc 备料接收实体
 */
public class PreMaterialEntity extends BaseEntity {

    /**
     * attrMap: null
     * cid: 1000
     * createStaff: null
     * createTime: null
     * deliverCode: "cs"
     * deliverDate: 1592534431200
     * deliverStaff: {id: 1007, name: "cuixin"}
     * fromStore: {attrMap: null, id: 1001, name: "原料仓01货位02"}
     * fromWare: {attrMap: null, id: 1001, name: "原料仓02"}
     * id: 1022
     * materialBatchNum: "A2"
     * materialId: {attrMap: null, code: "A", id: 1003, name: "A"}
     * preNum: 30
     * preOrderId: {attrMap: null, factoryId: {attrMap: null, id: 1002, name: "16层"}, id: 1118,…}
     * prePareDate: 1592477254298
     * preStaff: {id: 1007, name: "cuixin"}
     * receiveDate: null
     * recordState: {id: "WOM_prePareState/waitCollecte", value: "待收"}
     * tableInfoId: null
     * toStoreId: {attrMap: null, code: "factory0100002", id: 1001, name: "原料仓01货位02"}
     * toWareId: {attrMap: null, code: "factory01", id: 1000, name: "原料仓01", storesetState: true}
     * valid: true
     * version: 6
     */

    public Long id;
    public Object attrMap;
    public Long cid;
    public String  deliverCode;
    public Long deliverDate;
    public StaffEntity deliverStaff;

    public ObjectEntity fromStore;
    public ObjectEntity fromWare;
    public String materialBatchNum;
    public Float preNum;
    public PreOrderEntity preOrderId;
    public Long prePareDate;
    public StaffEntity preStaff;
    public ObjectEntity materialId;
    public Long receiveDate;
    public SystemCodeEntity recordState;
    public ObjectEntity toStoreId;
    public ObjectEntity toWareId;
}
