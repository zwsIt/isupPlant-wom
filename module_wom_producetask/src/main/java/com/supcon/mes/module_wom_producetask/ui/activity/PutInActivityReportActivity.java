package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import android.util.Log;
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
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.IntentRouter;
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
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.api.PutInReportAPI;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.presenter.PutInReportPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.PutInReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 人工投料活动报工
 */
@Router(Constant.Router.WOM_PUT_IN_REPORT)
@Presenter(value = {CommonListPresenter.class ,PutInReportPresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {GetPowerCodeController.class, CommonScanController.class})
public class PutInActivityReportActivity extends BaseRefreshRecyclerActivity<PutInDetailEntity> implements CommonListContract.View, PutInReportContract.View {
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
    private PutInReportDetailAdapter mPutInReportDetailAdapter;
    private int mCurrentPosition;
    private PutInDetailEntity mPutInDetailEntity;
    private String dgDeletedIds = "";

    private List<PutInDetailEntity> inDetailEntities=new ArrayList<>();

    @Override
    protected IListAdapter<PutInDetailEntity> createAdapter() {
        mPutInReportDetailAdapter = new PutInReportDetailAdapter(context);
        return mPutInReportDetailAdapter;
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
                outRect.set(DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),0);
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));


    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText(String.format("%s%s", mWaitPutinRecordEntity.getTaskActiveId().getActiveType().value, getString(R.string.wom_report)));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
//        customListWidgetEdit.setVisibility(View.GONE);
        customListWidgetEdit.setImageResource(R.drawable.ic_search_view);

        materialName.setContent(String.format("%s(%s)", mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getName(), mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode()));
