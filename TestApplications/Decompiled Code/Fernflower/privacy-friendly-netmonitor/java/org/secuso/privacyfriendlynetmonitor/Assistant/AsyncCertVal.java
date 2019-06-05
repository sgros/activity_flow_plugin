package org.secuso.privacyfriendlynetmonitor.Assistant;

import android.os.AsyncTask;
import de.bjoernr.ssllabs.Api;
import de.bjoernr.ssllabs.ConsoleUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.Collector;

public class AsyncCertVal extends AsyncTask {
   private Api mSSLLabsApi = new Api();

   private void fetchHostInfo(List var1) {
      int var2 = this.getMaxAssessments();

      ArrayList var3;
      for(var3 = new ArrayList(); var2 > 0 && var1.size() > 0; --var2) {
         String var4 = (String)var1.get(0);
         JSONObject var5 = this.mSSLLabsApi.fetchHostInformationCached(var4, (String)null, false, false);

         Map var7;
         try {
            var7 = ConsoleUtilities.jsonToMap(var5);
         } catch (JSONException var6) {
            var7 = null;
         }

         if (var7 != null && var7.size() > 0) {
            Collector.mCertValMap.put(var4, var7);
         }

         if (var7 != null && var7.size() > 0 && !Collector.analyseReady(var7)) {
            var3.add(var4);
         }

         var1.remove(0);
      }

      Collector.sCertValList.addAll(var3);
      Collector.updateCertHostHandler();
   }

   private int getMaxAssessments() {
      JSONObject var1 = this.mSSLLabsApi.fetchApiInfo();

      Map var3;
      try {
         var3 = ConsoleUtilities.jsonToMap(var1);
      } catch (JSONException var2) {
         var3 = null;
      }

      return var3.containsKey("maxAssessments") ? (Integer)var3.get("maxAssessments") : 0;
   }

   public Void doInBackground(Void... var1) {
      if (Collector.sCertValList.size() > 0) {
         this.fetchHostInfo(Collector.sCertValList);
      }

      return null;
   }
}
