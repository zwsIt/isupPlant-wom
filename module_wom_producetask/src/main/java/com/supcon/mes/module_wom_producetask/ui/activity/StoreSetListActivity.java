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
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.StoreSetListAdapter;

import org.greenrobot.eventbus.EventBus;

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
 * Desc 货位参照list
 */
@Router(Constant.Router.STORE_SET_LIST_REF)
@Presenter(value = {CommonListPresenter.class})
public class StoreSetListActivity extends BaseRefreshRecyclerActivity<StoreSetEntity> implements CommonListContract.View {
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private StoreSetListAdapter mStoreSetListAdapter;

    @Override
    protected IListAdapter<StoreSetEntity> createAdapter() {
        mStoreSetListAdapter = new StoreSetListAdapter(context);
        return mStoreSetListAdapter;
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
        Long wareId =  getIntent().getLongExtra(Constant.IntentKey.WARE_ID,-1L);
        customCondition.put("warehouseId", wareId);

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
        searchTitleBar.setTitleText("货位选择");
        searchTitleBar.searchView().setHint("请输入货位名称");
        searchTitleBar.disableRightBtn();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(CommonListAPI.class)
                .list(pageIndex, customCondition, queryParams, WomConstant.URL.STORE_SET_LIST_REF_URL,"storeSet"));
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    queryParams.put(Constant.BAPQuery.NAME, charSequence.toString().trim());
                    refreshListController.refreshBegin();
                });

        mStoreSetListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SelectDataEvent<StoreSetEntity> dataEvent = new SelectDataEvent<>((StoreSetEntity) obj,"");
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
        List<StoreSetEntity>  storeSetEntityList = GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),StoreSetEntity.class);
        // 方式二
//        Gson gson = new Gson();
//        List<WarehouseEntity>  warehouseEntityList = gson.fromJson(gson.toJson(commonBAPListEntity.result),new TypeToken<List<WarehouseEntity>>(){}.getType());
        refreshListController.refreshComplete(storeSetEntityList);
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
