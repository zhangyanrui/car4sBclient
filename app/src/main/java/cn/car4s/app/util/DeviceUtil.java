package cn.car4s.app.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import cn.car4s.app.AppContext;

import java.lang.reflect.Method;

/**
 * @author Nick create at 2011-3-18
 */
public class DeviceUtil {

    public static String getProp(String prop) {
        String output = "";
        try {
            Class<?> sp = Class.forName("android.os.SystemProperties");
            Method get = sp.getMethod("get", String.class);
            output = (String) get.invoke(null, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String getImei() {
        TelephonyManager tm = (TelephonyManager) AppContext.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    public static float getDensity() {
        DisplayMetrics displayMetrics = AppContext.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static float getDensityDpi() {
        DisplayMetrics displayMetrics = AppContext.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.densityDpi;
    }

    public static float getWidth() {
        DisplayMetrics displayMetrics = AppContext.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static float getHeigh() {
        DisplayMetrics displayMetrics = AppContext.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static String getResolution() {
        DisplayMetrics displayMetrics = AppContext.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
    }

    public static int getVersionCode() {
        PackageManager manager = AppContext.getInstance().getPackageManager();
        ApplicationInfo info = AppContext.getInstance().getApplicationInfo();

        try {
            return manager.getPackageInfo(info.packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static String getVersionName() {
        PackageManager manager = AppContext.getInstance().getPackageManager();
        ApplicationInfo info = AppContext.getInstance().getApplicationInfo();

        try {
            return manager.getPackageInfo(info.packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            return "Unknow";
        }
    }

    public static int getPxFromDip(float dip) {
        return (int) (0.5f + getDensity() * dip);
    }

    public static boolean isDevXiaoMi() {
        String name = Build.MANUFACTURER;
        return "Xiaomi".equals(name);
    }

    //魅族特殊型号
    public static boolean isDevMeizuSpecial() {
        String xinghao = Build.HARDWARE;//一般和基板一样
        String brand = Build.MANUFACTURER;
        //朵唯C9、美图秀秀1S、中兴zte u9180、中兴9128
//        if (("Meizu".equals(brand) && "mx2".equals(xinghao)) || ("C9".equals(xinghao) && "duowei".equalsIgnoreCase(brand))
//                || ("meitu".equalsIgnoreCase(brand) && "1S".equals(xinghao)) || ("zte".equalsIgnoreCase(brand) && "u9180".equalsIgnoreCase(xinghao))
//                || ("zte".equalsIgnoreCase(brand) && "9128".equals(xinghao)) || ("Meizu".equals(brand) && "mx3".equals(xinghao))) {
//            return true;
//        }
        return ("Meizu".equals(brand) && "mx2".equals(xinghao));
    }


}
