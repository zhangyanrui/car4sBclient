package cn.car4s.app.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import cn.car4s.app.AppContext;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;

public class UtilPhone {
    private final static String TAG = UtilPhone.class.getSimpleName();

    /**
     * 手机IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 获得本机ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得本机mac地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 得到MD5 注意 此MD5 都是小写
     *
     * @param str
     * @return
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString().toLowerCase();
    }

    /**
     * 得到手机的密度 0.75 1.0 1.5
     *
     * @param @param  context
     * @param @return
     * @return float
     * @throws
     * @Title: getScreenDensity
     * @Description: TODO
     */
    private static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * dip 转换为px
     *
     * @param @param  context
     * @param @param  dip
     * @param @return
     * @return int
     * @throws
     * @Title: getPxFromDip
     * @Description: TODO
     */
    public static int getPxFromDip(Context context, float dip) {
        return (int) (0.5f + getScreenDensity(context) * dip);
    }

    /**
     * 手机的高
     *
     * @param @param  context
     * @param @return 单位px
     * @return int
     * @throws
     * @Title: getAllHeight
     * @Description: TODO
     */
    public static int getAllHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 屏幕的高
     *
     * @param @param  context
     * @param @return
     * @return int
     * @throws
     * @Title: getScreenHeight
     * @Description: TODO
     */
    public static int getScreenHeight(Context context) {
        Class c;
        int y = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            y = context.getResources().getDimensionPixelSize(x);
            System.out.println(x + "=======" + y);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getAllHeight(context) - y;
    }

    /**
     * 手机的宽 即屏幕的宽
     *
     * @param @param  context
     * @param @return 单位px
     * @return int
     * @throws
     * @Title: getScreenWidth
     * @Description: TODO
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getvVersionCode(Context context) {
        int versonCode = -1;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versonCode = info.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versonCode;
    }

    public static float getPhoneScale(Context context) {
        return 1.0f * getScreenWidth(context) / getScreenHeight(context);
    }

    /**
     * 得到程序的包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 应用是否运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppRunning(Context context, String packageName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(500);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 弹出软键盘
     *
     * @param edt
     */
    public static void showSoftKey(EditText edt) {
        ((InputMethodManager) AppContext.getInstance().getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(edt, 0);
    }

    /**
     * alex
     * 隐藏软键盘
     */
    public static void hiddenSoftKey(Activity activity) {
        ((InputMethodManager) AppContext.getInstance().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity
                        .getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * alex
     * 隐藏软键盘
     */
    public static void hiddenSoftKey(EditText edt) {
        ((InputMethodManager) AppContext.getInstance().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edt.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static int getStatusBarHeight(Activity context) {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
