package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.WorkFlowButtonInfoController;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.ui.fragment.BatchMaterialEditSetFragment;
import com.supcon.mes.module_wom_batchmaterial.ui.fragment.BatchMaterialScanSetFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/19
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令集list
 */
@Router(value = Constant.AppCode.WOM_BATCH_MATERIAL_SET)
//@Controller(value = {WorkFlowButtonInfoController.class})
public class BatchMaterialInstructionSetListActivity extends BaseControllerActivity {

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private BatchMaterialEditSetFragment mBatchMaterialEditSetFragment;
    private BatchMaterialScanSetFragment mBatchMaterialScanSetFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_fragment_search_list;
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
        searchTitleBar.title().setText(R.string.batch_material_instruction_set);
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.batch_please_input_bucket_code));

        initTab();
        initViewPager();
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.batch_material_executing));
        customTab.addTab(context.getResources().getString(R.string.batch_material_scan_transport));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mBatchMaterialEditSetFragment = new BatchMaterialEditSetFragment();
        mBatchMaterialScanSetFragment = new BatchMaterialScanSetFragment();
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
                        mBatchMaterialEditSetFragment.search();
                    }else if (customTab.getCurrentPosition() == 1){
                        mBatchMaterialScanSetFragment.search();
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
                    searchTitleBar.rightBtn().setImageResource(R.drawable.ic_scan);
                }else {
                    searchTitleBar.rightBtn().setImageResource(R.drawable.ic_btn_add);
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

    public void setSearch(String code) {
        searchTitleBar.searchView().setInput(code);
    }

    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    return mBatchMaterialScanSetFragment;
                case 0:
                default:
                    return mBatchMaterialEditSetFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
