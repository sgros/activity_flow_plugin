package org.mozilla.cachedrequestloader;

import android.arch.lifecycle.MutableLiveData;
import android.support.p001v4.util.Pair;

public class ResponseData extends MutableLiveData<Pair<Integer, String>> {
    private boolean networkReturned = false;

    ResponseData() {
    }

    public void setValue(Pair<Integer, String> pair) {
        setNetworkReturned(pair);
        if (!shouldIgnoreCache(pair)) {
            super.setValue(pair);
        }
    }

    public void postValue(Pair<Integer, String> pair) {
        setNetworkReturned(pair);
        if (!shouldIgnoreCache(pair)) {
            super.postValue(pair);
        }
    }

    private void setNetworkReturned(Pair<Integer, String> pair) {
        if (pair != null && pair.first != null && ((Integer) pair.first).intValue() == 0) {
            this.networkReturned = true;
        }
    }

    private boolean shouldIgnoreCache(Pair<Integer, String> pair) {
        return this.networkReturned && pair != null && pair.first != null && 1 == ((Integer) pair.first).intValue();
    }
}
