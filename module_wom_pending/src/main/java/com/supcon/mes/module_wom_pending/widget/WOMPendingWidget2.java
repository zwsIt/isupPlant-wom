package com.supcon.mes.module_wom_pending.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Widget;
import com.supcon.common.view.base.view.BaseWidgetLayout;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_pending.R;
import com.supcon.mes.module_wom_pending.controller.WOMWidgetActivityListController;
import com.supcon.mes.module_wom_pending.controller.WOMWidgetProduceTaskListController;
import com.supcon.mes.module_wom_pending.ui.fragment.WOMWidgetActivityListFragment;
import com.supcon.mes.module_wom_pending.ui.fragment.WOMWidgetProduceTaskListFragment;
import com.supcon.mes.module_wom_producetask.IntentRouter;

/**
 * Created by wangshizhan on 2020/6/9
 * Email:wangshizhan@supcom.com
 */
@Widget(Constant.Widget.WOM_PENDING)
public class WOMPendingWidget2 extends BaseWidgetLayout {

    @BindByTag("widgetTitle")
    TextView widgetTitle;

    @BindByTag("widgetMore")
    ImageView widgetMore;

    @BindByTag("womPendingTab")
    CustomTab womPendingTab;

    WOMWidgetProduceTaskListController mWOMWidgetProduceTaskListController;
    WOMWidgetActivityListController mWOMWidgetActivityListController;

    int currentPage = 0;

    public WOMPendingWidget2(Context context) {
        super(context);
    }

    public WOMPendingWidget2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutId() {
        return R.layout.v_wom_widget_pending2;
    }

    @Override
    protected void initView() {
        super.initView();
        widgetTitle.setText("生产待办");
        if (context != null && context.toString().contains("com.supcon.mes.home.ui.MainActivity")) {
            widgetMore.setImageResource(R.drawable.sl_refresh);
        }
        initTabs();
        mWOMWidgetProduceTaskListController  = new WOMWidgetProduceTaskListController(rootView);
        mWOMWidgetProduceTaskListController.onInit();
        mWOMWidgetProduceTaskListController.initView();
        mWOMWidgetActivityListController =  new WOMWidgetActivityListController(rootView);
        mWOMWidgetActivityListController.onInit();
        mWOMWidgetActivityListController.initView();
    }

    private void initTabs() {
        womPendingTab.addTab("指令单");
        womPendingTab.addTab("活动");
        womPendingTab.setCurrentTab(0);
    }


    @Override
    public void bind(ViewStub view) {
        super.bind(view);
    }

    @Override
    public void doRefresh(long intervel) {
        super.doRefresh(intervel);
    }

    @Override
    protected void refresh() {
        super.refresh();
        if(currentPage == 0){
            mWOMWidgetProduceTaskListController.refresh();
        }else {
            mWOMWidgetActivityListController.refresh();
        }

    }

    @Override
    protected void initListener() {
        super.initListener();

        widgetMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goProduceTaskList();
            }
        });

        womPendingTab.setOnTabChangeListener(current -> {
            if(current == currentPage){
                return;
            }

            if(current == 0){
                mWOMWidgetActivityListController.hide();
                mWOMWidgetProduceTaskListController.show();
                currentPage = 0;
            }
            else{
                mWOMWidgetProduceTaskListController.hide();
                mWOMWidgetActivityListController.show();
                currentPage = 1;
            }

        });

       mWOMWidgetActivityListController.initListener();
       mWOMWidgetProduceTaskListController.initListener();
    }

    private void goProduceTaskList() {
        if (context != null && context.toString().contains("com.supcon.mes.home.ui.MainActivity")) {
            if(currentPage == 0){
                mWOMWidgetActivityListController.hide();
                mWOMWidgetProduceTaskListController.show();
            }
            else{
                mWOMWidgetProduceTaskListController.hide();
                mWOMWidgetActivityListController.show();
            }
//            mWOMWidgetActivityListController.refresh();
//            mWOMWidgetProduceTaskListController.refresh();
            return;
        }
        IntentRouter.go(context, Constant.AppCode.WOM_Production);
    }

}
