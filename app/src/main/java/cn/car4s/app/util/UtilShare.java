package cn.car4s.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import cn.car4s.app.AppConfig;
import cn.car4s.app.AppContext;
import cn.car4s.app.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by changxuebo on 14-9-2.
 */
public class UtilShare {

    private static final String textprefix = "快来下载指尖创业吧，我的推荐码是 ";
    private static final String title = "我在用指尖创业";
    private static final String shareurl = "http://fir.im/zjcy/";

//    public static void shareSina(CarSourceDetialEntity shareEntity, int type) {
//        ShareSDK.initSDK(AppContext.getInstance());
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle(shareEntity.car_name);
//        String shareUrl = null;
//        if (type == U.TYPE_CARSEARCH) {
//            shareUrl = "http://fbautoapp.fblife.com/index.php?c=web&a=singleXunche&xid=" + shareEntity.id;
//        } else {
//            shareUrl = "http://fbautoapp.fblife.com/index.php?c=web&a=singleCheyuan&cid=" + shareEntity.id;
//        }
//        sp.setText(textprefix + "( " + shareEntity.car_name + " ) " + shareUrl);
//
//        if (shareEntity.image != null && shareEntity.image.size() > 0) {
//            File file = DiskCacheUtils.findInCache(shareEntity.image.get(0).link, AppContext.AppDiskCache);
//            if (file != null && file.exists()) {
//                sp.setImagePath(file.toString());
//            } else {
//                sp.setImagePath(getBMAppIcon());
//            }
//        } else {
//            sp.setImagePath(getBMAppIcon());
//        }
//
//        Platform weibo = ShareSDK.getPlatform(AppContext.getInstance(), SinaWeibo.NAME);
//        weibo.share(sp);
//    }

    public static void shareWXFriend(String code) {
        ShareSDK.initSDK(AppContext.getInstance());
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(textprefix + " " + code + " ");
        sp.setUrl(shareurl);
//        if (image != null) {
//            File file = DiskCacheUtils.findInCache(shareEntity.image.get(0).link, AppContext.AppDiskCache);
//            if (file != null && file.exists()) {
//                sp.setImagePath(file.toString());
//            } else {
//                sp.setImagePath(getBMAppIcon());
//            }
//        } else {
//            sp.setImagePath(getBMAppIcon());
//        }
        sp.setImagePath(getBMAppIcon());
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform weibo = ShareSDK.getPlatform(AppContext.getInstance(), Wechat.NAME);
        weibo.share(sp);
    }

//
//    public static void shareWXCircle(CarSourceDetialEntity shareEntity, int type) {
//        ShareSDK.initSDK(AppContext.getInstance());
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle(shareEntity.car_name);
//        sp.setText(textprefix + "( " + shareEntity.car_name + " )");
//        String shareUrl = null;
//        if (type == U.TYPE_CARSEARCH) {
//            shareUrl = "http://fbautoapp.fblife.com/index.php?c=web&a=singleXunche&xid=" + shareEntity.id;
//        } else {
//            shareUrl = "http://fbautoapp.fblife.com/index.php?c=web&a=singleCheyuan&cid=" + shareEntity.id;
//        }
//        sp.setUrl(shareUrl);
//        if (shareEntity.image != null && shareEntity.image.size() > 0) {
//            File file = DiskCacheUtils.findInCache(shareEntity.image.get(0).link, AppContext.AppDiskCache);
//            if (file != null && file.exists()) {
//                sp.setImagePath(file.toString());
//            } else {
//                sp.setImagePath(getBMAppIcon());
//            }
//        } else {
//            sp.setImagePath(getBMAppIcon());
//        }
//
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//
//        Platform weibo = ShareSDK.getPlatform(AppContext.getInstance(), WechatMoments.NAME);
//        weibo.share(sp);
//    }


    public static void shareQQ(String code) {
        ShareSDK.initSDK(AppContext.getInstance());
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(title);
        sp.setText(textprefix + " " + code + " ");
        sp.setUrl(shareurl);
        sp.setTitleUrl(shareurl);
        sp.setImagePath(getBMAppIcon());
//        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform qq = ShareSDK.getPlatform(AppContext.getInstance(), QQ.NAME);
        qq.share(sp);
    }

//
//    public static void shareZhannei(CarSourceDetialEntity shareEntity, int type, Context context) {
//        String content = textprefix + "( " + shareEntity.car_name + " )";
//        Intent intent = new Intent();
//        intent.setClass(context, ShareZhanneiActivity.class);
//        intent.putExtra(U.EXT_LIST, content);
//        intent.putExtra(U.EXT_ZHANNEI_SHARELINK, shareEntity.id + "," + shareEntity.car);
//        intent.putExtra(U.EXT_ZHANNEI_SHARETYPE, type);
//        context.startActivity(intent);
//    }


    public static String FILE_NAME = "ic_logo.jpg";

    private static String getBMAppIcon() {
        String TEST_IMAGE;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && Environment.getExternalStorageDirectory().exists()) {
                TEST_IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppConfig.DIR_IMAGELOADER_CACHE + "/" + FILE_NAME;
            } else {
                TEST_IMAGE = AppContext.getInstance().getFilesDir().getAbsolutePath() + "/" + AppConfig.DIR_IMAGELOADER_CACHE + "/" + FILE_NAME;
            }
            File file = new File(TEST_IMAGE);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(AppContext.getInstance().getResources(), R.mipmap.ic_logo);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            TEST_IMAGE = null;
        }
        return TEST_IMAGE;
    }


}
