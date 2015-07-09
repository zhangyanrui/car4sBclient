package cn.car4s.app.util;


import android.util.Log;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/5/13.
 */
public class LogUtil {

    public static final boolean ISDEBUG = true;

    public static void e(String tag, String msg) {

        if (ISDEBUG) {
            Log.e(tag, msg);
        }
    }

}
