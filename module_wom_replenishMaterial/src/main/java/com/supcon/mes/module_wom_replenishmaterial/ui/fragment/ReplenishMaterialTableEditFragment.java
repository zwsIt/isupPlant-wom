package com.supcon.mes.module_wom_replenishmaterial.ui.fragment;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.WorkFlowButtonInfoController;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableListAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableListContract;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableListPresenter;
import com.supcon.mes.module_wom_replenishmaterial.ui.activity.ReplenishMaterialTableListActivity;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.ReplenishMaterialTableEditListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/12
 * Email zhangwenshuai1@supcon.com
 * Desc 补料单编辑列表
 */
@Presenter(value = {ReplenishMaterialTableListPresenter.class})
//@Controller(value = {WorkFlowButtonInfoController.class})
public class ReplenishMaterialTableEditFragment extends BaseRefreshRecyclerFragment<ReplenishMaterialTableEntity> implements ReplenishMaterialTableListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    ArrayMap<String, Object> queryParams = new ArrayMap<>(); // 快速查询

    @Override
    protected IListAdapter<ReplenishMaterialTableEntity> createAdapter() {
        return new ReplenishMaterialTableEditListAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_common_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,getString(R.string.wom_no_data_operate)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(12,context),DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(12,context),0);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
//        getController(WorkFlowButtonInfoController.class).checkWorkFlowButtonStatus("WOM_1.0.0_fillMaterial_fmBillList", "WOM_1.0.0_fillMaterial", new WorkFlowButtonInfoController.WorkFlowButtonShowListener() {
//            @Override
//            public void checkAddFlowButtonResult(boolean isHas) {
//                ((ReplenishMaterialTableListActivity)context).disableAdd(isHas);
//            }
//        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParams.put(Constant.BAPQuery.NAME, ((ReplenishMaterialTableListActivity)context).getSearch());
            presenterRouter.create(ReplenishMaterialTableListAPI.class).listReplenishMaterialTables(pageIndex, ReplenishConstant.URL.REPLENISH_MATERIAL_PENDING_LIST_URL,true,queryParams);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    public void search(){
        refreshListController.refreshBegin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listReplenishMaterialTablesSuccess(CommonBAP5ListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void listReplenishMaterialTablesFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }
}
