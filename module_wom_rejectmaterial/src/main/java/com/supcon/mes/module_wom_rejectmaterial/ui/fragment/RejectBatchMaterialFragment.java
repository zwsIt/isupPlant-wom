package com.supcon.mes.module_wom_rejectmaterial.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectMaterialEntity;
import com.supcon.mes.module_wom_rejectmaterial.ui.activity.RejectMaterialListActivity;
import com.supcon.mes.module_wom_rejectmaterial.ui.adapter.RejectBatchMaterialListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/21
 * Email zhangwenshuai1@supcon.com
 * Desc 配料退料
 */
@Presenter(value = {CommonListPresenter.class})
public class RejectBatchMaterialFragment extends BaseRefreshRecyclerFragment<RejectMaterialEntity> implements CommonListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private RejectBatchMaterialListAdapter mRejectBatchMaterialListAdapter;
    Map<String, Object> queryParams = new HashMap<>(); // 快速查询

    @Override
    protected IListAdapter<RejectMaterialEntity> createAdapter() {
        mRejectBatchMaterialListAdapter = new RejectBatchMaterialListAdapter(context);
        return mRejectBatchMaterialListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_common_list;
    }

    /**
     * @author zhangwenshuai1 2020/4/21
     * @param
     * @return
     * @description 封装待办查询加载url
     *
     */
    public static RejectBatchMaterialFragment getInstance(String url) {
        RejectBatchMaterialFragment fragment = new RejectBatchMaterialFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.IntentKey.URL,url);
        fragment.setArguments(bundle);
        return fragment;
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
    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                queryParams.put(Constant.BAPQuery.TABLE_NO, ((RejectMaterialListActivity)context).getSearch());
                presenterRouter.create(CommonListAPI.class).list(pageIndex,null,queryParams, getArguments() == null? "" : getArguments().getString(Constant.IntentKey.URL),"rejectMaterial");
            }
        });
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data),CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object)commonBAPListEntity.result), RejectMaterialEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    public void search(String searchContent){
        refreshListController.refreshBegin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
