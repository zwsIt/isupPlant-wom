package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

import java.math.BigDecimal;

/**
 * Created by wangshizhan on 2020/6/30
 * Email:wangshizhan@supcom.com
 * 备料记录数据提交对象
 */
public class PreMaterialSubmitEntity extends BaseEntity {

    public Long id;
    public String remark;
    public Long receiveDate;
    public SystemCodeUploadEntity receiveState;
    public SystemCodeUploadEntity rejectReason;
    public BigDecimal receiveNum;
    public StaffEntity receiveStaff;

}
