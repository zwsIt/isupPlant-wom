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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
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
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.PowerCode;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialInstructionEditAPI;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialInstructionEditContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialInstructionDTO;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialInstructionEditPresenter;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialRecordsEditAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterilEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 配方指令编辑
 */
@Router(Constant.Router.BATCH_MATERIAL_INSTRUCTION_EDIT)
@Presenter(value = {CommonListPresenter.class, BatchMaterialInstructionEditPresenter.class})
//@PowerCode(entityCode = BmConstant.PowerCode.BM_INSTRUCTION_EDIT)
@Controller(value = {GetPowerCodeController.class, WorkFlowViewController.class})
public class BatchMaterialInstructionEditActivity extends BaseRefreshRecyclerActivity<BatchMaterialPartEntity> implements CommonListContract.View, BatchMaterialInstructionEditContract.View {
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
    @BindByTag("batchMode")
    CustomTextView batchMode;

    //    @BindByTag("materialCode")
//    CustomTextView materialCode;
//    @BindByTag("planNum")
//    CustomTextView planNum;
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

    private BatchMaterilEntity mBatchMaterialEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private BatchMaterialRecordsEditAdapter mBatchMaterialRecordsEditAdapter;
    private int mCurrentPosition;
    private BatchMaterialPartEntity mBatchMaterialPartEntity;
    private String dgDeletedIds = "";

    @Override
    protected IListAdapter<BatchMaterialPartEntity> createAdapter() {
        mBatchMaterialRecordsEditAdapter = new BatchMaterialRecordsEditAdapter(context);
        return mBatchMaterialRecordsEditAdapter;
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
        mBatchMaterialEntity = (BatchMaterilEntity) getIntent().getSerializableExtra(Constant.IntentKey.BATCH_MATERIAL_INSTRUCTION);
        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(1, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

        if (!WomConstant.SystemCode.MATERIAL_BATCH_02.equals(mBatchMaterialEntity.getProductId().getIsBatch().id)){
            mBatchMaterialRecordsEditAdapter.setMaterialBatchNo(true);
        }
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

        materialName.setContent(String.format("%s(%s)", mBatchMaterialEntity.getProductId().getName(), mBatchMaterialEntity.getProductId().getCode()));
        batchMode.setContent(mBatchMaterialEntity.getBatchType().value);

        SpannableString planNumSpan = new SpannableString(context.getResources().getString(R.string.wom_need) + "\n\n" + mBatchMaterialEntity.getNeedNum());
        planNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_blue)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumTv.setText(planNumSpan);

        SpannableString sumNumSpan = new SpannableString(getString(R.string.wom_offer) + "\n\n" + mBatchMaterialEntity.getOfferNum());
        sumNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_green)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumTv.setText(sumNumSpan);

