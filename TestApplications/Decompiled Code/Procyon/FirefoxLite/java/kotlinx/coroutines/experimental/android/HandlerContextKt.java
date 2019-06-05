// 
// Decompiled by Procyon v0.5.34
// 

package kotlinx.coroutines.experimental.android;

import android.os.Handler;
import android.os.Looper;

public final class HandlerContextKt
{
    private static final HandlerContext UI;
    
    static {
        UI = new HandlerContext(new Handler(Looper.getMainLooper()), "UI");
    }
    
    public static final HandlerContext getUI() {
        return HandlerContextKt.UI;
    }
}
