package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;

public class PhotoFilterBlurControl extends FrameLayout {
   private static final float BlurInsetProximity = (float)AndroidUtilities.dp(20.0F);
   private static final float BlurMinimumDifference = 0.02F;
   private static final float BlurMinimumFalloff = 0.1F;
   private static final float BlurViewCenterInset = (float)AndroidUtilities.dp(30.0F);
   private static final float BlurViewRadiusInset = (float)AndroidUtilities.dp(30.0F);
   private final int GestureStateBegan = 1;
   private final int GestureStateCancelled = 4;
   private final int GestureStateChanged = 2;
   private final int GestureStateEnded = 3;
   private final int GestureStateFailed = 5;
   private PhotoFilterBlurControl.BlurViewActiveControl activeControl;
   private Size actualAreaSize = new Size();
   private float angle;
   private Paint arcPaint = new Paint(1);
   private RectF arcRect = new RectF();
   private Point centerPoint = new Point(0.5F, 0.5F);
   private boolean checkForMoving = true;
   private boolean checkForZooming;
   private PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate delegate;
   private float falloff = 0.15F;
   private boolean isMoving;
   private boolean isZooming;
   private Paint paint = new Paint(1);
   private float pointerScale = 1.0F;
   private float pointerStartX;
   private float pointerStartY;
   private float size = 0.35F;
   private Point startCenterPoint = new Point();
   private float startDistance;
   private float startPointerDistance;
   private float startRadius;
   private int type;

   public PhotoFilterBlurControl(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.paint.setColor(-1);
      this.arcPaint.setColor(-1);
      this.arcPaint.setStrokeWidth((float)AndroidUtilities.dp(2.0F));
      this.arcPaint.setStyle(Style.STROKE);
   }

   private float degreesToRadians(float var1) {
      return var1 * 3.1415927F / 180.0F;
   }

   private Point getActualCenterPoint() {
      float var1 = (float)this.getWidth();
      float var2 = this.actualAreaSize.width;
      float var3 = (var1 - var2) / 2.0F;
      float var4 = this.centerPoint.x;
      int var5;
      if (VERSION.SDK_INT >= 21) {
         var5 = AndroidUtilities.statusBarHeight;
      } else {
         var5 = 0;
      }

      float var6 = (float)var5;
      float var7 = (float)this.getHeight();
      Size var8 = this.actualAreaSize;
      var1 = var8.height;
      var7 = (var7 - var1) / 2.0F;
      float var9 = var8.width;
      return new Point(var3 + var4 * var2, var6 + var7 - (var9 - var1) / 2.0F + this.centerPoint.y * var9);
   }

   private float getActualInnerRadius() {
      Size var1 = this.actualAreaSize;
      float var2 = var1.width;
      float var3 = var1.height;
      if (var2 <= var3) {
         var3 = var2;
      }

      return var3 * this.falloff;
   }

   private float getActualOuterRadius() {
      Size var1 = this.actualAreaSize;
      float var2 = var1.width;
      float var3 = var1.height;
      if (var2 > var3) {
         var2 = var3;
      }

      return var2 * this.size;
   }

   private float getDistance(MotionEvent var1) {
      if (var1.getPointerCount() != 2) {
         return 0.0F;
      } else {
         float var2 = var1.getX(0);
         float var3 = var1.getY(0);
         float var4 = var1.getX(1);
         float var5 = var1.getY(1);
         var2 -= var4;
         var3 -= var5;
         return (float)Math.sqrt((double)(var2 * var2 + var3 * var3));
      }
   }

