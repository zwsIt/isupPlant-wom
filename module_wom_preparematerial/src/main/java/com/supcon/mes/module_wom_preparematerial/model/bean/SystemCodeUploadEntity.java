package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2020/4/11
 * Email:wangshizhan@supcom.com
 */
class SystemCodeUploadEntity extends BaseEntity {

    public String id;
    public String value;

    public SystemCodeUploadEntity(String id){
        this.id = id;
    }

}
