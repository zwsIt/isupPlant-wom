package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.api.ListAllActivityAPI;
import com.supcon.mes.module_wom_producetask.model.bean.TaskActiveEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ListAllActivityContract;
import com.supcon.mes.module_wom_producetask.presenter.ActivityProcessListPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.ActivityProcessInfoAdapter;


import java.util.HashMap;
import java.util.Map;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 当前工序下所有活动list
 */
@Router(Constant.Router.WOM_ACTIVITY_PROCESS_LIST)
@Presenter(value = {ActivityProcessListPresenter.class})
public class ActivityProcessListActivity extends BaseRefreshRecyclerActivity<TaskActiveEntity> implements ListAllActivityContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();

    private OnRefreshListener mOnRefreshListener;
    private RecyclerView.ItemDecoration mItemDecoration;

    @Override
    protected IListAdapter<TaskActiveEntity> createAdapter() {
        return new ActivityProcessInfoAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,context.getResources().getString(R.string.middleware_no_data)));
        WaitPutinRecordEntity waitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        queryParams.put("processId",waitPutinRecordEntity.getTaskProcessId().getId());
        queryParams.put("showBatch",false);
        customCondition.put("pageNo", 1);
        customCondition.put("pageSize", 65535);

        mItemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10,context),0,DisplayUtil.dip2px(10,context),0);
            }
        };

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(mItemDecoration);

        mOnRefreshListener = () -> presenterRouter.create(ListAllActivityAPI.class).listActivities(customCondition, queryParams);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_activity_process));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());

        refreshListController.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOnRefreshListener = null;
        mItemDecoration = null;
    }

    @Override
    public void listActivitiesSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = (CommonBAPListEntity) entity.data;
        refreshListController.refreshComplete(commonBAPListEntity.result);
    }

    @Override
    public void listActivitiesFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
