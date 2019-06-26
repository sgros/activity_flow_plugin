package org.telegram.p004ui;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ContactAddActivity$xQQmG-ikgUejGCdP82NOkH8zIao */
public final /* synthetic */ class C1465-$$Lambda$ContactAddActivity$xQQmG-ikgUejGCdP82NOkH8zIao implements OnEditorActionListener {
    private final /* synthetic */ ContactAddActivity f$0;

    public /* synthetic */ C1465-$$Lambda$ContactAddActivity$xQQmG-ikgUejGCdP82NOkH8zIao(ContactAddActivity contactAddActivity) {
        this.f$0 = contactAddActivity;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.f$0.lambda$createView$1$ContactAddActivity(textView, i, keyEvent);
    }
}
