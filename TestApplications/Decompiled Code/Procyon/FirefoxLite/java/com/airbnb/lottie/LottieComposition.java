// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import android.content.Context;
import java.util.Iterator;
import android.util.Log;
import java.util.HashSet;
import java.util.List;
import com.airbnb.lottie.model.layer.Layer;
import android.support.v4.util.LongSparseArray;
import com.airbnb.lottie.model.Font;
import java.util.Map;
import com.airbnb.lottie.model.FontCharacter;
import android.support.v4.util.SparseArrayCompat;
import android.graphics.Rect;

public class LottieComposition
{
    private Rect bounds;
    private SparseArrayCompat<FontCharacter> characters;
    private float endFrame;
    private Map<String, Font> fonts;
    private float frameRate;
    private Map<String, LottieImageAsset> images;
    private LongSparseArray<Layer> layerMap;
    private List<Layer> layers;
    private final PerformanceTracker performanceTracker;
    private Map<String, List<Layer>> precomps;
    private float startFrame;
    private final HashSet<String> warnings;
    
    public LottieComposition() {
        this.performanceTracker = new PerformanceTracker();
        this.warnings = new HashSet<String>();
    }
    
    public void addWarning(final String e) {
        Log.w("LOTTIE", e);
        this.warnings.add(e);
    }
    
    public Rect getBounds() {
        return this.bounds;
    }
    
    public SparseArrayCompat<FontCharacter> getCharacters() {
        return this.characters;
    }
    
    public float getDuration() {
        return (float)(long)(this.getDurationFrames() / this.frameRate * 1000.0f);
    }
    
    public float getDurationFrames() {
        return this.endFrame - this.startFrame;
    }
    
    public float getEndFrame() {
        return this.endFrame;
    }
    
    public Map<String, Font> getFonts() {
        return this.fonts;
    }
    
    public float getFrameRate() {
        return this.frameRate;
    }
    
    public Map<String, LottieImageAsset> getImages() {
        return this.images;
    }
    
    public List<Layer> getLayers() {
        return this.layers;
    }
    
    public PerformanceTracker getPerformanceTracker() {
        return this.performanceTracker;
    }
    
    public List<Layer> getPrecomps(final String s) {
        return this.precomps.get(s);
    }
    
    public float getStartFrame() {
        return this.startFrame;
    }
    
    public void init(final Rect bounds, final float startFrame, final float endFrame, final float frameRate, final List<Layer> layers, final LongSparseArray<Layer> layerMap, final Map<String, List<Layer>> precomps, final Map<String, LottieImageAsset> images, final SparseArrayCompat<FontCharacter> characters, final Map<String, Font> fonts) {
        this.bounds = bounds;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.frameRate = frameRate;
        this.layers = layers;
        this.layerMap = layerMap;
        this.precomps = precomps;
        this.images = images;
        this.characters = characters;
        this.fonts = fonts;
    }
    
    public Layer layerModelForId(final long n) {
        return this.layerMap.get(n);
    }
    
    public void setPerformanceTrackingEnabled(final boolean enabled) {
        this.performanceTracker.setEnabled(enabled);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LottieComposition:\n");
        final Iterator<Layer> iterator = this.layers.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString("\t"));
        }
        return sb.toString();
    }
    
    @Deprecated
    public static class Factory
    {
        @Deprecated
        public static Cancellable fromAssetFileName(final Context context, final String s, final OnCompositionLoadedListener onCompositionLoadedListener) {
            final ListenerAdapter listenerAdapter = new ListenerAdapter(onCompositionLoadedListener);
            LottieCompositionFactory.fromAsset(context, s).addListener(listenerAdapter);
            return listenerAdapter;
        }
        
        private static final class ListenerAdapter implements Cancellable, LottieListener<LottieComposition>
        {
            private boolean cancelled;
            private final OnCompositionLoadedListener listener;
            
            private ListenerAdapter(final OnCompositionLoadedListener listener) {
                this.cancelled = false;
                this.listener = listener;
            }
            
            @Override
            public void onResult(final LottieComposition lottieComposition) {
                if (this.cancelled) {
                    return;
                }
                this.listener.onCompositionLoaded(lottieComposition);
            }
        }
    }
}
