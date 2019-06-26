package com.airbnb.lottie;

import android.graphics.ColorFilter;
import android.graphics.PointF;
import com.airbnb.lottie.value.ScaleXY;

public interface LottieProperty {
   Integer COLOR = 1;
   ColorFilter COLOR_FILTER;
   Float CORNER_RADIUS;
   PointF ELLIPSE_SIZE = new PointF();
   Integer[] GRADIENT_COLOR;
   Integer OPACITY = 4;
   Float POLYSTAR_INNER_RADIUS;
   Float POLYSTAR_INNER_ROUNDEDNESS;
   Float POLYSTAR_OUTER_RADIUS;
   Float POLYSTAR_OUTER_ROUNDEDNESS;
   Float POLYSTAR_POINTS;
   Float POLYSTAR_ROTATION;
   PointF POSITION;
   PointF RECTANGLE_SIZE = new PointF();
   Float REPEATER_COPIES;
   Float REPEATER_OFFSET;
   Integer STROKE_COLOR = 2;
   Float STROKE_WIDTH;
   Float TEXT_TRACKING;
   Float TIME_REMAP;
   PointF TRANSFORM_ANCHOR_POINT = new PointF();
   Float TRANSFORM_END_OPACITY;
   Integer TRANSFORM_OPACITY = 3;
   PointF TRANSFORM_POSITION = new PointF();
   Float TRANSFORM_ROTATION;
   ScaleXY TRANSFORM_SCALE;
   Float TRANSFORM_SKEW;
   Float TRANSFORM_SKEW_ANGLE;
   Float TRANSFORM_START_OPACITY;

   static {
      Float var0 = 0.0F;
      CORNER_RADIUS = var0;
      POSITION = new PointF();
      TRANSFORM_SCALE = new ScaleXY();
      TRANSFORM_ROTATION = 1.0F;
      TRANSFORM_SKEW = var0;
      TRANSFORM_SKEW_ANGLE = var0;
      STROKE_WIDTH = 2.0F;
      TEXT_TRACKING = 3.0F;
      REPEATER_COPIES = 4.0F;
      REPEATER_OFFSET = 5.0F;
      POLYSTAR_POINTS = 6.0F;
      POLYSTAR_ROTATION = 7.0F;
      POLYSTAR_INNER_RADIUS = 8.0F;
      POLYSTAR_OUTER_RADIUS = 9.0F;
      POLYSTAR_INNER_ROUNDEDNESS = 10.0F;
      POLYSTAR_OUTER_ROUNDEDNESS = 11.0F;
      var0 = 12.0F;
      TRANSFORM_START_OPACITY = var0;
      TRANSFORM_END_OPACITY = var0;
      TIME_REMAP = 13.0F;
      COLOR_FILTER = new ColorFilter();
      GRADIENT_COLOR = new Integer[0];
   }
}
