package com.supcon.mes.module_wom_producetask.model.dto;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.ProcReportEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/6/28
 * Email zhangwenshuai1@supcon.com
 * Desc 工单结束报工提交DTO
 */
public class ProduceEndTaskDTO extends BaseEntity {

    /**
     * {
     *     "workFlowVar":{
     *
     *     },
     *     "operateType":"save",
     *     "dgList":{
     *         "dg1575977843605":"[{\"reportNum\":11,\"remainOperate\":{\"id\":\"WOM_remainOperate/noOperate\"},\"product\":{\"id\":104891119076611},\"materialBatchNum\":\"543276\",\"name\":null,\"specifications\":null,\"wareId\":{\"id\":null},\"storesetState\":false,\"storeId\":{\"id\":null},\"outputNum\":11,\"remainNum\":null,\"produceUnit\":{\"id\":null},\"putinTime\":null,\"putinEndTime\":1624851712000,\"remark\":null,\"mainUnit\":{\"id\":null},\"sort\":0}]"
     *     },
     *     "dgDeletedIds":{
     *         "dg1575977843605":null
     *     },
     *     "procReport":{
     *         "produceStaffId":{
     *             "id":1377343017963696,
     *             "code":"001",
     *             "name":"MES测试"
     *         },
     *         "produceTime":1623207832880,
     *         "taskId":{
     *             "id":106744829437184,
     *             "code":"",
     *             "produceBatchNum":"543276",
     *             "productId":{
     *                 "code":"40009417",
     *                 "name":"楷信餐饮-番茄汤锅底料-小料"
     *             },
     *             "planNum":1,
     *             "lineId":{
     *                 "name":"3"
     *             },
     *             "workAreaId":{
     *                 "name":"2"
     *             }
     *         },
     *         "version":0
     *     },
     *     "uploadFileFormMap":[
     *
     *     ],
     *     "ids2del":"",
     *     "viewCode":"WOM_1.0.0_procReport_outPutCommonTaskEdit"
     * }
     */

    private WorkFlowVar workFlowVar;
    private String operateType;
    private DgListEntity dgList;
    private DgDeletedIdsEntity dgDeletedIds;
    private ProcReportEntity procReport;
    private String ids2del;
    private String viewCode;
    private List<?> uploadFileFormMap;

    public WorkFlowVar getWorkFlowVar() {
        return workFlowVar;
    }

    public void setWorkFlowVar(WorkFlowVar workFlowVar) {
        this.workFlowVar = workFlowVar;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public DgListEntity getDgList() {
        return dgList;
    }

    public void setDgList(DgListEntity dgList) {
        this.dgList = dgList;
    }

    public DgDeletedIdsEntity getDgDeletedIds() {
        return dgDeletedIds;
    }

    public void setDgDeletedIds(DgDeletedIdsEntity dgDeletedIds) {
        this.dgDeletedIds = dgDeletedIds;
    }

    public ProcReportEntity getProcReport() {
        return procReport;
    }

    public void setProcReport(ProcReportEntity procReport) {
        this.procReport = procReport;
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

    public static class DgListEntity {
        /**
         * dg1575977843605 : [{"checkItems":"设备环境安全","headId":{"id":1925},"id":"1097","isPass":false,"remark":null,"reportNum":123,"rmCheckDetailId":{"id":1542},"sort":null,"standrad":null,"version":1},{"isPass":false,"checkItems":"设备温度","headId":{"id":1925},"id":"1098","remark":null,"reportNum":654,"rmCheckDetailId":{"id":1543},"sort":null,"standrad":"[20,100]","version":1},{"isPass":false,"checkItems":"设备压力","headId":{"id":1925},"id":"1099","remark":null,"reportNum":"","rmCheckDetailId":{"id":1544},"sort":null,"standrad":"(100,202.10)","version":1},{"checkItems":"生产俱备","headId":{"id":1925},"id":"1100","isPass":false,"remark":null,"reportNum":678,"rmCheckDetailId":{"id":1545},"sort":null,"standrad":null,"version":1}]
         */

        @SerializedName(value = WomConstant.DG_NAME.DG_PRODUCE_END_TASK_REPORT)
        private String dg;

        public String getDg() {
            return dg;
        }

        public void setDg(String dg) {
            this.dg = dg;
        }
    }

    public static class DgDeletedIdsEntity {
        /**
         * dg1575977843605 : null
         */

        @SerializedName(value = WomConstant.DG_NAME.DG_PRODUCE_END_TASK_REPORT)
        private String dg;

        public String getDg() {
            return dg;
        }

        public void setDg(String dg) {
            this.dg = dg;
        }
    }
}
