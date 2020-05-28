package com.supcon.mes.module_wom_rejectmaterial.model.dto;

import com.google.gson.annotations.SerializedName;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.module_wom_rejectmaterial.constant.RmConstant;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 退料提交
 */
public class RejectMaterialDTO {

    /**
     * workFlowVar : {"outcomeMapJson":"[{\"type\":\"normal\",\"dec\":\"生效\",\"outcome\":\"SequenceFlow_1dghow0\"}]","outcome":"SequenceFlow_1dghow0","comment":"","outcomeType":"normal","activityName":"TaskEvent_0nsp5v4","activityType":"task"}
     * operateType : submit
     * deploymentId : 1422
     * taskDescription : 编辑
     * activityName : TaskEvent_0nsp5v4
     * pendingId : 4383
     * dgList : {"dg1581993495790":"[{\"batchingPartId\":{\"batRecordState\":{\"id\":\"WOM_batMatState/reject\",\"value\":\"已拒签\"},\"id\":1205,\"offerNum\":9,\"putinNum\":null},\"headId\":{\"id\":1018},\"id\":\"1030\",\"materialBatchNum\":\"01\",\"materialId\":{\"code\":\"123001\",\"id\":1002,\"name\":\"物料A\"},\"outStoreId\":{\"code\":\"001001002\",\"id\":1001,\"name\":\"001层001排002格\"},\"outWareId\":{\"code\":\"wl001\",\"id\":1000,\"name\":\"物流仓001\",\"storesetState\":true},\"preparePartId\":null,\"rejectNum\":9,\"rejectReason\":{\"id\":\"WOM_rejectReason/wrong\",\"value\":\"错发\"},\"remark\":null,\"sort\":0,\"storeId\":null,\"taskId\":null,\"version\":0,\"wareId\":{\"attrMap\":null,\"belongDept\":{\"id\":1001,\"name\":\"MES一部\"},\"cid\":1000,\"code\":\"wl002\",\"createStaff\":null,\"createTime\":null,\"id\":1003,\"keeper\":{\"id\":1001,\"name\":\"甘卿媛\"},\"memoField\":null,\"name\":\"物流仓002\",\"status\":null,\"storesetState\":false,\"tableInfoId\":null,\"tableNo\":null,\"valid\":true,\"version\":0,\"warehouseAddress\":\"物流中心北面\",\"warehouseAttribute\":{\"id\":\"BaseSet_warehouseAttribute/warehouse\",\"value\":\"物流仓\"},\"warehouseClass\":{\"id\":1000,\"name\":\"物流仓\"},\"warehouseState\":{\"id\":\"BaseSet_warehouseState/using\",\"value\":\"启用\"},\"needCheck\":true,\"rowIndex\":3,\"currClickColKey\":\"888\",\"key\":1003,\"isChecked\":true}}]"}
     * dgDeletedIds : {"dg1581993495790":null}
     * rejectMaterial : {"rejectApplyStaff":{"id":1002,"name":"小袁"},"rejectApplyDate":1587037884000,"rejectType":{"undefined":"","id":"WOM_rejectType/forBatch"},"remark":"测试提交00000000000000000000000000","version":1}
     * uploadFileFormMap : []
     * ids2del :
     * viewCode : WOM_1.0.0_rejectMaterilal_batchRejectEdit
     */

    private WorkFlowVarDTO workFlowVar;
    private String operateType;
    private String deploymentId;
    private String taskDescription;
    private String activityName;
    private String pendingId;
    private DgListEntity dgList;
    private DgListEntity dgDeletedIds;
    private RejectMaterialEntity rejectMaterial;
    private String ids2del;
    private String viewCode;
    private List<?> uploadFileFormMap;

    public WorkFlowVarDTO getWorkFlowVar() {
        return workFlowVar;
    }

