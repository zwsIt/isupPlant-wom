package com.supcon.mes.module_wom_batchmaterial.model.dto;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterilEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令提交DTO
 */
public class BatchMaterialInstructionDTO extends BaseEntity {

    /**
     * mWorkFlowVarDTO : {"comment":"","activityName":"TaskEvent_09367jb","activityType":"task"}
     * operateType : save
     * deploymentId : 1069
     * taskDescription : 编辑
     * activityName : TaskEvent_09367jb
     * pendingId : 18653
     * dgList : {"dg1582113356339":null}
     * dgDeletedIds : {"dg1582113356339":null}
     * batchMateril : {"tableNo":"batchMaterial_20200415_005","taskId":{"id":1326,"tableNo":"produceTask_20200413_006"},"createTime":1586919579932,"makeStaff":{"id":1007,"name":"cuixin"},"productId":{"id":1006,"code":"Prod001","name":"产品001"},"needDate":"","needNum":50,"offerNum":"","minQuantity":50,"maxQuantity":150,"batchType":{"undefined":"","id":"RM_batchType/remoteBat"},"batchSite":{"id":"RM_batchSite/putinContainer"},"orderState":{"id":"WOM_batMatState/batching"},"remark":"","location":"","version":6}
     * uploadFileFormMap : []
     * ids2del :
     * viewCode : WOM_1.0.0_batchMaterial_batchMaterialOrder
     */

    private WorkFlowVarDTO workFlowVar;
    private String operateType;
    private String deploymentId;
    private String taskDescription;
    private String activityName;
    private String pendingId;
    private DgListEntity dgList;
    private DgListEntity dgDeletedIds;
    private BatchMaterilEntity batchMateril;
    private String ids2del;
    private String viewCode;
    private List<?> uploadFileFormMap;

    public WorkFlowVarDTO getWorkFlowVarDTO() {
        return workFlowVar;
    }

