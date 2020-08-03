package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.inter.PowerCode;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.api.OutputReportAPI;
import com.supcon.mes.module_wom_producetask.model.api.PutInReportAPI;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.OutputReportContract;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.OutputDetailDTO;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.presenter.OutputReportPresenter;
import com.supcon.mes.module_wom_producetask.presenter.PutInReportPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.OutputReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.ui.adapter.PutInReportDetailAdapter;
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
 * Desc 产出活动报工
 */
@Router(Constant.Router.WOM_OUTPUT_REPORT)
@Presenter(value = {CommonListPresenter.class, OutputReportPresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {
        GetPowerCodeController.class,
        CommonScanController.class
})
public class OutputActivityReportActivity extends BaseRefreshRecyclerActivity<OutputDetailEntity> implements CommonListContract.View, OutputReportContract.View {
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
    @BindByTag("materialName")
    CustomTextView materialName;
    //    @BindByTag("materialCode")
//    CustomTextView materialCode;
//    @BindByTag("planNum")
//    CustomTextView planNum;
    @BindByTag("sumNumTv")
    TextView sumNumTv;
    @BindByTag("planNumTv")
    TextView planNumTv;
    @BindByTag("remainderNumTv")
    TextView remainderNumTv;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("contentView")
    RecyclerView contentView;

    private WaitPutinRecordEntity mWaitPutinRecordEntity;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private OutputReportDetailAdapter mOutputReportDetailAdapter;
    private int mCurrentPosition;
    private OutputDetailEntity mOutputDetailEntity;
    private String dgDeletedIds = "";

    @Override
    protected IListAdapter<OutputDetailEntity> createAdapter() {
        mOutputReportDetailAdapter = new OutputReportDetailAdapter(context);
        return mOutputReportDetailAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_put_in_report;
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
        titleText.setText(String.format("%s%s", mWaitPutinRecordEntity.getTaskActiveId().getActiveType().value, getString(R.string.wom_report)));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
        customListWidgetEdit.setVisibility(View.GONE);

        materialName.setContent(String.format("%s(%s)", mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getName(), mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode()));
//        materialCode.setContent(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode());
//        planNum.setContent(String.valueOf(mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()));

        SpannableString planNumSpan = new SpannableString(getString(R.string.wom_plan) + "\n\n" + (mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null ? "--" : mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()));
        planNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_blue)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumTv.setText(planNumSpan);

        SpannableString sumNumSpan = new SpannableString(context.getResources().getString(R.string.wom_output) + "\n\n" + mWaitPutinRecordEntity.getTaskActiveId().getSumNum());
        sumNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_green)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumTv.setText(sumNumSpan);

        SpannableString remainderNumSpan;
        if (mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null) {
            remainderNumSpan = new SpannableString(context.getResources().getString(R.string.wom_remainder) + "\n\n" + 0);
        } else {
            remainderNumSpan = new SpannableString(context.getResources().getString(R.string.wom_remainder) + "\n\n" + mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().subtract(mWaitPutinRecordEntity.getTaskActiveId().getSumNum()));
        }
        remainderNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_yellow)), 4, remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumTv.setText(remainderNumSpan);

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
            outputDetailEntity.setProduct(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId()); // 物料
            outputDetailEntity.setPutinTime(new Date().getTime());  // 投料时间
            mOutputReportDetailAdapter.addData(outputDetailEntity);
            mOutputReportDetailAdapter.notifyItemRangeInserted(mOutputReportDetailAdapter.getItemCount() - 1, 1);
            mOutputReportDetailAdapter.notifyItemRangeChanged(mOutputReportDetailAdapter.getItemCount() - 1, 1);
            contentView.smoothScrollToPosition(mOutputReportDetailAdapter.getItemCount() - 1);
        });
        mOutputReportDetailAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mCurrentPosition = position;
                mOutputDetailEntity = (OutputDetailEntity) obj;
                switch (childView.getTag().toString()) {
                    case "warehouseTv":
                        IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                        break;
                    case "storeSetTv":
                        if (mOutputDetailEntity.getWareId() == null) {
                            ToastUtils.show(context, "请先选择仓库");
                            break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.WARE_ID, mOutputDetailEntity.getWareId().getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                        break;
                    case "itemViewDelBtn":
                        mOutputReportDetailAdapter.getList().remove(obj);
                        mOutputReportDetailAdapter.notifyItemRangeRemoved(position, 1);
                        mOutputReportDetailAdapter.notifyItemRangeChanged(position, mOutputReportDetailAdapter.getItemCount() - position);
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
     *
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        String[] arr = MaterQRUtil.materialQRCode(codeResultEvent.scanResult);
        if (arr != null && arr.length == 8) {
            String materCode=mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode();
            String incode = arr[0].replace("incode=", "");
            String batchno = arr[1].replace("batchno=", "");
            String batchno2 = arr[2].replace("batchno2=", "");
            String packqty = arr[3].replace("packqty=", "");
            String packs = arr[4].replace("packs=", "");
            String purcode = arr[5].replace("purcode=", "");
            String orderno = arr[6].replace("orderno=", "");
            String specs=arr[7].replace("specs=","");
            if (materCode.equals(incode)){
                OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
                outputDetailEntity.setMaterialBatchNum(batchno);
                outputDetailEntity.setOutputNum(!TextUtils.isEmpty(specs)?new BigDecimal(specs):null);
                outputDetailEntity.setProduct(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId()); // 物料
                outputDetailEntity.setPutinTime(new Date().getTime());  // 投料时间
                mOutputReportDetailAdapter.addData(outputDetailEntity);
                mOutputReportDetailAdapter.notifyItemRangeInserted(mOutputReportDetailAdapter.getItemCount() - 1, 1);
                mOutputReportDetailAdapter.notifyItemRangeChanged(mOutputReportDetailAdapter.getItemCount() - 1, 1);
                contentView.smoothScrollToPosition(mOutputReportDetailAdapter.getItemCount() - 1);
            }else {
                ToastUtils.show(context,"非当前所投物料，请重新扫描");
            }
        } else {
            ToastUtils.show(context, "二维码退料信息解析异常！");
        }
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
        dgListEntity.setDg(GsonUtil.gsonString(mOutputReportDetailAdapter.getList()));
        outputDetailDTO.setDgList(dgListEntity);

        OutputDetailDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new OutputDetailDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        outputDetailDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(OutputReportAPI.class).submit(mWaitPutinRecordEntity.getProcReportId().getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), outputDetailDTO);

    }

    private boolean checkSubmit() {
        if (mOutputReportDetailAdapter.getList() == null || mOutputReportDetailAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (OutputDetailEntity outputDetailEntity : mOutputReportDetailAdapter.getList()) {
            if (TextUtils.isEmpty(outputDetailEntity.getMaterialBatchNum())) {
                ToastUtils.show(context, "第【" + (mOutputReportDetailAdapter.getList().indexOf(outputDetailEntity) + 1) + "】项请填写物料批号");
                return true;
            }
            if (outputDetailEntity.getWareId() == null) {
                ToastUtils.show(context, "第【" + (mOutputReportDetailAdapter.getList().indexOf(outputDetailEntity) + 1) + "】项请填写仓库");
                return true;
            }
            if (outputDetailEntity.getOutputNum() == null) {
                ToastUtils.show(context, "第【" + (mOutputReportDetailAdapter.getList().indexOf(outputDetailEntity) + 1) + "】项请填写产出量");
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
        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
            mOutputDetailEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mOutputDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mOutputReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }


}
