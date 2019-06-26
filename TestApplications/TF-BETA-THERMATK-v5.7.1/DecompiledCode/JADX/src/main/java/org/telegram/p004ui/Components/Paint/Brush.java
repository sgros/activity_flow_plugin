package org.telegram.p004ui.Components.Paint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C1067R;

/* renamed from: org.telegram.ui.Components.Paint.Brush */
public interface Brush {

    /* renamed from: org.telegram.ui.Components.Paint.Brush$Elliptical */
    public static class Elliptical implements Brush {
        public float getAlpha() {
            return 0.3f;
        }

        public float getScale() {
            return 1.5f;
        }

        public float getSpacing() {
            return 0.04f;
        }

        public boolean isLightSaber() {
            return false;
        }

        public float getAngle() {
            return (float) Math.toRadians(125.0d);
        }

        public Bitmap getStamp() {
            Options options = new Options();
            options.inScaled = false;
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), C1067R.C1065drawable.paint_elliptical_brush, options);
        }
    }

    /* renamed from: org.telegram.ui.Components.Paint.Brush$Neon */
    public static class Neon implements Brush {
        public float getAlpha() {
            return 0.7f;
        }

        public float getAngle() {
            return 0.0f;
        }

        public float getScale() {
            return 1.45f;
        }

        public float getSpacing() {
            return 0.07f;
        }

        public boolean isLightSaber() {
            return true;
        }

        public Bitmap getStamp() {
            Options options = new Options();
            options.inScaled = false;
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), C1067R.C1065drawable.paint_neon_brush, options);
        }
    }

    /* renamed from: org.telegram.ui.Components.Paint.Brush$Radial */
    public static class Radial implements Brush {
        public float getAlpha() {
            return 0.85f;
        }

        public float getAngle() {
            return 0.0f;
        }

        public float getScale() {
            return 1.0f;
        }

        public float getSpacing() {
            return 0.15f;
        }

        public boolean isLightSaber() {
            return false;
        }

        public Bitmap getStamp() {
            Options options = new Options();
            options.inScaled = false;
            return BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), C1067R.C1065drawable.paint_radial_brush, options);
        }
    }

    float getAlpha();

    float getAngle();

    float getScale();

    float getSpacing();

    Bitmap getStamp();

    boolean isLightSaber();
}