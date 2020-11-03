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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
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
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
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
import com.supcon.mes.module_wom_producetask.model.api.PutInReportAPI;
import com.supcon.mes.module_wom_producetask.model.api.RemainQRCodeAPI;
import com.supcon.mes.module_wom_producetask.model.bean.PutInDetailEntity;
import com.supcon.mes.module_wom_producetask.model.bean.RemainMaterialEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.model.contract.PutInReportContract;
import com.supcon.mes.module_wom_producetask.model.contract.RemainQRCodeContract;
import com.supcon.mes.module_wom_producetask.model.dto.PutinDetailDTO;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;
import com.supcon.mes.module_wom_producetask.presenter.PutInReportPresenter;
import com.supcon.mes.module_wom_producetask.presenter.RemainQRCodePresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.PutInReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/9
 * Email zhangwenshuai1@supcon.com
 * Desc 人工投料活动报工
 */
@Router(Constant.Router.WOM_PUT_IN_REPORT)
@Presenter(value = {CommonListPresenter.class, PutInReportPresenter.class, RemainQRCodePresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {GetPowerCodeController.class, CommonScanController.class})
public class PutInActivityReportActivity extends BaseRefreshRecyclerActivity<PutInDetailEntity> implements CommonListContract.View, PutInReportContract.View, RemainQRCodeContract.View {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("customListWidgetName")
    TextView customListWidgetName;
    @BindByTag("customWidgetEditLl")
    LinearLayout customWidgetEditLl;
    @BindByTag("customListWidgetEdit")
    ImageView customListWidgetEdit;
    @BindByTag("customWidgetRightName")
    TextView customWidgetRightName;
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
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), 0);
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));

        if (!WomConstant.SystemCode.MATERIAL_BATCH_02.equals(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getIsBatch().id)) {
            mPutInReportDetailAdapter.setNoMaterialBatchNo(true);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(String.format("%s%s", mWaitPutinRecordEntity.getTaskActiveId().getActiveType().value, getString(R.string.wom_report)));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_material_report_detail));
        customWidgetEditLl.setVisibility(View.VISIBLE);
        customListWidgetEdit.setImageResource(R.drawable.ic_wxgd_reference);
        customWidgetRightName.setText(context.getResources().getString(R.string.wom_remain_material_refence));

        materialName.setContent(String.format("%s(%s)", mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getName(), mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode()));
