package com.tanjinc.tmediaplayer.player;

import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;

/**
 * Created by tanjincheng on 16/4/21.
 */
public interface RendererBuilder {
    /**
     * Builds renderers for playback.
     *
     * @param player The player for which renderers are being built. {@link DemoPlayer#onRenderers}
     *     should be invoked once the renderers have been built. If building fails,
     *     {@link DemoPlayer#onRenderersError} should be invoked.
     */
    void buildRenderers(ExoVideoView player);
    /**
     * Cancels the current build operation, if there is one. Else does nothing.
     * <p>
     * A canceled build operation must not invoke {@link DemoPlayer#onRenderers} or
     * {@link DemoPlayer#onRenderersError} on the player, which may have been released.
     */
    void cancel();
}
