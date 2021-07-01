package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
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
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.VesselEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.controller.ProduceTaskEndReportDetailController;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.api.ProcessOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.ProduceEndTaskAPI;
import com.supcon.mes.module_wom_producetask.model.api.ProduceTaskOperateAPI;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceEndTaskContract;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.dto.ProduceEndTaskDTO;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.presenter.ProduceEndTaskPresenter;
import com.supcon.mes.module_wom_producetask.presenter.ProduceTaskOperatePresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.ProduceTaskEndReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/1
 * Email zhangwenshuai1@supcon.com
 * Desc 指令单结束报工
 */
@Router(Constant.Router.WOM_PRODUCE_TASK_END_REPORT)
@Controller(value = {CommonScanController.class})
@Presenter(value = {ProduceTaskOperatePresenter.class, ProduceEndTaskPresenter.class, CommonListPresenter.class})
public class ProduceTaskEndReportActivity extends BaseRefreshRecyclerActivity<OutputDetailEntity> implements ProduceTaskOperateContract.View, ProduceEndTaskContract.View, CommonListContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleSetting")
    ImageView titleSetting;
    @BindByTag("productName")
    CustomTextView productName;
    @BindByTag("productCode")
    CustomTextView productCode;
    @BindByTag("planNum")
    CustomTextView planNum;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("saveBtn")
    Button saveBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("customListWidgetIc")
    ImageView customListWidgetIc;
    @BindByTag("customListWidgetName")
    TextView customListWidgetName;
    @BindByTag("customListWidgetEdit")
    ImageView customListWidgetEdit;
    @BindByTag("customListWidgetAdd")
    ImageView customListWidgetAdd;

    private ProduceTaskEndReportDetailAdapter mProduceTaskEndReportDetailAdapter;
//    private ProduceTaskEndReportDetailController mProduceTaskEndReportDetailController;

    private WaitPutinRecordEntity mWaitPutinRecordEntity;
    private OutputDetailEntity mOutputDetailEntity;
    private int mCurrentPosition;
    private String dgDeletedIds = "";
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    // 已保存报工明细
    private List<OutputDetailEntity> saveOutputDetailEntityList;

    @Override
    protected IListAdapter<OutputDetailEntity> createAdapter() {
        mProduceTaskEndReportDetailAdapter = new ProduceTaskEndReportDetailAdapter(context);
        return mProduceTaskEndReportDetailAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_produce_task_end_report;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        mWaitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);

        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0, 0, 0, DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_produce_task_end_report));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));

        productName.setContent(mWaitPutinRecordEntity.getProductId().getName());
        productCode.setContent(mWaitPutinRecordEntity.getProductId().getCode());
        planNum.setContent(String.valueOf(mWaitPutinRecordEntity.getTaskId().getActualPlanNum()));

        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
