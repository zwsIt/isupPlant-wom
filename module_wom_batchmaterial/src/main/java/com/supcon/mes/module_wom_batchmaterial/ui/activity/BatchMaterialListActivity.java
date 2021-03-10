package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.ui.fragment.BatchMaterialInstructionFragment;
import com.supcon.mes.module_wom_batchmaterial.ui.fragment.BatchMaterialRecordsFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/15
 * Email zhangwenshuai1@supcon.com
 * Desc 配料管理list
 */
@Router(Constant.AppCode.WOM_DeployMaterial)
public class BatchMaterialListActivity extends BaseFragmentActivity {

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private BatchMaterialInstructionFragment mBatchMaterialInstructionFragment;
    private BatchMaterialRecordsFragment mBatchMaterialRecordsFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_batch_material_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.disableRightBtn();
        searchTitleBar.rightBtn().setImageResource(R.drawable.ic_scan);
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.wom_input_produce_batch_num));

        initTab();
        initViewPager();
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.wom_batch_material_instruction));
        customTab.addTab(context.getResources().getString(R.string.wom_batch_material_records));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mBatchMaterialInstructionFragment = new BatchMaterialInstructionFragment();
        mBatchMaterialRecordsFragment = new BatchMaterialRecordsFragment();
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
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
        RxView.clicks(searchTitleBar.rightBtn()).throttleFirst(500,TimeUnit.MILLISECONDS)
                .subscribe(o -> mBatchMaterialRecordsFragment.scan());
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
                .subscribe(charSequence -> {
                    if (customTab.getCurrentPosition() == 0){
                        mBatchMaterialInstructionFragment.search(charSequence.toString().trim());
                    }else if (customTab.getCurrentPosition() == 1){
                        mBatchMaterialRecordsFragment.search(charSequence.toString().trim());
                    }
                });
        customTab.setOnTabChangeListener(current -> viewPager.setCurrentItem(current));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (customTab.getCurrentPosition() != i){
                    customTab.setCurrentTab(i);
                }

                if (i == 1){
                    searchTitleBar.enableRightBtn();
                }else {
                    searchTitleBar.disableRightBtn();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public String getSearch(){
        return searchTitleBar.editText().getText().toString();
    }

    public BatchMaterialInstructionFragment getBatchMaterialInstructionFragment() {
        return mBatchMaterialInstructionFragment;
    }

    public BatchMaterialRecordsFragment getBatchMaterialRecordsFragment() {
        return mBatchMaterialRecordsFragment;
    }

    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    return mBatchMaterialRecordsFragment;
                case 0:
                default:
                    return mBatchMaterialInstructionFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
