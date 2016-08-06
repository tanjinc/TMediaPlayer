package com.tanjinc.tmediaplayer.utils;

/**
 * Created by tanjinc on 16-7-11.
 */

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Observable;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SDCardHelper extends BroadcastReceiver {

    private static SDCardHelper sInstance = null;
    private Context mContext;
    private boolean mIsMounted = true;
    private final SDCardStateObservable mStateObservable = new SDCardStateObservable();
    private List<MountPoint> mMountPathList = Collections
            .synchronizedList(new ArrayList<MountPoint>());

    //反射优化，存储反射方法
    private Method sDescription;
    private Method sPath;
    private Method sIsRemovable;
    private Method sVolumeState;

    public interface SDCardStateObserver {
        public void onChanged(Intent intent, boolean mounted);
    }

    public static void createInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SDCardHelper(context);
        }
    }

    public static SDCardHelper getInstance() {
        return sInstance;
    }

    private SDCardHelper(Context context) {
        mContext = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        mContext.registerReceiver(this, filter);
        getMountPointList(context);
        mIsMounted = isSDCardMounted();
    }

    public void onDestory() {
        mContext.unregisterReceiver(this);
        sInstance = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        getMountPointList(context);
        if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
            mIsMounted = true;
            notifyStateChanged(intent, true);
        } else if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
            mIsMounted = false;
            notifyStateChanged(intent, false);
        } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
            mIsMounted = false;
            notifyStateChanged(intent, false);
        }
    }

    public boolean isMounted() {
        return mIsMounted;
    }

    public void registerStateObserver(SDCardStateObserver observer) {
        mStateObservable.registerObserver(observer);
    }

    public void unregisterStateObserver(SDCardStateObserver observer) {
        mStateObservable.unregisterObserver(observer);
    }

    public void notifyStateChanged(Intent intent, boolean mounted) {
        mStateObservable.notifyStateChanged(intent, mounted);
    }

    private static class SDCardStateObservable extends
            Observable<SDCardStateObserver> {

        public void notifyStateChanged(Intent intent, boolean mounted) {
            synchronized (mObservers) {
                for (int i = mObservers.size() - 1; i >= 0; i--) {
                    mObservers.get(i).onChanged(intent, mounted);
                }
            }
        }
    }

    public boolean isSDCardMounted() {
        if(getSDCardMountPoint()==null){
            return false;
        }
        return getSDCardMountPoint().isMounted();
    }

    public boolean isOtgMounted() {
        if(getOtgMountPoint()==null){
            return false;
        }
        return getOtgMountPoint().isMounted();
    }

    /**
     * @return null this device not exist
     */
    public MountPoint getOtgMountPoint() {
        for (MountPoint mountPoint : mMountPathList) {
            if (mountPoint.mIsExternal && mountPoint.mPath.contains("mnt")) {
                return mountPoint;
            }
        }
        return null;
    }

    /**
     * @return null this device not exist
     */
    public MountPoint getStorageMountPoint() {
        for (MountPoint mountPoint : mMountPathList) {
            if (!mountPoint.mIsExternal) {
                return mountPoint;
            }
        }
        return null;
    }

    /**
     * @return null this device not exist
     */
    public MountPoint getSDCardMountPoint() {
        for (MountPoint mountPoint : mMountPathList) {
            if (mountPoint.mIsExternal && mountPoint.mPath.contains("storage")) {
                return mountPoint;
            }
        }
        return null;
    }

    public List<MountPoint> getMountPointList(Context context) {
        StorageManager sm = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);
        mMountPathList.clear();

        try {
            // 获取sdcard的路径：外置和内置
            Object[] storageVolumes = (Object[]) sm.getClass()
                    .getMethod("getVolumeList").invoke(sm);

            if (storageVolumes != null) {
                for (Object volume : storageVolumes) {
                    MountPoint mountPoint = new MountPoint();
                    if (sDescription == null || sPath == null || sIsRemovable == null || sVolumeState == null) {
                        sDescription = volume.getClass().getDeclaredMethod("getDescription", Context.class);
                        sPath = volume.getClass().getDeclaredMethod("getPath");
                        sIsRemovable = volume.getClass().getDeclaredMethod("isRemovable");
                        sVolumeState = sm.getClass().getMethod("getVolumeState", String.class);
                    }
                    String location = (String) sPath.invoke(volume);

                    // 这是存储盘描述，内部存储或者SD卡
                    mountPoint.setDescription((String) sDescription.invoke(volume, context));
                    // 挂载路径
                    mountPoint.setPath(location);

                    // 检查存储盘是否挂载上了
                    mountPoint.setMountedState((String) sVolumeState
                            .invoke(sm, location));

                    // 检查存储盘是否可以移除，内部存储不可以移动(除非有bug)，外接SD卡可移除
                    mountPoint.setExternal((Boolean) sIsRemovable.invoke(volume));
                    mMountPathList.add(mountPoint);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return mMountPathList;
    }

    /**
     * Storage device mount info
     */
    public class MountPoint {
        private String mDescription;
        private String mPath;
        private String mMountedState;
        private boolean mIsExternal;

        /**
         * 获取存储设备信息描述字符
         */
        public String getDescription() {
            return mDescription;
        }

        /**
         * 设置存储设备信息描述字符
         *
         * @param mDescription
         */
        private void setDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        /**
         * 获取设备路径
         *
         * @return
         */
        public String getPath() {
            return mPath;
        }

        /**
         * 设置设备路径
         *
         * @param mPath
         */
        private void setPath(String mPath) {
            this.mPath = mPath;
        }

        /**
         * 获取挂载状态
         *
         * @return
         */
        public String getMountedState() {
            return mMountedState;
        }

        /**
         * 设置挂载状态
         *
         * @param mountedState
         */
        private void setMountedState(String mountedState) {
            this.mMountedState = mountedState;
        }

        /**
         * 是否mount
         *
         * @return
         */
        public boolean isMounted() {
            return mMountedState.equals(Environment.MEDIA_MOUNTED);
        }

        /**
         * @return 判断是否是外置存储设备
         */
        public boolean isExternal() {
            return mIsExternal;
        }

        private void setExternal(boolean mIsExternal) {
            this.mIsExternal = mIsExternal;
        }

        // 设备总空间大小
        @SuppressLint("NewApi")
        public long getTotalBlocks() {
            if (!isMounted()) {
                return 0;
            }
            StatFs statFs = new StatFs(mPath);
            return statFs.getBlockSizeLong() * statFs.getBlockCountLong();
        }

        // 剩余的空间大小
        @SuppressLint("NewApi")
        public long availableSpace() {
            if (!isMounted()) {
                return 0;
            }
            StatFs statFs = new StatFs(mPath);
            return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
        }
    }
}
