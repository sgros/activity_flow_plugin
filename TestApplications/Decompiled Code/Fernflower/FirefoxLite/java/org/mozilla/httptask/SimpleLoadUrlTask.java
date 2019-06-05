package org.mozilla.httptask;

import android.net.TrafficStats;
import android.os.AsyncTask;
import java.net.MalformedURLException;
import java.net.URL;
import org.mozilla.httprequest.HttpRequest;

public class SimpleLoadUrlTask extends AsyncTask {
   protected String doInBackground(String... var1) {
      try {
         TrafficStats.setThreadStatsTag(Integer.parseInt(var1[2]));
      } catch (NumberFormatException var4) {
         throw new IllegalArgumentException("Socket Tag should be a number");
      }

      try {
         URL var2 = new URL(var1[0]);
         String var5 = HttpRequest.get(var2, var1[1]);
         return var5;
      } catch (MalformedURLException var3) {
         throw new IllegalArgumentException("MalformedURLException");
      }
   }
}
