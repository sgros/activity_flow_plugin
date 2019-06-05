package org.mozilla.focus.home;

import org.mozilla.rocket.theme.ThemeManager;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$tUWOs_ryJciFQgYEqFIG0QAoSRs */
public final /* synthetic */ class C0715-$$Lambda$HomeFragment$tUWOs_ryJciFQgYEqFIG0QAoSRs implements DoWithThemeManager {
    private final /* synthetic */ HomeFragment f$0;

    public /* synthetic */ C0715-$$Lambda$HomeFragment$tUWOs_ryJciFQgYEqFIG0QAoSRs(HomeFragment homeFragment) {
        this.f$0 = homeFragment;
    }

    public final void doIt(ThemeManager themeManager) {
        themeManager.subscribeThemeChange(this.f$0.homeScreenBackground);
    }
}
