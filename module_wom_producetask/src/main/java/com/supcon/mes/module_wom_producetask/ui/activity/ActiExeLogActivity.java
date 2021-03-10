package com.supcon.mes.module_wom_producetask.ui.activity;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_producetask.R;
import com.supcon.mes.module_wom_producetask.model.api.ActivityExeLogListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogEntity;
import com.supcon.mes.module_wom_producetask.model.bean.ActiExelogListEntity;
import com.supcon.mes.module_wom_producetask.model.bean.WaitPutinRecordEntity;
import com.supcon.mes.module_wom_producetask.model.contract.ActivityExeLogListContract;
import com.supcon.mes.module_wom_producetask.presenter.ActiExeLogListPresenter;
import com.supcon.mes.module_wom_producetask.ui.adapter.ActiExeLogAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2019/8/3
 * Email zhangwenshuai1@supcon.com
 * Desc 活动执行记录列表
 */
@Presenter(value = {ActiExeLogListPresenter.class})
@Router(value = Constant.Router.ACTIVITY_EXEREDS_LIST)
public class ActiExeLogActivity extends BaseRefreshRecyclerActivity<ActiExelogEntity> implements ActivityExeLogListContract.View {

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleLayout")
    RelativeLayout titleLayout;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("refreshFrameLayout")
    PtrFrameLayout refreshFrameLayout;

    private WaitPutinRecordEntity waitPutinRecordEntity;
    private ActiExeLogAdapter mActiExeLogAdapter;

    @Override
    protected void onInit() {
        super.onInit();

        waitPutinRecordEntity = (WaitPutinRecordEntity) getIntent().getSerializableExtra(Constant.IntentKey.WAIT_PUT_RECORD);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(this));
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),DisplayUtil.dip2px(10,context),0);
            }
        });
    }

    @Override
    protected IListAdapter<ActiExelogEntity> createAdapter() {
        return new ActiExeLogAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_actiexelog_list;
    }

    @Override
    protected void initView() {
        super.initView();
        titleText.setText(context.getResources().getString(R.string.wom_activity_exe_records));
        initEmpty();
    }

    private void initEmpty() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> onBackPressed());
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(ActivityExeLogListAPI.class).listActivityExeLog(pageIndex,waitPutinRecordEntity.getProduceBatchNum());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new RefreshEvent());
    }

    @Override
    public void listActivityExeLogSuccess(ActiExelogListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void listActivityExeLogFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }
}
