package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.PowerCode;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CheckItemReportAPI;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.ProCheckDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CheckItemReportContract;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.dto.ProCheckDetailDTO;
import com.supcon.mes.module_wom_producetask.presenter.CheckItemReportPresenter;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.CheckItemListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 检查活动项list
 */
@Router(Constant.Router.WOM_CHECK_LIST)
@Presenter(value = {CommonListPresenter.class, CheckItemReportPresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {GetPowerCodeController.class})
public class CheckItemListActivity extends BaseRefreshRecyclerActivity<ProCheckDetailEntity> implements CommonListContract.View, CheckItemReportContract.View {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    public RecyclerView contentView;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("finishBtn")
    Button finishBtn;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private CheckItemListAdapter mCheckItemListAdapter;
    private WaitPutinRecordEntity mWaitPutinRecordEntity;

    @Override
    protected IListAdapter<ProCheckDetailEntity> createAdapter() {
        mCheckItemListAdapter = new CheckItemListAdapter(context);
        return mCheckItemListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_check_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));
        mWaitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);

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
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_check_item_list));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        refreshListController.setOnRefreshListener(() -> presenterRouter.create(CommonListAPI.class).list(0, customCondition, queryParams,
                WomConstant.URL.CHECK_ITEM_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()), ""));

        RxView.clicks(submitBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        doSubmit(false);
                    }
                });
        RxView.clicks(finishBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        doSubmit(true);
                    }
                });
        mCheckItemListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SelectDataEvent<ProCheckDetailEntity> dataEvent = new SelectDataEvent<>((ProCheckDetailEntity) obj, "");

        });
    }

    /**
     * @param
     * @param b
     * @return
     * @author zhangwenshuai1 2020/4/8
     * @description 检查活动报工提交
     */
    private void doSubmit(boolean b) {
        if (checkSubmit()) {
            return;
        }

        if (b){
            CustomDialog customDialog = new CustomDialog(context)
                    .layout(R.layout.wom_dialog_confirm, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
            Objects.requireNonNull(customDialog.getDialog().getWindow()).setBackgroundDrawableResource(R.color.transparent);
            customDialog.bindView(R.id.tipContentTv, context.getResources().getString(R.string.wom_check_submit_tip))
                    .bindClickListener(R.id.cancelTv, null, true)
                    .bindClickListener(R.id.confirmTv, v -> submit(true), true)
                    .show();
        }else {
            submit(false);
        }
    }

    private void submit(boolean b) {
        onLoading(getString(R.string.wom_dealing));

        ProCheckDetailDTO proCheckDetailDTO = new ProCheckDetailDTO();
        proCheckDetailDTO.setOperateType("save");
        proCheckDetailDTO.setIds2del("");
        proCheckDetailDTO.setViewCode("WOM_1.0.0_procReport_checkFeedBackEdit");
//        proCheckDetailDTO.setWorkFlowVar(new WorkFlowVar());
        mWaitPutinRecordEntity.getProcReportId().setSaveAndComplete(b);
        mWaitPutinRecordEntity.getProcReportId().setTaskId(mWaitPutinRecordEntity.getTaskId());
        mWaitPutinRecordEntity.getProcReportId().setTaskActiveId(mWaitPutinRecordEntity.getTaskActiveId());
        mWaitPutinRecordEntity.getProcReportId().setTaskProcessId(mWaitPutinRecordEntity.getTaskProcessId());

        proCheckDetailDTO.setProcReport(mWaitPutinRecordEntity.getProcReportId());

        ProCheckDetailDTO.DgListEntity dgListEntity = new ProCheckDetailDTO.DgListEntity();
        dgListEntity.setDg(GsonUtil.gsonString(mCheckItemListAdapter.getList()));
        proCheckDetailDTO.setDgList(dgListEntity);

        presenterRouter.create(CheckItemReportAPI.class).submit(mWaitPutinRecordEntity.getProcReportId().getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), proCheckDetailDTO);
    }

    private boolean checkSubmit() {
        if (mCheckItemListAdapter.getList() == null || mCheckItemListAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (ProCheckDetailEntity proCheckDetailEntity : mCheckItemListAdapter.getList()) {
            if (!TextUtils.isEmpty(proCheckDetailEntity.getStandard()) && TextUtils.isEmpty(proCheckDetailEntity.getReportValue())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mCheckItemListAdapter.getList().indexOf(proCheckDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_value));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        List<ProCheckDetailEntity> proCheckDetailEntityList = GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), ProCheckDetailEntity.class);
        refreshListController.refreshComplete(proCheckDetailEntityList);
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        onLoadSuccessAndExit(getString(R.string.wom_dealt_success), () -> {
            EventBus.getDefault().post(new RefreshEvent());
            finish();
        });
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }
}
