package com.supcon.mes.module_wom_batchmaterial.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomHorizontalSearchTitleBar;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntityDao;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.model.listener.OnAPIResultListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_wom_batchmaterial.R;
import com.supcon.mes.module_wom_batchmaterial.constant.BmConstant;
import com.supcon.mes.module_wom_batchmaterial.controller.BatchMaterialRecordsSubmitController;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_batchmaterial.presenter.batchMaterialRecordsPresenter;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialRecordsAbandonListAdapter;
import com.supcon.mes.module_wom_producetask.model.api.CommonListAPI;
import com.supcon.mes.module_wom_producetask.model.bean.BatchMaterialPartEntity;
import com.supcon.mes.module_wom_producetask.model.contract.CommonListContract;
import com.supcon.mes.module_wom_producetask.util.MaterQRUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/15
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录退废list
 */
@Router(value = Constant.Router.BATCH_MATERIAL_RECALL_ABANDON_LIST)
@Presenter(value = {batchMaterialRecordsPresenter.class})
@Controller(value = {BatchMaterialRecordsSubmitController.class, CommonScanController.class})
public class BatchMaterialRecordsRecallAbandonListActivity extends BaseRefreshRecyclerActivity<BatchMaterialPartEntity> implements CommonListContract.View, OnAPIResultListener {

    Map<String, Object> queryParams = new HashMap<>(); // 配料记录查询
    @BindByTag("searchTitleBar")
    CustomHorizontalSearchTitleBar searchTitleBar;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("allChooseCheckBox")
    CheckBox allChooseCheckBox;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("submitBtnLl")
    LinearLayout submitBtnLl;

    private List<BatchMaterialPartEntity> chooseList = new ArrayList<>();

    private BatchMaterialRecordsAbandonListAdapter mBatchMaterialRecordsAbandonListAdapter;
    private List<SystemCodeEntity> mSystemCodeEntities = new ArrayList<>(2);
    private List<String> abandonReason = new ArrayList<>();

    @Override
    protected IListAdapter<BatchMaterialPartEntity> createAdapter() {
        mBatchMaterialRecordsAbandonListAdapter = new BatchMaterialRecordsAbandonListAdapter(context);
        return mBatchMaterialRecordsAbandonListAdapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
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

        getController(BatchMaterialRecordsSubmitController.class).setOnAPIResultListener(this);  // 注册回调监听
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_ac_search_choose_list;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        searchTitleBar.setTitleText(getString(R.string.wom_batch_material)+getString(R.string.wom_recall_back));
//        searchTitleBar.disableRightBtn();
        searchTitleBar.rightBtn().setImageResource(R.drawable.ic_scan);
        searchTitleBar.editText().setHint(context.getResources().getString(R.string.wom_input_produce_batch_num));
    }
    @Override
    protected void initData() {
        super.initData();
        List<SystemCodeEntity> systemCodeEntityList = SupPlantApplication.dao().getSystemCodeEntityDao().queryBuilder().where(SystemCodeEntityDao.Properties.EntityCode.eq(Constant.SystemCode.RETIREMENT_STATE)).build().listLazy();
        for (SystemCodeEntity systemCodeEntity : systemCodeEntityList){
            if (BmConstant.SystemCode.RETRAIL_STATE_03.equals(systemCodeEntity.id) || BmConstant.SystemCode.RETRAIL_STATE_04.equals(systemCodeEntity.id)){
                abandonReason.add(systemCodeEntity.value);
                mSystemCodeEntities.add(systemCodeEntity);
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        searchTitleBar.leftBtn().setOnClickListener(v -> finish());
        searchTitleBar.rightBtn().setOnClickListener(view -> getController(CommonScanController.class).openCameraScan(context.getClass().getSimpleName()));
        searchTitleBar.setOnExpandListener(isExpand -> {
            if (isExpand) {
//                    searchTitleBar.searchView().setInputTextColor(R.color.black);
            } else {
                searchTitleBar.searchView().setInputTextColor(R.color.black);
            }
        });
        RxTextView.textChanges(searchTitleBar.editText()).skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> search(charSequence.toString().trim()));
        refreshListController.setOnRefreshPageListener(pageIndex -> {
            if (pageIndex == 1){
                allChooseCheckBox.setChecked(false);
            }
            presenterRouter.create(CommonListAPI.class).list(pageIndex, null, queryParams, BmConstant.URL.BATCH_MATERIAL_RECORDS_ABANDON_LIST_URL, "batMaterilPart");
        });

        mBatchMaterialRecordsAbandonListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            BatchMaterialPartEntity batchMaterialPartEntity = (BatchMaterialPartEntity) obj;
            String tag = childView.getTag().toString();
            if ("checkBox".equals(tag)) {
                batchMaterialPartEntity.setChecked(!batchMaterialPartEntity.isChecked());
                // do全选
                doAllSelect();
            }else if ("rejectReason".equals(tag)){
                if (mSystemCodeEntities == null || mSystemCodeEntities.size() <= 0){
                    ToastUtils.show(context, getString(R.string.wom_null_list));
                    return;
                }
                new SinglePickController<String>(this)
                        .list(abandonReason)
                        .listener((index, item) -> {
                            batchMaterialPartEntity.setRetirementState(mSystemCodeEntities.get(index));
                            mBatchMaterialRecordsAbandonListAdapter.notifyItemRangeChanged(position,1);
                        })
                        .show();
            }

        });
        allChooseCheckBox.setOnClickListener(v -> {
            if (mBatchMaterialRecordsAbandonListAdapter.getList() != null) {
                for (BatchMaterialPartEntity entity : mBatchMaterialRecordsAbandonListAdapter.getList()) {
                    entity.setChecked(!entity.isChecked());
                }
                mBatchMaterialRecordsAbandonListAdapter.notifyDataSetChanged();
            }
        });
        RxView.clicks(submitBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(o -> doAbandonSubmit());

    }

