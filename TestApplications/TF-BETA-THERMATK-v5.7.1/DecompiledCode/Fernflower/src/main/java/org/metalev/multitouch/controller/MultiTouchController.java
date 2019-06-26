package org.metalev.multitouch.controller;

import android.util.Log;
import android.view.MotionEvent;
import java.lang.reflect.Method;

public class MultiTouchController {
   private static int ACTION_POINTER_INDEX_SHIFT;
   private static int ACTION_POINTER_UP;
   private static Method m_getHistoricalPressure;
   private static Method m_getHistoricalX;
   private static Method m_getHistoricalY;
   private static Method m_getPointerCount;
   private static Method m_getPointerId;
   private static Method m_getPressure;
   private static Method m_getX;
   private static Method m_getY;
   public static final boolean multiTouchSupported;
   private static final int[] pointerIds;
   private static final float[] pressureVals;
   private static final float[] xVals;
   private static final float[] yVals;
   private boolean handleSingleTouchEvents;
   private MultiTouchController.PointInfo mCurrPt = new MultiTouchController.PointInfo();
   private float mCurrPtAng;
   private float mCurrPtDiam;
   private float mCurrPtHeight;
   private float mCurrPtWidth;
   private float mCurrPtX;
   private float mCurrPtY;
   private MultiTouchController.PositionAndScale mCurrXform = new MultiTouchController.PositionAndScale();
   private int mMode = 0;
   private MultiTouchController.PointInfo mPrevPt = new MultiTouchController.PointInfo();
   private long mSettleEndTime;
   private long mSettleStartTime;
   MultiTouchController.MultiTouchObjectCanvas objectCanvas;
   private Object selectedObject = null;
   private float startAngleMinusPinchAngle;
   private float startPosX;
   private float startPosY;
   private float startScaleOverPinchDiam;
   private float startScaleXOverPinchWidth;
   private float startScaleYOverPinchHeight;

   static {
      boolean var0 = true;

      try {
         m_getPointerCount = MotionEvent.class.getMethod("getPointerCount");
         m_getPointerId = MotionEvent.class.getMethod("getPointerId", Integer.TYPE);
         m_getPressure = MotionEvent.class.getMethod("getPressure", Integer.TYPE);
         m_getHistoricalX = MotionEvent.class.getMethod("getHistoricalX", Integer.TYPE, Integer.TYPE);
         m_getHistoricalY = MotionEvent.class.getMethod("getHistoricalY", Integer.TYPE, Integer.TYPE);
         m_getHistoricalPressure = MotionEvent.class.getMethod("getHistoricalPressure", Integer.TYPE, Integer.TYPE);
         m_getX = MotionEvent.class.getMethod("getX", Integer.TYPE);
         m_getY = MotionEvent.class.getMethod("getY", Integer.TYPE);
      } catch (Exception var3) {
         Log.e("MultiTouchController", "static initializer failed", var3);
         var0 = false;
      }

      multiTouchSupported = var0;
      if (multiTouchSupported) {
         try {
            ACTION_POINTER_UP = MotionEvent.class.getField("ACTION_POINTER_UP").getInt((Object)null);
            ACTION_POINTER_INDEX_SHIFT = MotionEvent.class.getField("ACTION_POINTER_INDEX_SHIFT").getInt((Object)null);
         } catch (Exception var2) {
         }
      }

      xVals = new float[20];
      yVals = new float[20];
      pressureVals = new float[20];
      pointerIds = new int[20];
   }

   public MultiTouchController(MultiTouchController.MultiTouchObjectCanvas var1, boolean var2) {
      this.handleSingleTouchEvents = var2;
      this.objectCanvas = var1;
   }

