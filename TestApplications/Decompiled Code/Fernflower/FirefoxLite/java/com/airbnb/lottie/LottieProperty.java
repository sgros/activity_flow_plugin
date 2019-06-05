package com.airbnb.lottie;

import android.graphics.ColorFilter;
import android.graphics.PointF;
import com.airbnb.lottie.value.ScaleXY;

public interface LottieProperty {
   Integer COLOR = 1;
   ColorFilter COLOR_FILTER = new ColorFilter();
   PointF ELLIPSE_SIZE = new PointF();
   Integer OPACITY = 4;
   Float POLYSTAR_INNER_RADIUS = 8.0F;
   Float POLYSTAR_INNER_ROUNDEDNESS = 10.0F;
   Float POLYSTAR_OUTER_RADIUS = 9.0F;
   Float POLYSTAR_OUTER_ROUNDEDNESS = 11.0F;
   Float POLYSTAR_POINTS = 6.0F;
   Float POLYSTAR_ROTATION = 7.0F;
   PointF POSITION = new PointF();
   Float REPEATER_COPIES = 4.0F;
   Float REPEATER_OFFSET = 5.0F;
   Integer STROKE_COLOR = 2;
   Float STROKE_WIDTH = 2.0F;
   Float TEXT_TRACKING = 3.0F;
   Float TIME_REMAP = 13.0F;
   PointF TRANSFORM_ANCHOR_POINT = new PointF();
   Float TRANSFORM_END_OPACITY = 12.0F;
   Integer TRANSFORM_OPACITY = 3;
   PointF TRANSFORM_POSITION = new PointF();
   Float TRANSFORM_ROTATION = 1.0F;
   ScaleXY TRANSFORM_SCALE = new ScaleXY();
   Float TRANSFORM_START_OPACITY = 12.0F;
}
