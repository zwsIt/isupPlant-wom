package com.supcon.mes.module_wom_producetask.ui.activity;

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
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.BatchMaterialRecordsRefPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.BatchMaterialRecordsRefListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录参照list
 */
@Router(Constant.Router.BATCH_MATERIAL_LIST_REF)
@Presenter(value = {BatchMaterialRecordsRefPresenter.class})
public class BatchMaterialRefListActivity extends BaseRefreshRecyclerActivity<BatchMaterialPartEntity> implements CommonListContract.View {
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("allChooseCheckBox")
    CheckBox allChooseCheckBox;
    @BindByTag("submitBtn")
    Button submitBtn;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private BatchMaterialRecordsRefListAdapter mBatchMaterialRecordsRefListAdapter;
    private List<BatchMaterialPartEntity> chooseList = new ArrayList<>();

    @Override
    protected IListAdapter<BatchMaterialPartEntity> createAdapter() {
        mBatchMaterialRecordsRefListAdapter = new BatchMaterialRecordsRefListAdapter(context);
        return mBatchMaterialRecordsRefListAdapter;
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
        WaitPutinRecordEntity waitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        customCondition.put("taskActiveId", waitPutinRecordEntity.getTaskActiveId().getId());

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
        searchTitleBar.setTitleText(context.getResources().getString(R.string.wom_batch_material_records_ref));
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
            presenterRouter.create(CommonListAPI.class).list(pageIndex, customCondition, queryParams, WomConstant.URL.BATCH_MATERIAL_LIST_REF_URL, "batMaterilPart");
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

        allChooseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBatchMaterialRecordsRefListAdapter.getList() != null){
                    for (BatchMaterialPartEntity entity : mBatchMaterialRecordsRefListAdapter.getList()){
                        entity.setChecked(!entity.isChecked());
                    }
                    mBatchMaterialRecordsRefListAdapter.notifyDataSetChanged();
                }
            }
        });
        RxView.clicks(submitBtn).throttleFirst(200,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (mBatchMaterialRecordsRefListAdapter.getList() == null || mBatchMaterialRecordsRefListAdapter.getList().size() <= 0){
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
                            return;
                        }
                        // do全选
                        for (BatchMaterialPartEntity entity : mBatchMaterialRecordsRefListAdapter.getList()){
                            if (entity.isChecked()){
                                chooseList.add(entity);
                            }
                        }
                        if (chooseList.size() <= 0){
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_select_material));
                            return;
                        }
                        SelectDataEvent dataEvent = new SelectDataEvent(chooseList,"");
                        EventBus.getDefault().post(dataEvent);
                        finish();

                    }
                });
        mBatchMaterialRecordsRefListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            BatchMaterialPartEntity batchMaterialPartEntity = (BatchMaterialPartEntity) obj;
            if ("checkBox".equals(childView.getTag().toString())){
                batchMaterialPartEntity.setChecked(!batchMaterialPartEntity.isChecked());
                // do全选
                int count = 0;
                for (BatchMaterialPartEntity entity : mBatchMaterialRecordsRefListAdapter.getList()) {
                    if (entity.isChecked()) {
                        count++;
                    }
                }
                if (count == mBatchMaterialRecordsRefListAdapter.getList().size()){
                    allChooseCheckBox.setChecked(true);
                }else {
                    allChooseCheckBox.setChecked(false);
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data),CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object)commonBAPListEntity.result),BatchMaterialPartEntity.class));
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
