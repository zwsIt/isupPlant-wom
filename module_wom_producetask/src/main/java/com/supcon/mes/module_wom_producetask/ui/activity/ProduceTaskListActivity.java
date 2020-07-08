package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.ui.fragment.CommonProduceTaskListFragment;
import com.supcon.mes.module_wom_producetask.ui.fragment.SimpleProduceTaskListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 生产工单list
 */
@Router(Constant.AppCode.WOM_Production)
public class ProduceTaskListActivity extends BaseMultiFragmentActivity {
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private CommonProduceTaskListFragment mCommonProduceTaskListFragment;
    private SimpleProduceTaskListFragment mSimpleProduceTaskListFragment;
    private boolean womType = false;

    @Override
    public int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void createFragments() {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_produce_task_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        womType = getIntent().getBooleanExtra(Constant.IntentKey.WOM_TYPE, false);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.disableRightBtn();
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.wom_input_produce_batch_num));

        initTab();
        initViewPager();
    }

    /**
     * 初始化页签视图
     */
    private void initViewPager() {
        mCommonProduceTaskListFragment = new CommonProduceTaskListFragment();
        mSimpleProduceTaskListFragment = new SimpleProduceTaskListFragment();
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
//        viewPager.setOffscreenPageLimit(2); // 预加载下一页的界面个数，默认加载下一页面
    }

    /**
     * 初始化tab页签
     */
    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.wom_common_produce_task));
        customTab.addTab(context.getResources().getString(R.string.wom_simple_produce_task));
        if (!womType) {
            customTab.setCurrentTab(0);
        } else {
            customTab.setCurrentTab(1);
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
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
//                    searchTitleBar.searchView().setInputTextColor(R.color.black);
            } else {
                searchTitleBar.searchView().setInputTextColor(R.color.black);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText()).skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .switchMap(new Function<CharSequence, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(CharSequence charSequence) throws Exception {
//                        return null;
//                    }
//                })
                .subscribe(charSequence -> {
//                    if (customTab.getCurrentPosition() == 0) {
                        mCommonProduceTaskListFragment.search(charSequence.toString().trim());
//                    } else if (customTab.getCurrentPosition() == 1) {
                        mSimpleProduceTaskListFragment.search(charSequence.toString().trim());
//                    }
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
                    return mSimpleProduceTaskListFragment;
                case 0:
                default:
                    return mCommonProduceTaskListFragment;
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
