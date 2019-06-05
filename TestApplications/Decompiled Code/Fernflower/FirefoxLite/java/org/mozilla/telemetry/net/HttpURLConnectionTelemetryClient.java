package org.mozilla.telemetry.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import mozilla.components.support.base.log.logger.Logger;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class HttpURLConnectionTelemetryClient implements TelemetryClient {
   private Logger logger = new Logger("telemetry/client");

   String createDateHeaderValue() {
      Calendar var1 = Calendar.getInstance();
      SimpleDateFormat var2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
      var2.setTimeZone(TimeZone.getTimeZone("GMT"));
      return var2.format(var1.getTime());
   }

   HttpURLConnection openConnectionConnection(String var1, String var2) throws IOException {
      StringBuilder var3 = new StringBuilder();
      var3.append(var1);
      var3.append(var2);
      return (HttpURLConnection)(new URL(var3.toString())).openConnection();
   }

   int upload(HttpURLConnection param1, String param2) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public boolean uploadPing(TelemetryConfiguration param1, String param2, String param3) {
      // $FF: Couldn't be decompiled
   }
}
