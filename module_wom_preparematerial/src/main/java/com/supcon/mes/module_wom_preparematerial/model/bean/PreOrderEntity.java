package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 * 备料指令单实体
 */
public class PreOrderEntity extends BaseEntity {

    /**
     * attrMap: null
     * factoryId: {attrMap: null, id: 1002, name: "16层"}
     * id: 1118
     * orderTableNo: "prePraOrder_2020_06_18_054"
     * preDepartMent: {attrMap: null, id: 1000, name: "MES开发一部"}
     * prePareMode: {id: "WOM_preMode/factoryMaterial", value: "车间物料"}
     * preSystem: {attrMap: null, code: "serviceUrl/001", id: 1001}
     */

    public ObjectEntity factoryId;
    public Long id;
    public String orderTableNo;
    public DepartmentEntity preDepartMent;
    public SystemCodeEntity prePareMode;
    public ObjectEntity preSystem;

}
