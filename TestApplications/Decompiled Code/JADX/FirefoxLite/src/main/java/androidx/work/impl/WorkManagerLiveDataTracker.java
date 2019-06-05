package androidx.work.impl;

import android.arch.lifecycle.LiveData;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

class WorkManagerLiveDataTracker {
    final Set<LiveData> mLiveDataSet = Collections.newSetFromMap(new IdentityHashMap());

    WorkManagerLiveDataTracker() {
    }
}
