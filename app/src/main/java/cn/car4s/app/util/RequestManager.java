package cn.car4s.app.util;

import android.content.Context;
import cn.car4s.app.bean.UserBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class RequestManager {

    private static AsyncHttpClient client;

    public static AsyncHttpClient getAsyncClient() {
        if (client == null) {
            client = new AsyncHttpClient();
            client.setTimeout(1000 * 30);
        }
        client.addHeader("Token", UserBean.getLocalUserinfo() == null ? "" : UserBean.getLocalUserinfo().Token);
        return client;
    }

//    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        client.get(url, params, responseHandler);
//    }

    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getAsyncClient().post(context, url, params, responseHandler);
    }

    public static void destoryRequest(Context context) {
        getAsyncClient().cancelRequests(context, true);
    }

}
