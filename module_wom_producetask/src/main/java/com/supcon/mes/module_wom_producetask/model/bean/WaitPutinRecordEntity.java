package com.supcon.mes.module_wom_producetask.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.bean.wom.FormulaEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 待办记录:适用生产工单、生产工序、生产活动实体映射
 */
public class WaitPutinRecordEntity extends BaseEntity {

    /**
     * actStaffId : {"code":"ch","id":1009,"name":"陈豪"}
     * actStaffName : 陈豪
     * activeName : 1-2~投料1
     * actualEndTime : 1585026324665
     * actualStartTime : 1585026229210
     * attrMap : null
     * cid : 1000
     * createStaff : null
     * createTime : null
     * exeState : {"id":"WOM_runState/finished","value":"已完成"}
     * exeSystem : {"id":"RM_exeSystem/mes","value":"MES系统"}
     * id : 1376
     * isAlige : false
     * lineCode : MES
     * lineName : MES开发线
     * planEndTime : null
     * planNum : 100
     * planStartTime : null
     * procReportId : {"id":1823}
     * processName : 工序1-2
     * produceBatchNum : 2020034
     * formulaId : {"id":1000,"formualCode":"FormulaCode"}
     * productId : {"code":"F","id":1004,"mainUnit":{"id":1003,"name":"1"},"name":"F"}
     * recordType : {"id":"WOM_recordType/active","value":"活动"}
     * status : null
     * tableInfoId : null
     * tableNo : null
     * taskActiveId : {"id":4258}
     * taskId : {"id":1260}
     * taskProcessId : {"id":1486}
     * valid : true
     * version : 0
     */

    private StaffEntity actStaffId;       // 操作人
    private String actStaffName;    // 操作人姓名
    private String activeName;      // 活动名称
    private Long actualEndTime;     // 实际结束时间
    private Long actualStartTime;   // 实际开始时间
    private Map attrMap;
    private Long cid;                // 公司ID
    private StaffEntity createStaff;
    private Long createTime;
    private SystemCodeEntity exeState;  // 执行状态
    private SystemCodeEntity exeSystem; // 执行系统
    private Long id;
    @SerializedName(value = "isAlige")
    private boolean agile;            // 灵活活动
    private FactoryModelEntity lineId;  // 产线
    private String lineCode;            // 生产线编码
    private String lineName;            // 生产线名称
    private Long planEndTime;           // 计划结束时间
    private BigDecimal planNum;         // 计划数量
    private Long planStartTime;         // 计划开始时间
    private ProcReportEntity procReportId; // 过程报工单
    private String processName;             // 工序名称
    private String produceBatchNum;         // 生产批号
    private MaterialEntity productId;       // 产品
    private SystemCodeEntity recordType;    // 待办类型
    private Object status;
    private Long tableInfoId;
    private String tableNo;
    private TaskActiveEntity taskActiveId;      // 指令单活动
    private ProduceTaskEntity taskId;           // 生产指令单
    private TaskProcessEntity taskProcessId;    // 指令单工序
    private boolean valid;
    private int version;
    private FormulaEntity formulaId;        // 配方
    private FactoryModelEntity euqId;       // 工作单元
    private WarehouseEntity ware;
    private String batchCode;//物料批号

    public FactoryModelEntity getEuqId() {
        if (euqId == null){
            euqId = new FactoryModelEntity();
        }
        return euqId;
    }

    public void setEuqId(FactoryModelEntity euqId) {
        this.euqId = euqId;
    }

    private List<WaitPutinRecordEntity> mProcessWaitPutinRecordEntityList = new ArrayList<>();  // 工序待办类型，用于展示/收起
    private boolean isExpand; // 展开收起
    private int fatherPosition; // 工序所属工单位置；

    public FactoryModelEntity getLineId() {
        return lineId;
    }

    public void setLineId(FactoryModelEntity lineId) {
        this.lineId = lineId;
    }

    public int getFatherPosition() {
        return fatherPosition;
    }

