package org.telegram.messenger;

import android.util.SparseArray;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ */
public final /* synthetic */ class C0298-$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ implements Runnable {
    private final /* synthetic */ ContactsController f$0;
    private final /* synthetic */ SparseArray f$1;

    public /* synthetic */ C0298-$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ(ContactsController contactsController, SparseArray sparseArray) {
        this.f$0 = contactsController;
        this.f$1 = sparseArray;
    }

    public final void run() {
        this.f$0.lambda$migratePhoneBookToV7$11$ContactsController(this.f$1);
    }
}
