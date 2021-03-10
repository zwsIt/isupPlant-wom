package com.supcon.mes.module_wom_preparematerial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.constant.PmConstant;
import com.supcon.mes.module_wom_preparematerial.model.bean.PrepareMaterialPartEntity;
import com.supcon.mes.module_wom_preparematerial.presenter.PrepareMaterialRecordsRefPresenter;
import com.supcon.mes.module_wom_preparematerial.ui.adapter.PrepareMaterialRecordsRefListAdapter;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 退料备料记录参照list
 */
@Router(Constant.Router.PREPARE_MATERIAL_REJECT_LIST_REF)
@Presenter(value = {PrepareMaterialRecordsRefPresenter.class})
public class PrepareMaterialRejectRefListActivity extends BaseRefreshRecyclerActivity<PrepareMaterialPartEntity> implements CommonListContract.View {
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("allChooseCheckBox")
    CheckBox allChooseCheckBox;
    @BindByTag("submitBtn")
    Button submitBtn;

    private Map<String, Object> queryParams = new HashMap<>();
    private Map<String, Object> customCondition = new HashMap<>();
    private PrepareMaterialRecordsRefListAdapter mPrepareMaterialRecordsRefListAdapter;
    private List<PrepareMaterialPartEntity> chooseList = new ArrayList<>();

    @Override
    protected IListAdapter<PrepareMaterialPartEntity> createAdapter() {
        mPrepareMaterialRecordsRefListAdapter = new PrepareMaterialRecordsRefListAdapter(context);
        return mPrepareMaterialRecordsRefListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_search_choose_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),0);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.setTitleText(context.getResources().getString(R.string.wom_prepare_material_records_ref));
        searchTitleBar.searchView().setHint(context.getResources().getString(R.string.wom_input_material_code));
        searchTitleBar.disableRightBtn();
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setText(context.getResources().getString(R.string.wom_confirm));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (pageIndex == 1){
                allChooseCheckBox.setChecked(false);
            }
            presenterRouter.create(CommonListAPI.class).list(pageIndex, customCondition, queryParams, PmConstant.URL.PREPARE_MATERIAL_RELECT_LIST_REF_URL, "prepMatralPart");
        });
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    queryParams.put(Constant.BAPQuery.CODE, charSequence.toString().trim());
                    refreshListController.refreshBegin();
                });

        allChooseCheckBox.setOnClickListener(v -> {
            if (mPrepareMaterialRecordsRefListAdapter.getList() != null){
                for (PrepareMaterialPartEntity entity : mPrepareMaterialRecordsRefListAdapter.getList()){
                    entity.setChecked(!entity.isChecked());
                }
                mPrepareMaterialRecordsRefListAdapter.notifyDataSetChanged();
            }
        });
        RxView.clicks(submitBtn).throttleFirst(200,TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (mPrepareMaterialRecordsRefListAdapter.getList() == null || mPrepareMaterialRecordsRefListAdapter.getList().size() <= 0){
                        ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
                        return;
                    }
                    // do全选
                    for (PrepareMaterialPartEntity entity : mPrepareMaterialRecordsRefListAdapter.getList()){
                        if (entity.isChecked()){
                            chooseList.add(entity);
                        }
                    }
                    if (chooseList.size() <= 0){
                        ToastUtils.show(context, context.getResources().getString(R.string.wom_select_material));
                        return;
                    }
                    SelectDataEvent dataEvent = new SelectDataEvent(chooseList,"prepareMaterial");
                    EventBus.getDefault().post(dataEvent);
                    finish();

                });
        mPrepareMaterialRecordsRefListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            PrepareMaterialPartEntity batchMaterialPartEntity = (PrepareMaterialPartEntity) obj;
            if ("checkBox".equals(childView.getTag().toString())){
                batchMaterialPartEntity.setChecked(!batchMaterialPartEntity.isChecked());
                // do全选
                int count = 0;
                for (PrepareMaterialPartEntity entity : mPrepareMaterialRecordsRefListAdapter.getList()) {
                    if (entity.isChecked()) {
                        count++;
                    }
                }
                if (count == mPrepareMaterialRecordsRefListAdapter.getList().size()){
                    allChooseCheckBox.setChecked(true);
                }else {
                    allChooseCheckBox.setChecked(false);
                }
            }

        });
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data),CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object)commonBAPListEntity.result),PrepareMaterialPartEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    public boolean getAllChosen(){
        return allChooseCheckBox.isChecked();
    }
}
