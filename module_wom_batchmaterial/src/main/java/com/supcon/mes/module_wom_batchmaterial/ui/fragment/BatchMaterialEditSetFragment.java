package com.supcon.mes.module_wom_batchmaterial.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.controller.BatchMaterialRecordsSubmitController;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialSetListAPI;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchSetBindBucketAPI;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialSetListContract;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchSetBindBucketContract;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialSetListPresenter;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchSetBindBucketPresenter;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialInstructionSetListActivity;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialSetListAdapter;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/19
 * Email zhangwenshuai1@supcon.com
 * Desc 配料指令集list
 */
@Presenter(value = {BatchMaterialSetListPresenter.class, BatchSetBindBucketPresenter.class})
@Controller(value = {CommonScanController.class})
public class BatchMaterialEditSetFragment extends BaseRefreshRecyclerFragment<BatchMaterialSetEntity> implements BatchMaterialSetListContract.View, BatchSetBindBucketContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("waitStateRadioGroup")
    RadioGroup waitStateRadioGroup;

    // 配料查询
    ArrayMap<String, Object> queryParams = new ArrayMap<>();

    // 是否自动配料模式
    ArrayMap<String, Object> autoBatchParams = new ArrayMap<>();

    private BatchMaterialSetListAdapter mBatchMaterialSetListAdapter;
    private CustomEditText mBucketCode;
    private BatchMaterialSetEntity batchMaterialSetEntity;

    @Override
    protected IListAdapter<BatchMaterialSetEntity> createAdapter() {
        mBatchMaterialSetListAdapter = new BatchMaterialSetListAdapter(context);
        return mBatchMaterialSetListAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.wom_no_data_operate)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(1, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context));
            }
        });

        autoBatchParams.put(Constant.BAPQuery.AUTO_BURDEN, false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.batch_fragment_batch_set_list;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        waitStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.autoRBtn) {
                autoBatchParams.put(Constant.BAPQuery.AUTO_BURDEN, true);
            } else if (checkedId == R.id.manualRBtn) {
                autoBatchParams.put(Constant.BAPQuery.AUTO_BURDEN, false);
            } else {
                autoBatchParams.put(Constant.BAPQuery.AUTO_BURDEN, "");
            }
            refreshListController.refreshBegin();
        });
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParams.put(Constant.BAPQuery.CODE, ((BatchMaterialInstructionSetListActivity) context).getSearch());
            // 配料状态
            queryParams.put(Constant.BAPQuery.FM_TASK, BmConstant.SystemCode.TASK_BATCH);
            queryParams.put(Constant.BAPQuery.AUTO_BURDEN,autoBatchParams.get(Constant.BAPQuery.AUTO_BURDEN));
            presenterRouter.create(BatchMaterialSetListAPI.class).listBatchMaterialSets(pageIndex, null, true, queryParams);
        });

        mBatchMaterialSetListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            String tag = childView.getTag().toString();
            batchMaterialSetEntity = (BatchMaterialSetEntity) obj;
            if ("bindBucket".equals(tag)) {
                showBindDialog();
            }
        });

    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2020/4/20
     * @description 绑桶操作
     */
    private void showBindDialog() {
        CustomDialog customDialog = new CustomDialog(context).layout(R.layout.batch_dialog_bind_bucket, (DisplayUtil.getScreenWidth(context)) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBucketCode = customDialog.getDialog().findViewById(R.id.bucketCode);
        customDialog.bindClickListener(R.id.customEditIcon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
            }
        }, false)
                .bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, v -> {

                    if (TextUtils.isEmpty(mBucketCode.getContent())) {
                        ToastUtils.show(context, getString(R.string.batch_please_input_bucket_code));
                        return;
                    }
                    customDialog.getDialog().dismiss();
                    onLoading(getString(R.string.wom_dealing));
                    Map<String, Object> paramsMap = new ArrayMap<>();
                    paramsMap.put("bmSetId", batchMaterialSetEntity.getId());
                    paramsMap.put("vesselCode", mBucketCode.getContent().trim());
                    presenterRouter.create(BatchSetBindBucketAPI.class).submit(paramsMap);
                }, false)
                .show();
    }

    public void search(String searchContent) {
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent) {
        refreshListController.refreshBegin();
    }

    /**
     * 扫描功能：红外、摄像头扫描监听事件
     *
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag) && ((BatchMaterialInstructionSetListActivity) context).getTabPos() == 0) {
            QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context, codeResultEvent.scanResult);
            if (qrCodeEntity != null) {
                switch (qrCodeEntity.getType()) {
                    // 扫描设备
                    case 0:
//                        mScanEam = true;
//                        ((BatchMaterialInstructionSetListActivity) context).setSearch(qrCodeEntity.getName());
//                        refreshListController.refreshBegin();
                        break;
                    // 扫描桶
                    case 1:
//                        mScanBucket = true;
                        if (mBucketCode != null) {
                            mBucketCode.setContent(qrCodeEntity.getCode());
                        }
                        break;
                    case 2:
                        break;
                    default:
                }
            }
//            if (mBatchMaterialSetListAdapter.getList().size() == 0){
//                ToastUtils.show(context,getResources().getString(R.string.middleware_no_data));
//                return;
//            }
//
//            MaterialQRCodeEntity materialQRCodeEntity = MaterQRUtil.materialQRCode(context,codeResultEvent.scanResult);
//            if (materialQRCodeEntity == null)return;
//
//            if (materialQRCodeEntity.isIsRequest()){
//                // TODO...
//            }else {
//                // 匹配配料记录数据
//                int count = 0;
//                for (BatchMaterialPartEntity batchMaterialPartEntity : mBatchMaterialSetListAdapter.getList()){
//                    if (TextUtils.isEmpty(batchMaterialPartEntity.getMaterialBatchNum())){
//                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode())){
//                            batchMaterialPartEntity.setChecked(true);
//                            mBatchMaterialSetListAdapter.notifyItemChanged(mBatchMaterialSetListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            contentView.scrollToPosition(mBatchMaterialSetListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            doAllSelect();
//                            return;
//                        }
//                    }else {
//                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode()) &&
//                                materialQRCodeEntity.getMaterialBatchNo().equals(batchMaterialPartEntity.getMaterialBatchNum())){
//                            batchMaterialPartEntity.setChecked(true);
//                            mBatchMaterialSetListAdapter.notifyItemChanged(mBatchMaterialSetListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            contentView.scrollToPosition(mBatchMaterialSetListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            doAllSelect();
//                            return;
//                        }
//                    }
//
//                    count++;
//                }
//
//                if (count == mBatchMaterialSetListAdapter.getList().size()){
//                    ToastUtils.show(context, context.getResources().getString(R.string.wom_no_scan_material_info));
//                }
//            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void listBatchMaterialSetsSuccess(CommonBAP5ListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void listBatchMaterialSetsFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        onLoadSuccess(getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
        // 跳转配料指令
        Bundle bundle = new Bundle();
        bundle.putSerializable(BmConstant.IntentKey.BATCH_MATERIAL_SET, batchMaterialSetEntity);
        IntentRouter.go(context, BmConstant.Router.BATCH_MATERIAL_INSTRUCTION_LIST, bundle);
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(errorMsg);
        ToastUtils.show(context, errorMsg);
    }
}