//        materialCode.setContent(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode());
//        planNum.setContent(String.valueOf(mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()));

        SpannableString planNumSpan = new SpannableString(getString(R.string.wom_plan) +"\n\n" + (mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null ? "--" : mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()));
        planNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_blue)),4,planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18,context)),4,planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumTv.setText(planNumSpan);

        SpannableString sumNumSpan = new SpannableString(getString(R.string.wom_sum) +"\n\n" + mWaitPutinRecordEntity.getTaskActiveId().getSumNum());
        sumNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_green)),4,sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18,context)),4,sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumTv.setText(sumNumSpan);

        SpannableString remainderNumSpan;
        if (mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null){
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) +"\n\n" + 0);
        }else {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) +"\n\n" + mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().subtract(mWaitPutinRecordEntity.getTaskActiveId().getSumNum()));
        }
        remainderNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_yellow)),4,remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18,context)),4,remainderNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        remainderNumTv.setText(remainderNumSpan);

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan();
        });
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(CommonListAPI.class).list(1,customCondition,queryParams,
                        WomConstant.URL.PUT_IN_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()),"");
            }
        });
        customListWidgetAdd.setOnClickListener(v -> {
            PutInDetailEntity putInDetailEntity = new PutInDetailEntity();
            putInDetailEntity.setMaterialId(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId()); // 物料
            putInDetailEntity.setWareId(mWaitPutinRecordEntity.getWare());
            putInDetailEntity.setMaterialBatchNum(mWaitPutinRecordEntity.getTaskActiveId().getBatchCode());
            putInDetailEntity.setPutinTime(new Date().getTime());  // 投料时间
            mPutInReportDetailAdapter.addData(putInDetailEntity);
            mPutInReportDetailAdapter.notifyItemRangeInserted(mPutInReportDetailAdapter.getItemCount() - 1, 1);
            mPutInReportDetailAdapter.notifyItemRangeChanged(mPutInReportDetailAdapter.getItemCount() - 1, 1);
            contentView.smoothScrollToPosition(mPutInReportDetailAdapter.getItemCount() - 1);
        });
        RxView.clicks(customListWidgetEdit)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                   if (inDetailEntities.isEmpty()){
                       ToastUtils.show(context,"已投料为空");
                       return;
                   }
                   Bundle bundle=new Bundle();
                   bundle.putSerializable("list", (Serializable) inDetailEntities);
                   IntentRouter.go(this,Constant.Router.WOM_PUT_IN_HISTORY,bundle);
                });
        mPutInReportDetailAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mCurrentPosition = position;
                mPutInDetailEntity = (PutInDetailEntity) obj;
                switch (childView.getTag().toString()) {
                    case "warehouseTv":
                        IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                        break;
                    case "storeSetTv":
                        if (mPutInDetailEntity.getWareId() == null) {
                            ToastUtils.show(context, "请先选择仓库");
                            break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.WARE_ID, mPutInDetailEntity.getWareId().getId());
                        IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                        break;
                    case "itemViewDelBtn":
                        mPutInReportDetailAdapter.getList().remove(obj);
                        mPutInReportDetailAdapter.notifyItemRangeRemoved(position,1);
                        mPutInReportDetailAdapter.notifyItemRangeChanged(position,mPutInReportDetailAdapter.getItemCount()-position);
                        if (mPutInDetailEntity.getId() != null){
                            dgDeletedIds += mPutInDetailEntity.getId() + ",";
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
    long lastTime;
    boolean isPutMaterialFull=false;
    PutInDetailEntity scanPutInDetailEntity;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {

        if (isDialogShowing()){

            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= 100) {
            String[] arr = MaterQRUtil.materialQRCode(codeResultEvent.scanResult);
            if (arr != null && arr.length == 7) {
                String incode = arr[0].replace("incode=", "");
                String batchno = arr[1].replace("batchno=", "");
                String batchno2 = arr[2].replace("batchno2=", "");
                String packqty = arr[3].replace("packqty=", "");
                String packs = arr[4].replace("packs=", "");
                String purcode = arr[5].replace("purcode=", "");
                String orderno = arr[6].replace("orderno=", "");

                if (incode.equals(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode())) {
                    String batchCode=mWaitPutinRecordEntity.getTaskActiveId().getBatchCode();
                    if (!StringUtil.isEmpty(batchCode) && !batchCode.trim().equals(batchno)) {

                        showTipDialog("非当前物料批号，请重新扫描");
                        return;
                    }

                    double planQuality=mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()!=null?mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().doubleValue():0;
                    int size=mPutInReportDetailAdapter.getItemCount();
                    double packqtyQuality=Double.parseDouble(packqty);
                    double totalInNum=0;
                    scanPutInDetailEntity = new PutInDetailEntity();
                    scanPutInDetailEntity.setMaterialId(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId()); // 物料
                    scanPutInDetailEntity.setMaterialBatchNum(batchno);
                    scanPutInDetailEntity.setWareId(mWaitPutinRecordEntity.getWare());
                    scanPutInDetailEntity.setPutinNum(!TextUtils.isEmpty(packqty) ? new BigDecimal(packqty) : null);
                    scanPutInDetailEntity.setPutinTime(new Date().getTime());  // 投料时间
                    for (int i = 0; i <size ; i++) {
                        PutInDetailEntity inDetailEntity=mPutInReportDetailAdapter.getItem(i);
                        double inNum=inDetailEntity.getPutinNum()!=null?inDetailEntity.getPutinNum().doubleValue():0;
                        totalInNum+=inNum;
                        if (totalInNum+packqtyQuality>planQuality){
                            isPutMaterialFull=true;
                            showTipDialog("请确认当前投料量与计划投料量是否相符");
                            return;
                        }
                    }

                    mPutInReportDetailAdapter.addData(scanPutInDetailEntity);
                    mPutInReportDetailAdapter.notifyItemRangeInserted(mPutInReportDetailAdapter.getItemCount() - 1, 1);
                    mPutInReportDetailAdapter.notifyItemRangeChanged(mPutInReportDetailAdapter.getItemCount() - 1, 1);
                    contentView.smoothScrollToPosition(mPutInReportDetailAdapter.getItemCount() - 1);
                } else {

                    showTipDialog("非当前物料，请重新扫描");
                }
            } else {

                ToastUtils.show(context, "二维码信息解析异常！");
            }
            lastTime = currentTime;
        }


    }
    CustomDialog customDialog;
    private void showTipDialog(String content){
        customDialog = new CustomDialog(context);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent); // 除去dialog设置四个圆角出现的黑色背景
        customDialog.layout(R.layout.tlzy_dialog_confirm, DisplayUtil.dip2px(300,context), WRAP_CONTENT)
                .title(content)
                .bindClickListener(R.id.cancelTv, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isPutMaterialFull){
                            isPutMaterialFull=false;
                        }
                    }
                }, true)
                .bindClickListener(R.id.confirmTv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPutMaterialFull){
                            mPutInReportDetailAdapter.addData(scanPutInDetailEntity);
                            mPutInReportDetailAdapter.notifyItemRangeInserted(mPutInReportDetailAdapter.getItemCount() - 1, 1);
                            mPutInReportDetailAdapter.notifyItemRangeChanged(mPutInReportDetailAdapter.getItemCount() - 1, 1);
                            contentView.smoothScrollToPosition(mPutInReportDetailAdapter.getItemCount() - 1);
                            isPutMaterialFull=false;
                        }
                    }
                }, true)
                .show();
        if (isDialogShowing() && !isPutMaterialFull) {
            customDialog.getDialog().findViewById(R.id.cancelTv).setVisibility(View.GONE);
            customDialog.getDialog().findViewById(R.id.ly_separator_line).setVisibility(View.GONE);
        }
    }
    private boolean isDialogShowing(){
        return customDialog != null && customDialog.getDialog().isShowing();
    }
    /**
     * @author zhangwenshuai1 2020/4/2
     * @param
     * @return
     * @description 报工
     *
     */
    private void submitReport() {
        if (checkSubmit()){
            return;
        }
        onLoading(context.getResources().getString(R.string.wom_dealing));
        PutinDetailDTO putinDetailDTO = new PutinDetailDTO();
        putinDetailDTO.setOperateType("save");
        putinDetailDTO.setIds2del("");
        putinDetailDTO.setViewCode("WOM_1.0.0_procReport_putInFeedBackEdit");
        putinDetailDTO.setWorkFlowVar(new WorkFlowVar());
        putinDetailDTO.setProcReport(mWaitPutinRecordEntity.getProcReportId());

        PutinDetailDTO.DgListEntity dgListEntity = new PutinDetailDTO.DgListEntity();
        dgListEntity.setDg(GsonUtil.gsonString(mPutInReportDetailAdapter.getList()));
        Log.i("PutinDetailDTO",GsonUtil.gsonString(mPutInReportDetailAdapter.getList()));
        putinDetailDTO.setDgList(dgListEntity);

        PutinDetailDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new PutinDetailDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        putinDetailDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(PutInReportAPI.class).submit(false,mWaitPutinRecordEntity.getProcReportId().getId(),getController(GetPowerCodeController.class).getPowerCodeResult(),putinDetailDTO,null);

    }

    private boolean checkSubmit() {
        if (mPutInReportDetailAdapter.getList() == null || mPutInReportDetailAdapter.getList().size() <= 0){
            ToastUtils.show(context,context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (PutInDetailEntity putInDetailEntity : mPutInReportDetailAdapter.getList()){
//            if (TextUtils.isEmpty(putInDetailEntity.getMaterialBatchNum())){
//                ToastUtils.show(context, "第【" + (mPutInReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + "】项请填写物料批号");
//                return true;
//            }
            if (putInDetailEntity.getWareId() == null){
                ToastUtils.show(context, "第【" + (mPutInReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + "】项请填写仓库");
                return true;
            }
            if (putInDetailEntity.getPutinNum() == null){
                ToastUtils.show(context, "第【" + (mPutInReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + "】项请填写用料量");
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
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data),CommonBAPListEntity.class);
//        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),PutInDetailEntity.class));
        refreshListController.refreshComplete();
        inDetailEntities.addAll(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),PutInDetailEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,ErrorMsgHelper.msgParse(errorMsg));
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
            mPutInDetailEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (object instanceof StoreSetEntity) {
            mPutInDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mPutInReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }



}
