package com.supcon.mes.module_wom_replenishmaterial.model.network;

import com.app.annotation.apt.ApiFactory;
import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    @POST("/msService/WOM/fillMaterialNotice/fmnNotice/fmnNoticeList-query")
    Flowable<CommonBAP5ListEntity<ReplenishMaterialNotifyEntity>> listReplenishMaterialNotifies(@Body Map<String, Object> paramMap);
}
