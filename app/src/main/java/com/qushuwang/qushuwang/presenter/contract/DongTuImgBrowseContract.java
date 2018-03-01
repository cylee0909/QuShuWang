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
package com.qushuwang.qushuwang.presenter.contract;


import com.qushuwang.qushuwang.base.BaseContract;

public interface DongTuImgBrowseContract {

    interface View extends BaseContract.BaseView {
        void downloadPicFromNet_Success(String filePath);
        void Fetch_DongTu_Img_Success(String Url);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void downloadPicFromNet(String imgUrl);
        void Fetch_DongTu_Img(String url);
    }

}
