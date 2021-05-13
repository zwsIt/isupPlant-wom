package com.supcon.mes.module_wom_replenishmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/11
 * @email zhangwenshuai1@supcon.com
 * Desc 关联设备（层次模型---工厂架构）
 */
public class AssociatedEquipmentEntity extends BaseEntity {
    private Long id;
    private String name;
    private String code;
    /**
     * 进料方式
     */
    private SystemCodeEntity feedStockType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SystemCodeEntity getFeedStockType() {
        return feedStockType;
    }

    public void setFeedStockType(SystemCodeEntity feedStockType) {
        this.feedStockType = feedStockType;
    }
}
