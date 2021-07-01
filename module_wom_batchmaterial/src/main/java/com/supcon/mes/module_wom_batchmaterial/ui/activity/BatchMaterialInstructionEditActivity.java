package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialInstructionEditAPI;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionEntity;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionPartEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialInstructionEditContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialInstructionEditPresenter;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchInstructionPartsAdapter;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialRecordsEditAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterilEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 配方指令编辑
 */
@Router(Constant.Router.BATCH_MATERIAL_INSTRUCTION_EDIT)
@Presenter(value = {CommonListPresenter.class, BatchMaterialInstructionEditPresenter.class})
//@PowerCode(entityCode = BmConstant.PowerCode.BM_INSTRUCTION_EDIT)
@Controller(value = {GetPowerCodeController.class, WorkFlowViewController.class,CommonScanController.class})
public class BatchMaterialInstructionEditActivity extends BaseRefreshRecyclerActivity<BatchInstructionPartEntity> implements CommonListContract.View, BatchMaterialInstructionEditContract.View {
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
    @BindByTag("materialName")
    CustomTextView materialName;
    @BindByTag("bucketCode")
    CustomTextView bucketCode;
    @BindByTag("sumNumTv")
    TextView sumNumTv;
    @BindByTag("planNumTv")
    TextView planNumTv;
    @BindByTag("remainderNumTv")
    TextView remainderNumTv;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;

    private BatchInstructionEntity mBatchInstructionEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private BatchInstructionPartsAdapter mBatchInstructionPartsAdapter;
    private BatchInstructionPartEntity mBatchMaterialPartEntity;
    private String dgDeletedIds = "";

    @Override
    protected IListAdapter<BatchInstructionPartEntity> createAdapter() {
        mBatchInstructionPartsAdapter = new BatchInstructionPartsAdapter(context);
        return mBatchInstructionPartsAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_batch_material_instruction_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(true);
        mBatchInstructionEntity = (BatchInstructionEntity) getIntent().getSerializableExtra(Constant.IntentKey.BATCH_MATERIAL_INSTRUCTION);
        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(1, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

//        if (!WomConstant.SystemCode.MATERIAL_BATCH_02.equals(mBatchInstructionEntity.getProductId().getIsBatch().id)) {
//            mBatchInstructionPartsAdapter.setMaterialBatchNo(true);
//        }
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_material_instruction_edit));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_batch_material_records));
        customListWidgetEdit.setVisibility(View.GONE);

        if (mBatchInstructionEntity.getActualNumber() == null){
            mBatchInstructionEntity.setActualNumber(new BigDecimal(0));
        }

        materialName.setContent(String.format("%s(%s)", mBatchInstructionEntity.getMaterial().getName(), mBatchInstructionEntity.getMaterial().getCode()));
        bucketCode.setContent(getIntent().getStringExtra(BmConstant.IntentKey.BUCKET_CODE));

        SpannableString planNumSpan = new SpannableString(context.getResources().getString(R.string.wom_need) + "\n\n" + mBatchInstructionEntity.getPlanNumber());
        planNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_blue)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumTv.setText(planNumSpan);

        SpannableString sumNumSpan = new SpannableString(getString(R.string.wom_offer) + "\n\n" + mBatchInstructionEntity.getActualNumber());
        sumNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_green)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumTv.setText(sumNumSpan);

        SpannableString remainderNumSpan;
        if (mBatchInstructionEntity.getPlanNumber() == null) {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) + "\n\n" + 0);
        } else {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) + "\n\n" + mBatchInstructionEntity.getPlanNumber().subtract(mBatchInstructionEntity.getActualNumber()));
        }
        remainderNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_yellow)), 4, remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumTv.setText(remainderNumSpan);

//        getController(WorkFlowViewController.class).initPendingWorkFlowView(workFlowView, mBatchInstructionEntity.getPending().id);

