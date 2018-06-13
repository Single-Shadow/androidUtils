package com.zmp.androidutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zmp.androidutils.bean.ColorBean;
import com.zmp.utils.IPAndMacUtils;
import com.zmp.utils.Logger;
import com.zmp.utils.okhttp.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                Logger.i("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils I/Logger: com.zmp.androidutils.MainActivity->>onCreate->>14:onCreate
                Logger.v("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils V/Logger: com.zmp.androidutils.MainActivity->>onCreate->>15:onCreate
                Logger.d("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils D/Logger: com.zmp.androidutils.MainActivity->>onCreate->>16:onCreate
                Logger.e("onCreate"); // - > 06-13 09:26:51.790 2160-2160/com.zmp.androidutils E/Logger: com.zmp.androidutils.MainActivity->>onCreate->>17:onCreate
                Logger.w("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils W/Logger: com.zmp.androidutils.MainActivity->>onCreate->>18:onCreate
                IPAndMacUtils.getLocalIpAddress();

                OkHttpUtils.MyCallBack<ColorBean> callBack = new OkHttpUtils.MyCallBack<ColorBean>() {
                        @Override
                        public void onResponse(ColorBean o) {

                        }

                        @Override
                        public Class<ColorBean> getClazz() {
                                return ColorBean.class;
                        }

                        @Override
                        public void onFailure(String s) {

                        }
                };
                
//                OkHttpUtils.getInstance().get();
        }
}
