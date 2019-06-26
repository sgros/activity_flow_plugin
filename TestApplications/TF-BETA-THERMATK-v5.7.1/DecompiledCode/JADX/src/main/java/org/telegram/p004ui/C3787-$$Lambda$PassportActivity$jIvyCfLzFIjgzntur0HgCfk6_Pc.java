package org.telegram.p004ui;

import org.telegram.messenger.MrzRecognizer.Result;
import org.telegram.p004ui.MrzCameraActivity.MrzCameraActivityDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PassportActivity$jIvyCfLzFIjgzntur0HgCfk6_Pc */
public final /* synthetic */ class C3787-$$Lambda$PassportActivity$jIvyCfLzFIjgzntur0HgCfk6_Pc implements MrzCameraActivityDelegate {
    private final /* synthetic */ PassportActivity f$0;

    public /* synthetic */ C3787-$$Lambda$PassportActivity$jIvyCfLzFIjgzntur0HgCfk6_Pc(PassportActivity passportActivity) {
        this.f$0 = passportActivity;
    }

    public final void didFindMrzInfo(Result result) {
        this.f$0.lambda$null$44$PassportActivity(result);
    }
}
