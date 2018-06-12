package com.zmp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zmp on 2017/4/8.
 */

public class OkHttpUtils {

        private static OkHttpClient mOkHttpClient;

        private static OkHttpUtils okHttpUtils;

        private static final String TAG = "OkHttpUtils";

        public static OkHttpUtils getInstance() {
                if (okHttpUtils == null) {
                        okHttpUtils = new OkHttpUtils();
                }
                return okHttpUtils;
        }

        private static final int CONNECT_TIMEOUT = 5000;

        private static final int READ_TIMEOUT = 5000;

        private static final int WRITE_TIMEOUT = 5000;

        private OkHttpUtils() {
                mOkHttpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS).readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS).writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS).addInterceptor(new Interceptor(){
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {

                                okhttp3.Response response = chain.proceed(chain.request());
                                //将ResponseBody转换成我们需要的FileResponseBody
                                return response.newBuilder().body(new FileResponseBody(response.body(), new IProgressCallback(){
                                        @Override
                                        public void onLoading(float current, float total) {
                                                Log.e("test", current * 100.0 / total + "%");
                                        }

                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailed(String message) {

                                        }

                                        @Override
                                        public void onSave(long startsPoint, int len) {

                                        }
                                })).build();
                        }
                }).build();
        }

        public void getString(String url, Object tag, final MyCallBack<String> myCallBack) {
                Request request = new Request.Builder().url(url).get().tag(tag).build();
                mOkHttpClient.newCall(request).enqueue(new Callback(){
                        @Override
                        public void onResponse(Call arg0, Response response) throws IOException {
                                myCallBack.onResponse(response.body().string());
                        }

                        @Override
                        public void onFailure(Call arg0, IOException arg1) {
                                myCallBack.onFailure(arg1.getMessage());
                        }
                });
        }

        public void getBitMap(String url, Object tag, final MyCallBack<Bitmap> myCallBack) {
                Request request = new Request.Builder().url(url).get().tag(tag).build();
                mOkHttpClient.newCall(request).enqueue(new Callback(){
                        @Override
                        public void onResponse(Call arg0, Response response) throws IOException {
                                Log.e("test", "onResponse:" + response.isSuccessful());
                                if (myCallBack != null) {
                                        myCallBack.onResponse(BitmapFactory.decodeStream(response.body().byteStream()));
                                }
                        }

                        @Override
                        public void onFailure(Call arg0, IOException arg1) {
                                if (myCallBack != null) {
                                        myCallBack.onFailure(arg1.getMessage());
                                }
                        }
                });
        }

        public void get(String httpOrs, String host, String[] pathSegment, Map<String, String> map, Object tag) {
                HttpUrl.Builder https = new HttpUrl.Builder().scheme(httpOrs).host(host);
                if (pathSegment != null && pathSegment.length > 0) {
                        for (String s : pathSegment) {
                                Log.e("test", s);
                                https.addPathSegment(s);
                        }
                }
                if (map != null && map.size() > 0) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                                https.addQueryParameter(entry.getKey(), entry.getValue());
                        }
                }
                Request request = new Request.Builder().url(https.build()).tag(tag).get().build();
                mOkHttpClient.newCall(request).enqueue(new Callback(){
                        @Override
                        public void onResponse(Call arg0, Response response) throws IOException {
                                Log.e("test", response.body().string());
                        }

                        @Override
                        public void onFailure(Call arg0, IOException arg1) {
                                Log.e("test", arg1.getMessage());
                        }
                });
        }

        public void get(Map<String, String> map) {
                HttpUrl.Builder builder = new HttpUrl.Builder().scheme("https").host("api.weixin.qq.com").addPathSegment("device").addPathSegment("getqrcode");
                for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        builder.addQueryParameter(key, value);
                }
                Request request = new Request.Builder().url(builder.build()).get().build();
                mOkHttpClient.newCall(request).enqueue(new Callback(){
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }
                });
        }

        public synchronized void cancel(Object tag) {
                if (tag != null) {
                        return;
                }
                for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                        if (tag.equals(call.request().tag())) {
                                call.cancel();
                        }
                }
                for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                        if (tag.equals(call.request().tag())) {
                                call.cancel();
                        }
                }
        }

        public interface MyCallBack<T>{

                void onResponse(T t);

                void onFailure(String s);
        }


        /**
         * 上传文件
         *
         * @param actionUrl 接口地址
         * @param paramsMap 参数
         * @param callBack  回调
         */
        public static void postForm(String actionUrl, HashMap<String, ? extends Object> paramsMap, final OkHttpCallBack callBack) {
                try {
                        //补全请求地址
                        //String requestUrl = "";//String.format("%checkStyle.xml/%checkStyle.xml", upload_head, actionUrl);
                        MultipartBody.Builder builder = new MultipartBody.Builder();
                        //设置类型
                        builder.setType(MultipartBody.FORM);
                        //追加参数
                        for (String key : paramsMap.keySet()) {
                                Object object = paramsMap.get(key);
                                Log.e(TAG, "file: " + object);
                                if (null == object) {
                                        continue;
                                }
                                if (!(object instanceof File)) {
                                        builder.addFormDataPart(key, object.toString());
                                } else {
                                        File file = (File) object;
                                        Log.v(TAG, "key" + key + "file: " + file.getName());
                                        String uploadType = "application/*";//二进制
                                        if (key.equals("audioFile")) {
                                                uploadType = "audio/*";
                                        } else if (key.equals("imageFile")) {
                                                uploadType = "image/*";
                                        }
                                        builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(uploadType), file));
                                }
                        }
                        //创建RequestBody
                        RequestBody body = builder.build();
                        //创建Request
                        final Request request = new Request.Builder().url(actionUrl).post(body).build();
                        //单独设置参数 比如读取超时时间
                        mOkHttpClient.newCall(request).enqueue(new Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                        Log.e(TAG, "response ----->" + e.toString());
                                        callBack.onFailure();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                                String string = response.body().string();
                                                Log.e(TAG, "response ----->" + string);
                                                callBack.onSuccess(string);
                                        } else {
                                                callBack.onFailure();
                                        }
                                }
                        });
                } catch (Exception e){
                }
        }

        public interface OkHttpCallBack{

                void onSuccess(String result);

                void onFailure();
        }


        //带进度监听功能的辅助类   断点下载
        public static class ProgressDownloader{

                public static final String TAG = "ProgressDownloader";

                private IProgressCallback progressListener;

                private String url;

                private OkHttpClient client;

                private File destination;

                private Call call;

                public ProgressDownloader(String url, File destination, IProgressCallback progressListener) {
                        this.url = url;
                        this.destination = destination;
                        this.progressListener = progressListener;
                        //在下载、暂停后的继续下载中可复用同一个client对象
                        client = getProgressClient();
                }

                //每次下载需要新建新的Call对象
                private Call newCall(long startPoints) {
                        Request request = new Request.Builder().url(url).header("RANGE", "bytes=" + startPoints + "-")//断点续传要用到的，指示下载的区间
                                .build();
                        return client.newCall(request);
                }

                public OkHttpClient getProgressClient() {  // 拦截器，用上ProgressResponseBody
                        Interceptor interceptor = new Interceptor(){
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                        Response originalResponse = chain.proceed(chain.request());
                                        return originalResponse.newBuilder().body(new FileResponseBody(originalResponse.body(), progressListener)).build();
                                }
                        };
                        return new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();
                }

                // startsPoint指定开始下载的点
                public void download(final long startsPoint) {
                        Log.e(TAG, "download:" + startsPoint);
                        call = newCall(startsPoint);
                        call.enqueue(new Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                        Log.e(TAG, e.getMessage());
                                        progressListener.onFailed(e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                        save(response, startsPoint);
                                }
                        });
                }

                public void pause() {
                        if (call != null) {
                                call.cancel();
                        }
                }

                private void save(Response response, long startsPoint) {
                        ResponseBody body = response.body();
                        InputStream in = body.byteStream();
                        FileChannel channelOut = null;
                        // 随机访问文件，可以指定断点续传的起始位置
                        RandomAccessFile randomAccessFile = null;
                        boolean isSuccess = false;
                        try {
                                randomAccessFile = new RandomAccessFile(destination, "rwd");
                                //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
                                channelOut = randomAccessFile.getChannel();      // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
                                long size = body.contentLength();
                                Log.e(TAG, "size:" + size);
                                Log.e(TAG, "startsPoint:" + startsPoint);
                                MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, size);
                                byte[] buffer = new byte[1024];
                                int len;
                                int count = 0;
                                while ((len = in.read(buffer)) != -1) {
                                        mappedBuffer.put(buffer, 0, len);
                                        count += len;
                                        progressListener.onSave(startsPoint, count);
                                }
                                isSuccess = true;
                        } catch (IOException e){
                                e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                                isSuccess = false;
                                progressListener.onFailed(e.getMessage());
                        } finally {
                                try {
                                        in.close();
                                        if (channelOut != null) {
                                                channelOut.close();
                                        }
                                        if (randomAccessFile != null) {
                                                randomAccessFile.close();
                                        }
                                } catch (IOException e){

                                        e.printStackTrace();
                                }
                        }
                        if (isSuccess) {
                                progressListener.onSuccess();
                        }
                }

        }

        /**
         * ss
         */
        public interface IProgressCallback{

                void onLoading(float current, float total);

                void onSuccess();

                void onFailed(String message);

                void onSave(long startsPoint, int len);
        }

        static final class FileResponseBody extends ResponseBody{

                /**
                 * 实际请求体
                 */
                private ResponseBody mResponseBody;

                /**
                 * 下载回调接口
                 */
                private IProgressCallback mCallback;

                /**
                 * BufferedSource
                 */
                private BufferedSource mBufferedSource;

                private FileResponseBody(ResponseBody responseBody, IProgressCallback callback) {
                        super();
                        this.mResponseBody = responseBody;
                        this.mCallback = callback;
                }

                @Override
                public BufferedSource source() {

                        if (mBufferedSource == null) {
                                mBufferedSource = Okio.buffer(source(mResponseBody.source()));
                        }
                        return mBufferedSource;
                }

                @Override
                public long contentLength() {
                        return mResponseBody.contentLength();
                }

                @Override
                public MediaType contentType() {
                        return mResponseBody.contentType();
                }

                /**
                 * 回调进度接口
                 *
                 * @param source dd
                 * @return Source
                 */
                private Source source(Source source) {
                        return new ForwardingSource(source){
                                volatile float totalBytesRead = 0L;

                                @Override
                                public synchronized long read(Buffer sink, long byteCount) throws IOException {
                                        float bytesRead;
                                        bytesRead = super.read(sink, byteCount);
                                        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                                        Log.e(TAG, "read: " + totalBytesRead + ",bytesRead:" + bytesRead);
                                        mCallback.onLoading(totalBytesRead, mResponseBody.contentLength());
                                        return (long) bytesRead;
                                }
                        };
                }
        }
}
