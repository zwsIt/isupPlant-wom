package com.supcon.mes.module_wom_rejectmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.constant.RmConstant;
import com.supcon.mes.module_wom_rejectmaterial.ui.fragment.RejectRecordMaterialFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Author by fengjun1,
 * Date on 2020/6/3.
 * 退料记录list
 */
@Router(Constant.Router.REJECT_RECORD_METERIAL)
public class RejectRecordMaterialListActivity extends BaseFragmentActivity {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("customTab")
    CustomTab customTab;

    @BindByTag("viewPager")
    NoScrollViewPager viewPager;

    private RejectRecordMaterialFragment mRejectBatchRecordMaterialFragment,mRejectPrepareRecordMaterialFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_reject_record_material;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getString(R.string.wom_reject_management));

        initTab();
        initViewPager();
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.wom_prepare_reject_records));
        customTab.addTab(context.getResources().getString(R.string.wom_batch_reject_records));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mRejectBatchRecordMaterialFragment = RejectRecordMaterialFragment.getInstance(RmConstant.URL.REJECT_PREPARE_RECORD_MATERIAL_LIST_URL);  // 配料退料记录
        mRejectPrepareRecordMaterialFragment = RejectRecordMaterialFragment.getInstance(RmConstant.URL.REJECT_BATCH_RECORD_MATERIAL_LIST_URL); // 备料退料记录
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        back();
                    }
                });
        customTab.setOnTabChangeListener(new CustomTab.OnTabChangeListener() {
            @Override
            public void onTabChanged(int current) {
                viewPager.setCurrentItem(current);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (customTab.getCurrentPosition() != i){
                    customTab.setCurrentTab(i);
                }
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
                    return mRejectPrepareRecordMaterialFragment;
                case 0:
                default:
                    return mRejectBatchRecordMaterialFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
