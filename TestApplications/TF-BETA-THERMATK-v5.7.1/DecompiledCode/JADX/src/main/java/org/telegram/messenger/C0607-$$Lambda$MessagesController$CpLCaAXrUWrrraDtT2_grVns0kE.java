package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC.Updates;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE */
public final /* synthetic */ class C0607-$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE implements Comparator {
    public static final /* synthetic */ C0607-$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE INSTANCE = new C0607-$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE();

    private /* synthetic */ C0607-$$Lambda$MessagesController$CpLCaAXrUWrrraDtT2_grVns0kE() {
    }

    public final int compare(Object obj, Object obj2) {
        return AndroidUtilities.compare(((Updates) obj).pts, ((Updates) obj2).pts);
    }
}
