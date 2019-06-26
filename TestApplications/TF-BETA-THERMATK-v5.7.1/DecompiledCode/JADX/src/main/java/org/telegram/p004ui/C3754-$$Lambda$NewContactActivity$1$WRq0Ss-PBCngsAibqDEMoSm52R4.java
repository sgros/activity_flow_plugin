package org.telegram.p004ui;

import org.telegram.p004ui.NewContactActivity.C42411;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_contacts_importContacts;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputPhoneContact;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$NewContactActivity$1$WRq0Ss-PBCngsAibqDEMoSm52R4 */
public final /* synthetic */ class C3754-$$Lambda$NewContactActivity$1$WRq0Ss-PBCngsAibqDEMoSm52R4 implements RequestDelegate {
    private final /* synthetic */ C42411 f$0;
    private final /* synthetic */ TL_inputPhoneContact f$1;
    private final /* synthetic */ TL_contacts_importContacts f$2;

    public /* synthetic */ C3754-$$Lambda$NewContactActivity$1$WRq0Ss-PBCngsAibqDEMoSm52R4(C42411 c42411, TL_inputPhoneContact tL_inputPhoneContact, TL_contacts_importContacts tL_contacts_importContacts) {
        this.f$0 = c42411;
        this.f$1 = tL_inputPhoneContact;
        this.f$2 = tL_contacts_importContacts;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$onItemClick$2$NewContactActivity$1(this.f$1, this.f$2, tLObject, tL_error);
    }
}
