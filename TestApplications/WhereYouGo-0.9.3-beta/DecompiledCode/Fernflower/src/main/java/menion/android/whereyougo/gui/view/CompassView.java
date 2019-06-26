package menion.android.whereyougo.gui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Utils;
import menion.android.whereyougo.utils.UtilsFormat;

public class CompassView extends View {
   private Drawable bitCompassArrow;
   private Drawable bitCompassBg;
   private float cX1;
   private float cY1;
   private int lastWidth;
   private float mAzimuth;
   private float mAzimuthToTarget;
   private double mDistanceToTarget;
   private Paint paintValueAzimuth;
   private Paint paintValueDistance;
   private Paint paintValueLabel;
   private Paint paintValueTilt;
   private float r1;

   public CompassView(Context var1) {
      super(var1);
      this.initialize();
   }

   public CompassView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.initialize();
   }

   private void drawCompassTexts(Canvas var1) {
      float var2 = this.r1 / 20.0F;
      var1.drawText(Locale.getString(2131165195), this.cX1, this.cY1 - this.paintValueDistance.getTextSize() - var2, this.paintValueLabel);
      var1.drawText(UtilsFormat.formatDistance(this.mDistanceToTarget, false), this.cX1, this.cY1 - var2, this.paintValueDistance);
      var1.drawText(Locale.getString(2131165189), this.cX1, this.cY1 + this.paintValueLabel.getTextSize() + var2, this.paintValueLabel);
      var1.drawText(UtilsFormat.formatAngle((double)(this.mAzimuthToTarget - this.mAzimuth)), this.cX1, this.cY1 + this.paintValueLabel.getTextSize() + this.paintValueAzimuth.getTextSize() + var2, this.paintValueAzimuth);
   }

   private void initialize() {
      this.mAzimuth = 0.0F;
      this.mAzimuthToTarget = 0.0F;
      this.bitCompassBg = Images.getImageD(2130837574);
      this.bitCompassArrow = Images.getImageD(2130837575);
      Paint var1 = new Paint();
      var1.setAntiAlias(true);
      var1.setFilterBitmap(true);
      this.paintValueLabel = new Paint();
      this.paintValueLabel.setAntiAlias(true);
      this.paintValueLabel.setTextAlign(Align.CENTER);
      this.paintValueLabel.setColor(-1);
      this.paintValueLabel.setTextSize(Utils.getDpPixels(12.0F));
      this.paintValueDistance = new Paint(this.paintValueLabel);
      this.paintValueAzimuth = new Paint(this.paintValueDistance);
      this.paintValueTilt = new Paint(this.paintValueDistance);
      this.paintValueTilt.setColor(Color.parseColor("#00a2e6"));
      this.paintValueTilt.setTypeface(Typeface.DEFAULT_BOLD);
      this.paintValueTilt.setShadowLayer(Utils.getDpPixels(3.0F), 0.0F, 0.0F, -16777216);
   }

   private void setConstants(Canvas var1) {
      if (this.lastWidth != var1.getWidth()) {
         this.lastWidth = var1.getWidth();
         int var2 = var1.getClipBounds().width();
         int var3 = var1.getClipBounds().height();
         this.r1 = (float)Math.min(var2, var3) / 2.0F * 0.9F;
         this.cX1 = (float)var2 / 2.0F;
         this.cY1 = (float)var3 / 2.0F;
         this.paintValueDistance.setTextSize(this.r1 / 5.0F);
         this.paintValueAzimuth.setTextSize(this.r1 / 6.0F);
         this.paintValueTilt.setTextSize(this.r1 / 8.0F);
      }

   }

   public void draw(Canvas var1) {
      super.draw(var1);
      this.setConstants(var1);
      var1.save();
      var1.translate(this.cX1, this.cY1);
      var1.rotate(-this.mAzimuth);
      this.bitCompassBg.setBounds((int)(-this.r1), (int)(-this.r1), (int)this.r1, (int)this.r1);
      this.bitCompassBg.draw(var1);
      var1.restore();
      if (A.getGuidingContent().isGuiding()) {
         var1.save();
         var1.translate(this.cX1, this.cY1);
         var1.rotate(this.mAzimuthToTarget - this.mAzimuth);
         this.bitCompassArrow.setBounds((int)(-this.r1), (int)(-this.r1), (int)this.r1, (int)this.r1);
         this.bitCompassArrow.draw(var1);
         var1.restore();
      }

      this.drawCompassTexts(var1);
   }

   public void moveAngles(float var1, float var2, float var3, float var4) {
      this.mAzimuthToTarget = var1;
      this.mAzimuth = var2;
      this.invalidate();
   }

   public void setDistance(double var1) {
      this.mDistanceToTarget = var1;
      this.invalidate();
   }
}
