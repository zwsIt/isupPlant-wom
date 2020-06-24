package com.supcon.mes.module_wom_preparematerial.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PrepareMaterialEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 备料管理网络请求URL接口
 */
@ApiFactory(name = "WomHttpClient")
public interface NetworkService {

    @POST("/msService/WOM/prePraOrder/prepMatralReco/recordingReceiveList-query")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<PreMaterialEntity>>> getPreMaterialReceiveList(@Body Map<String, Object> pageMap);

}
