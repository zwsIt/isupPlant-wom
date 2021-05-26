package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchInstructionEntity;
import com.supcon.mes.module_wom_batchmaterial.model.bean.BatchMaterialSetEntity;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchInstructionListAdapter;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.presenter.CommonListPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;


/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 当前指令集下所有指令
 */
@Router(BmConstant.Router.BATCH_MATERIAL_INSTRUCTION_LIST)
@Presenter(value = {CommonListPresenter.class})
public class BatchInstructionListActivity extends BaseRefreshRecyclerActivity<BatchInstructionEntity> implements CommonListContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();

    private OnRefreshListener mOnRefreshListener;
    private RecyclerView.ItemDecoration mItemDecoration;
    private BatchMaterialSetEntity mBatchMaterialSetEntity;

    @Override
    protected IListAdapter<BatchInstructionEntity> createAdapter() {
        return new BatchInstructionListAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_list;
    }

    @Override
    protected void onInit() {
        super.onInit();

        EventBus.getDefault().register(this);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context,context.getResources().getString(R.string.middleware_no_data)));
        mBatchMaterialSetEntity = (BatchMaterialSetEntity) getIntent().getSerializableExtra(BmConstant.IntentKey.BATCH_MATERIAL_SET);
//        queryParams.put("processId",waitPutinRecordEntity.getTaskProcessId().getId());
//        queryParams.put("showBatch",false);
//        customCondition.put("pageNo", 1);
//        customCondition.put("pageSize", 65535);

        mItemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10,context),0,DisplayUtil.dip2px(10,context),0);
            }
        };

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(mItemDecoration);

        mOnRefreshListener = () -> presenterRouter.create(CommonListAPI.class).list(1,customCondition, queryParams,BmConstant.URL.BATCH_INSTRUCTION_LIST_URL + mBatchMaterialSetEntity.getId(),null);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.batch_instruction_execute));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());

        refreshListController.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mOnRefreshListener = null;
        mItemDecoration = null;
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data),CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result),BatchInstructionEntity.class));
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    public String getBucketCode() {
        return mBatchMaterialSetEntity.getVessel().getCode();
    }
}
