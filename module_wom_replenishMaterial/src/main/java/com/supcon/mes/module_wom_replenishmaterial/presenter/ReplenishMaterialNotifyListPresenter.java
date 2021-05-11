package com.supcon.mes.module_wom_replenishmaterial.presenter;

import android.util.ArrayMap;

import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.FastQueryCondEntity;
import com.supcon.mes.middleware.util.BAPQueryParamsHelper;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogListEntity;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialNotifyListContract;
import com.supcon.mes.module_wom_replenishmaterial.model.network.HttpClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc
 */
public class ReplenishMaterialNotifyListPresenter extends ReplenishMaterialNotifyListContract.Presenter {

    @Override
    public void listReplenishMaterialNotifies(int pageIndex, ArrayMap<String, Object> queryMap) {
        Map<String, Object> pageQueryParams = new HashMap<>();
        pageQueryParams.put("pageNo",pageIndex);
        pageQueryParams.put("pageSize",20);
        pageQueryParams.put("maxPageSize",500);
        pageQueryParams.put("paging",true);

        FastQueryCondEntity fastQueryCondEntity = BAPQueryParamsHelper.createSingleFastQueryCond(queryMap);
        fastQueryCondEntity.modelAlias = "fmnNotice";
        fastQueryCondEntity.viewCode="WOM_1.0.0_fillMaterialNotice_fmnNoticeList";


        pageQueryParams.put("fastQueryCond", fastQueryCondEntity.toString());
        mCompositeSubscription.add(
                HttpClient.listReplenishMaterialNotifies(pageQueryParams)
                        .onErrorReturn(throwable -> {
                            CommonBAP5ListEntity<ReplenishMaterialNotifyEntity> commonBAP5ListEntity = new CommonBAP5ListEntity<>();
                            commonBAP5ListEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return commonBAP5ListEntity;
                        })
                        .subscribe(replenishMaterialNotifyEntityCommonBAP5ListEntity -> {
                            if (replenishMaterialNotifyEntityCommonBAP5ListEntity.success ){
                                getView().listReplenishMaterialNotifiesSuccess(replenishMaterialNotifyEntityCommonBAP5ListEntity);
                            }else {
                                getView().listReplenishMaterialNotifiesFailed(replenishMaterialNotifyEntityCommonBAP5ListEntity.msg);
                            }
                        })
        );
    }
}
