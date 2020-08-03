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
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
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
    private boolean mShouldScroll;
    private LinearSmoothScroller mLinearSmoothScroller;

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
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));

        productName.setContent(mWaitPutinRecordEntity.getProductId().getName());
        productCode.setContent(mWaitPutinRecordEntity.getProductId().getCode());
        planNum.setContent(String.valueOf(mWaitPutinRecordEntity.getTaskId().getPlanNum()));

        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
        customListWidgetEdit.setVisibility(View.GONE);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        getController(CommonScanController.class).openInfrared();
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan();
        });
        customListWidgetAdd.setOnClickListener(v -> {
            OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
            outputDetailEntity.setProduct(mWaitPutinRecordEntity.getProductId()); // 产品
            outputDetailEntity.setMaterialBatchNum(mWaitPutinRecordEntity.getProduceBatchNum()); // 生产批默认入库批号
            outputDetailEntity.setOutputNum(mWaitPutinRecordEntity.getTaskId().getPlanNum());  // 默认入库数量为计划数量
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
                        ToastUtils.show(context, "请先选择仓库");
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
        String[] arr = MaterQRUtil.materialQRCode(codeResultEvent.scanResult);
        if (arr != null && arr.length == 8) {
            String incode = arr[0].replace("incode=", "");
            String batchno = arr[1].replace("batchno=", "");
            String batchno2 = arr[2].replace("batchno2=", "");
            String packqty = arr[3].replace("packqty=", "");
            String packs = arr[4].replace("packs=", "");
            String purcode = arr[5].replace("purcode=", "");
            String orderno = arr[6].replace("orderno=", "");
            String specs=arr[7].replace("specs=","");
            if (mWaitPutinRecordEntity.getProductId().getCode().equals(incode)){
                OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
                outputDetailEntity.setProduct(mWaitPutinRecordEntity.getProductId()); // 产品
                if (!TextUtils.isEmpty(mWaitPutinRecordEntity.getProduceBatchNum()) && !mWaitPutinRecordEntity.getProduceBatchNum().equals(batchno)){
                    ToastUtils.show(context,"非当前指令单物料批号，请重新扫描");
                    return;
                }
                outputDetailEntity.setMaterialBatchNum(batchno); // 生产批默认入库批号
                outputDetailEntity.setOutputNum(!TextUtils.isEmpty(specs)?new BigDecimal(specs) :mWaitPutinRecordEntity.getTaskId().getPlanNum());  // 默认入库数量为计划数量
                mProduceTaskEndReportDetailAdapter.addData(outputDetailEntity);
                mProduceTaskEndReportDetailAdapter.notifyItemRangeInserted(mProduceTaskEndReportDetailAdapter.getItemCount() - 1, 1);
                mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mProduceTaskEndReportDetailAdapter.getItemCount() - 1, 1);

                contentView.smoothScrollToPosition(mProduceTaskEndReportDetailAdapter.getItemCount() - 1);
            }else {
                ToastUtils.show(context,"非当前指令单物料，请重新扫描");
            }
        } else {
            ToastUtils.show(context, "二维码退料信息解析异常！");
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
        presenterRouter.create(ProduceTaskOperateAPI.class).operateProduceTask(mWaitPutinRecordEntity.getId(),"stop",mProduceTaskEndReportDetailAdapter.getList());

    }

    public boolean checkSubmit() {
        if (mProduceTaskEndReportDetailAdapter.getList() == null || mProduceTaskEndReportDetailAdapter.getList().size() <= 0) {
            ToastUtils.show(context, "请添加报工明细");
            return true;
        }
        List<OutputDetailEntity> list = mProduceTaskEndReportDetailAdapter.getList();
        for (OutputDetailEntity entity : list) {
            if (TextUtils.isEmpty(entity.getMaterialBatchNum())) {
                ToastUtils.show(context, "第【" + (list.indexOf(entity) + 1) + "】项请填写入库批号");
                return true;
            }
            if (entity.getWareId() == null) {
                ToastUtils.show(context, "第【" + (list.indexOf(entity) + 1) + "】项请选择仓库");
                return true;
            }
            if (entity.getOutputNum() == null) {
                ToastUtils.show(context, "第【" + (list.indexOf(entity) + 1) + "】项请填写数量");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
            mOutputDetailEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mOutputDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }

}
