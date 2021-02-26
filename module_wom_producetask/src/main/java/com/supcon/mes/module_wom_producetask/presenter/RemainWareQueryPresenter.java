package com.supcon.mes.module_wom_producetask.presenter;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_wom_producetask.model.bean.RemainWareEntity;
import com.supcon.mes.module_wom_producetask.model.contract.RemainQRCodeContract;
import com.supcon.mes.module_wom_producetask.model.contract.RemainWareQueryContract;
import com.supcon.mes.module_wom_producetask.model.network.WomHttpClient;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/25
 * Email zhangwenshuai1@supcon.com
 * Desc 根据尾料id获取仓库信息
 */
public class RemainWareQueryPresenter extends RemainWareQueryContract.Presenter {

    @Override
    public void getWareByRemainId(Long id) {
        mCompositeSubscription.add(
                WomHttpClient.getWorkAreaId(id)
                        .onErrorReturn(new Function<Throwable, BAP5CommonEntity<RemainWareEntity>>() {
                            @Override
                            public BAP5CommonEntity<RemainWareEntity> apply(Throwable throwable) throws Exception {
                                BAP5CommonEntity bap5CommonEntity = new BAP5CommonEntity();
                                bap5CommonEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                                bap5CommonEntity.success = false;
                                return bap5CommonEntity;
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<RemainWareEntity>>() {
                            @Override
                            public void accept(BAP5CommonEntity<RemainWareEntity> bap5CommonEntity) throws Exception {
                                if (bap5CommonEntity.success) {
                                    getView().getWareByRemainIdSuccess(bap5CommonEntity);
                                } else {
                                    getView().getWareByRemainIdFailed(bap5CommonEntity.msg);
                                }
                            }
                        })
        );
    }
}
