package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoPickerActivity$67brVZzCZFPPW5KLbmmY3AJwLD4 */
public final /* synthetic */ class C3812-$$Lambda$PhotoPickerActivity$67brVZzCZFPPW5KLbmmY3AJwLD4 implements RequestDelegate {
    private final /* synthetic */ PhotoPickerActivity f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C3812-$$Lambda$PhotoPickerActivity$67brVZzCZFPPW5KLbmmY3AJwLD4(PhotoPickerActivity photoPickerActivity, boolean z) {
        this.f$0 = photoPickerActivity;
        this.f$1 = z;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchBotUser$7$PhotoPickerActivity(this.f$1, tLObject, tL_error);
    }
}
