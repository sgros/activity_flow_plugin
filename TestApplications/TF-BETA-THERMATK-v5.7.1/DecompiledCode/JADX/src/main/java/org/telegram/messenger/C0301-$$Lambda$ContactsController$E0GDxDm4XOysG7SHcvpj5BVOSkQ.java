package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$E0GDxDm4XOysG7SHcvpj5BVOSkQ */
public final /* synthetic */ class C0301-$$Lambda$ContactsController$E0GDxDm4XOysG7SHcvpj5BVOSkQ implements Runnable {
    private final /* synthetic */ ContactsController f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C0301-$$Lambda$ContactsController$E0GDxDm4XOysG7SHcvpj5BVOSkQ(ContactsController contactsController, ArrayList arrayList) {
        this.f$0 = contactsController;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$performWriteContactsToPhoneBook$43$ContactsController(this.f$1);
    }
}
