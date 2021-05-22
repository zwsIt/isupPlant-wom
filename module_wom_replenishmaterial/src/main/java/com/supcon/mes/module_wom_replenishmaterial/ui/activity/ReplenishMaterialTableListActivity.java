package com.supcon.mes.module_wom_replenishmaterial.ui.activity;

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
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.WorkFlowButtonInfoController;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.module_wom_replenishmaterial.IntentRouter;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.ui.fragment.ReplenishMaterialTableEditFragment;
import com.supcon.mes.module_wom_replenishmaterial.ui.fragment.ReplenishMaterialTableScanFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/15
 * Email zhangwenshuai1@supcon.com
 * Desc 补料单编辑list
 */
@Router(Constant.AppCode.WOM_ReplenishMaterial_Table)
@Controller(value = {WorkFlowButtonInfoController.class})
public class ReplenishMaterialTableListActivity extends BaseControllerActivity {

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private ReplenishMaterialTableEditFragment mReplenishMaterialTableEditFragment;
    private ReplenishMaterialTableScanFragment mReplenishMaterialTableScanFragment;

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
        searchTitleBar.title().setText(R.string.replenish_material_table);
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.please_input_eam_name));

        getController(WorkFlowButtonInfoController.class).checkWorkFlowButtonStatus("WOM_1.0.0_fillMaterial_fmBillList", "WOM_1.0.0_fillMaterial", isHas -> {
            if (!isHas){
                searchTitleBar.disableRightBtn();
            }
        });

        initTab();
        initViewPager();
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.replenish_material_edit));
        customTab.addTab(context.getResources().getString(R.string.replenish_material_scan));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mReplenishMaterialTableEditFragment = new ReplenishMaterialTableEditFragment();
        mReplenishMaterialTableScanFragment = new ReplenishMaterialTableScanFragment();
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
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (viewPager.getCurrentItem() == 0){
                            getController(WorkFlowButtonInfoController.class).getWorkFlowEntity(new WorkFlowButtonInfoController.WorkFlowButtonEntityListener() {
                                @Override
                                public void onChooseWorkFlowEntity(WorkFlowButtonInfo info) {
                                    ReplenishMaterialTableEntity replenishMaterialTableEntity = new ReplenishMaterialTableEntity();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE,replenishMaterialTableEntity);
                                    bundle.putSerializable(ReplenishConstant.IntentKey.WORK_FLOW_BTN_INFO,info);
                                    IntentRouter.go(context, ReplenishConstant.Router.REPLENISH_MATERIAL_EDIT,bundle);
                                }
                            });
                        }else if (viewPager.getCurrentItem() == 1){
                            mReplenishMaterialTableScanFragment.scan();
                        }
                    }
                });
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
                        mReplenishMaterialTableEditFragment.search();
                    }else if (customTab.getCurrentPosition() == 1){
                        mReplenishMaterialTableScanFragment.search();
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
                    return mReplenishMaterialTableScanFragment;
                case 0:
                default:
                    return mReplenishMaterialTableEditFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
