package cn.car4s.app.util;

import android.os.Handler;
import android.util.Log;
import cn.car4s.app.AppConfig;
import cn.car4s.app.AppContext;
import cn.car4s.app.api.HttpCallback;
import cn.car4s.app.bean.NetReturnBean;
import cn.car4s.app.bean.UserBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.okhttp.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/5/12.
 */
public class NetUtil {

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Handler handler = new Handler(AppContext.getInstance().getMainLooper());
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String doGet(String request_url) throws IOException {
        Request request = new Request.Builder().url(request_url).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().toString();
    }


    public static String doPostJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().string();
    }

    static NetReturnBean bean = null;
    static String result = null;

    public static void doPostMap(String url, Map<String, String> map, final HttpCallback callback) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = builder.build();


        UserBean beantemp = UserBean.getLocalUserinfo();
        String token;
        if (beantemp == null)
            token = "";
        else {
            token = beantemp.Token;
            if (token == null) {
                token = "";
            }
        }
        final Request request = new Request.Builder().url(url).post(body).addHeader("Token", token).build();

        Callback asyncCallback = new Callback() {


            @Override
            public void onFailure(final Request request, final IOException e) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtil.showToastShort(e.toString());
//                        callback.onFailure(request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.code() != 200) {
                    LogUtil.e("response code", "" + response.code());
                    return;
                }
                if (response != null) {
                    result = response.body().string();
                }
                Log.e("onResponse", "" + result);
                bean = new Gson().fromJson(result, NetReturnBean.class);
                if (bean == null) return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtil.showToastShort(bean.Message);
                        if ("0".equals(bean.Code)) {
                            callback.onResponse(result);
                        } else if ("-9".equals(bean.Code)) {
                            ToastUtil.showToastShort(bean.Message);
                            PreferencesUtil.putPreferences(AppConfig.SP_KEY_USERINFO, "");
                        } else {
                            if (!"1".equals(bean.Code))
                                ToastUtil.showToastShort(bean.Message);
                            else callback.onFailure(request, null);
                        }
                    }
                });
            }
        };
        okHttpClient.newCall(request).enqueue(asyncCallback);
    }

    //    action	UploadHeadPicture	必填	post
//    Token	用户密钥	必填	header
//    头像图片	必填	body
    public static void uploadImg(String url, String path, AsyncHttpResponseHandler handler) {

        path = UtilImage.get450File(path);

        RequestParams params = new RequestParams();
        try {
            params.put("action", "UploadHeadPicture");
            params.put("data", new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RequestManager.post(AppContext.getInstance(), url, params, handler);
    }

//    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//
//    public static void doPostMultipart(String url, String imagePath, final HttpCallback callback) {
//        final File file = new File(imagePath);
//        final long totalSize = file.length();
//        RequestBody requestBody = new MultipartBuilder().type(MultipartBuilder.FORM)
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
//                        createCustomRequestBody(MediaType.parse("image/*"), new File(imagePath)))
////                .addPart(
////                        Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + file.getName() + "\""),
////                        new CountingFileRequestBody(file, "image/*", new CountingFileRequestBody.ProgressListener() {
////                            @Override
////                            public void transferred(long num) {
////                            }
////                        })
////                )
//                .build();
//        final Request request = new Request.Builder().url(url).post(requestBody).addHeader("Token", UserBean.getLocalUserinfo() == null ? "" : UserBean.getLocalUserinfo().Token).build();
//
//        Callback asyncCallback = new Callback() {
//
//
//            @Override
//            public void onFailure(final Request request, final IOException e) {
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showToastShort(e.toString());
////                        callback.onFailure(request, e);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                if (response != null) {
//                    result = response.body().string();
//                }
//                Log.e("onResponse", "" + result);
//                bean = new Gson().fromJson(result, NetReturnBean.class);
//                if (bean == null) return;
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
////                        ToastUtil.showToastShort(bean.Message);
//                        if ("0".equals(bean.Code)) {
//                            callback.onResponse(result);
//                        } else {
//                            ToastUtil.showToastShort(bean.Message);
//                        }
//                    }
//                });
//            }
//        };
//        okHttpClient.newCall(request).enqueue(asyncCallback);
//    }
//
//
//    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file) {
//        return new RequestBody() {
//            @Override
//            public MediaType contentType() {
//                return contentType;
//            }
//
//            @Override
//            public long contentLength() {
//                return file.length();
//            }
//
//            @Override
//            public void writeTo(BufferedSink sink) throws IOException {
//                okio.Source source = null;
//                try {
//                    source = Okio.source(file);
//                    //sink.writeAll(source);
//                    Buffer buf = new Buffer();
//                    Long remaining = contentLength();
//                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
//                        sink.write(buf, readCount);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//    }

}
