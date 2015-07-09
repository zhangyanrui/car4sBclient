package cn.car4s.app.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import cn.car4s.app.bean.InfoBucket;
import cn.car4s.app.bean.InfoImages;
import cn.car4s.app.ui.activity.BaseActivity;

import java.io.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

public class UtilImage {
    private final static String TAG = UtilImage.class.getSimpleName();

    public final static int BMP_160 = 1;// 160
    public final static int BMP_450 = 2;// 450

    public static final int SHOW_CAM = 10;// 照相机获取图片
    public static final int SHOW_LIB = 11;// 从本地获取图片

    // 450图片的bitmap缓存
    private static HashMap<String, SoftReference<Bitmap>> imageCache_bmp = new HashMap<String, SoftReference<Bitmap>>();
    // 450图片的filepath缓存
    private static HashMap<String, String> imageCache_file = new HashMap<String, String>();
    // 160图片的bitmap缓存
    private static HashMap<String, SoftReference<Bitmap>> imageCache_bmp_160 = new HashMap<String, SoftReference<Bitmap>>();
    // 160图片的filepath缓存
    private static HashMap<String, String> imageCache_file_160 = new HashMap<String, String>();
    // 下载中的url缓存
    private static ArrayList<String> imageCache_down = new ArrayList<String>();
    // 主线程的handler
    private static Handler h = new Handler(Looper.getMainLooper());

    /**
     * 从缓存以及本地获得图片 160的图片
     *
     * @param url
     * @param dir
     * @return 注意判断返回值是否为null
     */
    public static Bitmap getBitmap_160(String url, String dir) {
        return getBitmap(url, dir, BMP_160);
    }

    /**
     * 从缓存以及本地获得图片 450的图片
     *
     * @param url
     * @param dir
     * @return 注意判断返回值是否为null
     */
    public static Bitmap getBitmap(String url, String dir) {
        return getBitmap(url, dir, BMP_450);
    }

    /**
     * 从网络下载图片 160的图片
     * <p/>
     * true 说明url不为空 false url为空
     *
     * @param url
     * @param dir
     * @param callback
     */
    public static boolean getBitmap_160(String url, String dir,
                                        ImageCallback callback) {
        return getBitmap(url, dir, callback, BMP_160);
    }

    /**
     * 从网络下载图片 450的图片
     * <p/>
     * true 说明url不为空 false url为空
     *
     * @param url
     * @param dir
     * @param callback
     */
    public static boolean getBitmap(String url, String dir,
                                    ImageCallback callback) {
        return getBitmap(url, dir, callback, BMP_450);
    }

    /**
     * 删除bitmap缓存 主要用在我的衣橱或者每日装扮的删除功能
     *
     * @param filePath
     */
    public static void removeBitmap(String filePath) {
        if (filePath != null && !"".equals(filePath)) {
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            imageCache_bmp_160.remove(fileName);
            imageCache_bmp.remove(fileName);
        }
    }

