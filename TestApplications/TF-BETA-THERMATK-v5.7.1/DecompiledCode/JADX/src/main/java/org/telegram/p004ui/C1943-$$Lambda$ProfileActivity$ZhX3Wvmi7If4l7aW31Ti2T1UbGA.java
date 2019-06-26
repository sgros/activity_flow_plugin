package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA */
public final /* synthetic */ class C1943-$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA implements OnClickListener {
    private final /* synthetic */ ProfileActivity f$0;
    private final /* synthetic */ Chat f$1;

    public /* synthetic */ C1943-$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA(ProfileActivity profileActivity, Chat chat) {
        this.f$0 = profileActivity;
        this.f$1 = chat;
    }

    public final void onClick(View view) {
        this.f$0.lambda$createView$10$ProfileActivity(this.f$1, view);
    }
}
