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
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.CommonBaseEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.DeploymentEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
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
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialNotifyListAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableEditAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableScanAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.AssociatedEquipmentEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTablePartEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.VesselEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableEditContract;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableScanContract;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialNotifyDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialScanDTO;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialTableDTO;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableEditPresenter;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableScanPresenter;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.ReplenishMaterialRecordsEditAdapter;
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
@Controller(value = {WorkFlowViewController.class, GetPowerCodeController.class,CommonScanController.class})
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

    private ReplenishMaterialTableEntity mReplenishMaterialTableEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private ReplenishMaterialRecordsScanAdapter mReplenishMaterialRecordsScanAdapter;
    private int mCurrentPosition;
    private ReplenishMaterialTablePartEntity mReplenishMaterialTablePartEntity;
    private String dgDeletedIds = "";
    private WorkFlowButtonInfo mWorkFlowButtonInfo;

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
        mWorkFlowButtonInfo = (WorkFlowButtonInfo) getIntent().getSerializableExtra(ReplenishConstant.IntentKey.WORK_FLOW_BTN_INFO);
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
        if (mReplenishMaterialTableEntity.getEquipment() != null && mReplenishMaterialTableEntity.getEquipment().getFeedStockType() != null
                && ReplenishConstant.SystemCode.MODEL_AIR.equals(mReplenishMaterialTableEntity.getEquipment().getFeedStockType().id)){
            bucket.setVisibility(View.GONE);
        }
        eamPoint.setEditable(false);
        material.setEditable(false);
        planNum.setEditable(false);
        bucket.setEditable(false);

        customListWidgetName.setText(context.getResources().getString(R.string.replenish_material_records));
        customListWidgetAdd.setVisibility(View.GONE);
        customListWidgetEdit.setVisibility(View.GONE);

        // 默认不可操作
        startBtn.setEnabled(false);
        startBtn.getBackground().setAlpha(100);
        endBtn.setEnabled(false);
        endBtn.getBackground().setAlpha(100);
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
            getController(CommonScanController.class).openCameraScan(this.getClass().getSimpleName());
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
                    if (TextUtils.isEmpty(charSequence.toString())){
                        mReplenishMaterialTableEntity.setPlanNumber(null);
                        return false;
                    }
                    if(charSequence.toString().startsWith(".")){
                        planNum.editText().setText("0.");
                        return false;
                    }
                    return true;
                })
                .subscribe(charSequence -> mReplenishMaterialTableEntity.setPlanNumber(new BigDecimal(charSequence.toString())));
        RxTextView.textChanges(actualNum.editText())
                .skipInitialValue()
                .filter(charSequence -> {
                    if (TextUtils.isEmpty(charSequence.toString())){
                        mReplenishMaterialTableEntity.setActualNumber(null);
                        return false;
                    }
                    if(charSequence.toString().startsWith(".")){
                        actualNum.editText().setText("0.");
                        return false;
                    }
                    return true;
                })
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        mReplenishMaterialTableEntity.setActualNumber(new BigDecimal(charSequence.toString()));
                    }
                });
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
        mReplenishMaterialRecordsScanAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mCurrentPosition = position;
            mReplenishMaterialTablePartEntity = (ReplenishMaterialTablePartEntity) obj;
            switch (childView.getTag().toString()) {
                case "itemViewDelBtn":
                    mReplenishMaterialRecordsScanAdapter.getList().remove(obj);
                    mReplenishMaterialRecordsScanAdapter.notifyItemRangeRemoved(position, 1);
                    mReplenishMaterialRecordsScanAdapter.notifyItemRangeChanged(position, mReplenishMaterialRecordsScanAdapter.getItemCount() - position);
                    if (mReplenishMaterialTablePartEntity.getId() != null) {
                        dgDeletedIds += mReplenishMaterialTablePartEntity.getId() + ",";
                    }
                    break;
                default:
            }
        });
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
        if (i == 0){
            customDialog.bindView(R.id.tipContentTv,getString(R.string.replenish_start_operate_open_valve));
        }else {
            customDialog.bindView(R.id.tipContentTv,getString(R.string.replenish_end_operate));
        }
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        customDialog.bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {
                    ReplenishMaterialScanDTO replenishMaterialScanDTO = new ReplenishMaterialScanDTO();
                    ReplenishMaterialTableEntity replenishMaterialTableEntity = new ReplenishMaterialTableEntity();
                    replenishMaterialTableEntity.setId(mReplenishMaterialTableEntity.getId());
                    replenishMaterialTableEntity.setEqScanFlag(true);
                    replenishMaterialTableEntity.setVesselScanFlag(true);
                    replenishMaterialScanDTO.setFmBill(replenishMaterialTableEntity);
                    if (i == 0){
                        replenishMaterialScanDTO.setState("start");
                    }else {
                        replenishMaterialScanDTO.setState("complete");
                    }
                    List<ReplenishMaterialTablePartEntity> list = new ArrayList<>();
                    list.addAll(mReplenishMaterialRecordsScanAdapter.getList());
                    replenishMaterialScanDTO.setFmBillDetais(list);

                    presenterRouter.create(ReplenishMaterialTableScanAPI.class).submit(replenishMaterialScanDTO);
                }, true)
                .show();
    }

    /**
     * @param
     * @param workFlowVar
     * @param operate
     * @return
     * @author zhangwenshuai1 2020/4/2
     * @description 提交
     */
    private void submitReport(WorkFlowVar workFlowVar, String operate) {
        if (Constant.OperateType.SUBMIT.equals(operate)) {
            if (checkSubmit()) {
                return;
            }
        }
        onLoading(context.getResources().getString(R.string.wom_dealing));
        ReplenishMaterialTableDTO replenishMaterialTableDTO = new ReplenishMaterialTableDTO();

        replenishMaterialTableDTO.setFmBill(mReplenishMaterialTableEntity);

        ReplenishMaterialTableDTO.DgListEntity dgListEntity = new ReplenishMaterialTableDTO.DgListEntity();
//        if (mReplenishMaterialRecordsScanAdapter.getList() != null){
//            for (ReplenishMaterialTablePartEntity partEntity :  mReplenishMaterialRecordsScanAdapter.getList()){
//                partEntity.setFmBill(null);
//            }
//        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        dgListEntity.setDg(mReplenishMaterialRecordsScanAdapter.getList() == null ? null : gson.toJson(mReplenishMaterialRecordsScanAdapter.getList()));
//        dgListEntity.setDg(mReplenishMaterialRecordsScanAdapter.getList() == null ? null : GsonUtil.gsonStringSerializeNulls(mReplenishMaterialRecordsScanAdapter.getList()));
        replenishMaterialTableDTO.setDgList(dgListEntity);
        ReplenishMaterialTableDTO.DgListEntity dgDeletedIdsList = new ReplenishMaterialTableDTO.DgListEntity();
        dgDeletedIdsList.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        replenishMaterialTableDTO.setDgDeletedIds(dgDeletedIdsList);


//        replenishMaterialTableDTO.setWorkFlowVar(getWorkFlowVar(workFlowVar, Constant.OperateType.SUBMIT.equals(operate)));
        replenishMaterialTableDTO.setOperateType(operate);

        DeploymentEntity deploymentEntity = getController(WorkFlowViewController.class).getDeploymentEntity();
        replenishMaterialTableDTO.setDeploymentId(String.valueOf(deploymentEntity.deploymentId));
        replenishMaterialTableDTO.setActivityName(deploymentEntity.code);
        replenishMaterialTableDTO.setTaskDescription(deploymentEntity.name);
        replenishMaterialTableDTO.setViewCode(deploymentEntity.viewCode);
//        replenishMaterialTableDTO.setPendingId(String.valueOf(mReplenishMaterialTableEntity.getPending().id));

        // bap 6.0  传输会单据异常
        mReplenishMaterialTableEntity.setPending(null);

        replenishMaterialTableDTO.setIds2del("");
        presenterRouter.create(ReplenishMaterialTableEditAPI.class).submit(mReplenishMaterialTableEntity.getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), replenishMaterialTableDTO);

    }