    public void setWorkFlowVar(WorkFlowVarDTO workFlowVarDTO) {
        this.workFlowVar = workFlowVarDTO;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPendingId() {
        return pendingId;
    }

    public void setPendingId(String pendingId) {
        this.pendingId = pendingId;
    }

    public DgListEntity getDgList() {
        return dgList;
    }

    public void setDgList(DgListEntity dgList) {
        this.dgList = dgList;
    }

    public DgListEntity getDgDeletedIds() {
        return dgDeletedIds;
    }

    public void setDgDeletedIds(DgListEntity dgDeletedIds) {
        this.dgDeletedIds = dgDeletedIds;
    }

    public RejectMaterialEntity getRejectMaterial() {
        return rejectMaterial;
    }

    public void setRejectMaterial(RejectMaterialEntity rejectMaterial) {
        this.rejectMaterial = rejectMaterial;
    }

    public String getIds2del() {
        return ids2del;
    }

    public void setIds2del(String ids2del) {
        this.ids2del = ids2del;
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public List<?> getUploadFileFormMap() {
        return uploadFileFormMap;
    }

    public void setUploadFileFormMap(List<?> uploadFileFormMap) {
        this.uploadFileFormMap = uploadFileFormMap;
    }

    /*public static class WorkFlowVarEntity {
        *//**
         * outcomeMapJson : [{"type":"normal","dec":"生效","outcome":"SequenceFlow_1dghow0"}]
         * outcome : SequenceFlow_1dghow0
         * comment :
         * outcomeType : normal
         * activityName : TaskEvent_0nsp5v4
         * activityType : task
         *//*

        private String outcomeMapJson;
        private String outcome;
        private String comment;
        private String outcomeType;
        private String activityName;
        private String activityType;

        public String getOutcomeMapJson() {
            return outcomeMapJson;
        }

        public void setOutcomeMapJson(String outcomeMapJson) {
            this.outcomeMapJson = outcomeMapJson;
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getOutcomeType() {
            return outcomeType;
        }

        public void setOutcomeType(String outcomeType) {
            this.outcomeType = outcomeType;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }
    }*/

    public static class DgListEntity {
        /**
         * dg1581993495790 : [{"batchingPartId":{"batRecordState":{"id":"WOM_batMatState/reject","value":"已拒签"},"id":1205,"offerNum":9,"putinNum":null},"headId":{"id":1018},"id":"1030","materialBatchNum":"01","materialId":{"code":"123001","id":1002,"name":"物料A"},"outStoreId":{"code":"001001002","id":1001,"name":"001层001排002格"},"outWareId":{"code":"wl001","id":1000,"name":"物流仓001","storesetState":true},"preparePartId":null,"rejectNum":9,"rejectReason":{"id":"WOM_rejectReason/wrong","value":"错发"},"remark":null,"sort":0,"storeId":null,"taskId":null,"version":0,"wareId":{"attrMap":null,"belongDept":{"id":1001,"name":"MES一部"},"cid":1000,"code":"wl002","createStaff":null,"createTime":null,"id":1003,"keeper":{"id":1001,"name":"甘卿媛"},"memoField":null,"name":"物流仓002","status":null,"storesetState":false,"tableInfoId":null,"tableNo":null,"valid":true,"version":0,"warehouseAddress":"物流中心北面","warehouseAttribute":{"id":"BaseSet_warehouseAttribute/warehouse","value":"物流仓"},"warehouseClass":{"id":1000,"name":"物流仓"},"warehouseState":{"id":"BaseSet_warehouseState/using","value":"启用"},"needCheck":true,"rowIndex":3,"currClickColKey":"888","key":1003,"isChecked":true}}]
         */

        @SerializedName(value = RmConstant.DG_NAME.DG_REJECT_BATCH_MATERIAL_EDIT)
        private String dgEditBatch; // 配料编辑dg
        @SerializedName(value = RmConstant.DG_NAME.DG_PREPARE_BATCH_MATERIAL_EDIT)
        private String dgEditPrepare; // 备料编辑dg

        public String getDgEditBatch() {
            return dgEditBatch;
        }

        public void setDgEditBatch(String dgEditBatch) {
            this.dgEditBatch = dgEditBatch;
        }

        public String getDgEditPrepare() {
            return dgEditPrepare;
        }

        public void setDgEditPrepare(String dgEditPrepare) {
            this.dgEditPrepare = dgEditPrepare;
        }
    }
}
