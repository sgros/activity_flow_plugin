// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.urlinput;

import java.util.List;
import android.arch.lifecycle.LiveData;

public interface QuickSearchDataSource
{
    LiveData<List<QuickSearch>> fetchEngines();
}