        SpannableString remainderNumSpan;
        if (mBatchMaterialEntity.getNeedNum() == null) {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) + "\n\n" + 0);
        } else {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) + "\n\n" + mBatchMaterialEntity.getNeedNum().subtract(mBatchMaterialEntity.getOfferNum()));
        }
        remainderNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_yellow)), 4, remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumTv.setText(remainderNumSpan);

        getController(WorkFlowViewController.class).initPendingWorkFlowView(workFlowView, mBatchMaterialEntity.getPending().id);

        getController(GetPowerCodeController.class).initPowerCode(mBatchMaterialEntity.getPending().activityName);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> ToastUtils.show(context, context.getResources().getString(R.string.middleware_stay_realization)));
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                        BmConstant.URL.BATCH_MATERIAL_INSTRUCTION_DG_LIST_URL + "&id=" + mBatchMaterialEntity.getId(), "");
            }
        });
        customListWidgetAdd.setOnClickListener(v -> {
            BatchMaterialPartEntity batchMaterialPartEntity = new BatchMaterialPartEntity();
            batchMaterialPartEntity.setMaterialId(mBatchMaterialEntity.getProductId()); // 物料
            batchMaterialPartEntity.setBatchDate(new Date().getTime());  // 配料时间
            StaffEntity exeStaff = new StaffEntity();
            exeStaff.id = SupPlantApplication.getAccountInfo().staffId;
            exeStaff.name = SupPlantApplication.getAccountInfo().staffName;
            batchMaterialPartEntity.setExeStaff(exeStaff); // 配料操作人
            batchMaterialPartEntity.setBatRecordState(new SystemCodeEntity(BmConstant.SystemCode.RECORD_STATE_BATCH, "", getString(R.string.wom_no_material_state), "", ""));

            if (mBatchMaterialRecordsEditAdapter.getItemCount() <= 0){
                mBatchMaterialRecordsEditAdapter.addData(batchMaterialPartEntity);
            }else {
                mBatchMaterialRecordsEditAdapter.getList().add(0,batchMaterialPartEntity);
            }
            mBatchMaterialRecordsEditAdapter.notifyItemRangeInserted(0, 1);
            mBatchMaterialRecordsEditAdapter.notifyItemRangeChanged(0, 1);
            contentView.smoothScrollToPosition(0);

        });
        mBatchMaterialRecordsEditAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mCurrentPosition = position;
                mBatchMaterialPartEntity = (BatchMaterialPartEntity) obj;
                switch (childView.getTag().toString()) {
                    case "warehouseTv":
                        IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                        break;
                    case "storeSetTv":
                        if (mBatchMaterialPartEntity.getWareId() == null) {
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
                            break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.WARE_ID, mBatchMaterialPartEntity.getWareId().getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                        break;
                    case "itemViewDelBtn":
                        mBatchMaterialRecordsEditAdapter.getList().remove(obj);
                        mBatchMaterialRecordsEditAdapter.notifyItemRangeRemoved(position, 1);
                        mBatchMaterialRecordsEditAdapter.notifyItemRangeChanged(position, mBatchMaterialRecordsEditAdapter.getItemCount()-position);
                        if (mBatchMaterialPartEntity.getId() != null) {
                            dgDeletedIds += mBatchMaterialPartEntity.getId() + ",";
                        }
                        break;
                    default:
                }
            }
        });
        RxView.clicks(submitBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        submitReport();
                    }
                });
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
        BatchMaterialInstructionDTO batchMaterialInstructionDTO = new BatchMaterialInstructionDTO();
        batchMaterialInstructionDTO.setWorkFlowVarDTO(getWorkFlowVar());
        batchMaterialInstructionDTO.setOperateType("save");
        batchMaterialInstructionDTO.setDeploymentId(String.valueOf(mBatchMaterialEntity.getPending().deploymentId));
        batchMaterialInstructionDTO.setActivityName(mBatchMaterialEntity.getPending().activityName);
        batchMaterialInstructionDTO.setTaskDescription(mBatchMaterialEntity.getPending().taskDescription);
        batchMaterialInstructionDTO.setPendingId(String.valueOf(mBatchMaterialEntity.getPending().id));
        BatchMaterialInstructionDTO.DgListEntity dgListEntity = new BatchMaterialInstructionDTO.DgListEntity();
        dgListEntity.setDg(GsonUtil.gsonString(mBatchMaterialRecordsEditAdapter.getList()));
        batchMaterialInstructionDTO.setDgList(dgListEntity);

        BatchMaterialInstructionDTO.DgListEntity dgDeletedIdsList = new BatchMaterialInstructionDTO.DgListEntity();
        dgDeletedIdsList.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        batchMaterialInstructionDTO.setDgDeletedIds(dgDeletedIdsList);

        mBatchMaterialEntity.setPending(null); // bap 6.0  传输会单据异常
        batchMaterialInstructionDTO.setBatchMateril(mBatchMaterialEntity);
        batchMaterialInstructionDTO.setIds2del("");
        batchMaterialInstructionDTO.setViewCode("WOM_1.0.0_batchMaterial_batchMaterialOrder");
        presenterRouter.create(BatchMaterialInstructionEditAPI.class).submit(mBatchMaterialEntity.getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), batchMaterialInstructionDTO);

    }

    private WorkFlowVarDTO getWorkFlowVar(){
        WorkFlowVarDTO workFlowVarDTO = new WorkFlowVarDTO();
//        getController(WorkFlowViewController.class).getLinkEntities();
        workFlowVarDTO.setComment("");
        workFlowVarDTO.setActivityName(mBatchMaterialEntity.getPending().activityName);
        workFlowVarDTO.setActivityType(mBatchMaterialEntity.getPending().activityType);
        return workFlowVarDTO;
    }



    private boolean checkSubmit() {
        if (mBatchMaterialRecordsEditAdapter.getList() == null || mBatchMaterialRecordsEditAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (BatchMaterialPartEntity batchMaterialPartEntity : mBatchMaterialRecordsEditAdapter.getList()) {
            if (WomConstant.SystemCode.MATERIAL_BATCH_02.equals(mBatchMaterialEntity.getProductId().getIsBatch().id) && TextUtils.isEmpty(batchMaterialPartEntity.getMaterialBatchNum())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchMaterialRecordsEditAdapter.getList().indexOf(batchMaterialPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (batchMaterialPartEntity.getWareId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchMaterialRecordsEditAdapter.getList().indexOf(batchMaterialPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_ware));
                return true;
            }
            if (batchMaterialPartEntity.getOfferNum() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchMaterialRecordsEditAdapter.getList().indexOf(batchMaterialPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_batchmaterial_num));
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
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), BatchMaterialPartEntity.class));
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
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
            mBatchMaterialPartEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mBatchMaterialPartEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mBatchMaterialRecordsEditAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }


}
