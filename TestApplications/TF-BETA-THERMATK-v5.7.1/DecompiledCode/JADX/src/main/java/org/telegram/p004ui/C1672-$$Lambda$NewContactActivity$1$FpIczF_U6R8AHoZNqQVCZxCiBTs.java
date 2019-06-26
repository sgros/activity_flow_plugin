package org.telegram.p004ui;

import org.telegram.p004ui.NewContactActivity.C42411;
import org.telegram.tgnet.TLRPC.TL_contacts_importContacts;
import org.telegram.tgnet.TLRPC.TL_contacts_importedContacts;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPhoneContact;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$NewContactActivity$1$FpIczF_U6R8AHoZNqQVCZxCiBTs */
public final /* synthetic */ class C1672-$$Lambda$NewContactActivity$1$FpIczF_U6R8AHoZNqQVCZxCiBTs implements Runnable {
    private final /* synthetic */ C42411 f$0;
    private final /* synthetic */ TL_contacts_importedContacts f$1;
    private final /* synthetic */ TL_inputPhoneContact f$2;
    private final /* synthetic */ TL_error f$3;
    private final /* synthetic */ TL_contacts_importContacts f$4;

    public /* synthetic */ C1672-$$Lambda$NewContactActivity$1$FpIczF_U6R8AHoZNqQVCZxCiBTs(C42411 c42411, TL_contacts_importedContacts tL_contacts_importedContacts, TL_inputPhoneContact tL_inputPhoneContact, TL_error tL_error, TL_contacts_importContacts tL_contacts_importContacts) {
        this.f$0 = c42411;
        this.f$1 = tL_contacts_importedContacts;
        this.f$2 = tL_inputPhoneContact;
        this.f$3 = tL_error;
        this.f$4 = tL_contacts_importContacts;
    }

    public final void run() {
        this.f$0.lambda$null$1$NewContactActivity$1(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
