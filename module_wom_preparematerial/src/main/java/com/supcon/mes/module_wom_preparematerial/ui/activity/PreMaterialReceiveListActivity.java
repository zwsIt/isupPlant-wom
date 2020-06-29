package com.supcon.mes.module_wom_preparematerial.ui.activity;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.ViewUtil;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemCodeController;
import com.supcon.mes.middleware.controller.SystemCodeJsonController;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.SearchHistoryEntity;
import com.supcon.mes.middleware.model.inter.ISearchContent;
import com.supcon.mes.middleware.model.inter.SystemCode;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_search.controller.SearchViewController;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.model.api.PreMaterialReceiveListAPI;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_preparematerial.model.contract.PreMaterialReceiveListContract;
import com.supcon.mes.module_wom_preparematerial.presenter.PreMaterialReceiveListPresenter;
import com.supcon.mes.module_wom_preparematerial.ui.adapter.PreMaterialReceiveListAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.AppCode.WOM_AdjustMaterial)
@Presenter(PreMaterialReceiveListPresenter.class)
@Controller(value = {SearchViewController.class, SystemCodeJsonController.class})
@SystemCode(entityCodes = {WomConstant.SystemCode.WOM_receiveState, WomConstant.SystemCode.WOM_rejectReason})
public class PreMaterialReceiveListActivity extends BaseRefreshRecyclerActivity<PreMaterialEntity> implements
        PreMaterialReceiveListContract.View{

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("customSearchTitleBar")
    CustomHorizontalSearchTitleBar customSearchTitleBar;

    @BindByTag("searchView")
    CustomSearchView searchView;

    @BindByTag("contentView")
    RecyclerView contentView;


    private PreMaterialReceiveListAdapter mPreMaterialReceiveListAdapter;
    private Map<String, Object> queryParams;

    @Override
    protected IListAdapter<PreMaterialEntity> createAdapter() {
        mPreMaterialReceiveListAdapter = new PreMaterialReceiveListAdapter(context);
        return mPreMaterialReceiveListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_pre_material_receive_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        queryParams = new HashMap<>();
        queryParams.put("RECORD_STATE","WOM_prePareState/waitCollecte");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, com.supcon.mes.module_wom_producetask.R.color.themeColor);
        ViewUtil.setPaddingRight(customSearchTitleBar.searchView(), ViewUtil.dpToPx(context, 80));
        titleText.setText(R.string.wom_prepare_material_receive_list);
        getController(SearchViewController.class).setExpandValue("请输入派送单号", "搜索");
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));

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

        RxView.clicks(customSearchTitleBar.rightBtn())
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //do submit
                    }
                });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {

                presenterRouter.create(PreMaterialReceiveListAPI.class).getPreMaterialReceiveList(pageIndex, queryParams);
            }
        });

        getController(SearchViewController.class).setSearchList(new SearchViewController.SearchResultListener() {
            @Override
            public void searchResult(ISearchContent data) {
                SearchHistoryEntity historyEntity = (SearchHistoryEntity) data;
                String content = historyEntity.getContent();
                refreshListController.refreshBegin();
            }

            @Override
            public void noSearchResult(String result) {

                refreshListController.refreshBegin();
            }

            @Override
            public void isExpandSearch(boolean isExpand) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void getPreMaterialReceiveListSuccess(CommonBAPListEntity entity) {

        if(entity == null || entity.result == null){
            refreshListController.refreshComplete(null);
            return;
        }

        if(entity.pageNo == 1){
            Map<String,String> rejectReasons =  getController(SystemCodeJsonController.class).getCodeMap(WomConstant.SystemCode.WOM_rejectReason);
            List<String> rejectReasonList = new ArrayList<>();
            if(rejectReasons!=null && rejectReasons.size()!=0){
                rejectReasonList.addAll(rejectReasons.values());
                mPreMaterialReceiveListAdapter.setRejectReasons(rejectReasonList);
            }

            Map<String,String> receiveStates =  getController(SystemCodeJsonController.class).getCodeMap(WomConstant.SystemCode.WOM_receiveState);

            if(receiveStates!=null && receiveStates.size()!=0){
                mPreMaterialReceiveListAdapter.setRejectStates(receiveStates);
            }
        }

        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getPreMaterialReceiveListFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete(null);
    }
}