//    private WorkFlowVarDTO getWorkFlowVar(WorkFlowVar workFlowVar, boolean submit) {
//        WorkFlowVarDTO workFlowVarDTO = new WorkFlowVarDTO();
//        if (submit) {
//            workFlowVarDTO.setOutcomeMapJson(workFlowVar.outcomeMapJson.toString());
//            workFlowVarDTO.setOutcome(workFlowVar.outCome);
//            workFlowVarDTO.setOutcomeType(workFlowVar.type);
//        }
//        workFlowVarDTO.setComment(workFlowView.getComment());
////        workFlowVarDTO.setActivityName(mReplenishMaterialTableEntity.getPending().activityName);
////        workFlowVarDTO.setActivityType(mReplenishMaterialTableEntity.getPending().activityType);
//        return workFlowVarDTO;
//    }


    private boolean checkSubmit() {

        if (mReplenishMaterialTableEntity.getEquipment() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getEquipment().getCode())) {
            ToastUtils.show(context, context.getResources().getString(R.string.replenish_choose_eam));
            return true;
        }
        if (mReplenishMaterialTableEntity.getMaterial() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getMaterial().code)) {
            ToastUtils.show(context, context.getResources().getString(R.string.replenish_choose_material));
            return true;
        }
        if (mReplenishMaterialTableEntity.getPlanNumber() == null) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_input_plan_num));
            return true;
        }
        if (mReplenishMaterialTableEntity.getActualNumber() == null) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_input_actual_num));
            return true;
        }
        if (mReplenishMaterialTableEntity.getVessel() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getVessel().getCode())) {
            ToastUtils.show(context, context.getResources().getString(R.string.replenish_choose_or_scan_bucket));
            return true;
        }
        if (mReplenishMaterialRecordsScanAdapter.getList() == null || mReplenishMaterialRecordsScanAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (ReplenishMaterialTablePartEntity replenishMaterialTablePartEntity : mReplenishMaterialRecordsScanAdapter.getList()) {
            if (TextUtils.isEmpty(replenishMaterialTablePartEntity.getBatch())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mReplenishMaterialRecordsScanAdapter.getList().indexOf(replenishMaterialTablePartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (replenishMaterialTablePartEntity.getFmNumber() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mReplenishMaterialRecordsScanAdapter.getList().indexOf(replenishMaterialTablePartEntity) + 1) + context.getResources().getString(R.string.wom_pleasr_write_num));
                return true;
            }
        }
        return false;
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
        ToastUtils.show(context,errorMsg);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getScanResult(CodeResultEvent codeResultEvent){
        if (this.getClass().getSimpleName().equals(codeResultEvent.scanTag)){

            QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context,codeResultEvent.scanResult);

            if (qrCodeEntity != null){
                switch (qrCodeEntity.getType()){
                    // 扫描设备
                    case 0:
                        if (qrCodeEntity.getCode().equals(mReplenishMaterialTableEntity.getEquipment().getCode())){
                            mReplenishMaterialTableEntity.setEqScanFlag(true);
                            eamPassIv.setImageResource(R.drawable.replenish_ic_pass);
                            // 是否桶已经校验
                            if (mReplenishMaterialTableEntity.isVesselScanFlag()){
                                startBtn.setEnabled(true);
                                startBtn.getBackground().setAlpha(255);
                            }
                        }else {
                            ToastUtils.show(context,"未能匹配设备，请再次确认！");
                        }
                        break;
                    // 扫描桶
                    case 1:
                        if (qrCodeEntity.getCode().equals(mReplenishMaterialTableEntity.getVessel().getCode())){
                            mReplenishMaterialTableEntity.setVesselScanFlag(true);
                            bucketPassIv.setImageResource(R.drawable.replenish_ic_pass);
                            // 是否设备已经校验
                            if (mReplenishMaterialTableEntity.isEqScanFlag()){
                                startBtn.setEnabled(true);
                                startBtn.getBackground().setAlpha(255);
                            }
                        }else {
                            ToastUtils.show(context,"未能匹配桶号，请再次确认！");
                        }
                        break;
                    // 扫描物料
                    case 2:
                        break;
                    default:
                }
            }

        }
    }

}
