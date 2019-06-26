package org.telegram.p004ui;

import java.util.Comparator;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$JfDHhpbwPgHf9ZtKvhAah5Ozjtk */
public final /* synthetic */ class C1449-$$Lambda$ChatUsersActivity$JfDHhpbwPgHf9ZtKvhAah5Ozjtk implements Comparator {
    private final /* synthetic */ ChatUsersActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1449-$$Lambda$ChatUsersActivity$JfDHhpbwPgHf9ZtKvhAah5Ozjtk(ChatUsersActivity chatUsersActivity, int i) {
        this.f$0 = chatUsersActivity;
        this.f$1 = i;
    }

    public final int compare(Object obj, Object obj2) {
        return this.f$0.lambda$null$18$ChatUsersActivity(this.f$1, (TLObject) obj, (TLObject) obj2);
    }
}
