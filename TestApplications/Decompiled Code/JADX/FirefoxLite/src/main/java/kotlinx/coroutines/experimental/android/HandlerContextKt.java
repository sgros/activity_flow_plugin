package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import android.os.Looper;

/* compiled from: HandlerContext.kt */
public final class HandlerContextKt {
    /* renamed from: UI */
    private static final HandlerContext f38UI = new HandlerContext(new Handler(Looper.getMainLooper()), "UI");

    public static final HandlerContext getUI() {
        return f38UI;
    }
}