    /**
     * 从缓存以及本地获得图片
     *
     * @param url
     * @param dir
     * @return
     */
    private static Bitmap getBitmap(String url, String dir, int bmp_size) {
        Bitmap bmp = null;
        boolean isNext = true;
        if (url != null && !url.equals("")) {
            String fileName = url.substring(url.lastIndexOf("/") + 1);

            // 1、从缓存看bitmap是否存在
            SoftReference<Bitmap> softReference = null;
            switch (bmp_size) {
                case BMP_160:
                    softReference = imageCache_bmp_160.get(fileName);
                    break;
                case BMP_450:
                    softReference = imageCache_bmp.get(fileName);
                    break;
            }

            if (softReference != null) {
                bmp = softReference.get();
                if (bmp != null && !bmp.isRecycled()) {
                    addCache(fileName, null, bmp, bmp_size);
                    isNext = false;
                } else {
                    imageCache_bmp.remove(fileName);
                }
            }
            // 2、从缓存中看文件路径是否存在
            if (isNext) {
                String filePath = null;
                switch (bmp_size) {
                    case BMP_160:
                        filePath = imageCache_file_160.get(fileName);
                        break;
                    case BMP_450:
                        filePath = imageCache_file.get(fileName);
                        break;

                }
                if (filePath != null && !"".equals(filePath)) {
                    if (bmp_size == BMP_450 && dir.equals(UtilFile.DIR_CACHE)) {// 得到450的我的衣橱图片，因为原图都很大
                        // 需要缩放
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        bmp = BitmapFactory.decodeFile(filePath, options);
                        bmp = null;

                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inPurgeable = true;
                        options.inInputShareable = true;
                        int scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_SHOW);
                        options.inSampleSize = scale;
                        bmp = BitmapFactory.decodeFile(filePath, options);
                    } else {
                        bmp = BitmapFactory.decodeFile(filePath);
                    }
                    if (bmp != null) {
                        addCache(fileName, filePath, bmp, bmp_size);
                        isNext = false;
                    }
                } else {
                    imageCache_file.remove(fileName);
                }
            }
            // 3、判断文件名是否存在
            if (isNext) {
                String filePath = null;
                switch (bmp_size) {
                    case BMP_160:
                        filePath = UtilFile.getFolderPath(dir) + "/" + fileName
                                + UtilFile.IMG_BAK;
                        break;
                    case BMP_450:
                        filePath = UtilFile.getFolderPath(dir) + "/" + fileName;
                        break;
                }
                if (UtilFile.isFileExist(filePath)) {
                    if (bmp_size == BMP_450 && dir.equals(UtilFile.DIR_CACHE)) {// 得到450的我的衣橱图片，因为原图都很大
                        // 需要缩放
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        bmp = BitmapFactory.decodeFile(filePath, options);
                        bmp = null;

                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inPurgeable = true;
                        options.inInputShareable = true;
                        int scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_SHOW);
                        options.inSampleSize = scale;
                        bmp = BitmapFactory.decodeFile(filePath, options);
                    } else {
                        bmp = BitmapFactory.decodeFile(filePath);
                    }
                    if (bmp != null) {
                        addCache(fileName, filePath, bmp, bmp_size);
                        isNext = false;
                    } else {
                        UtilFile.deleteFile(filePath);
                    }
                }
            }
        }
        return bmp;
    }

    /**
     * 从网络下载图片
     * <p/>
     * true 说明url不为空 false url为空
     *
     * @param url
     * @param dir
     * @param callback
     */
    public static boolean getBitmap(String url, String dir,
                                    ImageCallback callback, int bmp_size) {
        // 4、url从网络下载
        if (url != null && !url.equals("") && url.contains("http://")) {
            if (!imageCache_down.contains(url)) {
//                loadDrawable(url, dir, callback, bmp_size);
            }
            return true;
        }
        return false;
    }

    private static void addCache(String fileName, String filePath,
                                 Bitmap bitmap, int bmp_size) {
        switch (bmp_size) {
            case BMP_160:
                imageCache_bmp_160.put(fileName, new SoftReference<Bitmap>(bitmap));
                if (filePath != null && !"".equals(filePath)) {
                    imageCache_file_160.put(fileName, filePath);
                }
                break;
            case BMP_450:
                imageCache_bmp.put(fileName, new SoftReference<Bitmap>(bitmap));
                if (filePath != null && !"".equals(filePath)) {
                    imageCache_file.put(fileName, filePath);
                }
                break;
        }

    }

