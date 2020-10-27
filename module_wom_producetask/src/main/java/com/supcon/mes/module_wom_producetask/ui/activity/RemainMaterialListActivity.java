package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
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
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.RemainMaterialEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.RemainMaterialRefListAdapter;
import com.supcon.mes.module_wom_producetask.ui.adapter.WarehouseListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/10/19
 * Email zhangwenshuai1@supcon.com
 * Desc 尾料参照list
 */
@Router(Constant.Router.WOM_REMAIN_MATERIAL_LIST)
@Presenter(value = {CommonListPresenter.class})
public class RemainMaterialListActivity extends BaseRefreshRecyclerActivity<RemainMaterialEntity> implements CommonListContract.View {
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private RemainMaterialRefListAdapter mRemainMaterialRefListAdapter;

    @Override
    protected IListAdapter<RemainMaterialEntity> createAdapter() {
        mRemainMaterialRefListAdapter = new RemainMaterialRefListAdapter(context);
        return mRemainMaterialRefListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_search_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,context.getResources().getString(R.string.middleware_no_data)));
//        WaitPutinRecordEntity waitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
//        customCondition.put("processId", waitPutinRecordEntity.getTaskProcessId().getFormulaProcessId() == null ? -1 : waitPutinRecordEntity.getTaskProcessId().getFormulaProcessId().getId());
        if (getIntent().getLongExtra(Constant.IntentKey.ID,-1) != -1){
            customCondition.put("materialId", getIntent().getLongExtra(Constant.IntentKey.ID,-1));
        }

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
        searchTitleBar.setTitleText(context.getResources().getString(R.string.wom_remain_material_select));
        searchTitleBar.searchView().setHint(context.getResources().getString(R.string.wom_material_batch_num));
        searchTitleBar.disableRightBtn();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(CommonListAPI.class)
                .list(pageIndex, customCondition, queryParams, WomConstant.URL.REMAIN_MATERIAL_LIST_REF_URL,"remainMaterial"));
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    queryParams.put(Constant.BAPQuery.BATCH_TEXT, charSequence.toString().trim());
                    refreshListController.refreshBegin();
                });

        mRemainMaterialRefListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SelectDataEvent<RemainMaterialEntity> dataEvent = new SelectDataEvent<>((RemainMaterialEntity) obj,"");
            EventBus.getDefault().post(dataEvent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        List<RemainMaterialEntity>  remainMaterialEntityList = GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),RemainMaterialEntity.class);
        // 方式二
//        Gson gson = new Gson();
//        List<WarehouseEntity>  warehouseEntityList = gson.fromJson(gson.toJson(commonBAPListEntity.result),new TypeToken<List<WarehouseEntity>>(){}.getType());
        refreshListController.refreshComplete(remainMaterialEntityList);
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
