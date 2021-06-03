package com.supcon.mes.module_wom_producetask.ui.fragment;

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
import com.supcon.mes.module_wom_producetask.R;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 配方活动list
 */
@Presenter(value = {WaitPutinRecordPresenter.class, ActivityOperatePresenter.class})
//@Controller(value = {GetPowerCodeController.class})
//@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
public class FormulaActivityListFragment extends BaseRefreshRecyclerFragment<WaitPutinRecordEntity> implements WaitPutinRecordsListContract.View, ActivityOperateContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    private FormulaActivityListAdapter mFormulaActivityListAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // 活动查询
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // 当前操作项

    @Override
    protected IListAdapter<WaitPutinRecordEntity> createAdapter() {
        mFormulaActivityListAdapter = new FormulaActivityListAdapter(context);
        return mFormulaActivityListAdapter;
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
        queryParams.put(Constant.BAPQuery.IS_MORE_OTHER, false); // 非其他活动
        WaitPutinRecordEntity waitPutinRecordEntity = (WaitPutinRecordEntity) getActivity().getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM, waitPutinRecordEntity.getProduceBatchNum()); // 当前生产批
        if (WomConstant.SystemCode.RECORD_TYPE_PROCESS.equals(waitPutinRecordEntity.getRecordType().id)) {
            queryParams.put(Constant.BAPQuery.TASK_PROCESS_ID, waitPutinRecordEntity.getTaskProcessId().getId()); // 当前工序
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
            presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(pageIndex, 20, queryParams, false);
        });

        mFormulaActivityListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mWaitPutinRecordEntity = (WaitPutinRecordEntity) obj;
            String tag = String.valueOf(childView.getTag());
            switch (tag) {
                case "routineStartTv":
                case "checkStartTv":
                case "putInStartTv":
                case "qualityStartTv":
                    showOperateConfirmDialog(mWaitPutinRecordEntity.getTaskActiveId().getCheckTip());
                    break;
                default:
            }

        });

    }

    /**
     * @param
     * @param checkTip
     * @return
     * @description 活动操作确认：开启/结束
     * @author zhangwenshuai1 2020/3/25
     */
    private void showOperateConfirmDialog(String checkTip) {
        CustomDialog customDialog = new CustomDialog(context,R.style.custom_dialog_transparent)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
//        Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(mWaitPutinRecordEntity.getExeState().id)) {
            customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_start_activity_operate))
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(context.getResources().getString(R.string.wom_dealing));
                        presenterRouter.create(ActivityOperateAPI.class).operateActivity(mWaitPutinRecordEntity.getId(), null, false);
                    }, true)
                    .show();
        } else {
            if (WomConstant.SystemCode.RM_activeType_QUALITY.equals(mWaitPutinRecordEntity.getTaskActiveId().getActiveType().id)) {
                customDialog.bindView(R.id.tipContentTv, TextUtils.isEmpty(mWaitPutinRecordEntity.getTaskActiveId().getReleaseConditions()) ? context.getResources().getString(R.string.wom_advance_release_sure) : mWaitPutinRecordEntity.getTaskActiveId().getReleaseConditions());
            } else {
                customDialog.bindView(R.id.tipContentTv, TextUtils.isEmpty(checkTip) ? context.getResources().getString(R.string.wom_end_activity_operate) : checkTip);
            }
            customDialog.bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(context.getResources().getString(R.string.wom_dealing));
                        presenterRouter.create(ActivityOperateAPI.class).operateActivity(mWaitPutinRecordEntity.getId(), null, true);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSelectDataEvent(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof FactoryModelEntity) {
            mWaitPutinRecordEntity.setEuqId((FactoryModelEntity) selectDataEvent.getEntity());
            mWaitPutinRecordEntity.getTaskProcessId().setEquipmentId((FactoryModelEntity) selectDataEvent.getEntity());
        }
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
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Override
    public void operateActivityFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
