package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;

public class PhotoFilterCurvesControl extends View {
   private static final int CurvesSegmentBlacks = 1;
   private static final int CurvesSegmentHighlights = 4;
   private static final int CurvesSegmentMidtones = 3;
   private static final int CurvesSegmentNone = 0;
   private static final int CurvesSegmentShadows = 2;
   private static final int CurvesSegmentWhites = 5;
   private static final int GestureStateBegan = 1;
   private static final int GestureStateCancelled = 4;
   private static final int GestureStateChanged = 2;
   private static final int GestureStateEnded = 3;
   private static final int GestureStateFailed = 5;
   private int activeSegment = 0;
   private Rect actualArea = new Rect();
   private boolean checkForMoving = true;
   private PhotoFilterView.CurvesToolValue curveValue;
   private PhotoFilterCurvesControl.PhotoFilterCurvesControlDelegate delegate;
   private boolean isMoving;
   private float lastX;
   private float lastY;
   private Paint paint = new Paint(1);
   private Paint paintCurve = new Paint(1);
   private Paint paintDash = new Paint(1);
   private Path path = new Path();
   private TextPaint textPaint = new TextPaint(1);

   public PhotoFilterCurvesControl(Context var1, PhotoFilterView.CurvesToolValue var2) {
      super(var1);
      this.setWillNotDraw(false);
      this.curveValue = var2;
      this.paint.setColor(-1711276033);
      this.paint.setStrokeWidth((float)AndroidUtilities.dp(1.0F));
      this.paint.setStyle(Style.STROKE);
      this.paintDash.setColor(-1711276033);
      this.paintDash.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.paintDash.setStyle(Style.STROKE);
      this.paintCurve.setColor(-1);
      this.paintCurve.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.paintCurve.setStyle(Style.STROKE);
      this.textPaint.setColor(-4210753);
      this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
   }

