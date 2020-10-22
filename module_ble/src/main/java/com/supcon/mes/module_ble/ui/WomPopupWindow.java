package com.supcon.mes.module_wom_ble.ui;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.supcon.mes.middleware.model.bean.PopupWindowEntity;
import com.supcon.mes.middleware.util.AnimationUtil;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_wom_ble.ui.adapter.WmsPopupWindowAdapter;

import java.util.List;

/**
 * Author by fengjun1,
 * Date on 2020/4/7.
 */
public class WomPopupWindow extends PopupWindow {

    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;

    private Context mContext;
    private View mMenuView;
    private final ListView mListView;
    private WmsPopupWindowAdapter mPopupWindowAdapter;
    private AnimationUtil mAnimationUtil;

    private float bgAlpha = 1f;
    private boolean bright = false;

    public WomPopupWindow(Context context, List<PopupWindowEntity> items) {
        mContext = context;
        mMenuView = LayoutInflater.from(context).inflate(com.supcon.mes.middleware.R.layout.ac_popupwindow, null);
        mListView = mMenuView.findViewById(com.supcon.mes.middleware.R.id.popup_lv);

        mAnimationUtil = new AnimationUtil();

        initListView(items);
        setPopup();

    }

    private void setPopup() {
        // 设置Popup的总布局View
        setContentView(mMenuView);
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setWidth(displayMetrics.widthPixels / 3 + 10);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
//        setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
//        setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        setOutsideTouchable(true);
        // 设置pop关闭监听，用于改变背景透明度
        setOnDismissListener(() -> {
            toggleBright();
        });

        // 刷新状态
        update();

    }

    private void initListView(List<PopupWindowEntity> items) {
        mPopupWindowAdapter = new WmsPopupWindowAdapter(mContext, items);
        mListView.setAdapter(mPopupWindowAdapter);
    }

    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        mAnimationUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        mAnimationUtil.addUpdateListener(new AnimationUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        mAnimationUtil.addEndListner(animator -> {
            // 在一次动画结束的时候，翻转状态
            bright = !bright;
        });
        mAnimationUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    public void setOnItemClick(AdapterView.OnItemClickListener myOnItemClickListener) {
        mListView.setOnItemClickListener(myOnItemClickListener);
    }

    public void showPopupWindow(View view) {
        int xoff = Util.dpToPx(mContext, (float) 90);//dp转px,解决不同手机错位问题:Android 7.0
        showAsDropDown(view,-xoff+15, -15);
        toggleBright();
    }
}
