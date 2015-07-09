package cn.car4s.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.car4s.app.R;

/**
 * Description:
 * Author: Alex
 * Email: xuebo.chang@langtaojin.com
 * Time: 2015/5/13.
 */
public class DialogUtil {

    public static Dialog buildDialog(Context mContext, View view, int gravity, int animationResId, boolean outsideCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (gravity >= 0) {
            window.setGravity(gravity);
        }
        if (animationResId > 0) {
            window.setWindowAnimations(animationResId);
        }
        window.setContentView(view);
        dialog.setCanceledOnTouchOutside(outsideCancelable);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }


    public static void buildTelDialog(final Context context) {
        if (((Activity) context).isFinishing())
            return;
        final String telnumber = "051387303062";
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("")
                .setMessage("0513-87303062")
                .setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        context.startActivity(TelephonyUtil.dial(telnumber));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    public static void showUploadDialog(Context context,
                                        View.OnClickListener onClickListener) {
        View view = View.inflate(context, R.layout.dialog_upload_menu, null);
        View layout_dialog_allwidth = view
                .findViewById(R.id.layout_dialog_allwidth);
        layout_dialog_allwidth.getLayoutParams().width = UtilPhone.getScreenWidth(context);
        Dialog dialog = buildDialog(context, view, Gravity.BOTTOM,
                R.style.BottomDialogAnimation, true);
        View dialog_share_menu_friends = view
                .findViewById(R.id.dialog_upload_take);
        dialog_share_menu_friends.setOnClickListener(onClickListener);
        dialog_share_menu_friends.setTag(dialog);
        View dialog_share_menu_circle = view
                .findViewById(R.id.dialog_upload_abulmb);
        dialog_share_menu_circle.setOnClickListener(onClickListener);
        dialog_share_menu_circle.setTag(dialog);
        View dialog_share_menu_cancel = view
                .findViewById(R.id.dialog_upload_cancel);
        dialog_share_menu_cancel.setOnClickListener(onClickListener);
        dialog_share_menu_cancel.setTag(dialog);
    }


}
