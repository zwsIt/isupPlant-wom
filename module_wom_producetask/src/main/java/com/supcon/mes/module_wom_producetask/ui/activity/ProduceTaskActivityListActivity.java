package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.ui.fragment.FormulaActivityListFragment;
import com.supcon.mes.module_wom_producetask.ui.fragment.TemporaryActivityListFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 生产工单活动list
 */
@Router(Constant.Router.WOM_ACTIVITY_LIST)
public class ProduceTaskActivityListActivity extends BaseMultiFragmentActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private FormulaActivityListFragment mFormulaActivityListFragment;
    private TemporaryActivityListFragment mTemporaryActivityListFragment;
    WaitPutinRecordEntity waitPutinRecordEntity;

    @Override
    public int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void createFragments() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_task_activity_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        waitPutinRecordEntity= (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_activity_list));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.ic_wts_reference_white);
        initTab();
        initViewPager();
    }

    /**
     * 初始化页签视图
     */
    private void initViewPager() {
        mFormulaActivityListFragment = new FormulaActivityListFragment();
        mTemporaryActivityListFragment = new TemporaryActivityListFragment();
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
//        viewPager.setOffscreenPageLimit(2); // 预加载下一页的界面个数，默认加载下一页面
    }

    /**
     * 初始化tab页签
     */
    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.wom_formula_activity));
        customTab.addTab(context.getResources().getString(R.string.wom_temporary_activity));
        customTab.setCurrentTab(0);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }
        });
        RxView.clicks(rightBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o->{
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,waitPutinRecordEntity);
                    IntentRouter.go(context,Constant.Router.ACTIVITY_EXEREDS_LIST,bundle);
                });
        customTab.setOnTabChangeListener(current -> viewPager.setCurrentItem(current));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (customTab.getCurrentPosition() != i)
                    customTab.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    return mTemporaryActivityListFragment;
                case 0:
                default:
                    return mFormulaActivityListFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
