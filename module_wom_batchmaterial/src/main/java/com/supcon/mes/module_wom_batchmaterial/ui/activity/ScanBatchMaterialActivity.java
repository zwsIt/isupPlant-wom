package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialSetListAPI;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialSetListContract;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialSetListPresenter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @author created by zhangwenshuai1
 * @desc 配料快速扫描补料桶
 * @date 2021/5/17
 */
@Router(value = Constant.AppCode.WOM_BATCH_MATERIAL_SCAN)
@Presenter(BatchMaterialSetListPresenter.class)
@Controller(value = {CommonScanController.class})
public class ScanBatchMaterialActivity extends BaseControllerActivity implements BatchMaterialSetListContract.View {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("titleSetting")
    ImageView titleSetting;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;

    ArrayMap<String, Object> queryParams = new ArrayMap<>(1);

    @Override
    protected int getLayoutID() {
        return R.layout.wom_scan_bucket;
    }

    @Override
    protected void onInit() {
        super.onInit();
        titleText.setText(context.getResources().getString(R.string.batch_quick_scan_bucket));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.ic_scan);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        EventBus.getDefault().register(this);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        leftBtn.setOnClickListener(v -> onBackPressed());

        RxView.clicks(rightBtn)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void accept(Object o) throws Exception {
                        getController(CommonScanController.class).openCameraScan(this.getClass().getSimpleName());
                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();
        getController(CommonScanController.class).openCameraScan(this.getClass().getSimpleName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (this.getClass().getSimpleName().equals(codeResultEvent.scanTag)) {
            if (!StringUtil.isEmpty(codeResultEvent.scanResult)) {
                QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);
                if (qrCodeEntity != null && qrCodeEntity.getType() == 1) {
                    onLoading(getString(R.string.wom_dealing));
                    queryParams.put(Constant.BAPQuery.CODE, qrCodeEntity.getCode());
                    presenterRouter.create(BatchMaterialSetListAPI.class).listBatchMaterialSets(1, null,false, queryParams);
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listBatchMaterialSetsSuccess(CommonBAP5ListEntity entity) {
        onLoadSuccess();
        if (entity.data.result != null && entity.data.result.size() > 0) {
            Bundle bundle = new Bundle();
            BatchMaterialSetEntity batchMaterialSetEntity = (BatchMaterialSetEntity) entity.data.result.get(0);
            bundle.putSerializable(BmConstant.IntentKey.BATCH_MATERIAL_SET,batchMaterialSetEntity);
            // 判断是否已经配料完成
            if (BmConstant.SystemCode.TASK_TRANSPORT.equals(batchMaterialSetEntity.getFmTask().id)){

            }else {
                IntentRouter.go(context,BmConstant.Router.BATCH_MATERIAL_INSTRUCTION_LIST,bundle);
            }
//            bundle.putSerializable(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE, batchMaterialSetEntity);
//            IntentRouter.go(context, ReplenishConstant.Router.REPLENISH_MATERIAL_SCAN,bundle);
        } else {
            ToastUtils.show(context, getString(R.string.batch_no_match_bucket));
        }

    }

    @Override
    public void listBatchMaterialSetsFailed(String errorMsg) {
        onLoadFailed(errorMsg);
        ToastUtils.show(context, errorMsg);
    }
}
