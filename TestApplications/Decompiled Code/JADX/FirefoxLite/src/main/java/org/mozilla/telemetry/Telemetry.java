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
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement.DefaultSearchEngineProvider;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.net.TelemetryClient;
import org.mozilla.telemetry.ping.TelemetryCorePingBuilder;
import org.mozilla.telemetry.ping.TelemetryEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryMobileEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import org.mozilla.telemetry.schedule.TelemetryScheduler;
import org.mozilla.telemetry.storage.TelemetryStorage;

public class Telemetry {
    private final TelemetryClient client;
    private final TelemetryConfiguration configuration;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, TelemetryPingBuilder> pingBuilders;
    private final TelemetryScheduler scheduler;
    private final TelemetryStorage storage;

    /* renamed from: org.mozilla.telemetry.Telemetry$3 */
    class C06013 implements Runnable {
        C06013() {
        }

        public void run() {
            Telemetry.this.scheduler.scheduleUpload(Telemetry.this.configuration);
        }
    }

    /* renamed from: org.mozilla.telemetry.Telemetry$4 */
    class C06024 implements Function0<Unit> {
        C06024() {
        }

        public Unit invoke() {
            throw new IllegalStateException("Expected session to be started before session end is called");
        }
    }

    public Telemetry(TelemetryConfiguration telemetryConfiguration, TelemetryStorage telemetryStorage, TelemetryClient telemetryClient, TelemetryScheduler telemetryScheduler) {
        this.configuration = telemetryConfiguration;
        this.storage = telemetryStorage;
        this.client = telemetryClient;
        this.scheduler = telemetryScheduler;
        this.pingBuilders = new HashMap();
    }

    public Telemetry addPingBuilder(TelemetryPingBuilder telemetryPingBuilder) {
        this.pingBuilders.put(telemetryPingBuilder.getType(), telemetryPingBuilder);
        return this;
    }

    public Telemetry queuePing(final String str) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        this.executor.submit(new Runnable() {
            public void run() {
                TelemetryPingBuilder telemetryPingBuilder = (TelemetryPingBuilder) Telemetry.this.pingBuilders.get(str);
                if (telemetryPingBuilder.canBuild()) {
                    Telemetry.this.storage.store(telemetryPingBuilder.build());
                }
            }
        });
        return this;
    }

    public Telemetry queueEvent(final TelemetryEvent telemetryEvent) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        this.executor.submit(new Runnable() {
            public void run() {
                String type;
                EventsMeasurement eventsMeasurement;
                TelemetryPingBuilder telemetryPingBuilder = (TelemetryPingBuilder) Telemetry.this.pingBuilders.get("mobile-event");
                TelemetryPingBuilder telemetryPingBuilder2 = (TelemetryPingBuilder) Telemetry.this.pingBuilders.get("focus-event");
                if (telemetryPingBuilder != null) {
                    EventsMeasurement eventsMeasurement2 = ((TelemetryMobileEventPingBuilder) telemetryPingBuilder).getEventsMeasurement();
                    type = telemetryPingBuilder.getType();
                    eventsMeasurement = eventsMeasurement2;
                } else if (telemetryPingBuilder2 != null) {
                    eventsMeasurement = ((TelemetryEventPingBuilder) telemetryPingBuilder2).getEventsMeasurement();
                    type = telemetryPingBuilder2.getType();
                } else {
                    throw new IllegalStateException("Expect either TelemetryEventPingBuilder or TelemetryMobileEventPingBuilder to be added to queue events");
                }
                eventsMeasurement.add(telemetryEvent);
                if (eventsMeasurement.getEventCount() >= ((long) Telemetry.this.configuration.getMaximumNumberOfEventsPerPing())) {
                    Telemetry.this.queuePing(type);
                }
            }
        });
        return this;
    }

    public Collection<TelemetryPingBuilder> getBuilders() {
        return this.pingBuilders.values();
    }

    public Telemetry scheduleUpload() {
        if (!this.configuration.isUploadEnabled()) {
            return this;
        }
        this.executor.submit(new C06013());
        return this;
    }

    public void recordSessionStart() {
        if (!this.configuration.isCollectionEnabled()) {
            return;
        }
        if (this.pingBuilders.containsKey("core")) {
            TelemetryCorePingBuilder telemetryCorePingBuilder = (TelemetryCorePingBuilder) this.pingBuilders.get("core");
            telemetryCorePingBuilder.getSessionDurationMeasurement().recordSessionStart();
            telemetryCorePingBuilder.getSessionCountMeasurement().countSession();
            return;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }

    public Telemetry recordSessionEnd(Function0<Unit> function0) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        if (this.pingBuilders.containsKey("core")) {
            if (!((TelemetryCorePingBuilder) this.pingBuilders.get("core")).getSessionDurationMeasurement().recordSessionEnd()) {
                function0.invoke();
            }
            return this;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }

    public Telemetry recordSessionEnd() {
        return recordSessionEnd(new C06024());
    }

    public Telemetry recordSearch(String str, String str2) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        if (this.pingBuilders.containsKey("core")) {
            ((TelemetryCorePingBuilder) this.pingBuilders.get("core")).getSearchesMeasurement().recordSearch(str, str2);
            return this;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }

    public Telemetry setDefaultSearchProvider(DefaultSearchEngineProvider defaultSearchEngineProvider) {
        if (this.pingBuilders.containsKey("core")) {
            ((TelemetryCorePingBuilder) this.pingBuilders.get("core")).getDefaultSearchMeasurement().setDefaultSearchEngineProvider(defaultSearchEngineProvider);
            return this;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }

    public TelemetryClient getClient() {
        return this.client;
    }

    public TelemetryStorage getStorage() {
        return this.storage;
    }

    public TelemetryConfiguration getConfiguration() {
        return this.configuration;
    }
}
