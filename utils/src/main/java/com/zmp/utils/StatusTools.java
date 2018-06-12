package com.zmp.utils;

import android.content.Context;

/**
 * Created by zmp on 2017/12/27.
 */

public class StatusTools {

        public static int getStatusBarHeight(Context context) {
                // 获取状态栏高度——方法
                int statusBarHeight = -1;
                //获取status_bar_height资源的ID
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                        //根据资源ID获取响应的尺寸值
                        statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
                }
                return statusBarHeight;
        }

}
