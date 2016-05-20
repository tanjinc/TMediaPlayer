package com.tanjinc.tmediaplayer.widgets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;
import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.data.AccessTokenKeeper;
import com.tanjinc.tmediaplayer.data.WeiBoConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 * Created by tanjincheng on 16/5/14.
 */
public class WeiboShareView extends ImageButton implements WeiboAuthListener{
    private static final String TAG = "WeiboShareView";

    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;

    private Activity mActivity;
    private int mShareType = SHARE_CLIENT;

    private AuthInfo mAuthInfo;

    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private IWeiboShareAPI mWeiboShareAPI = null;

    public WeiboShareView(Context context) {
        super(context, null);
    }

    public WeiboShareView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public WeiboShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeiboShareView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void initView(Activity activity) {
        mActivity = activity;
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mActivity, WeiBoConstants.APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
//        }
        mAuthInfo = new AuthInfo(mActivity, WeiBoConstants.APP_KEY, WeiBoConstants.REDIRECT_URL, WeiBoConstants.SCOPE);

    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = "demo video";
        videoObject.description = "this is description";
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里  设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。


        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            System.out.println("kkkkkkk    size  "+ os.toByteArray().length );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Weibo.BaseMediaObject", "put thumb failed");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        videoObject.setThumbImage(bitmap);
        videoObject.actionUrl = "http://www.modrails.com/videos/passenger_nginx.mov";
        videoObject.dataUrl = "http://www.modrails.com/videos/passenger_nginx.mov";
        videoObject.dataHdUrl = "http://www.modrails.com/videos/passenger_nginx.mov";
        videoObject.duration = 10;
        videoObject.defaultText = "Vedio 默认文案";
        return videoObject;
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     */
    private void sendMessage() {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        // 用户可以分享视频资源
        weiboMessage.mediaObject = getVideoObj();


        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mActivity.getApplicationContext());
        String token = "";
        if (accessToken != null && accessToken.getToken().equals("")) {
            token = accessToken.getToken();
        } else {
            mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
            mSsoHandler.authorizeClientSso(this);
        }
        mWeiboShareAPI.sendRequest(mActivity, request, mAuthInfo, token, this);
    }

    public void share() {
        Log.d(TAG, "video share: ");
        sendMessage();
    }

    @Override
    public void onComplete(Bundle bundle) {
        Log.d(TAG, "video onComplete: ");
        Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
        AccessTokenKeeper.writeAccessToken(mActivity.getApplicationContext(), newToken);
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Log.e(TAG, "video onWeiboException: " + e.toString() );
    }

    @Override
    public void onCancel() {

    }
}
