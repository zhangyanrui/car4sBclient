package cn.car4s.app.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件夹实体
 *
 * @author admin
 */
public class InfoBucket implements Serializable {

    private String name;

    private int imageCount;

    private String path;

    private String bucketId;//根据这个来得到文件夹下的所有图片

    private List<InfoImages> images = new ArrayList<InfoImages>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<InfoImages> getImages() {
        return images;
    }

    public void setImages(List<InfoImages> images) {
        this.images = images;
    }

    public void addImages(InfoImages image) {

        this.images.add(image);

    }


    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    @Override
    public String toString() {
        return "Bucket [name=" + name + ", imageCount=" + imageCount
                + ", path=" + path + ", images=" + images + "]";
    }


}
