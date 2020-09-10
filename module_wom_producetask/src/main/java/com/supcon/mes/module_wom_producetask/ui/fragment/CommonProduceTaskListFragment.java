package com.supcon.mes.module_wom_producetask.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.PowerCode;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.ProcessOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.ProduceTaskOperateAPI;
import com.supcon.mes.module_wom_producetask.model.api.WaitPutinRecordsListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.TaskProcessEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ProcessOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.ProduceTaskOperateContract;
import com.supcon.mes.module_wom_producetask.model.contract.WaitPutinRecordsListContract;
import com.supcon.mes.module_wom_producetask.presenter.ProcessOperatePresenter;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/23
 * Email zhangwenshuai1@supcon.com
 * Desc 标准生产工单list
 */
@Presenter(value = {WaitPutinRecordPresenter.class, ProduceTaskOperatePresenter.class, ProcessOperatePresenter.class})
@Controller(value = {GetPowerCodeController.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
public class CommonProduceTaskListFragment extends BaseRefreshRecyclerFragment<WaitPutinRecordEntity> implements WaitPutinRecordsListContract.View, ProduceTaskOperateContract.View,
        ProcessOperateContract.View {

    @BindByTag("waitStateRadioGroup")
    RadioGroup waitStateRadioGroup;
    @BindByTag("contentView")
    RecyclerView contentView;
    private ProduceTaskListAdapter mProduceTaskListAdapter;
    Map<String, Object> queryParams = new HashMap<>();          // 指令单查询
    Map<String, Object> processQueryParams = new HashMap<>();   // 工序查询
    Map<String, Object> submitMap = new HashMap<>();            // 工序修改工作单元submit
    private boolean mIsTaskLoad = true;         // 默认工单待办类型加载
    private int mCurrentItemPos;
    private CustomTextView mFactoryCustomTv;    // 工作单元
    private WaitPutinRecordEntity mWaitPutinRecordEntity;   // 当前操作项
    private int mFatherPosition;

    @Override
    protected IListAdapter<WaitPutinRecordEntity> createAdapter() {
        mProduceTaskListAdapter = new ProduceTaskListAdapter(context);
        return mProduceTaskListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_common_produce_task_list;
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
        // 默认标准指令单查询
        queryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_TASK);
        queryParams.put(Constant.BAPQuery.FORMULA_SET_PROCESS, WomConstant.SystemCode.RM_TYPE_COMMON);
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
            if (mIsTaskLoad) {
                presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(pageIndex, 20, queryParams,true);
            } else {
                presenterRouter.create(WaitPutinRecordsListAPI.class).listWaitPutinRecords(pageIndex, 20, processQueryParams,false);
            }
        });

        waitStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.waitExecutedRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_WAIT);
            } else if (checkedId == R.id.executingRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_ING);
            } else if (checkedId == R.id.pausedRBtn) {
                queryParams.put(Constant.BAPQuery.EXE_STATE, WomConstant.SystemCode.EXE_STATE_PAUSED);
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
            mCurrentItemPos = position;
            String tag = String.valueOf(childView.getTag());
            List<Object> paramsList = new ArrayList<>(2);
            switch (tag) {
                case "startTv":
                    paramsList.add(context.getResources().getString(R.string.wom_start));
                    paramsList.add("start");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "pauseTv":
                    paramsList.add(context.getResources().getString(R.string.wom_pause));
                    paramsList.add("pause");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "resumeTv":
                    paramsList.add(context.getResources().getString(R.string.wom_resume));
                    paramsList.add("resume");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
                case "stopTv":
                    // 结束工单报工

                    if(mWaitPutinRecordEntity!=null && mWaitPutinRecordEntity.getTaskId()!=null
                            && mWaitPutinRecordEntity.getTaskId().getBatchContral()!=null
                            &&mWaitPutinRecordEntity.getTaskId().getBatchContral()){
                        paramsList.add(context.getResources().getString(R.string.wom_end));
                        paramsList.add("stop");
                        showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    }
                    else{
                        endProduceTaskReport();
                    }

                    break;
                case "expandIv":
                    if (mWaitPutinRecordEntity.getProcessWaitPutinRecordEntityList().size() > 0) {
                        updateProcessList();
                    } else {
                        mIsTaskLoad = false;
                        processQueryParams.put(Constant.BAPQuery.RECORD_TYPE, WomConstant.SystemCode.RECORD_TYPE_PROCESS); // 工序类型
                        processQueryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM, mWaitPutinRecordEntity.getProduceBatchNum());
                        refreshListController.refreshBegin();
                    }
                    break;
                case "processStartTv":
                    if (WomConstant.SystemCode.EXE_STATE_WAIT.equals(mWaitPutinRecordEntity.getExeState().id)) {
                        paramsList.add(context.getResources().getString(R.string.wom_start));
                        paramsList.add(false);
                    } else {
                        paramsList.add(context.getResources().getString(R.string.wom_end));
                        paramsList.add(true);
                    }
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, false);
                    break;
                case "setEquipmentTv":
                    updateCurrentFactory();
                    break;
                case "dischargeTv":
                    paramsList.add(mWaitPutinRecordEntity.getTaskId().getFeedCondition() == null? context.getResources().getString(R.string.wom_start_predischarge) : mWaitPutinRecordEntity.getTaskId().getFeedCondition());
                    paramsList.add("discharge");
                    showOperateConfirmDialog(paramsList, mWaitPutinRecordEntity, true);
                    break;
            }

        });

    }

    /**
     * @author zhangwenshuai1 2020/4/1
     * @param
     * @return
     * @description 工单结束报工
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
     * @author zhangwenshuai1 2020/3/29
     * @description 更改当前工序之工作单元
     */
    private void updateCurrentFactory() {
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_set_factory, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        mFactoryCustomTv = customDialog.getDialog().findViewById(R.id.factoryCustomTv);
        mFactoryCustomTv.setContent(mWaitPutinRecordEntity.getTaskProcessId().getEquipmentId().getName());
        customDialog.bindChildListener(R.id.factoryCustomTv, (childView, action, obj) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.IntentKey.WAIT_PUT_RECORD,mWaitPutinRecordEntity);
            IntentRouter.go(context, Constant.Router.WOM_FACTORY_LIST,bundle);
        }).bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {
                    if (TextUtils.isEmpty(mFactoryCustomTv.getContent())) {
                        ToastUtils.show(context, R.string.wom_setting_factory);
                        return;
                    }
                    customDialog.getDialog().dismiss();
                    onLoading(context.getResources().getString(R.string.wom_dealing));
                    submitMap.put("operateType","save");
                    submitMap.put("taskProcess",mWaitPutinRecordEntity.getTaskProcessId());
                    submitMap.put("ids2del","");
                    submitMap.put("viewCode","WOM_1.0.0_produceTask_processUnitEdit");
//                    submitMap.put("workFlowVar",new WorkFlowVarDTO());
                    presenterRouter.create(ProcessOperateAPI.class).updateProcessFactoryModelUnit(mWaitPutinRecordEntity.getTaskProcessId().getId(),getController(GetPowerCodeController.class).getPowerCodeResult(),submitMap);
                }, false);
        customDialog.show();
    }

    /**
     * @param
     * @return
     * @description 工单操作确认：开启/暂停/恢复/停止
     * @author zhangwenshuai1 2020/3/25
     */
    private void showOperateConfirmDialog(List<Object> paramsList, WaitPutinRecordEntity waitPutinRecordEntity, boolean isTask) {
        CustomDialog customDialog = new CustomDialog(context)
                .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
        if (isTask) {
            if ("discharge".equals(paramsList.get(1))){ // "提前放料"
                customDialog.bindView(R.id.tipContentTv, String.valueOf(paramsList.get(0)))
                        .bindClickListener(R.id.cancelTv, null, true)
                        .bindClickListener(R.id.confirmTv, v -> {
                            onLoading(getString(R.string.wom_dealing));
                            presenterRouter.create(ProduceTaskOperateAPI.class).operateDischarge(waitPutinRecordEntity.getTaskId().getId());
                        }, true)
                        .show();
            }else {
                customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_confirm_tip) + paramsList.get(0) + context.getResources().getString(R.string.wom_task_operate))
                        .bindClickListener(R.id.cancelTv, null, true)
                        .bindClickListener(R.id.confirmTv, v -> {
                            onLoading(getString(R.string.wom_dealing));
                            presenterRouter.create(ProduceTaskOperateAPI.class).operateProduceTask(waitPutinRecordEntity.getId(), String.valueOf(paramsList.get(1)),null);
                        }, true)
                        .show();
            }

        } else {
            customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_confirm_tip) + paramsList.get(0) + context.getResources().getString(R.string.wom_process_operate))
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> {
                        onLoading(getString(R.string.wom_dealing));
                        mFatherPosition = mWaitPutinRecordEntity.getFatherPosition();
                        presenterRouter.create(ProcessOperateAPI.class).operateProcess(waitPutinRecordEntity.getId(), (Boolean) paramsList.get(1));
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
        if (selectDataEvent.getEntity() instanceof FactoryModelEntity){
            mFactoryCustomTv.setContent(((FactoryModelEntity) selectDataEvent.getEntity()).getName());
            mWaitPutinRecordEntity.setEuqId((FactoryModelEntity) selectDataEvent.getEntity());
            mWaitPutinRecordEntity.getTaskProcessId().setEquipmentId((FactoryModelEntity) selectDataEvent.getEntity());
        }
    }

    @Override
    public void listWaitPutinRecordsSuccess(CommonBAPListEntity entity) {
//        List<WaitPutinRecordEntity> list = GsonUtil.jsonToList(GsonUtil.gsonString(entity.result),WaitPutinRecordEntity.class);
        if (mIsTaskLoad) {
            refreshListController.refreshComplete(entity.result);
        } else {
            // 工序加载
            mIsTaskLoad = true; // 恢复工单加载
            if (entity.result.size() <= 0) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_no_processing));
            } else {
                for (Object waitPutinRecordEntity : entity.result){
                    ((WaitPutinRecordEntity)waitPutinRecordEntity).setFatherPosition(mCurrentItemPos); // 记录父位置
                }
                mProduceTaskListAdapter.getItem(mCurrentItemPos).getProcessWaitPutinRecordEntityList().addAll(entity.result); // 当前工单存储工序list
                updateProcessList();
            }
            refreshListController.refreshComplete();
        }

    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/3/27
     * @description 更新工序list
     */
    private void updateProcessList() {
        List<WaitPutinRecordEntity> processWaitRecordsList = mProduceTaskListAdapter.getItem(mCurrentItemPos).getProcessWaitPutinRecordEntityList();
        mProduceTaskListAdapter.getList().addAll(mCurrentItemPos + 1, processWaitRecordsList);
        mProduceTaskListAdapter.notifyItemRangeInserted(mCurrentItemPos + 1, processWaitRecordsList.size());
        mProduceTaskListAdapter.notifyItemRangeChanged(mCurrentItemPos, processWaitRecordsList.size());
    }

    @Override
    public void listWaitPutinRecordsFailed(String errorMsg) {
        mIsTaskLoad = true; // 恢复工单加载
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateProduceTaskSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Override
    public void operateProduceTaskFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateDischargeSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Override
    public void operateDischargeFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void updateProcessFactoryModelUnitSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        // 只刷新当前项
        mProduceTaskListAdapter.notifyItemRangeChanged(mCurrentItemPos,1);

    }

    @Override
    public void updateProcessFactoryModelUnitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void operateProcessSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        // 更新当前项目
        TaskProcessEntity process;
        try {
            Map<String,Object> map = (Map<String, Object>) entity.data;
            process = GsonUtil.gsonToBean(GsonUtil.gsonString(map.get("process")),TaskProcessEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            refreshListController.refreshBegin();
            return;
        }
        if (WomConstant.SystemCode.EXE_STATE_END.equals(process.getProcessRunState().id)){ // 已结束工序
            // 清理工序list位置项
            mCurrentItemPos = mFatherPosition;  // 确保加载工序后位置
            List<WaitPutinRecordEntity> processList = mProduceTaskListAdapter.getItem(mFatherPosition).getProcessWaitPutinRecordEntityList();
            mProduceTaskListAdapter.getList().removeAll(processList);
            mProduceTaskListAdapter.notifyItemRangeRemoved(mFatherPosition+1,processList.size());
            mProduceTaskListAdapter.notifyItemRangeChanged(mFatherPosition,mProduceTaskListAdapter.getItemCount()-1);
            // 清理当前指令单下工序
            processList.clear();
            // 重新加载工序
            mIsTaskLoad = false;
            refreshListController.refreshBegin();
        }else {
            WaitPutinRecordEntity waitPutinRecordEntity = mProduceTaskListAdapter.getList().get(mCurrentItemPos);
            waitPutinRecordEntity.setExeState(process.getProcessRunState());
            waitPutinRecordEntity.setTaskProcessId(process);
//            waitPutinRecordEntity.getTaskProcessId().setActStartTime(process.getActStartTime());
            mProduceTaskListAdapter.notifyItemRangeChanged(mCurrentItemPos,1);
        }

    }

    @Override
    public void operateProcessFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    public void search(String searchContent){
        queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM, searchContent);
        if(refreshListController!=null){
            refreshListController.refreshBegin();
        }

    }


}
