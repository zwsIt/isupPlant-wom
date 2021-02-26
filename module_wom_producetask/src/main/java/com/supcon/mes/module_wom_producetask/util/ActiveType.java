package com.supcon.mes.module_wom_producetask.util;

import com.supcon.mes.module_wom_producetask.constant.WomConstant;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/2/24
 * @email zhangwenshuai1@supcon.com
 * Desc
 */
public enum ActiveType {
    // 常规
    COMMON(WomConstant.SystemCode.RM_activeType_COMMON,1),
    // 检查
    CHECK(WomConstant.SystemCode.RM_activeType_CHECK,2),
    // 人工投料
    PUTIN(WomConstant.SystemCode.RM_activeType_PUTIN,3),
    // 投配料
    BATCH_PUTIN(WomConstant.SystemCode.RM_activeType_BATCH_PUTIN,4),
    // 质检
    QUALITY(WomConstant.SystemCode.RM_activeType_QUALITY,5),
    // 人工产出
    OUTPUT(WomConstant.SystemCode.RM_activeType_OUTPUT,6),
    // 管道投料
    PIPE_PUTIN(WomConstant.SystemCode.RM_activeType_PIPE_PUTIN,7),
    // 管道投配料
    PIPE_BATCH_PUTIN(WomConstant.SystemCode.RM_activeType_PIPE_BATCH_PUTIN,8),
    // 管道产出
    PIPE_OUTPUT(WomConstant.SystemCode.RM_activeType_PIPE_OUTPUT,9),;
    // 常规
//    COMMON(WomConstant.SystemCode.RM_activeType_COMMON,10),;

    public String systemCodeId;
    public int type;

    ActiveType(String systemCodeId, int i) {
        this.systemCodeId = systemCodeId;
        this.type = i;
    }

    public static int getType(String systemCodeId){
        for (ActiveType activeType : ActiveType.values()){
            if (systemCodeId.equals(activeType.systemCodeId)){
                return activeType.type;
            }
        }
        return -1;
    }

}
