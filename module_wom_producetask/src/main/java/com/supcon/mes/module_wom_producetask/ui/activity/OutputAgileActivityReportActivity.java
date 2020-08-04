package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.beans.WorkFlowVar;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.GetPowerCodeController;
import com.supcon.mes.middleware.controller.ProductController;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.Good;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.PowerCode;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.api.OutputReportAPI;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.OutputReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.OutputDetailDTO;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.presenter.OutputReportPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.OutputAgileReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.ui.adapter.OutputReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 灵活产出活动报工
 */
@Router(Constant.Router.WOM_OUTPUT_AGILE_REPORT)
@Presenter(value = {CommonListPresenter.class, OutputReportPresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {GetPowerCodeController.class, CommonScanController.class})
public class OutputAgileActivityReportActivity extends BaseRefreshRecyclerActivity<OutputDetailEntity> implements CommonListContract.View, OutputReportContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("customListWidgetName")
    TextView customListWidgetName;
    @BindByTag("customListWidgetEdit")
    ImageView customListWidgetEdit;
    @BindByTag("customListWidgetAdd")
    ImageView customListWidgetAdd;
    @BindByTag("taskProcess")
    CustomTextView taskProcess;
    @BindByTag("planNum")
    CustomTextView planNum;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    private WaitPutinRecordEntity mWaitPutinRecordEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private OutputAgileReportDetailAdapter mOutputAgileReportDetailAdapter;
    private int mCurrentPosition;
    private OutputDetailEntity mOutputDetailEntity;
    private String dgDeletedIds = "";

    @Override
    protected IListAdapter<OutputDetailEntity> createAdapter() {
        mOutputAgileReportDetailAdapter = new OutputAgileReportDetailAdapter(context);
        return mOutputAgileReportDetailAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_put_in_agile_report;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(true);
        mWaitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        contentView.setLayoutManager(new SmoothScrollLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), 0);
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));


    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.wom_manual_agile_output_report));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        rightBtn.setVisibility(View.GONE);
        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
        customListWidgetEdit.setVisibility(View.GONE);

        taskProcess.setContent(TextUtils.isEmpty(mWaitPutinRecordEntity.getTaskProcessId().getName()) ? mWaitPutinRecordEntity.getProcessName(): mWaitPutinRecordEntity.getTaskProcessId().getName());
        planNum.setContent(mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null ? "--" : mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().toString());

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        getController(CommonScanController.class).openInfrared();
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan();
        });
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                        WomConstant.URL.OUTPUT_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()), "");
            }
        });
        customListWidgetAdd.setOnClickListener(v -> {
            OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
            outputDetailEntity.setPutinTime(new Date().getTime());  // 投料时间
            outputDetailEntity.setWareId(mWaitPutinRecordEntity.getWare());
            mOutputAgileReportDetailAdapter.addData(outputDetailEntity);
            mOutputAgileReportDetailAdapter.notifyItemRangeInserted(mOutputAgileReportDetailAdapter.getItemCount() - 1, 1);
            mOutputAgileReportDetailAdapter.notifyItemRangeChanged(mOutputAgileReportDetailAdapter.getItemCount() - 1, 1);
            contentView.smoothScrollToPosition(mOutputAgileReportDetailAdapter.getItemCount() - 1);
        });
        mOutputAgileReportDetailAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mCurrentPosition = position;
                mOutputDetailEntity = (OutputDetailEntity) obj;
                switch (childView.getTag().toString()) {
                    case "materialName":
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(Constant.IntentKey.SINGLE_CHOICE,true);
                        IntentRouter.go(context, Constant.Router.PRODUCT_DETAIL,bundle);
                        break;
                    case "warehouseTv":
                        IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                        break;
                    case "storeSetTv":
                        if (mOutputDetailEntity.getWareId() == null) {
                            ToastUtils.show(context, "请先选择仓库");
                            break;
                        }
                        Bundle bundle1 = new Bundle();
                        bundle1.putLong(Constant.IntentKey.WARE_ID, mOutputDetailEntity.getWareId().getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle1);
                        break;
                    case "itemViewDelBtn":
                        mOutputAgileReportDetailAdapter.getList().remove(obj);
                        mOutputAgileReportDetailAdapter.notifyItemRangeRemoved(position, 1);
                        mOutputAgileReportDetailAdapter.notifyItemRangeChanged(position, mOutputAgileReportDetailAdapter.getItemCount()-position);
                        if (mOutputDetailEntity.getId() != null) {
                            dgDeletedIds += mOutputDetailEntity.getId() + ",";
                        }
                        break;
                    default:
                }
            }
        });
        RxView.clicks(submitBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        submitReport();
                    }
                });
    }
    /**
     * 扫描功能：红外、摄像头扫描监听事件
     * @param codeResultEvent
     */
    Map<String, Object> goodMap = new HashMap<>();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {


    }
    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/4/2
     * @description 报工
     */
    private void submitReport() {
        if (checkSubmit()) {
            return;
        }
        onLoading(context.getResources().getString(R.string.wom_dealing));
        OutputDetailDTO outputDetailDTO = new OutputDetailDTO();
        outputDetailDTO.setOperateType("save");
        outputDetailDTO.setIds2del("");
        outputDetailDTO.setViewCode("WOM_1.0.0_procReport_outputFeedBackEdit");
        outputDetailDTO.setWorkFlowVar(new WorkFlowVar());
        outputDetailDTO.setProcReport(mWaitPutinRecordEntity.getProcReportId());

        OutputDetailDTO.DgListEntity dgListEntity = new OutputDetailDTO.DgListEntity();
        dgListEntity.setDg(GsonUtil.gsonString(mOutputAgileReportDetailAdapter.getList()));
        outputDetailDTO.setDgList(dgListEntity);

        OutputDetailDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new OutputDetailDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        outputDetailDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(OutputReportAPI.class).submit(mWaitPutinRecordEntity.getProcReportId().getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), outputDetailDTO);

    }

    private boolean checkSubmit() {
        if (mOutputAgileReportDetailAdapter.getList() == null || mOutputAgileReportDetailAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (OutputDetailEntity outputDetailEntity : mOutputAgileReportDetailAdapter.getList()) {
            if (TextUtils.isEmpty(outputDetailEntity.getMaterialBatchNum())) {
                ToastUtils.show(context, "第【" + (mOutputAgileReportDetailAdapter.getList().indexOf(outputDetailEntity) + 1) + "】项请填写物料批号");
                return true;
            }
            if (outputDetailEntity.getWareId() == null) {
                ToastUtils.show(context, "第【" + (mOutputAgileReportDetailAdapter.getList().indexOf(outputDetailEntity) + 1) + "】项请填写仓库");
                return true;
            }
            if (outputDetailEntity.getOutputNum() == null) {
                ToastUtils.show(context, "第【" + (mOutputAgileReportDetailAdapter.getList().indexOf(outputDetailEntity) + 1) + "】项请填写产出量");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), OutputDetailEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(context.getResources().getString(R.string.wom_dealt_success));
        EventBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        Object object = selectDataEvent.getEntity();
        if (object instanceof WarehouseEntity) {
            mOutputDetailEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (object instanceof StoreSetEntity) {
            mOutputDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }else if (object instanceof Good) {
            Good good = (Good) selectDataEvent.getEntity();
            MaterialEntity materialEntity = new MaterialEntity();
            materialEntity.setId(good.id);
            materialEntity.setCode(good.code);
            materialEntity.setName(good.name);
            mOutputDetailEntity.setProduct(materialEntity);
        }
        mOutputAgileReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }


}
