package org.telegram.messenger;

import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$gajOA1_r5XMonBIZ6Lf97P0bD9I */
public final /* synthetic */ class C0325-$$Lambda$ContactsController$gajOA1_r5XMonBIZ6Lf97P0bD9I implements Runnable {
    private final /* synthetic */ ContactsController f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ HashMap f$2;
    private final /* synthetic */ HashMap f$3;
    private final /* synthetic */ ArrayList f$4;

    public /* synthetic */ C0325-$$Lambda$ContactsController$gajOA1_r5XMonBIZ6Lf97P0bD9I(ContactsController contactsController, ArrayList arrayList, HashMap hashMap, HashMap hashMap2, ArrayList arrayList2) {
        this.f$0 = contactsController;
        this.f$1 = arrayList;
        this.f$2 = hashMap;
        this.f$3 = hashMap2;
        this.f$4 = arrayList2;
    }

    public final void run() {
        this.f$0.lambda$mergePhonebookAndTelegramContacts$39$ContactsController(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
