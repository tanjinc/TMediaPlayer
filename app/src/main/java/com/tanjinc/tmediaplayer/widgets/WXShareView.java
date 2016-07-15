package com.tanjinc.tmediaplayer.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.data.WeiXinConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * Created by tanjinc on 16-5-17.
 */
public class WXShareView extends ImageView {
    private static final String TAG = "WeChatView";

    private Activity mActivity;
    private IWXAPI mAPI;

    public WXShareView(Context context) {
        this(context, null);
    }

    public WXShareView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WXShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView(Activity activity) {
        mActivity = activity;
        mAPI = WXAPIFactory.createWXAPI(mActivity, WeiXinConstants.APP_ID, true);
        mAPI.registerApp(WeiXinConstants.APP_ID);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void share(){
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = "http://www.modrails.com/videos/passenger_nginx.mov";

        WXMediaMessage msg = new WXMediaMessage(videoObject);
        msg.title = "Video Title Very Long Very Long Very ry Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        msg.description = "Video Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");// transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        boolean ret = mAPI.sendReq(req);
        Log.d(TAG, "share: ret" + ret);
    }
}
