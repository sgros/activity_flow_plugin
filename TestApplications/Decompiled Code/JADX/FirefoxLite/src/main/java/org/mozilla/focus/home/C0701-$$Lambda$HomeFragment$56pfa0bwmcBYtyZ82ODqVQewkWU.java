package org.mozilla.focus.home;

import org.mozilla.rocket.theme.ThemeManager;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$56pfa0bwmcBYtyZ82ODqVQewkWU */
public final /* synthetic */ class C0701-$$Lambda$HomeFragment$56pfa0bwmcBYtyZ82ODqVQewkWU implements DoWithThemeManager {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0701-$$Lambda$HomeFragment$56pfa0bwmcBYtyZ82ODqVQewkWU(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void doIt(ThemeManager themeManager) {
        themeManager.unsubscribeThemeChange(this.f$0.homeScreenBackground);
    }
}
