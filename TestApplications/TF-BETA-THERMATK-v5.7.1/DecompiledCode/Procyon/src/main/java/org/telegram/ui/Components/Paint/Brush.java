// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Paint;

import android.graphics.BitmapFactory;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.BitmapFactory$Options;
import android.graphics.Bitmap;

public interface Brush
{
    float getAlpha();
    
    float getAngle();
    
    float getScale();
    
    float getSpacing();
    
    Bitmap getStamp();
    
    boolean isLightSaber();
    
    public static class Elliptical implements Brush
    {
        @Override
        public float getAlpha() {
            return 0.3f;
        }
        
        @Override
        public float getAngle() {
            return (float)Math.toRadians(125.0);
        }
        
        @Override
        public float getScale() {
            return 1.5f;
        }
        
        @Override
        public float getSpacing() {
            return 0.04f;
        }
        
        @Override
        public Bitmap getStamp() {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inScaled = false;
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), 2131165727, bitmapFactory$Options);
        }
        
        @Override
        public boolean isLightSaber() {
            return false;
        }
    }
    
    public static class Neon implements Brush
    {
        @Override
        public float getAlpha() {
            return 0.7f;
        }
        
        @Override
        public float getAngle() {
            return 0.0f;
        }
        
        @Override
        public float getScale() {
            return 1.45f;
        }
        
        @Override
        public float getSpacing() {
            return 0.07f;
        }
        
        @Override
        public Bitmap getStamp() {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inScaled = false;
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), 2131165729, bitmapFactory$Options);
        }
        
        @Override
        public boolean isLightSaber() {
            return true;
        }
    }
    
    public static class Radial implements Brush
    {
        @Override
        public float getAlpha() {
            return 0.85f;
        }
        
        @Override
        public float getAngle() {
            return 0.0f;
        }
        
        @Override
        public float getScale() {
            return 1.0f;
        }
        
        @Override
        public float getSpacing() {
            return 0.15f;
        }
        
        @Override
        public Bitmap getStamp() {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inScaled = false;
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), 2131165731, bitmapFactory$Options);
        }
        
        @Override
        public boolean isLightSaber() {
            return false;
        }
    }
}
