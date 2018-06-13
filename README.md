README
=======

# 使用 
* <H4>compile 'com.zmp.zhanpple:utils:1.0.1'</H4>

## 打印类名 行号的log
* public class Logger --->源码[Logger.java](https://github.com/zhanpple/androidUtils/blob/master/utils/src/main/java/com/zmp/utils/Logger.java)
### 用法
<h4>
```java
	Logger.i("onCreate"); 
	// 控制台- >  I/Logger: com.zmp.androidutils.MainActivity->>onCreate->>14:onCreate
	Logger.v("onCreate");
	// 控制台- >  V/Logger: com.zmp.androidutils.MainActivity->>onCreate->>15:onCreate
	Logger.d("onCreate"); 
	// 控制台- >  D/Logger: com.zmp.androidutils.MainActivity->>onCreate->>16:onCreate
	Logger.e("onCreate"); 
	// 控制台- >  E/Logger: com.zmp.androidutils.MainActivity->>onCreate->>17:onCreate 
	Logger.w("onCreate"); 
	// 控制台- >  W/Logger: com.zmp.androidutils.MainActivity->>onCreate->>18:onCreate
	
```
