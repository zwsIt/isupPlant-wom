package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.PowerCode;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.api.PutInReportAPI;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.BatchPutinDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.presenter.BatchMaterialRecordsRefPresenter;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.presenter.PutInReportPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.PutInReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 人工投配料活动报工
 */
@Router(Constant.Router.WOM_BATCH_PUT_IN_REPORT)
@Presenter(value = {CommonListPresenter.class, PutInReportPresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {GetPowerCodeController.class, CommonScanController.class})
public class BatchPutInActivityReportActivity extends BaseRefreshRecyclerActivity<PutInDetailEntity> implements CommonListContract.View, PutInReportContract.View {
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
    @BindByTag("taskProcess")
    CustomTextView taskProcess;
//    @BindByTag("materialCode")
//    CustomTextView materialCode;
    @BindByTag("planNum")
    CustomTextView planNum;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    private WaitPutinRecordEntity mWaitPutinRecordEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private PutInReportDetailAdapter mPutInReportDetailAdapter;
    private int mCurrentPosition;
    private PutInDetailEntity mPutInDetailEntity;
    private String dgDeletedIds = "";
    private boolean scan;

    @Override
    protected IListAdapter<PutInDetailEntity> createAdapter() {
        mPutInReportDetailAdapter = new PutInReportDetailAdapter(context);
        return mPutInReportDetailAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_batch_put_in_report;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(true);
        mWaitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), 0);
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

        mPutInReportDetailAdapter.setBatchPutInActivity(true);

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(String.format("%s%s", mWaitPutinRecordEntity.getTaskActiveId().getActiveType().value, getString(R.string.wom_report)));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
//        customListWidgetEdit.setVisibility(View.GONE);
        customListWidgetAdd.setImageResource(R.drawable.ic_wxgd_reference);

        taskProcess.setContent(TextUtils.isEmpty(mWaitPutinRecordEntity.getTaskProcessId().getName()) ? mWaitPutinRecordEntity.getProcessName(): mWaitPutinRecordEntity.getTaskProcessId().getName());
        planNum.setContent(mWaitPutinRecordEntity.getTaskId().getPlanNum() == null ? "" : mWaitPutinRecordEntity.getTaskId().getPlanNum().toString());

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
                WomConstant.URL.BATCH_PUT_IN_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()), ""));
        customListWidgetAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, mWaitPutinRecordEntity);
            IntentRouter.go(context, Constant.Router.BATCH_MATERIAL_LIST_REF, bundle);
        });
        mPutInReportDetailAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mCurrentPosition = position;
                mPutInDetailEntity = (PutInDetailEntity) obj;
                switch (childView.getTag().toString()) {
                    case "warehouseTv":
                        IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                        break;
                    case "storeSetTv":
                        if (mPutInDetailEntity.getWareId() == null) {
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
                            break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.WARE_ID, mPutInDetailEntity.getWareId().getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                        break;
                    case "itemViewDelBtn":
                        mPutInReportDetailAdapter.getList().remove(obj);
                        mPutInReportDetailAdapter.notifyItemRangeRemoved(position, 1);
                        mPutInReportDetailAdapter.notifyItemRangeChanged(position, mPutInReportDetailAdapter.getItemCount()-position);
                        if (mPutInDetailEntity.getId() != null) {
                            dgDeletedIds += mPutInDetailEntity.getId() + ",";
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
        BatchPutinDetailDTO batchPutinDetailDTO = new BatchPutinDetailDTO();
        batchPutinDetailDTO.setOperateType("save");
        batchPutinDetailDTO.setIds2del("");
        batchPutinDetailDTO.setViewCode("WOM_1.0.0_procReport_batchFeedBackEdit");
        batchPutinDetailDTO.setWorkFlowVar(new WorkFlowVar());
        batchPutinDetailDTO.setProcReport(mWaitPutinRecordEntity.getProcReportId());

        for (PutInDetailEntity putInDetailEntity : mPutInReportDetailAdapter.getList()) {
            // 尾料处理方式
            if (putInDetailEntity.getRemainId() == null) {
                if (putInDetailEntity.getRemainNum() == null || putInDetailEntity.getRemainNum().floatValue() == 0) {
                    putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_03));
                } else {
                    putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_01));
                }
            }
        }
        BatchPutinDetailDTO.DgListEntity dgListEntity = new BatchPutinDetailDTO.DgListEntity();
        dgListEntity.setDg(GsonUtil.gsonStringSerializeNulls(mPutInReportDetailAdapter.getList()));
        batchPutinDetailDTO.setDgList(dgListEntity);


        BatchPutinDetailDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new BatchPutinDetailDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        batchPutinDetailDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(PutInReportAPI.class).submit(true, mWaitPutinRecordEntity.getProcReportId().getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), null,batchPutinDetailDTO);

    }

    private boolean checkSubmit() {
        List<PutInDetailEntity> list = mPutInReportDetailAdapter.getList();
        if (list == null || list.size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (PutInDetailEntity putInDetailEntity : list) {
            if (WomConstant.SystemCode.MATERIAL_BATCH_02.equals(putInDetailEntity.getMaterialId().getIsBatch().id) && TextUtils.isEmpty(putInDetailEntity.getMaterialBatchNum())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (putInDetailEntity.getWareId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_ware));
                return true;
            }
            if (putInDetailEntity.getWareId() != null && putInDetailEntity.getWareId().getStoreSetState() && putInDetailEntity.getStoreId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_warehouse_enable_please_write_storage));
                return true;
            }
            if (putInDetailEntity.getPutinNum() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_num));
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
        if (scan){
            scan=false;
            List<BatchMaterialPartEntity> list=GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), BatchMaterialPartEntity.class);
            if (!list.isEmpty()){
                addNewBatchMaterialRecords(list);
            }
        }else {
            refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), PutInDetailEntity.class));
        }

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
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)){
            MaterialQRCodeEntity materialQRCodeEntity = MaterQRUtil.materialQRCode(context,codeResultEvent.scanResult);
            if (materialQRCodeEntity == null) return;
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_realize));
//        if (arr != null && arr.length == 8) {
//            String materCode=mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode();
//            specs=arr[7].replace("specs=","");
//            customCondition.put("taskActiveId", mWaitPutinRecordEntity.getTaskActiveId().getId());
//            queryParams.put(Constant.BAPQuery.CODE, incode);
//            scan=true;
//            presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams, WomConstant.URL.BATCH_MATERIAL_LIST_REF_URL, "batMaterilPart");
//
//        } else {
//            ToastUtils.show(context, "二维码退料信息解析异常！");
//        }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof List) {
            // 批量添加配料记录
            List<BatchMaterialPartEntity> batchMaterialPartEntityList = (List<BatchMaterialPartEntity>) selectDataEvent.getEntity();
            addNewBatchMaterialRecords(batchMaterialPartEntityList);
        } else {
            if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
                WarehouseEntity warehouseEntity = (WarehouseEntity) selectDataEvent.getEntity();
                if (mPutInDetailEntity.getWareId() != null && !warehouseEntity.getId().equals(mPutInDetailEntity.getWareId().getId())){
                    mPutInDetailEntity.setStoreId(null);
                }
                mPutInDetailEntity.setWareId(warehouseEntity);
            } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
                mPutInDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
            }
            mPutInReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
        }

    }

    /**
     * 批量添加配料记录
     *
     * @param list
     */
    private void addNewBatchMaterialRecords(List<BatchMaterialPartEntity> list) {
        List<Long> currentListId = new ArrayList<>();
        for (PutInDetailEntity putInDetailEntity : mPutInReportDetailAdapter.getList()) {
            currentListId.add(putInDetailEntity.getBatchingRecordId());
        }
        PutInDetailEntity putInDetailEntity;
        List<PutInDetailEntity> putInDetailEntityList = new ArrayList<>();
        for (BatchMaterialPartEntity entity : list){
            // 防止重复添加
            if (currentListId.contains(entity.getId())){
                continue;
            }
            putInDetailEntity = new PutInDetailEntity();
            putInDetailEntity.setBatchingRecordId(entity.getId()); // require
            putInDetailEntity.setMaterialId(entity.getMaterialId());
            putInDetailEntity.setMaterialBatchNum(entity.getMaterialBatchNum());
            putInDetailEntity.setWareId(entity.getWareId());
            putInDetailEntity.setStoreId(entity.getStoreId());
            putInDetailEntity.setPutinNum(entity.getOfferNum());
            putInDetailEntity.setAvailableNum(entity.getOfferNum());
            putInDetailEntity.setPutinTime(new Date().getTime());
            putInDetailEntityList.add(putInDetailEntity);
        }
        if (putInDetailEntityList.size() <= 0){
            return;
        }
        mPutInReportDetailAdapter.addList(putInDetailEntityList);
        mPutInReportDetailAdapter.notifyItemRangeInserted(mPutInReportDetailAdapter.getList().size() - putInDetailEntityList.size(), putInDetailEntityList.size());
        mPutInReportDetailAdapter.notifyItemRangeChanged(mPutInReportDetailAdapter.getList().size() - putInDetailEntityList.size(), putInDetailEntityList.size());
    }


}
