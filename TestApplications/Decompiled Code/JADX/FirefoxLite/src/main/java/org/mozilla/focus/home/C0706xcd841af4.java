package org.mozilla.focus.home;

import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.theme.ThemeManager;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$GestureListenerAdapter$hfUYXF8Tlz2VOkjOAN_ypxg1zHg */
public final /* synthetic */ class C0706xcd841af4 implements DoWithThemeManager {
    public static final /* synthetic */ C0706xcd841af4 INSTANCE = new C0706xcd841af4();

    private /* synthetic */ C0706xcd841af4() {
    }

    public final void doIt(ThemeManager themeManager) {
        TelemetryWrapper.changeThemeTo(themeManager.toggleNextTheme().name());
    }
}
