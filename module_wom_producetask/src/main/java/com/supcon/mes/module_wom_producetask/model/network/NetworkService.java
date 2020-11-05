package com.supcon.mes.module_wom_producetask.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogListEntity;
import com.supcon.mes.module_wom_producetask.model.bean.TaskActiveEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.dto.BatchPutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.OutputDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.ProCheckDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 工单管理网络请求URL接口
 */
@ApiFactory(name = "WomHttpClient")
public interface NetworkService {
    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/23
     * @description 工单待办记录list
     */
    @POST("/msService/WOM/waitPutinRecord/waitPutRecord/waitPutTaskList-query")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<WaitPutinRecordEntity>>> waitPutTaskList(@Body Map<String, Object> paramMap);

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/23
     * @description 工序待办记录list
     */
    @POST("/msService/WOM/waitPutinRecord/waitPutRecord/waitPutProcessList-query")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<WaitPutinRecordEntity>>> waitPutProcessList(@Body Map<String, Object> paramMap);

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/23
     * @description 活动待办记录list
     */
    @POST("/msService/WOM/waitPutinRecord/waitPutRecord/waitPutActiveList-query")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<WaitPutinRecordEntity>>> waitPutActiveList(@Body Map<String, Object> paramMap);

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/25
     * @description 指令单操作
     */
    @POST("/msService/WOMmobile/produceTask/mobile/taskOperate")
    Flowable<BAP5CommonEntity<Object>> taskOperate(@Body Map<String, Object> paramMap/*,@Query(value = "waitPutRecordId") Long waitPutRecordId, @Query(value = "operateType") String operateType, @Body List<OutputDetailEntity> list*/);

    /**
     * @author zhangwenshuai1 2020/3/27
     * @param
     * @return
     * @description 指令单工序操作：开/关
     *
     */
    @POST("/msService/WOMmobile/produceTask/mobile/taskProcessOperate")
    Flowable<BAP5CommonEntity<Object>> taskProcessOperate(@Query(value = "waitPutRecordId") Long waitPutRecordId, @Query(value = "isFinish") boolean isFinish);

    /**
     * 工厂架构参照：层次模型->工厂架构->factoryUnitRef2视图
     * @param paramMap
     * @return
     */
    @GET(" /msService/WOM/produceTask/produceTask/unitGroupAndUnit/2/{productLineId}/{processId}")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<FactoryModelEntity>>> factoryUnitRef2Query(@Path("productLineId") String productLineId, @Path("processId") String processId,@QueryMap Map<String,Object> paramMap);

    /**
     * 工序工作单元update
     * @param id 指令单工序id
     * @param submitMap
     * @return
     */
    @POST("/msService/WOM/produceTask/taskProcess/processUnitEdit/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> submit(@Query("id") Long id, @Query("__pc__") String __pc__, @Body Map<String, Object> submitMap);

    /**
     * 通用list
     * @param paramMap
     * @return
     */
    @POST()
    Flowable<BAP5CommonEntity<Object>> list(@Url String url, @Body Map<String, Object> paramMap);

    /**
     * @author zhangwenshuai1 2020/4/8
     * @param
     * @return
     * @description 活动操作：开始/结束
     *
     */
    @POST("/msService/WOMmobile/produceTask/mobile/commonActiveReport")
    Flowable<BAP5CommonEntity<Object>> activityOperate(@Query(value = "waitPutRecordId") Long waitPutRecordId,@Query(value = "qualityActiveId") Long qualityActiveId, @Query(value = "isFinish") boolean isFinish);

    /**
     * 检查活动报工提交
     * @return
     */
    @POST("/msService/WOM/procReport/procReport/checkFeedBackEdit/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> checkItemReportSubmit(@Query("id") Long id, @Query("__pc__") String __pc__,@Body ProCheckDetailDTO proCheckDetailDTO);
    /**
     * 投料活动报工提交
     * @return
     */
    @POST("/msService/WOM/procReport/procReport/putInFeedBackEdit/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> putInReportSubmit(@Query("id") Long id, @Query("__pc__") String __pc__,@Body PutinDetailDTO putinDetailDTO);
    /**
     * 投配料活动报工提交
     * @return
     */
    @POST("/msService/WOM/procReport/procReport/batchFeedBackEdit/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> batchPutInReportSubmit(@Query("id") Long id, @Query("__pc__") String __pc__,@Body BatchPutinDetailDTO batchPutinDetailDTO);
    /**
     * 产出活动报工提交
     * @return
     */
    @POST("/msService/WOM/procReport/procReport/outputFeedBackEdit/submit")
    Flowable<BAP5CommonEntity<BapResultEntity>> outputReportSubmit(@Query("id") Long id, @Query("__pc__") String __pc__,@Body OutputDetailDTO outputDetailDTO);

    /**
     * 活动执行记录
     * @param params
     * @return
     */
    @POST("/msService/WOM/produceTask/actiExelog/activeExeLogList-query")
    Flowable<ActiExelogListEntity> listActivityExeLog(@Body Map<String,Object> params);

    /**
     * 调整活动：发起请检
     * @param activeId
     * @return
     */
    @GET("/msService/WOM/produceTask/produceTask/adjustFinish")
    Flowable<BAP5CommonEntity> adjustFinish(@Query("ajustActiveId") Long activeId);

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/25
     * @description 指令单提前放料操作
     */
    @GET("/msService/WOM/produceTask/produceTask/setAdvanceTrue/{taskId}")
    Flowable<BAP5CommonEntity> operateDischarge(@Path(value = "taskId") Long taskId);

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/10/26
     * @description 尾料二维码获取物料信息
     */
    @GET("/msService/WOM/remainMaterial/remainMaterial/getRemainMaterialJSON")
    Flowable<BAP5CommonEntity<MaterialQRCodeEntity>> getRemainMaterialJSON(@Query("pk") Long id);
    /**
     * 根据工序获取活动
     * @param paramMap
     * @return
     */
    @POST("/msService/WOM/produceTask/taskActive/queryByProcess")
    Flowable<BAP5CommonEntity<CommonBAPListEntity<TaskActiveEntity>>> listActivities(@QueryMap Map<String,Object> paramMap, @Body Map<String,Object> params);

}