//        customListWidgetEdit.setVisibility(View.GONE);
        customListWidgetEdit.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_work_describe));

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName()));
        customListWidgetEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,mWaitPutinRecordEntity);
            IntentRouter.go(context,WomConstant.Router.REPORT_DETAIL_LIST,bundle);
        });
        refreshListController.setOnRefreshListener(() -> presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                WomConstant.URL.PRODUCE_END_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()), ""));
        customListWidgetAdd.setOnClickListener(v -> {
            OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
            outputDetailEntity.setProduct(mWaitPutinRecordEntity.getProductId()); // 产品
            outputDetailEntity.setMaterialBatchNum(mWaitPutinRecordEntity.getProduceBatchNum()); // 生产批默认入库批号
            outputDetailEntity.setOutputNum(mWaitPutinRecordEntity.getTaskId().getActualPlanNum());  // 默认入库数量为计划数量
            outputDetailEntity.setPutinTime(System.currentTimeMillis());  // 报工时间

            if (mProduceTaskEndReportDetailAdapter.getItemCount() <= 0){
                mProduceTaskEndReportDetailAdapter.addData(outputDetailEntity);
            }else {
                mProduceTaskEndReportDetailAdapter.getList().add(0,outputDetailEntity);
            }
            mProduceTaskEndReportDetailAdapter.notifyItemRangeInserted(0, 1);
            mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(0, 1);
            contentView.smoothScrollToPosition(0);

        });

        mProduceTaskEndReportDetailAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mCurrentPosition = position;
            mOutputDetailEntity = (OutputDetailEntity) obj;
            switch (childView.getTag().toString()) {
                case "warehouseTv":
                    IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                    break;
                case "storeSetTv":
                    if (mOutputDetailEntity.getWareId() == null) {
                        ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
                        break;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.WARE_ID, mOutputDetailEntity.getWareId().getId());
                    IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                    break;
                case "itemViewDelBtn":
                    mProduceTaskEndReportDetailAdapter.getList().remove(obj);
                    mProduceTaskEndReportDetailAdapter.notifyItemRangeRemoved(position, 1);
                    mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(position, mProduceTaskEndReportDetailAdapter.getItemCount() - position);
                    if (mOutputDetailEntity.getId() != null) {
                        dgDeletedIds += mOutputDetailEntity.getId() + ",";
                    }
                    break;
                default:
            }
        });
        RxView.clicks(saveBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        saveReport();
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

    private void saveReport() {
        if (checkSubmit()) {
            return;
        }
        onLoading(getString(R.string.wom_dealing));
        for (OutputDetailEntity outputDetailEntity : mProduceTaskEndReportDetailAdapter.getList()) {
            // 尾料处理方式
            if (outputDetailEntity.getRemainNum() == null || outputDetailEntity.getRemainNum().floatValue() == 0) {
                outputDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_03));
            } else {
                outputDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_01));
            }
        }
        ProduceEndTaskDTO produceEndTaskDTO = new ProduceEndTaskDTO();
        produceEndTaskDTO.setOperateType("save");
        produceEndTaskDTO.setIds2del("");
        produceEndTaskDTO.setViewCode("WOM_1.0.0_procReport_outPutCommonTaskEdit");
        produceEndTaskDTO.setWorkFlowVar(new WorkFlowVar());
        produceEndTaskDTO.setProcReport(mWaitPutinRecordEntity.getProcReportId());

        ProduceEndTaskDTO.DgListEntity dgListEntity = new ProduceEndTaskDTO.DgListEntity();
        dgListEntity.setDg(GsonUtil.gsonStringSerializeNulls(mProduceTaskEndReportDetailAdapter.getList()));
        produceEndTaskDTO.setDgList(dgListEntity);

        ProduceEndTaskDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new ProduceEndTaskDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        produceEndTaskDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(ProduceEndTaskAPI.class).save(mWaitPutinRecordEntity.getProcReportId().getId(), produceEndTaskDTO);
    }

    /**
     * 扫描功能：红外、摄像头扫描监听事件
     *
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)) {
            QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);
            if (qrCodeEntity != null && qrCodeEntity.getType() == 1) {
                OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
                // 产品
                outputDetailEntity.setProduct(mWaitPutinRecordEntity.getProductId());
                outputDetailEntity.setMaterialBatchNum(mWaitPutinRecordEntity.getProduceBatchNum()); // 生产批默认入库批号
                outputDetailEntity.setOutputNum(mWaitPutinRecordEntity.getTaskId().getActualPlanNum());  // 默认入库数量为计划数量
                // 报工时间
                outputDetailEntity.setPutinTime(System.currentTimeMillis());
                outputDetailEntity.setPutinEndTime(outputDetailEntity.getPutinTime());
                // 桶编码
                VesselEntity vesselEntity = new VesselEntity();
                vesselEntity.setId(qrCodeEntity.getId());
                vesselEntity.setCode(qrCodeEntity.getCode());
                outputDetailEntity.setVessel(vesselEntity);

                if (mProduceTaskEndReportDetailAdapter.getItemCount() <= 0){
                    mProduceTaskEndReportDetailAdapter.addData(outputDetailEntity);
                }else {
                    mProduceTaskEndReportDetailAdapter.getList().add(0,outputDetailEntity);
                }
                mProduceTaskEndReportDetailAdapter.notifyItemRangeInserted(0, 1);
                mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(0, 1);
                contentView.smoothScrollToPosition(0);
            }
        }
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
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_end_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        customDialog.getDialog().setCanceledOnTouchOutside(true);

        customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_end_task_and_report))
                .bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {
                    onLoading(getString(R.string.wom_dealing));

                    for (OutputDetailEntity outputDetailEntity : mProduceTaskEndReportDetailAdapter.getList()) {
                        // 尾料处理方式
                        if (outputDetailEntity.getRemainNum() == null || outputDetailEntity.getRemainNum().floatValue() == 0) {
                            outputDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_03));
                        } else {
                            outputDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_01));
                        }
                    }

                    presenterRouter.create(ProduceTaskOperateAPI.class).operateProduceTask(mWaitPutinRecordEntity.getId(), "stop", mProduceTaskEndReportDetailAdapter.getList());
                }, true)
                .show();

    }

    public boolean checkSubmit() {
        List<OutputDetailEntity> list = mProduceTaskEndReportDetailAdapter.getList();
        if (saveOutputDetailEntityList != null){
            list.addAll(saveOutputDetailEntityList);
        }
        if (list == null || list.size() <= 0) {
            ToastUtils.show(context, "请添加报工明细");
            return true;
        }

        for (OutputDetailEntity entity : list) {
            if (TextUtils.isEmpty(entity.getMaterialBatchNum())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(entity) + 1) + context.getResources().getString(R.string.wom_please_write_into_ware_batch));
                return true;
            }
//            if (entity.getWareId() == null) {
//                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(entity) + 1) + context.getResources().getString(R.string.wom_select_ware));
//                return true;
//            }
//            if (entity.getWareId() != null && entity.getWareId().getStoreSetState() && entity.getStoreId() == null) {
//                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(entity) + 1) + context.getResources().getString(R.string.wom_warehouse_enable_please_write_storage));
//                return true;
//            }
            if (entity.getOutputNum() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(entity) + 1) + context.getResources().getString(R.string.wom_pleasr_write_num));
                return true;
            }
            if (entity.getOutputNum().floatValue() == 0f) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(entity) + 1) + context.getResources().getString(R.string.wom_num_greater_than_zero));
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
    public void operateProduceTaskSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(getString(R.string.wom_dealt_success));
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void operateProduceTaskFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateDischargeSuccess(BAP5CommonEntity bap5CommonEntity) {

    }

    @Override
    public void operateDischargeFailed(String errorMsg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
            WarehouseEntity warehouseEntity = (WarehouseEntity) selectDataEvent.getEntity();
            if (mOutputDetailEntity.getWareId() != null && !warehouseEntity.getId().equals(mOutputDetailEntity.getWareId().getId())) {
                mOutputDetailEntity.setStoreId(null);
            }
            mOutputDetailEntity.setWareId(warehouseEntity);
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mOutputDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }

    @Override
    public void saveSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Override
    public void saveFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        if (mProduceTaskEndReportDetailAdapter.getList() != null){
            mProduceTaskEndReportDetailAdapter.getList().clear();
        }
        refreshListController.refreshComplete(mProduceTaskEndReportDetailAdapter.getList());

        // 已保存数据另外窗口展示
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        saveOutputDetailEntityList = GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), OutputDetailEntity.class);
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
