package org.telegram.p004ui.Adapters;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$BaseLocationAdapter$OUu2tRVUfz8Mg0cIvwT-Hlk9dBk */
public final /* synthetic */ class C3904-$$Lambda$BaseLocationAdapter$OUu2tRVUfz8Mg0cIvwT-Hlk9dBk implements RequestDelegate {
    private final /* synthetic */ BaseLocationAdapter f$0;

    public /* synthetic */ C3904-$$Lambda$BaseLocationAdapter$OUu2tRVUfz8Mg0cIvwT-Hlk9dBk(BaseLocationAdapter baseLocationAdapter) {
        this.f$0 = baseLocationAdapter;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchBotUser$1$BaseLocationAdapter(tLObject, tL_error);
    }
}
