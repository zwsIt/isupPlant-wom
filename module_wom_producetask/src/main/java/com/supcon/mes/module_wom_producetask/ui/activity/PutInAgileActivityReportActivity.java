package com.supcon.mes.module_wom_producetask.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.wom.MaterialEntity;
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
import com.supcon.mes.module_wom_producetask.ui.adapter.PutInAgileReportDetailAdapter;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_producetask.util.SmoothScrollLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/14
 * Email zhangwenshuai1@supcon.com
 * Desc 灵活投料活动报工
 */
@Router(Constant.Router.WOM_PUT_IN_AGILE_REPORT)
@Presenter(value = {CommonListPresenter.class, PutInReportPresenter.class, RemainQRCodePresenter.class})
@PowerCode(entityCode = WomConstant.PowerCode.PRODUCE_TASK_LIST)
@Controller(value = {GetPowerCodeController.class, CommonScanController.class, ProductController.class})
public class PutInAgileActivityReportActivity extends BaseRefreshRecyclerActivity<PutInDetailEntity> implements CommonListContract.View, PutInReportContract.View, RemainQRCodeContract.View {
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
    @BindByTag("taskProcess")
    CustomTextView taskProcess;
    @BindByTag("planNum")
    CustomTextView planNum;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private WaitPutinRecordEntity mWaitPutinRecordEntity;
    private PutInAgileReportDetailAdapter mPutInAgileReportDetailAdapter;
    private int mCurrentPosition;
    private PutInDetailEntity mPutInDetailEntity;
    private String dgDeletedIds = "";

    @Override
    protected IListAdapter<PutInDetailEntity> createAdapter() {
        mPutInAgileReportDetailAdapter = new PutInAgileReportDetailAdapter(context);
        return mPutInAgileReportDetailAdapter;
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
        titleText.setText(context.getResources().getString(R.string.wom_agile_put_in_report));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_top_scan));
        customListWidgetName.setText(context.getResources().getString(R.string.wom_material_report_detail));
        customWidgetEditLl.setVisibility(View.VISIBLE);
        customListWidgetEdit.setImageResource(R.drawable.ic_wxgd_reference);
        customWidgetRightName.setText(context.getResources().getString(R.string.wom_remain_material_refence));

        taskProcess.setContent(TextUtils.isEmpty(mWaitPutinRecordEntity.getTaskProcessId().getName()) ? mWaitPutinRecordEntity.getProcessName() : mWaitPutinRecordEntity.getTaskProcessId().getName());
        planNum.setContent(mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity() == null ? "--" : mWaitPutinRecordEntity.getTaskActiveId().getPlanQuantity().toString());

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        rightBtn.setOnClickListener(v -> {
            getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
        });
        refreshListController.setOnRefreshListener(() -> presenterRouter.create(CommonListAPI.class).list(1, customCondition, queryParams,
                WomConstant.URL.PUT_IN_REPORT_LIST_URL + "&id=" + (mWaitPutinRecordEntity.getProcReportId().getId() == null ? -1 : mWaitPutinRecordEntity.getProcReportId().getId()), ""));
        customListWidgetAdd.setOnClickListener(v -> {
            addMaterialReport(null, null);
        });
        RxView.clicks(customWidgetEditLl).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(o -> IntentRouter.go(context, Constant.Router.WOM_REMAIN_MATERIAL_LIST));
        mPutInAgileReportDetailAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            mCurrentPosition = position;
            mPutInDetailEntity = (PutInDetailEntity) obj;
            Bundle bundle = new Bundle();
            switch (childView.getTag().toString()) {
                case "materialName":
                    bundle.putBoolean(Constant.IntentKey.SINGLE_CHOICE, true);
                    IntentRouter.go(context, Constant.Router.PRODUCT_DETAIL, bundle);
                    break;
                case "warehouseTv":
                    IntentRouter.go(context, Constant.Router.WAREHOUSE_LIST_REF);
                    break;
                case "storeSetTv":
                    if (mPutInDetailEntity.getWareId() == null) {
                        ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_ware));
                        break;
                    }
                    bundle.putLong(Constant.IntentKey.WARE_ID, mPutInDetailEntity.getWareId().getId());
                    IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                    break;
                case "itemViewDelBtn":
                    mPutInAgileReportDetailAdapter.getList().remove(obj);
                    mPutInAgileReportDetailAdapter.notifyItemRangeRemoved(position, 1);
                    mPutInAgileReportDetailAdapter.notifyItemRangeChanged(position, mPutInAgileReportDetailAdapter.getItemCount() - position);
                    if (mPutInDetailEntity.getId() != null) {
                        dgDeletedIds += mPutInDetailEntity.getId() + ",";
                    }
                    break;
                default:
            }
        });
        RxView.clicks(submitBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(o -> submitReport());
    }

    /**
     * 扫描功能：红外、摄像头扫描监听事件
     *
     * @param codeResultEvent
     */
