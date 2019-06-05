package org.mozilla.focus.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.support.v4.content.ContextCompat;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.utils.ViewUtils;

public final class FocusView extends View {
   private int centerX;
   private int centerY;
   private final Path path;
   private int radius;
   private int statusBarOffset;
   private final Paint transparentPaint;

   public FocusView(Context var1, int var2, int var3, int var4) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      super(var1);
      this.transparentPaint = new Paint();
      this.path = new Path();
      this.centerX = var2;
      this.centerY = var3;
      this.statusBarOffset = ViewUtils.getStatusBarHeight((Activity)var1);
      this.radius = var4;
      this.initPaints();
   }

   private final void initPaints() {
      this.transparentPaint.setColor(0);
      this.transparentPaint.setStrokeWidth(10.0F);
   }

   protected void onDraw(Canvas var1) {
      Intrinsics.checkParameterIsNotNull(var1, "canvas");
      super.onDraw(var1);
      this.path.reset();
      this.path.addCircle((float)this.centerX, (float)(this.centerY - this.statusBarOffset), (float)this.radius, Direction.CW);
      this.path.setFillType(FillType.INVERSE_EVEN_ODD);
      var1.drawCircle((float)this.centerX, (float)(this.centerY - this.statusBarOffset), (float)this.radius, this.transparentPaint);
      var1.clipPath(this.path);
      var1.drawColor(ContextCompat.getColor(this.getContext(), 2131099821));
   }
}
