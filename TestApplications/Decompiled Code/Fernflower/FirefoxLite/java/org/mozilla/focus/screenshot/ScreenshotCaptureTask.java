package org.mozilla.focus.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.provider.QueryHandler;
import org.mozilla.focus.screenshot.model.Screenshot;
import org.mozilla.focus.telemetry.TelemetryWrapper;

public class ScreenshotCaptureTask extends AsyncTask {
   private final Context context;

   public ScreenshotCaptureTask(Context var1) {
      this.context = var1.getApplicationContext();
   }

   private static String saveBitmapToStorage(Context param0, String param1, Bitmap param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   protected String doInBackground(Object... var1) {
      String var2 = (String)var1[0];
      String var3 = (String)var1[1];
      Bitmap var4 = (Bitmap)var1[2];
      long var5 = System.currentTimeMillis();
      SimpleDateFormat var7 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());

      try {
         Context var11 = this.context;
         StringBuilder var8 = new StringBuilder();
         var8.append("Screenshot_");
         Date var9 = new Date(var5);
         var8.append(var7.format(var9));
         String var13 = saveBitmapToStorage(var11, var8.toString(), var4);
         if (!TextUtils.isEmpty(var13)) {
            FileUtils.notifyMediaScanner(this.context, var13);
            Screenshot var12 = new Screenshot(var2, var3, var5, var13);
            ScreenshotManager.getInstance().insert(var12, (QueryHandler.AsyncInsertListener)null);
            TelemetryWrapper.clickToolbarCapture(ScreenshotManager.getInstance().getCategory(this.context, var3), ScreenshotManager.getInstance().getCategoryVersion());
         }

         return var13;
      } catch (IOException var10) {
         return null;
      }
   }
}
