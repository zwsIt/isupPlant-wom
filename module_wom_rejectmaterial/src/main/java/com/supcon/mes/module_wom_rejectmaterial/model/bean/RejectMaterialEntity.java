package com.supcon.mes.module_wom_rejectmaterial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 退料单实体
 */
public class RejectMaterialEntity extends BaseEntity {

    /**
     * attrMap : null
     * cid : 1000
     * createStaff : null
     * createTime : null
     * id : 1021
     * pending : {"activityName":"TaskEvent_17rdgt4","activityType":"task","bulkDealFlag":null,"deploymentId":1023,"id":21281,"openUrl":"/msService/WOM/rejectMaterilal/rejectMaterial/prePareRejectEdit","processDescription":"备料退料","processId":"prePareRejectFlow-1","processKey":"prePareRejectFlow","processVersion":1,"taskDescription":"编辑","userId":1009}
     * rejectApplyDate : 1587449535000
     * rejectApplyStaff : {"id":1009,"name":"陈豪"}
     * rejectType : {"id":"WOM_rejectType/forPrepare","value":"备料退料"}
     * remark : null
     * status : 88
     * tableInfoId : 2951
     * tableNo : rejectMaterilal_20200421_002
     * valid : true
     * version : 1
     */

    private Object attrMap;
    private Long cid;
    private StaffEntity createStaff;
    private Long createTime;
    private Long id;
    private PendingEntity pending;
    private Long rejectApplyDate;
    private StaffEntity rejectApplyStaff;
    private SystemCodeEntity rejectType;
    private String remark;
    private int status;
    private Long tableInfoId;
    private String tableNo;
    private boolean valid;
    private int version;

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
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

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public Long getRejectApplyDate() {
        return rejectApplyDate;
    }

    public void setRejectApplyDate(Long rejectApplyDate) {
        this.rejectApplyDate = rejectApplyDate;
    }

    public StaffEntity getRejectApplyStaff() {
        return rejectApplyStaff;
    }

    public void setRejectApplyStaff(StaffEntity rejectApplyStaff) {
        this.rejectApplyStaff = rejectApplyStaff;
    }

    public SystemCodeEntity getRejectType() {
        if (rejectType == null){
            rejectType = new SystemCodeEntity();
        }
        return rejectType;
    }

    public void setRejectType(SystemCodeEntity rejectType) {
        this.rejectType = rejectType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
