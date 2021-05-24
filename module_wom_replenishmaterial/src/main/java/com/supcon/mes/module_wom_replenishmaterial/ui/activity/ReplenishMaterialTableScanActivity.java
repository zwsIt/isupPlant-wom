package com.supcon.mes.module_wom_replenishmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
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
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;
import com.supcon.mes.module_wom_replenishmaterial.IntentRouter;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableScanAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.AssociatedEquipmentEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTablePartEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableScanContract;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialScanDTO;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableScanPresenter;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.ReplenishMaterialRecordsScanAdapter;

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
 * Created by zhangwenshuai1 on 2021/05/13
 * Email zhangwenshuai1@supcon.com
 * Desc 补料扫码
 */
@Router(value = ReplenishConstant.Router.REPLENISH_MATERIAL_SCAN/*, viewCode = ReplenishConstant.ViewCode.REPLENISH_MATERIAL_EDIT*/)
@Presenter(value = {CommonListPresenter.class, ReplenishMaterialTableScanPresenter.class})
//@PowerCode(entityCode = BmConstant.PowerCode.BM_INSTRUCTION_EDIT)
@Controller(value = {WorkFlowViewController.class, GetPowerCodeController.class, CommonScanController.class})
public class ReplenishMaterialTableScanActivity extends BaseRefreshRecyclerActivity<ReplenishMaterialTablePartEntity> implements CommonListContract.View, ReplenishMaterialTableScanContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("customListWidgetName")
    TextView customListWidgetName;
    @BindByTag("customListWidgetEdit")
    ImageView customListWidgetEdit;
    @BindByTag("customListWidgetAdd")
    ImageView customListWidgetAdd;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("startBtn")
    Button startBtn;
    @BindByTag("endBtn")
    Button endBtn;
    @BindByTag("eamPoint")
    CustomTextView eamPoint;
    @BindByTag("material")
    CustomTextView material;
    @BindByTag("planNum")
    CustomEditText planNum;
    @BindByTag("actualNum")
    CustomEditText actualNum;
    @BindByTag("bucket")
    CustomTextView bucket;
    @BindByTag("customListWidgetIc")
    ImageView customListWidgetIc;
    @BindByTag("eamPassIv")
    ImageView eamPassIv;
    @BindByTag("bucketPassIv")
    ImageView bucketPassIv;
    @BindByTag("bucketRl")
    RelativeLayout bucketRl;

    private ReplenishMaterialTableEntity mReplenishMaterialTableEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private ReplenishMaterialRecordsScanAdapter mReplenishMaterialRecordsScanAdapter;

    @Override
    protected IListAdapter<ReplenishMaterialTablePartEntity> createAdapter() {
        mReplenishMaterialRecordsScanAdapter = new ReplenishMaterialRecordsScanAdapter(context);
        return mReplenishMaterialRecordsScanAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.replenish_ac_replenish_material_table_scan;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mReplenishMaterialTableEntity = (ReplenishMaterialTableEntity) getIntent().getSerializableExtra(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(1, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

        if (mReplenishMaterialTableEntity.getVessel() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getVessel().getCode())) {
            mReplenishMaterialRecordsScanAdapter.setBucket(false);
        }

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.replenish_material_table_scan));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));

        planNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        actualNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (mReplenishMaterialTableEntity.getVessel() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getVessel().getCode())) {
            bucketRl.setVisibility(View.GONE);
        }
        eamPoint.setEditable(false);
        material.setEditable(false);
        planNum.setEditable(false);
        bucket.setEditable(false);

        // 是否已经开始
        if (mReplenishMaterialTableEntity.isEqScanFlag()) {
            eamPassIv.setImageResource(R.drawable.replenish_ic_pass);
        }
        if (mReplenishMaterialTableEntity.isVesselScanFlag()) {
            bucketPassIv.setImageResource(R.drawable.replenish_ic_pass);
        }

        customListWidgetName.setText(context.getResources().getString(R.string.replenish_material_records));
        customListWidgetAdd.setVisibility(View.GONE);
        customListWidgetEdit.setVisibility(View.GONE);

        // 补料中状态 结束按钮可操作
        if (ReplenishConstant.SystemCode.TABLE_STATE_ING.equals(mReplenishMaterialTableEntity.getFmState().id)) {
            endBtn.setEnabled(true);
            endBtn.setAlpha(1);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (mReplenishMaterialTableEntity.getEquipment() != null && !TextUtils.isEmpty(mReplenishMaterialTableEntity.getEquipment().getCode())) {
            eamPoint.setContent(mReplenishMaterialTableEntity.getEquipment().getName() + "(" + mReplenishMaterialTableEntity.getEquipment().getCode() + ")");
        }
        if (mReplenishMaterialTableEntity.getMaterial() != null && !TextUtils.isEmpty(mReplenishMaterialTableEntity.getMaterial().code)) {
            material.setContent(mReplenishMaterialTableEntity.getMaterial().name + "(" + mReplenishMaterialTableEntity.getMaterial().code + ")");
        }
        if (mReplenishMaterialTableEntity.getPlanNumber() != null) {
            planNum.setContent(mReplenishMaterialTableEntity.getPlanNumber().toString());
        }
        if (mReplenishMaterialTableEntity.getActualNumber() != null) {
            actualNum.setContent(mReplenishMaterialTableEntity.getActualNumber().toString());
        }
        if (mReplenishMaterialTableEntity.getVessel() != null && !TextUtils.isEmpty(mReplenishMaterialTableEntity.getVessel().getCode())) {
            bucket.setContent(mReplenishMaterialTableEntity.getVessel().getName() + "(" + mReplenishMaterialTableEntity.getVessel().getCode() + ")");
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
        });

        refreshListController.setOnRefreshListener(() -> presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                ReplenishConstant.URL.REPLENISH_MATERIAL_TABLE_EDIT_DG_LIST_URL + "&id=" + mReplenishMaterialTableEntity.getId(), ""));

        eamPoint.setOnChildViewClickListener((childView, action, obj) -> IntentRouter.go(context, ReplenishConstant.Router.ASSOCIATION_EQUIPMENT_LIST_REF));
        material.setOnChildViewClickListener((childView, action, obj) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IntentKey.SINGLE_CHOICE, true);
            IntentRouter.go(context, Constant.Router.PRODUCT_DETAIL, bundle);
        });
        RxTextView.textChanges(planNum.editText())
                .skipInitialValue()
                .filter(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString())) {
                        mReplenishMaterialTableEntity.setPlanNumber(null);
                        return false;
                    }
                    if (charSequence.toString().startsWith(".")) {
                        planNum.editText().setText("0.");
                        return false;
                    }
                    return true;
                })
                .subscribe(charSequence -> mReplenishMaterialTableEntity.setPlanNumber(new BigDecimal(charSequence.toString())));
        RxTextView.textChanges(actualNum.editText())
                .skipInitialValue()
                .filter(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString())) {
                        mReplenishMaterialTableEntity.setActualNumber(null);
                        return false;
                    }
                    if (charSequence.toString().startsWith(".")) {
                        actualNum.editText().setText("0.");
                        return false;
                    }
                    return true;
                })
                .subscribe(charSequence -> mReplenishMaterialTableEntity.setActualNumber(new BigDecimal(charSequence.toString())));
        bucket.setOnChildViewClickListener((childView, action, obj) -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IntentKey.URL, "/msService/WOM/vesselMg/vessel/vesselRef-query");
            bundle.putString(Constant.IntentKey.MODEL_ALIAS, "vessel");
            IntentRouter.go(context, Constant.Router.COMMON_LIST_REF, bundle);
        });
        customListWidgetAdd.setOnClickListener(v -> {
            ReplenishMaterialTablePartEntity replenishMaterialTablePartEntity = new ReplenishMaterialTablePartEntity();
//            replenishMaterialTablePartEntity.setMaterialId(mReplenishMaterialTableEntity.getMaterial()); // 物料
            replenishMaterialTablePartEntity.setFmTime(System.currentTimeMillis());

            if (mReplenishMaterialRecordsScanAdapter.getItemCount() <= 0) {
                mReplenishMaterialRecordsScanAdapter.addData(replenishMaterialTablePartEntity);
            } else {
                mReplenishMaterialRecordsScanAdapter.getList().add(0, replenishMaterialTablePartEntity);
            }
            mReplenishMaterialRecordsScanAdapter.notifyItemRangeInserted(0, 1);
            mReplenishMaterialRecordsScanAdapter.notifyItemRangeChanged(0, 1);
            contentView.smoothScrollToPosition(0);

        });
//        mReplenishMaterialRecordsScanAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
//            mCurrentPosition = position;
//            mReplenishMaterialTablePartEntity = (ReplenishMaterialTablePartEntity) obj;
//            switch (childView.getTag().toString()) {
//                case "itemViewDelBtn":
//                    mReplenishMaterialRecordsScanAdapter.getList().remove(obj);
//                    mReplenishMaterialRecordsScanAdapter.notifyItemRangeRemoved(position, 1);
//                    mReplenishMaterialRecordsScanAdapter.notifyItemRangeChanged(position, mReplenishMaterialRecordsScanAdapter.getItemCount() - position);
//                    if (mReplenishMaterialTablePartEntity.getId() != null) {
//                        dgDeletedIds += mReplenishMaterialTablePartEntity.getId() + ",";
//                    }
//                    break;
//                default:
//            }
//        });
        RxView.clicks(startBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        showDialogOperate(0);
                    }
                });
        RxView.clicks(endBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        showDialogOperate(1);
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
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (i == 0) {
            customDialog.bindView(R.id.tipContentTv, getString(R.string.replenish_start_operate_open_valve));
        } else {
            customDialog.bindView(R.id.tipContentTv, getString(R.string.replenish_end_operate));
        }
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        customDialog.bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {
                    onLoading(getString(R.string.wom_dealing));
                    ReplenishMaterialScanDTO replenishMaterialScanDTO = new ReplenishMaterialScanDTO();
                    ReplenishMaterialTableEntity replenishMaterialTableEntity = new ReplenishMaterialTableEntity();
                    replenishMaterialTableEntity.setId(mReplenishMaterialTableEntity.getId());
                    replenishMaterialTableEntity.setEqScanFlag(true);
                    replenishMaterialTableEntity.setVesselScanFlag(true);
                    replenishMaterialScanDTO.setFmBill(replenishMaterialTableEntity);
                    if (i == 0) {
                        replenishMaterialScanDTO.setState("start");
                    } else {
                        replenishMaterialScanDTO.setState("complete");
                    }
                    List<ReplenishMaterialTablePartEntity> list = new ArrayList<>();
                    list.addAll(mReplenishMaterialRecordsScanAdapter.getList());
                    replenishMaterialScanDTO.setFmBillDetais(list);

                    presenterRouter.create(ReplenishMaterialTableScanAPI.class).submit(replenishMaterialScanDTO);
                }, true)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), ReplenishMaterialTablePartEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof AssociatedEquipmentEntity) {
            AssociatedEquipmentEntity associatedEquipmentEntity = (AssociatedEquipmentEntity) selectDataEvent.getEntity();
            eamPoint.setContent(associatedEquipmentEntity.getName() + "(" + associatedEquipmentEntity.getCode() + ")");
            mReplenishMaterialTableEntity.setEquipment(associatedEquipmentEntity);
            // 物料
            if (associatedEquipmentEntity.getProductId() != null && !TextUtils.isEmpty(associatedEquipmentEntity.getProductId().code)) {
                material.setContent(associatedEquipmentEntity.getProductId().name + "(" + associatedEquipmentEntity.getProductId().code + ")");
                mReplenishMaterialTableEntity.setMaterial(associatedEquipmentEntity.getProductId());
            }
        } else if (selectDataEvent.getEntity() instanceof Good) {
            Good good = (Good) selectDataEvent.getEntity();
            material.setContent(good.name + "(" + good.code + ")");
            mReplenishMaterialTableEntity.setMaterial(good);
        } else if (selectDataEvent.getEntity() instanceof CommonBaseEntity) {
            CommonBaseEntity commonBaseEntity = (CommonBaseEntity) selectDataEvent.getEntity();
            bucket.setContent(commonBaseEntity.getName() + "(" + commonBaseEntity.getCode() + ")");

            VesselEntity vesselEntity = new VesselEntity();
            vesselEntity.setId(commonBaseEntity.getId());
            vesselEntity.setCode(commonBaseEntity.getCode());
            vesselEntity.setName(commonBaseEntity.getName());
            mReplenishMaterialTableEntity.setVessel(vesselEntity);
        }
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
                        if (qrCodeEntity.getCode().equals(mReplenishMaterialTableEntity.getEquipment().getCode())) {
                            // 已经通过无需再次设置
                            if (mReplenishMaterialTableEntity.isEqScanFlag()) {
                                return;
                            }
                            mReplenishMaterialTableEntity.setEqScanFlag(true);
                            eamPassIv.setImageResource(R.drawable.replenish_ic_pass);
                            if (mReplenishMaterialTableEntity.getVessel() != null && !TextUtils.isEmpty(mReplenishMaterialTableEntity.getVessel().getCode())){
                                // 是否桶已经校验
                                if (mReplenishMaterialTableEntity.isVesselScanFlag()) {
                                    startBtn.setEnabled(true);
                                    startBtn.setAlpha(1);
                                }
                            }else {
                                // 是否物料已经校验
                                if (scanPassCount >= mReplenishMaterialRecordsScanAdapter.getList().size()){
                                    startBtn.setEnabled(true);
                                    startBtn.setAlpha(1);
                                }
                            }
                        } else {
                            ToastUtils.show(context, getString(R.string.replenish_no_match_bucket));
                        }
                        break;
                    // 扫描桶
                    case 1:
                        if (qrCodeEntity.getCode().equals(mReplenishMaterialTableEntity.getVessel().getCode())) {
                            // 已经通过无需再次设置
                            if (mReplenishMaterialTableEntity.isVesselScanFlag()) {
                                return;
                            }
                            mReplenishMaterialTableEntity.setVesselScanFlag(true);
                            bucketPassIv.setImageResource(R.drawable.replenish_ic_pass);
                            // 是否设备已经校验
                            if (mReplenishMaterialTableEntity.isEqScanFlag()) {
                                startBtn.setEnabled(true);
                                startBtn.setAlpha(1);
                            }
                        } else {
                            ToastUtils.show(context, getString(R.string.replenish_no_match_bucket));
                        }
                        break;
                    // 扫描物料
                    case 2:
                        // 目前暂时按照MES产品定义格式
                        if (mReplenishMaterialRecordsScanAdapter.getList() == null){
                            ToastUtils.show(context,getString(R.string.middleware_no_data));
                            return;
                        }
                        for (ReplenishMaterialTablePartEntity partEntity : mReplenishMaterialRecordsScanAdapter.getList()){
                            if (qrCodeEntity.getCode().equals(mReplenishMaterialTableEntity.getMaterial().code) && partEntity.getBatch().equals(qrCodeEntity.getBatch())){
                                count = 0;
                                partEntity.setScanFlag(true);
                                mReplenishMaterialRecordsScanAdapter.notifyItemRangeChanged(mReplenishMaterialRecordsScanAdapter.getList().indexOf(partEntity),1,partEntity);
                                break;
                            }
                            count++;
                        }
                        if (count >= mReplenishMaterialRecordsScanAdapter.getList().size()){
                            ToastUtils.show(context,getString(R.string.replenish_check_failed_please_check_material_right));
                            LogUtil.d("------scan------",getString(R.string.replenish_check_failed_please_check_material_right));
                            return;
                        }
                         // 判断物料是否全部校验
                        for (ReplenishMaterialTablePartEntity partEntity : mReplenishMaterialRecordsScanAdapter.getList()){
                            if (!partEntity.getScanFlag()){
                                return;
                            }
                            scanPassCount ++;
                        }
                        // 是否设备已经校验
                        if (/*scanPassCount >= mReplenishMaterialRecordsScanAdapter.getList().size() && */mReplenishMaterialTableEntity.isEqScanFlag()) {
                            startBtn.setEnabled(true);
                            startBtn.setAlpha(1);
                        }
                       break;
                    default:
                }
            }

        }
    }

}