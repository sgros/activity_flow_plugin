// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry;

import org.mozilla.telemetry.measurement.DefaultSearchMeasurement;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.mozilla.telemetry.ping.TelemetryCorePingBuilder;
import org.mozilla.telemetry.measurement.EventsMeasurement;
import org.mozilla.telemetry.ping.TelemetryEventPingBuilder;
import org.mozilla.telemetry.ping.TelemetryMobileEventPingBuilder;
import org.mozilla.telemetry.event.TelemetryEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import org.mozilla.telemetry.storage.TelemetryStorage;
import org.mozilla.telemetry.schedule.TelemetryScheduler;
import org.mozilla.telemetry.ping.TelemetryPingBuilder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.net.TelemetryClient;

public class Telemetry
{
    private final TelemetryClient client;
    private final TelemetryConfiguration configuration;
    private final ExecutorService executor;
    private final Map<String, TelemetryPingBuilder> pingBuilders;
    private final TelemetryScheduler scheduler;
    private final TelemetryStorage storage;
    
    public Telemetry(final TelemetryConfiguration configuration, final TelemetryStorage storage, final TelemetryClient client, final TelemetryScheduler scheduler) {
        this.executor = Executors.newSingleThreadExecutor();
        this.configuration = configuration;
        this.storage = storage;
        this.client = client;
        this.scheduler = scheduler;
        this.pingBuilders = new HashMap<String, TelemetryPingBuilder>();
    }
    
    public Telemetry addPingBuilder(final TelemetryPingBuilder telemetryPingBuilder) {
        this.pingBuilders.put(telemetryPingBuilder.getType(), telemetryPingBuilder);
        return this;
    }
    
    public Collection<TelemetryPingBuilder> getBuilders() {
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
    
    public Telemetry queueEvent(final TelemetryEvent telemetryEvent) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                final TelemetryPingBuilder telemetryPingBuilder = Telemetry.this.pingBuilders.get("mobile-event");
                final TelemetryPingBuilder telemetryPingBuilder2 = Telemetry.this.pingBuilders.get("focus-event");
                EventsMeasurement eventsMeasurement;
                String s;
                if (telemetryPingBuilder != null) {
                    eventsMeasurement = ((TelemetryMobileEventPingBuilder)telemetryPingBuilder).getEventsMeasurement();
                    s = telemetryPingBuilder.getType();
                }
                else {
                    if (telemetryPingBuilder2 == null) {
                        throw new IllegalStateException("Expect either TelemetryEventPingBuilder or TelemetryMobileEventPingBuilder to be added to queue events");
                    }
                    eventsMeasurement = ((TelemetryEventPingBuilder)telemetryPingBuilder2).getEventsMeasurement();
                    s = telemetryPingBuilder2.getType();
                }
                eventsMeasurement.add(telemetryEvent);
                if (eventsMeasurement.getEventCount() >= Telemetry.this.configuration.getMaximumNumberOfEventsPerPing()) {
                    Telemetry.this.queuePing(s);
                }
            }
        });
        return this;
    }
    
    public Telemetry queuePing(final String s) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                final TelemetryPingBuilder telemetryPingBuilder = Telemetry.this.pingBuilders.get(s);
                if (!telemetryPingBuilder.canBuild()) {
                    return;
                }
                Telemetry.this.storage.store(telemetryPingBuilder.build());
            }
        });
        return this;
    }
    
    public Telemetry recordSearch(final String s, final String s2) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        if (this.pingBuilders.containsKey("core")) {
            this.pingBuilders.get("core").getSearchesMeasurement().recordSearch(s, s2);
            return this;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }
    
    public Telemetry recordSessionEnd() {
        return this.recordSessionEnd(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                throw new IllegalStateException("Expected session to be started before session end is called");
            }
        });
    }
    
    public Telemetry recordSessionEnd(final Function0<Unit> function0) {
        if (!this.configuration.isCollectionEnabled()) {
            return this;
        }
        if (this.pingBuilders.containsKey("core")) {
            if (!this.pingBuilders.get("core").getSessionDurationMeasurement().recordSessionEnd()) {
                function0.invoke();
            }
            return this;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }
    
    public void recordSessionStart() {
        if (!this.configuration.isCollectionEnabled()) {
            return;
        }
        if (this.pingBuilders.containsKey("core")) {
            final TelemetryCorePingBuilder telemetryCorePingBuilder = this.pingBuilders.get("core");
            telemetryCorePingBuilder.getSessionDurationMeasurement().recordSessionStart();
            telemetryCorePingBuilder.getSessionCountMeasurement().countSession();
            return;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }
    
    public Telemetry scheduleUpload() {
        if (!this.configuration.isUploadEnabled()) {
            return this;
        }
        this.executor.submit(new Runnable() {
            @Override
            public void run() {
                Telemetry.this.scheduler.scheduleUpload(Telemetry.this.configuration);
            }
        });
        return this;
    }
    
    public Telemetry setDefaultSearchProvider(final DefaultSearchMeasurement.DefaultSearchEngineProvider defaultSearchEngineProvider) {
        if (this.pingBuilders.containsKey("core")) {
            this.pingBuilders.get("core").getDefaultSearchMeasurement().setDefaultSearchEngineProvider(defaultSearchEngineProvider);
            return this;
        }
        throw new IllegalStateException("This configuration does not contain a core ping builder");
    }
}
