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
import com.supcon.mes.module_wom_pending.ui.fragment.WOMWidgetActivityListFragment;
import com.supcon.mes.module_wom_pending.ui.fragment.WOMWidgetProduceTaskListFragment;
import com.supcon.mes.module_wom_producetask.IntentRouter;

/**
 * Created by wangshizhan on 2020/6/9
 * Email:wangshizhan@supcom.com
 */
@Widget(Constant.Widget.WOM_PENDING)
public class WOMPendingWidget extends BaseWidgetLayout {

    @BindByTag("widgetTitle")
    TextView widgetTitle;

    @BindByTag("widgetMore")
    ImageView widgetMore;

    @BindByTag("womPendingTab")
    CustomTab womPendingTab;

    @BindByTag("womPendingVP")
    NoScrollViewPager womPendingVP;

    WOMWidgetProduceTaskListFragment mWOMWidgetProduceTaskListFragment;
    WOMWidgetActivityListFragment mWOMWidgetActivityListFragment;

    public WOMPendingWidget(Context context) {
        super(context);
    }

    public WOMPendingWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutId() {
        return R.layout.v_wom_widget_pending;
    }

    @Override
    protected void initView() {
        super.initView();
        widgetTitle.setText("生产待办");
        initTabs();
        initViewPager();
    }

    private void initTabs() {
        womPendingTab.addTab("指令单");
        womPendingTab.addTab("活动");
    }

    private void initViewPager() {

        mWOMWidgetProduceTaskListFragment = new WOMWidgetProduceTaskListFragment();
        mWOMWidgetActivityListFragment = new WOMWidgetActivityListFragment();

        LogUtil.d("contextv:"+context);

        womPendingVP.setAdapter(new MyFragmentAdapter(((FragmentActivity)context).getSupportFragmentManager()));

        womPendingVP.setOffscreenPageLimit(2);
        womPendingTab.setCurrentTab(0);
        womPendingVP.setCurrentItem(0);
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
            womPendingVP.setCurrentItem(current);

        });

        womPendingVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (womPendingTab.getCurrentPosition() != position) {
                    womPendingTab.setCurrentTab(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void goProduceTaskList() {
        IntentRouter.go(context, Constant.AppCode.WOM_Production);
    }

    private class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return mWOMWidgetProduceTaskListFragment;
                case 1:
                    return mWOMWidgetActivityListFragment;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
