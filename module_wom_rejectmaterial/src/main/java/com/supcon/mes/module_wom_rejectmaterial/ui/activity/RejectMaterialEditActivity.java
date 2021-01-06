package com.supcon.mes.module_wom_rejectmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.beans.WorkFlowEntity;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.constant.WorkFlowBtn;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.mbap.view.CustomWorkFlowView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.WorkFlowViewController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_preparematerial.model.bean.PrepareMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;
import com.supcon.mes.module_wom_rejectmaterial.IntentRouter;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.constant.RmConstant;
import com.supcon.mes.module_wom_rejectmaterial.controller.RejectMaterialSubmitController;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialEntity;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialPartEntity;
import com.supcon.mes.module_wom_rejectmaterial.model.dto.RejectMaterialDTO;
import com.supcon.mes.module_wom_rejectmaterial.ui.adapter.RejectMaterialRecordsEditAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 退料编辑
 */
@Router(Constant.Router.REJECT_MATERIAL_EDIT)
@Presenter(value = {CommonListPresenter.class})
//@PowerCode(entityCode = RmConstant.PowerCode.BM_INSTRUCTION_EDIT)
@Controller(value = {GetPowerCodeController.class, WorkFlowViewController.class, RejectMaterialSubmitController.class})
public class RejectMaterialEditActivity extends BaseRefreshRecyclerActivity<RejectMaterialPartEntity> implements CommonListContract.View, OnAPIResultListener {
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
    @BindByTag("rejectType")
    CustomTextView rejectType;
    @BindByTag("applyStaff")
    CustomTextView applyStaff;
    @BindByTag("time")
    CustomTextView time;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;

    private RejectMaterialEntity mRejectMaterialEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private RejectMaterialRecordsEditAdapter mRejectMaterialRecordsEditAdapter;
    private int mCurrentPosition;
    private String dgUrl = "";
    private RejectMaterialPartEntity mRejectMaterialPartEntity;
    private String dgDeletedIds = "";
    private List<SystemCodeEntity> mSystemCodeEntities;
    private List<String> rejectReason = new ArrayList<>();
    private SinglePickController<String> mStringSinglePickController = new SinglePickController<>(this);

