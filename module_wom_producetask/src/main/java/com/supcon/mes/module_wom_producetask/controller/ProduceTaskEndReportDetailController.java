package com.supcon.mes.module_wom_producetask.controller;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.ui.activity.ProduceTaskEndReportActivity;
import com.supcon.mes.module_wom_producetask.ui.adapter.ProduceTaskEndReportDetailAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/1
 * Email zhangwenshuai1@supcon.com
 * Desc 工单结束报工明细Controller
 */
public class ProduceTaskEndReportDetailController extends BaseViewController {

    private ProduceTaskEndReportDetailAdapter mProduceTaskEndReportDetailAdapter;
    private OutputDetailEntity mOutputDetailEntity;
    private int mCurrentPosition;

    public ProduceTaskEndReportDetailController(View rootView) {
        super(rootView);
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void initListener() {
        super.initListener();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
