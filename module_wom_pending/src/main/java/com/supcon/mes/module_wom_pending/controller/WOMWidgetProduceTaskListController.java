package com.supcon.mes.module_wom_pending.controller;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_pending.R;
import com.supcon.mes.module_wom_pending.ui.adapter.WOMWidgetProduceTaskAdapter;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.ProduceTaskOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.ProduceTaskOperatePresenter;
import com.supcon.mes.module_wom_producetask.presenter.WaitPutinRecordPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 标准生产工单list
 */
@Presenter(value = {WaitPutinRecordPresenter.class, ProduceTaskOperatePresenter.class})
public class WOMWidgetProduceTaskListController extends BaseViewController implements WaitPutinRecordsListContract.View,
        ProduceTaskOperateContract.View{

    @BindByTag("womPendingProduceTaskListView")
    RecyclerView womPendingProduceTaskListView;
    @BindByTag("noDataTv")
    TextView noDataTv;
    private WOMWidgetProduceTaskAdapter mProduceTaskListAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // 指令单查询
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // 当前操作项

    public WOMWidgetProduceTaskListController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_TASK);
    }

    @Override
    public void initView() {
        super.initView();
        // 默认标准指令单查询

        mProduceTaskListAdapter = new WOMWidgetProduceTaskAdapter(context);
        womPendingProduceTaskListView.setLayoutManager(new LinearLayoutManager(context));
        womPendingProduceTaskListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(2, context), DisplayUtil.dip2px(10, context), 0);
            }
        });
        womPendingProduceTaskListView.setAdapter(mProduceTaskListAdapter);
    }
    @Override
    public void initData() {
        super.initData();

    }

    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();

        mProduceTaskListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mWaitPutinRecordEntity = (WaitPutinRecordEntity) obj;
            String tag = String.valueOf(childView.getTag());
            List<Object> paramsList = new ArrayList<>(2);
            switch (tag) {
                case "startTv":
                    paramsList.add("开启");
                    paramsList.add("start");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "pauseTv":
                    paramsList.add("暂停");
                    paramsList.add("pause");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "resumeTv":
                    paramsList.add("恢复");
                    paramsList.add("resume");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "stopTv":
                    // 结束工单报工

                    if(mWaitPutinRecordEntity!=null && mWaitPutinRecordEntity.getTaskId()!=null
                            && mWaitPutinRecordEntity.getTaskId().getBatchContral()!=null
                            &&mWaitPutinRecordEntity.getTaskId().getBatchContral()){
                        paramsList.add("结束");
                        paramsList.add("stop");
                        showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    }
                    else{
                        endProduceTaskReport();
                    }

                    break;

                default:

                    break;
            }

        });

    }

    /**
     * @author zhangwenshuai1 2020/4/1
     * @param
     * @return
     * @description 工单结束报工
     *
     */
    private void endProduceTaskReport() {
       Bundle bundle = new Bundle();
       bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,mWaitPutinRecordEntity);
        IntentRouter.go(context,Constant.Router.WOM_PRODUCE_TASK_END_REPORT,bundle);
    }



    /**
     * @param
     * @return
     * @description 工单操作确认：开启/暂停/恢复/停止
     * @author zhangwenshuai1 2020/3/25
     */
    private void showOperateConfirmDialog(List<Object> paramsList, WaitPutinRecordEntity waitPutinRecordEntity, boolean isTask) {
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        if (isTask) {
            customDialog.bindView(R.id.tipContentTv, "确认 " + paramsList.get(0) + " 该工单操作？")
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        ToastUtils.show(context, context.getString(R.string.wom_dealing));
                        presenterRouter.create(ProduceTaskOperateAPI.class).operateProduceTask(waitPutinRecordEntity.getId(), String.valueOf(paramsList.get(1)),null);
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
        refresh();
    }


    @Override
    public void listWaitPutinRecordsSuccess(CommonBAPListEntity entity) {
        if(entity!=null && entity.result!=null && entity.result.size() > 0) {
            womPendingProduceTaskListView.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            mProduceTaskListAdapter.setList(entity.result);
            mProduceTaskListAdapter.notifyDataSetChanged();
        }else {
            womPendingProduceTaskListView.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void listWaitPutinRecordsFailed(String errorMsg) {
        womPendingProduceTaskListView.setVisibility(View.GONE);
        noDataTv.setVisibility(View.VISIBLE);
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateProduceTaskSuccess(BAP5CommonEntity entity) {
        ToastUtils.show(context, context.getResources().getString(R.string.wom_dealt_success));
    }

    @Override
    public void operateProduceTaskFailed(String errorMsg) {
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateDischargeSuccess(BAP5CommonEntity bap5CommonEntity) {

    }

    @Override
    public void operateDischargeFailed(String errorMsg) {

    }

    public void refresh(){
        presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(1, 2, queryParams,true);
    }

    public void show(){
        refresh();
//        womPendingProduceTaskListView.setVisibility(View.VISIBLE);
//        mProduceTaskListAdapter.notifyDataSetChanged();
    }

    public void hide(){
        womPendingProduceTaskListView.setVisibility(View.GONE);
    }
}
