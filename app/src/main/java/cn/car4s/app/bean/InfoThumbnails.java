package cn.car4s.app.bean;

import java.io.Serializable;

/**
 * 缩略图实体
 *
 * @author admin
 *         <p/>
 *         自己保存120*120的图片
 */
public class InfoThumbnails implements Serializable {

    public final static String TABLE_NAME = "thumbnails";
    public final static String VAR_SRCPATH = "srcPath";
    public final static String VAR_DSTPATH = "dstPath";

    private String srcPath;
    private String dstPath;

    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getDstPath() {
        return dstPath;
    }

    public void setDstPath(String dstPath) {
        this.dstPath = dstPath;
    }

}
