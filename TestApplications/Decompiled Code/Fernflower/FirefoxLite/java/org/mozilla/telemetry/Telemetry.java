package org.mozilla.telemetry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.event.TelemetryEvent;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.ping.TelemetryCorePingBuilder;
import org.mozilla.telemetry.ping.TelemetryEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryMobileEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryPing;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.schedule.TelemetryScheduler;
import org.mozilla.telemetry.storage.TelemetryStorage;

public class Telemetry {
   private final TelemetryClient client;
   private final TelemetryConfiguration configuration;
   private final ExecutorService executor = Executors.newSingleThreadExecutor();
   private final Map pingBuilders;
   private final TelemetryScheduler scheduler;
   private final TelemetryStorage storage;

   public Telemetry(TelemetryConfiguration var1, TelemetryStorage var2, TelemetryClient var3, TelemetryScheduler var4) {
      this.configuration = var1;
      this.storage = var2;
      this.client = var3;
      this.scheduler = var4;
      this.pingBuilders = new HashMap();
   }

   public Telemetry addPingBuilder(TelemetryPingBuilder var1) {
      this.pingBuilders.put(var1.getType(), var1);
      return this;
   }

   public Collection getBuilders() {
      return this.pingBuilders.values();
   }

   public TelemetryClient getClient() {
      return this.client;
   }

   public TelemetryConfiguration getConfiguration() {
      return this.configuration;
   }

   public TelemetryStorage getStorage() {
      return this.storage;
   }

   public Telemetry queueEvent(final TelemetryEvent var1) {
      if (!this.configuration.isCollectionEnabled()) {
         return this;
      } else {
         this.executor.submit(new Runnable() {
            public void run() {
               TelemetryPingBuilder var1x = (TelemetryPingBuilder)Telemetry.this.pingBuilders.get("mobile-event");
               TelemetryPingBuilder var2 = (TelemetryPingBuilder)Telemetry.this.pingBuilders.get("focus-event");
               EventsMeasurement var3;
               String var4;
               if (var1x != null) {
                  var3 = ((TelemetryMobileEventPingBuilder)var1x).getEventsMeasurement();
                  var4 = var1x.getType();
               } else {
                  if (var2 == null) {
                     throw new IllegalStateException("Expect either TelemetryEventPingBuilder or TelemetryMobileEventPingBuilder to be added to queue events");
                  }

                  var3 = ((TelemetryEventPingBuilder)var2).getEventsMeasurement();
                  var4 = var2.getType();
               }

               var3.add(var1);
               if (var3.getEventCount() >= (long)Telemetry.this.configuration.getMaximumNumberOfEventsPerPing()) {
                  Telemetry.this.queuePing(var4);
               }

            }
         });
         return this;
      }
   }

   public Telemetry queuePing(final String var1) {
      if (!this.configuration.isCollectionEnabled()) {
         return this;
      } else {
         this.executor.submit(new Runnable() {
            public void run() {
               TelemetryPingBuilder var1x = (TelemetryPingBuilder)Telemetry.this.pingBuilders.get(var1);
               if (var1x.canBuild()) {
                  TelemetryPing var2 = var1x.build();
                  Telemetry.this.storage.store(var2);
               }
            }
         });
         return this;
      }
   }

   public Telemetry recordSearch(String var1, String var2) {
      if (!this.configuration.isCollectionEnabled()) {
         return this;
      } else if (this.pingBuilders.containsKey("core")) {
         ((TelemetryCorePingBuilder)this.pingBuilders.get("core")).getSearchesMeasurement().recordSearch(var1, var2);
         return this;
      } else {
         throw new IllegalStateException("This configuration does not contain a core ping builder");
      }
   }

   public Telemetry recordSessionEnd() {
      return this.recordSessionEnd(new Function0() {
         public Unit invoke() {
            throw new IllegalStateException("Expected session to be started before session end is called");
         }
      });
   }

   public Telemetry recordSessionEnd(Function0 var1) {
      if (!this.configuration.isCollectionEnabled()) {
         return this;
      } else if (this.pingBuilders.containsKey("core")) {
         if (!((TelemetryCorePingBuilder)this.pingBuilders.get("core")).getSessionDurationMeasurement().recordSessionEnd()) {
            var1.invoke();
         }

         return this;
      } else {
         throw new IllegalStateException("This configuration does not contain a core ping builder");
      }
   }

   public void recordSessionStart() {
      if (this.configuration.isCollectionEnabled()) {
         if (this.pingBuilders.containsKey("core")) {
            TelemetryCorePingBuilder var1 = (TelemetryCorePingBuilder)this.pingBuilders.get("core");
            var1.getSessionDurationMeasurement().recordSessionStart();
            var1.getSessionCountMeasurement().countSession();
         } else {
            throw new IllegalStateException("This configuration does not contain a core ping builder");
         }
      }
   }

   public Telemetry scheduleUpload() {
      if (!this.configuration.isUploadEnabled()) {
         return this;
      } else {
         this.executor.submit(new Runnable() {
            public void run() {
               Telemetry.this.scheduler.scheduleUpload(Telemetry.this.configuration);
            }
         });
         return this;
      }
   }

   public Telemetry setDefaultSearchProvider(DefaultSearchMeasurement.DefaultSearchEngineProvider var1) {
      if (this.pingBuilders.containsKey("core")) {
         ((TelemetryCorePingBuilder)this.pingBuilders.get("core")).getDefaultSearchMeasurement().setDefaultSearchEngineProvider(var1);
         return this;
      } else {
         throw new IllegalStateException("This configuration does not contain a core ping builder");
      }
   }
}
