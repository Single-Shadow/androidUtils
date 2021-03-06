package com.zmp.androidutils;

import android.app.Application;

import com.zmp.utils.Logger;
import com.zmp.utils.SendEmailUtils;
import com.zmp.utils.ToastTools;

public class App extends Application {

        @Override
        public void onCreate() {
                super.onCreate();
                ToastTools.getDefault().init(getApplicationContext());
                String username = "xxx.xxx@xx.xx";
                String password = "x";
                SendEmailUtils.getInstance().init(username, password, new SendEmailUtils.ISendResult() {
                        @Override
                        public void onSuccess() {
                                Logger.d("ISendResult:onSuccess");
                        }

                        @Override
                        public void onFail(String ex) {
                                Logger.d("ISendResult:onFail:" + ex);
                        }
                });
                SendEmailUtils.getInstance().addToUser(username);
                String title = "title";
                String content = "content";
                String filePath = "fileName";
                String emailFileName = "emailFileName";
                SendEmailUtils.getInstance().sendEmail(title, content);
                SendEmailUtils.getInstance().sendEmail(title, content, filePath, emailFileName);
        }
}
