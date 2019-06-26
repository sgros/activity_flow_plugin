package org.telegram.p004ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Comparator;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY */
public final /* synthetic */ class C2257-$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY implements Comparator {
    private final /* synthetic */ SparseArray f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C2257-$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY(SparseArray sparseArray, ArrayList arrayList) {
        this.f$0 = sparseArray;
        this.f$1 = arrayList;
    }

    public final int compare(Object obj, Object obj2) {
        return MentionsAdapter.lambda$searchUsernameOrHashtag$5(this.f$0, this.f$1, (User) obj, (User) obj2);
    }
}
