package com.supcon.mes.module_wom_producetask.presenter;


import android.text.TextUtils;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.middleware.util.ListRequestParamUtil;
import com.supcon.mes.module_wom_producetask.model.bean.TaskActiveEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.ListAllActivityContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ActivityProcessListPresenter extends ListAllActivityContract.Presenter {

    @Override
    public void listActivities(Map<String, Object> customCondition, Map<String, Object> queryParams) {
        mCompositeSubscription.add(
                WomHttpClient.listActivities(queryParams,customCondition)
                .onErrorReturn(new Function<Throwable, BAP5CommonEntity<CommonBAPListEntity<TaskActiveEntity>>>() {
                    @Override
                    public BAP5CommonEntity<CommonBAPListEntity<TaskActiveEntity>> apply(Throwable throwable) throws Exception {
                        BAP5CommonEntity bap5CommonEntity = new BAP5CommonEntity();
                        bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                        bap5CommonEntity.success = false;
                        return bap5CommonEntity;
                    }
                })
                .subscribe(new Consumer<BAP5CommonEntity<CommonBAPListEntity<TaskActiveEntity>>>() {
                    @Override
                    public void accept(BAP5CommonEntity<CommonBAPListEntity<TaskActiveEntity>> commonBAPListEntityBAP5CommonEntity) throws Exception {
                        if (commonBAPListEntityBAP5CommonEntity.success) {
                            ActivityProcessListPresenter.this.getView().listActivitiesSuccess(commonBAPListEntityBAP5CommonEntity);
                        } else {
                            ActivityProcessListPresenter.this.getView().listActivitiesFailed(commonBAPListEntityBAP5CommonEntity.msg);
                        }
                    }
                })
        );
    }
}
