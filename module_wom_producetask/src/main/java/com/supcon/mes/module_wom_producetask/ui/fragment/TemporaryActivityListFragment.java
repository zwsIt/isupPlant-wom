package com.supcon.mes.module_wom_producetask.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.ActivityOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.ProduceTaskOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.ActivityOperatePresenter;
import com.supcon.mes.module_wom_producetask.presenter.ProduceTaskOperatePresenter;
import com.supcon.mes.module_wom_producetask.presenter.WaitPutinRecordPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.ProduceTaskListAdapter;
import com.supcon.mes.module_wom_producetask.ui.adapter.TemporaryActivityListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 机动活动list
 */
@Presenter(value = {WaitPutinRecordPresenter.class, ActivityOperatePresenter.class})
public class TemporaryActivityListFragment extends BaseRefreshRecyclerFragment<WaitPutinRecordEntity> implements WaitPutinRecordsListContract.View, ActivityOperateContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    private TemporaryActivityListAdapter mTemporaryActivityListAdapter;
    Map<String, Object> queryParams = new HashMap<>();       // 活动查询
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // 当前操作项

    @Override
    protected IListAdapter<WaitPutinRecordEntity> createAdapter() {
        mTemporaryActivityListAdapter = new TemporaryActivityListAdapter(context);
        return mTemporaryActivityListAdapter;
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
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), 0);
            }
        });

    }

    @Override
    protected void initView() {
        super.initView();
        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_ACTIVE); // 默认活动查询
//        queryParams.put(Constant.BAPQuery.IS_MORE_OTHER, true); // 其他活动
        queryParams.put(Constant.BAPQuery.IS_FOR_TEMP, true); // 机动活动

        WaitPutinRecordEntity waitPutinRecordEntity = (WaitPutinRecordEntity)getActivity().getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        mTemporaryActivityListAdapter.setWarehouseEntity(waitPutinRecordEntity.getWare());
        queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM,waitPutinRecordEntity.getProduceBatchNum()); // 当前生产批
        if (WomConstant.SystemCode.RECORD_TYPE_PROCESS.equals(waitPutinRecordEntity.getRecordType().id)){
            queryParams.put(Constant.BAPQuery.TASK_PROCESS_ID,waitPutinRecordEntity.getTaskProcessId().getId()); // 当前工序
        }
    }
    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(pageIndex, 20, queryParams,false);
        });

        mTemporaryActivityListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mWaitPutinRecordEntity = (WaitPutinRecordEntity) obj;
            String tag = String.valueOf(childView.getTag());
            switch (tag) {
                case "routineStartTv":
                case "checkStartTv":
                case "putInStartTv":
                    showOperateConfirmDialog();
                    break;
            }

        });

    }

    /**
     * @param
     * @return
     * @description 活动操作确认：开启/结束
     * @author zhangwenshuai1 2020/3/25
     */
    private void showOperateConfirmDialog() {
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(mWaitPutinRecordEntity.getExeState().id)) {
            customDialog.bindView(R.id.tipContentTv, "确认开始活动操作？")
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(context.getResources().getString(R.string.wom_dealing));
                        presenterRouter.create(ActivityOperateAPI.class).operateActivity(mWaitPutinRecordEntity.getId(), null,false);
                    }, true)
                    .show();
        } else {
            double planNum=mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()!=null?mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().doubleValue():0;
            double sumNum=mWaitPutinRecordEntity.getTaskActiveId().getSumNum()!=null?mWaitPutinRecordEntity.getTaskActiveId().getSumNum().doubleValue():0;
            String checkTip="确认结束活动操作？";
            if (sumNum<planNum)
                checkTip="当前用料量小于计划投料量，是否需要结束此活动？";
            else if (sumNum>planNum)
                checkTip="当前用料量多于计划投料量，是否需要结束此活动？";
            customDialog.bindView(R.id.tipContentTv,checkTip)
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(context.getResources().getString(R.string.wom_dealing));
                        presenterRouter.create(ActivityOperateAPI.class).operateActivity(mWaitPutinRecordEntity.getId(), null,true);
                    }, true)
                    .show();
        }

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

    @Override
    public void operateActivitySuccess(BAP5CommonEntity entity) {
        if (mTemporaryActivityListAdapter.getList()!=null && !mTemporaryActivityListAdapter.getList().isEmpty()){
            mTemporaryActivityListAdapter.clear();
            mTemporaryActivityListAdapter.notifyDataSetChanged();
        }
        onLoadSuccessAndExit(context.getResources().getString(R.string.wom_dealt_success), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
                refreshListController.refreshBegin();
            }
        });
    }

    @Override
    public void operateActivityFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
