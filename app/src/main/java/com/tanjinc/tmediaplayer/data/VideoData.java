package com.tanjinc.tmediaplayer.data;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class VideoData {
    private String name;
    private String path;
    public Bitmap thumbnail;



    public String thumbPath;
    private String parentDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getParentDir() {
        return parentDir;
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
}
