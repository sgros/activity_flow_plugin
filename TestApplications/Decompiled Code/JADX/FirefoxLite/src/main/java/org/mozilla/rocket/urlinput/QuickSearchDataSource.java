package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.LiveData;
import java.util.List;

/* compiled from: QuickSearchDataSource.kt */
public interface QuickSearchDataSource {
    LiveData<List<QuickSearch>> fetchEngines();
}
