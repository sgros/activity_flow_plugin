// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.circularreveal;

import android.util.Property;
import android.support.design.widget.MathUtils;
import android.animation.TypeEvaluator;
import android.graphics.drawable.Drawable;

public interface CircularRevealWidget extends Delegate
{
    void buildCircularRevealCache();
    
    void destroyCircularRevealCache();
    
    int getCircularRevealScrimColor();
    
    RevealInfo getRevealInfo();
    
    void setCircularRevealOverlayDrawable(final Drawable p0);
    
    void setCircularRevealScrimColor(final int p0);
    
    void setRevealInfo(final RevealInfo p0);
    
    public static class CircularRevealEvaluator implements TypeEvaluator<RevealInfo>
    {
        public static final TypeEvaluator<RevealInfo> CIRCULAR_REVEAL;
        private final RevealInfo revealInfo;
        
        static {
            CIRCULAR_REVEAL = (TypeEvaluator)new CircularRevealEvaluator();
        }
        
        public CircularRevealEvaluator() {
            this.revealInfo = new RevealInfo();
        }
        
        public RevealInfo evaluate(final float n, final RevealInfo revealInfo, final RevealInfo revealInfo2) {
            this.revealInfo.set(MathUtils.lerp(revealInfo.centerX, revealInfo2.centerX, n), MathUtils.lerp(revealInfo.centerY, revealInfo2.centerY, n), MathUtils.lerp(revealInfo.radius, revealInfo2.radius, n));
            return this.revealInfo;
        }
    }
    
    public static class CircularRevealProperty extends Property<CircularRevealWidget, RevealInfo>
    {
        public static final Property<CircularRevealWidget, RevealInfo> CIRCULAR_REVEAL;
        
        static {
            CIRCULAR_REVEAL = new CircularRevealProperty("circularReveal");
        }
        
        private CircularRevealProperty(final String s) {
            super((Class)RevealInfo.class, s);
        }
        
        public RevealInfo get(final CircularRevealWidget circularRevealWidget) {
            return circularRevealWidget.getRevealInfo();
        }
        
        public void set(final CircularRevealWidget circularRevealWidget, final RevealInfo revealInfo) {
            circularRevealWidget.setRevealInfo(revealInfo);
        }
    }
    
    public static class CircularRevealScrimColorProperty extends Property<CircularRevealWidget, Integer>
    {
        public static final Property<CircularRevealWidget, Integer> CIRCULAR_REVEAL_SCRIM_COLOR;
        
        static {
            CIRCULAR_REVEAL_SCRIM_COLOR = new CircularRevealScrimColorProperty("circularRevealScrimColor");
        }
        
        private CircularRevealScrimColorProperty(final String s) {
            super((Class)Integer.class, s);
        }
        
        public Integer get(final CircularRevealWidget circularRevealWidget) {
            return circularRevealWidget.getCircularRevealScrimColor();
        }
        
        public void set(final CircularRevealWidget circularRevealWidget, final Integer n) {
            circularRevealWidget.setCircularRevealScrimColor(n);
        }
    }
    
    public static class RevealInfo
    {
        public float centerX;
        public float centerY;
        public float radius;
        
        private RevealInfo() {
        }
        
        public RevealInfo(final float centerX, final float centerY, final float radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
        
        public RevealInfo(final RevealInfo revealInfo) {
            this(revealInfo.centerX, revealInfo.centerY, revealInfo.radius);
        }
        
        public boolean isInvalid() {
            return this.radius == Float.MAX_VALUE;
        }
        
        public void set(final float centerX, final float centerY, final float radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }
        
        public void set(final RevealInfo revealInfo) {
            this.set(revealInfo.centerX, revealInfo.centerY, revealInfo.radius);
        }
    }
}
