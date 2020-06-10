package com.supcon.mes.module_wom_rejectmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.ValueEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PrepareMaterialEntity;

/**
 * Author by fengjun1,
 * Date on 2020/6/4.
 * 退料记录实体
 */
public class RejectRecordMaterialEntity extends BaseEntity {
    public Object attrMap;
    public Long cid;
    public StaffEntity createStaff;
    public Long createTime;
    public Long id;


    public PrepareMaterialEntity headId; // 表头
    public String materialBatchNum;
    public ObjectEntity materialId;
    public ObjectEntity outStoreId;
    public ObjectEntity outWareId;
    public ObjectEntity storeId;
    public ObjectEntity wareId;
    public ValueEntity rejectReason;
    public Long rejectNum;
    public Long tableInfoId;
    public String remark;

    public int version;
    public boolean valid;

}
