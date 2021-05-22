package com.supcon.mes.module_wom_replenishmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.listener.OnTextListener;
import com.supcon.mes.mbap.listener.OnTitleSearchExpandListener;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_wom_replenishmaterial.IntentRouter;
import com.supcon.mes.module_wom_replenishmaterial.R;
import com.supcon.mes.module_wom_replenishmaterial.constant.ReplenishConstant;
import com.supcon.mes.module_wom_replenishmaterial.model.api.ReplenishMaterialNotifyListAPI;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialNotifyEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.bean.ReplenishMaterialTableEntity;
import com.supcon.mes.module_wom_replenishmaterial.model.contract.ReplenishMaterialNotifyListContract;
import com.supcon.mes.module_wom_replenishmaterial.model.dto.ReplenishMaterialNotifyDTO;
import com.supcon.mes.module_wom_replenishmaterial.presenter.ReplenishMaterialNotifyListPresenter;
import com.supcon.mes.module_wom_replenishmaterial.ui.adapter.ReplenishMaterialNotifyAdapter;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2021/5/11
 * Email zhangwenshuai1@supcon.com
 * Desc 补料通知列表
 */
@Presenter(value = {ReplenishMaterialNotifyListPresenter.class})
@Router(value = Constant.AppCode.WOM_ReplenishMaterial_Notify)
public class ReplenishMaterialNotifyListActivity extends BaseRefreshRecyclerActivity<ReplenishMaterialNotifyEntity> implements ReplenishMaterialNotifyListContract.View {

    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("submitBtn")
    Button submitBtn;
    private ArrayMap<String, Object> queryMap = new ArrayMap<>();
    private ReplenishMaterialNotifyAdapter mReplenishMaterialNotifyAdapter;

    @Override
    protected void onInit() {
        super.onInit();

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        contentView.setLayoutManager(new LinearLayoutManager(this));
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);

        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(10, context), 0);
            }
        });
    }

    @Override
    protected IListAdapter<ReplenishMaterialNotifyEntity> createAdapter() {
        mReplenishMaterialNotifyAdapter = new ReplenishMaterialNotifyAdapter(context);
        return mReplenishMaterialNotifyAdapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_search_list;
    }

    @Override
    protected void initView() {
        super.initView();
        searchTitleBar.title().setText(context.getResources().getString(R.string.replenishNotify));
        searchTitleBar.searchView().setHint(getString(R.string.please_input_eam_name));
        searchTitleBar.disableRightBtn();
        initEmpty();
    }

    private void initEmpty() {
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, ""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> onBackPressed());
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                queryMap.put(Constant.BAPQuery.NOTICE_STATE, ReplenishConstant.SystemCode.NOTIFY_INSTRUCTION);
                presenterRouter.create(ReplenishMaterialNotifyListAPI.class).listReplenishMaterialNotifies(pageIndex, queryMap);
            }
        });
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (!isExpand) {
                searchTitleBar.searchView().setInputTextColor(R.color.black);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText())
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    queryMap.put(Constant.BAPQuery.NAME,charSequence.toString());
                    refreshListController.refreshBegin();
                });
        mReplenishMaterialNotifyAdapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                ReplenishMaterialNotifyEntity replenishMaterialNotifyEntity = (ReplenishMaterialNotifyEntity) obj;
                if ("startTv".equals(childView.getTag().toString())) {
                    showCreateDialog(replenishMaterialNotifyEntity);
                }
            }
        });
    }

    /**
     * @param
     * @return
     * @author zhangwenshuai1 2021/5/12
     * @description 创建配料单
     */
    private void showCreateDialog(ReplenishMaterialNotifyEntity replenishMaterialNotifyEntity) {
        CustomDialog customDialog = new CustomDialog(context).layout(R.layout.wom_dialog_create_replenish, DisplayUtil.getScreenWidth(context) * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        CustomTextView material = customDialog.getDialog().findViewById(R.id.material);
        CustomTextView eamPoint = customDialog.getDialog().findViewById(R.id.eamPoint);
        CustomEditText planNum = customDialog.getDialog().findViewById(R.id.planNum);
        planNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        material.setContent(replenishMaterialNotifyEntity.getMaterial().name);
        eamPoint.setContent(replenishMaterialNotifyEntity.getEquipment().getCode());
        planNum.setContent(replenishMaterialNotifyEntity.getPlanNumber().toString());

        customDialog.bindClickListener(R.id.cancelTv, null, true)
                .bindClickListener(R.id.confirmTv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(planNum.getContent())) {
                            ToastUtils.show(context, getString(R.string.wom_please_input_plan_num));
                            return;
                        }
                        customDialog.dismiss();
                        onLoading(getString(R.string.wom_dealing));

                        List<ReplenishMaterialNotifyDTO> dtoList = new ArrayList<>();
                        ReplenishMaterialNotifyDTO dtoElement = new ReplenishMaterialNotifyDTO();
                        dtoElement.setFmnNotice(replenishMaterialNotifyEntity);
                        dtoElement.setPlanNumber(new BigDecimal(planNum.getContent()));
                        dtoList.add(dtoElement);

                        presenterRouter.create(ReplenishMaterialNotifyListAPI.class).submit(dtoList);
                    }
                }, false)
                .show();
    }

    @Override
    public void listReplenishMaterialNotifiesSuccess(CommonBAP5ListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void listReplenishMaterialNotifiesFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void submitSuccess(BAP5CommonEntity entity) {
        onLoadSuccessAndExit(getString(R.string.wom_dealt_success), new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                IntentRouter.go(context,Constant.AppCode.WOM_ReplenishMaterial_Table);
            }
        });
    }

    @Override
    public void submitFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }
}
