package com.supcon.mes.module_wom_replenishmaterial.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
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
    @POST("")
    Flowable<BAP5CommonEntity<BapResultEntity>> submit(@Body ReplenishMaterialNotifyEntity dto);

    /**
     * 获取补料通知列表
     * @param paramMap
     * @return
     */
    @POST()
    Flowable<CommonBAP5ListEntity<ReplenishMaterialTableEntity>> listReplenishMaterialTables(@Url String url, @Body Map<String, Object> paramMap);

}
