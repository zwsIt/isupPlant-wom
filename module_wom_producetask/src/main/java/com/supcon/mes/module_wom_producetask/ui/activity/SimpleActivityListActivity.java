package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.ActivityOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.ActivityOperatePresenter;
import com.supcon.mes.module_wom_producetask.presenter.WaitPutinRecordPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.SimpleActivityListAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 普通工单活动list
 */
@Router(value = Constant.Router.WOM_SIMPLE_ACTIVITY_LIST)
@Presenter(value = {WaitPutinRecordPresenter.class, ActivityOperatePresenter.class})
@Controller(value = {CommonScanController.class})
public class SimpleActivityListActivity extends BaseRefreshRecyclerActivity<WaitPutinRecordEntity> implements WaitPutinRecordsListContract.View, ActivityOperateContract.View{

    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleSetting")
    ImageView titleSetting;

    private SimpleActivityListAdapter mSimpleActivityListAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // 活动查询
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // 当前操作项
    private WaitPutinRecordEntity mWaitPutinRecordParam; // 工单待办参数

    @Override
    protected IListAdapter<WaitPutinRecordEntity> createAdapter() {
        mSimpleActivityListAdapter = new SimpleActivityListAdapter(context);
        return mSimpleActivityListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), 0);
            }
        });

        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_ACTIVE); // 默认活动查询
        queryParams.put(Constant.BAPQuery.IS_MORE_OTHER, false); // 非其他活动
        mWaitPutinRecordParam = (WaitPutinRecordEntity)getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM, mWaitPutinRecordParam.getProduceBatchNum()); // 当前生产批

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText(getResources().getString(R.string.wom_activity_list));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.ic_wts_reference_white);

        if (isPack()){
            titleSetting.setVisibility(View.VISIBLE);
            titleSetting.setImageResource(R.drawable.ic_scan);
        }

    }
    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        titleSetting.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
        });
        RxView.clicks(rightBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o->{
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,mWaitPutinRecordParam);
                    IntentRouter.go(context,Constant.Router.ACTIVITY_EXEREDS_LIST,bundle);
                });
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(pageIndex, 20, queryParams,false);
        });

        mSimpleActivityListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mWaitPutinRecordEntity = (WaitPutinRecordEntity) obj;
            String tag = String.valueOf(childView.getTag());
            switch (tag) {
                case "routineStartTv":
                case "putInStartTv":
                    showOperateConfirmDialog(mWaitPutinRecordEntity.getTaskActiveId().getCheckTip());
                    break;
            }

        });

    }

    /**
     * @param
     * @param checkTip
     * @return
     * @description 活动操作确认：开启/结束
     * @author zhangwenshuai1 2020/3/25
     */
    private void showOperateConfirmDialog(String checkTip) {
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(mWaitPutinRecordEntity.getExeState().id)) {
            customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_start_activity_operate))
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(context.getResources().getString(R.string.wom_dealing));
                        presenterRouter.create(ActivityOperateAPI.class).operateActivity(mWaitPutinRecordEntity.getId(), null, false);
                    }, true)
                    .show();
        } else {
            customDialog.bindView(R.id.tipContentTv, TextUtils.isEmpty(checkTip) ? context.getResources().getString(R.string.wom_end_activity_operate) : checkTip)
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(context.getResources().getString(R.string.wom_dealing));
                        presenterRouter.create(ActivityOperateAPI.class).operateActivity(mWaitPutinRecordEntity.getId(),null, true);
                    }, true)
                    .show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        refreshListController.refreshBegin();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSelectDataEvent(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof FactoryModelEntity){
            mWaitPutinRecordEntity.setEuqId((FactoryModelEntity) selectDataEvent.getEntity());
            mWaitPutinRecordEntity.getTaskProcessId().setEquipmentId((FactoryModelEntity) selectDataEvent.getEntity());
        }
    }
    /**
     * 扫描功能：红外、摄像头扫描监听事件
     *
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)) {
            QrCodeEntity materialQRCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);
            if (materialQRCodeEntity == null) return;
            if (materialQRCodeEntity.getType() == 0){
                // 包装 机台校验
                int count = 0;
                for (WaitPutinRecordEntity waitPutinRecordEntity : mSimpleActivityListAdapter.getList()){
                    if (materialQRCodeEntity.getCode().equals(waitPutinRecordEntity.getEuqId() == null ? "" : waitPutinRecordEntity.getEuqId().getCode())){
                       Bundle bundle = new Bundle();
                        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD, waitPutinRecordEntity);
                        IntentRouter.go(context, WomConstant.Router.WOM_PACK_REPORT, bundle);
                        break;
                    }
                    count ++;
                }
                if (count == mSimpleActivityListAdapter.getList().size()){
                    ToastUtils.show(context, getResources().getString(R.string.wom_no_match_pack_machine));
                }
            }else {
                ToastUtils.show(context, getResources().getString(R.string.wom_please_scan_pack_machine_qr));
            }
        }

    }

    @Override
    public void listWaitPutinRecordsSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);

    }

    @Override
    public void listWaitPutinRecordsFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }


    @Override
    public void operateActivitySuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Override
    public void operateActivityFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    public boolean isPack() {
        return mWaitPutinRecordParam.getTaskId().isNeedPack();
    }
}
