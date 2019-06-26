package org.telegram.p004ui;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM */
public final /* synthetic */ class C3789-$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM implements RequestDelegate {
    public static final /* synthetic */ C3789-$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM INSTANCE = new C3789-$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM();

    private /* synthetic */ C3789-$$Lambda$PassportActivity$pP0eV3Hy639MJNxG0c3lV4tp0MM() {
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new C1770-$$Lambda$PassportActivity$fK7dwz8bOfjtlF5bGs64fAkaNiU(tLObject));
    }
}
