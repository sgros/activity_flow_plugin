package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import java.io.FileNotFoundException;

public final class PrintHelper {
   public static final int COLOR_MODE_COLOR = 2;
   public static final int COLOR_MODE_MONOCHROME = 1;
   public static final int ORIENTATION_LANDSCAPE = 1;
   public static final int ORIENTATION_PORTRAIT = 2;
   public static final int SCALE_MODE_FILL = 2;
   public static final int SCALE_MODE_FIT = 1;
   PrintHelper.PrintHelperVersionImpl mImpl;

   public PrintHelper(Context var1) {
      if (systemSupportsPrint()) {
         if (VERSION.SDK_INT >= 24) {
            this.mImpl = new PrintHelper.PrintHelperApi24Impl(var1);
         } else if (VERSION.SDK_INT >= 23) {
            this.mImpl = new PrintHelper.PrintHelperApi23Impl(var1);
         } else if (VERSION.SDK_INT >= 20) {
            this.mImpl = new PrintHelper.PrintHelperApi20Impl(var1);
         } else {
            this.mImpl = new PrintHelper.PrintHelperKitkatImpl(var1);
         }
      } else {
         this.mImpl = new PrintHelper.PrintHelperStubImpl();
      }

   }

   public static boolean systemSupportsPrint() {
      boolean var0;
      if (VERSION.SDK_INT >= 19) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public int getColorMode() {
      return this.mImpl.getColorMode();
   }

   public int getOrientation() {
      return this.mImpl.getOrientation();
   }

   public int getScaleMode() {
      return this.mImpl.getScaleMode();
   }

   public void printBitmap(String var1, Bitmap var2) {
      this.mImpl.printBitmap(var1, (Bitmap)var2, (PrintHelper.OnPrintFinishCallback)null);
   }

   public void printBitmap(String var1, Bitmap var2, PrintHelper.OnPrintFinishCallback var3) {
      this.mImpl.printBitmap(var1, var2, var3);
   }

   public void printBitmap(String var1, Uri var2) throws FileNotFoundException {
      this.mImpl.printBitmap(var1, (Uri)var2, (PrintHelper.OnPrintFinishCallback)null);
   }

   public void printBitmap(String var1, Uri var2, PrintHelper.OnPrintFinishCallback var3) throws FileNotFoundException {
      this.mImpl.printBitmap(var1, var2, var3);
   }

   public void setColorMode(int var1) {
      this.mImpl.setColorMode(var1);
   }

   public void setOrientation(int var1) {
      this.mImpl.setOrientation(var1);
   }

   public void setScaleMode(int var1) {
      this.mImpl.setScaleMode(var1);
   }

   public interface OnPrintFinishCallback {
      void onFinish();
   }

   private static final class PrintHelperApi20Impl extends PrintHelper.PrintHelperImpl {
      PrintHelperApi20Impl(Context var1) {
         super(new PrintHelperApi20(var1));
      }
   }

   private static final class PrintHelperApi23Impl extends PrintHelper.PrintHelperImpl {
      PrintHelperApi23Impl(Context var1) {
         super(new PrintHelperApi23(var1));
      }
   }

   private static final class PrintHelperApi24Impl extends PrintHelper.PrintHelperImpl {
      PrintHelperApi24Impl(Context var1) {
         super(new PrintHelperApi24(var1));
      }
   }

   private static class PrintHelperImpl implements PrintHelper.PrintHelperVersionImpl {
      private final PrintHelperKitkat mPrintHelper;

      protected PrintHelperImpl(PrintHelperKitkat var1) {
         this.mPrintHelper = var1;
      }

      public int getColorMode() {
         return this.mPrintHelper.getColorMode();
      }

      public int getOrientation() {
         return this.mPrintHelper.getOrientation();
      }

      public int getScaleMode() {
         return this.mPrintHelper.getScaleMode();
      }

      public void printBitmap(String var1, Bitmap var2, final PrintHelper.OnPrintFinishCallback var3) {
         PrintHelperKitkat.OnPrintFinishCallback var4 = null;
         if (var3 != null) {
            var4 = new PrintHelperKitkat.OnPrintFinishCallback() {
               public void onFinish() {
                  var3.onFinish();
               }
            };
         }

         this.mPrintHelper.printBitmap(var1, var2, var4);
      }

      public void printBitmap(String var1, Uri var2, final PrintHelper.OnPrintFinishCallback var3) throws FileNotFoundException {
         PrintHelperKitkat.OnPrintFinishCallback var4 = null;
         if (var3 != null) {
            var4 = new PrintHelperKitkat.OnPrintFinishCallback() {
               public void onFinish() {
                  var3.onFinish();
               }
            };
         }

         this.mPrintHelper.printBitmap(var1, var2, var4);
      }

      public void setColorMode(int var1) {
         this.mPrintHelper.setColorMode(var1);
      }

      public void setOrientation(int var1) {
         this.mPrintHelper.setOrientation(var1);
      }

      public void setScaleMode(int var1) {
         this.mPrintHelper.setScaleMode(var1);
      }
   }

   private static final class PrintHelperKitkatImpl extends PrintHelper.PrintHelperImpl {
      PrintHelperKitkatImpl(Context var1) {
         super(new PrintHelperKitkat(var1));
      }
   }

   private static final class PrintHelperStubImpl implements PrintHelper.PrintHelperVersionImpl {
      int mColorMode;
      int mOrientation;
      int mScaleMode;

      private PrintHelperStubImpl() {
         this.mScaleMode = 2;
         this.mColorMode = 2;
         this.mOrientation = 1;
      }

      // $FF: synthetic method
      PrintHelperStubImpl(Object var1) {
         this();
      }

      public int getColorMode() {
         return this.mColorMode;
      }

      public int getOrientation() {
         return this.mOrientation;
      }

      public int getScaleMode() {
         return this.mScaleMode;
      }

      public void printBitmap(String var1, Bitmap var2, PrintHelper.OnPrintFinishCallback var3) {
      }

      public void printBitmap(String var1, Uri var2, PrintHelper.OnPrintFinishCallback var3) {
      }

      public void setColorMode(int var1) {
         this.mColorMode = var1;
      }

      public void setOrientation(int var1) {
         this.mOrientation = var1;
      }

      public void setScaleMode(int var1) {
         this.mScaleMode = var1;
      }
   }

   interface PrintHelperVersionImpl {
      int getColorMode();

      int getOrientation();

      int getScaleMode();

      void printBitmap(String var1, Bitmap var2, PrintHelper.OnPrintFinishCallback var3);

      void printBitmap(String var1, Uri var2, PrintHelper.OnPrintFinishCallback var3) throws FileNotFoundException;

      void setColorMode(int var1);

      void setOrientation(int var1);

      void setScaleMode(int var1);
   }
}
