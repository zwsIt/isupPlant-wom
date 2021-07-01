package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
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
import com.supcon.mes.module_wom_batchmaterial.ui.fragment.BatchMaterialNextAreaSetFragment;
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
public class BatchMaterialInstructionSetListActivity extends BaseControllerActivity {

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private BatchMaterialEditSetFragment mBatchMaterialEditSetFragment;
    private BatchMaterialScanSetFragment mBatchMaterialScanSetFragment;
    private BatchMaterialNextAreaSetFragment mBatchMaterialNextAreaSetFragment;

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
        customTab.addTab(context.getResources().getString(R.string.batch_material_transport_next_area));
        customTab.setCurrentTab(0);
        viewPager.setOffscreenPageLimit(3);
//        customTab.setTabNum(1,10);
//        LinearLayout tabLayout = customTab.findViewById(R.id.tabLayout);
//        TextView tab = (TextView) tabLayout.getChildAt(1);
//        tab.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_necessary,0);
//        tab.setPadding(0,0, DisplayUtil.dip2px(100,context),0);
    }

    private void initViewPager() {
        mBatchMaterialEditSetFragment = new BatchMaterialEditSetFragment();
        mBatchMaterialScanSetFragment = new BatchMaterialScanSetFragment();
        mBatchMaterialNextAreaSetFragment = new BatchMaterialNextAreaSetFragment();
        viewPager.setAdapter(new InnerFragmentPagerAdapter(getSupportFragmentManager()));
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
                        mBatchMaterialEditSetFragment.search(charSequence.toString());
                    }else if (customTab.getCurrentPosition() == 1){
                        mBatchMaterialScanSetFragment.search();
                    }else if (customTab.getCurrentPosition() == 2){
                        mBatchMaterialNextAreaSetFragment.search();
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
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(BmConstant.IntentKey.BATCH_AREA_AUTO,false)){
            viewPager.setCurrentItem(1);
        }else {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getSearch(){
        return searchTitleBar.editText().getText().toString();
    }

    public void setSearch(String code) {
        searchTitleBar.searchView().setInput(code);
    }

    public int getTabPos(){
        return customTab.getCurrentPosition();
    }

    /**
     * 设置扫码转运代办数量
     * @param size
     */
    public void setPendingNum(int size) {
        customTab.setTabNum(1,size);
    }

    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 2:
                    return mBatchMaterialNextAreaSetFragment;
                case 1:
                    return mBatchMaterialScanSetFragment;
                case 0:
                default:
                    return mBatchMaterialEditSetFragment;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
