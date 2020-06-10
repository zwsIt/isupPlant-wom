package com.supcon.mes.module_wom_rejectmaterial.ui.activity;

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
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTab;
import com.supcon.mes.mbap.view.NoScrollViewPager;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_rejectmaterial.IntentRouter;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.constant.RmConstant;
import com.supcon.mes.module_wom_rejectmaterial.ui.fragment.RejectBatchMaterialFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 退料管理list
 */
@Router(Constant.AppCode.WOM_ReturnMaterial)
public class RejectMaterialListActivity extends BaseFragmentActivity {

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("btnTitleNotes")
    CustomImageButton btnTitleNotes;
    @BindByTag("customTab")
    CustomTab customTab;
    @BindByTag("viewPager")
    NoScrollViewPager viewPager;
    private RejectBatchMaterialFragment mRejectBatchMaterialFragment;
    private RejectBatchMaterialFragment mRejectPrepareMaterialFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_reject_material_list;
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
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.wom_table_no));

        initTab();
        initViewPager();
    }

    private void initTab() {
        customTab.addTab(context.getResources().getString(R.string.wom_batch_reject_material));
        customTab.addTab(context.getResources().getString(R.string.wom_prepare_reject_material));
        customTab.setCurrentTab(0);
    }

    private void initViewPager() {
        mRejectBatchMaterialFragment = RejectBatchMaterialFragment.getInstance(RmConstant.URL.REJECT_BATCH_MATERIAL_PENDING_LIST_URL);  // 配料退料
        mRejectPrepareMaterialFragment = RejectBatchMaterialFragment.getInstance(RmConstant.URL.REJECT_PREPARE_MATERIAL_PENDING_LIST_URL); // 备料退料
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
        RxView.clicks(btnTitleNotes)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        IntentRouter.go(context,Constant.Router.REJECT_RECORD_METERIAL);
                    }
                });
        RxTextView.textChanges(searchTitleBar.editText()).skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    if (customTab.getCurrentPosition() == 0){
                        mRejectBatchMaterialFragment.search(charSequence.toString().trim());
                    }else if (customTab.getCurrentPosition() == 1){
                        mRejectPrepareMaterialFragment.search(charSequence.toString().trim());
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

    public String getSearch(){
        return searchTitleBar.editText().getText().toString();
    }

//    public BatchMaterialInstructionFragment getBatchMaterialInstructionFragment() {
//        return mBatchMaterialInstructionFragment;
//    }

//    public BatchMaterialRecordsFragment getBatchMaterialRecordsFragment() {
//        return mBatchMaterialRecordsFragment;
//    }

    private class InnerFragmentPagerAdapter extends FragmentPagerAdapter {

        public InnerFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 1:
                    return mRejectPrepareMaterialFragment;
                case 0:
                default:
                    return mRejectBatchMaterialFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
