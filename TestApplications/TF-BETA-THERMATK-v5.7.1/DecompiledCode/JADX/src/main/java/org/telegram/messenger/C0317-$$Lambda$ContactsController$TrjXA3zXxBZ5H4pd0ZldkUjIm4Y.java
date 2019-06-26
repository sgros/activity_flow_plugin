package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC.TL_contact;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y */
public final /* synthetic */ class C0317-$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y implements Comparator {
    public static final /* synthetic */ C0317-$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y INSTANCE = new C0317-$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y();

    private /* synthetic */ C0317-$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y() {
    }

    public final int compare(Object obj, Object obj2) {
        return ContactsController.lambda$getContactsHash$25((TL_contact) obj, (TL_contact) obj2);
    }
}
