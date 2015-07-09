package cn.car4s.app.api;

import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public interface HttpCallback {

    void onFailure(Request request, IOException e);

    void onResponse(String result);
}
