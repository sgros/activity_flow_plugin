package org.mozilla.lite.partner;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.p001v4.util.Pair;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.mozilla.cachedrequestloader.BackgroundCachedRequestLoader;
import org.mozilla.cachedrequestloader.ResponseData;

public abstract class Repository<T extends NewsItem> {
    private boolean cacheIsDirty = false;
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

    public interface OnCacheInvalidateListener {
        void onCacheInvalidate();
    }

    public interface OnDataChangedListener<T> {
        void onDataChanged(List<T> list);
    }

    private static class PageSubscription {
        private BackgroundCachedRequestLoader cachedRequestLoader;
        private Observer<Pair<Integer, String>> observer;
        private int page;

        private PageSubscription() {
        }

        /* synthetic */ PageSubscription(C05801 c05801) {
            this();
        }
    }

    public interface Parser<T extends NewsItem> {
        List<T> parse(String str) throws JSONException;
    }

    public abstract String getSubscriptionUrl(int i);

    public Repository(Context context, String str, int i, OnDataChangedListener onDataChangedListener, OnCacheInvalidateListener onCacheInvalidateListener, String str2, String str3, int i2, Parser<T> parser, boolean z) {
        this.context = context;
        this.onDataChangedListener = onDataChangedListener;
        this.onCacheInvalidateListener = onCacheInvalidateListener;
        this.subscriptionKeyName = str2;
        this.subscriptionUrl = str3;
        this.firstPage = i2;
        this.currentPage = i2;
        this.userAgent = str;
        this.socketTag = i;
        this.parser = parser;
        this.dedup = z;
        this.itemPojoList = new ArrayList();
        nextSubscription();
    }

    public void loadMore() {
        nextSubscription();
    }

    public void reloadData() {
        this.itemPojoList = new ArrayList();
        this.cacheIsDirty = true;
        this.currentPage = this.firstPage;
        this.currentPageSubscription = null;
        nextSubscription();
    }

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    public void setSubscriptionUrl(String str) {
        this.subscriptionUrl = str;
        reloadData();
    }

    public void reset() {
        this.itemPojoList = new ArrayList();
        this.cacheIsDirty = true;
        this.currentPage = this.firstPage;
        this.currentPageSubscription = null;
        this.onDataChangedListener = null;
    }

    private void nextSubscription() {
        if (this.currentPageSubscription == null) {
            PageSubscription pageSubscription = new PageSubscription();
            pageSubscription.page = this.currentPage;
            pageSubscription.cachedRequestLoader = createLoader(pageSubscription.page);
            this.currentPage++;
            ResponseData stringLiveData = pageSubscription.cachedRequestLoader.getStringLiveData();
            pageSubscription.observer = createObserver(stringLiveData, pageSubscription.page);
            stringLiveData.observeForever(pageSubscription.observer);
            this.currentPageSubscription = pageSubscription;
        }
    }

    private BackgroundCachedRequestLoader createLoader(int i) {
        return new BackgroundCachedRequestLoader(this.context, getSubscriptionKey(i), getSubscriptionUrl(i), this.userAgent, this.socketTag, this.cacheIsDirty);
    }

    private Observer<Pair<Integer, String>> createObserver(final LiveData<Pair<Integer, String>> liveData, final int i) {
        return new Observer<Pair<Integer, String>>() {
            private List<T> lastValue;

            private void removeObserver() {
                liveData.removeObserver(this);
                Repository.this.currentPageSubscription = null;
            }

            private boolean updateCacheStatus(List<T> list, List<T> list2) {
                boolean z = false;
                if (list == null) {
                    return false;
                }
                ArrayList arrayList = new ArrayList(list);
                arrayList.removeAll(list2);
                if (arrayList.size() != 0) {
                    z = true;
                }
                if (z && Repository.this.onCacheInvalidateListener != null) {
                    Repository.this.onCacheInvalidateListener.onCacheInvalidate();
                }
                return z;
            }

            public void onChanged(Pair<Integer, String> pair) {
                if (pair != null && pair.first != null) {
                    if (!TextUtils.isEmpty((CharSequence) pair.second)) {
                        try {
                            List parse = Repository.this.parser.parse((String) pair.second);
                            if (!Repository.this.cacheIsDirty) {
                                Repository.this.cacheIsDirty = updateCacheStatus(this.lastValue, parse);
                                if (Repository.this.cacheIsDirty && ((Integer) pair.first).intValue() == 0) {
                                    Repository.this.correctData(this.lastValue, parse);
                                    removeObserver();
                                    return;
                                }
                            }
                            if (((Integer) pair.first).intValue() == 0 || !Repository.this.cacheIsDirty) {
                                Repository.this.addData(i, parse);
                                this.lastValue = parse;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (((Integer) pair.first).intValue() == 0 && "".equals(pair.second) && Repository.this.onDataChangedListener != null) {
                        Repository.this.onDataChangedListener.onDataChanged(Repository.this.cloneData());
                    }
                    if (((Integer) pair.first).intValue() == 0) {
                        removeObserver();
                    }
                }
            }
        };
    }

    private List<T> cloneData() {
        return Collections.unmodifiableList(new ArrayList(this.itemPojoList));
    }

    private void correctData(List<T> list, List<T> list2) {
        this.itemPojoList.removeAll(list);
        addData(false, (List) list2);
        if (this.onDataChangedListener != null) {
            this.onDataChangedListener.onDataChanged(this.itemPojoList);
        }
    }

    private void addData(int i, List<T> list) {
        addData(i == this.firstPage, (List) list);
    }

    private void addData(boolean z, List<T> list) {
        if (z) {
            this.itemPojoList.clear();
        }
        if (this.dedup) {
            list.removeAll(this.itemPojoList);
        }
        this.itemPojoList.addAll(list);
        if (this.onDataChangedListener != null) {
            this.onDataChangedListener.onDataChanged(cloneData());
        }
    }

    private String getSubscriptionKey(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.subscriptionKeyName);
        stringBuilder.append("/");
        stringBuilder.append(i);
        return stringBuilder.toString();
    }
}
