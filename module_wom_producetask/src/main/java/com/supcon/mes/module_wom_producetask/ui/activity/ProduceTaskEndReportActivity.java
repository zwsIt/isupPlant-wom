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
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomListWidget;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
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
import com.supcon.mes.module_wom_producetask.model.api.ProduceTaskOperateAPI;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.presenter.ProduceTaskOperatePresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.ProduceTaskEndReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
@Presenter(value = {ProduceTaskOperatePresenter.class})
public class ProduceTaskEndReportActivity extends BaseRefreshRecyclerActivity<OutputDetailEntity> implements ProduceTaskOperateContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("productName")
    CustomTextView productName;
    @BindByTag("productCode")
    CustomTextView productCode;
    @BindByTag("planNum")
    CustomTextView planNum;
    @BindByTag("submitBtn")
    Button submitBtn;
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
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        mWaitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);

        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0,0,0, DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_produce_task_end_report));
        rightBtn.setVisibility(View.GONE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));

        productName.setContent(mWaitPutinRecordEntity.getProductId().getName());
        productCode.setContent(mWaitPutinRecordEntity.getProductId().getCode());
        planNum.setContent(String.valueOf(mWaitPutinRecordEntity.getTaskId().getActualPlanNum()));

        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
        customListWidgetEdit.setVisibility(View.GONE);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
        });
        customListWidgetAdd.setOnClickListener(v -> {
            OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
            outputDetailEntity.setProduct(mWaitPutinRecordEntity.getProductId()); // 产品
            outputDetailEntity.setMaterialBatchNum(mWaitPutinRecordEntity.getProduceBatchNum()); // 生产批默认入库批号
            outputDetailEntity.setOutputNum(mWaitPutinRecordEntity.getTaskId().getActualPlanNum());  // 默认入库数量为回掺计划数量
            outputDetailEntity.setPutinTime(System.currentTimeMillis());  // 报工时间
            outputDetailEntity.setPutinEndTime(outputDetailEntity.getPutinTime());
            mProduceTaskEndReportDetailAdapter.addData(outputDetailEntity);
            mProduceTaskEndReportDetailAdapter.notifyItemRangeInserted(mProduceTaskEndReportDetailAdapter.getItemCount() - 1, 1);
            mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mProduceTaskEndReportDetailAdapter.getItemCount() - 1, 1);

            contentView.smoothScrollToPosition(mProduceTaskEndReportDetailAdapter.getItemCount() - 1);

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
                default:
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
     * 扫描功能：红外、摄像头扫描监听事件
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)){
            MaterialQRCodeEntity materialQRCodeEntity = MaterQRUtil.materialQRCode(context,codeResultEvent.scanResult);
            if (materialQRCodeEntity == null) return;
            if (!mWaitPutinRecordEntity.getProductId().getCode().equals(materialQRCodeEntity.getMaterial().getCode())){
                ToastUtils.show(context, context.getResources().getString(R.string.wom_scan_material_error));
                return;
            }
            if (!materialQRCodeEntity.getMaterialBatchNo().equals(mWaitPutinRecordEntity.getProduceBatchNum())){
                ToastUtils.show(context, context.getResources().getString(R.string.wom_scan_batchNo_error));
                return;
            }

            if(materialQRCodeEntity.isRequest()){
                // TODO...
            }else {
                OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
                outputDetailEntity.setProduct(mWaitPutinRecordEntity.getProductId()); // 产品
                outputDetailEntity.setMaterialBatchNum(materialQRCodeEntity.getMaterialBatchNo()); // 生产批默认入库批号
                outputDetailEntity.setOutputNum(materialQRCodeEntity.getNum());  // 扫描数量
                outputDetailEntity.setWareId(materialQRCodeEntity.getToWare());
                outputDetailEntity.setStoreId(materialQRCodeEntity.getToStore());
                outputDetailEntity.setPutinTime(System.currentTimeMillis());  // 报工时间
                outputDetailEntity.setPutinEndTime(outputDetailEntity.getPutinTime());
                mProduceTaskEndReportDetailAdapter.addData(outputDetailEntity);
                mProduceTaskEndReportDetailAdapter.notifyItemRangeInserted(mProduceTaskEndReportDetailAdapter.getItemCount() - 1, 1);
                mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mProduceTaskEndReportDetailAdapter.getItemCount() - 1, 1);

                contentView.smoothScrollToPosition(mProduceTaskEndReportDetailAdapter.getItemCount() - 1);
            }
        }
    }
    /**
     * @author zhangwenshuai1 2020/4/2
     * @param
     * @return
     * @description 报工
     *
     */
    private void submitReport() {
        if (checkSubmit()){
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
        presenterRouter.create(ProduceTaskOperateAPI.class).operateProduceTask(mWaitPutinRecordEntity.getId(),"stop",mProduceTaskEndReportDetailAdapter.getList());

    }

    public boolean checkSubmit() {
        List<OutputDetailEntity> list = mProduceTaskEndReportDetailAdapter.getList();
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
            if (mOutputDetailEntity.getWareId() != null && !warehouseEntity.getId().equals(mOutputDetailEntity.getWareId().getId())){
                mOutputDetailEntity.setStoreId(null);
            }
            mOutputDetailEntity.setWareId(warehouseEntity);
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mOutputDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }

}
