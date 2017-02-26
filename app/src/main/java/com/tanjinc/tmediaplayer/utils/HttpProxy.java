package com.tanjinc.tmediaplayer.utils;

import java.net.ServerSocket;
import java.net.SocketAddress;

/**
 * Created by tanjincheng on 17/1/8.
 */
public class HttpProxy {
    public static int LOCAL_PORT = 80;
    public static int SIZE = (int)(5 * 1024 * 1024);//  5MB缓存
    public static String TAG = HttpProxy.class.getSimpleName();

    private SocketAddress mSocketAddress;
}
