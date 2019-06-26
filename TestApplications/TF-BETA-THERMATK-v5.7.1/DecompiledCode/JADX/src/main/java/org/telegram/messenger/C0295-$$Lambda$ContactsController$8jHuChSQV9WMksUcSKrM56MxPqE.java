package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE */
public final /* synthetic */ class C0295-$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE implements Runnable {
    private final /* synthetic */ ContactsController f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C0295-$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE(ContactsController contactsController, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = contactsController;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
    }

    public final void run() {
        this.f$0.lambda$applyContactsUpdates$46$ContactsController(this.f$1, this.f$2);
    }
}