    private void doAllSelect() {
        int count = 0;
        for (BatchMaterialPartEntity entity : mBatchMaterialRecordsAbandonListAdapter.getList()) {
            if (entity.isChecked()) {
                count++;
            }
        }
        if (count == mBatchMaterialRecordsAbandonListAdapter.getList().size()){
            allChooseCheckBox.setChecked(true);
        }else {
            allChooseCheckBox.setChecked(false);
        }
    }

    /**
     * @author zhangwenshuai1 2020/4/20
     * @param
     * @return
     * @description 配料记录退废
     *
     */
    private void doAbandonSubmit() {
        if (checkSubmit()){
            return;
        }
        CustomDialog customDialog = new CustomDialog(context).layout(R.layout.wom_dialog_confirm,(DisplayUtil.getScreenWidth(context))*4/5, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        customDialog.bindView(R.id.tipContentTv,getString(R.string.wom_confirm_batch_material_operate)+getString(R.string.wom_recall_back)+getString(R.string.wom_middle_right_brackets))
                .bindClickListener(R.id.cancelTv,null,true)
                .bindClickListener(R.id.confirmTv, v -> {
                    onLoading(getString(R.string.wom_dealing));
                    BatchMaterialRecordsSignSubmitDTO dto = new BatchMaterialRecordsSignSubmitDTO();
                    dto.setDetails(GsonUtil.gsonString(chooseList));
                    getController(BatchMaterialRecordsSubmitController.class).submit(null,dto,false);
                }, true)
                .show();
    }

    /**
     * 提交检查
     * @return
     */
    private boolean checkSubmit() {
        List<BatchMaterialPartEntity> list = mBatchMaterialRecordsAbandonListAdapter.getList();
        if (list == null || list.size() <= 0){
            ToastUtils.show(context,getString(R.string.wom_no_data_operate));
            return true;
        }
        chooseList.clear();
        for (BatchMaterialPartEntity entity : list){
            if (entity.isChecked()){
                if (entity.getRetirementState() == null){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_di) + (mBatchMaterialRecordsAbandonListAdapter.getList().indexOf(entity) + 1) + context.getResources().getString(R.string.wom_please_write) + getString(R.string.wom_abandon_status));
                    return true;
                }
                chooseList.add(entity);
            }
        }
        if (chooseList.size() <= 0){
            ToastUtils.show(context, context.getResources().getString(R.string.wom_choose_batch_material_records));
            return true;
        }
        return false;
    }

    @Override
    public void listSuccess(BAP5CommonEntity entity) {
        CommonBAPListEntity commonBAPListEntity = GsonUtil.gsonToBean(GsonUtil.gsonString(entity.data), CommonBAPListEntity.class);
        refreshListController.refreshComplete(GsonUtil.jsonToList(GsonUtil.gsonString((Object) commonBAPListEntity.result), BatchMaterialPartEntity.class));
        if (mBatchMaterialRecordsAbandonListAdapter.getList().size() <= 0){
            submitBtnLl.setVisibility(View.GONE);
        }else {
            submitBtnLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        submitBtnLl.setVisibility(View.GONE);
    }
    public void search(String searchContent) {
        queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM,searchContent);
        refreshListController.refreshBegin();
    }

    public boolean getAllChosen(){
        return allChooseCheckBox.isChecked();
    }

    @Override
    public void onFail(String errorMsg) {
        onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
    }

    @Override
    public void onSuccess(Object result) {
        onLoadSuccess(getString(R.string.wom_dealt_success));
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent refreshEvent){
        refreshListController.refreshBegin();
    }

    /**
     * 扫描功能：红外、摄像头扫描监听事件
     * @param codeResultEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReceiver(CodeResultEvent codeResultEvent) {
        if (context.getClass().getSimpleName().equals(codeResultEvent.scanTag)){
            if (mBatchMaterialRecordsAbandonListAdapter.getList().size() == 0){
                ToastUtils.show(context,getResources().getString(R.string.middleware_no_data));
                return;
            }

            MaterialQRCodeEntity materialQRCodeEntity = MaterQRUtil.materialQRCode(context,codeResultEvent.scanResult);
            if (materialQRCodeEntity == null)return;

            if (materialQRCodeEntity.isIsRequest()){
                // TODO...
            }else {
                // 匹配配料记录数据
                int count = 0;
                for (BatchMaterialPartEntity batchMaterialPartEntity : mBatchMaterialRecordsAbandonListAdapter.getList()){
                    if (TextUtils.isEmpty(batchMaterialPartEntity.getMaterialBatchNum())){
                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode())){
                            batchMaterialPartEntity.setChecked(true);
                            mBatchMaterialRecordsAbandonListAdapter.notifyItemChanged(mBatchMaterialRecordsAbandonListAdapter.getList().indexOf(batchMaterialPartEntity));
                            doAllSelect();
                            return;
                        }
                    }else {
                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode()) &&
                                materialQRCodeEntity.getMaterialBatchNo().equals(batchMaterialPartEntity.getMaterialBatchNum())){
                            batchMaterialPartEntity.setChecked(true);
                            mBatchMaterialRecordsAbandonListAdapter.notifyItemChanged(mBatchMaterialRecordsAbandonListAdapter.getList().indexOf(batchMaterialPartEntity));
                            doAllSelect();
                            return;
                        }
                    }

                    count++;
                }

                if (count == mBatchMaterialRecordsAbandonListAdapter.getList().size()){
                    ToastUtils.show(context, context.getResources().getString(R.string.wom_no_scan_material_info));
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