   private void anchorAtThisPositionAndScale() {
      Object var1 = this.selectedObject;
      if (var1 != null) {
         this.objectCanvas.getPositionAndScale(var1, this.mCurrXform);
         float var2;
         if (this.mCurrXform.updateScale && this.mCurrXform.scale != 0.0F) {
            var2 = this.mCurrXform.scale;
         } else {
            var2 = 1.0F;
         }

         var2 = 1.0F / var2;
         this.extractCurrPtInfo();
         this.startPosX = (this.mCurrPtX - this.mCurrXform.xOff) * var2;
         this.startPosY = (this.mCurrPtY - this.mCurrXform.yOff) * var2;
         this.startScaleOverPinchDiam = this.mCurrXform.scale / this.mCurrPtDiam;
         this.startScaleXOverPinchWidth = this.mCurrXform.scaleX / this.mCurrPtWidth;
         this.startScaleYOverPinchHeight = this.mCurrXform.scaleY / this.mCurrPtHeight;
         this.startAngleMinusPinchAngle = this.mCurrXform.angle - this.mCurrPtAng;
      }
   }

   private void decodeTouchEvent(int var1, float[] var2, float[] var3, float[] var4, int[] var5, int var6, boolean var7, long var8) {
      MultiTouchController.PointInfo var10 = this.mPrevPt;
      this.mPrevPt = this.mCurrPt;
      this.mCurrPt = var10;
      this.mCurrPt.set(var1, var2, var3, var4, var5, var6, var7, var8);
      this.multiTouchController();
   }

   private void extractCurrPtInfo() {
      this.mCurrPtX = this.mCurrPt.getX();
      this.mCurrPtY = this.mCurrPt.getY();
      boolean var1 = this.mCurrXform.updateScale;
      float var2 = 0.0F;
      float var3;
      if (!var1) {
         var3 = 0.0F;
      } else {
         var3 = this.mCurrPt.getMultiTouchDiameter();
      }

      this.mCurrPtDiam = Math.max(21.3F, var3);
      if (!this.mCurrXform.updateScaleXY) {
         var3 = 0.0F;
      } else {
         var3 = this.mCurrPt.getMultiTouchWidth();
      }

      this.mCurrPtWidth = Math.max(30.0F, var3);
      if (!this.mCurrXform.updateScaleXY) {
         var3 = 0.0F;
      } else {
         var3 = this.mCurrPt.getMultiTouchHeight();
      }

      this.mCurrPtHeight = Math.max(30.0F, var3);
      if (!this.mCurrXform.updateAngle) {
         var3 = var2;
      } else {
         var3 = this.mCurrPt.getMultiTouchAngle();
      }

      this.mCurrPtAng = var3;
   }

   private void multiTouchController() {
      int var1 = this.mMode;
      if (var1 != 0) {
         MultiTouchController.MultiTouchObjectCanvas var2;
         if (var1 != 1) {
            if (var1 == 2) {
               if (this.mCurrPt.isMultiTouch() && this.mCurrPt.isDown()) {
                  if (Math.abs(this.mCurrPt.getX() - this.mPrevPt.getX()) <= 30.0F && Math.abs(this.mCurrPt.getY() - this.mPrevPt.getY()) <= 30.0F && Math.abs(this.mCurrPt.getMultiTouchWidth() - this.mPrevPt.getMultiTouchWidth()) * 0.5F <= 40.0F && Math.abs(this.mCurrPt.getMultiTouchHeight() - this.mPrevPt.getMultiTouchHeight()) * 0.5F <= 40.0F) {
                     if (this.mCurrPt.eventTime < this.mSettleEndTime) {
                        this.anchorAtThisPositionAndScale();
                     } else {
                        this.performDragOrPinch();
                     }
                  } else {
                     this.anchorAtThisPositionAndScale();
                     this.mSettleStartTime = this.mCurrPt.getEventTime();
                     this.mSettleEndTime = this.mSettleStartTime + 20L;
                  }
               } else if (!this.mCurrPt.isDown()) {
                  this.mMode = 0;
                  var2 = this.objectCanvas;
                  this.selectedObject = null;
                  var2.selectObject((Object)null, this.mCurrPt);
               } else {
                  this.mMode = 1;
                  this.anchorAtThisPositionAndScale();
                  this.mSettleStartTime = this.mCurrPt.getEventTime();
                  this.mSettleEndTime = this.mSettleStartTime + 20L;
               }
            }
         } else if (!this.mCurrPt.isDown()) {
            this.mMode = 0;
            var2 = this.objectCanvas;
            this.selectedObject = null;
            var2.selectObject((Object)null, this.mCurrPt);
         } else if (this.mCurrPt.isMultiTouch()) {
            this.mMode = 2;
            this.anchorAtThisPositionAndScale();
            this.mSettleStartTime = this.mCurrPt.getEventTime();
            this.mSettleEndTime = this.mSettleStartTime + 20L;
         } else if (this.mCurrPt.getEventTime() < this.mSettleEndTime) {
            this.anchorAtThisPositionAndScale();
         } else {
            this.performDragOrPinch();
         }
      } else if (this.mCurrPt.isDown()) {
         this.selectedObject = this.objectCanvas.getDraggableObjectAtPoint(this.mCurrPt);
         Object var5 = this.selectedObject;
         if (var5 != null) {
            this.mMode = 1;
            this.objectCanvas.selectObject(var5, this.mCurrPt);
            this.anchorAtThisPositionAndScale();
            long var3 = this.mCurrPt.getEventTime();
            this.mSettleEndTime = var3;
            this.mSettleStartTime = var3;
         }
      }

   }

