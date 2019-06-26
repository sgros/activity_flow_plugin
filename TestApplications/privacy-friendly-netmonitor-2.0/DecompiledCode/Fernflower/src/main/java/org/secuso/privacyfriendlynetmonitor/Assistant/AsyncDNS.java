package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.os.AsyncTask;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;

public class AsyncDNS extends AsyncTask {
   protected String doInBackground(String... var1) {
      Collector.resolveHosts();
      return "Executed!";
   }
}
