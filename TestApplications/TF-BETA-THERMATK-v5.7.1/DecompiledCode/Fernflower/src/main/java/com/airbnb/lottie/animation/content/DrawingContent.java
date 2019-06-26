package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

public interface DrawingContent extends Content {
   void draw(Canvas var1, Matrix var2, int var3);

   void getBounds(RectF var1, Matrix var2, boolean var3);
}