   private void handlePan(int var1, MotionEvent var2) {
      float var3 = var2.getX();
      float var4 = var2.getY();
      Point var5 = this.getActualCenterPoint();
      Point var6 = new Point(var3 - var5.x, var4 - var5.y);
      float var7 = var6.x;
      float var8 = var6.y;
      float var9 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
      Size var10 = this.actualAreaSize;
      var7 = var10.width;
      var8 = var10.height;
      if (var7 > var8) {
         var7 = var8;
      }

      float var11 = this.falloff * var7;
      float var12 = this.size * var7;
      double var13 = (double)var6.x;
      double var15 = (double)this.degreesToRadians(this.angle);
      Double.isNaN(var15);
      double var17 = Math.cos(var15 + 1.5707963267948966D);
      Double.isNaN(var13);
      var15 = (double)var6.y;
      double var19 = (double)this.degreesToRadians(this.angle);
      Double.isNaN(var19);
      var19 = Math.sin(var19 + 1.5707963267948966D);
      Double.isNaN(var15);
      float var21 = (float)Math.abs(var13 * var17 + var15 * var19);
      boolean var22 = false;
      byte var23 = 0;
      var8 = 0.0F;
      boolean var24;
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 == 3 || var1 == 4 || var1 == 5) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlNone;
               this.setSelected(false, true);
            }
         } else {
            var1 = this.type;
            Size var25;
            if (var1 != 0) {
               if (var1 == 1) {
                  var1 = null.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
                  if (var1 != 1) {
                     if (var1 != 2) {
                        if (var1 == 3) {
                           var8 = this.startDistance;
                           this.size = Math.max(this.falloff + 0.02F, (this.startRadius + (var9 - var8)) / var7);
                        }
                     } else {
                        var8 = this.startDistance;
                        this.falloff = Math.min(Math.max(0.1F, (this.startRadius + (var9 - var8)) / var7), this.size - 0.02F);
                     }
                  } else {
                     var8 = this.pointerStartX;
                     var7 = this.pointerStartY;
                     var12 = ((float)this.getWidth() - this.actualAreaSize.width) / 2.0F;
                     var9 = (float)this.getHeight();
                     var25 = this.actualAreaSize;
                     var21 = var25.height;
                     Rect var27 = new Rect(var12, (var9 - var21) / 2.0F, var25.width, var21);
                     var9 = var27.x;
                     var8 = Math.max(var9, Math.min(var27.width + var9, this.startCenterPoint.x + (var3 - var8)));
                     var3 = var27.y;
                     Point var31 = new Point(var8, Math.max(var3, Math.min(var27.height + var3, this.startCenterPoint.y + (var4 - var7))));
                     var4 = var31.x;
                     var7 = var27.x;
                     Size var29 = this.actualAreaSize;
                     var8 = var29.width;
                     this.centerPoint = new Point((var4 - var7) / var8, (var31.y - var27.y + (var8 - var29.height) / 2.0F) / var8);
                  }
               }
            } else {
               var1 = null.$SwitchMap$org$telegram$ui$Components$PhotoFilterBlurControl$BlurViewActiveControl[this.activeControl.ordinal()];
               if (var1 != 1) {
                  if (var1 == 2) {
                     var8 = this.startDistance;
                     this.falloff = Math.min(Math.max(0.1F, (this.startRadius + (var21 - var8)) / var7), this.size - 0.02F);
                  } else if (var1 != 3) {
                     if (var1 == 4) {
                        var7 = var3 - this.pointerStartX;
                        var8 = var4 - this.pointerStartY;
                        if (var3 > var5.x) {
                           var24 = true;
                        } else {
                           var24 = false;
                        }

                        if (var4 > var5.y) {
                           var22 = true;
                        } else {
                           var22 = false;
                        }

                        byte var26;
                        label203: {
                           if (!var24 && !var22) {
                              if (Math.abs(var8) > Math.abs(var7)) {
                                 var26 = var23;
                                 if (var8 >= 0.0F) {
                                    break label203;
                                 }
                              } else {
                                 var26 = var23;
                                 if (var7 <= 0.0F) {
                                    break label203;
                                 }
                              }
                           } else if (var24 && !var22) {
                              if (Math.abs(var8) > Math.abs(var7)) {
                                 var26 = var23;
                                 if (var8 <= 0.0F) {
                                    break label203;
                                 }
                              } else {
                                 var26 = var23;
                                 if (var7 <= 0.0F) {
                                    break label203;
                                 }
                              }
                           } else if (var24 && var22) {
                              if (Math.abs(var8) > Math.abs(var7)) {
                                 var26 = var23;
                                 if (var8 <= 0.0F) {
                                    break label203;
                                 }
                              } else {
                                 var26 = var23;
                                 if (var7 >= 0.0F) {
                                    break label203;
                                 }
                              }
                           } else if (Math.abs(var8) > Math.abs(var7)) {
                              var26 = var23;
                              if (var8 >= 0.0F) {
                                 break label203;
                              }
                           } else {
                              var26 = var23;
                              if (var7 >= 0.0F) {
                                 break label203;
                              }
                           }

                           var26 = 1;
                        }

                        var7 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
                        this.angle += var7 * (float)(var26 * 2 - 1) / 3.1415927F / 1.15F;
                        this.pointerStartX = var3;
                        this.pointerStartY = var4;
                     }
                  } else {
                     var8 = this.startDistance;
                     this.size = Math.max(this.falloff + 0.02F, (this.startRadius + (var21 - var8)) / var7);
                  }
               } else {
                  var8 = this.pointerStartX;
                  var7 = this.pointerStartY;
                  var9 = ((float)this.getWidth() - this.actualAreaSize.width) / 2.0F;
                  var12 = (float)this.getHeight();
                  var25 = this.actualAreaSize;
                  var21 = var25.height;
                  Rect var30 = new Rect(var9, (var12 - var21) / 2.0F, var25.width, var21);
                  var9 = var30.x;
                  var8 = Math.max(var9, Math.min(var30.width + var9, this.startCenterPoint.x + (var3 - var8)));
                  var3 = var30.y;
                  var5 = new Point(var8, Math.max(var3, Math.min(var30.height + var3, this.startCenterPoint.y + (var4 - var7))));
                  var4 = var5.x;
                  var7 = var30.x;
                  var25 = this.actualAreaSize;
                  var8 = var25.width;
                  this.centerPoint = new Point((var4 - var7) / var8, (var5.y - var30.y + (var8 - var25.height) / 2.0F) / var8);
               }
            }

            this.invalidate();
            PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate var28 = this.delegate;
            if (var28 != null) {
               var28.valueChanged(this.centerPoint, this.falloff, this.size, this.degreesToRadians(this.angle) + 1.5707964F);
            }
         }
      } else {
         this.pointerStartX = var2.getX();
         this.pointerStartY = var2.getY();
         var24 = var22;
         if (Math.abs(var12 - var11) < BlurInsetProximity) {
            var24 = true;
         }

         if (var24) {
            var7 = 0.0F;
         } else {
            var7 = BlurViewRadiusInset;
         }

         if (!var24) {
            var8 = BlurViewRadiusInset;
         }

         var1 = this.type;
         if (var1 == 0) {
            if (var9 < BlurViewCenterInset) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlCenter;
               this.startCenterPoint = var5;
            } else if (var21 > var11 - BlurViewRadiusInset && var21 < var11 + var7) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlInnerRadius;
               this.startDistance = var21;
               this.startRadius = var11;
            } else if (var21 > var12 - var8 && var21 < var12 + BlurViewRadiusInset) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlOuterRadius;
               this.startDistance = var21;
               this.startRadius = var12;
            } else {
               var7 = BlurViewRadiusInset;
               if (var21 <= var11 - var7 || var21 >= var12 + var7) {
                  this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlRotation;
               }
            }
         } else if (var1 == 1) {
            if (var9 < BlurViewCenterInset) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlCenter;
               this.startCenterPoint = var5;
            } else if (var9 > var11 - BlurViewRadiusInset && var9 < var11 + var7) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlInnerRadius;
               this.startDistance = var9;
               this.startRadius = var11;
            } else if (var9 > var12 - var8 && var9 < var12 + BlurViewRadiusInset) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlOuterRadius;
               this.startDistance = var9;
               this.startRadius = var12;
            }
         }

         this.setSelected(true, true);
      }

   }

   private void handlePinch(int var1, MotionEvent var2) {
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 == 3 || var1 == 4 || var1 == 5) {
               this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlNone;
               this.setSelected(false, true);
            }

            return;
         }
      } else {
         this.startPointerDistance = this.getDistance(var2);
         this.pointerScale = 1.0F;
         this.activeControl = PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlWholeArea;
         this.setSelected(true, true);
      }

      float var3 = this.getDistance(var2);
      this.pointerScale += (var3 - this.startPointerDistance) / AndroidUtilities.density * 0.01F;
      this.falloff = Math.max(0.1F, this.falloff * this.pointerScale);
      this.size = Math.max(this.falloff + 0.02F, this.size * this.pointerScale);
      this.pointerScale = 1.0F;
      this.startPointerDistance = var3;
      this.invalidate();
      PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate var4 = this.delegate;
      if (var4 != null) {
         var4.valueChanged(this.centerPoint, this.falloff, this.size, this.degreesToRadians(this.angle) + 1.5707964F);
      }

   }

   private void setSelected(boolean var1, boolean var2) {
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      Point var2 = this.getActualCenterPoint();
      float var3 = this.getActualInnerRadius();
      float var4 = this.getActualOuterRadius();
      var1.translate(var2.x, var2.y);
      int var5 = this.type;
      float var6;
      if (var5 == 0) {
         var1.rotate(this.angle);
         var6 = (float)AndroidUtilities.dp(6.0F);
         float var7 = (float)AndroidUtilities.dp(12.0F);
         float var8 = (float)AndroidUtilities.dp(1.5F);

         float var9;
         float var10;
         float var11;
         float var12;
         float var14;
         for(var5 = 0; var5 < 30; ++var5) {
            var9 = (float)var5;
            var10 = var7 + var6;
            var11 = var9 * var10;
            var12 = -var3;
            var9 = var11 + var7;
            float var13 = var8 - var3;
            var1.drawRect(var11, var12, var9, var13, this.paint);
            var14 = (float)(-var5) * var10 - var6;
            var10 = var14 - var7;
            var1.drawRect(var10, var12, var14, var13, this.paint);
            var12 = var8 + var3;
            var1.drawRect(var11, var3, var9, var12, this.paint);
            var1.drawRect(var10, var3, var14, var12, this.paint);
         }

         var3 = (float)AndroidUtilities.dp(6.0F);

         for(var5 = 0; var5 < 64; ++var5) {
            var7 = (float)var5;
            var10 = var3 + var6;
            var9 = var7 * var10;
            var11 = -var4;
            var7 = var3 + var9;
            var14 = var8 - var4;
            var1.drawRect(var9, var11, var7, var14, this.paint);
            var10 = (float)(-var5) * var10 - var6;
            var12 = var10 - var3;
            var1.drawRect(var12, var11, var10, var14, this.paint);
            var11 = var8 + var4;
            var1.drawRect(var9, var4, var7, var11, this.paint);
            var1.drawRect(var12, var4, var10, var11, this.paint);
         }
      } else if (var5 == 1) {
         RectF var15 = this.arcRect;
         var6 = -var3;
         var15.set(var6, var6, var3, var3);

         for(var5 = 0; var5 < 22; ++var5) {
            var1.drawArc(this.arcRect, 16.35F * (float)var5, 10.2F, false, this.arcPaint);
         }

         var15 = this.arcRect;
         var6 = -var4;
         var15.set(var6, var6, var4, var4);

         for(var5 = 0; var5 < 64; ++var5) {
            var1.drawArc(this.arcRect, 5.62F * (float)var5, 3.6F, false, this.arcPaint);
         }
      }

      var1.drawCircle(0.0F, 0.0F, (float)AndroidUtilities.dp(8.0F), this.paint);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      if (var2 != 0) {
         label141: {
            if (var2 != 1) {
               if (var2 == 2) {
                  if (this.isMoving) {
                     this.handlePan(2, var1);
                  } else if (this.isZooming) {
                     this.handlePinch(2, var1);
                     return true;
                  }

                  return true;
               }

               if (var2 != 3) {
                  if (var2 == 5) {
                     break label141;
                  }

                  if (var2 != 6) {
                     return true;
                  }
               }
            }

            if (this.isMoving) {
               this.handlePan(3, var1);
               this.isMoving = false;
            } else if (this.isZooming) {
               this.handlePinch(3, var1);
               this.isZooming = false;
            }

            this.checkForMoving = true;
            this.checkForZooming = true;
            return true;
         }
      }

      if (var1.getPointerCount() == 1) {
         if (this.checkForMoving && !this.isMoving) {
            float var3 = var1.getX();
            float var4 = var1.getY();
            Point var5 = this.getActualCenterPoint();
            var5 = new Point(var3 - var5.x, var4 - var5.y);
            var3 = var5.x;
            var4 = var5.y;
            float var6 = (float)Math.sqrt((double)(var3 * var3 + var4 * var4));
            float var7 = this.getActualInnerRadius();
            float var8 = this.getActualOuterRadius();
            boolean var18;
            if (Math.abs(var8 - var7) < BlurInsetProximity) {
               var18 = true;
            } else {
               var18 = false;
            }

            var3 = 0.0F;
            if (var18) {
               var4 = 0.0F;
            } else {
               var4 = BlurViewRadiusInset;
            }

            if (!var18) {
               var3 = BlurViewRadiusInset;
            }

            var2 = this.type;
            if (var2 == 0) {
               double var9 = (double)var5.x;
               double var11 = (double)this.degreesToRadians(this.angle);
               Double.isNaN(var11);
               var11 = Math.cos(var11 + 1.5707963267948966D);
               Double.isNaN(var9);
               double var13 = (double)var5.y;
               double var15 = (double)this.degreesToRadians(this.angle);
               Double.isNaN(var15);
               var15 = Math.sin(var15 + 1.5707963267948966D);
               Double.isNaN(var13);
               float var17 = (float)Math.abs(var9 * var11 + var13 * var15);
               if (var6 < BlurViewCenterInset) {
                  this.isMoving = true;
               } else if (var17 > var7 - BlurViewRadiusInset && var17 < var4 + var7) {
                  this.isMoving = true;
               } else if (var17 > var8 - var3 && var17 < BlurViewRadiusInset + var8) {
                  this.isMoving = true;
               } else {
                  var4 = BlurViewRadiusInset;
                  if (var17 <= var7 - var4 || var17 >= var8 + var4) {
                     this.isMoving = true;
                  }
               }
            } else if (var2 == 1) {
               if (var6 < BlurViewCenterInset) {
                  this.isMoving = true;
               } else if (var6 > var7 - BlurViewRadiusInset && var6 < var7 + var4) {
                  this.isMoving = true;
               } else if (var6 > var8 - var3 && var6 < var8 + BlurViewRadiusInset) {
                  this.isMoving = true;
               }
            }

            this.checkForMoving = false;
            if (this.isMoving) {
               this.handlePan(1, var1);
            }
         }
      } else {
         if (this.isMoving) {
            this.handlePan(3, var1);
            this.checkForMoving = true;
            this.isMoving = false;
         }

         if (var1.getPointerCount() == 2) {
            if (this.checkForZooming && !this.isZooming) {
               this.handlePinch(1, var1);
               this.isZooming = true;
            }
         } else {
            this.handlePinch(3, var1);
            this.checkForZooming = true;
            this.isZooming = false;
         }
      }

      return true;
   }

   public void setActualAreaSize(float var1, float var2) {
      Size var3 = this.actualAreaSize;
      var3.width = var1;
      var3.height = var2;
   }

   public void setDelegate(PhotoFilterBlurControl.PhotoFilterLinearBlurControlDelegate var1) {
      this.delegate = var1;
   }

   public void setType(int var1) {
      this.type = var1;
      this.invalidate();
   }

   private static enum BlurViewActiveControl {
      BlurViewActiveControlCenter,
      BlurViewActiveControlInnerRadius,
      BlurViewActiveControlNone,
      BlurViewActiveControlOuterRadius,
      BlurViewActiveControlRotation,
      BlurViewActiveControlWholeArea;
   }

   public interface PhotoFilterLinearBlurControlDelegate {
      void valueChanged(Point var1, float var2, float var3, float var4);
   }
}
