package com.supcon.mes.module_wom_replenishmaterial.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialNotifyDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialScanDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialTableDTO;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * ClassName
 *
 * @author Created by zhangwenshuai1
 * @date on 2021/5/11
 * @email zhangwenshuai1@supcon.com
 * Desc
 */
@ApiFactory
interface NetworkService {
    /**
     * 获取补料通知列表
     * @param paramMap
     * @return
     */
    @POST("/msService/WOM/fillMaterialNotice/fmnNotice/fmnNoticeList-query")
    Flowable<CommonBAP5ListEntity<ReplenishMaterialNotifyEntity>> listReplenishMaterialNotifies(@Body Map<String, Object> paramMap);

    /**
     * 创建补料单
     * @param dto
     * @return
     */
    @POST("/msService/WOM/fillMaterial/fmBill/createTask")
    Flowable<BAP5CommonEntity<BapResultEntity>> submit(@Body ReplenishMaterialNotifyDTO[] dto);

    /**
     * 获取补料通知列表
     * @param paramMap
     * @return
     */
    @POST()
    Flowable<CommonBAP5ListEntity<ReplenishMaterialTableEntity>> listReplenishMaterialTables(@Url String url, @Body Map<String, Object> paramMap);
    /**
     * 补料编辑提交
     * @return
     */
    @POST("/msService/WOM/fillMaterial/fmBill/fmBillEdit/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> replenishMaterialEditSubmit(@Query("id") Long id, @Query("__pc__") String __pc__, @Body ReplenishMaterialTableDTO replenishMaterialTableDTO);
    /**
     * 补料单扫描 开始、结束
     * @return
     */
    @POST("/msService/WOM/fillMaterial/fmBill/changeBillState")
    Flowable<BAP5CommonEntity<BapResultEntity>> changeBillState(@Body ReplenishMaterialScanDTO replenishMaterialScanDTO);

}
