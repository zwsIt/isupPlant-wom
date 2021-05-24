package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.CommonBaseEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialSetListAPI;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialSetListContract;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchTrunkScanContract;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/05/22
 * Email zhangwenshuai1@supcon.com
 * Desc 配料中继位扫码
 */
@Router(value = BmConstant.Router.BATCH_TRUNK_AREA_SCAN)
@Presenter(value = {/*ReplenishMaterialTableScanPresenter.class*/})
@Controller(value = {CommonScanController.class})
public class BatchTrunkAreaScanActivity extends BaseControllerActivity implements BatchMaterialSetListContract.View, BatchTrunkScanContract.View {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("titleSetting")
    ImageView titleSetting;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleLayout")
    RelativeLayout titleLayout;
    @BindByTag("batchLine")
    CustomTextView batchLine;
    @BindByTag("batchIndex")
    CustomTextView batchIndex;
    @BindByTag("trunkCode")
    CustomTextView trunkCode;
    @BindByTag("workLine")
    CustomTextView workLine;
    @BindByTag("trunkPassIv")
    ImageView trunkPassIv;
    @BindByTag("submitBtn")
    Button submitBtn;
    private BatchMaterialSetEntity mBatchMaterialSetEntity;
    ArrayMap<String, Object> queryParams = new ArrayMap<>();

    @Override
    protected int getLayoutID() {
        return R.layout.batch_ac_trunk_area_scan;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mBatchMaterialSetEntity = (BatchMaterialSetEntity) getIntent().getSerializableExtra(BmConstant.IntentKey.BATCH_MATERIAL_SET);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.batch_bucket_scan_into));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));

        batchLine.setContent(mBatchMaterialSetEntity.getNextBurendManage().getCode());
        batchIndex.setContent(String.valueOf(mBatchMaterialSetEntity.getFmOrder()));

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
        });

        RxView.clicks(submitBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        showDialogOperate(0);
                    }
                });

    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2021/5/12
     * @description 配料单扫描开始结束
     */
    private void showDialogOperate(int i) {
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT)
                .bindView(R.id.tipContentTv, getString(R.string.batch_pass_make_sure_into));
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        customDialog.bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {
                    onLoading(getString(R.string.wom_dealing));
//                    ReplenishMaterialScanDTO replenishMaterialScanDTO = new ReplenishMaterialScanDTO();
//                    ReplenishMaterialTableEntity replenishMaterialTableEntity = new ReplenishMaterialTableEntity();
//                    replenishMaterialTableEntity.setId(mReplenishMaterialTableEntity.getId());
//                    replenishMaterialTableEntity.setEqScanFlag(true);
//                    replenishMaterialTableEntity.setVesselScanFlag(true);
//                    replenishMaterialScanDTO.setFmBill(replenishMaterialTableEntity);
//                    if (i == 0) {
//                        replenishMaterialScanDTO.setState("start");
//                    } else {
//                        replenishMaterialScanDTO.setState("complete");
//                    }
//                    List<ReplenishMaterialTablePartEntity> list = new ArrayList<>();
//                    list.addAll(mReplenishMaterialRecordsScanAdapter.getList());
//                    replenishMaterialScanDTO.setFmBillDetais(list);
//
//                    presenterRouter.create(ReplenishMaterialTableScanAPI.class).submit(replenishMaterialScanDTO);
                }, true)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(errorMsg);
        ToastUtils.show(context, errorMsg);
    }

    /**
     * 物料是否全部扫描通过
     */
    private int scanPassCount;
    /**
     * 循环次数
     */
    private int count;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getScanResult(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)) {

            QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);

            if (qrCodeEntity != null) {
                switch (qrCodeEntity.getType()) {
                    // 扫描设备
                    case 0:
                        break;
                    // 扫描桶
                    case 1:
                        onLoading(getString(R.string.wom_dealing));
                        queryParams.put(Constant.BAPQuery.CODE, qrCodeEntity.getCode());
                        presenterRouter.create(BatchMaterialSetListAPI.class).listBatchMaterialSets(1, null,false, queryParams);
                        break;
                    // 扫描物料
                    case 2:
                        // 目前暂时按照MES产品定义格式
                       break;
                    // 中继位
                    case 3:
                        if (qrCodeEntity.getPlx().equals(mBatchMaterialSetEntity.getNextBurendManage().getCode())){
                            trunkCode.setContent(qrCodeEntity.getCode());
                            workLine.setContent(qrCodeEntity.getPlx());
                            trunkPassIv.setImageResource(R.drawable.replenish_ic_pass);
                            submitBtn.setEnabled(true);
                            submitBtn.setAlpha(1);
                        }else {
                            trunkCode.setContent(null);
                            workLine.setContent(null);
                            trunkPassIv.setImageResource(R.drawable.replenish_ic_no_pass);
                            submitBtn.setEnabled(false);
                            submitBtn.setAlpha(0.3f);
                        }
                        break;
                    default:
                }
            }

        }
    }

    @Override
    public void listBatchMaterialSetsSuccess(CommonBAP5ListEntity entity) {
        onLoadSuccess();
        if (entity.data.result != null && entity.data.result.size() > 0) {
            BatchMaterialSetEntity batchMaterialSetEntity = (BatchMaterialSetEntity) entity.data.result.get(0);
            // 判断是否已经配料完成
            if (BmConstant.SystemCode.TASK_TRANSPORT.equals(batchMaterialSetEntity.getFmTask().id)){
                batchLine.setContent(batchMaterialSetEntity.getNextBurendManage().getCode());
                batchIndex.setContent(String.valueOf(batchMaterialSetEntity.getFmOrder()));
            }else {
                ToastUtils.show(context, getString(R.string.batch_ing_please_end_first));
            }
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
