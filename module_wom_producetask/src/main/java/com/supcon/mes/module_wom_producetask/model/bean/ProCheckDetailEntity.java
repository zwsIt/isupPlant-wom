package com.supcon.mes.module_wom_producetask.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/8
 * Email zhangwenshuai1@supcon.com
 * Desc 检查清单
 */
public class ProCheckDetailEntity extends BaseEntity {

    /**
     * checkItems : 设备温度
     * headId : {"id":1925}
     * id : 1098
     * isPass : false
     * remark : null
     * reportNum : null
     * rmCheckDetailId : {"id":1543}
     * sort : null
     * standrad : [20,100]
     * version : 0
     */

    private String checkItems;      // 检查项
    private ProcReportEntity headId;// 工序报工Id
    private Long id;
    private Boolean isPass;         // 是否通过
    private String remark;
    private String reportValue;   // 报出值
    private RmCheckDetailIdEntity rmCheckDetailId;  // 配方检查项Id
    private Object sort;
    @SerializedName(value = "standrad")
    private String standard;
//    @Expose(deserialize = false,serialize = false)
//    private int version;

    public String getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(String checkItems) {
        this.checkItems = checkItems;
    }

    public ProcReportEntity getHeadId() {
        return headId;
    }

    public void setHeadId(ProcReportEntity headId) {
        this.headId = headId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReportValue() {
        return reportValue;
    }

    public void setReportValue(String reportValue) {
        this.reportValue = reportValue;
    }

    public RmCheckDetailIdEntity getRmCheckDetailId() {
        return rmCheckDetailId;
    }

    public void setRmCheckDetailId(RmCheckDetailIdEntity rmCheckDetailId) {
        this.rmCheckDetailId = rmCheckDetailId;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }

    /**
     * ProCheckDetailEntity
     * created by zhangwenshuai1 2020/4/8
     * 配方管理/检查明细实体
     */
    public static class RmCheckDetailIdEntity extends BaseEntity {
        /**
         * id : 1543
         */

        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
