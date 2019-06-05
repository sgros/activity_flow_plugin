package org.mozilla.focus.screenshot;

import android.arch.lifecycle.Observer;
import android.support.p001v4.util.Pair;
import java.util.concurrent.CountDownLatch;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.screenshot.-$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf-qmMs */
public final /* synthetic */ class C0735-$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf-qmMs implements Observer {
    private final /* synthetic */ ScreenshotManager f$0;
    private final /* synthetic */ CountDownLatch f$1;

    public /* synthetic */ C0735-$$Lambda$ScreenshotManager$DuDV0OUCiu80Bc2qNHJMkf-qmMs(ScreenshotManager screenshotManager, CountDownLatch countDownLatch) {
        this.f$0 = screenshotManager;
        this.f$1 = countDownLatch;
    }

    public final void onChanged(Object obj) {
        ScreenshotManager.lambda$initFromRemote$0(this.f$0, this.f$1, (Pair) obj);
    }
}
