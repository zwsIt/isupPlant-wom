package com.supcon.mes.module_wom_preparematerial.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/23
 * Email zhangwenshuai1@supcon.com
 * Desc 备料指令实体
 */
public class PrepareMaterialEntity extends BaseEntity {

    /**
     * attrMap : null
     * cid : 1000
     * createStaff : {"id":1007,"name":"cuixin"}
     * createTime : null
     * doneNumSum : 5
     * id : 1219
     * needDate : 1587461637000
     * needNum : 10
     * orderState : {"id":"WOM_prePareState/complete","value":"已完成"}
     * pending : {"activityName":null,"activityType":null,"bulkDealFlag":null,"deploymentId":null,"id":null,"openUrl":null,"processDescription":null,"processId":null,"processKey":null,"processVersion":null,"taskDescription":"生效","userId":null}
     * prePareDate : 1587461523094
     * prePareNeedPartId : {"headId":{"id":1165,"tableNo":"prepareMaterialNeed_20200421_009"}}
     * prePareStaff : {"name":"cuixin"}
     * productId : {"code":"A","id":1003,"name":"A"}
     * remark : null
     * returnNum : null
     * status : 99
     * tableInfoId : 2967
     * tableNo : prepareMaterial_20200421_013
     * valid : true
     * version : 5
     */

    private Object attrMap;
    private Long cid;
    private StaffEntity createStaff;
    private Object createTime;
    private BigDecimal doneNumSum; // 备料总量
    private Long id;
    private Long needDate;    // 需备时间
    private BigDecimal needNum; // 需备数量
    private SystemCodeEntity orderState; // 备料指令状态
    private PendingEntity pending;
    private Long prePareDate; // 备料时间
    private PrePareNeedPartIdEntity prePareNeedPartId; // 备料需求明细id
    private StaffEntity prePareStaff; // 备料指令创建人
    private StaffEntity rejectApplyStaff;// 退料申请人
    private MaterialEntity productId; // 物料
    private String remark;
    private BigDecimal returnNum; // 已退数量
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

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getDoneNumSum() {
        return doneNumSum;
    }

    public void setDoneNumSum(BigDecimal doneNumSum) {
        this.doneNumSum = doneNumSum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNeedDate() {
        return needDate;
    }

    public void setNeedDate(Long needDate) {
        this.needDate = needDate;
    }

    public BigDecimal getNeedNum() {
        return needNum;
    }

    public void setNeedNum(BigDecimal needNum) {
        this.needNum = needNum;
    }

    public SystemCodeEntity getOrderState() {
        return orderState;
    }

    public void setOrderState(SystemCodeEntity orderState) {
        this.orderState = orderState;
    }

    public PendingEntity getPending() {
        return pending;
    }

    public void setPending(PendingEntity pending) {
        this.pending = pending;
    }

    public Long getPrePareDate() {
        return prePareDate;
    }

    public void setPrePareDate(Long prePareDate) {
        this.prePareDate = prePareDate;
    }

    public PrePareNeedPartIdEntity getPrePareNeedPartId() {
        return prePareNeedPartId;
    }

    public void setPrePareNeedPartId(PrePareNeedPartIdEntity prePareNeedPartId) {
        this.prePareNeedPartId = prePareNeedPartId;
    }

    public StaffEntity getPrePareStaff() {
        return prePareStaff;
    }

    public void setPrePareStaff(StaffEntity prePareStaff) {
        this.prePareStaff = prePareStaff;
    }

    public MaterialEntity getProductId() {
        return productId;
    }

    public void setProductId(MaterialEntity productId) {
        this.productId = productId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(BigDecimal returnNum) {
        this.returnNum = returnNum;
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

    public StaffEntity getRejectApplyStaff() {
        return rejectApplyStaff;
    }

    public void setRejectApplyStaff(StaffEntity rejectApplyStaff) {
        this.rejectApplyStaff = rejectApplyStaff;
    }

    public static class PrePareNeedPartIdEntity extends BaseEntity {
        /**
         * headId : {"id":1165,"tableNo":"prepareMaterialNeed_20200421_009"}
         */

        private HeadIdEntity headId;

        public HeadIdEntity getHeadId() {
            return headId;
        }

        public void setHeadId(HeadIdEntity headId) {
            this.headId = headId;
        }

        public static class HeadIdEntity extends BaseEntity {
            /**
             * id : 1165
             * tableNo : prepareMaterialNeed_20200421_009
             */

            private Long id;
            private String tableNo;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getTableNo() {
                return tableNo;
            }

            public void setTableNo(String tableNo) {
                this.tableNo = tableNo;
            }
        }
    }
}
