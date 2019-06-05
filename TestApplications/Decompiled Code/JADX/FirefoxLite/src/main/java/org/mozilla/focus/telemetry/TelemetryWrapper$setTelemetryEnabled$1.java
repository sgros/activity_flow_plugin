package org.mozilla.focus.telemetry;

import kotlin.jvm.internal.Intrinsics;
import org.mozilla.telemetry.Telemetry;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.config.TelemetryConfiguration;

/* compiled from: TelemetryWrapper.kt */
final class TelemetryWrapper$setTelemetryEnabled$1 implements Runnable {
    final /* synthetic */ boolean $enabled;
    final /* synthetic */ String $key;

    TelemetryWrapper$setTelemetryEnabled$1(String str, boolean z) {
        this.$key = str;
        this.$enabled = z;
    }

    public final void run() {
        String str = this.$key;
        Intrinsics.checkExpressionValueIsNotNull(str, "key");
        TelemetryWrapper.settingsEvent(str, String.valueOf(this.$enabled), true);
        Telemetry telemetry = TelemetryHolder.get();
        Intrinsics.checkExpressionValueIsNotNull(telemetry, "TelemetryHolder.get()");
        TelemetryConfiguration configuration = telemetry.getConfiguration();
        Intrinsics.checkExpressionValueIsNotNull(configuration, "TelemetryHolder.get()\n  …           .configuration");
        configuration.setCollectionEnabled(this.$enabled);
    }
}
