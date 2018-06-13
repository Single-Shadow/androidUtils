README
======

# 使用 
* <H3>compile 'com.zmp.zhanpple:utils:1.0.1'</H3>

## 打印类名 方法 行号的[logger源码](https://github.com/zhanpple/androidUtils/blob/master/utils/src/main/java/com/zmp/utils/Logger.java)
### 用法
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

## 发送有邮件工具类 [SendEmailUtils源码](https://github.com/zhanpple/androidUtils/blob/master/utils/src/main/java/com/zmp/utils/SendEmailUtils.java)
### 用法
```java

	String username = "xxx.xxx@xx.xx";
	String password = "xxxxxxxxxxx";
	//初始化用户名密码(使用账户需开通SMTP服务)
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
	//添加收件人
	SendEmailUtils.getInstance().addToUser(toUsername1);
	SendEmailUtils.getInstance().addToUser(toUsername2);
	......
	......
	SendEmailUtils.getInstance().addToUser(toUsername);

	//发送邮件
	SendEmailUtils.getInstance().sendEmail(title,content);
	//发送带附件的邮件
	SendEmailUtils.getInstance().sendEmail(title,content,filePath,emailFileName);
		
```

## OkHttp工具类 [SendEmailUtils源码](https://github.com/zhanpple/androidUtils/blob/master/utils/src/main/java/com/zmp/utils/SendEmailUtils.java)
### 用法