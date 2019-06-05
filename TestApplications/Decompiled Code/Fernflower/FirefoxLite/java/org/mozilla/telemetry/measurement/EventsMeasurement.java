package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.io.File;
import mozilla.components.support.base.log.logger.Logger;
import org.json.JSONArray;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.event.TelemetryEvent;

public class EventsMeasurement extends TelemetryMeasurement {
   private static final String LOG_TAG = "EventsMeasurement";
   private TelemetryConfiguration configuration;
   private Logger logger;

   public EventsMeasurement(TelemetryConfiguration var1) {
      super("events");
      this.configuration = var1;
      this.logger = new Logger("telemetry/events");
   }

   private void countEvent() {
      synchronized(this){}

      try {
         SharedPreferences var1 = this.configuration.getSharedPreferences();
         long var2 = var1.getLong("event_count", 0L);
         var1.edit().putLong("event_count", var2 + 1L).apply();
      } finally {
         ;
      }

   }

   private JSONArray readAndClearEventsFromDisk() {
      // $FF: Couldn't be decompiled
   }

   private void resetEventCount() {
      synchronized(this){}

      try {
         this.configuration.getSharedPreferences().edit().putLong("event_count", 0L).apply();
      } finally {
         ;
      }

   }

   private void saveEventToDisk(TelemetryEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public void add(TelemetryEvent var1) {
      this.saveEventToDisk(var1);
   }

   public Object flush() {
      return this.readAndClearEventsFromDisk();
   }

   public long getEventCount() {
      return this.configuration.getSharedPreferences().getLong("event_count", 0L);
   }

   File getEventFile() {
      return new File(this.configuration.getDataDirectory(), "events1");
   }
}
