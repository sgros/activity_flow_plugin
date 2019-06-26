package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC.TL_contact;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$l3XEyiXk02DazId-mQdpRpST3Co */
public final /* synthetic */ class C0329-$$Lambda$ContactsController$l3XEyiXk02DazId-mQdpRpST3Co implements Comparator {
    private final /* synthetic */ ContactsController f$0;

    public /* synthetic */ C0329-$$Lambda$ContactsController$l3XEyiXk02DazId-mQdpRpST3Co(ContactsController contactsController) {
        this.f$0 = contactsController;
    }

    public final int compare(Object obj, Object obj2) {
        return this.f$0.lambda$buildContactsSectionsArrays$41$ContactsController((TL_contact) obj, (TL_contact) obj2);
    }
}
