// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2;

import com.google.android.exoplayer2.analytics.AnalyticsCollector;
import android.os.Looper;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import android.content.Context;
import com.google.android.exoplayer2.upstream.BandwidthMeter;

public final class ExoPlayerFactory
{
    private static BandwidthMeter singletonBandwidthMeter;
    
    private static BandwidthMeter getDefaultBandwidthMeter(final Context context) {
        synchronized (ExoPlayerFactory.class) {
            if (ExoPlayerFactory.singletonBandwidthMeter == null) {
                ExoPlayerFactory.singletonBandwidthMeter = new DefaultBandwidthMeter.Builder(context).build();
            }
            return ExoPlayerFactory.singletonBandwidthMeter;
        }
    }
    
    public static SimpleExoPlayer newSimpleInstance(final Context context, final RenderersFactory renderersFactory, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        return newSimpleInstance(context, renderersFactory, trackSelector, loadControl, drmSessionManager, Util.getLooper());
    }
    
    public static SimpleExoPlayer newSimpleInstance(final Context context, final RenderersFactory renderersFactory, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final Looper looper) {
        return newSimpleInstance(context, renderersFactory, trackSelector, loadControl, drmSessionManager, new AnalyticsCollector.Factory(), looper);
    }
    
    public static SimpleExoPlayer newSimpleInstance(final Context context, final RenderersFactory renderersFactory, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final AnalyticsCollector.Factory factory, final Looper looper) {
        return newSimpleInstance(context, renderersFactory, trackSelector, loadControl, drmSessionManager, getDefaultBandwidthMeter(context), factory, looper);
    }
    
    public static SimpleExoPlayer newSimpleInstance(final Context context, final RenderersFactory renderersFactory, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final BandwidthMeter bandwidthMeter, final AnalyticsCollector.Factory factory, final Looper looper) {
        return new SimpleExoPlayer(context, renderersFactory, trackSelector, loadControl, drmSessionManager, bandwidthMeter, factory, looper);
    }
    
    @Deprecated
    public static SimpleExoPlayer newSimpleInstance(final Context context, final TrackSelector trackSelector, final LoadControl loadControl, final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, final int n) {
        return newSimpleInstance(context, new DefaultRenderersFactory(context, n), trackSelector, loadControl, drmSessionManager);
    }
}
