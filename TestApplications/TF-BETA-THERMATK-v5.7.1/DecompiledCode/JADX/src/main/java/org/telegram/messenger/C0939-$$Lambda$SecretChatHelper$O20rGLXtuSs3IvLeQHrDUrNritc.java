package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC.Message;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc */
public final /* synthetic */ class C0939-$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc implements Comparator {
    public static final /* synthetic */ C0939-$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc INSTANCE = new C0939-$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc();

    private /* synthetic */ C0939-$$Lambda$SecretChatHelper$O20rGLXtuSs3IvLeQHrDUrNritc() {
    }

    public final int compare(Object obj, Object obj2) {
        return AndroidUtilities.compare(((Message) obj).seq_out, ((Message) obj2).seq_out);
    }
}
