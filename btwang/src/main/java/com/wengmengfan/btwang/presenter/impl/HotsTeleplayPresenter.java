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
package com.wengmengfan.btwang.presenter.impl;

import com.wengmengfan.btwang.api.Api;
import com.wengmengfan.btwang.base.RxPresenter;
import com.wengmengfan.btwang.presenter.contract.HotsMangaContract;
import com.wengmengfan.btwang.presenter.contract.HotsTeleplayContract;

import javax.inject.Inject;

public class HotsTeleplayPresenter extends RxPresenter<HotsTeleplayContract.View> implements HotsTeleplayContract.Presenter<HotsTeleplayContract.View> {


    private Api Api;
    public static boolean isLastSyncUpdateed = false;

    @Inject
    public HotsTeleplayPresenter(Api Api) {
        this.Api = Api;
    }


}
