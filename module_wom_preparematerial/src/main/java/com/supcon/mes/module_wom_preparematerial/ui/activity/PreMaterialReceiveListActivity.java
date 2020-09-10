package com.supcon.mes.module_wom_preparematerial.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.utils.ViewUtil;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomSearchView;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemCodeController;
import com.supcon.mes.middleware.controller.SystemCodeJsonController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.SearchHistoryEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.ISearchContent;
import com.supcon.mes.middleware.model.inter.SystemCode;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_search.controller.SearchViewController;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.model.api.PreMaterialReceiveListAPI;
import com.supcon.mes.module_wom_preparematerial.model.api.PreMaterialReceiveSubmitAPI;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialSubmitEntity;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreResultEntity;
import com.supcon.mes.module_wom_preparematerial.model.contract.PreMaterialReceiveListContract;
import com.supcon.mes.module_wom_preparematerial.model.contract.PreMaterialReceiveSubmitContract;
import com.supcon.mes.module_wom_preparematerial.presenter.PreMaterialReceiveListPresenter;
import com.supcon.mes.module_wom_preparematerial.presenter.PreMaterialReceiveSubmitPresenter;
import com.supcon.mes.module_wom_preparematerial.ui.adapter.PreMaterialReceiveListAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.AppCode.WOM_AdjustMaterial)
@Presenter(value = {PreMaterialReceiveListPresenter.class, PreMaterialReceiveSubmitPresenter.class})
@Controller(value = {SearchViewController.class, SystemCodeJsonController.class})
@SystemCode(entityCodes = {WomConstant.SystemCode.WOM_receiveState, WomConstant.SystemCode.WOM_rejectReason})
public class PreMaterialReceiveListActivity extends BaseRefreshRecyclerActivity<PreMaterialEntity> implements
        PreMaterialReceiveListContract.View, PreMaterialReceiveSubmitContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("customSearchTitleBar")
    CustomHorizontalSearchTitleBar customSearchTitleBar;

    @BindByTag("customSearchView")
    CustomSearchView customSearchView;

    @BindByTag("contentView")
    RecyclerView contentView;


    private PreMaterialReceiveListAdapter mPreMaterialReceiveListAdapter;
    private Map<String, Object> queryParams;
    private int actionPosition = -1;
    private PreMaterialEntity mPreMaterialEntity;

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
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,""));
        queryParams = new HashMap<>();
        queryParams.put("RECORD_STATE","WOM_prePareState/waitCollecte"); // 默认记录状态：待收
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
//        ViewUtil.setPaddingRight(customSearchTitleBar.searchView(), ViewUtil.dpToPx(context, 80));
        titleText.setText(R.string.wom_prepare_material_receive_list);
        getController(SearchViewController.class).setExpandValue(context.getResources().getString(R.string.wom_input_delivery_no), context.getResources().getString(R.string.search));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(8, context)));

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //do submit
                        doSubmit(mPreMaterialReceiveListAdapter.getList());
                    }
                });

        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(PreMaterialReceiveListAPI.class).getPreMaterialReceiveList(pageIndex, queryParams));

        getController(SearchViewController.class).setSearchList(new SearchViewController.SearchResultListener() {
            @Override
            public void searchResult(ISearchContent data) {
                SearchHistoryEntity historyEntity = (SearchHistoryEntity) data;
                String content = historyEntity.getContent();
                queryParams.put(Constant.BAPQuery.DELIVER_CODE, content);
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

        RxTextView.textChanges(customSearchView.editText())
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if(TextUtils.isEmpty(charSequence)){
                            queryParams.remove(Constant.BAPQuery.DELIVER_CODE);
                        }
                        else{
                            queryParams.put(Constant.BAPQuery.DELIVER_CODE, charSequence.toString());
                        }

                        refreshListController.refreshBegin();
                    }
                });

        mPreMaterialReceiveListAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object object) {
                Bundle bundle = new Bundle();
                String tag = (String) childView.getTag();
                mPreMaterialEntity = (PreMaterialEntity)object;
                actionPosition = position;
                switch (tag){
                    case "itemPreMaterialReceiveStaff":

                        bundle.putBoolean(Constant.IntentKey.IS_MULTI, false);
                        bundle.putBoolean(Constant.IntentKey.IS_SELECT, true);
                        bundle.putString(Constant.IntentKey.SELECT_TAG, "itemPreMaterialReceiveStaff");
                        IntentRouter.go(context, Constant.Router.CONTACT_SELECT, bundle);
                        break;
                    case "itemPreMaterialReceiveStoreLocation":
                        if (action == 0){
                            mPreMaterialEntity.toStoreId=null;
                        }
                        bundle.putLong(Constant.IntentKey.WARE_ID, mPreMaterialEntity.toWareId.getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                        break;
                        default:

                }
            }
        });
    }

    private void doSubmit(List<PreMaterialEntity> list) {

        List<PreMaterialSubmitEntity> preMaterialEntities = new ArrayList<>();
        for(PreMaterialEntity preMaterialEntity : list){
            if(preMaterialEntity.isChecked){
                if(preMaterialEntity.toStoreId==null || preMaterialEntity.toStoreId.getId()==null){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_store_forbidden_null));
                    return;
                }
                if(preMaterialEntity.receiveStaff==null || preMaterialEntity.receiveStaff.name==null){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_receiver_forbidden_null));
                    return;
                }

                if(preMaterialEntity.receiveState==null){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_receivetype_forbidden_null));
                    return;
                }

                if(preMaterialEntity.receiveNum==null){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_receivenum_forbidden_null));
                    return;
                }


                if("WOM_receiveState/reject".equals(preMaterialEntity.receiveState.id) && preMaterialEntity.rejectReason==null){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_receivereason_forbidden_null));
                    return;
                }

                if("WOM_receiveState/partReceive".equals(preMaterialEntity.receiveState.id) && TextUtils.isEmpty(preMaterialEntity.remark)){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_part_receivereason_forbidden_null));
                    return;
                }

                preMaterialEntity.receiveDate = System.currentTimeMillis();

                PreMaterialSubmitEntity submitEntity = GsonUtil.gsonToBean(preMaterialEntity.toString(), PreMaterialSubmitEntity.class);
                preMaterialEntities.add(submitEntity);
            }

        }

        if(preMaterialEntities.size() == 0){
            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_one));
            return;
        }
        onLoading(context.getResources().getString(R.string.wom_dealing));
        presenterRouter.create(PreMaterialReceiveSubmitAPI.class).doSubmitPreMaterial(preMaterialEntities);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSelect(SelectDataEvent event) {

        if (event.getSelectTag().equals("itemPreMaterialReceiveStaff")) {
//            LogUtil.d("人员单选："+ GsonUtil.gsonString(event.getEntity()));
            ContactEntity selectContactEntity = (ContactEntity) event.getEntity();
            ObjectEntity objectEntity = new ObjectEntity(selectContactEntity.staffId);
            objectEntity.name = selectContactEntity.name;

            if(actionPosition == -1){
                return;
            }
            PreMaterialEntity materialEntity = mPreMaterialReceiveListAdapter.getItem(actionPosition);

            if (materialEntity == null) {
                return;
            }
            materialEntity.receiveStaff = objectEntity;
            mPreMaterialReceiveListAdapter.notifyItemChanged(actionPosition);
        }else if (event.getEntity() instanceof StoreSetEntity){
            mPreMaterialEntity.toStoreId = ((StoreSetEntity) event.getEntity());
            mPreMaterialReceiveListAdapter.notifyItemChanged(actionPosition);
        }
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
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
        refreshListController.refreshComplete();
    }


    @Override
    public void doSubmitPreMaterialSuccess(PreResultEntity entity) {
        if(entity.dealSuccessFlag){
            onLoadSuccess();
            refreshListController.refreshBegin();
        }
        else{
            onLoadFailed(""+entity.errMsg);
        }
    }

    @Override
    public void doSubmitPreMaterialFailed(String errorMsg) {
        onLoadFailed(""+errorMsg);
    }
}
