package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$laGcyL2VMuNvphN9xrLjowzyS3Y */
public final /* synthetic */ class C0865-$$Lambda$MessagesStorage$laGcyL2VMuNvphN9xrLjowzyS3Y implements Runnable {
    private final /* synthetic */ ArrayList f$0;

    public /* synthetic */ C0865-$$Lambda$MessagesStorage$laGcyL2VMuNvphN9xrLjowzyS3Y(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    public final void run() {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.wallpapersDidLoad, this.f$0);
    }
}
