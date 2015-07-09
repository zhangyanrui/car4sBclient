package cn.car4s.app.util;

import android.widget.Toast;
import cn.car4s.app.AppContext;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/5/13.
 */
public class ToastUtil {

    public static void showToastShort(String text) {
        Toast.makeText(AppContext.getInstance(), text, Toast.LENGTH_SHORT).show();
    }
}
