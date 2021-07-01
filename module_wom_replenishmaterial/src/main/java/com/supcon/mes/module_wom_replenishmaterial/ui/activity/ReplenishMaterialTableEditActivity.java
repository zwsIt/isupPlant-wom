package com.supcon.mes.module_wom_replenishmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.StartProcessEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.middleware.model.bean.WorkFlowVarDTO;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.UrlUtil;
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
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableEditAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.AssociatedEquipmentEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.BucketDetailEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTablePartEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableEditContract;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialTableDTO;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableEditPresenter;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.ReplenishMaterialRecordsEditAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/05/13
 * Email zhangwenshuai1@supcon.com
 * Desc 补料编辑
 */
@Router(value = ReplenishConstant.Router.REPLENISH_MATERIAL_EDIT, viewCode = ReplenishConstant.ViewCode.REPLENISH_MATERIAL_EDIT)
@Presenter(value = {CommonListPresenter.class, ReplenishMaterialTableEditPresenter.class})
//@PowerCode(entityCode = BmConstant.PowerCode.BM_INSTRUCTION_EDIT)
@Controller(value = {WorkFlowViewController.class, GetPowerCodeController.class, CommonScanController.class})
public class ReplenishMaterialTableEditActivity extends BaseRefreshRecyclerActivity<ReplenishMaterialTablePartEntity> implements CommonListContract.View, ReplenishMaterialTableEditContract.View {
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
    @BindByTag("workFlowView")
    CustomWorkFlowView workFlowView;
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

    private ReplenishMaterialTableEntity mReplenishMaterialTableEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private ReplenishMaterialRecordsEditAdapter mReplenishMaterialRecordsEditAdapter;
    private int mCurrentPosition;
    private ReplenishMaterialTablePartEntity mReplenishMaterialTablePartEntity;
    private String dgDeletedIds = "";
    private WorkFlowButtonInfo mWorkFlowButtonInfo;
    private PendingEntity pendingEntity;
    private StartProcessEntity startProcessEntity;
    private QrCodeEntity qrCodeEntity;

