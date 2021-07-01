package com.supcon.mes.module_wom_batchmaterial.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.QrCodeEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_batchmaterial.IntentRouter;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialSetListAPI;
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchSetBindBucketAPI;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialSetListContract;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchSetBindBucketContract;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialSetListPresenter;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchSetBindBucketPresenter;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialInstructionSetListActivity;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialSetListAdapter;
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
@Presenter(value = {BatchMaterialSetListPresenter.class})
@Controller(value = {CommonScanController.class})
public class BatchMaterialNextAreaSetFragment extends BaseRefreshRecyclerFragment<BatchMaterialSetEntity> implements BatchMaterialSetListContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    // 配料查询
    ArrayMap<String, Object> queryParams = new ArrayMap<>();

    private BatchMaterialSetListAdapter mBatchMaterialSetListAdapter;
    private BatchMaterialSetEntity batchMaterialSetEntity;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Override
    protected IListAdapter<BatchMaterialSetEntity> createAdapter() {
        mBatchMaterialSetListAdapter = new BatchMaterialSetListAdapter(context);
        return mBatchMaterialSetListAdapter;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
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

    }


    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_common_list;
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
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            queryParams.put(Constant.BAPQuery.CODE, ((BatchMaterialInstructionSetListActivity) context).getSearch());
            // 配料状态
            queryParams.put(Constant.BAPQuery.FM_TASK, BmConstant.SystemCode.TASK_NEXT_AREA);
            presenterRouter.create(BatchMaterialSetListAPI.class).listBatchMaterialSets(pageIndex, null, false, queryParams);
        });

        mBatchMaterialSetListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            String tag = childView.getTag().toString();
            batchMaterialSetEntity = (BatchMaterialSetEntity) obj;
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
        } else {
            isUIVisible = false;
        }
    }

    public void search() {
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
                        break;
                    case 2:
                        break;
                    default:
                }
            }
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
}
