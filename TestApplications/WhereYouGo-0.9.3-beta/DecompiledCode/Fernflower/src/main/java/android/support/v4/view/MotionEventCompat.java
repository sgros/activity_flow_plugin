package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MotionEvent;

public final class MotionEventCompat {
   public static final int ACTION_HOVER_ENTER = 9;
   public static final int ACTION_HOVER_EXIT = 10;
   public static final int ACTION_HOVER_MOVE = 7;
   public static final int ACTION_MASK = 255;
   public static final int ACTION_POINTER_DOWN = 5;
   public static final int ACTION_POINTER_INDEX_MASK = 65280;
   public static final int ACTION_POINTER_INDEX_SHIFT = 8;
   public static final int ACTION_POINTER_UP = 6;
   public static final int ACTION_SCROLL = 8;
   public static final int AXIS_BRAKE = 23;
   public static final int AXIS_DISTANCE = 24;
   public static final int AXIS_GAS = 22;
   public static final int AXIS_GENERIC_1 = 32;
   public static final int AXIS_GENERIC_10 = 41;
   public static final int AXIS_GENERIC_11 = 42;
   public static final int AXIS_GENERIC_12 = 43;
   public static final int AXIS_GENERIC_13 = 44;
   public static final int AXIS_GENERIC_14 = 45;
   public static final int AXIS_GENERIC_15 = 46;
   public static final int AXIS_GENERIC_16 = 47;
   public static final int AXIS_GENERIC_2 = 33;
   public static final int AXIS_GENERIC_3 = 34;
   public static final int AXIS_GENERIC_4 = 35;
   public static final int AXIS_GENERIC_5 = 36;
   public static final int AXIS_GENERIC_6 = 37;
   public static final int AXIS_GENERIC_7 = 38;
   public static final int AXIS_GENERIC_8 = 39;
   public static final int AXIS_GENERIC_9 = 40;
   public static final int AXIS_HAT_X = 15;
   public static final int AXIS_HAT_Y = 16;
   public static final int AXIS_HSCROLL = 10;
   public static final int AXIS_LTRIGGER = 17;
   public static final int AXIS_ORIENTATION = 8;
   public static final int AXIS_PRESSURE = 2;
   public static final int AXIS_RELATIVE_X = 27;
   public static final int AXIS_RELATIVE_Y = 28;
   public static final int AXIS_RTRIGGER = 18;
   public static final int AXIS_RUDDER = 20;
   public static final int AXIS_RX = 12;
   public static final int AXIS_RY = 13;
   public static final int AXIS_RZ = 14;
   public static final int AXIS_SIZE = 3;
   public static final int AXIS_THROTTLE = 19;
   public static final int AXIS_TILT = 25;
   public static final int AXIS_TOOL_MAJOR = 6;
   public static final int AXIS_TOOL_MINOR = 7;
   public static final int AXIS_TOUCH_MAJOR = 4;
   public static final int AXIS_TOUCH_MINOR = 5;
   public static final int AXIS_VSCROLL = 9;
   public static final int AXIS_WHEEL = 21;
   public static final int AXIS_X = 0;
   public static final int AXIS_Y = 1;
   public static final int AXIS_Z = 11;
   public static final int BUTTON_PRIMARY = 1;
   static final MotionEventCompat.MotionEventVersionImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 14) {
         IMPL = new MotionEventCompat.ICSMotionEventVersionImpl();
      } else if (VERSION.SDK_INT >= 12) {
         IMPL = new MotionEventCompat.HoneycombMr1MotionEventVersionImpl();
      } else {
         IMPL = new MotionEventCompat.BaseMotionEventVersionImpl();
      }

   }

   private MotionEventCompat() {
   }

   @Deprecated
   public static int findPointerIndex(MotionEvent var0, int var1) {
      return var0.findPointerIndex(var1);
   }

   public static int getActionIndex(MotionEvent var0) {
      return (var0.getAction() & '\uff00') >> 8;
   }

   public static int getActionMasked(MotionEvent var0) {
      return var0.getAction() & 255;
   }

   public static float getAxisValue(MotionEvent var0, int var1) {
      return IMPL.getAxisValue(var0, var1);
   }

   public static float getAxisValue(MotionEvent var0, int var1, int var2) {
      return IMPL.getAxisValue(var0, var1, var2);
   }

   public static int getButtonState(MotionEvent var0) {
      return IMPL.getButtonState(var0);
   }

   @Deprecated
   public static int getPointerCount(MotionEvent var0) {
      return var0.getPointerCount();
   }

   @Deprecated
   public static int getPointerId(MotionEvent var0, int var1) {
      return var0.getPointerId(var1);
   }

   @Deprecated
   public static int getSource(MotionEvent var0) {
      return var0.getSource();
   }

   @Deprecated
   public static float getX(MotionEvent var0, int var1) {
      return var0.getX(var1);
   }

   @Deprecated
   public static float getY(MotionEvent var0, int var1) {
      return var0.getY(var1);
   }

   public static boolean isFromSource(MotionEvent var0, int var1) {
      boolean var2;
      if ((var0.getSource() & var1) == var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   static class BaseMotionEventVersionImpl implements MotionEventCompat.MotionEventVersionImpl {
      public float getAxisValue(MotionEvent var1, int var2) {
         return 0.0F;
      }

      public float getAxisValue(MotionEvent var1, int var2, int var3) {
         return 0.0F;
      }

      public int getButtonState(MotionEvent var1) {
         return 0;
      }
   }

   static class HoneycombMr1MotionEventVersionImpl extends MotionEventCompat.BaseMotionEventVersionImpl {
      public float getAxisValue(MotionEvent var1, int var2) {
         return MotionEventCompatHoneycombMr1.getAxisValue(var1, var2);
      }

      public float getAxisValue(MotionEvent var1, int var2, int var3) {
         return MotionEventCompatHoneycombMr1.getAxisValue(var1, var2, var3);
      }
   }

   private static class ICSMotionEventVersionImpl extends MotionEventCompat.HoneycombMr1MotionEventVersionImpl {
      ICSMotionEventVersionImpl() {
      }

      public int getButtonState(MotionEvent var1) {
         return MotionEventCompatICS.getButtonState(var1);
      }
   }

   interface MotionEventVersionImpl {
      float getAxisValue(MotionEvent var1, int var2);

      float getAxisValue(MotionEvent var1, int var2, int var3);

      int getButtonState(MotionEvent var1);
   }
}
