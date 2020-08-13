package com.supcon.mes.module_wom_producetask.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;
import com.supcon.mes.module_wom_producetask.ui.adapter.PutInReportDetailAdapter;




import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/10
 * Email:wanghaidong1@supcon.com
 */
@Router(value=Constant.Router.WOM_PUT_IN_HISTORY)
public class PutInHistoryReportActivity extends BasePresenterActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;
    private PutInReportDetailAdapter mPutInReportDetailAdapter;
    private List<PutInDetailEntity> list;

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_put_in_history;
    }

    @Override
    protected void onInit() {
        super.onInit();
        Intent intent=getIntent();
        list= (List<PutInDetailEntity>) intent.getSerializableExtra("list");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText("已投料");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        mPutInReportDetailAdapter = new PutInReportDetailAdapter(context);
        mPutInReportDetailAdapter.edit=false;
        mPutInReportDetailAdapter.setList(list);
        contentView.setAdapter(mPutInReportDetailAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o->{
                    back();
                });
    }
}
