package com.wengmengfan.btwang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.ToastUtils;
import com.pgyersdk.activity.FeedbackActivity;
import com.wengmengfan.btwang.R;
import com.wengmengfan.btwang.base.BaseFragment;
import com.wengmengfan.btwang.base.Constant;
import com.wengmengfan.btwang.component.AppComponent;
import com.wengmengfan.btwang.component.DaggerMainComponent;
import com.wengmengfan.btwang.ui.activity.AboutActivity;
import com.wengmengfan.btwang.ui.activity.DownListActivity;
import com.wengmengfan.btwang.ui.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * sayid ....
 * Created by wengmf on 2018/3/12.
 */

public class MeFragment extends BaseFragment {


    @BindView(R.id.updae)
    RelativeLayout updae;
    @BindView(R.id.baout)
    RelativeLayout baout;
    @BindView(R.id.feedback)
    RelativeLayout feedback;
    @BindView(R.id.exit)
    RelativeLayout exit;
    @BindView(R.id.down)
    RelativeLayout down;
    Unbinder unbinder;


    @Override
    public void loadData() {
        setState(Constant.STATE_SUCCESS);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_me_home;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void attachView() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @OnClick({R.id.updae, R.id.baout, R.id.feedback, R.id.exit,R.id.down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.down:
                getActivity().startActivity(new Intent(getActivity(), DownListActivity.class));
                break;
            case R.id.updae:
                ToastUtils.showLongToast("已是最新版本");
                break;
            case R.id.baout:
                getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.feedback:
                getActivity().startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.exit:
                MainActivity.mainActivity.killAll();
                break;
        }
    }
}
