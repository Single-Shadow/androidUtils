README
======

# 使用 
* `compile 'com.zmp.zhanpple:utils:1.0.1'`

## 打印类名 行号的log
* public class Logger --->源码[Logger.java](https://github.com/zhanpple/androidUtils/blob/master/utils/src/main/java/com/zmp/utils/Logger.java)
### 用法
```java
	Logger.i("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils I/Logger: com.zmp.androidutils.MainActivity->>onCreate->>14:onCreate
	Logger.v("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils V/Logger: com.zmp.androidutils.MainActivity->>onCreate->>15:onCreate
	Logger.d("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils D/Logger: com.zmp.androidutils.MainActivity->>onCreate->>16:onCreate
	Logger.e("onCreate"); // - > 06-13 09:26:51.790 2160-2160/com.zmp.androidutils E/Logger: com.zmp.androidutils.MainActivity->>onCreate->>17:onCreate 
	Logger.w("onCreate"); // - >  06-13 09:26:51.790 2160-2160/com.zmp.androidutils W/Logger: com.zmp.androidutils.MainActivity->>onCreate->>18:onCreate
```
