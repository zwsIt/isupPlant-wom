package com.supcon.mes.module_wom_pending.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_pending.R;
import com.supcon.mes.module_wom_pending.ui.adapter.WOMWidgetActivityAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.ActivityOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.ActivityOperatePresenter;
import com.supcon.mes.module_wom_producetask.presenter.WaitPutinRecordPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.FormulaActivityListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配方活动list
 */
@Presenter(value = {WaitPutinRecordPresenter.class})
public class WOMWidgetActivityListFragment extends BaseRefreshRecyclerFragment<WaitPutinRecordEntity> implements WaitPutinRecordsListContract.View{

    @BindByTag("contentView")
    RecyclerView contentView;
    private WOMWidgetActivityAdapter mWidgetActivityAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // 活动查询
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // 当前操作项

    @Override
    protected IListAdapter<WaitPutinRecordEntity> createAdapter() {
        mWidgetActivityAdapter = new WOMWidgetActivityAdapter(context);
        return mWidgetActivityAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.frag_wom_widget_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(2, context), DisplayUtil.dip2px(10, context), 0);
            }
        });

    }

    @Override
    protected void initView() {
        super.initView();
        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_ACTIVE); // 默认活动查询
    }
    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(1, 2, queryParams);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        refreshListController.refreshBegin();
    }

    @Override
    public void listWaitPutinRecordsSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);

    }

    @Override
    public void listWaitPutinRecordsFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    public void refresh(){
        if (refreshListController !=null) {
            refreshListController.refreshBegin();
        }
    }
}