    @Override
    protected IListAdapter<RejectMaterialPartEntity> createAdapter() {
        mRejectMaterialRecordsEditAdapter = new RejectMaterialRecordsEditAdapter(context);
        return mRejectMaterialRecordsEditAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_reject_material_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(true);
        mRejectMaterialEntity = (RejectMaterialEntity) getIntent().getSerializableExtra(Constant.IntentKey.REJECT_MATERIAL);
        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(1, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

        if (RmConstant.SystemCode.REJECT_TYPE_BATCH.equals(mRejectMaterialEntity.getRejectType().id)) {
            dgUrl = RmConstant.URL.REJECT_BATCH_MATERIAL_EDIT_DG_LIST_URL;
        } else if (RmConstant.SystemCode.REJECT_TYPE_PREPARE.equals(mRejectMaterialEntity.getRejectType().id)) {
            dgUrl = RmConstant.URL.REJECT_PREPARE_MATERIAL_EDIT_DG_LIST_URL;
        }

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_reject_material_edit));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_reject_material_details));
        customListWidgetEdit.setVisibility(View.GONE);

        rejectType.setContent(mRejectMaterialEntity.getRejectType().value);
        applyStaff.setContent(mRejectMaterialEntity.getRejectApplyStaff() == null ? "" : mRejectMaterialEntity.getRejectApplyStaff().name);
        time.setContent(mRejectMaterialEntity.getRejectApplyDate() == null ? "" : DateUtil.dateTimeFormat(mRejectMaterialEntity.getRejectApplyDate()));

        getController(WorkFlowViewController.class).initPendingWorkFlowView(workFlowView, mRejectMaterialEntity.getPending().id);
        getController(RejectMaterialSubmitController.class).setOnAPIResultListener(this);  // 注册回调监听
    }

    @Override
    protected void initData() {
        super.initData();
        getController(GetPowerCodeController.class).initPowerCode(mRejectMaterialEntity.getPending().activityName);

        mSystemCodeEntities = SupPlantApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.REJECT_REASON)).build().listLazy();
        for (SystemCodeEntity systemCodeEntity : mSystemCodeEntities) {
            rejectReason.add(systemCodeEntity.value);
        }
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
                        dgUrl + "&id=" + mRejectMaterialEntity.getId(), "");
            }
        });
        customListWidgetAdd.setOnClickListener(v -> {
            if (RmConstant.SystemCode.REJECT_TYPE_BATCH.equals(mRejectMaterialEntity.getRejectType().id)) {
                // 退料配料记录参照lsit
                IntentRouter.go(context, Constant.Router.BATCH_MATERIAL_REJECT_LIST_REF);
            } else if (RmConstant.SystemCode.REJECT_TYPE_PREPARE.equals(mRejectMaterialEntity.getRejectType().id)) {
                // 退料备料记录参照lsit
                IntentRouter.go(context, Constant.Router.PREPARE_MATERIAL_REJECT_LIST_REF);
            }


        });
        mRejectMaterialRecordsEditAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mCurrentPosition = position;
                mRejectMaterialPartEntity = (RejectMaterialPartEntity) obj;
                switch (childView.getTag().toString()) {
                    case "warehouseTv":
                        IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                        break;
                    case "storeSetTv":
                        if (mRejectMaterialPartEntity.getWareId() == null) {
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
                            break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.WARE_ID, mRejectMaterialPartEntity.getWareId().getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                        break;
                    case "rejectReason":
                        if (mSystemCodeEntities == null || mSystemCodeEntities.size() <= 0) {
                            ToastUtils.show(context, getString(R.string.wom_null_systemcode));
                            return;
                        }
                        mStringSinglePickController
                                .list(rejectReason)
                                .listener(new SinglePicker.OnItemPickListener() {
                                    @Override
                                    public void onItemPicked(int index, Object item) {
                                        mRejectMaterialPartEntity.setRejectReason(mSystemCodeEntities.get(index));
                                        mRejectMaterialRecordsEditAdapter.notifyItemRangeChanged(position, 1);
                                    }
                                })
                                .show();
                        break;
                    case "itemViewDelBtn":
                        mRejectMaterialRecordsEditAdapter.getList().remove(obj);
                        mRejectMaterialRecordsEditAdapter.notifyItemRangeRemoved(position, 1);
                        mRejectMaterialRecordsEditAdapter.notifyItemRangeChanged(position, mRejectMaterialRecordsEditAdapter.getItemCount() - position);
                        if (mRejectMaterialPartEntity.getId() != null) {
                            dgDeletedIds += mRejectMaterialPartEntity.getId() + ",";
                        }
                        break;
                    default:
                }
            }
        });

        workFlowView.setOnChildViewClickListener(new OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int action, Object obj) {
                WorkFlowVar workFlowVar = (WorkFlowVar) obj;
                switch (action) {
                    case 4:
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                        bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                        bundle.putString(Constant.IntentKey.SELECT_TAG, childView.getTag().toString());
                        IntentRouter.go(context, Constant.Router.CONTACT_SELECT);
                        break;
                    default:
                        doSubmit(workFlowVar, action);
                        break;
                }

            }
        });

    }

    /**
     * @param
     * @param action
     * @return
     * @author zhangwenshuai1 2020/4/2
     * @description 报工
     */
    private void doSubmit(WorkFlowVar workFlowVar, int action) {
        if (checkSubmit(action)) {
            return;
        }
        onLoading(context.getResources().getString(R.string.wom_dealing));
        RejectMaterialDTO rejectMaterialDTO = getWorkFlowVarDTO(workFlowVar, action);
        rejectMaterialDTO.setDeploymentId(String.valueOf(mRejectMaterialEntity.getPending().deploymentId));
        rejectMaterialDTO.setActivityName(mRejectMaterialEntity.getPending().activityName);
        rejectMaterialDTO.setPendingId(String.valueOf(mRejectMaterialEntity.getPending().id));
        rejectMaterialDTO.setTaskDescription(mRejectMaterialEntity.getPending().taskDescription);
        rejectMaterialDTO.setUploadFileFormMap(new ArrayList<>());

        RejectMaterialDTO.DgListEntity dgListEntity = new RejectMaterialDTO.DgListEntity();
        RejectMaterialDTO.DgListEntity dgDeletedIdsList = new RejectMaterialDTO.DgListEntity();
        String path = ""; // 提交前缀url
        if (RmConstant.SystemCode.REJECT_TYPE_BATCH.equals(mRejectMaterialEntity.getRejectType().id)) { // 配料退料
            dgListEntity.setDgEditBatch(GsonUtil.gsonString(mRejectMaterialRecordsEditAdapter.getList()));
            dgDeletedIdsList.setDgEditBatch(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);

            path = RmConstant.URL.REJECT_BATCH_MATERIAL_EDIT__URL;
            rejectMaterialDTO.setViewCode("WOM_1.0.0_rejectMaterilal_batchRejectEdit");
        } else if (RmConstant.SystemCode.REJECT_TYPE_PREPARE.equals(mRejectMaterialEntity.getRejectType().id)) { // 备料退料
            dgListEntity.setDgEditPrepare(GsonUtil.gsonString(mRejectMaterialRecordsEditAdapter.getList()));
            dgDeletedIdsList.setDgEditPrepare(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);

            path = RmConstant.URL.REJECT_PREPARE_MATERIAL_EDIT_URL;
            rejectMaterialDTO.setViewCode("WOM_1.0.0_rejectMaterilal_prePareRejectEdit");
        }
        rejectMaterialDTO.setDgList(dgListEntity);
        rejectMaterialDTO.setDgDeletedIds(dgDeletedIdsList);

        rejectMaterialDTO.setRejectMaterial(mRejectMaterialEntity);
        rejectMaterialDTO.setIds2del("");

        getController(RejectMaterialSubmitController.class).submit(mRejectMaterialEntity.getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), path, rejectMaterialDTO, false);

    }

    private RejectMaterialDTO getWorkFlowVarDTO(WorkFlowVar workFlowVar, int action) {
        RejectMaterialDTO rejectMaterialDTO = new RejectMaterialDTO();
        WorkFlowVarDTO workFlowVarDTO = new WorkFlowVarDTO();
        workFlowVarDTO.setComment(workFlowVar.comment);
        workFlowVarDTO.setActivityName(mRejectMaterialEntity.getPending().activityName);
        workFlowVarDTO.setActivityType(mRejectMaterialEntity.getPending().activityType);

        if (action == WorkFlowBtn.SAVE_BTN.value()) { // 保存
            rejectMaterialDTO.setOperateType(Constant.Transition.SAVE);
        } else { // 提交
            // 选人参数
            List<WorkFlowEntity> outcomeMapJson = workFlowVar.outcomeMapJson;
            if (!TextUtils.isEmpty(outcomeMapJson.get(0).assignUser)) {
                outcomeMapJson.get(0).assignUser = ("\"\"".equals(outcomeMapJson.get(0).assignUser)) ? null : outcomeMapJson.get(0).assignUser.replace("\"", "");
            }
            workFlowVarDTO.setOutcome(workFlowVar.outCome);
            workFlowVarDTO.setOutcomeType(workFlowVar.outcomeMapJson.get(0).type);
            workFlowVarDTO.setOutcomeMapJson(GsonUtil.gsonString(workFlowVar.outcomeMapJson));

            rejectMaterialDTO.setOperateType(Constant.Transition.SUBMIT);
        }
        rejectMaterialDTO.setWorkFlowVar(workFlowVarDTO);

        return rejectMaterialDTO;
    }


    private boolean checkSubmit(int action) {
        if (action == WorkFlowBtn.SUBMIT_BTN.value()
                && (mRejectMaterialRecordsEditAdapter.getList() == null || mRejectMaterialRecordsEditAdapter.getList().size() <= 0)) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (RejectMaterialPartEntity rejectMaterialPartEntity : mRejectMaterialRecordsEditAdapter.getList()) {
            if (rejectMaterialPartEntity.getWareId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mRejectMaterialRecordsEditAdapter.getList().indexOf(rejectMaterialPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_ware));
                return true;
            }
            if (rejectMaterialPartEntity.getRejectNum() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mRejectMaterialRecordsEditAdapter.getList().indexOf(rejectMaterialPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_reject_num));
                return true;
            }
            if (rejectMaterialPartEntity.getRejectReason() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mRejectMaterialRecordsEditAdapter.getList().indexOf(rejectMaterialPartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_reject_reason));
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
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), RejectMaterialPartEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof ContactEntity) {

            return;
        }
        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
            // 判断是否清空货位
            if(mRejectMaterialPartEntity.getWareId() != null && !mRejectMaterialPartEntity.getWareId().getId().equals(((WarehouseEntity) selectDataEvent.getEntity()).getId())){
                mRejectMaterialPartEntity.setStoreId(null);
            }
            mRejectMaterialPartEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mRejectMaterialPartEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        } else if (selectDataEvent.getEntity() instanceof List) {
            if ("prepareMaterial".equals(selectDataEvent.getSelectTag())) {
                // 批量添加退料备料记录
                List<PrepareMaterialPartEntity> prepareMaterialPartEntityList = (List<PrepareMaterialPartEntity>) selectDataEvent.getEntity();
                addNewRejectPrepareMaterialRecords(prepareMaterialPartEntityList);
            } else {
                // 批量添加退料配料记录
                List<BatchMaterialPartEntity> batchMaterialPartEntityList = (List<BatchMaterialPartEntity>) selectDataEvent.getEntity();
                addNewRejectBatchMaterialRecords(batchMaterialPartEntityList);
            }

        }
        mRejectMaterialRecordsEditAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/4/23
     * @description 批量添加备料记录退料
     */
    private void addNewRejectPrepareMaterialRecords(List<PrepareMaterialPartEntity> prepareMaterialPartEntityList) {
        List<Long> currentListId = new ArrayList<>();
        for (RejectMaterialPartEntity materialPartEntity : mRejectMaterialRecordsEditAdapter.getList()) {
            currentListId.add(materialPartEntity.getPreparePartId().getId());
        }
        RejectMaterialPartEntity rejectMaterialPartEntity;
        List<RejectMaterialPartEntity> rejectMaterialPartEntityList = new ArrayList<>();
        for (PrepareMaterialPartEntity entity : prepareMaterialPartEntityList) {
            // 防止重复添加
            if (currentListId.contains(entity.getId())){
                continue;
            }
            rejectMaterialPartEntity = new RejectMaterialPartEntity();
            rejectMaterialPartEntity.setPreparePartId(entity); // 备料记录
            rejectMaterialPartEntity.setMaterialId(entity.getMaterialId()); // 物料
            rejectMaterialPartEntity.setMaterialBatchNum(entity.getMaterialBatchNum()); // 物料批号
            rejectMaterialPartEntity.setOutWareId(entity.getInWareId()); // 出库
            rejectMaterialPartEntity.setOutStoreId(entity.getInStoreId()); // 出货位
            rejectMaterialPartEntity.setWareId(entity.getInWareId());  // 默认出库仓
            rejectMaterialPartEntity.setStoreId(entity.getInStoreId()); // 默认出货位
            rejectMaterialPartEntity.setRejectNum(entity.getDoneNum()); // // 默认备料数量
            rejectMaterialPartEntity.setRejectReason(entity.getRejectReason()); // 默认拒签原因

            rejectMaterialPartEntityList.add(rejectMaterialPartEntity);
        }
        if (rejectMaterialPartEntityList.size() <= 0){
            return;
        }
        mRejectMaterialRecordsEditAdapter.getList().addAll(rejectMaterialPartEntityList);

        mRejectMaterialRecordsEditAdapter.notifyItemRangeInserted(mRejectMaterialRecordsEditAdapter.getItemCount() - 1, 1);
        mRejectMaterialRecordsEditAdapter.notifyItemRangeChanged(mRejectMaterialRecordsEditAdapter.getItemCount() - 1, 1);
        contentView.smoothScrollToPosition(mRejectMaterialRecordsEditAdapter.getItemCount() - 1);
    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/4/22
     * @description 批量添加配料记录退料
     */
    private void addNewRejectBatchMaterialRecords(List<BatchMaterialPartEntity> batchMaterialPartEntityList) {
        List<Long> currentListId = new ArrayList<>();
        for (RejectMaterialPartEntity materialPartEntity : mRejectMaterialRecordsEditAdapter.getList()) {
            currentListId.add(materialPartEntity.getBatchingPartId().getId());
        }
        RejectMaterialPartEntity rejectMaterialPartEntity;
        List<RejectMaterialPartEntity> rejectMaterialPartEntityList = new ArrayList<>();
        for (BatchMaterialPartEntity entity : batchMaterialPartEntityList) {
            // 防止重复添加
            if (currentListId.contains(entity.getId())){
                continue;
            }
            rejectMaterialPartEntity = new RejectMaterialPartEntity();
            rejectMaterialPartEntity.setBatchingPartId(entity); // 配料记录
            rejectMaterialPartEntity.setMaterialId(entity.getMaterialId()); // 物料
            rejectMaterialPartEntity.setMaterialBatchNum(entity.getMaterialBatchNum()); // 物料批号
            rejectMaterialPartEntity.setOutWareId(entity.getWareId()); // 出库
            rejectMaterialPartEntity.setOutStoreId(entity.getStoreId()); // 出货位
            rejectMaterialPartEntity.setWareId(entity.getWareId());  // 默认出库仓
            rejectMaterialPartEntity.setStoreId(entity.getStoreId()); // 默认出货位
            rejectMaterialPartEntity.setRejectNum(entity.getOfferNum()); // // 默认配料数量
            rejectMaterialPartEntity.setRejectReason(entity.getRejectReason()); // 默认拒签原因

            rejectMaterialPartEntityList.add(rejectMaterialPartEntity);

        }
        if (rejectMaterialPartEntityList.size() <= 0){
            return;
        }
        mRejectMaterialRecordsEditAdapter.getList().addAll(rejectMaterialPartEntityList);

        mRejectMaterialRecordsEditAdapter.notifyItemRangeInserted(mRejectMaterialRecordsEditAdapter.getItemCount() - 1, 1);
        mRejectMaterialRecordsEditAdapter.notifyItemRangeChanged(mRejectMaterialRecordsEditAdapter.getItemCount() - 1, 1);
        contentView.smoothScrollToPosition(mRejectMaterialRecordsEditAdapter.getItemCount() - 1);
    }


    @Override
    public void onFail(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onSuccess(Object result) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }
}
