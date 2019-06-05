package org.mozilla.telemetry.ping;

import java.util.Map;

public class TelemetryPing {
   private final String documentId;
   private final Map measurementResults;
   private final String type;
   private final String uploadPath;

   TelemetryPing(String var1, String var2, String var3, Map var4) {
      this.type = var1;
      this.documentId = var2;
      this.uploadPath = var3;
      this.measurementResults = var4;
   }

   public String getDocumentId() {
      return this.documentId;
   }

   public Map getMeasurementResults() {
      return this.measurementResults;
   }

   public String getType() {
      return this.type;
   }

   public String getUploadPath() {
      return this.uploadPath;
   }
}
