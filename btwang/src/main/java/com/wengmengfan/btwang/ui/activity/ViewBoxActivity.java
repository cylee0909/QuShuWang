package com.wengmengfan.btwang.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.blankj.utilcode.utils.ZipUtils;
import com.orhanobut.logger.Logger;
import com.wengmengfan.btwang.R;
import com.wengmengfan.btwang.base.BaseActivity;
import com.wengmengfan.btwang.bean.DownHrefBean;
import com.wengmengfan.btwang.bean.ViewBoxBean;
import com.wengmengfan.btwang.component.AppComponent;
import com.wengmengfan.btwang.component.DaggerMainComponent;
import com.wengmengfan.btwang.presenter.contract.ViewBoxContract;
import com.wengmengfan.btwang.presenter.impl.ViewBoxPresenter;
import com.wengmengfan.btwang.utils.DeviceUtils;
import com.wengmengfan.btwang.utils.ImgLoadUtils;
import com.wengmengfan.btwang.utils.PreferUtil;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import player.XLVideoPlayActivity;
import pub.devrel.easypermissions.EasyPermissions;

import static com.wengmengfan.btwang.tinker.SampleApplicationContext.context;
import static com.wengmengfan.btwang.utils.DeviceUtils.updatePlayStart;
import static xllib.FileUtils.convertFileSize;


/**
 * sayid ....
 * Created by wengmf on 2018/3/12.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ViewBoxActivity extends BaseActivity implements ViewBoxContract.View, EasyPermissions.PermissionCallbacks {

    @Inject
    ViewBoxPresenter mPresenter;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.llExit)
    LinearLayout llExit;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.size)
    TextView size;
    @BindView(R.id.sizeNum)
    TextView sizeNum;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.down_Video)
    Button downVideo;
    @BindView(R.id.play_Video)
    TextView playVideo;

    private String hrefUrl;
    private String imgUrl;
    private DownHrefBean downHrefBean;
    private String torrFile = null;
    private String videoPath;
    private int clickType;
    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                long taskId = (long) msg.obj;
                XLTaskInfo taskInfo = XLTaskHelper.instance().getTaskInfo(taskId);

                Logger.e("fileSize:" + taskInfo.mFileSize + "\n" +
                        " downSize:" + taskInfo.mDownloadSize
                        + "/sdcdnSpeed:" + convertFileSize(taskInfo.mAdditionalResDCDNSpeed)
                        + "/s filePath:" + videoPath

                );
                handler.sendMessageDelayed(handler.obtainMessage(0, taskId), 1000);
            }
        }
    };

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_viewbox;
    }

    @Override
    public void attachView() {
        mPresenter.attachView(this);
    }

    @Override
    public void detachView() {
        mPresenter.detachView();
    }

    @Override
    public void initView() {
//        XLTaskHelper.init(getApplicationContext());
        String Url = "http://www.zei8.me" + getIntent().getStringExtra("Url");
        mPresenter.Fetch_ViewBoxInfo(Url);

    }

    @Override
    public void showError(String message) {
        Logger.e("message  >>>  " + message);
    }

    @Override
    public void Fetch_ViewBoxInfo_Success(ViewBoxBean data) {
        imgUrl = data.getImgUrl();
        ImgLoadUtils.loadImage(ViewBoxActivity.this, data.getImgUrl(), img);
        title.setText(data.getAlt());
        tvTitle.setText(data.getAlt());
        size.setText(data.getSize());
        sizeNum.setText(data.getSizeNum());
        content.setText(data.getContext());
        hrefUrl = "http://www.zei8.me" + data.getHref();

    }

    @Override
    public void Fetch_HrefUrl_Success(DownHrefBean data) {
        this.downHrefBean = data;
        mPresenter.download_Zip(data);
    }

    @Override
    public void download_Zip_Success(String filePath) {
        String destFileDir = DeviceUtils.getSDPath(downHrefBean.getTitle());
        videoPath = DeviceUtils.getSDVideoPath(downHrefBean.getTitle());

        try {
            boolean jieya = ZipUtils.unzipFile(filePath, destFileDir);
            if (jieya) {
                FileUtils.deleteFile(filePath);
                List<File> files = FileUtils.listFilesInDir(destFileDir);
                for (File f : files) {
                    if (f.getAbsolutePath().endsWith(".torrent")) {
                        torrFile = f.getAbsolutePath();
                        switch (clickType) {
                            case 0:
                                PreferUtil.getInstance().setPlayPath(torrFile);
                                PreferUtil.getInstance().setPlayTitle(downHrefBean.getTitle());
                                PreferUtil.getInstance().setPlayimgUrl(imgUrl);
                                Intent intent = new Intent();
                                ComponentName componentName = new ComponentName("com.wengmengfan.btwang",
                                        "com.wengmengfan.btwang.service.DownTorrentVideoService");
                                intent.setComponent(componentName);
                                startService(intent);
                                break;

                            case 1:

                                break;

                        }
//                    XLVideoPlayActivity.intentTo(context, torrFile, downHrefBean.getTitle());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Down_Torrent_File_Success() {

    }

    @OnClick({R.id.llExit, R.id.tvTitle, R.id.down_Video, R.id.play_Video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llExit:
                this.finish();

                break;
            case R.id.down_Video:
                clickType = 0;


                if (!EasyPermissions.hasPermissions(this, perms)) {
                    EasyPermissions.requestPermissions(this, "需要读写权限", 1000, perms);
                } else {
                    mPresenter.Fetch_HrefUrl(hrefUrl);
                }
                break;
            case R.id.play_Video:
                clickType = 1;
                if (!EasyPermissions.hasPermissions(this, perms)) {
                    EasyPermissions.requestPermissions(this, "需要读写权限", 1000, perms);
                } else {
                    mPresenter.Fetch_HrefUrl(hrefUrl);
                }
                break;
            case R.id.tvTitle:

                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mPresenter.Fetch_HrefUrl(hrefUrl);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        ToastUtils.showLongToast("没有权限无法下载电影");
    }

}
