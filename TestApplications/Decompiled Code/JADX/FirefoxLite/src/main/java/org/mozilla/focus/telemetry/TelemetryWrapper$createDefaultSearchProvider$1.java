package org.mozilla.focus.telemetry;

import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.telemetry.measurement.DefaultSearchMeasurement.DefaultSearchEngineProvider;

/* compiled from: TelemetryWrapper.kt */
final class TelemetryWrapper$createDefaultSearchProvider$1 implements DefaultSearchEngineProvider {
    final /* synthetic */ Context $context;

    TelemetryWrapper$createDefaultSearchProvider$1(Context context) {
        this.$context = context;
    }

    public final String getDefaultSearchEngineIdentifier() {
        SearchEngine defaultSearchEngine = SearchEngineManager.getInstance().getDefaultSearchEngine(this.$context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSearchEngine, "SearchEngineManager.getI…aultSearchEngine(context)");
        return defaultSearchEngine.getIdentifier();
    }
}
