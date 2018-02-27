/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qushuwang.qushuwang.presenter.impl;

import com.orhanobut.logger.Logger;
import com.qushuwang.qushuwang.api.Api;
import com.qushuwang.qushuwang.base.RxPresenter;
import com.qushuwang.qushuwang.bean.ImgContent;
import com.qushuwang.qushuwang.bean.MhContentBean;
import com.qushuwang.qushuwang.bean.request.Meinvha_Title_request;
import com.qushuwang.qushuwang.presenter.contract.ImgContentContract;
import com.qushuwang.qushuwang.presenter.contract.TuPianImgContentContract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TuPianImgContentPresenter extends RxPresenter<TuPianImgContentContract.View> implements TuPianImgContentContract.Presenter<TuPianImgContentContract.View> {

    private Api bookApi;
    public static boolean isLastSyncUpdateed = false;

    @Inject
    public TuPianImgContentPresenter(Api bookApi) {
        this.bookApi = bookApi;
    }


    @Override
    public void Fetch_TuPian_ImgInfo_Success(final String ImgUrl, final String Url) {
        Observable.create(new Observable.OnSubscribe<List<MhContentBean>>() {
            @Override
            public void call(Subscriber<? super List<MhContentBean>> subscriber) {
                //在call方法中执行异步任务
                List<MhContentBean> mhContentBeanList = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(ImgUrl).get();
                    Elements elements = doc.select("div[id=pages]");

                    String html = elements.html();
                    Elements document = Jsoup.parse(html).getElementsByTag("a");

                    for (Element e : document) {
                        MhContentBean mhContentBean = new MhContentBean();
                        String url = e.select("a").attr("href");
                        if(url.endsWith("html")){

                            mhContentBean.setImgSrc(Url+ e.select("a").attr("href"));
                            mhContentBean.setType("TuPian");
                            mhContentBeanList.add(mhContentBean);

                        }
                    }
                } catch (Exception e) {
                    //注意：如果异步任务中需要抛出异常，在执行结果中处理异常。需要将异常转化未RuntimException
                    throw new RuntimeException(e);
                }
                //调用subscriber#onNext方法将执行结果返回
                subscriber.onNext(mhContentBeanList);
                //调用subscriber#onCompleted方法完成异步任务
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())//指定异步任务在IO线程运行
                .observeOn(AndroidSchedulers.mainThread())//制定执行结果在主线程运行
                .subscribe(new Observer<List<MhContentBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        if(e!=null&& mView!=null){
//                            mView.showError(e.toString());
//                        }
                    }

                    @Override
                    public void onNext(List<MhContentBean> data) {
                        if (data != null && mView != null) {
                            mView.Fetch_TuPian_ImgInfo_Success(data);
                        }
                    }
                });
    }
}
