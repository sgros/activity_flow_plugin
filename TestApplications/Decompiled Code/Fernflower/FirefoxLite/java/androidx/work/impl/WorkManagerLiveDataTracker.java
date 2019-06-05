package androidx.work.impl;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

class WorkManagerLiveDataTracker {
   final Set mLiveDataSet = Collections.newSetFromMap(new IdentityHashMap());
}
