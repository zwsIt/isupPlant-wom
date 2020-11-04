package com.supcon.mes.module_wom_pending.controller;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_pending.R;
import com.supcon.mes.module_wom_pending.ui.adapter.WOMWidgetActivityAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.WaitPutinRecordPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配方活动list
 */
@Presenter(value = {WaitPutinRecordPresenter.class})
public class WOMWidgetActivityListController extends BaseViewController implements WaitPutinRecordsListContract.View{

    @BindByTag("womPendingTab")
    CustomTab womPendingTab;
    @BindByTag("womPendingActivityListView")
    RecyclerView womPendingActivityListView;
    @BindByTag("noDataTv")
    TextView noDataTv;
    private WOMWidgetActivityAdapter mWidgetActivityAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // 活动查询
    private boolean isFace;

    public WOMWidgetActivityListController(View rootView) {
        super(rootView);
    }


    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_ACTIVE); // 默认活动查询
    }

    @Override
    public void initView() {
        super.initView();

        womPendingActivityListView.setLayoutManager(new LinearLayoutManager(context));
        womPendingActivityListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(2, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(1,context));
            }
        });

        mWidgetActivityAdapter = new WOMWidgetActivityAdapter(context);
        womPendingActivityListView.setAdapter(mWidgetActivityAdapter);
    }
    @Override
    public void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    public void initListener() {
        super.initListener();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        if (womPendingTab.getCurrentPosition() == 1)refresh();
    }

    @Override
    public void listWaitPutinRecordsSuccess(CommonBAPListEntity entity) {
        if(entity!=null && entity.result!=null && entity.result.size() > 0) {
            womPendingActivityListView.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            mWidgetActivityAdapter.setList(entity.result);
            mWidgetActivityAdapter.notifyDataSetChanged();
        }else {
            womPendingActivityListView.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void listWaitPutinRecordsFailed(String errorMsg) {
        womPendingActivityListView.setVisibility(View.GONE);
        noDataTv.setVisibility(View.VISIBLE);
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
    }

    public void refresh(){
        presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(1, 2, queryParams,false);
    }

    public void show(){

        refresh();
//        womPendingActivityListView.setVisibility(View.VISIBLE);
//        mWidgetActivityAdapter.notifyDataSetChanged();
    }

    public void hide(){
        womPendingActivityListView.setVisibility(View.GONE);
    }
}