//    private static void loadDrawable(final String imageUrl, final String dir,
//                                     final ImageCallback imageCallback, final int bmp_size) {
//
//        imageCache_down.add(imageUrl);
//
//        UtilThreadP.getInstance().getPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                HttpClient client = new DefaultHttpClient();
//                HttpGet get = new HttpGet(imageUrl);
//                HttpResponse response;
//                String tempName = null;
//                switch (bmp_size) {
//                    case BMP_160:
//                        if (dir.equals(UtilFile.DIR_CACHE)) {// 我的衣橱等的图片 先下载原图 然后
//                            // 后面生成一张160的bak
//                            tempName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
//                        } else {
//                            tempName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
//                                    + UtilFile.IMG_BAK;
//                        }
//                        break;
//                    case BMP_450:
//                        tempName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
//                        break;
//                }
//                final String fileName = tempName;
//
//                FileOutputStream fileOutputStream = null;
//                try {
//                    response = client.execute(get);
//                    HttpEntity entity = response.getEntity();
//                    InputStream is = entity.getContent();
//                    File file = null;
//                    String root = null;
//                    if (is != null) {
//                        if (dir.equals(UtilFile.DIR_CACHE_160)) {
//                            root = UtilFile
//                                    .getFolderPath(UtilFile.DIR_CACHE_160);
//                        } else if (dir.equals(UtilFile.DIR_CACHE_450)) {
//                            root = UtilFile
//                                    .getFolderPath(UtilFile.DIR_CACHE_450);
//                        } else if (dir.equals(UtilFile.DIR_PORTRAIT)) {
//                            root = UtilFile
//                                    .getFolderPath(UtilFile.DIR_PORTRAIT);
//                        } else if (dir.equals(UtilFile.DIR_PORTRAIT_180)) {
//                            root = UtilFile
//                                    .getFolderPath(UtilFile.DIR_PORTRAIT_180);
//                        } else if (dir.equals(UtilFile.DIR_ACTIVITY)) {
//                            root = UtilFile
//                                    .getFolderPath(UtilFile.DIR_ACTIVITY);
//                        } else if (dir.equals(UtilFile.DIR_CACHE)) {
//                            root = UtilFile.getFolderPath(UtilFile.DIR_CACHE);
//                        } else if (dir.equals(UtilFile.DIR_APP)) {
//                            root = UtilFile.getFolderPath(UtilFile.DIR_APP);
//                        }
//
//                        file = new File(root + "/" + fileName);
//                        fileOutputStream = new FileOutputStream(file);
//
//                        byte[] buf = new byte[1024];
//                        int ch = -1;
//                        while ((ch = is.read(buf)) != -1) {
//                            fileOutputStream.write(buf, 0, ch);
//                        }
//                    }
//                    fileOutputStream.flush();
//                    if (file == null) {
//                        return;
//                    }
//
//                    final String filePath = file.getAbsolutePath();
//
//                    if (dir.equals(UtilFile.DIR_CACHE)) {
//                        Bitmap tempBitmap = null;
//                        if (bmp_size == BMP_160) {// 生成160bak图片
//                            // 还需要在保存一张.bak的文件
//                            File fileBak = new File(filePath + UtilFile.IMG_BAK);
//                            if (!fileBak.exists()) {
//                                BitmapFactory.Options options = new BitmapFactory.Options();
//                                options.inJustDecodeBounds = true;// 只得到图片大小信息
//                                // 不分配内存
//                                tempBitmap = BitmapFactory.decodeFile(filePath,
//                                        options);
//                                options.inJustDecodeBounds = false;
//                                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                                options.inPurgeable = true;
//                                options.inInputShareable = true;
//                                int scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_MIN);
//                                options.inSampleSize = scale;// 设置缩放级别
//                                tempBitmap = BitmapFactory.decodeFile(filePath,
//                                        options);
//                                try {
//
//                                    if (tempBitmap != null) {
//                                        FileOutputStream out = new FileOutputStream(
//                                                fileBak);
//                                        tempBitmap.compress(
//                                                CompressFormat.PNG, 100,
//                                                out);// 复制图片到缓存目录
//                                        out.flush();
//                                        out.close();
//                                    }
//                                } catch (FileNotFoundException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                tempBitmap = BitmapFactory.decodeFile(fileBak
//                                        .getAbsolutePath());
//                            }
//                        } else {// 根据原图 生成450的图片
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inJustDecodeBounds = true;// 只得到图片大小信息 不分配内存
//                            tempBitmap = BitmapFactory.decodeFile(filePath,
//                                    options);
//                            options.inJustDecodeBounds = false;
//                            options.inPreferredConfig = Bitmap.Config.RGB_565;
//                            options.inPurgeable = true;
//                            options.inInputShareable = true;
//                            int scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_SHOW);
//                            options.inSampleSize = scale;// 设置缩放级别
//                            tempBitmap = BitmapFactory.decodeFile(filePath,
//                                    options);
//                        }
//                        final Bitmap bitmap = tempBitmap;
//                        h.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageCache_down.remove(imageUrl);
//                                addCache(fileName, filePath, bitmap, bmp_size);
//                                imageCallback.imageLoaded(bitmap, imageUrl,
//                                        filePath);
//                            }
//                        });
//
//                    } else {
//                        final Bitmap bmp = BitmapFactory.decodeFile(filePath);
//                        if (bmp == null) {
//                            file.deleteOnExit();
//                            return;
//                        }
//                        h.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageCache_down.remove(imageUrl);
//                                addCache(fileName, filePath, bmp, bmp_size);
//                                imageCallback.imageLoaded(bmp, imageUrl,
//                                        filePath);
//                            }
//                        });
//                    }
//                } catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fileOutputStream != null) {
//                        try {
//                            fileOutputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    imageCache_down.remove(imageUrl);
//                }
//            }
//        });
//    }

    public interface ImageCallback {
        public void imageLoaded(Bitmap bmp, String imageUrl, String filePath);
    }

    // =============================分割线==========================================

    /**
     * 将衣服图片另存一张160的bak文件
     *
     * @param imagePath
     * @return
     */
    public static boolean saveImage_160(String imagePath) {
        boolean isSuccess = true;
        if (imagePath != null && !"".equals(imagePath)) {
            Bitmap bitmap = null;
            // Compress
            BitmapFactory.Options options;
            FileOutputStream out = null;
            try {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;// 只得到图片大小信息 不分配内存
                bitmap = BitmapFactory.decodeFile(imagePath, options);
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                options.inInputShareable = true;
                int scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_MIN);
                options.inSampleSize = scale;// 设置缩放级别
                bitmap = BitmapFactory.decodeFile(imagePath, options);

                File file = new File(imagePath + UtilFile.IMG_BAK);
                out = new FileOutputStream(file);
                if (!bitmap.compress(CompressFormat.PNG, 100, out)) {// 复制图片到缓存目录
                    isSuccess = false;
                }
                out.flush();
                bitmap = null;

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 等比例得到一张450的图片
     *
     * @return 注意判断返回值为null
     */
//    public static Bitmap getImage_450(InfoNetReturn bean) {
//        Bitmap bitmap = null;
//        if (bean instanceof InfoCloth) {
//            InfoCloth mInfoCloth = (InfoCloth) bean;
//            bitmap = getImage_450(mInfoCloth.getImgPath(),
//                    mInfoCloth.getRemoteImgPath());
//        } else if (bean instanceof InfoDayDress) {
//            InfoDayDress mInfoDayDress = (InfoDayDress) bean;
//            bitmap = getImage_450(mInfoDayDress.getImgPath(),
//                    mInfoDayDress.getRemoteImgPath());
//        }
//        return bitmap;
//    }

    /**
     * 根据路径 得到450的图片
     *
     * @param localPath
     * @param remotePath
     * @return
     */
    public static Bitmap getImage_450(String localPath, String remotePath) {
        String dir = UtilFile.DIR_CACHE;
        // 1、先从本地路径的文件名 找
        Bitmap bitmap = getBitmap(localPath, dir);
        if (bitmap == null) {
            // 2、根据网络路径的文件名找
            bitmap = getBitmap(remotePath, dir);
            if (bitmap == null) {
                // 下载
                getBitmap(remotePath, dir, new ImageCallback() {
                    @Override
                    public void imageLoaded(Bitmap bmp, String imageUrl,
                                            String filePath) {
                    }
                });
            }
        }
        return bitmap;
    }

    public static Bitmap getImage_160(String localPath, String remotePath) {
        String dir = UtilFile.DIR_CACHE;
        // 1、先从本地路径的文件名 找
        Bitmap bitmap = getBitmap_160(localPath, dir);
        if (bitmap == null) {
            // 2、根据网络路径的文件名找
            bitmap = getBitmap_160(remotePath, dir);
            if (bitmap == null) {
                // 下载
                getBitmap_160(remotePath, dir, new ImageCallback() {
                    @Override
                    public void imageLoaded(Bitmap bmp, String imageUrl,
                                            String filePath) {
                    }
                });
            }
        }
        return bitmap;
    }

    /**
     * 调用照相机
     *
     * @param activity
     * @return 注意判断返回值为null
     */
    public static String callCamera(Activity activity) {
        String imagePath = null;
        String str_dir = UtilFile.getFolderPath(UtilFile.DIR_PHOTO_CACHE);
        if (str_dir == null || "".equals(str_dir)) {
            return imagePath;
        }
        File dir = new File(str_dir);
        File filepic = new File(dir, System.currentTimeMillis() + ".jpg");
        imagePath = filepic.getAbsolutePath();// 缓存图片路径

        callCamera(activity, imagePath);
        return imagePath;
    }

    public static String callCamera(Fragment activity) {
        String imagePath = null;
        String str_dir = UtilFile.getFolderPath(UtilFile.DIR_PHOTO_CACHE);
        if (str_dir == null || "".equals(str_dir)) {
            return imagePath;
        }
        File dir = new File(str_dir);
        File filepic = new File(dir, System.currentTimeMillis() + ".png");
        imagePath = filepic.getAbsolutePath();// 缓存图片路径

        callCamera(activity, imagePath);
        return imagePath;
    }


    /**
     * 调用照相机
     *
     * @param activity
     * @param outputPath
     */
    public static void callCamera(Activity activity, String outputPath) {
        File filepic = new File(outputPath);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri fileuri = Uri.fromFile(filepic);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        activity.startActivityForResult(intent, SHOW_CAM);
    }

    public static void callCamera(Fragment activity, String outputPath) {
        File filepic = new File(outputPath);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri fileuri = Uri.fromFile(filepic);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
        activity.startActivityForResult(intent, SHOW_CAM);
    }

    /**
     * 从本地获取图片
     *
     * @param activity
     */
    public static void callLocal(BaseActivity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, SHOW_LIB);
    }

    /**
     * 裁剪图片 主要用于头像
     *
     * @param context
     * @return 注意判断返回值为null
     */
//    public static Intent getImageClipIntent(Context context) {
//
//        String dir = UtilFile.getFolderPath(UtilFile.DIR_PORTRAIT);
//        if (dir == null || "".equals(dir)) {
//            return null;
//        }
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//        intent.setType("image/*");
//        intent.putExtra("crop", "true");
//        // Trim Scale
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // Image Size
//        intent.putExtra("outputX", Constant.AVASTER_SIZE);
//        intent.putExtra("outputY", Constant.AVASTER_SIZE);
//        // intent.putExtra("return-data", true);
//        intent.putExtra("outputFormat", "JPEG");
//        intent.putExtra("output",
//                Uri.fromFile(new File(dir + UtilFile.PORTRAIT)));
//        return intent;
//    }

    /**
     * 裁剪图片 主页背景图
     *
     * @param context
     * @return 注意判断返回值为null
     */
    public static Intent getImageClipIntentForBg(Context context,
                                                 int scalex, int scaley, int outx, int outy) {

        String dir = UtilFile.getFolderPath(UtilFile.DIR_PORTRAIT);
        if (dir == null || "".equals(dir)) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        // Trim Scale
        intent.putExtra("aspectX", scalex);
        intent.putExtra("aspectY", scaley);
        // Image Size
        intent.putExtra("outputX", outx);
        intent.putExtra("outputY", outy);
        // intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("output",
                Uri.fromFile(new File(dir + UtilFile.PORTRAIT)));
        return intent;
    }

    /**
     * 根据从本地获取衣服图片的uri来保存图片 返回 路径
     *
     * @param uri
     * @param context
     * @return 注意判断返回值为null
     */
    public static String saveImageForLocal(Uri uri, Context context) {
        String imagePath = null;
        ContentResolver cr = context.getContentResolver();
        FileOutputStream os = null;
        InputStream is = null;
        File file_pic = null;
        try {// 下面就是保存图片到缓存目录下面
            is = cr.openInputStream(uri);

            String str_dir = UtilFile.getFolderPath(UtilFile.DIR_CLOTH);
            if (str_dir == null || "".equals(str_dir)) {
                return imagePath;
            }
            String str_pic = System.currentTimeMillis() + ".png";

            File dir = new File(str_dir);
            file_pic = new File(dir, str_pic);

            os = new FileOutputStream(file_pic);
            byte[] data = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) > 0) {
                os.write(data, 0, len);
            }
            os.flush();

            imagePath = file_pic.getAbsolutePath();
        } catch (Exception e) {
            if (file_pic != null) {
                file_pic.deleteOnExit();
            }
            e.printStackTrace();
        } finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imagePath;
    }

    public static String saveImageForLocal(String filePath, Context context) {
        String imagePath = null;
        FileOutputStream os = null;
        FileInputStream is = null;
        File file_pic = null;
        try {// 下面就是保存图片到缓存目录下面
            is = new FileInputStream(new File(filePath));

            String str_dir = UtilFile.getFolderPath(UtilFile.DIR_CLOTH);
            if (str_dir == null || "".equals(str_dir)) {
                return imagePath;
            }
            String str_pic = System.currentTimeMillis() + ".png";

            File dir = new File(str_dir);
            file_pic = new File(dir, str_pic);

            os = new FileOutputStream(file_pic);
            byte[] data = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) > 0) {
                os.write(data, 0, len);
            }
            os.flush();

            imagePath = file_pic.getAbsolutePath();
        } catch (Exception e) {
            if (file_pic != null) {
                file_pic.deleteOnExit();
            }
            e.printStackTrace();
        } finally {

            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imagePath;
    }

    /**
     * 发送彩信
     *
     * @param context
     * @param
     * @param
     * @throws
     * @Title sendSMS
     */
