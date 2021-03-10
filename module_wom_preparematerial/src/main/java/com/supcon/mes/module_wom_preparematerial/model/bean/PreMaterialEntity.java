package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.google.gson.annotations.Expose;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;



/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/23
 * Email zhangwenshuai1@supcon.com
 * Desc 备料接收实体
 */
public class PreMaterialEntity extends BaseEntity {

    /**
     {
     "attrMap":null,
     "cid":1000,
     "createStaff":null,
     "createTime":null,
     "deliverCode":"0629084601",
     "deliverDate":1593391574262,
     "deliverStaff":{"id":1003,"name":"何轶超"},
     "fromStore":{"attrMap":null,"code":"001","id":1003,"name":"工厂2货位1"},
     "fromWare":{"attrMap":null,"code":"factory02","id":1001,"name":"原料仓02"},
     "id":1045,
     "materialBatchNum":"0629084401",
     "materialId":{"attrMap":null,"code":"A","id":1003,"name":"A"},
     "preNum":10,
     "preOrderId":{"attrMap":null,"factoryId":{"attrMap":null,"id":1002,"name":"16层"},"id":1209,"orderTableNo":"prePraOrder_2020_06_28_016",
     "preDepartMent":{"attrMap":null,"id":1000,"name":"MES开发一部"},
     "prePareMode":{"id":"WOM_preMode/factoryMaterial","value":"车间物料"},
     "prePareNeedPartId":{
     "attrMap":null,
     "headId":{"attrMap":null,"id":1335,"tableNo":"prepareMaterialNeed_20200628_009"}},
     "preSystem":{"attrMap":null,"code":"nulllabel","id":1002}},
     "preStaff":{"id":1003,"name":"何轶超"},
     "receiveDate":null,
     "receiveNum":null,
     "receiveStaff":{"id":null,"name":null},
     "recordState":{"id":"WOM_prePareState/waitCollecte","value":"待收"},
     "rejectReason":null,
     "remark":"0629084601",
     "retirementState":null,
     "tableInfoId":null,
     "toStoreId":{"attrMap":null,"code":"factory0100002","id":1001,"name":"原料仓01货位02"},
     "toWareId":{"attrMap":null,"code":"factory01","id":1000,"name":"原料仓01","storesetState":true,"warehouseState":{"id":"BaseSet_warehouseState/using","value":"启用"}},
     "valid":true,
     "version":2
     }
     */

    public Long id;
    public Object attrMap;
    public Long cid;
    public String  deliverCode;
    public Long deliverDate;
    public StaffEntity deliverStaff;

    public StoreSetEntity fromStore;  // 源货位
    public WarehouseEntity fromWare;   // 源仓库
    public String materialBatchNum;
    public Float preNum;
    public PreOrderEntity preOrderId;
    public Long prePareDate;
    public StaffEntity preStaff;
    public MaterialEntity materialId;

    public SystemCodeEntity recordState;
    public StoreSetEntity toStoreId; // 目的货位
    public WarehouseEntity toWareId; // 目的仓库
    public String remark;
    public Long receiveDate;
    public SystemCodeEntity receiveState; // 接收状态
    public SystemCodeEntity rejectReason; // 拒收原因
    public Float receiveNum; // 接收数量
    public ObjectEntity receiveStaff;

    public boolean isChecked;

    @Expose
    public StoreSetEntity toStoreIdInit; // 原始货位
}