    public void setFatherPosition(int fatherPosition) {
        this.fatherPosition = fatherPosition;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public List<WaitPutinRecordEntity> getProcessWaitPutinRecordEntityList() {
        return mProcessWaitPutinRecordEntityList;
    }

    public void setProcessWaitPutinRecordEntityList(List<WaitPutinRecordEntity> processWaitPutinRecordEntityList) {
        mProcessWaitPutinRecordEntityList = processWaitPutinRecordEntityList;
    }

    public StaffEntity getActStaffId() {
        return actStaffId;
    }

    public void setActStaffId(StaffEntity actStaffId) {
        this.actStaffId = actStaffId;
    }

    public String getActStaffName() {
        return actStaffName;
    }

    public void setActStaffName(String actStaffName) {
        this.actStaffName = actStaffName;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public Long getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Long actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Long getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Long actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Map getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map attrMap) {
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

    public SystemCodeEntity getExeState() {
        if (exeState == null){
            exeState = new SystemCodeEntity();
        }
        return exeState;
    }

    public void setExeState(SystemCodeEntity exeState) {
        this.exeState = exeState;
    }

    public SystemCodeEntity getExeSystem() {
        if (exeSystem == null){
            exeSystem = new SystemCodeEntity();
        }
        return exeSystem;
    }

    public void setExeSystem(SystemCodeEntity exeSystem) {
        this.exeSystem = exeSystem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getAgile() {
        return agile;
    }

    public void setAgile(boolean agile) {
        this.agile = agile;
    }

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Long getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Long planEndTime) {
        this.planEndTime = planEndTime;
    }

    public BigDecimal getPlanNum() {
        return planNum!=null?planNum.setScale(WomConstant.scale):null;
    }

    public void setPlanNum(BigDecimal planNum) {
        this.planNum = planNum;
    }

    public Long getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Long planStartTime) {
        this.planStartTime = planStartTime;
    }

    public ProcReportEntity getProcReportId() {
        if (procReportId == null){
            procReportId = new ProcReportEntity();
        }
        return procReportId;
    }

    public void setProcReportId(ProcReportEntity procReportId) {
        this.procReportId = procReportId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProduceBatchNum() {
        return produceBatchNum;
    }

    public void setProduceBatchNum(String produceBatchNum) {
        this.produceBatchNum = produceBatchNum;
    }

    public MaterialEntity getProductId() {
        if (productId == null){
            productId = new MaterialEntity();
        }
        return productId;
    }

    public void setProductId(MaterialEntity productId) {
        this.productId = productId;
    }

    public SystemCodeEntity getRecordType() {
        if (recordType == null){
            recordType = new SystemCodeEntity();
        }
        return recordType;
    }

    public void setRecordType(SystemCodeEntity recordType) {
        this.recordType = recordType;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
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

    public TaskActiveEntity getTaskActiveId() {
        if (taskActiveId == null){
            taskActiveId = new TaskActiveEntity();
        }
        return taskActiveId;
    }

    public void setTaskActiveId(TaskActiveEntity taskActiveId) {
        this.taskActiveId = taskActiveId;
    }

    public ProduceTaskEntity getTaskId() {
        return taskId;
    }

    public void setTaskId(ProduceTaskEntity taskId) {
        this.taskId = taskId;
    }

    public TaskProcessEntity getTaskProcessId() {
        if (taskProcessId == null){
            taskProcessId = new TaskProcessEntity();
        }
        return taskProcessId;
    }

    public void setTaskProcessId(TaskProcessEntity taskProcessId) {
        this.taskProcessId = taskProcessId;
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

    public FormulaEntity getFormulaId() {
        if (formulaId == null){
            formulaId = new FormulaEntity();
        }
        return formulaId;
    }

    public void setFormulaId(FormulaEntity formulaId) {
        this.formulaId = formulaId;
    }

    public WarehouseEntity getWare() {
        return ware;
    }

    public void setWare(WarehouseEntity ware) {
        this.ware = ware;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }
}
