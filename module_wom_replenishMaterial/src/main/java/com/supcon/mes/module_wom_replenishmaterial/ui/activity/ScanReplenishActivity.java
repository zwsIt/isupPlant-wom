package com.supcon.mes.module_wom_replenishmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_replenishmaterial.IntentRouter;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableListAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableListContract;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.functions.Consumer;

/**
 * @author created by zhangwenshuai1
 * @desc 补料快速扫描补料桶
 * @date 2021/5/17
 */
@Router(value = Constant.AppCode.WOM_ReplenishMaterial_Scan)
@Presenter(ReplenishMaterialTableListPresenter.class)
@Controller(value = {CommonScanController.class})
public class ScanReplenishActivity extends BaseControllerActivity implements ReplenishMaterialTableListContract.View {

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
        return R.layout.replenish_scan_bucket;
    }

    @Override
    protected void onInit() {
        super.onInit();
        titleText.setText(context.getResources().getString(R.string.replenish_quick_scan_bucket));
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
                    presenterRouter.create(ReplenishMaterialTableListAPI.class).listReplenishMaterialTables(1, ReplenishConstant.URL.REPLENISH_MATERIAL_SCAN_LIST_URL, false, queryParams);
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
    public void listReplenishMaterialTablesSuccess(CommonBAP5ListEntity entity) {
        onLoadSuccess();
        if (entity.data.result != null && entity.data.result.size() > 0) {
            Bundle bundle = new Bundle();
            ReplenishMaterialTableEntity replenishMaterialTableEntity = (ReplenishMaterialTableEntity) entity.data.result.get(0);
            bundle.putSerializable(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE, replenishMaterialTableEntity);
            IntentRouter.go(context, ReplenishConstant.Router.REPLENISH_MATERIAL_SCAN,bundle);
        } else {
            ToastUtils.show(context, getString(R.string.replenish_no_match_bucket));
        }

    }

    @Override
    public void listReplenishMaterialTablesFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
    }
}
