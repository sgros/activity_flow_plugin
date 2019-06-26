package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DownloadController$0LtKveHOl8NLZKx-EDiX80oSJa0 */
public final /* synthetic */ class C3394-$$Lambda$DownloadController$0LtKveHOl8NLZKx-EDiX80oSJa0 implements RequestDelegate {
    public static final /* synthetic */ C3394-$$Lambda$DownloadController$0LtKveHOl8NLZKx-EDiX80oSJa0 INSTANCE = new C3394-$$Lambda$DownloadController$0LtKveHOl8NLZKx-EDiX80oSJa0();

    private /* synthetic */ C3394-$$Lambda$DownloadController$0LtKveHOl8NLZKx-EDiX80oSJa0() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        DownloadController.lambda$savePresetToServer$3(tLObject, tL_error);
    }
}
