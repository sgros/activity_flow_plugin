package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.widget.EditText;

public class EditTextOutline extends EditText {
   private Bitmap mCache;
   private final Canvas mCanvas = new Canvas();
   private final TextPaint mPaint = new TextPaint();
   private int mStrokeColor = 0;
   private float mStrokeWidth;
   private boolean mUpdateCachedBitmap = true;

   public EditTextOutline(Context var1) {
      super(var1);
      this.mPaint.setAntiAlias(true);
      this.mPaint.setStyle(Style.FILL_AND_STROKE);
   }

   protected void onDraw(Canvas var1) {
      if (this.mCache != null && this.mStrokeColor != 0) {
         if (this.mUpdateCachedBitmap) {
            int var2 = this.getMeasuredWidth();
            int var3 = this.getPaddingLeft();
            int var4 = this.getPaddingRight();
            int var5 = this.getMeasuredHeight();
            String var6 = this.getText().toString();
            this.mCanvas.setBitmap(this.mCache);
            this.mCanvas.drawColor(0, Mode.CLEAR);
            float var7 = this.mStrokeWidth;
            if (var7 <= 0.0F) {
               var7 = (float)Math.ceil((double)(this.getTextSize() / 11.5F));
            }

            this.mPaint.setStrokeWidth(var7);
            this.mPaint.setColor(this.mStrokeColor);
            this.mPaint.setTextSize(this.getTextSize());
            this.mPaint.setTypeface(this.getTypeface());
            this.mPaint.setStyle(Style.FILL_AND_STROKE);
            StaticLayout var8 = new StaticLayout(var6, this.mPaint, var2 - var3 - var4, Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            this.mCanvas.save();
            var7 = (float)(var5 - this.getPaddingTop() - this.getPaddingBottom() - var8.getHeight()) / 2.0F;
            this.mCanvas.translate((float)this.getPaddingLeft(), var7 + (float)this.getPaddingTop());
            var8.draw(this.mCanvas);
            this.mCanvas.restore();
            this.mUpdateCachedBitmap = false;
         }

         var1.drawBitmap(this.mCache, 0.0F, 0.0F, this.mPaint);
      }

      super.onDraw(var1);
   }

   protected void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      if (var1 > 0 && var2 > 0) {
         this.mUpdateCachedBitmap = true;
         this.mCache = Bitmap.createBitmap(var1, var2, Config.ARGB_8888);
      } else {
         this.mCache = null;
      }

   }

   protected void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      super.onTextChanged(var1, var2, var3, var4);
      this.mUpdateCachedBitmap = true;
   }

   public void setStrokeColor(int var1) {
      this.mStrokeColor = var1;
      this.mUpdateCachedBitmap = true;
      this.invalidate();
   }

   public void setStrokeWidth(float var1) {
      this.mStrokeWidth = var1;
      this.mUpdateCachedBitmap = true;
      this.invalidate();
   }
}
