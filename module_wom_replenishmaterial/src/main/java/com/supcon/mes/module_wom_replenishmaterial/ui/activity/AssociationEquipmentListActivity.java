package com.supcon.mes.module_wom_replenishmaterial.ui.activity;

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
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.AssociatedEquipmentEntity;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.AssociationEquipmentListAdapter;

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
 * Desc 关联设备参照list
 * @author zhangwenshuai1
 */
@Router(ReplenishConstant.Router.ASSOCIATION_EQUIPMENT_LIST_REF)
@Presenter(value = {CommonListPresenter.class})
public class AssociationEquipmentListActivity extends BaseRefreshRecyclerActivity<AssociatedEquipmentEntity> implements CommonListContract.View {
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private AssociationEquipmentListAdapter mAssociationEquipmentListAdapter;

    @Override
    protected IListAdapter<AssociatedEquipmentEntity> createAdapter() {
        mAssociationEquipmentListAdapter = new AssociationEquipmentListAdapter(context);
        return mAssociationEquipmentListAdapter;
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
        customCondition.put("runModel", ReplenishConstant.SystemCode.MODEL_MANUAL);

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
        searchTitleBar.setTitleText(context.getResources().getString(R.string.replenish_eam_ref));
        searchTitleBar.searchView().setHint(context.getResources().getString(R.string.please_input_eam_name));
        searchTitleBar.disableRightBtn();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        refreshListController.setOnRefreshPageListener(pageIndex -> presenterRouter.create(CommonListAPI.class)
                .list(pageIndex, customCondition, queryParams, ReplenishConstant.URL.ASSOCIATION_EQUIPMENT_LIST_REF_URL,"ftyEquipment"));
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    queryParams.put(Constant.BAPQuery.NAME, charSequence.toString().trim());
                    refreshListController.refreshBegin();
                });

        mAssociationEquipmentListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SelectDataEvent<AssociatedEquipmentEntity> dataEvent = new SelectDataEvent<>((AssociatedEquipmentEntity) obj,"");
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
        List<AssociatedEquipmentEntity>  storeSetEntityList = GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),AssociatedEquipmentEntity.class);
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
