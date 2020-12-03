package com.supcon.mes.module_wom_rejectmaterial.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.module_wom_rejectmaterial.model.dto.RejectMaterialDTO;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 工单退料管理网络请求URL接口
 */
@ApiFactory(name = "RmHttpClient")
public interface NetworkService {
    /**
     * 退料编辑提交
     * @return
     */
    @POST("{path}/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> submit(@Path(value = "path",encoded = true) String path, @Query("id") Long id, @Query("__pc__") String __pc__, @Body RejectMaterialDTO dto);
}
