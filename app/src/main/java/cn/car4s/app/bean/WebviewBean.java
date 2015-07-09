package cn.car4s.app.bean;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/4/22.
 */
public class WebviewBean extends BaseBean {
    public String webTitle;
    public String loadUrl;
    public boolean isShare;

    public WebviewBean(String webTitle, String loadUrl, boolean isShare) {
        this.webTitle = webTitle;
        this.loadUrl = loadUrl;
        this.isShare = isShare;
    }
}
