package com.supcon.mes.module_wom_batchmaterial.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.bean.MaterialQRCodeEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.SystemCodeEntity;
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
import com.supcon.mes.module_wom_batchmaterial.model.api.BatchMaterialRecordsSubmitAPI;
import com.supcon.mes.module_wom_batchmaterial.model.contract.BatchMaterialRecordsSubmitContract;
import com.supcon.mes.module_wom_batchmaterial.model.dto.BatchMaterialRecordsSignSubmitDTO;
import com.supcon.mes.module_wom_batchmaterial.presenter.BatchMaterialRecordsSubmitPresenter;
import com.supcon.mes.module_wom_batchmaterial.presenter.batchMaterialRecordsPresenter;
import com.supcon.mes.module_wom_batchmaterial.ui.activity.BatchMaterialListActivity;
import com.supcon.mes.module_wom_batchmaterial.ui.adapter.BatchMaterialRecordsListAdapter;
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

import io.reactivex.functions.Consumer;

/**
 * ClassName
 * Created by zhangwenshuai1 on 2020/4/15
 * Email zhangwenshuai1@supcon.com
 * Desc 配料记录
 */
@Presenter(value = {batchMaterialRecordsPresenter.class})
@Controller(value = {BatchMaterialRecordsSubmitController.class, CommonScanController.class})
public class BatchMaterialRecordsFragment extends BaseRefreshRecyclerFragment<BatchMaterialPartEntity> implements CommonListContract.View, OnAPIResultListener {

    @BindByTag("signBtnLl")
    LinearLayout signBtnLl;
    @BindByTag("rejectBtn")
    Button rejectBtn; // 拒签
    @BindByTag("allChooseCheckBox")
    CheckBox allChooseCheckBox;
    @BindByTag("submitBtn")
    Button submitBtn;
    @BindByTag("waitStateRadioGroup")
    RadioGroup waitStateRadioGroup;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("recallTv")
    TextView recallTv; // 撤回
    @BindByTag("recallBackTv")
    TextView recallBackTv; // 退废

    Map<String, Object> queryParams = new HashMap<>(); // 配料记录查询
    private int status = 1;  // 当前配料记录状态
    private List<BatchMaterialPartEntity> chooseList = new ArrayList<>();
    private StringBuilder ids = new StringBuilder(); // 配料记录

    private BatchMaterialRecordsListAdapter mBatchMaterialRecordsListAdapter;

    @Override
    protected IListAdapter<BatchMaterialPartEntity> createAdapter() {
        mBatchMaterialRecordsListAdapter = new BatchMaterialRecordsListAdapter(context);
        return mBatchMaterialRecordsListAdapter;
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

        queryParams.put(Constant.BAPQuery.BAT_RECORD_STATE, BmConstant.SystemCode.RECORD_STATE_BATCH);

        getController(BatchMaterialRecordsSubmitController.class).setOnAPIResultListener(this);  // 注册回调监听
    }