//    Map<String, Object> goodMap = new HashMap<>();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)){
            MaterialQRCodeEntity materialQRCodeEntity = MaterQRUtil.materialQRCode(context, codeResultEvent.scanResult);
            if (materialQRCodeEntity == null) return;

            if (materialQRCodeEntity.isRequest()) {
                if ("remain".equals(materialQRCodeEntity.getType())) { // 尾料
                    onLoading(context.getResources().getString(R.string.loading));
                    presenterRouter.create(RemainQRCodeAPI.class).getMaterialByQR(materialQRCodeEntity.getPK());
                } else {
                    //TODO...
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_no_realize));
                }
            } else {
                addMaterialReport(materialQRCodeEntity, null);
            }
        }

    }

    /**
     * 新增明细
     *
     * @param materialQRCodeEntity
     */
    private void addMaterialReport(MaterialQRCodeEntity materialQRCodeEntity, RemainMaterialEntity remainMaterialEntity) {
        PutInDetailEntity putInDetailEntity = new PutInDetailEntity();

        if (remainMaterialEntity != null) { // 尾料参照
            putInDetailEntity.setRemainId(remainMaterialEntity);
            putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_02)); // 使用
            putInDetailEntity.setMaterialBatchNum(remainMaterialEntity.getBatchText());
            putInDetailEntity.setPutinNum(remainMaterialEntity.getRemainNum());
            putInDetailEntity.setWareId(remainMaterialEntity.getWareId());
        } else {
            if (materialQRCodeEntity != null) { // 扫描物料
//                MaterialEntity materialEntity = new MaterialEntity();
//                materialEntity.setCode(materialQRCodeEntity.getMaterialCode());
//                materialEntity.setName(materialQRCodeEntity.getMaterialName());
                putInDetailEntity.setMaterialId(materialQRCodeEntity.getMaterial());

                putInDetailEntity.setMaterialBatchNum(materialQRCodeEntity.getMaterialBatchNo());
                putInDetailEntity.setPutinNum(materialQRCodeEntity.getNum());
                putInDetailEntity.setSpecificationNum(materialQRCodeEntity.getNum());
            }
        }

        putInDetailEntity.setPutinTime(new Date().getTime());  // 投料时间
        mPutInAgileReportDetailAdapter.addData(putInDetailEntity);
        mPutInAgileReportDetailAdapter.notifyItemRangeInserted(mPutInAgileReportDetailAdapter.getItemCount() - 1, 1);
        mPutInAgileReportDetailAdapter.notifyItemRangeChanged(mPutInAgileReportDetailAdapter.getItemCount() - 1, 1);
        contentView.smoothScrollToPosition(mPutInAgileReportDetailAdapter.getItemCount() - 1);
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

        for (PutInDetailEntity putInDetailEntity : mPutInAgileReportDetailAdapter.getList()) {
            // 尾料处理方式
            if (putInDetailEntity.getRemainId() == null && putInDetailEntity.getRemainOperate() == null) {
                if (putInDetailEntity.getRemainNum() == null || putInDetailEntity.getRemainNum().floatValue() == 0) {
                    putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_03));
                } else {
                    putInDetailEntity.setRemainOperate(new SystemCodeEntity(WomConstant.SystemCode.WOM_remainOperate_01));
                }
            }
        }

        PutinDetailDTO.DgListEntity dgListEntity = new PutinDetailDTO.DgListEntity();