   private void performDragOrPinch() {
      if (this.selectedObject != null) {
         boolean var1 = this.mCurrXform.updateScale;
         float var2 = 1.0F;
         if (var1 && this.mCurrXform.scale != 0.0F) {
            var2 = this.mCurrXform.scale;
         }

         this.extractCurrPtInfo();
         float var3 = this.mCurrPtX;
         float var4 = this.startPosX;
         float var5 = this.mCurrPtY;
         float var6 = this.startPosY;
         float var7 = this.startScaleOverPinchDiam;
         float var8 = this.mCurrPtDiam;
         float var9 = this.startScaleXOverPinchWidth;
         float var10 = this.mCurrPtWidth;
         float var11 = this.startScaleYOverPinchHeight;
         float var12 = this.mCurrPtHeight;
         float var13 = this.startAngleMinusPinchAngle;
         float var14 = this.mCurrPtAng;
         this.mCurrXform.set(var3 - var4 * var2, var5 - var6 * var2, var7 * var8, var9 * var10, var11 * var12, var13 + var14);
         this.objectCanvas.setPositionAndScale(this.selectedObject, this.mCurrXform, this.mCurrPt);
      }
   }

   public boolean isPinching() {
      boolean var1;
      if (this.mMode == 2) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean onTouchEvent(MotionEvent var1) {
      Exception var10000;
      label271: {
         int var2;
         boolean var10001;
         label260: {
            try {
               if (multiTouchSupported) {
                  var2 = (Integer)m_getPointerCount.invoke(var1);
                  break label260;
               }
            } catch (Exception var48) {
               var10000 = var48;
               var10001 = false;
               break label271;
            }

            var2 = 1;
         }

         label251: {
            try {
               if (this.mMode != 0 || this.handleSingleTouchEvents) {
                  break label251;
               }
            } catch (Exception var47) {
               var10000 = var47;
               var10001 = false;
               break label271;
            }

            if (var2 == 1) {
               return false;
            }
         }

         int var3;
         int var4;
         try {
            var3 = var1.getAction();
            var4 = var1.getHistorySize() / var2;
         } catch (Exception var46) {
            var10000 = var46;
            var10001 = false;
            break label271;
         }

         int var5 = 0;

         label240:
         while(true) {
            if (var5 > var4) {
               return true;
            }

            boolean var6;
            if (var5 < var4) {
               var6 = true;
            } else {
               var6 = false;
            }

            int var8;
            float[] var10;
            float[] var52;
            label266: {
               label236: {
                  try {
                     if (!multiTouchSupported) {
                        break label236;
                     }
                  } catch (Exception var45) {
                     var10000 = var45;
                     var10001 = false;
                     break;
                  }

                  if (var2 != 1) {
                     int var7;
                     try {
                        var7 = Math.min(var2, 20);
                     } catch (Exception var43) {
                        var10000 = var43;
                        var10001 = false;
                        break;
                     }

                     var8 = 0;

                     while(true) {
                        if (var8 >= var7) {
                           break label266;
                        }

                        try {
                           int var9 = (Integer)m_getPointerId.invoke(var1, var8);
                           pointerIds[var8] = var9;
                           var10 = xVals;
                        } catch (Exception var42) {
                           var10000 = var42;
                           var10001 = false;
                           break label240;
                        }

                        Method var11;
                        Object[] var12;
                        if (var6) {
                           try {
                              var11 = m_getHistoricalX;
                              var12 = new Object[]{var8, var5};
                           } catch (Exception var41) {
                              var10000 = var41;
                              var10001 = false;
                              break label240;
                           }
                        } else {
                           try {
                              var11 = m_getX;
                              var12 = new Object[]{var8};
                           } catch (Exception var40) {
                              var10000 = var40;
                              var10001 = false;
                              break label240;
                           }
                        }

                        Object var51;
                        try {
                           var51 = var11.invoke(var1, var12);
                        } catch (Exception var39) {
                           var10000 = var39;
                           var10001 = false;
                           break label240;
                        }

                        try {
                           var10[var8] = (Float)var51;
                           var10 = yVals;
                        } catch (Exception var38) {
                           var10000 = var38;
                           var10001 = false;
                           break label240;
                        }

                        Object[] var50;
                        Method var53;
                        if (var6) {
                           try {
                              var53 = m_getHistoricalY;
                              var50 = new Object[]{var8, var5};
                           } catch (Exception var37) {
                              var10000 = var37;
                              var10001 = false;
                              break label240;
                           }
                        } else {
                           try {
                              var53 = m_getY;
                              var50 = new Object[]{var8};
                           } catch (Exception var36) {
                              var10000 = var36;
                              var10001 = false;
                              break label240;
                           }
                        }

                        try {
                           var51 = var53.invoke(var1, var50);
                        } catch (Exception var35) {
                           var10000 = var35;
                           var10001 = false;
                           break label240;
                        }

                        try {
                           var10[var8] = (Float)var51;
                           var52 = pressureVals;
                        } catch (Exception var34) {
                           var10000 = var34;
                           var10001 = false;
                           break label240;
                        }

                        if (var6) {
                           try {
                              var51 = m_getHistoricalPressure.invoke(var1, var8, var5);
                           } catch (Exception var33) {
                              var10000 = var33;
                              var10001 = false;
                              break label240;
                           }
                        } else {
                           try {
                              var51 = m_getPressure.invoke(var1, var8);
                           } catch (Exception var32) {
                              var10000 = var32;
                              var10001 = false;
                              break label240;
                           }
                        }

                        try {
                           var52[var8] = (Float)var51;
                        } catch (Exception var31) {
                           var10000 = var31;
                           var10001 = false;
                           break label240;
                        }

                        ++var8;
                     }
                  }
               }

               float[] var54;
               try {
                  var54 = xVals;
               } catch (Exception var30) {
                  var10000 = var30;
                  var10001 = false;
                  break;
               }

               float var13;
               if (var6) {
                  try {
                     var13 = var1.getHistoricalX(var5);
                  } catch (Exception var29) {
                     var10000 = var29;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     var13 = var1.getX();
                  } catch (Exception var28) {
                     var10000 = var28;
                     var10001 = false;
                     break;
                  }
               }

               var54[0] = var13;

               try {
                  var54 = yVals;
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break;
               }

               if (var6) {
                  try {
                     var13 = var1.getHistoricalY(var5);
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     var13 = var1.getY();
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break;
                  }
               }

               var54[0] = var13;

               try {
                  var54 = pressureVals;
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break;
               }

               if (var6) {
                  try {
                     var13 = var1.getHistoricalPressure(var5);
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break;
                  }
               } else {
                  try {
                     var13 = var1.getPressure();
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break;
                  }
               }

               var54[0] = var13;
            }

            float[] var14;
            int[] var55;
            try {
               var10 = xVals;
               var52 = yVals;
               var14 = pressureVals;
               var55 = pointerIds;
            } catch (Exception var21) {
               var10000 = var21;
               var10001 = false;
               break;
            }

            if (var6) {
               var8 = 2;
            } else {
               var8 = var3;
            }

            boolean var15;
            label274: {
               if (!var6) {
                  label275: {
                     if (var3 != 1) {
                        label273: {
                           try {
                              if (((1 << ACTION_POINTER_INDEX_SHIFT) - 1 & var3) == ACTION_POINTER_UP) {
                                 break label273;
                              }
                           } catch (Exception var44) {
                              var10000 = var44;
                              var10001 = false;
                              break;
                           }

                           if (var3 != 3) {
                              break label275;
                           }
                        }
                     }

                     var15 = false;
                     break label274;
                  }
               }

               var15 = true;
            }

            long var16;
            if (var6) {
               try {
                  var16 = var1.getHistoricalEventTime(var5);
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break;
               }
            } else {
               try {
                  var16 = var1.getEventTime();
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break;
               }
            }

            try {
               this.decodeTouchEvent(var2, var10, var52, var14, var55, var8, var15, var16);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break;
            }

            ++var5;
         }
      }

      Exception var49 = var10000;
      Log.e("MultiTouchController", "onTouchEvent() failed", var49);
      return false;
   }

   public interface MultiTouchObjectCanvas {
      Object getDraggableObjectAtPoint(MultiTouchController.PointInfo var1);

      void getPositionAndScale(Object var1, MultiTouchController.PositionAndScale var2);

      void selectObject(Object var1, MultiTouchController.PointInfo var2);

      boolean setPositionAndScale(Object var1, MultiTouchController.PositionAndScale var2, MultiTouchController.PointInfo var3);
   }

   public static class PointInfo {
      private int action;
      private float angle;
      private boolean angleIsCalculated;
      private float diameter;
      private boolean diameterIsCalculated;
      private float diameterSq;
      private boolean diameterSqIsCalculated;
      private float dx;
      private float dy;
      private long eventTime;
      private boolean isDown;
      private boolean isMultiTouch;
      private int numPoints;
      private int[] pointerIds = new int[20];
      private float pressureMid;
      private float[] pressures = new float[20];
      private float xMid;
      private float[] xs = new float[20];
      private float yMid;
      private float[] ys = new float[20];

      private int julery_isqrt(int var1) {
         int var2 = 0;
         int var3 = 32768;
         byte var4 = 15;
         int var5 = var1;
         var1 = var4;

         while(true) {
            int var6 = (var2 << 1) + var3 << var1;
            int var7 = var2;
            int var8 = var5;
            if (var5 >= var6) {
               var7 = var2 + var3;
               var8 = var5 - var6;
            }

            var3 >>= 1;
            if (var3 <= 0) {
               return var7;
            }

            --var1;
            var2 = var7;
            var5 = var8;
         }
      }

      private void set(int var1, float[] var2, float[] var3, float[] var4, int[] var5, int var6, boolean var7, long var8) {
         this.eventTime = var8;
         this.action = var6;
         this.numPoints = var1;

         for(var6 = 0; var6 < var1; ++var6) {
            this.xs[var6] = var2[var6];
            this.ys[var6] = var3[var6];
            this.pressures[var6] = var4[var6];
            this.pointerIds[var6] = var5[var6];
         }

         this.isDown = var7;
         if (var1 >= 2) {
            var7 = true;
         } else {
            var7 = false;
         }

         this.isMultiTouch = var7;
         if (this.isMultiTouch) {
            this.xMid = (var2[0] + var2[1]) * 0.5F;
            this.yMid = (var3[0] + var3[1]) * 0.5F;
            this.pressureMid = (var4[0] + var4[1]) * 0.5F;
            this.dx = Math.abs(var2[1] - var2[0]);
            this.dy = Math.abs(var3[1] - var3[0]);
         } else {
            this.xMid = var2[0];
            this.yMid = var3[0];
            this.pressureMid = var4[0];
            this.dy = 0.0F;
            this.dx = 0.0F;
         }

         this.angleIsCalculated = false;
         this.diameterIsCalculated = false;
         this.diameterSqIsCalculated = false;
      }

      public long getEventTime() {
         return this.eventTime;
      }

      public float getMultiTouchAngle() {
         if (!this.angleIsCalculated) {
            if (!this.isMultiTouch) {
               this.angle = 0.0F;
            } else {
               float[] var1 = this.ys;
               double var2 = (double)(var1[1] - var1[0]);
               var1 = this.xs;
               this.angle = (float)Math.atan2(var2, (double)(var1[1] - var1[0]));
            }

            this.angleIsCalculated = true;
         }

         return this.angle;
      }

      public float getMultiTouchDiameter() {
         if (!this.diameterIsCalculated) {
            boolean var1 = this.isMultiTouch;
            float var2 = 0.0F;
            if (!var1) {
               this.diameter = 0.0F;
            } else {
               float var3 = this.getMultiTouchDiameterSq();
               if (var3 != 0.0F) {
                  var2 = (float)this.julery_isqrt((int)(var3 * 256.0F)) / 16.0F;
               }

               this.diameter = var2;
               var2 = this.diameter;
               var3 = this.dx;
               if (var2 < var3) {
                  this.diameter = var3;
               }

               var2 = this.diameter;
               var3 = this.dy;
               if (var2 < var3) {
                  this.diameter = var3;
               }
            }

            this.diameterIsCalculated = true;
         }

         return this.diameter;
      }

      public float getMultiTouchDiameterSq() {
         if (!this.diameterSqIsCalculated) {
            float var1;
            if (this.isMultiTouch) {
               var1 = this.dx;
               float var2 = this.dy;
               var1 = var1 * var1 + var2 * var2;
            } else {
               var1 = 0.0F;
            }

            this.diameterSq = var1;
            this.diameterSqIsCalculated = true;
         }

         return this.diameterSq;
      }

      public float getMultiTouchHeight() {
         float var1;
         if (this.isMultiTouch) {
            var1 = this.dy;
         } else {
            var1 = 0.0F;
         }

         return var1;
      }

      public float getMultiTouchWidth() {
         float var1;
         if (this.isMultiTouch) {
            var1 = this.dx;
         } else {
            var1 = 0.0F;
         }

         return var1;
      }

      public float getX() {
         return this.xMid;
      }

      public float getY() {
         return this.yMid;
      }

      public boolean isDown() {
         return this.isDown;
      }

      public boolean isMultiTouch() {
         return this.isMultiTouch;
      }
   }

   public static class PositionAndScale {
      private float angle;
      private float scale;
      private float scaleX;
      private float scaleY;
      private boolean updateAngle;
      private boolean updateScale;
      private boolean updateScaleXY;
      private float xOff;
      private float yOff;

      public float getScale() {
         float var1;
         if (!this.updateScale) {
            var1 = 1.0F;
         } else {
            var1 = this.scale;
         }

         return var1;
      }

      public float getXOff() {
         return this.xOff;
      }

      public float getYOff() {
         return this.yOff;
      }

      protected void set(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.xOff = var1;
         this.yOff = var2;
         var2 = 1.0F;
         var1 = var3;
         if (var3 == 0.0F) {
            var1 = 1.0F;
         }

         this.scale = var1;
         var1 = var4;
         if (var4 == 0.0F) {
            var1 = 1.0F;
         }

         this.scaleX = var1;
         if (var5 == 0.0F) {
            var5 = var2;
         }

         this.scaleY = var5;
         this.angle = var6;
      }

      public void set(float var1, float var2, boolean var3, float var4, boolean var5, float var6, float var7, boolean var8, float var9) {
         this.xOff = var1;
         this.yOff = var2;
         this.updateScale = var3;
         var2 = 1.0F;
         var1 = var4;
         if (var4 == 0.0F) {
            var1 = 1.0F;
         }

         this.scale = var1;
         this.updateScaleXY = var5;
         var1 = var6;
         if (var6 == 0.0F) {
            var1 = 1.0F;
         }

         this.scaleX = var1;
         if (var7 == 0.0F) {
            var1 = var2;
         } else {
            var1 = var7;
         }

         this.scaleY = var1;
         this.updateAngle = var8;
         this.angle = var9;
      }
   }
}
