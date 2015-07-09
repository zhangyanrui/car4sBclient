/*
 * 官网地站:http://www.ShareSDK.cn
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? ShareSDK.cn. All rights reserved.
 */

package cn.car4s.app.wxapi;

import android.content.Intent;
import android.widget.Toast;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/**
 * ΢�ſͻ��˻ص�activityʾ��
 */
public class WXEntryActivity extends WechatHandlerActivity {

    /**
     * ����΢�ŷ������������Ӧ������app message
     * <p/>
     * ��΢�ſͻ����е�����ҳ���С���ӹ��ߡ������Խ���Ӧ�õ�ͼ����ӵ�����
     * �˺���ͼ�꣬����Ĵ���ᱻִ�С�Demo����ֻ�Ǵ��Լ����ѣ������
     * �������������飬�������������κ�ҳ��
     */
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(iLaunchMyself);
    }

    /**
     * ����΢���������Ӧ�÷������Ϣ
     * <p/>
     * �˴��������մ�΢�ŷ��͹�������Ϣ���ȷ�˵��demo��wechatpage�������
     * Ӧ��ʱ���Բ�����Ӧ���ļ���������һ��Ӧ�õ��Զ�����Ϣ�����ܷ���΢��
     * �ͻ��˻�ͨ������������������Ϣ���ͻؽ��շ��ֻ��ϵı�demo�У�����
     * �ص���
     * <p/>
     * ��Demoֻ�ǽ���Ϣչʾ������������������������飬��������ֻ��Toast
     */
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }

}
