package com.supcon.mes.module_wom_ble.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.supcon.mes.middleware.model.bean.PopupWindowEntity;
import com.supcon.mes.module_ble.R;


import java.util.List;

/**
 * Author by fengjun1,
 * Date on 2020/4/7.
 */
public class WmsPopupWindowAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<PopupWindowEntity> mItems;

    public WmsPopupWindowAdapter(Context context, List<PopupWindowEntity> items) {
        mContext = context;
        mItems = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopupWindowHolder popupWindowHolder = null;
        if (convertView == null) {
            popupWindowHolder = new PopupWindowHolder();
            convertView = inflater.inflate(R.layout.item_wom_popuwindow, null);
            popupWindowHolder.mTextView = convertView.findViewById(com.supcon.mes.middleware.R.id.popupTv);
            convertView.setTag(popupWindowHolder);

        } else {
            popupWindowHolder = (PopupWindowHolder) convertView.getTag();
        }
        PopupWindowEntity popupWindowEntity = (PopupWindowEntity) getItem(position);
        popupWindowHolder.mTextView.setText(popupWindowEntity.getText());

        return convertView;
    }

    private class PopupWindowHolder {
        TextView mTextView;
    }
}
