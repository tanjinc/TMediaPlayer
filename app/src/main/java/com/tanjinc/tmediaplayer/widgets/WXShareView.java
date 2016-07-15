package com.tanjinc.tmediaplayer.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.data.WXConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.platformtools.BackwardSupportUtil;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * Created by tanjincheng on 16/5/19.
 */
public class WXShareView extends ImageButton{
    private Activity mActivity;
    private IWXAPI mApi;
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
        mApi = WXAPIFactory.createWXAPI(mActivity, WXConstants.APP_ID, true);
        mApi.registerApp(WXConstants.APP_ID);
    }
    public void share(boolean isSession) {
        WXVideoObject videoObject = new WXVideoObject();
        videoObject.videoUrl = "http://www.modrails.com/videos/passenger_nginx.mov";

        WXMediaMessage msg = new WXMediaMessage(videoObject);
        msg.title = "test mp4";
        msg.description = "just a test for video";
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmpToByteArray(bitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = isSession ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        mApi.sendReq(req);
    }
}