    @Override
    protected int getLayoutID() {
        return R.layout.wom_fragment_batch_material_records_list;
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
            if (pageIndex == 1){
                allChooseCheckBox.setChecked(false);
            }
            queryParams.put(Constant.BAPQuery.PRODUCE_BATCH_NUM, ((BatchMaterialListActivity) context).getSearch());
            presenterRouter.create(CommonListAPI.class).list(pageIndex, null, queryParams, BmConstant.URL.BATCH_MATERIAL_RECORDS_LIST_URL, "batMaterilPart");
        });

        RxView.clicks(recallBackTv).throttleFirst(200,TimeUnit.MILLISECONDS)
                .subscribe(o -> IntentRouter.go(context,Constant.Router.BATCH_MATERIAL_RECALL_ABANDON_LIST));
        RxView.clicks(recallTv).throttleFirst(200,TimeUnit.MILLISECONDS)
                .subscribe(o -> IntentRouter.go(context,Constant.Router.BATCH_MATERIAL_RECALL_LIST));
        waitStateRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.batchRBtn) { // 未配料
                status = 1;
                queryParams.put(Constant.BAPQuery.BAT_RECORD_STATE, BmConstant.SystemCode.RECORD_STATE_BATCH);
                rejectBtn.setVisibility(View.GONE);
                submitBtn.setText(getString(R.string.wom_batch_material_finish));
            } else if (checkedId == R.id.deliverRBtn) { // 待派送
                status = 2;
                rejectBtn.setVisibility(View.VISIBLE);
                rejectBtn.setText(getString(R.string.wom_recall));
                submitBtn.setText(context.getResources().getString(R.string.wom_deliver));
                queryParams.put(Constant.BAPQuery.BAT_RECORD_STATE, BmConstant.SystemCode.RECORD_STATE_DELIVER);
            } else if (checkedId == R.id.signRBtn) { // 待签收
                status = 3;
                rejectBtn.setVisibility(View.VISIBLE);
                rejectBtn.setText(getString(R.string.wom_reject_sign));
                submitBtn.setText(context.getResources().getString(R.string.wom_sign));
                queryParams.put(Constant.BAPQuery.BAT_RECORD_STATE, BmConstant.SystemCode.RECORD_STATE_SIGN);
            } else if (checkedId == R.id.allocateRBtn) { // 待放
                status = 4;
                rejectBtn.setVisibility(View.GONE);
                submitBtn.setText(getString(R.string.wom_allocate));
                queryParams.put(Constant.BAPQuery.BAT_RECORD_STATE, BmConstant.SystemCode.RECORD_STATE_ALLOCATE);
            } else {
                queryParams.put(Constant.BAPQuery.BAT_RECORD_STATE, "");
            }
            refreshListController.refreshBegin();
        });
        mBatchMaterialRecordsListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            BatchMaterialPartEntity batchMaterialPartEntity = (BatchMaterialPartEntity) obj;
            if ("checkBox".equals(childView.getTag().toString())) {
                batchMaterialPartEntity.setChecked(!batchMaterialPartEntity.isChecked());
                // do全选
                doAllSelect();
            }

        });
        allChooseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBatchMaterialRecordsListAdapter.getList() != null) {
                    for (BatchMaterialPartEntity entity : mBatchMaterialRecordsListAdapter.getList()) {
                        entity.setChecked(!entity.isChecked());
                    }
                    mBatchMaterialRecordsListAdapter.notifyDataSetChanged();
                }
            }
        });
        RxView.clicks(submitBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        switchSubmit(1);
                    }
                });
        RxView.clicks(rejectBtn).throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        switchSubmit(0);
                    }
                });

    }

    /**
     * @author zhangwenshuai1 2020/8/27
     * @param
     * @return
     * @description  全选
     *
     */
    private void doAllSelect() {
        int count = 0;
        for (BatchMaterialPartEntity entity : mBatchMaterialRecordsListAdapter.getList()) {
            if (entity.isChecked()) {
                count++;
            }
        }
        if (count == mBatchMaterialRecordsListAdapter.getList().size()){
            allChooseCheckBox.setChecked(true);
        }else {
            allChooseCheckBox.setChecked(false);
        }
    }

    /**
     * @author zhangwenshuai1 2020/4/18
     * @param
     * @param submitType
     * @return
     * @description 配料记录提交
     *
     */
    private void switchSubmit(int submitType) {
        if (checkSubmit()){
            return;
        }
        List<String> stringList = new ArrayList<>();
        switch (status){
            case 1: // 配料完成
                stringList.add(getString(R.string.wom_batch_material_finish));
                stringList.add("complete");
                doSubmit(stringList);
                break;
            case 2: // 派送/撤回
                if (submitType == 0){
                    stringList.add(getString(R.string.wom_recall));
                    stringList.add("recall");
                }else {
                    stringList.add(getString(R.string.wom_deliver));
                    stringList.add("delivered");
                }
                doSubmit(stringList);
                break;
            case 3:
                doSignSubmit(submitType);
                break;
            case 4: // 配放
                stringList.add(getString(R.string.wom_allocate));
                stringList.add("allocat");
                doSubmit(stringList);
                break;
        }
    }

    /**
     * @author zhangwenshuai1 2020/4/20
     * @param
     * @return
     * @description 配料记录签收/拒签
     *
     */
    private void doSignSubmit(int submitType) {
        if (submitType == 0){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IntentKey.MATERIAL_RECORDS_LIST,GsonUtil.gsonString(chooseList));
            IntentRouter.go(context,Constant.Router.BATCH_MATERIAL_REJECT_LIST,bundle);
        }else {
            CustomDialog customDialog = new CustomDialog(context).layout(R.layout.wom_dialog_confirm,(DisplayUtil.getScreenWidth(context))*4/5, ViewGroup.LayoutParams.WRAP_CONTENT);
            customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
            customDialog.bindView(R.id.tipContentTv,getString(R.string.wom_confirm_batch_material_operate)+getString(R.string.wom_sign)+getString(R.string.wom_middle_right_brackets))
                    .bindClickListener(R.id.cancelTv,null,true)
                    .bindClickListener(R.id.confirmTv, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onLoading(getString(R.string.wom_dealing));
                            BatchMaterialRecordsSignSubmitDTO dto = new BatchMaterialRecordsSignSubmitDTO();
                            dto.setDetails(chooseList.toString()/*GsonUtil.gsonString(chooseList)*/);
                            getController(BatchMaterialRecordsSubmitController.class).submit(null,dto,true);
                        }
                    }, true)
                    .show();
        }
    }

    /**
     * @author zhangwenshuai1 2020/4/20
     * @param
     * @return
     * @description 配料记录操作：配料完成/派送/撤回/配放
     *
     */
    private void doSubmit(List<String> stringList) {
        CustomDialog customDialog = new CustomDialog(context).layout(R.layout.wom_dialog_confirm,(DisplayUtil.getScreenWidth(context))*4/5, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        customDialog.bindView(R.id.tipContentTv,getString(R.string.wom_confirm_batch_material_operate)+stringList.get(0)+getString(R.string.wom_middle_right_brackets))
                .bindClickListener(R.id.cancelTv,null,true)
                .bindClickListener(R.id.confirmTv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoading(getString(R.string.wom_dealing));
                        Map<String,Object> paramsMap = new ArrayMap<>();
                        paramsMap.put("ids",ids.substring(0,ids.length()-1));
                        paramsMap.put("status",stringList.get(1));
                        getController(BatchMaterialRecordsSubmitController.class).submit(paramsMap,null,false);
                    }
                }, true)
                .show();
    }

    /**
     * 提交检查
     * @return
     */
    private boolean checkSubmit() {
        List<BatchMaterialPartEntity> list = mBatchMaterialRecordsListAdapter.getList();
        if (list.size() <= 0){
            ToastUtils.show(context,getString(R.string.wom_no_data_operate));
            return true;
        }
        ids.delete(0,ids.length());
        chooseList.clear();
        for (BatchMaterialPartEntity entity : list){
            if (entity.isChecked()){
                chooseList.add(entity);
                ids.append(entity.getId()).append(",");

                // 签收记录处理
                entity.setReceiveDate(System.currentTimeMillis());
                StaffEntity staffEntity = new StaffEntity();
                staffEntity.id = SupPlantApplication.getAccountInfo().staffId;
                entity.setReceiveStaff(staffEntity);
                SystemCodeEntity systemCodeEntity = new SystemCodeEntity();
                systemCodeEntity.id = BmConstant.SystemCode.RECEIVE_STATE_RECEIVE;
                entity.setReceiveState(systemCodeEntity);
                entity.setReceiveNum(entity.getOfferNum());
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
        if (mBatchMaterialRecordsListAdapter.getList().size() <= 0){
            signBtnLl.setVisibility(View.GONE);
        }else {
            signBtnLl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void listFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, ErrorMsgHelper.msgParse(errorMsg));
        signBtnLl.setVisibility(View.GONE);
    }
    public void search(String searchContent) {
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
            if (mBatchMaterialRecordsListAdapter.getList().size() == 0){
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
                for (BatchMaterialPartEntity batchMaterialPartEntity : mBatchMaterialRecordsListAdapter.getList()){
                    if (TextUtils.isEmpty(batchMaterialPartEntity.getMaterialBatchNum())){
                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode())){
                            batchMaterialPartEntity.setChecked(true);
                            mBatchMaterialRecordsListAdapter.notifyItemChanged(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
                            contentView.scrollToPosition(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
                            doAllSelect();
                            return;
                        }
                    }else {
                        if (materialQRCodeEntity.getMaterial().getCode().equals(batchMaterialPartEntity.getMaterialId().getCode()) &&
                                materialQRCodeEntity.getMaterialBatchNo().equals(batchMaterialPartEntity.getMaterialBatchNum())){
                            batchMaterialPartEntity.setChecked(true);
                            mBatchMaterialRecordsListAdapter.notifyItemChanged(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
                            contentView.scrollToPosition(mBatchMaterialRecordsListAdapter.getList().indexOf(batchMaterialPartEntity));
                            doAllSelect();
                            return;
                        }
                    }

                    count++;
                }

                if (count == mBatchMaterialRecordsListAdapter.getList().size()){
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
}
