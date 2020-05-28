package com.supcon.mes.module_wom_producetask.controller;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.model.bean.wom.WarehouseEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.module_wom_producetask.IntentRouter;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.bean.OutputDetailEntity;
import com.supcon.mes.module_wom_producetask.ui.activity.ProduceTaskEndReportActivity;
import com.supcon.mes.module_wom_producetask.ui.adapter.ProduceTaskEndReportDetailAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/1
 * Email zhangwenshuai1@supcon.com
 * Desc 人工投料报工明细Controller
 */
@Deprecated
public class PutInReportDetailController extends BaseViewController {
    @BindByTag("customListWidgetIc")
    ImageView customListWidgetIc;
    @BindByTag("customListWidgetName")
    TextView customListWidgetName;
    @BindByTag("customListWidgetEdit")
    ImageView customListWidgetEdit;
    @BindByTag("customListWidgetAdd")
    ImageView customListWidgetAdd;
    @BindByTag("contentView")
    RecyclerView contentView;

    private ProduceTaskEndReportDetailAdapter mProduceTaskEndReportDetailAdapter;
    private OutputDetailEntity mOutputDetailEntity;
    private int mCurrentPosition;

    public PutInReportDetailController(View rootView) {
        super(rootView);
    }

    public ProduceTaskEndReportDetailAdapter getProduceTaskEndReportDetailAdapter() {
        return mProduceTaskEndReportDetailAdapter;
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(0,0,0,DisplayUtil.dip2px(10, context));
            }
        });
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
        mProduceTaskEndReportDetailAdapter = new ProduceTaskEndReportDetailAdapter(context);
        contentView.setAdapter(mProduceTaskEndReportDetailAdapter);
    }

    @Override
    public void initView() {
        super.initView();
        customListWidgetName.setText(context.getResources().getString(R.string.wom_produce_task_report_detail));
        customListWidgetEdit.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        customListWidgetAdd.setOnClickListener(v -> {
            OutputDetailEntity outputDetailEntity = new OutputDetailEntity();
//            outputDetailEntity.setMaterialBatchNum(((ProduceTaskEndReportActivity) context).getWaitPutinRecordEntity().getProduceBatchNum()); // 生产批默认入库批号
//            outputDetailEntity.setOutputNum(((ProduceTaskEndReportActivity) context).getWaitPutinRecordEntity().getPlanNum());  // 默认入库数量为计划数量
            if (mProduceTaskEndReportDetailAdapter.getList() == null) {
                mProduceTaskEndReportDetailAdapter.addData(outputDetailEntity);
            } else {
                mProduceTaskEndReportDetailAdapter.getList().add(outputDetailEntity);
            }
            mProduceTaskEndReportDetailAdapter.notifyItemRangeInserted(mProduceTaskEndReportDetailAdapter.getList().size() - 1, 1);
            mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mProduceTaskEndReportDetailAdapter.getList().size() - 1, 1);
        });

        mProduceTaskEndReportDetailAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
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
                    bundle.putSerializable(Constant.IntentKey.OUTPUT_DETAIL, mOutputDetailEntity);
                    IntentRouter.go(context, Constant.Router.STORE_SET_LIST_REF, bundle);
                    break;
                default:
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventPost(SelectDataEvent selectDataEvent) {
        if (selectDataEvent.getEntity() instanceof WarehouseEntity) {
            mOutputDetailEntity.setWareId((WarehouseEntity) selectDataEvent.getEntity());
        } else if (selectDataEvent.getEntity() instanceof StoreSetEntity) {
            mOutputDetailEntity.setStoreId((StoreSetEntity) selectDataEvent.getEntity());
        }
        mProduceTaskEndReportDetailAdapter.notifyItemRangeChanged(mCurrentPosition, 1);
    }


    public boolean checkSubmit() {
        if (mProduceTaskEndReportDetailAdapter.getList() == null || mProduceTaskEndReportDetailAdapter.getList().size() <= 0) {
            ToastUtils.show(context, "请添加报工明细");
            return true;
        }
        List<OutputDetailEntity> list = mProduceTaskEndReportDetailAdapter.getList();
        for (OutputDetailEntity entity : list) {
            if (TextUtils.isEmpty(entity.getMaterialBatchNum())) {
                ToastUtils.show(context, "第【" + (list.indexOf(entity) + 1) + "】项请填写入库批号");
                return true;
            }
            if (entity.getWareId() == null) {
                ToastUtils.show(context, "第【" + (list.indexOf(entity) + 1) + "】项请选择仓库");
                return true;
            }
            if (entity.getStoreId() == null) {
                ToastUtils.show(context, "第【" + (list.indexOf(entity) + 1) + "】项请填写数量");
                return true;
            }
        }
        return false;
    }

    public List<OutputDetailEntity> getReportDetailList() {
        return mProduceTaskEndReportDetailAdapter.getList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
