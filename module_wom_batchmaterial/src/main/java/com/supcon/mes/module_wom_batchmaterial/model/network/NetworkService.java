package com.supcon.mes.module_wom_batchmaterial.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配料管理网络请求URL接口
 */
@ApiFactory(name = "BmHttpClient")
public interface NetworkService {
    /**
     * 配料指令编辑提交
     * @return
     */
    @POST("/msService/WOM/batchMaterial/batchMateril/batchMaterialOrder/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> batchMaterialOrderSubmit(@Query("id") Long id, @Query("__pc__") String __pc__, @Body BatchMaterialInstructionDTO batchMaterialInstructionDTO);

    /**
     * 配料记录提交：配料完成、撤回、派送、配放
     * @return
     */
    @POST("/msService/WOM/batchMaterial/mobile/pdaChangeRecord")
    Flowable<BAP5CommonEntity<Object>> batchMaterialRecordsSubmit(@QueryMap Map<String, Object> paramsMap);

    /**
     * 配料记录提交：签收/拒签
     * @return
     */
//    @POST("/msService/WOM/batchMaterial/batMaterilPart/signFor")
    @POST("/msService/WOMmobile/batchMaterial/mobile/pdaSignforRecord")// 人为接口
    Flowable<BAP5CommonEntity<Object>> batchMaterialRecordsSignSubmit(@Body BatchMaterialRecordsSignSubmitDTO dto);

    /**
     * 配料记录提交：退废
     * @return
     */
    @POST("/msService/WOM/batchMaterial/batMaterilPart/retirement")
    Flowable<BAP5CommonEntity<Object>> batchMaterialRecordsRetirement(@Body  List<BatchMaterialPartEntity> list /*BatchMaterialRecordsSignSubmitDTO dto*/);

}
