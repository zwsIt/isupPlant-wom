package com.supcon.mes.module_wom_replenishmaterial.model.dto;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/13
 * @email zhangwenshuai1@supcon.com
 * Desc 补料单提交实体DTO
 */
public class ReplenishMaterialTableDTO extends BaseEntity {

    /**
     * workFlowVar
     */
    private WorkFlowVarDTO workFlowVar;
    /**
     * operateType
     */
    private String operateType;
    /**
     * deploymentId
     */
    private String deploymentId;
    /**
     * taskDescription
     */
    private String taskDescription;
    /**
     * activityName
     */
    private String activityName;
    /**
     * dgList
     */
    private DgListEntity dgList;
    /**
     * dgDeletedIds
     */
    private DgListEntity dgDeletedIds;
    /**
     * fmBill
     */
    private ReplenishMaterialTableEntity fmBill;
    /**
     * uploadFileFormMap
     */
    private List<?> uploadFileFormMap;
    /**
     * ids2del
     */
    private String ids2del;
    /**
     * viewCode
     */
    private String viewCode;

    /**
     *pendingId
     */
    private String pendingId;

    public String getPendingId() {
        return pendingId;
    }

    public void setPendingId(String pendingId) {
        this.pendingId = pendingId;
    }

    public WorkFlowVarDTO getWorkFlowVar() {
        return workFlowVar;
    }

    public void setWorkFlowVar(WorkFlowVarDTO workFlowVar) {
        this.workFlowVar = workFlowVar;
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

    public ReplenishMaterialTableEntity getFmBill() {
        return fmBill;
    }

    public void setFmBill(ReplenishMaterialTableEntity fmBill) {
        this.fmBill = fmBill;
    }

    public List<?> getUploadFileFormMap() {
        return uploadFileFormMap;
    }

    public void setUploadFileFormMap(List<?> uploadFileFormMap) {
        this.uploadFileFormMap = uploadFileFormMap;
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

    public static class DgListEntity {
        /**
         * dg1620710952134
         */
        @SerializedName(value = ReplenishConstant.DG_NAME.DG_REPLENISH_MATERIAL_EDIT)
        private String dg;

        public String getDg() {
            return dg;
        }

        public void setDg(String dg) {
            this.dg = dg;
        }
    }
}