//        Gson gsonBuilder = new GsonBuilder().serializeNulls().create(); // 保证不会过滤掉null字段
        dgListEntity.setDg(GsonUtil.gsonStringSerializeNulls(mPutInAgileReportDetailAdapter.getList()));
        putinDetailDTO.setDgList(dgListEntity);

        PutinDetailDTO.DgDeletedIdsEntity dgDeletedIdsEntity = new PutinDetailDTO.DgDeletedIdsEntity();
        dgDeletedIdsEntity.setDg(TextUtils.isEmpty(dgDeletedIds) ? null : dgDeletedIds);
        putinDetailDTO.setDgDeletedIds(dgDeletedIdsEntity);

        presenterRouter.create(PutInReportAPI.class).submit(false, mWaitPutinRecordEntity.getProcReportId().getId(), getController(GetPowerCodeController.class).getPowerCodeResult(), putinDetailDTO, null);

    }

    private boolean checkSubmit() {
        if (mPutInAgileReportDetailAdapter.getList() == null || mPutInAgileReportDetailAdapter.getList().size() <= 0) {
            ToastUtils.show(context, context.getResources().getString(R.string.wom_no_data_operate));
            return true;
        }
        for (PutInDetailEntity putInDetailEntity : mPutInAgileReportDetailAdapter.getList()) {
            if (putInDetailEntity.getMaterialId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mPutInAgileReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material));
                return true;
            }
            if (WomConstant.SystemCode.MATERIAL_BATCH_02.equals(putInDetailEntity.getMaterialId().getIsBatch().id) && TextUtils.isEmpty(putInDetailEntity.getMaterialBatchNum())) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mPutInAgileReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_batch));
                return true;
            }
            if (putInDetailEntity.getWareId() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mPutInAgileReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_ware));
                return true;
            }
            if (putInDetailEntity.getPutinNum() == null) {
                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mPutInAgileReportDetailAdapter.getList().indexOf(putInDetailEntity) + 1) + context.getResources().getString(R.string.wom_please_write_material_num));
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
            mPutInDetailEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
            mPutInAgileReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
        } else if (object instanceof StoreSetEntity) {
            mPutInDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
            mPutInAgileReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
        } else if (object instanceof Good) {
            Good good = (Good) selectDataEvent.getEntity();
            MaterialEntity materialEntity = new MaterialEntity();
            materialEntity.setId(good.id);
            materialEntity.setCode(good.code);
            materialEntity.setName(good.name);
            materialEntity.setIsBatch(good.isBatch);
            mPutInDetailEntity.setMaterialId(materialEntity);
            mPutInAgileReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
        } else if (object instanceof RemainMaterialEntity) {
            addMaterialReport(null, (RemainMaterialEntity) selectDataEvent.getEntity());
        }

    }

    @Override
    public void getMaterialByQRSuccess(BAP5CommonEntity entity) {
        onLoadSuccess();
        MaterialQRCodeEntity materialQRCodeEntity = GsonUtil.gsonToBean((String) entity.data,MaterialQRCodeEntity.class);
        addMaterialReport(materialQRCodeEntity,null);
    }

    @Override
    public void getMaterialByQRFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getEventInfo(EventInfo eventInfo) {
//        if (EventInfo.singleGood == eventInfo.getEventId()) {
//            MaterialEntity materialEntity = (MaterialEntity) eventInfo.getValue();
//            mPutInDetailEntity.setMaterialId(materialEntity);
//        }
//        mPutInAgileReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
//    }


}
