package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogListEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityExeLogListContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ActiExeLogListPresenter extends ActivityExeLogListContract.Presenter {
    @Override
    public void listActivityExeLog(int pageIndex, String produceBatchNum) {

        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("pageNo",pageIndex);
        pageQueryParams.put("pageSize",20);
        pageQueryParams.put("maxPageSize",500);
        pageQueryParams.put("paging",true);

        Map<String, Object> condQueryParams = new HashMap<>();
        BAPQueryParamsHelper.setLike(true);
        condQueryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM,produceBatchNum);
        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(condQueryParams);
        fastQueryCondEntity.modelAlias = "actiExelog";
        fastQueryCondEntity.viewCode="WOM_1.0.0_produceTask_activeExeLogList";


        pageQueryParams.put("fastQueryCond", fastQueryCondEntity.toString());
        mCompositeSubscription.add(
                WomHttpClient.listActivityExeLog(pageQueryParams)
                .onErrorReturn(new Function<Throwable, ActiExelogListEntity>() {
                    @Override
                    public ActiExelogListEntity apply(Throwable throwable) throws Exception {
                        ActiExelogListEntity actiExelogListEntity = new ActiExelogListEntity();
                        actiExelogListEntity.msg = throwable.toString();
                        return actiExelogListEntity;
                    }
                })
                .subscribe(new Consumer<ActiExelogListEntity>() {
                    @Override
                    public void accept(ActiExelogListEntity actiExelogListEntity) throws Exception {
                        if (actiExelogListEntity.success ){
                            getView().listActivityExeLogSuccess(actiExelogListEntity);
                        }else {
                            getView().listActivityExeLogFailed(actiExelogListEntity.msg);
                        }
                    }
                })
        );

    }
}
