package cn.car4s.app.ui.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.car4s.app.AppConfig;
import cn.car4s.app.R;
import cn.car4s.app.bean.UserBean;
import cn.car4s.app.bean.WebviewBean;
import cn.car4s.app.util.UtilShare;

/**
 * Description:֧��js�ӿڵ���
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/5/13.
 */
public class WebviewActivity extends BaseActivity implements IBase {

    @InjectView(R.id.btn_actionbar_back_img)
    ImageView mActionbarBack;
    @InjectView(R.id.tv_actionbar_title)
    TextView mActionbarTitle;
    @InjectView(R.id.webview)
    WebView webview;


    @InjectView(R.id.share_layout)
    LinearLayout mShareLayout;
    @InjectView(R.id.share_qq)
    TextView mShareBtnQQ;
    @InjectView(R.id.share_weixin)
    TextView mShareBtnWX;

    WebviewBean bean = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.inject(this);
        initData();
        initUI();
    }

    @Override
    public void initUI() {
        mActionbarBack.setVisibility(View.VISIBLE);
        mActionbarBack.setImageResource(R.mipmap.ic_loginactivity_back);
        mActionbarBack.setOnClickListener(onClickListener);
        mActionbarTitle.setText(bean.webTitle);
        if (bean.isShare) {
            mShareLayout.setVisibility(View.VISIBLE);
            mShareBtnWX.setOnClickListener(onClickListener);
            mShareBtnQQ.setOnClickListener(onClickListener);
        }
        initWebview();
    }

    public void initWebview() {
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUserAgentString(
                "Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();// 接受证书
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

                Toast.makeText(getApplicationContext(), "页面加载失败！" + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String _url) {
                super.onPageFinished(view, _url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;

            }
        });
        webview.loadUrl(bean.loadUrl);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_actionbar_back_img:
                    finish();
                    break;
                case R.id.share_qq:
                    if (UserBean.checkUserLoginStatus()) {
                        mUserbean = UserBean.getLocalUserinfo();
                        UtilShare.shareQQ(mUserbean.ReferralCode_I);
                    } else {
                        UserBean.toLogin(WebviewActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                    }
                    break;
                case R.id.share_weixin:
                    if (UserBean.checkUserLoginStatus()) {
                        mUserbean = UserBean.getLocalUserinfo();
                        UtilShare.shareWXFriend(mUserbean.ReferralCode_I);
                    } else {
                        UserBean.toLogin(WebviewActivity.this, AppConfig.REQUEST_CODE_LOGIN);
                    }
                    break;
            }
        }
    };

    @Override
    public void initData() {
        bean = (WebviewBean) getIntent().getSerializableExtra(AppConfig.INTENT_PARA_KEY_BEAN);
        if (bean == null) {
            finish();
        }
    }
}
