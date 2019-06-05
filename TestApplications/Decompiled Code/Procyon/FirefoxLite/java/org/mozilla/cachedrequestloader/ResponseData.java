// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.cachedrequestloader;

import android.support.v4.util.Pair;
import android.arch.lifecycle.MutableLiveData;

public class ResponseData extends MutableLiveData<Pair<Integer, String>>
{
    private boolean networkReturned;
    
    ResponseData() {
        this.networkReturned = false;
    }
    
    private void setNetworkReturned(final Pair<Integer, String> pair) {
        if (pair != null && pair.first != null && pair.first == 0) {
            this.networkReturned = true;
        }
    }
    
    private boolean shouldIgnoreCache(final Pair<Integer, String> pair) {
        final boolean networkReturned = this.networkReturned;
        boolean b = true;
        if (!networkReturned || pair == null || pair.first == null || 1 != pair.first) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void postValue(final Pair<Integer, String> networkReturned) {
        this.setNetworkReturned(networkReturned);
        if (this.shouldIgnoreCache(networkReturned)) {
            return;
        }
        super.postValue(networkReturned);
    }
    
    @Override
    public void setValue(final Pair<Integer, String> pair) {
        this.setNetworkReturned(pair);
        if (this.shouldIgnoreCache(pair)) {
            return;
        }
        super.setValue(pair);
    }
}
