package com.supcon.mes.module_wom_producetask.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.DepartmentEntity;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;

import java.math.BigDecimal;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/11
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令实体
 */
@Deprecated
public class BatchMaterilEntity extends BaseEntity {

    /**
     * attrMap : null
     * batchSite : {"id":"RM_batchSite/putinContainer","value":"配放于容器"}
     * batchType : {"id":"RM_batchType/sceneBat","value":"现场配"}
     * cid : 1000
     * createStaff : null
     * createTime : 1586522292069
     * id : 1215
     * location : null
     * makeStaff : {"id":1009,"name":"陈豪"}
     * maxQuantity : 495
     * minQuantity : 495
     * needDate : 1586609788831
     * needId : {"id":1141,"materialId":{"code":"P092903"}}
     * needNum : 495
     * offerNum : 100
     * orderState : {"id":"WOM_batMatState/batching","value":"配料中"}
     * pending : {"activityName":"TaskEvent_09367jb","activityType":"task","bulkDealFlag":false,"deploymentId":1069,"id":16222,"openUrl":null,"processDescription":"配料指令单","processId":"batchMateril-2","processKey":"batchMateril","processVersion":2,"taskDescription":"编辑","userId":null}
     * produceBatchNum : 202004081328
     * productId : {"code":"P092903","id":1002,"name":"P092903"}
     * remark : null
     * returnNum : null
     * status : 88
     * tableInfoId : 2611
     * tableNo : batchMaterial_20200410_008
     * taskId : {"id":1291,"produceBatchNum":"202004081328","tableNo":"produceTask_20200408_003"}
     * valid : true
     * version : 4
     */

    private Object attrMap;
    private SystemCodeEntity batchSite; // 配于
    private SystemCodeEntity batchType; // 配料模式
    private Long cid;
    private StaffEntity createStaff;
    private Long createTime;
    private Long id;
    private String location; // 位置
    private StaffEntity makeStaff; // 配料指令创建人
    private BigDecimal maxQuantity; //区间上限
    private BigDecimal minQuantity; // 区间下限
    private Long needDate; // 需送达时间
    private NeedIdEntity needId; // 配料需求id
    private BigDecimal needNum; // 需配数量
    private BigDecimal offerNum; // 已配数量
    private StaffEntity oderDoneStaff; // 配料指令负责人
    @SerializedName("orderDepartMent")
    private DepartmentEntity orderDepartment; // 配料部门
    private SystemCodeEntity orderState; // 配料指令状态
    @Expose(serialize = false)
    private PendingEntity pending;
    @Expose(deserialize = false)
    private String produceBatchNum; // 生产批号
    private MaterialEntity productId; // 物料
    private String remark;
    private BigDecimal returnNum; // 已退数量
    private int status;
    private Long tableInfoId;
    private String tableNo;
    private ProduceTaskEntity taskId; // 制造指令单
    private boolean valid;
    private int version;

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public SystemCodeEntity getBatchSite() {
        if(batchSite == null){
            batchSite = new SystemCodeEntity();
        }
        return batchSite;
    }

    public void setBatchSite(SystemCodeEntity batchSite) {
        this.batchSite = batchSite;
    }

    public SystemCodeEntity getBatchType() {
        if(batchType == null){
            batchType = new SystemCodeEntity();
        }
        return batchType;
    }

    public void setBatchType(SystemCodeEntity batchType) {
        this.batchType = batchType;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public StaffEntity getMakeStaff() {
        return makeStaff;
    }

    public void setMakeStaff(StaffEntity makeStaff) {
        this.makeStaff = makeStaff;
    }

    public BigDecimal getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(BigDecimal maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public BigDecimal getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(BigDecimal minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Long getNeedDate() {
        return needDate;
    }

    public void setNeedDate(Long needDate) {
        this.needDate = needDate;
    }

    public NeedIdEntity getNeedId() {
        return needId;
    }

    public void setNeedId(NeedIdEntity needId) {
        this.needId = needId;
    }

    public BigDecimal getNeedNum() {
        return needNum;
    }

    public void setNeedNum(BigDecimal needNum) {
        this.needNum = needNum;
    }

    public BigDecimal getOfferNum() {
        if (offerNum == null){
            offerNum = new BigDecimal(0);
        }
        return offerNum;
    }

    public void setOfferNum(BigDecimal offerNum) {
        this.offerNum = offerNum;
    }

    public StaffEntity getOderDoneStaff() {
        return oderDoneStaff;
    }

    public void setOderDoneStaff(StaffEntity oderDoneStaff) {
        this.oderDoneStaff = oderDoneStaff;
    }

    public DepartmentEntity getOrderDepartment() {
        return orderDepartment;
    }

    public void setOrderDepartment(DepartmentEntity orderDepartment) {
        this.orderDepartment = orderDepartment;
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

    public String getProduceBatchNum() {
        return produceBatchNum;
    }

    public void setProduceBatchNum(String produceBatchNum) {
        this.produceBatchNum = produceBatchNum;
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

    public ProduceTaskEntity getTaskId() {
        return taskId;
    }

    public void setTaskId(ProduceTaskEntity taskId) {
        this.taskId = taskId;
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

    public static class NeedIdEntity extends BaseEntity{
        /**
         * id : 1141
         * materialId : {"code":"P092903"}
         */

        private Long id;
        private MaterialEntity materialId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public MaterialEntity getMaterialId() {
            return materialId;
        }

        public void setMaterialId(MaterialEntity materialId) {
            this.materialId = materialId;
        }
    }
}
