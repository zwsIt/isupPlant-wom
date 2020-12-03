package com.supcon.mes.module_wom_producetask.presenter;


import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.model.bean.JoinSubcondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class BatchMaterialRecordsRefPresenter extends CommonListContract.Presenter {
    @Override
    public void list(int pageNo, Map<String, Object> customCondition, Map<String, Object> queryParams, String url, String modelAlias) {
        FastQueryCondEntity fastQueryCondEntity = new FastQueryCondEntity();
        fastQueryCondEntity.modelAlias = modelAlias;
        fastQueryCondEntity.subconds = new ArrayList<>();
        JoinSubcondEntity joinSubcondEntity = BAPQueryParamsHelper.crateJoinSubcondEntity(queryParams,"BASESET_MATERIALS,ID,WOM_BAT_MATERIL_PARTS,MATERIAL_ID");
        fastQueryCondEntity.subconds.add(joinSubcondEntity);

        Map<String, Object> requestParamsMap = ListRequestParamUtil.getQueryParam(pageNo,20,true,fastQueryCondEntity,customCondition);
        mCompositeSubscription.add(
                WomHttpClient.list(url, requestParamsMap)
                        .onErrorReturn(new Function<Throwable, BAP5CommonEntity<Object>>() {
                            @Override
                            public BAP5CommonEntity<Object> apply(Throwable throwable) throws Exception {
                                BAP5CommonEntity<Object> bap5CommonEntity = new BAP5CommonEntity<>();
                                bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                                bap5CommonEntity.success =false;
                                return bap5CommonEntity;
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<Object>>() {
                            @Override
                            public void accept(BAP5CommonEntity<Object> objectBAP5CommonEntity) throws Exception {
                                if (objectBAP5CommonEntity.success){
                                    getView().listSuccess(objectBAP5CommonEntity);
                                }else {
                                    getView().listFailed(objectBAP5CommonEntity.msg);
                                }
                            }
                        })
        );
    }
}
