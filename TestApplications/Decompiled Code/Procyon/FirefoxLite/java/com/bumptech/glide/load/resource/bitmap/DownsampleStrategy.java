// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.resource.bitmap;

public abstract class DownsampleStrategy
{
    public static final DownsampleStrategy AT_LEAST;
    public static final DownsampleStrategy AT_MOST;
    public static final DownsampleStrategy CENTER_INSIDE;
    public static final DownsampleStrategy CENTER_OUTSIDE;
    public static final DownsampleStrategy DEFAULT;
    public static final DownsampleStrategy FIT_CENTER;
    public static final DownsampleStrategy NONE;
    
    static {
        FIT_CENTER = new FitCenter();
        CENTER_OUTSIDE = new CenterOutside();
        AT_LEAST = new AtLeast();
        AT_MOST = new AtMost();
        CENTER_INSIDE = new CenterInside();
        NONE = new None();
        DEFAULT = DownsampleStrategy.CENTER_OUTSIDE;
    }
    
    public abstract SampleSizeRounding getSampleSizeRounding(final int p0, final int p1, final int p2, final int p3);
    
    public abstract float getScaleFactor(final int p0, final int p1, final int p2, final int p3);
    
    private static class AtLeast extends DownsampleStrategy
    {
        AtLeast() {
        }
        
        @Override
        public SampleSizeRounding getSampleSizeRounding(final int n, final int n2, final int n3, final int n4) {
            return SampleSizeRounding.QUALITY;
        }
        
        @Override
        public float getScaleFactor(int min, final int n, final int n2, final int n3) {
            min = Math.min(n / n3, min / n2);
            float n4 = 1.0f;
            if (min != 0) {
                n4 = 1.0f / Integer.highestOneBit(min);
            }
            return n4;
        }
    }
    
    private static class AtMost extends DownsampleStrategy
    {
        AtMost() {
        }
        
        @Override
        public SampleSizeRounding getSampleSizeRounding(final int n, final int n2, final int n3, final int n4) {
            return SampleSizeRounding.MEMORY;
        }
        
        @Override
        public float getScaleFactor(int n, int b, int i, final int n2) {
            i = (int)Math.ceil(Math.max(b / (float)n2, n / (float)i));
            b = Integer.highestOneBit(i);
            n = 1;
            b = Math.max(1, b);
            if (b >= i) {
                n = 0;
            }
            return 1.0f / (b << n);
        }
    }
    
    private static class CenterInside extends DownsampleStrategy
    {
        CenterInside() {
        }
        
        @Override
        public SampleSizeRounding getSampleSizeRounding(final int n, final int n2, final int n3, final int n4) {
            return SampleSizeRounding.QUALITY;
        }
        
        @Override
        public float getScaleFactor(final int n, final int n2, final int n3, final int n4) {
            return Math.min(1.0f, CenterInside.FIT_CENTER.getScaleFactor(n, n2, n3, n4));
        }
    }
    
    private static class CenterOutside extends DownsampleStrategy
    {
        CenterOutside() {
        }
        
        @Override
        public SampleSizeRounding getSampleSizeRounding(final int n, final int n2, final int n3, final int n4) {
            return SampleSizeRounding.QUALITY;
        }
        
        @Override
        public float getScaleFactor(final int n, final int n2, final int n3, final int n4) {
            return Math.max(n3 / (float)n, n4 / (float)n2);
        }
    }
    
    private static class FitCenter extends DownsampleStrategy
    {
        FitCenter() {
        }
        
        @Override
        public SampleSizeRounding getSampleSizeRounding(final int n, final int n2, final int n3, final int n4) {
            return SampleSizeRounding.QUALITY;
        }
        
        @Override
        public float getScaleFactor(final int n, final int n2, final int n3, final int n4) {
            return Math.min(n3 / (float)n, n4 / (float)n2);
        }
    }
    
    private static class None extends DownsampleStrategy
    {
        None() {
        }
        
        @Override
        public SampleSizeRounding getSampleSizeRounding(final int n, final int n2, final int n3, final int n4) {
            return SampleSizeRounding.QUALITY;
        }
        
        @Override
        public float getScaleFactor(final int n, final int n2, final int n3, final int n4) {
            return 1.0f;
        }
    }
    
    public enum SampleSizeRounding
    {
        MEMORY, 
        QUALITY;
    }
}
