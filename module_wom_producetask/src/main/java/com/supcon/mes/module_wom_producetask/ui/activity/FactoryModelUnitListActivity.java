package com.supcon.mes.module_wom_producetask.ui.activity;

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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.wom.FactoryModelEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.api.FactoryModelAPI;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.FactoryModelContract;
import com.supcon.mes.module_wom_producetask.presenter.FactoryModelPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.FactoryModelUnitListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/3/30
 * Email zhangwenshuai1@supcon.com
 * Desc 工厂架构：工作单元list
 */
@Router(Constant.Router.WOM_FACTORY_LIST)
@Presenter(value = {FactoryModelPresenter.class})
public class FactoryModelUnitListActivity extends BaseRefreshRecyclerActivity<FactoryModelEntity> implements FactoryModelContract.View {
//    @BindByTag("searchTitleBar")
//    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;
    Map<String, Object> queryParams = new HashMap<>();
    Map<String, Object> customCondition = new HashMap<>();
    private FactoryModelUnitListAdapter mFactoryModelUnitListAdapter;

    @Override
    protected IListAdapter<FactoryModelEntity> createAdapter() {
        mFactoryModelUnitListAdapter = new FactoryModelUnitListAdapter(context);
        return mFactoryModelUnitListAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_factory_model_unit;
    }

    @Override
    protected void onInit() {
        super.onInit();

        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, context.getResources().getString(R.string.middleware_no_data)));
        WaitPutinRecordEntity waitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        customCondition.put("processId", waitPutinRecordEntity.getTaskProcessId().getFormulaProcessId() == null ? -1 : waitPutinRecordEntity.getTaskProcessId().getFormulaProcessId().getId());
        customCondition.put("lineId", waitPutinRecordEntity.getLineId() == null ? -1 : waitPutinRecordEntity.getLineId().getId());

        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),0);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText("工作单元");
//        searchTitleBar.searchView().setHint("请输入工作单元名称");
//        searchTitleBar.disableRightBtn();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> finish());

//        refreshListController.setOnRefreshPageListener(pageIndex ->
//                presenterRouter.create(FactoryModelAPI.class).listFactoryModelUnit(pageIndex, customCondition, queryParams));
        refreshListController.setOnRefreshListener(()->{
            presenterRouter.create(FactoryModelAPI.class).listFactoryModelUnit(1, customCondition, queryParams);
        });
//        RxTextView.textChanges(searchTitleBar.editText())
//                .skipInitialValue()
//                .debounce(300, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(charSequence -> {
//                    queryParams.put(Constant.BAPQuery.NAME, charSequence.toString().trim());
//                    refreshListController.refreshBegin();
//                });

        mFactoryModelUnitListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            SelectDataEvent<FactoryModelEntity> dataEvent = new SelectDataEvent<>((FactoryModelEntity) obj,"");
            EventBus.getDefault().post(dataEvent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void listFactoryModelUnitSuccess(BAP5CommonEntity entity) {
        refreshListController.refreshComplete(((CommonBAPListEntity) entity.data).result);
    }

    @Override
    public void listFactoryModelUnitFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