   private void handlePan(int var1, MotionEvent var2) {
      float var3 = var2.getX();
      float var4 = var2.getY();
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 == 3 || var1 == 4 || var1 == 5) {
               this.unselectSegments();
            }
         } else {
            float var5 = Math.min(2.0F, (this.lastY - var4) / 8.0F);
            PhotoFilterView.CurvesValue var7 = null;
            PhotoFilterView.CurvesToolValue var6 = this.curveValue;
            var1 = var6.activeType;
            if (var1 != 0) {
               if (var1 != 1) {
                  if (var1 != 2) {
                     if (var1 == 3) {
                        var7 = var6.blueCurve;
                     }
                  } else {
                     var7 = var6.greenCurve;
                  }
               } else {
                  var7 = var6.redCurve;
               }
            } else {
               var7 = var6.luminanceCurve;
            }

            var1 = this.activeSegment;
            if (var1 != 1) {
               if (var1 != 2) {
                  if (var1 != 3) {
                     if (var1 != 4) {
                        if (var1 == 5) {
                           var7.whitesLevel = Math.max(0.0F, Math.min(100.0F, var7.whitesLevel + var5));
                        }
                     } else {
                        var7.highlightsLevel = Math.max(0.0F, Math.min(100.0F, var7.highlightsLevel + var5));
                     }
                  } else {
                     var7.midtonesLevel = Math.max(0.0F, Math.min(100.0F, var7.midtonesLevel + var5));
                  }
               } else {
                  var7.shadowsLevel = Math.max(0.0F, Math.min(100.0F, var7.shadowsLevel + var5));
               }
            } else {
               var7.blacksLevel = Math.max(0.0F, Math.min(100.0F, var7.blacksLevel + var5));
            }

            this.invalidate();
            PhotoFilterCurvesControl.PhotoFilterCurvesControlDelegate var8 = this.delegate;
            if (var8 != null) {
               var8.valueChanged();
            }

            this.lastX = var3;
            this.lastY = var4;
         }
      } else {
         this.selectSegmentWithPoint(var3);
      }

   }

   private void selectSegmentWithPoint(float var1) {
      if (this.activeSegment == 0) {
         Rect var2 = this.actualArea;
         float var3 = var2.width / 5.0F;
         this.activeSegment = (int)Math.floor((double)((var1 - var2.x) / var3 + 1.0F));
      }
   }

   private void unselectSegments() {
      if (this.activeSegment != 0) {
         this.activeSegment = 0;
      }
   }

   @SuppressLint({"DrawAllocation"})
   protected void onDraw(Canvas var1) {
      float var2 = this.actualArea.width / 5.0F;
      byte var3 = 0;

      int var4;
      Rect var5;
      float var6;
      float var8;
      for(var4 = 0; var4 < 4; ++var4) {
         var5 = this.actualArea;
         var6 = var5.x;
         float var7 = (float)var4 * var2;
         var8 = var5.y;
         var1.drawLine(var6 + var2 + var7, var8, var6 + var2 + var7, var8 + var5.height, this.paint);
      }

      var5 = this.actualArea;
      var8 = var5.x;
      var6 = var5.y;
      var1.drawLine(var8, var6 + var5.height, var8 + var5.width, var6, this.paintDash);
      PhotoFilterView.CurvesValue var9 = null;
      var4 = this.curveValue.activeType;
      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 == 3) {
                  this.paintCurve.setColor(-13404165);
                  var9 = this.curveValue.blueCurve;
               }
            } else {
               this.paintCurve.setColor(-15667555);
               var9 = this.curveValue.greenCurve;
            }
         } else {
            this.paintCurve.setColor(-1229492);
            var9 = this.curveValue.redCurve;
         }
      } else {
         this.paintCurve.setColor(-1);
         var9 = this.curveValue.luminanceCurve;
      }

      Rect var10;
      for(var4 = 0; var4 < 5; ++var4) {
         String var12;
         if (var4 != 0) {
            if (var4 != 1) {
               if (var4 != 2) {
                  if (var4 != 3) {
                     if (var4 != 4) {
                        var12 = "";
                     } else {
                        var12 = String.format(Locale.US, "%.2f", var9.whitesLevel / 100.0F);
                     }
                  } else {
                     var12 = String.format(Locale.US, "%.2f", var9.highlightsLevel / 100.0F);
                  }
               } else {
                  var12 = String.format(Locale.US, "%.2f", var9.midtonesLevel / 100.0F);
               }
            } else {
               var12 = String.format(Locale.US, "%.2f", var9.shadowsLevel / 100.0F);
            }
         } else {
            var12 = String.format(Locale.US, "%.2f", var9.blacksLevel / 100.0F);
         }

         var6 = this.textPaint.measureText(var12);
         var10 = this.actualArea;
         var1.drawText(var12, var10.x + (var2 - var6) / 2.0F + (float)var4 * var2, var10.y + var10.height - (float)AndroidUtilities.dp(4.0F), this.textPaint);
      }

      float[] var13 = var9.interpolateCurve();
      this.invalidate();
      this.path.reset();

      for(var4 = var3; var4 < var13.length / 2; ++var4) {
         int var11;
         Path var14;
         if (var4 == 0) {
            var14 = this.path;
            var10 = this.actualArea;
            var2 = var10.x;
            var11 = var4 * 2;
            var14.moveTo(var2 + var13[var11] * var10.width, var10.y + (1.0F - var13[var11 + 1]) * var10.height);
         } else {
            var14 = this.path;
            var10 = this.actualArea;
            var2 = var10.x;
            var11 = var4 * 2;
            var14.lineTo(var2 + var13[var11] * var10.width, var10.y + (1.0F - var13[var11 + 1]) * var10.height);
         }
      }

      var1.drawPath(this.path, this.paintCurve);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 != 0) {
         label68: {
            if (var2 != 1) {
               if (var2 == 2) {
                  if (this.isMoving) {
                     this.handlePan(2, var1);
                  }

                  return true;
               }

               if (var2 != 3) {
                  if (var2 == 5) {
                     break label68;
                  }

                  if (var2 != 6) {
                     return true;
                  }
               }
            }

            if (this.isMoving) {
               this.handlePan(3, var1);
               this.isMoving = false;
            }

            this.checkForMoving = true;
            return true;
         }
      }

      if (var1.getPointerCount() == 1) {
         if (this.checkForMoving && !this.isMoving) {
            float var3 = var1.getX();
            float var4 = var1.getY();
            this.lastX = var3;
            this.lastY = var4;
            Rect var5 = this.actualArea;
            float var6 = var5.x;
            if (var3 >= var6 && var3 <= var6 + var5.width) {
               var3 = var5.y;
               if (var4 >= var3 && var4 <= var3 + var5.height) {
                  this.isMoving = true;
               }
            }

            this.checkForMoving = false;
            if (this.isMoving) {
               this.handlePan(1, var1);
            }
         }
      } else if (this.isMoving) {
         this.handlePan(3, var1);
         this.checkForMoving = true;
         this.isMoving = false;
      }

      return true;
   }

   public void setActualArea(float var1, float var2, float var3, float var4) {
      Rect var5 = this.actualArea;
      var5.x = var1;
      var5.y = var2;
      var5.width = var3;
      var5.height = var4;
   }

   public void setDelegate(PhotoFilterCurvesControl.PhotoFilterCurvesControlDelegate var1) {
      this.delegate = var1;
   }

   public interface PhotoFilterCurvesControlDelegate {
      void valueChanged();
   }
}