//        getController(GetPowerCodeController.class).initPowerCode(mBatchInstructionEntity.getPending().activityName);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName()));
        refreshListController.setOnRefreshListener(() -> presenterRouter.create(BatchMaterialInstructionEditAPI.class).listBatchParts(mBatchInstructionEntity.getId()));
        customListWidgetAdd.setOnClickListener(v -> {
            addItem(null);
        });
        mBatchInstructionPartsAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mBatchMaterialPartEntity = (BatchInstructionPartEntity) obj;
            switch (childView.getTag().toString()) {
//                case "warehouseTv":
//                    IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
//                    break;
//                case "storeSetTv":
//                    if (mBatchMaterialPartEntity.getWareId() == null) {
//                        ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
//                        break;
//                    }
//                    Bundle bundle = new Bundle();
//                    bundle.putLong(Constant.IntentKey.WARE_ID, mBatchMaterialPartEntity.getWareId().getId());
//                    IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
//                    break;
                case "itemViewDelBtn":
                    mBatchInstructionPartsAdapter.getList().remove(obj);
                    mBatchInstructionPartsAdapter.notifyItemRangeRemoved(position, 1);
                    mBatchInstructionPartsAdapter.notifyItemRangeChanged(position, mBatchInstructionPartsAdapter.getItemCount() - position);
                    if (mBatchMaterialPartEntity.getId() != null) {
                        dgDeletedIds += mBatchMaterialPartEntity.getId() + ",";
                    }
                    break;
                default:
            }
        });
        RxView.clicks(submitBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(o -> submitReport());
    }

    private void addItem(QrCodeEntity qrCodeEntity) {
        BatchInstructionPartEntity batchInstructionPartEntity = new BatchInstructionPartEntity();
        if (qrCodeEntity != null){
            batchInstructionPartEntity.setBatch(qrCodeEntity.getBatch());
            batchInstructionPartEntity.setBmNumber(qrCodeEntity.getNum());
        }
        batchInstructionPartEntity.setOperatorTime(System.currentTimeMillis());
        StaffEntity staffEntity = new StaffEntity();
        staffEntity.id = SupPlantApplication.getAccountInfo().staffId;
        batchInstructionPartEntity.setOperator(staffEntity);
        batchInstructionPartEntity.setBmSetDetail(mBatchInstructionEntity);

        if (mBatchInstructionPartsAdapter.getItemCount() <= 0) {
            mBatchInstructionPartsAdapter.addData(batchInstructionPartEntity);
        } else {
            mBatchInstructionPartsAdapter.getList().add(0, batchInstructionPartEntity);
        }
        mBatchInstructionPartsAdapter.notifyItemRangeInserted(0, 1);
        mBatchInstructionPartsAdapter.notifyItemRangeChanged(0, 1);
        contentView.smoothScrollToPosition(0);
    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/4/2
     * @description 报工
     */
    private void submitReport() {
        if (checkSubmit()) {
            return;
        }
        onLoading(context.getResources().getString(R.string.wom_dealing));
//        BatchMaterialInstructionDTO batchMaterialInstructionDTO = new BatchMaterialInstructionDTO();
//        batchMaterialInstructionDTO.setWorkFlowVarDTO(getWorkFlowVar());
//        batchMaterialInstructionDTO.setOperateType("save");
//        batchMaterialInstructionDTO.setDeploymentId(String.valueOf(mBatchMaterialEntity.getPending().deploymentId));
//        batchMaterialInstructionDTO.setActivityName(mBatchMaterialEntity.getPending().activityName);
//        batchMaterialInstructionDTO.setTaskDescription(mBatchMaterialEntity.getPending().taskDescription);
//        batchMaterialInstructionDTO.setPendingId(String.valueOf(mBatchMaterialEntity.getPending().id));
//        BatchMaterialInstructionDTO.DgListEntity dgListEntity = new BatchMaterialInstructionDTO.DgListEntity();
//        dgListEntity.setDg(GsonUtil.gsonStringSerializeNulls(mBatchInstructionPartsAdapter.getList()));
//        batchMaterialInstructionDTO.setDgList(dgListEntity);
//
//        BatchMaterialInstructionDTO.DgListEntity dgDeletedIdsList = new BatchMaterialInstructionDTO.DgListEntity();
//        dgDeletedIdsList.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
//        batchMaterialInstructionDTO.setDgDeletedIds(dgDeletedIdsList);
//
//        // bap 6.0  传输会单据异常
//        mBatchMaterialEntity.setPending(null);
//        batchMaterialInstructionDTO.setBatchMateril(mBatchMaterialEntity);
//        batchMaterialInstructionDTO.setIds2del("");
//        batchMaterialInstructionDTO.setViewCode("WOM_1.0.0_batchMaterial_batchMaterialOrder");
        presenterRouter.create(BatchMaterialInstructionEditAPI.class).submit(mBatchInstructionPartsAdapter.getList());

    }

    private WorkFlowVarDTO getWorkFlowVar() {
        WorkFlowVarDTO workFlowVarDTO = new WorkFlowVarDTO();
//        getController(WorkFlowViewController.class).getLinkEntities();
//        workFlowVarDTO.setComment("");
//        workFlowVarDTO.setActivityName(mBatchMaterialEntity.getPending().activityName);
//        workFlowVarDTO.setActivityType(mBatchMaterialEntity.getPending().activityType);
        return workFlowVarDTO;
    }


    private boolean checkSubmit() {
        if (mBatchInstructionPartsAdapter.getList() == null || mBatchInstructionPartsAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (BatchInstructionPartEntity batchInstructionPartEntity : mBatchInstructionPartsAdapter.getList()) {
            if (TextUtils.isEmpty(batchInstructionPartEntity.getBatch())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchInstructionPartsAdapter.getList().indexOf(batchInstructionPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (batchInstructionPartEntity.getBmNumber() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchInstructionPartsAdapter.getList().indexOf(batchInstructionPartEntity) + 1) + context.getResources().getString(R.string.wom_pleasr_write_num));
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
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), BatchInstructionPartEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void listBatchPartsSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
//        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
//        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), BatchInstructionPartEntity.class));
    }

    @Override
    public void listBatchPartsFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        EventBus.getDefault().post(new RefreshEvent());
        if ((Boolean)entity.data){
            Bundle bundle = new Bundle();
            bundle.putBoolean(BmConstant.IntentKey.BATCH_AREA_AUTO,true);
            IntentRouter.go(context,Constant.AppCode.WOM_BATCH_MATERIAL_SET,bundle);
        }
        finish();
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getScanResult(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)) {
            QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);
            if (qrCodeEntity != null) {
                switch (qrCodeEntity.getType()) {
                    // 扫描设备
                    case 0:
//                        AssociatedEquipmentEntity associatedEquipmentEntity = new AssociatedEquipmentEntity();
//                        associatedEquipmentEntity.setId(qrCodeEntity.getId());
//                        associatedEquipmentEntity.setCode(qrCodeEntity.getCode());
//                        associatedEquipmentEntity.setName(qrCodeEntity.getName());
//                        eamPoint.setContent(associatedEquipmentEntity.getName() + "(" + associatedEquipmentEntity.getCode() + ")");
//                        mReplenishMaterialTableEntity.setEquipment(associatedEquipmentEntity);
                        break;
                    // 扫描桶
                    case 1:
//                        VesselEntity vesselEntity = new VesselEntity();
//                        vesselEntity.setId(qrCodeEntity.getId());
//                        vesselEntity.setCode(qrCodeEntity.getCode());
//                        vesselEntity.setName(qrCodeEntity.getName());
//                        bucket.setContent(qrCodeEntity.getName() + "(" + qrCodeEntity.getCode() + ")");
//                        mReplenishMaterialTableEntity.setVessel(vesselEntity);
                        break;
                    // 扫描物料
                    case 2:
                        // 目前暂时按照MES产品定义格式
                        if (mBatchInstructionEntity.getMaterial() != null && mBatchInstructionEntity.getMaterial().getCode().equals(qrCodeEntity.getCode())){
                            addItem(qrCodeEntity);
                        }else {
                            ToastUtils.show(context, getResources().getString(R.string.batch_no_match_current_material));
                        }

                        break;
                    default:
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
//        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
//            mBatchMaterialPartEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
//        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
//            mBatchMaterialPartEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
//        }
//        mBatchInstructionPartsAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }


}
