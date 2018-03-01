package com.qushuwang.qushuwang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.qushuwang.qushuwang.R;
import com.qushuwang.qushuwang.base.BaseFragment;
import com.qushuwang.qushuwang.base.Constant;
import com.qushuwang.qushuwang.bean.TuPianHomeBean;
import com.qushuwang.qushuwang.component.AppComponent;
import com.qushuwang.qushuwang.component.DaggerMainComponent;
import com.qushuwang.qushuwang.presenter.contract.DongTu_TitleContract;
import com.qushuwang.qushuwang.presenter.contract.TuPian_TitleContract;
import com.qushuwang.qushuwang.presenter.impl.DongTu_TitlePresenter;
import com.qushuwang.qushuwang.presenter.impl.TuPian_TitlePresenter;
import com.qushuwang.qushuwang.ui.activity.DongTuImgContentActivity;
import com.qushuwang.qushuwang.ui.activity.TuPianImgContentActivity;
import com.qushuwang.qushuwang.ui.adapter.TuPian_Home_Adapter;
import com.qushuwang.qushuwang.utils.StringUtlis;
import com.qushuwang.qushuwang.view.MyLoadMoreView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class DongTu_Title extends BaseFragment implements DongTu_TitleContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    DongTu_TitlePresenter mPresenter;

    @BindView(R.id.title_list)
    RecyclerView Book_Dir_List;

    @BindView(R.id.srl_android)
    SwipeRefreshLayout srlAndroid;

    private TuPian_Home_Adapter mAdapter;
    private List<TuPianHomeBean> dataBean;
    private String Url;
    private int index = 1;
    private int id;

    private boolean isRefresh = false;

    public static DongTu_Title newInstance(String url,int id) {

        DongTu_Title manHuan_name = new DongTu_Title();
        Bundle bundle = new Bundle();
        bundle.putString("Url", url);
        bundle.putInt("Id", id);
        manHuan_name.setArguments(bundle);
        return manHuan_name;

    }


    @Override
    public void loadData() {
        setState(Constant.STATE_SUCCESS);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_meinvha_dir;
    }

    @Override
    protected void initView(Bundle bundle) {

        Url = bundle.getString("Url");
        id = bundle.getInt("Id");

        mPresenter.Fetch_DongTu_Img(Url);

        srlAndroid.setOnRefreshListener(this);

        mAdapter = new TuPian_Home_Adapter(dataBean, getSupportActivity(), "DongTu");
        Book_Dir_List.setLayoutManager(new GridLayoutManager(getSupportActivity(), 2));
        mAdapter.setOnLoadMoreListener(this, Book_Dir_List);
        mAdapter.setLoadMoreView(new MyLoadMoreView());

        Book_Dir_List.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new TuPian_Home_Adapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(TuPianHomeBean item) {

                String itemUrl = item.getUrl();
                itemUrl = StringUtlis.subString(itemUrl, "/");
                itemUrl = StringUtlis.subString(itemUrl, "/");

                Intent intent = new Intent(getActivity(), DongTuImgContentActivity.class);
                intent.putExtra("Url", Url + itemUrl );
                intent.putExtra("BaseUrl", Url);
               getActivity().startActivity(intent);

            }
        });
    }

    @Override
    public void attachView() {
        mPresenter.attachView(this);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showError(String message) {
        LogUtils.e(message);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        mAdapter.setEnableLoadMore(false);
        mPresenter.Fetch_DongTu_Img(Url);
    }

    @Override
    public void Fetch_DongTu_Img_Success(List<TuPianHomeBean> dataBean) {
        if (dataBean.size() == 0) {
            setState(Constant.STATE_ERROR);
        }

        if (isRefresh) {
            srlAndroid.setRefreshing(false);
            mAdapter.setEnableLoadMore(true);
            isRefresh = false;
            mAdapter.setNewData(dataBean);
        } else {
            srlAndroid.setEnabled(true);
            mAdapter.addData(dataBean);
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (index < 8) {
            index++;
//            http://www.yaoqmhw.net/mntp/list_6_2.html
           String hh=  Url + "/list_" + id+"_" +index+ ".html";
           Logger.e("HH"+hh);
            mPresenter.Fetch_DongTu_Img(hh);
            srlAndroid.setEnabled(false);
        }
    }
}