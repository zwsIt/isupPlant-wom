package com.supcon.mes.module_wom_producetask.model.dto;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.bean.ProcReportEntity;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 人工产出活动报工提交DTO
 */
public class OutputDetailDTO extends BaseEntity {

    /**
     * workFlowVar : {}
     * operateType : save
     * dgList : {"dg1576141500525":"[{\"checkItems\":\"设备环境安全\",\"headId\":{\"id\":1925},\"id\":\"1097\",\"isPass\":false,\"remark\":null,\"reportNum\":123,\"rmCheckDetailId\":{\"id\":1542},\"sort\":null,\"standrad\":null,\"version\":1},{\"isPass\":false,\"checkItems\":\"设备温度\",\"headId\":{\"id\":1925},\"id\":\"1098\",\"remark\":null,\"reportNum\":654,\"rmCheckDetailId\":{\"id\":1543},\"sort\":null,\"standrad\":\"[20,100]\",\"version\":1},{\"isPass\":false,\"checkItems\":\"设备压力\",\"headId\":{\"id\":1925},\"id\":\"1099\",\"remark\":null,\"reportNum\":\"\",\"rmCheckDetailId\":{\"id\":1544},\"sort\":null,\"standrad\":\"(100,202.10)\",\"version\":1},{\"checkItems\":\"生产俱备\",\"headId\":{\"id\":1925},\"id\":\"1100\",\"isPass\":false,\"remark\":null,\"reportNum\":678,\"rmCheckDetailId\":{\"id\":1545},\"sort\":null,\"standrad\":null,\"version\":1}]"}
     * dgDeletedIds : {"dg1576141500525":1215}
     * procReport : {"produceStaffId":{"id":1009,"name":"陈豪"},"produceTime":1586335967314,"taskActiveId":{"id":4460,"taskProcessId":{"name":"工序one"},"name":"产前检查活动","activeType":{"id":"RM_activeType/check"}},"exeSystem":{"id":"RM_exeSystem/mes"},"remark":"","version":2}
     * uploadFileFormMap : []
     * ids2del :
     * viewCode : WOM_1.0.0_procReport_putInFeedBackEdit
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
         * dg1576203064378 : [{"checkItems":"设备环境安全","headId":{"id":1925},"id":"1097","isPass":false,"remark":null,"reportNum":123,"rmCheckDetailId":{"id":1542},"sort":null,"standrad":null,"version":1},{"isPass":false,"checkItems":"设备温度","headId":{"id":1925},"id":"1098","remark":null,"reportNum":654,"rmCheckDetailId":{"id":1543},"sort":null,"standrad":"[20,100]","version":1},{"isPass":false,"checkItems":"设备压力","headId":{"id":1925},"id":"1099","remark":null,"reportNum":"","rmCheckDetailId":{"id":1544},"sort":null,"standrad":"(100,202.10)","version":1},{"checkItems":"生产俱备","headId":{"id":1925},"id":"1100","isPass":false,"remark":null,"reportNum":678,"rmCheckDetailId":{"id":1545},"sort":null,"standrad":null,"version":1}]
         */

        @SerializedName(value = WomConstant.DG_NAME.DG_OUTPUT_ACTIVITY_REPORT)
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
         * dg1576203064378 : null
         */

        @SerializedName(value = WomConstant.DG_NAME.DG_OUTPUT_ACTIVITY_REPORT)
        private String dg;

        public String getDg() {
            return dg;
        }

        public void setDg(String dg) {
            this.dg = dg;
        }
    }

//    public static class ProcReportEntity {
//        /**
//         * produceStaffId : {"id":1009,"name":"陈豪"}
//         * produceTime : 1586335967314
//         * taskActiveId : {"id":4460,"taskProcessId":{"name":"工序one"},"name":"产前检查活动","activeType":{"id":"RM_activeType/check"}}
//         * exeSystem : {"id":"RM_exeSystem/mes"}
//         * remark :
//         * version : 2
//         */
//
//        private ProduceStaffIdEntity produceStaffId;
//        private long produceTime;
//        private TaskActiveIdEntity taskActiveId;
//        private ExeSystemEntity exeSystem;
//        private String remark;
//        private int version;
//
//        public ProduceStaffIdEntity getProduceStaffId() {
//            return produceStaffId;
//        }
//
//        public void setProduceStaffId(ProduceStaffIdEntity produceStaffId) {
//            this.produceStaffId = produceStaffId;
//        }
//
//        public long getProduceTime() {
//            return produceTime;
//        }
//
//        public void setProduceTime(long produceTime) {
//            this.produceTime = produceTime;
//        }
//
//        public TaskActiveIdEntity getTaskActiveId() {
//            return taskActiveId;
//        }
//
//        public void setTaskActiveId(TaskActiveIdEntity taskActiveId) {
//            this.taskActiveId = taskActiveId;
//        }
//
//        public ExeSystemEntity getExeSystem() {
//            return exeSystem;
//        }
//
//        public void setExeSystem(ExeSystemEntity exeSystem) {
//            this.exeSystem = exeSystem;
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
//        public int getVersion() {
//            return version;
//        }
//
//        public void setVersion(int version) {
//            this.version = version;
//        }
//
//        public static class ProduceStaffIdEntity {
//            /**
//             * id : 1009
//             * name : 陈豪
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
//        public static class TaskActiveIdEntity {
//            /**
//             * id : 4460
//             * taskProcessId : {"name":"工序one"}
//             * name : 产前检查活动
//             * activeType : {"id":"RM_activeType/check"}
//             */
//
//            private int id;
//            private TaskProcessIdEntity taskProcessId;
//            private String name;
//            private ActiveTypeEntity activeType;
//
//            public int getId() {
//                return id;
//            }
//
//            public void setId(int id) {
//                this.id = id;
//            }
//
//            public TaskProcessIdEntity getTaskProcessId() {
//                return taskProcessId;
//            }
//
//            public void setTaskProcessId(TaskProcessIdEntity taskProcessId) {
//                this.taskProcessId = taskProcessId;
//            }
//
//            public String getName() {
//                return name;
//            }
//
//            public void setName(String name) {
//                this.name = name;
//            }
//
//            public ActiveTypeEntity getActiveType() {
//                return activeType;
//            }
//
//            public void setActiveType(ActiveTypeEntity activeType) {
//                this.activeType = activeType;
//            }
//
//            public static class TaskProcessIdEntity {
//                /**
//                 * name : 工序one
//                 */
//
//                private String name;
//
//                public String getName() {
//                    return name;
//                }
//
//                public void setName(String name) {
//                    this.name = name;
//                }
//            }
//
//            public static class ActiveTypeEntity {
//                /**
//                 * id : RM_activeType/check
//                 */
//
//                private String id;
//
//                public String getId() {
//                    return id;
//                }
//
//                public void setId(String id) {
//                    this.id = id;
//                }
//            }
//        }
//
//        public static class ExeSystemEntity {
//            /**
//             * id : RM_exeSystem/mes
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
