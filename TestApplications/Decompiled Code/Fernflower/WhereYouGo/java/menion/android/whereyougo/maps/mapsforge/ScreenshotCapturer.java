package menion.android.whereyougo.maps.mapsforge;

import android.graphics.Bitmap.CompressFormat;
import java.io.File;
import java.util.Locale;
import org.mapsforge.android.maps.PausableThread;

class ScreenshotCapturer extends PausableThread {
   private static final String SCREENSHOT_FILE_NAME = "Map screenshot";
   private static final int SCREENSHOT_QUALITY = 90;
   private static final String THREAD_NAME = "ScreenshotCapturer";
   private final MapsforgeActivity advancedMapViewer;
   private CompressFormat compressFormat;

   ScreenshotCapturer(MapsforgeActivity var1) {
      this.advancedMapViewer = var1;
   }

   private File assembleFilePath(File var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Map screenshot");
      var2.append('.');
      var2.append(this.compressFormat.name().toLowerCase(Locale.ENGLISH));
      return new File(var1, var2.toString());
   }

   void captureScreenshot(CompressFormat var1) {
      synchronized(this){}

      try {
         this.compressFormat = var1;
         this.notify();
      } finally {
         ;
      }

   }

   protected void doWork() {
      // $FF: Couldn't be decompiled
   }

   protected String getThreadName() {
      return "ScreenshotCapturer";
   }

   protected PausableThread.ThreadPriority getThreadPriority() {
      return PausableThread.ThreadPriority.BELOW_NORMAL;
   }

   protected boolean hasWork() {
      synchronized(this){}
      boolean var4 = false;

      CompressFormat var1;
      try {
         var4 = true;
         var1 = this.compressFormat;
         var4 = false;
      } finally {
         if (var4) {
            ;
         }
      }

      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }
}
