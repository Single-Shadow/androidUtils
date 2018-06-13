package com.zmp.androidutils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.zmp.androidutils.bean.ColorBean;
import com.zmp.utils.Logger;
import com.zmp.utils.ToastTools;
import com.zmp.utils.okhttp.IProgressCallback;
import com.zmp.utils.okhttp.OkHttpUtils;
import com.zmp.utils.okhttp.ProgressDownloader;

import java.io.File;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                final ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Logger.i("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils I/Logger: com.zmp.androidutils.MainActivity->>onCreate->>14:onCreate
                Logger.v("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils V/Logger: com.zmp.androidutils.MainActivity->>onCreate->>15:onCreate
                Logger.d("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils D/Logger: com.zmp.androidutils.MainActivity->>onCreate->>16:onCreate
                Logger.e("onCreate"); // - > 06-13 09:26:51.790 2160-2160/com.zmp.androidutils E/Logger: com.zmp.androidutils.MainActivity->>onCreate->>17:onCreate
                Logger.w("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils W/Logger: com.zmp.androidutils.MainActivity->>onCreate->>18:onCreate

                String url0 = "https://raw.githubusercontent.com/zhanpple/androidUtils/master/testFile/color.json";
                String tag0 = url0;
                OkHttpUtils.getInstance().get(url0, tag0, new OkHttpUtils.MyCallBack<String>() {
                        @Override
                        public void onResponse(String string) {
                                ToastTools.getDefault().show("colorBean:" +string);
                        }

                        @Override
                        public Class<String> getClazz() {
                                return String.class;
                        }

                        @Override
                        public void onFailure(String s) {
                                Logger.e(s);
                                ToastTools.getDefault().show("onFailure:" + s);
                        }
                });

                String url = "https://raw.githubusercontent.com/zhanpple/androidUtils/master/testFile/color.json";
                String tag = url;
                OkHttpUtils.getInstance().get(url, tag, new OkHttpUtils.MyCallBack<ColorBean>() {
                        @Override
                        public void onResponse(ColorBean colorBean) {
                                ToastTools.getDefault().show("colorBean:" + colorBean.getColor());
                        }

                        @Override
                        public Class<ColorBean> getClazz() {
                                return ColorBean.class;
                        }

                        @Override
                        public void onFailure(String s) {
                                Logger.e(s);
                                ToastTools.getDefault().show("onFailure:" + s);
                        }
                });



                String url2 = " https://raw.githubusercontent.com/zhanpple/androidUtils/master/testFile/ic_launcher.png";
                String tag2 = url;
                OkHttpUtils.getInstance().get(url2, tag2, new OkHttpUtils.MyCallBack<Bitmap>() {
                        @Override
                        public void onResponse(final Bitmap bitmap) {
                                runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                imageView.setImageBitmap(bitmap);
                                        }
                                });
                        }

                        @Override
                        public Class<Bitmap> getClazz() {
                                return Bitmap.class;
                        }

                        @Override
                        public void onFailure(String s) {
                                Logger.e(s);
                                ToastTools.getDefault().show("onFailure:" + s);
                        }
                });
                String url3 = " https://raw.githubusercontent.com/zhanpple/androidUtils/master/testFile/ic_launcher.png";
                File file = new File(Environment.getExternalStorageDirectory(), "ic_launcher.png");
                ProgressDownloader progressDownloader = new ProgressDownloader(url3, file, new IProgressCallback() {
                        @Override
                        public void onLoading(float current, float total) {
                                float percent;
                                if (total == 0) {
                                        percent = 100;
                                }
                                else {
                                        percent = current * 100.0F / total;
                                }
                                Log.e("TAG", "current:" + current);
                                Log.e("TAG", "total:" + total);
                                String format = String.format("下载进度%1$.2f%2$s".toLowerCase(), percent, "%");
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailed(String message) {

                        }

                        @Override
                        public void onSave(long startsPoint, int len) {
                                Log.e("MapTaskActivity", "onSave" + (startsPoint + len));
                        }
                });
                progressDownloader.download(0);
                progressDownloader.pause();
        }
}
