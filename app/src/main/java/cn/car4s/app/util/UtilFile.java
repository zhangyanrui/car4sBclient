package cn.car4s.app.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;

public class UtilFile {
    public static final String DIR_PHOTO_CACHE = "/cachephoto";
    public static final String ROOT = "fblife";
    private final static String TAG = UtilFile.class.getSimpleName();

    public static final String IMG_BAK = ".160.bak";// 临时图片后缀
    public static final int BITMAP_WIDTH_MIN = 120;// 小图片的宽度 即瀑布流里面的图片
    public static final int BITMAP_WIDTH_SHOW = 300;// 大图片的管库 即详细显示的图片宽度


    public static final String DIR_CACHE = "/imageCache";//
    public static final String DIR_CLOTH = "/imageCache";// 衣服图片 因为历史原因
    // 这个和每日装扮的地址 都是
    // imageCache
    public static final String DIR_DAYDRESS = "/imageCache";// 每日装扮图片
    public static final String DIR_CACHE_160 = "/imageCache_160";// 160宽度的达人秀图片和衣橱秀图片
    public static final String DIR_CACHE_450 = "/imageCache_450";// 450宽度的达人秀图片和衣橱秀图片
    public static final String DIR_WEIBO = "/Weibo";// 微博
    public static final String DIR_CAIXIN = "/Caixin";
    public static final String DIR_ACTIVITY = "/activity";// 活动
    public static final String DIR_PORTRAIT = "/portrait";// 头像存自己的头像。
    public static final String DIR_PORTRAIT_180 = "/portrait_180";// 180头像,存别人的头像
    public static final String DIR_APK = "/apk";
    public static final String DIR_BANNER = "/banners";// cxb 广场活动
    public static final String DIR_THUMBNAILS = "/thumbnails";
    public final static String PORTRAIT = "/portrait.jpeg";
    public final static String PORTRAIT_BG = "/portrait_bg.jpeg";
    public final static String PORTRAIT_BAK = "/123.jpeg";
    public static final String DIR_IMAGELOADER = "/imageloader";

    public final static String DIR_FASHION = "/fashionCache";
    public final static String DIR_APP = "/app";

    /**
     * 判断sd卡读写能力
     *
     * @return
     */
    public static boolean isSDCardWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到文件夹缺省路径 因为有的时候 可能没有sdcard
     *
     * @return 注意判断是否为null
     */
    private static String getDefaultFolderPath() {
        String folderUrl = null;
        // 有读写权限
        if (isSDCardWritable()) {
            String pathFile = Environment
                    .getExternalStorageDirectory().getAbsolutePath();
            folderUrl = pathFile + File.separator + ROOT;

        } else {// 没有读写能力
            LogUtil.e(TAG, "sd卡没有读写能力1， 使用内部安装文件路径");
            folderUrl = packagePath + File.separator + ROOT;
        }
        return folderUrl;
    }

    // 应用在系统里面的位置 类似于data/data/com.wmyc.activity 用于在没有sdcard的时候 创建根文件
    public static String packagePath;

    /**
     * 创建缺省的文件夹
     *
     * @return
     */
    private static boolean createDefaultFolder() {
        String folderPath = getDefaultFolderPath();
        if (folderPath == null) {
            return false;
        }
        File file = new File(folderPath);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 得到根路径
     *
     * @return 注意判断是否为null
     */
    public static String getRoot() {
        String filePath = null;
        if (createDefaultFolder()) {
            filePath = getDefaultFolderPath();
        }
        return filePath;
    }

    /**
     * 判断 该路径的文件或者文件夹是否存在
     *
     * @param
     * @return
     */
    public static boolean isFileExist(String filePath) {
        try {
            File folder = new File(filePath);
            if (folder.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param dir
     * @return
     */
    public static boolean createdFolder(String dir) {
        boolean result = false;
        if (dir != null && !dir.equals("")) {
            String filePath = getRoot() + dir;
            File folder = new File(filePath);
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    createNoMedia(filePath);
                    result = true;
                } else {
                    result = false;
                }
            } else {
                createNoMedia(filePath);
                result = true;
            }
        }
        return result;
    }

    /**
     * 得到文件夹路径 如果不存在 就创建
     *
     * @param dir
     * @return 注意判断返回值是否为null
     */
    public static String getFolderPath(String dir) {
        String path = null;
        if (createdFolder(dir)) {
            path = getRoot() + dir;
        }
        return path;
    }

    // 创建一个.nomedia的文件 不被图库扫描
    private static void createNoMedia(String dir) {
        File f = new File(dir, ".nomedia");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除一个文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                result = file.delete();
            }
        }
        return result;
    }

    /**
     * 删除文件夹
     *
     * @param
     * @return
     */
    public static boolean deleteDir(String deleteDir) {
        boolean result = true;
        String dirPath = getRoot() + deleteDir;
        File dir = new File(dirPath);
        if (dir.exists()) {
            String[] files = dir.list();
            if (files != null && files.length > 0) {
                File file;
                for (int i = 0; i < files.length; i++) {
                    file = new File(dir, files[i]);
                    if (file.exists()) {
                        result = file.delete();
                    }
                }
            }
        }
        return result;
    }

    /**
     * 清除缓存
     *
     * @return
     */
    public static boolean deleteCache() {
        boolean result = true;

        result = deleteDir(DIR_APK);
        result = deleteDir(DIR_CACHE_160);
        result = deleteDir(DIR_CACHE_450);
        result = deleteDir(DIR_CAIXIN);
        result = deleteDir(DIR_ACTIVITY);
        result = deleteDir(DIR_PORTRAIT);
        result = deleteDir(DIR_PORTRAIT_180);
        result = deleteDir(DIR_WEIBO);
        result = deleteDir(DIR_FASHION);
        result = deleteDir(DIR_THUMBNAILS);

        return result;
    }

    // public static boolean deleteFashionCache(){
    // boolean result = true;
    // result = deleteDir(DIR_FASHION);
    // return result;
    // }

    /**
     * 得到sdcard上可用的大小
     *
     * @return
     */
    public static long getAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        // 取得sdcard文件路径
        StatFs statfs = new StatFs(path.getPath());
        // 获取block的SIZE
        long blocSize = statfs.getBlockSize();
        // 获取BLOCK数量
        // long totalBlocks = statfs.getBlockCount();
        // 可用的Block的数量
        long availaBlock = statfs.getAvailableBlocks();
        return availaBlock * blocSize;
    }

    /**
     * 得到临时图像路径
     *
     * @return String
     * @throws
     * @Title getPortraitPath
     */
    public static String getPortraitPath() {
        String dir = getFolderPath(UtilFile.DIR_PORTRAIT);
        if (dir == null || "".equals(dir)) {
            return null;
        }
        return dir + PORTRAIT;
    }

    /**
     * 修改文件名字
     *
     * @param srcFilepath
     * @param dstFileName
     * @return boolean
     * @throws
     * @Title renameFile
     */
    public static String renameFile(String srcFilePath, String dstFileName) {
        boolean result = false;
        if (dstFileName.contains("/")) {
            dstFileName = dstFileName
                    .substring(dstFileName.lastIndexOf("/") + 1);
        }
        String dstFilePath = "";
        File file = new File(srcFilePath);
        if (file.exists() && file.isFile()) {
            dstFilePath = file.getParent() + File.separator + dstFileName;
            File newFile = new File(dstFilePath);
            result = file.renameTo(newFile);
        }
        if (result) {
            return dstFilePath;
        } else {
            return null;
        }
    }
}
