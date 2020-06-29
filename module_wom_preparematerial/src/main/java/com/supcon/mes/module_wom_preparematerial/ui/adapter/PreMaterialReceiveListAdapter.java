package com.supcon.mes.module_wom_preparematerial.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.listener.OnChildViewClickListener;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomRoundTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialReceiveListAdapter extends BaseListDataRecyclerViewAdapter<PreMaterialEntity> {

    private List<String> rejectReasons;
    private Map<String, String> rejectStates;
    private RejectReasonAdapter mRejectReasonAdapter;

    public PreMaterialReceiveListAdapter(Context context) {
        super(context);
    }

    public PreMaterialReceiveListAdapter(Context context, List<PreMaterialEntity> list) {
        super(context, list);
    }

    @Override
    protected BaseRecyclerViewHolder<PreMaterialEntity> getViewHolder(int viewType) {
        return new PreMaterialReceiveViewHolder(context);
    }

    public void setRejectReasons(List<String> rejectReasons) {
        this.rejectReasons = rejectReasons;
        if(mRejectReasonAdapter == null){
            mRejectReasonAdapter = new RejectReasonAdapter(context, rejectReasons);
        }
        else{
            mRejectReasonAdapter.setList(rejectReasons);
        }
        mRejectReasonAdapter.notifyDataSetChanged();
    }

    public void setRejectStates(Map<String, String> rejectStates) {
        this.rejectStates = rejectStates;
    }

    class PreMaterialReceiveViewHolder extends BaseRecyclerViewHolder<PreMaterialEntity>{

        @BindByTag("itemPreMaterialReceiveTableNo")
        TextView itemPreMaterialReceiveTableNo;

        @BindByTag("itemPreMaterialReceiveDeliverCode")
        TextView itemPreMaterialReceiveDeliverCode;

        @BindByTag("itemPreMaterialReceiveIc")
        CustomRoundTextImageView itemPreMaterialReceiveIc;

        @BindByTag("itemPreMaterialReceiveMaterial")
        TextView itemPreMaterialReceiveMaterial;

        @BindByTag("itemPreMaterialReceiveStoreLocation")
        TextView itemPreMaterialReceiveStoreLocation;

        @BindByTag("itemPreMaterialReceiveBatchNum")
        TextView itemPreMaterialReceiveBatchNum;

        @BindByTag("itemPreMaterialReceiveNum")
        TextView itemPreMaterialReceiveNum;

        @BindByTag("itemPreMaterialReceiveStaff")
        CustomTextView itemPreMaterialReceiveStaff;

        @BindByTag("itemPreMaterialReceiveReasons")
        Spinner itemPreMaterialReceiveReasons;

        @BindByTag("itemPreMaterialReceiveRealNum")
        CustomEditText itemPreMaterialReceiveRealNum;

        @BindByTag("itemPreMaterialRejectReasonView")
        RecyclerView itemPreMaterialRejectReasonView;

        @BindByTag("itemPreMaterialRejectReasonLayout")
        LinearLayout itemPreMaterialRejectReasonLayout;

        @BindByTag("itemPreMaterialReceiveReason")
        CustomEditText itemPreMaterialReceiveReason;




        public PreMaterialReceiveViewHolder(Context context) {
            super(context);
        }

        public PreMaterialReceiveViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        public PreMaterialReceiveViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView() {
            super.initView();
            itemPreMaterialRejectReasonView.setAdapter(mRejectReasonAdapter);
            if(rejectStates!=null && rejectStates.size()!=0){
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.ly_spinner_item_dark, (List<String>) rejectStates.values());
                adapter.setDropDownViewResource(R.layout.ly_spinner_dropdown_item);
                itemPreMaterialReceiveReasons.setAdapter(adapter);
            }
        }

        @Override
        protected void initListener() {
            super.initListener();

            itemPreMaterialReceiveStaff.setOnChildViewClickListener(new OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int action, Object obj) {

                }
            });

            itemPreMaterialReceiveReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String value = (String) itemPreMaterialReceiveReasons.getSelectedItem();
                    if(TextUtils.isEmpty(value)){
                        return;
                    }
                    for(String key: rejectStates.keySet()){
                        if(value.equals(rejectStates.get(key))){
                            if("WOM_receiveState/partReceive".equals(key)){
                                itemPreMaterialReceiveReason.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_pre_material_receive;
        }

        @Override
        protected void update(PreMaterialEntity data) {
            itemPreMaterialReceiveTableNo.setText(data.preOrderId.orderTableNo);
            itemPreMaterialReceiveDeliverCode.setText(data.deliverCode);
            if(data.materialId!=null && data.materialId.name!=null){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(data.materialId.name);
                stringBuilder.append("(");
                stringBuilder.append(data.materialId.code);
                stringBuilder.append(")");
                itemPreMaterialReceiveIc.setText(data.materialId.name);
                itemPreMaterialReceiveMaterial.setText(stringBuilder.toString());
            }
            if(data.fromWare!=null && data.fromWare.name!=null){
                StringBuilder fromStr = new StringBuilder(data.fromWare.name);
                fromStr.append("/");
                if(data.fromStore!=null && data.fromStore.name!=null){
                    fromStr.append(data.fromStore.name);
                }
                else{
                    fromStr.append("--");
                }
                itemPreMaterialReceiveStoreLocation.setText(fromStr.toString());
            }
            else{
                itemPreMaterialReceiveStoreLocation.setText("--/--");
            }

            itemPreMaterialReceiveBatchNum.setText(String.valueOf(data.materialBatchNum));
            itemPreMaterialReceiveNum.setText(String.valueOf(data.preNum));

            ///=====================//
            itemPreMaterialReceiveStaff.setContent(SupPlantApplication.getAccountInfo().getStaffName());

            if("WOM_receiveState/partReceive".equals(data.receiveState.id)){
                itemPreMaterialReceiveReason.setContent(data.remark);
                itemPreMaterialReceiveReason.setVisibility(View.VISIBLE);
            }
            else{
                itemPreMaterialReceiveReason.setVisibility(View.GONE);
            }

            if("WOM_receiveState/reject".equals(data.receiveState.id)){
                itemPreMaterialRejectReasonView.setVisibility(View.VISIBLE);
                if(data.receiveReason!=null && data.receiveReason.value!=null) {
                    mRejectReasonAdapter.setPosition(rejectReasons.indexOf(data.receiveReason.value));
                }
            }
            else{
                itemPreMaterialRejectReasonView.setVisibility(View.GONE);
            }

            itemPreMaterialReceiveRealNum.setText(data.receiveNum!=null?String.valueOf(data.receiveNum):"");

        }
    }
}