//    public static void sendSMS(Context context, String imagePath, String content) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("compose_mode", false);
//        intent.putExtra("exit_on_sent", true);
//        intent.putExtra("address", "13800138000");
//        intent.putExtra("subject",
//                context.getResources().getString(R.string.app_name));
//        intent.putExtra(
//                "sms_body",
//                content
//                        + context.getResources().getString(
//                        R.string.message_content_end));
//        // intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" +
//        // imagePath));
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
//        // intent.setClassName("com.android.mms",
//        // "com.android.mms.ui.ComposeMessageActivity");
//        intent.setType("image/*");
//        context.startActivity(intent);
//    }

    // ============================图库=================================
    // 获取所有文件夹信息
    public static ArrayList<InfoBucket> loadAllBucketList(Context context) {
        ArrayList<InfoBucket> tempBucketList = new ArrayList<InfoBucket>();
        // String[] projection = new String[]
        // {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA,MediaStore.Images.Media.SIZE,MediaStore.Images.Media.DISPLAY_NAME
        // ,MediaStore.Images.Media.SIZE,MediaStore.Images.Media.MIME_TYPE,MediaStore.Images.Media.TITLE,
        // MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.Media.DATE_MODIFIED,MediaStore.Images.Media.DESCRIPTION,
        // MediaStore.Images.Media.PICASA_ID,MediaStore.Images.Media.IS_PRIVATE,
        // MediaStore.Images.Media.LATITUDE,MediaStore.Images.Media.LONGITUDE,MediaStore.Images.Media.DATE_TAKEN,
        // MediaStore.Images.Media.ORIENTATION,MediaStore.Images.Media.MINI_THUMB_MAGIC,MediaStore.Images.Media.BUCKET_ID,
        // MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "count(*) as cnt"};
        String[] projection = new String[]{MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "count(*) as cnt"};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                " 1=1) group by " + MediaStore.Images.Media.BUCKET_ID + " --(",
                null, null);
        // int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        // int sizeColumn = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
        // int displayNameColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        // int mineTypeColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
        // int titleColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
        // int dateAddedColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
        // int dateModifiedColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
        // int descriptionColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
        // int picasaIdColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.PICASA_ID);
        // int isprivateColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE);
        // int latitudeColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
        // int longitudeColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
        // int datetakenColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        // int orientationColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
        // int miniColumn =
        // cursor.getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC);
        if (cursor != null) {
            int dateColumn = cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumn = cursor
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketDisplayNameColumn = cursor
                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int count = cursor.getColumnIndex("cnt");
            while (cursor.moveToNext()) {
                InfoBucket info = new InfoBucket();
                info.setBucketId(cursor.getString(bucketIdColumn));
                info.setName(cursor.getString(bucketDisplayNameColumn));
                info.setImageCount(cursor.getInt(count));
                info.setPath(cursor.getString(dateColumn));
                tempBucketList.add(info);
            }
            // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
            cursor.close();
        }
        return tempBucketList;
    }

    // 获取所有文件夹信息
    public static ArrayList<InfoImages> loadAllImagesList(Context context,
                                                          String bucketId) {
//        DaoThumbnails mDao = new DaoThumbnails();
        ArrayList<InfoImages> tempImagesList = new ArrayList<InfoImages>();
//        String[] projection = new String[]{MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.MIME_TYPE,
//                MediaStore.Images.Media.TITLE,
//                MediaStore.Images.Media.DATE_ADDED,
//                MediaStore.Images.Media.DATE_MODIFIED,
//                MediaStore.Images.Media.DESCRIPTION,
//                MediaStore.Images.Media.PICASA_ID,
//                MediaStore.Images.Media.IS_PRIVATE,
//                MediaStore.Images.Media.LATITUDE,
//                MediaStore.Images.Media.LONGITUDE,
//                MediaStore.Images.Media.DATE_TAKEN,
//                MediaStore.Images.Media.ORIENTATION,
//                MediaStore.Images.Media.MINI_THUMB_MAGIC,
//                MediaStore.Images.Media.BUCKET_ID,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
//                MediaStore.Images.Media.BUCKET_ID + " = '" + bucketId + "'",
//                null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
//        if (cursor != null) {
//            int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//            int dateColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DATA);
//            int sizeColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.SIZE);
//            int displayNameColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
//            int mineTypeColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
//            int titleColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.TITLE);
//            int dateAddedColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
//            int dateModifiedColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
//            int descriptionColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
//            int picasaIdColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.PICASA_ID);
//            int isprivateColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.IS_PRIVATE);
//            int latitudeColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.LATITUDE);
//            int longitudeColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.LONGITUDE);
//            int datetakenColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
//            int orientationColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.ORIENTATION);
//            LogUtil.e("------->", "" + orientationColumn);
//            int miniColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.MINI_THUMB_MAGIC);
//            int bucketIdColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
//            int bucketDisplayNameColumn = cursor
//                    .getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//            while (cursor.moveToNext()) {
//                InfoImages image = new InfoImages();
//                image.set_id(cursor.getInt(idColumn));
//                image.set_data(cursor.getString(dateColumn));
//                image.set_size(cursor.getInt(sizeColumn));
//                image.set_display_name(cursor.getString(displayNameColumn));
//                image.setMime_type(cursor.getString(mineTypeColumn));
//                image.setTitle(cursor.getString(titleColumn));
//                image.setDate_added(cursor.getLong(dateAddedColumn));
//                image.setDate_modified(cursor.getLong(dateModifiedColumn));
//                image.setDescription(cursor.getString(descriptionColumn));
//                image.setPicasa_id(cursor.getString(picasaIdColumn));
//                image.setIsprivate(cursor.getInt(isprivateColumn));
//                image.setLatitude(cursor.getFloat(latitudeColumn));
//                image.setLongitude(cursor.getFloat(longitudeColumn));
//                image.setDatetaken(cursor.getLong(datetakenColumn));
//                image.setOrientation(cursor.getInt(orientationColumn));
//                image.setMini_thumb_magic(cursor.getInt(miniColumn));
//                image.setBucket_id(cursor.getString(bucketIdColumn));
//                image.setBucket_display_name(cursor
//                        .getString(bucketDisplayNameColumn));
//
//                /**
//                 * thumbnails.set_id(cursor2.getInt(cursor2
//                 * .getColumnIndex(MediaStore.Images.Thumbnails._ID)));
//                 * thumbnails
//                 * .set_data(cursor2.getString(cursor2.getColumnIndex(MediaStore
//                 * .Images.Thumbnails.DATA)));
//                 * thumbnails.setImage_id(cursor2.getInt
//                 * (cursor2.getColumnIndex(MediaStore
//                 * .Images.Thumbnails.IMAGE_ID)));
//                 * thumbnails.setKind(cursor2.getInt
//                 * (cursor2.getColumnIndex(MediaStore.Images.Thumbnails.KIND)));
//                 * thumbnails
//                 * .setWidth(cursor2.getInt(cursor2.getColumnIndex(MediaStore
//                 * .Images.Thumbnails.WIDTH)));
//                 * thumbnails.setHeight(cursor2.getInt
//                 * (cursor2.getColumnIndex(MediaStore
//                 * .Images.Thumbnails.HEIGHT))); } //4.0以上的版本会自动关闭 (4.0--14;;
//                 * 4.0.3--15) if(Integer.parseInt(Build.VERSION.SDK) < 14) {
//                 * cursor2.close(); } }
//                 */
//                // InfoThumbnails thumbnails = mDao
//                // .getThumbnails(image.get_data());
//                // if (thumbnails != null) {
//                // image.setThumbnails(thumbnails);
//                // }
//                // 根据大图取得缩略图。
////                String[] projection2 = new String[]{
////                        MediaStore.Images.Thumbnails._ID,
////                        MediaStore.Images.Thumbnails.DATA,
////                        MediaStore.Images.Thumbnails.IMAGE_ID,
////                        MediaStore.Images.Thumbnails.KIND};
////                Cursor cursor2 = context.getContentResolver().query(
////                        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
////                        projection2,
////                        MediaStore.Images.Thumbnails.IMAGE_ID + "= "
////                                + image.get_id() + "", null, null);
////                InfoThumbnails thumbnails = null;
////                if (cursor2 != null) {
////                    if (cursor2.moveToFirst()) {
////                        thumbnails = new InfoThumbnails();
////                        thumbnails
////                                .setData(cursor2.getString(cursor2
////                                        .getColumnIndex(MediaStore.Images.Thumbnails.DATA)));
////                        if (!UtilFile.isFileExist(thumbnails.getData())) {
////                            thumbnails = null;
////                        }
////                    }
////                    image.setThumbnails(thumbnails);
////                }
//                tempImagesList.add(image);
////                cursor2.close();
//            }
//            cursor.close();
//        }
        return tempImagesList;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public static void rotateAndReplaceOldImageFile(String mStrPicPath) {
        int angel = UtilImage.readPictureDegree(mStrPicPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(mStrPicPath, options);
        bmp = null;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        int scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_SHOW);
        options.inSampleSize = scale;
        bmp = BitmapFactory.decodeFile(mStrPicPath, options);

        /**
         * 把图片旋转为正的方向
         */
        bmp = UtilImage.rotaingImageView(angel, bmp);
        try {
            bmp.compress(CompressFormat.PNG, 100,
                    new FileOutputStream(new File(mStrPicPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 回收bitmap
        recycleBitmap(bmp);
    }

    public static String get450File(String mStrPicPath) {
        int angel = UtilImage.readPictureDegree(mStrPicPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(mStrPicPath, options);
        bmp = null;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        int scale = 1;
        if (options.outWidth > UtilFile.BITMAP_WIDTH_SHOW)
            scale = (int) (options.outWidth / UtilFile.BITMAP_WIDTH_SHOW);
        options.inSampleSize = scale;
        bmp = BitmapFactory.decodeFile(mStrPicPath, options);

        /**
         * 把图片旋转为正的方向
         */
        bmp = UtilImage.rotaingImageView(angel, bmp);


        String str_dir = UtilFile.getFolderPath(UtilFile.DIR_PHOTO_CACHE);
        File dir = new File(str_dir);
        File filepic = new File(dir, System.currentTimeMillis() + ".jpg");

        try {
            bmp.compress(CompressFormat.JPEG, 100,
                    new FileOutputStream(filepic));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 回收bitmap
        recycleBitmap(bmp);
        if (filepic != null)
            return filepic.toString();
        return "";
    }


    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    public static String changeFileToUrl(String path) {
        return "file://" + path;
    }
}