//        materialCode.setContent(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode());
//        planNum.setContent(String.valueOf(mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()));

        SpannableString planNumSpan = new SpannableString(getString(R.string.wom_plan) + "\n\n" + (mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null ? "--" : mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity()));
        planNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_blue)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, planNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        planNumTv.setText(planNumSpan);

        SpannableString sumNumSpan = new SpannableString(getString(R.string.wom_sum) + "\n\n" + mWaitPutinRecordEntity.getTaskActiveId().getSumNum());
        sumNumSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.dark_green)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumSpan.setSpan(new AbsoluteSizeSpan(DisplayUtil.dip2px(18, context)), 4, sumNumSpan.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sumNumTv.setText(sumNumSpan);

        SpannableString remainderNumSpan;
        if (mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null) {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) + "\n\n" + 0);
        } else {
            remainderNumSpan = new SpannableString(getString(R.string.wom_remainder) + "\n\n" + mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().subtract(mWaitPutinRecordEntity.getTaskActiveId().getSumNum()));
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
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
        });
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                        WomConstant.URL.PUT_IN_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()), "");
            }
        });
        customListWidgetAdd.setOnClickListener(v -> {
            addMaterialReport(null, null);
        });
        RxView.clicks(customWidgetEditLl).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constant.IntentKey.ID, mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getId() == null ? -1L : mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getId());
                        IntentRouter.go(context, Constant.Router.WOM_REMAIN_MATERIAL_LIST, bundle);
                    }
                });
        mPutInReportDetailAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mCurrentPosition = position;
            mPutInDetailEntity = (PutInDetailEntity) obj;
            switch (childView.getTag().toString()) {
                case "warehouseTv":
                    IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                    break;
                case "storeSetTv":
                    if (mPutInDetailEntity.getWareId() == null) {
                        ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
                        break;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constant.IntentKey.WARE_ID, mPutInDetailEntity.getWareId().getId());
                    IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                    break;
                case "itemViewDelBtn":
                    mPutInReportDetailAdapter.getList().remove(obj);
                    mPutInReportDetailAdapter.notifyItemRangeRemoved(position, 1);
                    mPutInReportDetailAdapter.notifyItemRangeChanged(position, mPutInReportDetailAdapter.getItemCount() - position);
                    if (mPutInDetailEntity.getId() != null) {
                        dgDeletedIds += mPutInDetailEntity.getId() + ",";
                    }
                    break;
                default:
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
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)){
            MaterialQRCodeEntity materialQRCodeEntity = MaterQRUtil.materialQRCode(context, codeResultEvent.scanResult);
            if (materialQRCodeEntity == null) return;
            if (materialQRCodeEntity.isRequest()) {
                if ("remain".equals(materialQRCodeEntity.getType())){ // 尾料
                    onLoading(context.getResources().getString(R.string.loading));
                    presenterRouter.create(RemainQRCodeAPI.class).getMaterialByQR(materialQRCodeEntity.getPK());
                }else {
                    //TODO...
                    ToastUtils.show(context,context.getResources().getString(R.string.wom_no_realize));
                }
            } else {
                if (checkMaterial(materialQRCodeEntity))
                    return;
                addMaterialReport(materialQRCodeEntity, null);
            }
        }

    }

    /**
     * 新增明细
     *
     * @param materialQRCodeEntity
     * @param remainMaterialEntity
     */
    private void addMaterialReport(MaterialQRCodeEntity materialQRCodeEntity, RemainMaterialEntity remainMaterialEntity) {
        PutInDetailEntity putInDetailEntity = new PutInDetailEntity();
        putInDetailEntity.setMaterialId(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId()); // 物料
        putInDetailEntity.setPutinTime(new Date().getTime());  // 投料时间

        if (remainMaterialEntity != null) { // 尾料参照
            putInDetailEntity.setRemainId(remainMaterialEntity);
            putInDetailEntity.setMaterialId(remainMaterialEntity.getMaterial());
            putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_02)); // 使用
            putInDetailEntity.setMaterialBatchNum(remainMaterialEntity.getBatchText());
            putInDetailEntity.setPutinNum(remainMaterialEntity.getRemainNum());
            putInDetailEntity.setWareId(remainMaterialEntity.getWareId());
            putInDetailEntity.setStoreId(remainMaterialEntity.getStoreId());

        } else {
            if (materialQRCodeEntity != null) { // 扫描物料
                putInDetailEntity.setMaterialBatchNum(materialQRCodeEntity.getMaterialBatchNo());
                putInDetailEntity.setPutinNum(materialQRCodeEntity.getNum());
                putInDetailEntity.setSpecificationNum(materialQRCodeEntity.getNum());
                putInDetailEntity.setWareId(materialQRCodeEntity.getFromWare());
                putInDetailEntity.setStoreId(materialQRCodeEntity.getFromStore());
            }
        }

        mPutInReportDetailAdapter.addData(putInDetailEntity);
        mPutInReportDetailAdapter.notifyItemRangeInserted(mPutInReportDetailAdapter.getItemCount() - 1, 1);
        mPutInReportDetailAdapter.notifyItemRangeChanged(mPutInReportDetailAdapter.getItemCount() - 1, 1);
        contentView.smoothScrollToPosition(mPutInReportDetailAdapter.getItemCount() - 1);
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
        PutinDetailDTO putinDetailDTO = new PutinDetailDTO();
        putinDetailDTO.setOperateType("save");
        putinDetailDTO.setIds2del("");
        putinDetailDTO.setViewCode("WOM_1.0.0_procReport_putInFeedBackEdit");
        putinDetailDTO.setWorkFlowVar(new WorkFlowVar());
        putinDetailDTO.setProcReport(mWaitPutinRecordEntity.getProcReportId());

        PutinDetailDTO.DgListEntity dgListEntity = new PutinDetailDTO.DgListEntity();

        for (PutInDetailEntity putInDetailEntity : mPutInReportDetailAdapter.getList()) {
            // 尾料处理方式
            if (putInDetailEntity.getRemainId() == null) {
                if (putInDetailEntity.getRemainNum() == null || putInDetailEntity.getRemainNum().floatValue() == 0) {
                    putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_03));
                } else {
                    putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_01));
                }
            }
        }

        dgListEntity.setDg(GsonUtil.gsonStringSerializeNulls(mPutInReportDetailAdapter.getList()));
        putinDetailDTO.setDgList(dgListEntity);

        PutinDetailDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new PutinDetailDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        putinDetailDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(PutInReportAPI.class).submit(false, mWaitPutinRecordEntity.getProcReportId().getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), putinDetailDTO, null);

    }

    private boolean checkSubmit() {
        List<PutInDetailEntity> list = mPutInReportDetailAdapter.getList();
        if (list == null || list.size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }

        for (PutInDetailEntity putInDetailEntity : list) {
            if (WomConstant.SystemCode.MATERIAL_BATCH_02.equals(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getIsBatch().id) && TextUtils.isEmpty(putInDetailEntity.getMaterialBatchNum())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (putInDetailEntity.getWareId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_ware));
                return true;
            }
            if (putInDetailEntity.getWareId() != null && putInDetailEntity.getWareId().getStoreSetState() && putInDetailEntity.getStoreId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_warehouse_enable_please_write_storage));
                return true;
            }
            if (putInDetailEntity.getPutinNum() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (list.indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_num));
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
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), PutInDetailEntity.class));
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
            WarehouseEntity warehouseEntity = (WarehouseEntity) selectDataEvent.getEntity();
            if (mPutInDetailEntity.getWareId() != null && !warehouseEntity.getId().equals(mPutInDetailEntity.getWareId().getId())){
                mPutInDetailEntity.setStoreId(null);
            }
            mPutInDetailEntity.setWareId(warehouseEntity);
            mPutInReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
        } else if (object instanceof StoreSetEntity) {
            mPutInDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
            mPutInReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
        } else if (object instanceof RemainMaterialEntity) {
            addMaterialReport(null, (RemainMaterialEntity) selectDataEvent.getEntity());
        }
    }


    @Override
    public void getMaterialByQRSuccess(BAP5CommonEntity entity) {
        onLoadSuccess();
        MaterialQRCodeEntity materialQRCodeEntity = (MaterialQRCodeEntity) entity.data;
        if (checkMaterial(materialQRCodeEntity)) return;
        addMaterialReport(materialQRCodeEntity,null);
    }

    @Override
    public void getMaterialByQRFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }

    private boolean checkMaterial(MaterialQRCodeEntity materialQRCodeEntity) {
        if (!materialQRCodeEntity.getMaterial().getCode().equals(mWaitPutinRecordEntity.getTaskActiveId().getMaterialId().getCode())) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_scan_material_error));
            return true;
        }
        if (!TextUtils.isEmpty(mWaitPutinRecordEntity.getTaskActiveId().getMaterialBatchNum()) && !materialQRCodeEntity.getMaterialBatchNo().equals(mWaitPutinRecordEntity.getTaskActiveId().getMaterialBatchNum())) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_scan_batchNo_error));
            return true;
        }
        return false;
    }

}