    @Override
    protected IListAdapter<ReplenishMaterialTablePartEntity> createAdapter() {
        mReplenishMaterialRecordsEditAdapter = new ReplenishMaterialRecordsEditAdapter(context);
        return mReplenishMaterialRecordsEditAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.replenish_ac_replenish_material_table_edit;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        mReplenishMaterialTableEntity = (ReplenishMaterialTableEntity) getIntent().getSerializableExtra(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE);
        pendingEntity = (PendingEntity) getIntent().getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
        startProcessEntity = (StartProcessEntity) getIntent().getSerializableExtra(Constant.IntentKey.STARTPROCESS_ENTITY);
        mWorkFlowButtonInfo = (WorkFlowButtonInfo) getIntent().getSerializableExtra(ReplenishConstant.IntentKey.WORK_FLOW_BTN_INFO);
        if (mReplenishMaterialTableEntity == null) {
            mReplenishMaterialTableEntity = new ReplenishMaterialTableEntity();
        }
        // 来源：补料单列表、主页代办
        if (mReplenishMaterialTableEntity.getId() != null || pendingEntity != null) {
            refreshListController.setPullDownRefreshEnabled(true);
            refreshListController.setAutoPullDownRefresh(true);
        } else {
            refreshListController.setPullDownRefreshEnabled(false);
            refreshListController.setAutoPullDownRefresh(false);
        }
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
        titleText.setText(context.getResources().getString(R.string.replenish_material_table_edit));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));

        planNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        actualNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        actualNum.setEditable(false);
        bucket.setEditable(false);

        customListWidgetName.setText(context.getResources().getString(R.string.replenish_material_records));
        customListWidgetEdit.setVisibility(View.GONE);

        if (pendingEntity == null) {
            updateView();
        }

        initWorkFlow();
    }

    private void initWorkFlow() {
        // 新增单据
        if (mWorkFlowButtonInfo != null) {
            getController(WorkFlowViewController.class).initStartWorkFlowView(workFlowView, mWorkFlowButtonInfo.getDeploymentId());
            getController(GetPowerCodeController.class).initPowerCode(mWorkFlowButtonInfo.getCode());
        }
        // 发起工作流程
        if (startProcessEntity != null) {
            getController(WorkFlowViewController.class).initStartWorkFlowView(workFlowView, getIntent().getLongExtra(Constant.IntentKey.STARTPROCESS_DEPLOYMENTID, 0));

//            getController(GetPowerCodeController.class).initPowerCode(startProcessEntity.getCode());
        }

        // 补料单列表
        if (mReplenishMaterialTableEntity != null && mReplenishMaterialTableEntity.getId() != null) {
            getController(WorkFlowViewController.class).initPendingWorkFlowView(workFlowView, mReplenishMaterialTableEntity.getPending().id);
            getController(GetPowerCodeController.class).initPowerCode(mReplenishMaterialTableEntity.getPending().activityName);
        }
        // 主页代办
        if (pendingEntity != null) {
            getController(WorkFlowViewController.class).initPendingWorkFlowView(workFlowView, pendingEntity.id);
            getController(GetPowerCodeController.class).initPowerCode(pendingEntity.activityName);
        }
    }

    private void updateView() {
        if (mReplenishMaterialTableEntity.getEquipment() != null && mReplenishMaterialTableEntity.getEquipment().getFeedStockType() != null
                && ReplenishConstant.SystemCode.MODEL_AIR.equals(mReplenishMaterialTableEntity.getEquipment().getFeedStockType().id)) {
            bucket.setVisibility(View.GONE);
        }

        if (mReplenishMaterialTableEntity.getEquipment() != null && mReplenishMaterialTableEntity.getEquipment().getRunModel() != null
                && ReplenishConstant.SystemCode.MODEL_NOTIFY.equals(mReplenishMaterialTableEntity.getEquipment().getRunModel().id)) {
            eamPoint.setEditable(false);
            material.setEditable(false);
            planNum.setEditable(false);
        }

        if (mReplenishMaterialTableEntity.getOperator() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getOperator().code)) {
            // 操作人
            StaffEntity exeStaff = new StaffEntity();
            exeStaff.id = SupPlantApplication.getAccountInfo().staffId;
            exeStaff.name = SupPlantApplication.getAccountInfo().staffName;
            mReplenishMaterialTableEntity.setOperator(exeStaff);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (pendingEntity == null) {
            updateData();
        }
    }

    private void updateData() {
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
        // 新增单据:状态
        SystemCodeEntity state = new SystemCodeEntity();
        state.id = ReplenishConstant.SystemCode.TABLE_STATE_NEW;
        mReplenishMaterialTableEntity.setFmState(state);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
            }
        });

        refreshListController.setOnRefreshListener(() -> {
            if (pendingEntity != null) {
                presenterRouter.create(ReplenishMaterialTableEditAPI.class).getTableInfo(pendingEntity.modelId, pendingEntity.id);
            }
            presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                    ReplenishConstant.URL.REPLENISH_MATERIAL_TABLE_EDIT_DG_LIST_URL + "&id=" + mReplenishMaterialTableEntity.getId(), "");
        });

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
        customListWidgetAdd.setOnClickListener(v -> addItem(null,null));
        mReplenishMaterialRecordsEditAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mCurrentPosition = position;
            mReplenishMaterialTablePartEntity = (ReplenishMaterialTablePartEntity) obj;
            switch (childView.getTag().toString()) {
                case "itemViewDelBtn":
                    mReplenishMaterialRecordsEditAdapter.getList().remove(obj);
                    mReplenishMaterialRecordsEditAdapter.notifyItemRangeRemoved(position, 1);
                    mReplenishMaterialRecordsEditAdapter.notifyItemRangeChanged(position, mReplenishMaterialRecordsEditAdapter.getItemCount() - position);
                    if (mReplenishMaterialTablePartEntity.getId() != null) {
                        dgDeletedIds += mReplenishMaterialTablePartEntity.getId() + ",";
                    }
                    updateActualNum();
                    break;
                case "numEt":
                    updateActualNum();
                    break;
                default:
            }
        });

        workFlowView.setOnChildViewClickListener((childView, action, obj) -> {
            WorkFlowVar workFlowVar = (WorkFlowVar) obj;
            switch (action) {
                case 0:
                    submitReport(workFlowVar, Constant.OperateType.SAVE);
                    break;
                case 1:
                case 2:
                    submitReport(workFlowVar, Constant.OperateType.SUBMIT);
                    break;
                case 4:
                    break;
                default:

            }
        });

    }

    private void addItem(QrCodeEntity qrCodeEntity, ReplenishMaterialTablePartEntity mReplenishMaterialTablePartEntity) {
        // 防错
        if (qrCodeEntity != null && !mReplenishMaterialTableEntity.getMaterial().code.equals(qrCodeEntity.getCode())) {
            ToastUtils.show(context, getResources().getString(R.string.replenish_no_match_current_material));
            return;
        }
        ReplenishMaterialTablePartEntity replenishMaterialTablePartEntity = new ReplenishMaterialTablePartEntity();
        if (qrCodeEntity != null) {
            replenishMaterialTablePartEntity.setBatch(qrCodeEntity.getBatch());
            replenishMaterialTablePartEntity.setFmNumber(qrCodeEntity.getNum());
        }
        if (mReplenishMaterialTablePartEntity != null) {
            replenishMaterialTablePartEntity = mReplenishMaterialTablePartEntity;
        }
        replenishMaterialTablePartEntity.setFmTime(System.currentTimeMillis());
        replenishMaterialTablePartEntity.setFmBill(mReplenishMaterialTableEntity);

        if (mReplenishMaterialRecordsEditAdapter.getItemCount() <= 0) {
            mReplenishMaterialRecordsEditAdapter.addData(replenishMaterialTablePartEntity);
        } else {
            mReplenishMaterialRecordsEditAdapter.getList().add(0, replenishMaterialTablePartEntity);
        }
        mReplenishMaterialRecordsEditAdapter.notifyItemRangeInserted(0, 1);
        mReplenishMaterialRecordsEditAdapter.notifyItemRangeChanged(0, 1);
        contentView.smoothScrollToPosition(0);
        // 更新表头实际数量
        updateActualNum();
    }

    private void updateActualNum() {
        BigDecimal totalNum = new BigDecimal(0);
        for (ReplenishMaterialTablePartEntity entity : mReplenishMaterialRecordsEditAdapter.getList()) {
            totalNum = totalNum.add(entity.getFmNumber() == null ? new BigDecimal(0) : entity.getFmNumber());
        }
        actualNum.setContent(totalNum.toString());
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
//        if (mReplenishMaterialRecordsEditAdapter.getList() != null){
//            for (ReplenishMaterialTablePartEntity partEntity :  mReplenishMaterialRecordsEditAdapter.getList()){
//                partEntity.setFmBill(null);
//            }
//        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        dgListEntity.setDg(mReplenishMaterialRecordsEditAdapter.getList() == null ? null : gson.toJson(mReplenishMaterialRecordsEditAdapter.getList()));
//        dgListEntity.setDg(mReplenishMaterialRecordsEditAdapter.getList() == null ? null : GsonUtil.gsonStringSerializeNulls(mReplenishMaterialRecordsEditAdapter.getList()));
        replenishMaterialTableDTO.setDgList(dgListEntity);
        ReplenishMaterialTableDTO.DgListEntity dgDeletedIdsList = new ReplenishMaterialTableDTO.DgListEntity();
        dgDeletedIdsList.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        replenishMaterialTableDTO.setDgDeletedIds(dgDeletedIdsList);


        replenishMaterialTableDTO.setWorkFlowVar(getWorkFlowVar(workFlowVar, Constant.OperateType.SUBMIT.equals(operate)));
        replenishMaterialTableDTO.setOperateType(operate);

        DeploymentEntity deploymentEntity = getController(WorkFlowViewController.class).getDeploymentEntity();
        replenishMaterialTableDTO.setDeploymentId(String.valueOf(deploymentEntity.deploymentId));
        replenishMaterialTableDTO.setActivityName(deploymentEntity.code);
        replenishMaterialTableDTO.setTaskDescription(deploymentEntity.name);
        replenishMaterialTableDTO.setViewCode(deploymentEntity.viewCode);
        if (mReplenishMaterialTableEntity.getPending() != null) {
            replenishMaterialTableDTO.setPendingId(String.valueOf(mReplenishMaterialTableEntity.getPending().id));
        }

        // bap 6.0  传输会单据异常
        mReplenishMaterialTableEntity.setPending(null);

        replenishMaterialTableDTO.setIds2del("");

        String pc;
        if (startProcessEntity != null) {
            pc = UrlUtil.getPc(startProcessEntity.openUrl);
        } else {
            pc = getController(GetPowerCodeController.class).getPowerCodeResult();
        }
        presenterRouter.create(ReplenishMaterialTableEditAPI.class).submit(mReplenishMaterialTableEntity.getId(), pc, replenishMaterialTableDTO);

    }

    private WorkFlowVarDTO getWorkFlowVar(WorkFlowVar workFlowVar, boolean submit) {
        WorkFlowVarDTO workFlowVarDTO = new WorkFlowVarDTO();
        if (submit) {
            workFlowVarDTO.setOutcomeMapJson(workFlowVar.outcomeMapJson.toString());
            workFlowVarDTO.setOutcome(workFlowVar.outCome);
            workFlowVarDTO.setOutcomeType(workFlowVar.type);
        }
        workFlowVarDTO.setComment(workFlowView.getComment());
//        workFlowVarDTO.setActivityName(mReplenishMaterialTableEntity.getPending().activityName);
//        workFlowVarDTO.setActivityType(mReplenishMaterialTableEntity.getPending().activityType);
        return workFlowVarDTO;
    }


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
        if (mReplenishMaterialTableEntity.getEquipment() != null && mReplenishMaterialTableEntity.getEquipment().getFeedStockType() != null
                && !ReplenishConstant.SystemCode.MODEL_AIR.equals(mReplenishMaterialTableEntity.getEquipment().getFeedStockType().id)) {
            if (mReplenishMaterialTableEntity.getVessel() == null || TextUtils.isEmpty(mReplenishMaterialTableEntity.getVessel().getCode())) {
                ToastUtils.show(context, context.getResources().getString(R.string.replenish_choose_or_scan_bucket));
                return true;
            }
        }
        if (mReplenishMaterialRecordsEditAdapter.getList() == null || mReplenishMaterialRecordsEditAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (ReplenishMaterialTablePartEntity replenishMaterialTablePartEntity : mReplenishMaterialRecordsEditAdapter.getList()) {
            if (TextUtils.isEmpty(replenishMaterialTablePartEntity.getBatch())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mReplenishMaterialRecordsEditAdapter.getList().indexOf(replenishMaterialTablePartEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (replenishMaterialTablePartEntity.getFmNumber() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mReplenishMaterialRecordsEditAdapter.getList().indexOf(replenishMaterialTablePartEntity) + 1) + context.getResources().getString(R.string.wom_pleasr_write_num));
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
    public void getTableInfoSuccess(ReplenishMaterialTableEntity entity) {
        mReplenishMaterialTableEntity = entity == null ? new ReplenishMaterialTableEntity() : entity;
        updateView();
        updateData();
    }

    @Override
    public void getTableInfoFailed(String errorMsg) {
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

    @Override
    public void getBucketStateSuccess(BAP5CommonEntity entity) {
        VesselEntity vesselEntity = new VesselEntity();
        vesselEntity.setId(qrCodeEntity.getId());
        vesselEntity.setCode(qrCodeEntity.getCode());
        vesselEntity.setName(qrCodeEntity.getName());
        bucket.setContent(qrCodeEntity.getName() + "(" + qrCodeEntity.getCode() + ")");
        mReplenishMaterialTableEntity.setVessel(vesselEntity);
        // 补料明细处理
        if (entity.data != null){
            onLoadSuccess();
            List<BucketDetailEntity> list = (List<BucketDetailEntity>) entity.data;
            BucketDetailEntity bucketDetailEntity = list.get(0);
            if (bucketDetailEntity != null){
                ReplenishMaterialTablePartEntity partEntity = new ReplenishMaterialTablePartEntity();
                partEntity.setBatch(bucketDetailEntity.getBatch());
                partEntity.setFmNumber(bucketDetailEntity.getMaterilNumber());
                addItem(null,partEntity);
            }
        }else {
           onLoadSuccess(entity.message);
        }
    }

    @Override
    public void getBucketStateFailed(String errorMsg) {
        onLoadFailed(errorMsg);
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
            // 是否不需要绑桶
            if (mReplenishMaterialTableEntity.getEquipment() != null && mReplenishMaterialTableEntity.getEquipment().getFeedStockType() != null
                    && ReplenishConstant.SystemCode.MODEL_AIR.equals(mReplenishMaterialTableEntity.getEquipment().getFeedStockType().id)) {
                bucket.setVisibility(View.GONE);
            } else {
                bucket.setVisibility(View.VISIBLE);
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
    public void getScanResult(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)) {
            qrCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);
            if (qrCodeEntity != null) {
                switch (qrCodeEntity.getType()) {
                    // 扫描设备
                    case 0:
                        AssociatedEquipmentEntity associatedEquipmentEntity = new AssociatedEquipmentEntity();
                        associatedEquipmentEntity.setId(qrCodeEntity.getId());
                        associatedEquipmentEntity.setCode(qrCodeEntity.getCode());
                        associatedEquipmentEntity.setName(qrCodeEntity.getName());
                        eamPoint.setContent(associatedEquipmentEntity.getName() + "(" + associatedEquipmentEntity.getCode() + ")");
                        mReplenishMaterialTableEntity.setEquipment(associatedEquipmentEntity);
                        break;
                    // 扫描桶
                    case 1:
                        if (mReplenishMaterialTableEntity.getEquipment() != null && mReplenishMaterialTableEntity.getEquipment().getFeedStockType() != null
                                && !ReplenishConstant.SystemCode.MODEL_AIR.equals(mReplenishMaterialTableEntity.getEquipment().getFeedStockType().id)) {
                            queryBucketState();
                        }
                        break;
                    // 扫描物料
                    case 2:
                        // 目前暂时按照MES产品定义格式
                        addItem(qrCodeEntity,null);
                        break;
                    default:
                }
            }
        }
    }

    private void queryBucketState() {
        onLoading(getResources().getString(R.string.wom_dealing));
        presenterRouter.create(ReplenishMaterialTableEditAPI.class).getBucketState(mReplenishMaterialTableEntity.getId(), qrCodeEntity.getCode());
    }
}
