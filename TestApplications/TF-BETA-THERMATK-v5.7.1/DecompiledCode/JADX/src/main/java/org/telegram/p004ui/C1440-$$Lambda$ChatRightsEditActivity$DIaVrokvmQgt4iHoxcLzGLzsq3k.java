package org.telegram.p004ui;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.TimePicker;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatRightsEditActivity$DIaVrokvmQgt4iHoxcLzGLzsq3k */
public final /* synthetic */ class C1440-$$Lambda$ChatRightsEditActivity$DIaVrokvmQgt4iHoxcLzGLzsq3k implements OnTimeSetListener {
    private final /* synthetic */ ChatRightsEditActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1440-$$Lambda$ChatRightsEditActivity$DIaVrokvmQgt4iHoxcLzGLzsq3k(ChatRightsEditActivity chatRightsEditActivity, int i) {
        this.f$0 = chatRightsEditActivity;
        this.f$1 = i;
    }

    public final void onTimeSet(TimePicker timePicker, int i, int i2) {
        this.f$0.lambda$null$0$ChatRightsEditActivity(this.f$1, timePicker, i, i2);
    }
}
