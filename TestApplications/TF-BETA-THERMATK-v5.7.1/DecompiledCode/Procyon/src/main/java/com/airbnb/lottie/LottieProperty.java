// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import com.airbnb.lottie.value.ScaleXY;
import android.graphics.PointF;
import android.graphics.ColorFilter;

public interface LottieProperty
{
    public static final Integer COLOR = 1;
    public static final ColorFilter COLOR_FILTER = new ColorFilter();
    public static final Float CORNER_RADIUS;
    public static final PointF ELLIPSE_SIZE = new PointF();
    public static final Integer[] GRADIENT_COLOR = new Integer[0];
    public static final Integer OPACITY = 4;
    public static final Float POLYSTAR_INNER_RADIUS = 8.0f;
    public static final Float POLYSTAR_INNER_ROUNDEDNESS = 10.0f;
    public static final Float POLYSTAR_OUTER_RADIUS = 9.0f;
    public static final Float POLYSTAR_OUTER_ROUNDEDNESS = 11.0f;
    public static final Float POLYSTAR_POINTS = 6.0f;
    public static final Float POLYSTAR_ROTATION = 7.0f;
    public static final PointF POSITION = new PointF();
    public static final PointF RECTANGLE_SIZE = new PointF();
    public static final Float REPEATER_COPIES = 4.0f;
    public static final Float REPEATER_OFFSET = 5.0f;
    public static final Integer STROKE_COLOR = 2;
    public static final Float STROKE_WIDTH = 2.0f;
    public static final Float TEXT_TRACKING = 3.0f;
    public static final Float TIME_REMAP = 13.0f;
    public static final PointF TRANSFORM_ANCHOR_POINT = new PointF();
    public static final Float TRANSFORM_END_OPACITY = TRANSFORM_START_OPACITY = 12.0f;
    public static final Integer TRANSFORM_OPACITY = 3;
    public static final PointF TRANSFORM_POSITION = new PointF();
    public static final Float TRANSFORM_ROTATION = 1.0f;
    public static final ScaleXY TRANSFORM_SCALE = new ScaleXY();
    public static final Float TRANSFORM_SKEW = n;
    public static final Float TRANSFORM_SKEW_ANGLE = n;
    public static final Float TRANSFORM_START_OPACITY;
    
    default static {
        final Float n = CORNER_RADIUS = 0.0f;
    }
}
