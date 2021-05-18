
package com.supcon.mes.module_wom_replenishmaterial.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;
import com.supcon.mes.module_wom_replenishmaterial.IntentRouter;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialTableListAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialTableListContract;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialTableListPresenter;
import com.supcon.mes.module_wom_replenishmaterial.ui.activity.ReplenishMaterialTableListActivity;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.ReplenishMaterialTableEditListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/15
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录
 */

@Presenter(value = {ReplenishMaterialTableListPresenter.class})
@Controller(value = {CommonScanController.class})
public class ReplenishMaterialTableScanFragment extends BaseRefreshRecyclerFragment<ReplenishMaterialTableEntity> implements ReplenishMaterialTableListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    ArrayMap<String, Object> queryParams = new ArrayMap<>(); // 快速查询

    private ReplenishMaterialTableEditListAdapter mReplenishMaterialTableEditListAdapter;
    private boolean mScanEam;
    private boolean mScanBucket;

    @Override
    protected IListAdapter<ReplenishMaterialTableEntity> createAdapter() {
        return mReplenishMaterialTableEditListAdapter = new ReplenishMaterialTableEditListAdapter(context);
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

        queryParams.put(Constant.BAPQuery.NAME, ((ReplenishMaterialTableListActivity) context).getSearch());

        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.wom_no_data_operate)));
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
    protected int getLayoutID() {
        return R.layout.wom_fragment_common_list;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParams.put(Constant.BAPQuery.NAME, ((ReplenishMaterialTableListActivity) context).getSearch());
            presenterRouter.create(ReplenishMaterialTableListAPI.class).listReplenishMaterialTables(pageIndex, ReplenishConstant.URL.REPLENISH_MATERIAL_SCAN_LIST_URL,false, queryParams);
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 扫描功能：红外、摄像头扫描监听事件
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)){
            if (mReplenishMaterialTableEditListAdapter.getList().size() == 0){
                ToastUtils.show(context,getResources().getString(R.string.middleware_no_data));
                return;
            }

            QrCodeEntity qrCodeEntity = MaterQRUtil.getQRCode(context,codeResultEvent.scanResult);
            if (qrCodeEntity != null) {
                switch (qrCodeEntity.getType()){
                    // 扫描设备
                    case 0:
                        mScanEam = true;
                        ((ReplenishMaterialTableListActivity) context).setSearch(qrCodeEntity.getName());
                        refreshListController.refreshBegin();
                        break;
                    // 扫描桶
                    case 1:
                        mScanBucket = true;
                        queryParams.put(Constant.BAPQuery.CODE,qrCodeEntity.getCode());
                        refreshListController.refreshBegin();
                        break;
                    case 2:
                        break;
                    default:
                }
            }
//            if (materialQRCodeEntity == null)return;

//            if (materialQRCodeEntity.isIsRequest()){
//                // TODO...
//            }else {
//                // 匹配配料记录数据
//                int count = 0;
//                for (BatchMaterialPartEntity batchMaterialPartEntity : mBatchMaterialRecordsListAdapter.getList()){
//                    if (TextUtils.isEmpty(batchMaterialPartEntity.getMaterialBatchNum())){
//                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode())){
//                            batchMaterialPartEntity.setChecked(true);
//                            mBatchMaterialRecordsListAdapter.notifyItemChanged(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            contentView.scrollToPosition(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            doAllSelect();
//                            return;
//                        }
//                    }else {
//                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode()) &&
//                                materialQRCodeEntity.getMaterialBatchNo().equals(batchMaterialPartEntity.getMaterialBatchNum())){
//                            batchMaterialPartEntity.setChecked(true);
//                            mBatchMaterialRecordsListAdapter.notifyItemChanged(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            contentView.scrollToPosition(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
//                            doAllSelect();
//                            return;
//                        }
//                    }
//
//                    count++;
//                }
//
//                if (count == mBatchMaterialRecordsListAdapter.getList().size()){
//                    ToastUtils.show(context, context.getResources().getString(R.string.wom_no_scan_material_info));
//                }
//            }
        }

    }

    /**
     * @author zhangwenshuai1 2020/8/27
     * @param
     * @return
     * @description 摄像头扫描
     *
     */
    public void scan() {
        getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName());
    }

    public void search(){
        refreshListController.refreshBegin();
    }

    @Override
    public void listReplenishMaterialTablesSuccess(CommonBAP5ListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
        if (mScanBucket && entity.data.result.size() == 1){
            ReplenishMaterialTableEntity replenishMaterialTableEntity = (ReplenishMaterialTableEntity) entity.data.result.get(0);
            replenishMaterialTableEntity.setVesselScanFlag(true);
            goReplenishScanActivity(replenishMaterialTableEntity);
        }
        if (mScanEam && entity.data.result.size() == 1){
            ReplenishMaterialTableEntity replenishMaterialTableEntity = (ReplenishMaterialTableEntity) entity.data.result.get(0);
            replenishMaterialTableEntity.setEqScanFlag(true);
            goReplenishScanActivity(replenishMaterialTableEntity);
        }
    }

    private void goReplenishScanActivity(ReplenishMaterialTableEntity replenishMaterialTableEntity) {
        mScanEam = false;
        mScanBucket = false;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReplenishConstant.IntentKey.REPLENISH_MATERIAL_TABLE,replenishMaterialTableEntity);
        IntentRouter.go(context, ReplenishConstant.Router.REPLENISH_MATERIAL_SCAN,bundle);
    }

    @Override
    public void listReplenishMaterialTablesFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, errorMsg);
        mScanEam = false;
        mScanBucket = false;
    }

}