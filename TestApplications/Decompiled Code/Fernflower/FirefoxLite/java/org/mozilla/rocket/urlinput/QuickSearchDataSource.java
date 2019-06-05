package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.LiveData;

public interface QuickSearchDataSource {
   LiveData fetchEngines();
}
