package com.tanjinc.tmediaplayer.widgets;

/**
 * 自定义播放器内控件的公共接口
 * Created by tanjincheng on 16/4/24.
 */
public interface IWidget {
    /**
     * 现实控件
     * @param anim  是否有动画
     */
    void showWithAnim(boolean anim);

    /**
     * 隐藏控件
     * @param anim  是否有动画
     */
    void hideWithAnim(boolean anim);

    /**
     * 根据是否横竖屏重新布局
     * @param isHorizion    是否横屏
     */
    void resetLayout(boolean isHorizion);
}
