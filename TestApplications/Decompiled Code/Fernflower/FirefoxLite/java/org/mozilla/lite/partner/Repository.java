package org.mozilla.lite.partner;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.mozilla.cachedrequestloader.BackgroundCachedRequestLoader;
import org.mozilla.cachedrequestloader.ResponseData;

public abstract class Repository {
   private boolean cacheIsDirty = false;
   private Context context;
   private int currentPage;
   private Repository.PageSubscription currentPageSubscription;
   private boolean dedup;
   private final int firstPage;
   private List itemPojoList;
   private Repository.OnCacheInvalidateListener onCacheInvalidateListener;
   private Repository.OnDataChangedListener onDataChangedListener;
   private Repository.Parser parser;
   private int socketTag;
   private final String subscriptionKeyName;
   protected String subscriptionUrl;
   private String userAgent;

   public Repository(Context var1, String var2, int var3, Repository.OnDataChangedListener var4, Repository.OnCacheInvalidateListener var5, String var6, String var7, int var8, Repository.Parser var9, boolean var10) {
      this.context = var1;
      this.onDataChangedListener = var4;
      this.onCacheInvalidateListener = var5;
      this.subscriptionKeyName = var6;
      this.subscriptionUrl = var7;
      this.firstPage = var8;
      this.currentPage = var8;
      this.userAgent = var2;
      this.socketTag = var3;
      this.parser = var9;
      this.dedup = var10;
      this.itemPojoList = new ArrayList();
      this.nextSubscription();
   }

   private void addData(int var1, List var2) {
      boolean var3;
      if (var1 == this.firstPage) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.addData(var3, var2);
   }

   private void addData(boolean var1, List var2) {
      if (var1) {
         this.itemPojoList.clear();
      }

      if (this.dedup) {
         var2.removeAll(this.itemPojoList);
      }

      this.itemPojoList.addAll(var2);
      if (this.onDataChangedListener != null) {
         this.onDataChangedListener.onDataChanged(this.cloneData());
      }

   }

   private List cloneData() {
      return Collections.unmodifiableList(new ArrayList(this.itemPojoList));
   }

   private void correctData(List var1, List var2) {
      this.itemPojoList.removeAll(var1);
      this.addData(false, var2);
      if (this.onDataChangedListener != null) {
         this.onDataChangedListener.onDataChanged(this.itemPojoList);
      }

   }

   private BackgroundCachedRequestLoader createLoader(int var1) {
      return new BackgroundCachedRequestLoader(this.context, this.getSubscriptionKey(var1), this.getSubscriptionUrl(var1), this.userAgent, this.socketTag, this.cacheIsDirty);
   }

   private Observer createObserver(final LiveData var1, final int var2) {
      return new Observer() {
         private List lastValue;

         private void removeObserver() {
            var1.removeObserver(this);
            Repository.this.currentPageSubscription = null;
         }

         private boolean updateCacheStatus(List var1x, List var2x) {
            boolean var3 = false;
            if (var1x == null) {
               return false;
            } else {
               ArrayList var4 = new ArrayList(var1x);
               var4.removeAll(var2x);
               if (var4.size() != 0) {
                  var3 = true;
               }

               if (var3 && Repository.this.onCacheInvalidateListener != null) {
                  Repository.this.onCacheInvalidateListener.onCacheInvalidate();
               }

               return var3;
            }
         }

         public void onChanged(Pair var1x) {
            if (var1x != null && var1x.first != null) {
               if (!TextUtils.isEmpty((CharSequence)var1x.second)) {
                  try {
                     List var2x = Repository.this.parser.parse((String)var1x.second);
                     if (!Repository.this.cacheIsDirty) {
                        Repository.this.cacheIsDirty = this.updateCacheStatus(this.lastValue, var2x);
                        if (Repository.this.cacheIsDirty && (Integer)var1x.first == 0) {
                           Repository.this.correctData(this.lastValue, var2x);
                           this.removeObserver();
                           return;
                        }
                     }

                     if ((Integer)var1x.first == 0 || !Repository.this.cacheIsDirty) {
                        Repository.this.addData(var2, var2x);
                        this.lastValue = var2x;
                     }
                  } catch (JSONException var3) {
                     var3.printStackTrace();
                  }
               } else if ((Integer)var1x.first == 0 && "".equals(var1x.second) && Repository.this.onDataChangedListener != null) {
                  Repository.this.onDataChangedListener.onDataChanged(Repository.this.cloneData());
               }

               if ((Integer)var1x.first == 0) {
                  this.removeObserver();
               }

            }
         }
      };
   }

   private String getSubscriptionKey(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.subscriptionKeyName);
      var2.append("/");
      var2.append(var1);
      return var2.toString();
   }

   private void nextSubscription() {
      if (this.currentPageSubscription == null) {
         Repository.PageSubscription var1 = new Repository.PageSubscription();
         var1.page = this.currentPage;
         var1.cachedRequestLoader = this.createLoader(var1.page);
         ++this.currentPage;
         ResponseData var2 = var1.cachedRequestLoader.getStringLiveData();
         var1.observer = this.createObserver(var2, var1.page);
         var2.observeForever(var1.observer);
         this.currentPageSubscription = var1;
      }
   }

   protected abstract String getSubscriptionUrl(int var1);

   public void loadMore() {
      this.nextSubscription();
   }

   public void reloadData() {
      this.itemPojoList = new ArrayList();
      this.cacheIsDirty = true;
      this.currentPage = this.firstPage;
      this.currentPageSubscription = null;
      this.nextSubscription();
   }

   public void reset() {
      this.itemPojoList = new ArrayList();
      this.cacheIsDirty = true;
      this.currentPage = this.firstPage;
      this.currentPageSubscription = null;
      this.onDataChangedListener = null;
   }

   public void setOnDataChangedListener(Repository.OnDataChangedListener var1) {
      this.onDataChangedListener = var1;
   }

   public void setSubscriptionUrl(String var1) {
      this.subscriptionUrl = var1;
      this.reloadData();
   }

   public interface OnCacheInvalidateListener {
      void onCacheInvalidate();
   }

   public interface OnDataChangedListener {
      void onDataChanged(List var1);
   }

   private static class PageSubscription {
      private BackgroundCachedRequestLoader cachedRequestLoader;
      private Observer observer;
      private int page;

      private PageSubscription() {
      }

      // $FF: synthetic method
      PageSubscription(Object var1) {
         this();
      }
   }

   public interface Parser {
      List parse(String var1) throws JSONException;
   }
}
