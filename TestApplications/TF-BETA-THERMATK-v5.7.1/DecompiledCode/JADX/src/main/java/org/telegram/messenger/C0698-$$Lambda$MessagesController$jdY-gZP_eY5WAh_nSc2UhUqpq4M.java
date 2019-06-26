package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC.Update;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$jdY-gZP_eY5WAh_nSc2UhUqpq4M */
public final /* synthetic */ class C0698-$$Lambda$MessagesController$jdY-gZP_eY5WAh_nSc2UhUqpq4M implements Comparator {
    private final /* synthetic */ MessagesController f$0;

    public /* synthetic */ C0698-$$Lambda$MessagesController$jdY-gZP_eY5WAh_nSc2UhUqpq4M(MessagesController messagesController) {
        this.f$0 = messagesController;
    }

    public final int compare(Object obj, Object obj2) {
        return this.f$0.lambda$new$0$MessagesController((Update) obj, (Update) obj2);
    }
}
