package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.common.view.view.picker.SinglePicker;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.controller.BatchMaterialRecordsSubmitController;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialRecordsRejectListAdapter;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录拒签list
 */
@Router(Constant.Router.BATCH_MATERIAL_REJECT_LIST)
@Controller(value = {BatchMaterialRecordsSubmitController.class})
public class BatchMaterialRecordRejectListActivity extends BaseRefreshRecyclerActivity<BatchMaterialPartEntity> implements OnAPIResultListener {
    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("submitBtn")
    Button submitBtn;

    private BatchMaterialRecordsRejectListAdapter mBatchMaterialRecordsRejectListAdapter;
    private List<SystemCodeEntity> mSystemCodeEntities;
    private List<String> rejectReason = new ArrayList<>();

    @Override
    protected IListAdapter<BatchMaterialPartEntity> createAdapter() {
        mBatchMaterialRecordsRejectListAdapter = new BatchMaterialRecordsRejectListAdapter(context);
        return mBatchMaterialRecordsRejectListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),0);
            }
        });
        getController(BatchMaterialRecordsSubmitController.class).setOnAPIResultListener(this);  // 注册回调监听
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.wom_material_reject));
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setText(context.getResources().getString(R.string.wom_reject_sign));
    }

    @Override
    protected void initData() {
        super.initData();
        mSystemCodeEntities = SupPlantApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.REJECT_REASON)).build().listLazy();
        for (SystemCodeEntity systemCodeEntity : mSystemCodeEntities){
            rejectReason.add(systemCodeEntity.value);
        }
        refreshListController.refreshComplete(GsonUtil.jsonToList(getIntent().getStringExtra(Constant.IntentKey.MATERIAL_RECORDS_LIST),BatchMaterialPartEntity.class));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());
        RxView.clicks(submitBtn).throttleFirst(200,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        for (BatchMaterialPartEntity entity : mBatchMaterialRecordsRejectListAdapter.getList()){
                            if (entity.getRejectReason() == null){
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchMaterialRecordsRejectListAdapter.getList().indexOf(entity) + 1) + context.getResources().getString(R.string.wom_please_write) + getString(R.string.wom_reject_reason));
                                return;
                            }
                            SystemCodeEntity systemCodeEntity = new SystemCodeEntity();
                            systemCodeEntity.id = BmConstant.SystemCode.RECEIVE_STATE_REJECT;
                            entity.setReceiveState(systemCodeEntity);
                        }
                        onLoading(getString(R.string.wom_dealing));
                        BatchMaterialRecordsSignSubmitDTO dto = new BatchMaterialRecordsSignSubmitDTO();
                        dto.setDetails(GsonUtil.gsonString(mBatchMaterialRecordsRejectListAdapter.getList()));
                        getController(BatchMaterialRecordsSubmitController.class).submit(null,dto,true);
                    }
                });
        mBatchMaterialRecordsRejectListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            BatchMaterialPartEntity batchMaterialPartEntity = (BatchMaterialPartEntity) obj;
            if ("rejectReason".equals(childView.getTag().toString())){
                if (mSystemCodeEntities == null || mSystemCodeEntities.size() <= 0){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_null_systemcode));
                    return;
                }
                new SinglePickController<String>(this)
                        .list(rejectReason)
                        .listener(new SinglePicker.OnItemPickListener() {
                            @Override
                            public void onItemPicked(int index, Object item) {
                                batchMaterialPartEntity.setRejectReason(mSystemCodeEntities.get(index));
                                mBatchMaterialRecordsRejectListAdapter.notifyItemRangeChanged(position,1);
                            }
                        })
                        .show();
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFail(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onSuccess(Object result) {
        onLoadSuccessAndExit(getString(R.string.wom_dealt_success), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                EventBus.getDefault().post(new RefreshEvent());
                finish();
            }
        });
    }
}
