package com.supcon.mes.module_wom_producetask.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

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
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.ProduceTaskOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.ProduceTaskOperatePresenter;
import com.supcon.mes.module_wom_producetask.presenter.WaitPutinRecordPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.ProduceTaskListAdapter;

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
 * Desc ??????????????????list
 */
@Presenter(value = {WaitPutinRecordPresenter.class, ProduceTaskOperatePresenter.class})
public class SimpleProduceTaskListFragment extends BaseRefreshRecyclerFragment<WaitPutinRecordEntity> implements WaitPutinRecordsListContract.View, ProduceTaskOperateContract.View {

    @BindByTag("waitStateRadioGroup")
    RadioGroup waitStateRadioGroup;
    @BindByTag("contentView")
    RecyclerView contentView;
    private ProduceTaskListAdapter mProduceTaskListAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // ???????????????
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // ???????????????

    @Override
    protected IListAdapter<WaitPutinRecordEntity> createAdapter() {
        mProduceTaskListAdapter = new ProduceTaskListAdapter(context);
        return mProduceTaskListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_simple_produce_task_list;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
//        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(10,context)));
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
        // ???????????????????????????
        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_TASK);
        queryParams.put(Constant.BAPQuery.FORMULA_SET_PROCESS, WomConstant.SystemCode.RM_TYPE_SIMPLE);
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
            presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(pageIndex, 20, queryParams,true);
        });
        waitStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.waitExecutedRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_WAIT);
            } else if (checkedId == R.id.executingRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_ING);
            } else if (checkedId == R.id.heldRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_HOLD);
            } else if (checkedId == R.id.stoppedRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_STOPPED);
            } else if (checkedId == R.id.abandonedRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_ABANDONED);
            } else {
                queryParams.put(Constant.BAPQuery.EXE_STATE, "");
            }
            refreshListController.refreshBegin();
        });

        mProduceTaskListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mWaitPutinRecordEntity = (WaitPutinRecordEntity) obj;
            String tag = String.valueOf(childView.getTag());
            List<Object> paramsList = new ArrayList<>(2);
            switch (tag) {
                case "startTv":
                    paramsList.add(context.getResources().getString(R.string.wom_start));
                    paramsList.add("start");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "holdTv":
                    paramsList.add(context.getResources().getString(R.string.wom_hold));
                    paramsList.add("hold");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "restartTv":
                    paramsList.add(context.getResources().getString(R.string.wom_restart));
                    paramsList.add("restart");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "stopTv":
                    // ??????????????????
                    endProduceTaskReport();
                    break;
                case "expandIv":
                    break;
                default:
            }

        });

    }

    /**
     * @author zhangwenshuai1 2020/4/1
     * @param
     * @return
     * @description ??????????????????
     *
     */
    private void endProduceTaskReport() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,mWaitPutinRecordEntity);
        IntentRouter.go(context,Constant.Router.WOM_PRODUCE_TASK_END_REPORT,bundle);
    }

    /**
     * @param
     * @return
     * @description ???????????????????????????/??????/??????/??????
     * @author zhangwenshuai1 2020/3/25
     */
    private void showOperateConfirmDialog(List<Object> paramsList, WaitPutinRecordEntity waitPutinRecordEntity, boolean isTask) {
        CustomDialog customDialog = new CustomDialog(context,R.style.custom_dialog_transparent)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_confirm_tip) + paramsList.get(0) + context.getResources().getString(R.string.wom_task_operate))
                .bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {
                    onLoading(getString(R.string.wom_dealing));
                    presenterRouter.create(ProduceTaskOperateAPI.class).operateProduceTask(waitPutinRecordEntity.getId(), String.valueOf(paramsList.get(1)),null);
                }, true)
                .show();

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
    public void operateProduceTaskSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Override
    public void operateProduceTaskFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateDischargeSuccess(BAP5CommonEntity bap5CommonEntity) {

    }

    @Override
    public void operateDischargeFailed(String errorMsg) {

    }

    public void search(String searchContent){
        queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM, searchContent);
        if(refreshListController!=null){
            refreshListController.refreshBegin();
        }
    }

    /**
     * @author zhangwenshuai1 2020/10/26
     * @param
     * @return
     * @description
     *
     */
    public void matchTask(String scanResult) {
        if (mProduceTaskListAdapter.getList() == null || mProduceTaskListAdapter.getList().size() == 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.middleware_no_data));
            return;
        }
        int index = 0;
        for (WaitPutinRecordEntity waitPutinRecordEntity : mProduceTaskListAdapter.getList()) {
            if (scanResult.equals(waitPutinRecordEntity.getProduceBatchNum())) {

                int scrollPosition = mProduceTaskListAdapter.getList().indexOf(waitPutinRecordEntity);

                RecyclerView.LayoutManager layoutManager = contentView.getLayoutManager();
                int firstVisibleItemPosition = 0;
                if (layoutManager instanceof LinearLayoutManager){
                    firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition(); // ???????????????????????????
                }
                // ????????????
                if (firstVisibleItemPosition > scrollPosition){ // ?????????
                    contentView.scrollToPosition(scrollPosition);
                }else { // ?????????
                    contentView.scrollToPosition(scrollPosition + 1);
                }

                if (WomConstant.SystemCode.EXE_STATE_PAUSED.equals(waitPutinRecordEntity.getExeState().id)){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_task_paused));
                    return;
                }
                if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(waitPutinRecordEntity.getExeState().id) ||
                        WomConstant.SystemCode.EXE_STATE_ING.equals(waitPutinRecordEntity.getExeState().id)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,waitPutinRecordEntity);
                    IntentRouter.go(context,Constant.Router.WOM_SIMPLE_ACTIVITY_LIST,bundle);
                }
                return;
            }

            index++;

            if (index == mProduceTaskListAdapter.getList().size()) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_scan_no_produce_no));
            }

        }
    }

}
