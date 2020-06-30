package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * Created by wangshizhan on 2020/6/30
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialSubmitEntity extends BaseEntity {

    public Long id;
    public String remark;
    public Long receiveDate;
    public SystemCodeUploadEntity receiveState;
    public SystemCodeUploadEntity receiveReason;
    public Float receiveNum;
    public ObjectEntity receiveStaff;

}
