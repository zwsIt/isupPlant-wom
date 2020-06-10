package com.supcon.mes.module_wom_pending.ui;

import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseMultiFragmentActivity;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_wom_pending.R;
import com.supcon.mes.module_wom_pending.ui.fragment.WOMWidgetActivityListFragment;
import com.supcon.mes.module_wom_pending.ui.fragment.WOMWidgetProduceTaskListFragment;

/**
 * Created by wangshizhan on 2020/6/9
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.AppCode.WOM_Pending)
public class WOMPendingListActivvity extends BaseMultiFragmentActivity {

    @BindByTag("textTitle")
    TextView textTitle;

    @Override
    public int getFragmentContainerId() {
        return R.id.fragmentLayout;
    }

    @Override
    public void createFragments() {
        fragments.add(new WOMWidgetProduceTaskListFragment());
        fragments.add(new WOMWidgetActivityListFragment());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_wom_pending;
    }

    @Override
    protected void initView() {
        super.initView();
        textTitle.setText("生产待办");
    }


}
