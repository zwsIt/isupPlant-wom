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
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_rejectmaterial.R;
import com.supcon.mes.module_wom_rejectmaterial.model.bean.RejectRecordMaterialEntity;
import com.supcon.mes.module_wom_rejectmaterial.ui.adapter.RejectRecordMaterialAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author by fengjun1,
 * Date on 2020/6/4.
 */

@Presenter(value = {CommonListPresenter.class})
public class RejectRecordMaterialFragment extends BaseRefreshRecyclerFragment<RejectRecordMaterialEntity> implements CommonListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private RejectRecordMaterialAdapter rejectRecordMaterialAdapter;
    Map<String, Object> queryParams = new HashMap<>(); // 快速查询

    @Override
    protected IListAdapter<RejectRecordMaterialEntity> createAdapter() {
        rejectRecordMaterialAdapter = new RejectRecordMaterialAdapter(context);
        return rejectRecordMaterialAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_common_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
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

    public static RejectRecordMaterialFragment getInstance(String url) {
        RejectRecordMaterialFragment fragment = new RejectRecordMaterialFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.IntentKey.URL,url);
        fragment.setArguments(bundle);
        return fragment;
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
                presenterRouter.create(CommonListAPI.class).list(pageIndex,null,queryParams, getArguments() == null? "" : getArguments().getString(Constant.IntentKey.URL),"rejctMatalPart");
            }
        });
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object)commonBAPListEntity.result), RejectRecordMaterialEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
