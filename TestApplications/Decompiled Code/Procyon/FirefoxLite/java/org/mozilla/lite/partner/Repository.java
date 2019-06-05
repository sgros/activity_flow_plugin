// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.lite.partner;

import org.mozilla.cachedrequestloader.ResponseData;
import org.json.JSONException;
import android.text.TextUtils;
import android.arch.lifecycle.Observer;
import android.support.v4.util.Pair;
import android.arch.lifecycle.LiveData;
import org.mozilla.cachedrequestloader.BackgroundCachedRequestLoader;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;

public abstract class Repository<T extends NewsItem>
{
    private boolean cacheIsDirty;
    private Context context;
    private int currentPage;
    private PageSubscription currentPageSubscription;
    private boolean dedup;
    private final int firstPage;
    private List<T> itemPojoList;
    private OnCacheInvalidateListener onCacheInvalidateListener;
    private OnDataChangedListener<T> onDataChangedListener;
    private Parser<T> parser;
    private int socketTag;
    private final String subscriptionKeyName;
    protected String subscriptionUrl;
    private String userAgent;
    
    public Repository(final Context context, final String userAgent, final int socketTag, final OnDataChangedListener onDataChangedListener, final OnCacheInvalidateListener onCacheInvalidateListener, final String subscriptionKeyName, final String subscriptionUrl, final int n, final Parser<T> parser, final boolean dedup) {
        this.cacheIsDirty = false;
        this.context = context;
        this.onDataChangedListener = (OnDataChangedListener<T>)onDataChangedListener;
        this.onCacheInvalidateListener = onCacheInvalidateListener;
        this.subscriptionKeyName = subscriptionKeyName;
        this.subscriptionUrl = subscriptionUrl;
        this.firstPage = n;
        this.currentPage = n;
        this.userAgent = userAgent;
        this.socketTag = socketTag;
        this.parser = parser;
        this.dedup = dedup;
        this.itemPojoList = new ArrayList<T>();
        this.nextSubscription();
    }
    
    private void addData(final int n, final List<T> list) {
        this.addData(n == this.firstPage, list);
    }
    
    private void addData(final boolean b, final List<T> list) {
        if (b) {
            this.itemPojoList.clear();
        }
        if (this.dedup) {
            list.removeAll(this.itemPojoList);
        }
        this.itemPojoList.addAll((Collection<? extends T>)list);
        if (this.onDataChangedListener != null) {
            this.onDataChangedListener.onDataChanged(this.cloneData());
        }
    }
    
    private List<T> cloneData() {
        return Collections.unmodifiableList((List<? extends T>)new ArrayList<T>((Collection<? extends T>)this.itemPojoList));
    }
    
    private void correctData(final List<T> list, final List<T> list2) {
        this.itemPojoList.removeAll(list);
        this.addData(false, list2);
        if (this.onDataChangedListener != null) {
            this.onDataChangedListener.onDataChanged(this.itemPojoList);
        }
    }
    
    private BackgroundCachedRequestLoader createLoader(final int n) {
        return new BackgroundCachedRequestLoader(this.context, this.getSubscriptionKey(n), this.getSubscriptionUrl(n), this.userAgent, this.socketTag, this.cacheIsDirty);
    }
    
    private Observer<Pair<Integer, String>> createObserver(final LiveData<Pair<Integer, String>> liveData, final int n) {
        return new Observer<Pair<Integer, String>>() {
            private List<T> lastValue;
            
            private void removeObserver() {
                liveData.removeObserver(this);
                Repository.this.currentPageSubscription = null;
            }
            
            private boolean updateCacheStatus(final List<T> c, final List<T> list) {
                boolean b = false;
                if (c == null) {
                    return false;
                }
                final ArrayList list2 = new ArrayList((Collection<? extends E>)c);
                list2.removeAll(list);
                if (list2.size() != 0) {
                    b = true;
                }
                if (b && Repository.this.onCacheInvalidateListener != null) {
                    Repository.this.onCacheInvalidateListener.onCacheInvalidate();
                }
                return b;
            }
            
            @Override
            public void onChanged(final Pair<Integer, String> pair) {
                if (pair != null && pair.first != null) {
                    if (!TextUtils.isEmpty((CharSequence)pair.second)) {
                        try {
                            final List<T> parse = Repository.this.parser.parse(pair.second);
                            if (!Repository.this.cacheIsDirty) {
                                Repository.this.cacheIsDirty = this.updateCacheStatus(this.lastValue, parse);
                                if (Repository.this.cacheIsDirty && pair.first == 0) {
                                    Repository.this.correctData(this.lastValue, parse);
                                    this.removeObserver();
                                    return;
                                }
                            }
                            if (pair.first == 0 || !Repository.this.cacheIsDirty) {
                                Repository.this.addData(n, parse);
                                this.lastValue = parse;
                            }
                        }
                        catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else if (pair.first == 0 && "".equals(pair.second) && Repository.this.onDataChangedListener != null) {
                        Repository.this.onDataChangedListener.onDataChanged(Repository.this.cloneData());
                    }
                    if (pair.first == 0) {
                        this.removeObserver();
                    }
                }
            }
        };
    }
    
    private String getSubscriptionKey(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.subscriptionKeyName);
        sb.append("/");
        sb.append(i);
        return sb.toString();
    }
    
    private void nextSubscription() {
        if (this.currentPageSubscription != null) {
            return;
        }
        final PageSubscription currentPageSubscription = new PageSubscription();
        currentPageSubscription.page = this.currentPage;
        currentPageSubscription.cachedRequestLoader = this.createLoader(currentPageSubscription.page);
        ++this.currentPage;
        final ResponseData stringLiveData = currentPageSubscription.cachedRequestLoader.getStringLiveData();
        currentPageSubscription.observer = this.createObserver(stringLiveData, currentPageSubscription.page);
        stringLiveData.observeForever(currentPageSubscription.observer);
        this.currentPageSubscription = currentPageSubscription;
    }
    
    protected abstract String getSubscriptionUrl(final int p0);
    
    public void loadMore() {
        this.nextSubscription();
    }
    
    public void reloadData() {
        this.itemPojoList = new ArrayList<T>();
        this.cacheIsDirty = true;
        this.currentPage = this.firstPage;
        this.currentPageSubscription = null;
        this.nextSubscription();
    }
    
    public void reset() {
        this.itemPojoList = new ArrayList<T>();
        this.cacheIsDirty = true;
        this.currentPage = this.firstPage;
        this.currentPageSubscription = null;
        this.onDataChangedListener = null;
    }
    
    public void setOnDataChangedListener(final OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = (OnDataChangedListener<T>)onDataChangedListener;
    }
    
    public void setSubscriptionUrl(final String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
        this.reloadData();
    }
    
    public interface OnCacheInvalidateListener
    {
        void onCacheInvalidate();
    }
    
    public interface OnDataChangedListener<T>
    {
        void onDataChanged(final List<T> p0);
    }
    
    private static class PageSubscription
    {
        private BackgroundCachedRequestLoader cachedRequestLoader;
        private Observer<Pair<Integer, String>> observer;
        private int page;
    }
    
    public interface Parser<T extends NewsItem>
    {
        List<T> parse(final String p0) throws JSONException;
    }
}
