package android.support.v4.hardware.display;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import java.util.WeakHashMap;

public abstract class DisplayManagerCompat {
   public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
   private static final WeakHashMap sInstances = new WeakHashMap();

   DisplayManagerCompat() {
   }

   public static DisplayManagerCompat getInstance(Context var0) {
      WeakHashMap var1 = sInstances;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label357: {
         DisplayManagerCompat var2;
         try {
            var2 = (DisplayManagerCompat)sInstances.get(var0);
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label357;
         }

         Object var3 = var2;
         if (var2 == null) {
            label358: {
               try {
                  if (VERSION.SDK_INT >= 17) {
                     var3 = new DisplayManagerCompat.JellybeanMr1Impl(var0);
                     break label358;
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label357;
               }

               try {
                  var3 = new DisplayManagerCompat.LegacyImpl(var0);
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label357;
               }
            }

            try {
               sInstances.put(var0, var3);
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label357;
            }
         }

         label337:
         try {
            return (DisplayManagerCompat)var3;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label337;
         }
      }

      while(true) {
         Throwable var46 = var10000;

         try {
            throw var46;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            continue;
         }
      }
   }

   public abstract Display getDisplay(int var1);

   public abstract Display[] getDisplays();

   public abstract Display[] getDisplays(String var1);

   private static class JellybeanMr1Impl extends DisplayManagerCompat {
      private final Object mDisplayManagerObj;

      public JellybeanMr1Impl(Context var1) {
         this.mDisplayManagerObj = DisplayManagerJellybeanMr1.getDisplayManager(var1);
      }

      public Display getDisplay(int var1) {
         return DisplayManagerJellybeanMr1.getDisplay(this.mDisplayManagerObj, var1);
      }

      public Display[] getDisplays() {
         return DisplayManagerJellybeanMr1.getDisplays(this.mDisplayManagerObj);
      }

      public Display[] getDisplays(String var1) {
         return DisplayManagerJellybeanMr1.getDisplays(this.mDisplayManagerObj, var1);
      }
   }

   private static class LegacyImpl extends DisplayManagerCompat {
      private final WindowManager mWindowManager;

      public LegacyImpl(Context var1) {
         this.mWindowManager = (WindowManager)var1.getSystemService("window");
      }

      public Display getDisplay(int var1) {
         Display var2 = this.mWindowManager.getDefaultDisplay();
         if (var2.getDisplayId() != var1) {
            var2 = null;
         }

         return var2;
      }

      public Display[] getDisplays() {
         return new Display[]{this.mWindowManager.getDefaultDisplay()};
      }

      public Display[] getDisplays(String var1) {
         Display[] var2;
         if (var1 == null) {
            var2 = this.getDisplays();
         } else {
            var2 = new Display[0];
         }

         return var2;
      }
   }
}
