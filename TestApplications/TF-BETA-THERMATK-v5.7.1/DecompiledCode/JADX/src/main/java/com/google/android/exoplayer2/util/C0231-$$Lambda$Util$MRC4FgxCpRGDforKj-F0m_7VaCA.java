package com.google.android.exoplayer2.util;

import java.util.concurrent.ThreadFactory;

/* compiled from: lambda */
/* renamed from: com.google.android.exoplayer2.util.-$$Lambda$Util$MRC4FgxCpRGDforKj-F0m_7VaCA */
public final /* synthetic */ class C0231-$$Lambda$Util$MRC4FgxCpRGDforKj-F0m_7VaCA implements ThreadFactory {
    private final /* synthetic */ String f$0;

    public /* synthetic */ C0231-$$Lambda$Util$MRC4FgxCpRGDforKj-F0m_7VaCA(String str) {
        this.f$0 = str;
    }

    public final Thread newThread(Runnable runnable) {
        return new Thread(runnable, this.f$0);
    }
}
