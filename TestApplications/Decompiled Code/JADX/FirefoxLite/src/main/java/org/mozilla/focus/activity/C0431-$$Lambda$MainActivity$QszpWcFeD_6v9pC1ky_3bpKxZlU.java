package org.mozilla.focus.activity;

import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.rocket.C0769R;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.activity.-$$Lambda$MainActivity$QszpWcFeD_6v9pC1ky_3bpKxZlU */
public final /* synthetic */ class C0431-$$Lambda$MainActivity$QszpWcFeD_6v9pC1ky_3bpKxZlU implements Runnable {
    private final /* synthetic */ MainActivity f$0;

    public /* synthetic */ C0431-$$Lambda$MainActivity$QszpWcFeD_6v9pC1ky_3bpKxZlU(MainActivity mainActivity) {
        this.f$0 = mainActivity;
    }

    public final void run() {
        DialogUtils.showSpotlight(this.f$0, this.f$0.nightModeButton, C0430-$$Lambda$MainActivity$NCjHwftAzp9_j6nZuorYgzoqY2E.INSTANCE, C0769R.string.night_mode_on_boarding_message);
    }
}
