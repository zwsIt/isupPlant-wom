package com.supcon.mes.module_wom_preparematerial.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.constant.ViewAction;
import com.supcon.mes.mbap.utils.GridSpaceItemDecoration;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.TextHelper;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomRoundTextImageView;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.model.bean.ObjectEntity;
import com.supcon.mes.middleware.model.bean.wom.StoreSetEntity;
import com.supcon.mes.middleware.util.SystemCodeManager;
import com.supcon.mes.module_wom_preparematerial.R;
import com.supcon.mes.module_wom_preparematerial.model.bean.PreMaterialEntity;
import com.supcon.mes.module_wom_producetask.constant.WomConstant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by wangshizhan on 2020/6/24
 * Email:wangshizhan@supcom.com
 */
public class PreMaterialReceiveListAdapter extends BaseListDataRecyclerViewAdapter<PreMaterialEntity> {

    private List<String> rejectReasons;
    private Map<String, String> receiveStates;

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

    }

    public void setRejectStates(Map<String, String> receiveStates) {
        this.receiveStates = receiveStates;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder<PreMaterialEntity> holder, int position) {
        super.onBindViewHolder(holder, position);
        LogUtil.d("-------onBindViewHolder-----" + (position + 1) + "------------");
    }

    @Override
    public BaseRecyclerViewHolder<PreMaterialEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.d("-------onCreateViewHolder-----------");
        return super.onCreateViewHolder(parent, viewType);

    }

    class PreMaterialReceiveViewHolder extends BaseRecyclerViewHolder<PreMaterialEntity> {

        @BindByTag("itemPreMaterialReceiveTableNo")
        TextView itemPreMaterialReceiveTableNo;

        @BindByTag("itemPreMaterialReceiveDeliverCode")
        CustomTextView itemPreMaterialReceiveDeliverCode;

        @BindByTag("itemPreMaterialReceiveIc")
        CustomRoundTextImageView itemPreMaterialReceiveIc;

        @BindByTag("itemPreMaterialReceiveMaterial")
        TextView itemPreMaterialReceiveMaterial;

        @BindByTag("itemPreMaterialReceiveStoreLocation")
        CustomTextView itemPreMaterialReceiveStoreLocation;

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

        @BindByTag("itemPreMaterialRejectReasonViewText")
        TextView itemPreMaterialRejectReasonViewText;

        @BindByTag("itemPreMaterialReceiveReason")
        CustomEditText itemPreMaterialReceiveReason;

        @BindByTag("itemPreMaterialReceiveCheck")
        ImageView itemPreMaterialReceiveCheck;

        @BindByTag("itemPreMaterialReceiveLine")
        View itemPreMaterialReceiveLine;

        private RejectReasonAdapter mRejectReasonAdapter;
        private List<String> mReceiveStateStr;

        PreMaterialReceiveViewHolder(Context context) {
            super(context);
        }

        public PreMaterialReceiveViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        public PreMaterialReceiveViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected int layoutId() {
            return R.layout.wom_item_pre_material_receive;
        }

        @Override
        protected void initView() {
            super.initView();
            itemPreMaterialRejectReasonView.setLayoutManager(new GridLayoutManager(context, 4));
            itemPreMaterialRejectReasonView.addItemDecoration(new GridSpaceItemDecoration(DisplayUtil.dip2px(3, context), 4));

            if (receiveStates != null && receiveStates.size() != 0) {
                mReceiveStateStr = new ArrayList<>();
                mReceiveStateStr.addAll(receiveStates.values());
                ArrayAdapter<String> receiveStateAdapter = new ArrayAdapter<>(context, R.layout.ly_spinner_item_dark14, mReceiveStateStr);
                receiveStateAdapter.setDropDownViewResource(R.layout.ly_spinner_dropdown_item);
                itemPreMaterialReceiveReasons.setAdapter(receiveStateAdapter);
                itemPreMaterialReceiveReasons.setSelection(itemPreMaterialReceiveReasons.getSelectedItem() == null ?
                        mReceiveStateStr.indexOf(receiveStates.get("WOM_receiveState/receive")) : mReceiveStateStr.indexOf(itemPreMaterialReceiveReasons.getSelectedItem().toString()));
            }
            TextHelper.setRequired(true, itemPreMaterialRejectReasonViewText);

            mRejectReasonAdapter = new RejectReasonAdapter(context, rejectReasons);
            mRejectReasonAdapter.setList(rejectReasons);
            itemPreMaterialRejectReasonView.setAdapter(mRejectReasonAdapter);

            itemPreMaterialReceiveRealNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        @SuppressLint("CheckResult")
        @Override
        protected void initListener() {
            super.initListener();

            itemPreMaterialReceiveStaff.setOnChildViewClickListener((childView, action, obj) -> {

                if (action == ViewAction.CONTENT_CLEAN.value()) {
                    getItem(getAdapterPosition()).receiveStaff = null;
                } else {
                    onItemChildViewClick(itemPreMaterialReceiveStaff, 0, getItem(getAdapterPosition()));
                }
            });

            itemPreMaterialReceiveReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PreMaterialEntity entity = getItem(getAdapterPosition());
                    String value = /*entity.receiveState.value*/ (String) itemPreMaterialReceiveReasons.getSelectedItem();
                    if (TextUtils.isEmpty(value)) {
                        return;
                    }

                    for (String key : receiveStates.keySet()) {
                        if (value.equals(receiveStates.get(key))) {
                            entity.receiveState = SystemCodeManager.getInstance().getSystemCodeEntity(key);

                            // ??????
//                            if(entity.toWareId!=null && entity.toWareId.getName()!=null){
//                                StringBuilder fromStr = new StringBuilder(entity.toWareId.getName());
//                                fromStr.append("/");
//                                if(entity.toStoreId!=null && entity.toStoreId.getName()!=null){
//                                    fromStr.append(entity.toStoreId.getName());
//                                }
//                                else{
//                                    fromStr.append("--");
//                                }
//                                itemPreMaterialReceiveStoreLocation.setContent(fromStr.toString());
//                            } else{
//                                itemPreMaterialReceiveStoreLocation.setContent("--/--");
//                            }

                            itemPreMaterialReceiveStoreLocation.setEditable(true);
                            itemPreMaterialReceiveStoreLocation.findViewById(R.id.customDeleteIcon).setVisibility(View.VISIBLE);
                            if ("WOM_receiveState/partReceive".equals(key)) {
                                itemPreMaterialReceiveLine.setVisibility(View.VISIBLE);
                                itemPreMaterialReceiveReason.setVisibility(View.VISIBLE);
                                itemPreMaterialRejectReasonLayout.setVisibility(View.GONE);
                                itemPreMaterialReceiveRealNum.setEditable(true);
                                itemPreMaterialReceiveRealNum.setContent(null);
                                itemPreMaterialReceiveReason.setContent(entity.remark);
                                entity.receiveNum = null;
                            } else if ("WOM_receiveState/reject".equals(key)) {
                                itemPreMaterialReceiveStoreLocation.setEditable(false);
                                itemPreMaterialReceiveStoreLocation.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);

                                itemPreMaterialReceiveLine.setVisibility(View.VISIBLE);
                                itemPreMaterialReceiveReason.setVisibility(View.GONE);
                                itemPreMaterialRejectReasonLayout.setVisibility(View.VISIBLE);
                                if (entity.rejectReason != null && entity.rejectReason.value != null) {
                                    mRejectReasonAdapter.setPosition(rejectReasons.indexOf(entity.rejectReason.value));
                                } else {
                                    mRejectReasonAdapter.setPosition(-1);
                                }

                                mRejectReasonAdapter.notifyDataSetChanged();

                                itemPreMaterialReceiveRealNum.setEnabled(false);
                                itemPreMaterialReceiveRealNum.setContent(null);
                                entity.receiveNum = null;
                            } else {
                                itemPreMaterialReceiveRealNum.setEnabled(false);
                                if (entity.preNum != null)
                                    itemPreMaterialReceiveRealNum.setContent(String.valueOf(entity.preNum));
                                itemPreMaterialReceiveLine.setVisibility(View.GONE);
                                itemPreMaterialReceiveReason.setVisibility(View.GONE);
                                itemPreMaterialRejectReasonLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            RxView.clicks(itemPreMaterialReceiveCheck)
                    .throttleFirst(200, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        PreMaterialEntity preMaterialEntity = getItem(getAdapterPosition());
                        preMaterialEntity.isChecked = !preMaterialEntity.isChecked;
                        if (preMaterialEntity.isChecked) {
                            itemPreMaterialReceiveCheck.setImageResource(R.drawable.ic_check_yes);
                        } else {
                            itemPreMaterialReceiveCheck.setImageResource(R.drawable.ic_check_no);
                        }
                    });

            mRejectReasonAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
                String reason = (String) obj;
                PreMaterialEntity materialEntity = getItem(getAdapterPosition());
                materialEntity.rejectReason = SystemCodeManager.getInstance().getSystemCodeEntity(WomConstant.SystemCode.WOM_rejectReason, reason);
            });

            RxTextView.textChanges(itemPreMaterialReceiveRealNum.editText())
                    .skipInitialValue()
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(charSequence -> {

                        if (TextUtils.isEmpty(charSequence)) {
                            PreMaterialEntity materialEntity = getItem(getAdapterPosition());
                            materialEntity.receiveNum = null;
                            return;
                        }
                        PreMaterialEntity materialEntity = getItem(getAdapterPosition());
                        if (materialEntity == null)return;
                        float receiveNumF = new BigDecimal(charSequence.toString()).floatValue();

                        if (materialEntity.receiveState == null) {
                            ToastUtils.show(context, context.getResources().getString(R.string.wom_please_select_receive_type));
                            return;
                        }
                        if ("WOM_receiveState/partReceive".equals(materialEntity.receiveState.id) || "WOM_receiveState/receive".equals(materialEntity.receiveState.id)) {
                            if (materialEntity.preNum != null && receiveNumF > materialEntity.preNum) {
                                ToastUtils.show(context, context.getResources().getString(R.string.wom_receive_num_smaller_than_preparenum));
                                return;
                            }
                        }

                        materialEntity.receiveNum = receiveNumF;

                    });

            RxTextView.textChanges(itemPreMaterialReceiveReason.editText())
                    .skipInitialValue()
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .subscribe(charSequence -> {
                        PreMaterialEntity materialEntity = getItem(getAdapterPosition());
                        if (TextUtils.isEmpty(charSequence)) {

                            materialEntity.remark = "";
                            return;
                        }

                        materialEntity.remark = charSequence.toString();

                    });
            itemPreMaterialReceiveStoreLocation.setOnChildViewClickListener((childView, action, obj) -> onItemChildViewClick(itemPreMaterialReceiveStoreLocation, 0, getItem(getAdapterPosition())));
        }

        @Override
        protected void update(PreMaterialEntity data) {
            itemPreMaterialReceiveTableNo.setText(data.preOrderId.orderTableNo);
            itemPreMaterialReceiveDeliverCode.setContent(data.deliverCode != null ? data.deliverCode : "");
            if (data.materialId != null && data.materialId.getName() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(data.materialId.getName());
                stringBuilder.append("(");
                stringBuilder.append(data.materialId.getCode());
                stringBuilder.append(")");

                if (data.materialId.getName().length() > 2) {
                    itemPreMaterialReceiveIc.setText(data.materialId.getName().substring(0, 2));
                } else {
                    itemPreMaterialReceiveIc.setText(data.materialId.getName());
                }
                itemPreMaterialReceiveMaterial.setText(stringBuilder.toString());
            }
            if (data.toWareId != null && data.toWareId.getName() != null) {
                StringBuilder fromStr = new StringBuilder(data.toWareId.getName());
                fromStr.append("/");
                if (data.toStoreId != null && data.toStoreId.getName() != null) {
                    fromStr.append(data.toStoreId.getName());
                } else {
                    fromStr.append("--");
                }
                itemPreMaterialReceiveStoreLocation.setContent(fromStr.toString());
            } else {
                itemPreMaterialReceiveStoreLocation.setContent("--/--");
            }

            if (data.toStoreIdInit == null && data.toStoreId != null) {
                data.toStoreIdInit = GsonUtil.gsonToBean(data.toStoreId.toString(), StoreSetEntity.class);
            }

            itemPreMaterialReceiveBatchNum.setText(data.materialBatchNum != null ? data.materialBatchNum : "--");
            itemPreMaterialReceiveNum.setText(String.valueOf(data.preNum));

            if (data.receiveStaff != null && data.receiveStaff.name != null) {
                itemPreMaterialReceiveStaff.setContent(data.receiveStaff.name);
            } else {
                itemPreMaterialReceiveStaff.setContent(SupPlantApplication.getAccountInfo().getStaffName());
                ObjectEntity staff = new ObjectEntity(SupPlantApplication.getAccountInfo().staffId);
                staff.name = SupPlantApplication.getAccountInfo().getStaffName();
                data.receiveStaff = staff;
            }

            if (data.receiveState == null) {
                data.receiveState = SystemCodeManager.getInstance().getSystemCodeEntity("WOM_receiveState/receive");
            }

            if ("WOM_receiveState/partReceive".equals(data.receiveState.id)) {
                itemPreMaterialReceiveReason.setContent(data.remark);
                itemPreMaterialReceiveLine.setVisibility(View.VISIBLE);
                itemPreMaterialReceiveReason.setVisibility(View.VISIBLE);
                itemPreMaterialRejectReasonLayout.setVisibility(View.GONE);


                itemPreMaterialReceiveReasons.setSelection(/*itemPreMaterialReceiveReasons.getSelectedItem() == null ? 1 : */mReceiveStateStr.indexOf(receiveStates.get(data.receiveState.id)));
//                mReceiveStateAdapter.notifyDataSetChanged();
            } else if ("WOM_receiveState/reject".equals(data.receiveState.id)) {
                itemPreMaterialRejectReasonLayout.setVisibility(View.VISIBLE);
                itemPreMaterialReceiveLine.setVisibility(View.VISIBLE);
                itemPreMaterialReceiveReason.setVisibility(View.GONE);
                if (data.rejectReason != null && data.rejectReason.value != null) {
                    mRejectReasonAdapter.setPosition(rejectReasons.indexOf(data.rejectReason.value));
                }
                mRejectReasonAdapter.notifyDataSetChanged();
                itemPreMaterialReceiveRealNum.setEnabled(false);
                data.receiveNum = null;
            } else if ("WOM_receiveState/receive".equals(data.receiveState.id)) {
                itemPreMaterialRejectReasonLayout.setVisibility(View.GONE);
                itemPreMaterialReceiveLine.setVisibility(View.GONE);
                itemPreMaterialReceiveReason.setVisibility(View.GONE);
                itemPreMaterialReceiveReasons.setSelection(mReceiveStateStr.indexOf(receiveStates.get(data.receiveState.id)));
                itemPreMaterialReceiveRealNum.setEnabled(false);
                data.receiveNum = data.preNum;
            }

            itemPreMaterialReceiveRealNum.setContent(data.receiveNum != null ? String.valueOf(data.receiveNum) : "");

            if (data.isChecked) {
                itemPreMaterialReceiveCheck.setImageResource(R.drawable.ic_check_yes);
            } else {
                itemPreMaterialReceiveCheck.setImageResource(R.drawable.ic_check_no);
            }

        }
    }
}
