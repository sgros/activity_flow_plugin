// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import android.arch.lifecycle.LiveData;
import java.util.Set;

class WorkManagerLiveDataTracker
{
    final Set<LiveData> mLiveDataSet;
    
    WorkManagerLiveDataTracker() {
        this.mLiveDataSet = (Set<LiveData>)Collections.newSetFromMap(new IdentityHashMap<LiveData, Boolean>());
    }
}