    public void setWorkFlowVarDTO(WorkFlowVarDTO workFlowVarDTO) {
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

    public BatchMaterilEntity getBatchMateril() {
        return batchMateril;
    }

    public void setBatchMateril(BatchMaterilEntity batchMateril) {
        this.batchMateril = batchMateril;
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
         * dg1582113356339 : null
         */

        @SerializedName(value = BmConstant.DG_NAME.DG_BATCH_MATERIAL_EDIT)
        private String dg;

        public String getDg() {
            return dg;
        }

        public void setDg(String dg) {
            this.dg = dg;
        }
    }

//    public static class BatchMaterilEntity {
//        /**
//         * tableNo : batchMaterial_20200415_005
//         * taskId : {"id":1326,"tableNo":"produceTask_20200413_006"}
//         * createTime : 1586919579932
//         * makeStaff : {"id":1007,"name":"cuixin"}
//         * productId : {"id":1006,"code":"Prod001","name":"产品001"}
//         * needDate :
//         * needNum : 50
//         * offerNum :
//         * minQuantity : 50
//         * maxQuantity : 150
//         * batchType : {"undefined":"","id":"RM_batchType/remoteBat"}
//         * batchSite : {"id":"RM_batchSite/putinContainer"}
//         * orderState : {"id":"WOM_batMatState/batching"}
//         * remark :
//         * location :
//         * version : 6
//         */
//
//        private String tableNo;
//        private TaskIdEntity taskId;
//        private Long createTime;
//        private MakeStaffEntity makeStaff;
//        private ProductIdEntity productId;
//        private String needDate;
//        private int needNum;
//        private String offerNum;
//        private int minQuantity;
//        private int maxQuantity;
//        private BatchTypeEntity batchType;
//        private BatchSiteEntity batchSite;
//        private OrderStateEntity orderState;
//        private String remark;
//        private String location;
//        private int version;
//
//        public String getTableNo() {
//            return tableNo;
//        }
//
//        public void setTableNo(String tableNo) {
//            this.tableNo = tableNo;
//        }
//
//        public TaskIdEntity getTaskId() {
//            return taskId;
//        }
//
//        public void setTaskId(TaskIdEntity taskId) {
//            this.taskId = taskId;
//        }
//
//        public Long getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(Long createTime) {
//            this.createTime = createTime;
//        }
//
//        public MakeStaffEntity getMakeStaff() {
//            return makeStaff;
//        }
//
//        public void setMakeStaff(MakeStaffEntity makeStaff) {
//            this.makeStaff = makeStaff;
//        }
//
//        public ProductIdEntity getProductId() {
//            return productId;
//        }
//
//        public void setProductId(ProductIdEntity productId) {
//            this.productId = productId;
//        }
//
//        public String getNeedDate() {
//            return needDate;
//        }
//
//        public void setNeedDate(String needDate) {
//            this.needDate = needDate;
//        }
//
//        public int getNeedNum() {
//            return needNum;
//        }
//
//        public void setNeedNum(int needNum) {
//            this.needNum = needNum;
//        }
//
//        public String getOfferNum() {
//            return offerNum;
//        }
//
//        public void setOfferNum(String offerNum) {
//            this.offerNum = offerNum;
//        }
//
//        public int getMinQuantity() {
//            return minQuantity;
//        }
//
//        public void setMinQuantity(int minQuantity) {
//            this.minQuantity = minQuantity;
//        }
//
//        public int getMaxQuantity() {
//            return maxQuantity;
//        }
//
//        public void setMaxQuantity(int maxQuantity) {
//            this.maxQuantity = maxQuantity;
//        }
//
//        public BatchTypeEntity getBatchType() {
//            return batchType;
//        }
//
//        public void setBatchType(BatchTypeEntity batchType) {
//            this.batchType = batchType;
//        }
//
//        public BatchSiteEntity getBatchSite() {
//            return batchSite;
//        }
//
//        public void setBatchSite(BatchSiteEntity batchSite) {
//            this.batchSite = batchSite;
//        }
//
//        public OrderStateEntity getOrderState() {
//            return orderState;
//        }
//
//        public void setOrderState(OrderStateEntity orderState) {
//            this.orderState = orderState;
//        }
//
//        public String getRemark() {
//            return remark;
//        }
//
//        public void setRemark(String remark) {
//            this.remark = remark;
//        }
//
//        public String getLocation() {
//            return location;
//        }
//
//        public void setLocation(String location) {
//            this.location = location;
//        }
//
//        public int getVersion() {
//            return version;
//        }
//
//        public void setVersion(int version) {
//            this.version = version;
//        }
//
//        public static class TaskIdEntity {
//            /**
//             * id : 1326
//             * tableNo : produceTask_20200413_006
//             */
//
//            private int id;
//            private String tableNo;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getTableNo() {
//                return tableNo;
//            }
//
//            public void setTableNo(String tableNo) {
//                this.tableNo = tableNo;
//            }
//        }
//
//        public static class MakeStaffEntity {
//            /**
//             * id : 1007
//             * name : cuixin
//             */
//
//            private int id;
//            private String name;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//        }
//
//        public static class ProductIdEntity {
//            /**
//             * id : 1006
//             * code : Prod001
//             * name : 产品001
//             */
//
//            private int id;
//            private String code;
//            private String name;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public String getCode() {
//                return code;
//            }
//
//            public void setCode(String code) {
//                this.code = code;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//        }
//
//        public static class BatchTypeEntity {
//            /**
//             * undefined :
//             * id : RM_batchType/remoteBat
//             */
//
//            private String undefined;
//            private String id;
//
//            public String getUndefined() {
//                return undefined;
//            }
//
//            public void setUndefined(String undefined) {
//                this.undefined = undefined;
//            }
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//        }
//
//        public static class BatchSiteEntity {
//            /**
//             * id : RM_batchSite/putinContainer
//             */
//
//            private String id;
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//        }
//
//        public static class OrderStateEntity {
//            /**
//             * id : WOM_batMatState/batching
//             */
//
//            private String id;
//
//            public String getId() {
//                return id;
//            }
//
//            public void setId(String id) {
//                this.id = id;
//            }
//        }
//    }
}
