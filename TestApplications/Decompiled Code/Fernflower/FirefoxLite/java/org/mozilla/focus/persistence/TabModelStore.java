package org.mozilla.focus.persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.Inject;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabViewEngineSession;

public class TabModelStore {
   private static volatile TabModelStore instance;
   private TabsDatabase tabsDatabase;

   private TabModelStore(Context var1) {
      this.tabsDatabase = Inject.getTabsDatabase(var1);
   }

   public static TabModelStore getInstance(Context var0) {
      if (instance == null) {
         synchronized(TabModelStore.class){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (instance == null) {
                  TabModelStore var1 = new TabModelStore(var0);
                  instance = var1;
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return instance;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      } else {
         return instance;
      }
   }

   public void getSavedTabs(Context var1, TabModelStore.AsyncQueryListener var2) {
      (new TabModelStore.QueryTabsTask(var1, this.tabsDatabase, var2)).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
   }

   public void saveTabs(Context var1, List var2, String var3, TabModelStore.AsyncSaveListener var4) {
      PreferenceManager.getDefaultSharedPreferences(var1).edit().putString(var1.getResources().getString(2131755309), var3).apply();
      (new TabModelStore.SaveTabsTask(var1, this.tabsDatabase, var4)).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, var2.toArray(new Session[0]));
   }

   public interface AsyncQueryListener {
      void onQueryComplete(List var1, String var2);
   }

   public interface AsyncSaveListener {
      void onSaveComplete();
   }

   private static class QueryTabsTask extends AsyncTask {
      private WeakReference contextRef;
      private WeakReference listenerRef;
      private TabsDatabase tabsDatabase;

      public QueryTabsTask(Context var1, TabsDatabase var2, TabModelStore.AsyncQueryListener var3) {
         this.contextRef = new WeakReference(var1);
         this.tabsDatabase = var2;
         this.listenerRef = new WeakReference(var3);
      }

      private List restoreWebViewState(Context var1, List var2) {
         ArrayList var3 = new ArrayList();
         File var6 = new File(var1.getCacheDir(), "tabs_cache");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Session var7 = (Session)var4.next();
            TabViewEngineSession var5 = new TabViewEngineSession();
            var5.setWebViewState(FileUtils.readBundleFromStorage(var6, var7.getId()));
            var3.add(new SessionManager.SessionWithState(var7, var5));
         }

         return var3;
      }

      protected List doInBackground(Void... var1) {
         Context var2 = (Context)this.contextRef.get();
         if (var2 != null && this.tabsDatabase != null) {
            List var6 = this.tabsDatabase.tabDao().getTabs();
            ArrayList var3 = new ArrayList();
            Iterator var4 = var6.iterator();

            while(var4.hasNext()) {
               TabEntity var7 = (TabEntity)var4.next();
               Session var5 = new Session(var7.getId(), var7.getParentId(), var7.getUrl());
               String var8;
               if (var7.getTitle() == null) {
                  var8 = "";
               } else {
                  var8 = var7.getTitle();
               }

               var5.setTitle(var8);
               var3.add(var5);
            }

            return this.restoreWebViewState(var2, var3);
         } else {
            return null;
         }
      }

      protected void onPostExecute(List var1) {
         Context var2 = (Context)this.contextRef.get();
         TabModelStore.AsyncQueryListener var3 = (TabModelStore.AsyncQueryListener)this.listenerRef.get();
         if (var3 != null && var2 != null) {
            var3.onQueryComplete(var1, PreferenceManager.getDefaultSharedPreferences(var2).getString(var2.getResources().getString(2131755309), ""));
         }

      }
   }

   private static class SaveTabsTask extends AsyncTask {
      private WeakReference contextRef;
      private WeakReference listenerRef;
      private TabsDatabase tabsDatabase;

      public SaveTabsTask(Context var1, TabsDatabase var2, TabModelStore.AsyncSaveListener var3) {
         this.contextRef = new WeakReference(var1);
         this.tabsDatabase = var2;
         this.listenerRef = new WeakReference(var3);
      }

      private void saveWebViewState(Context var1, Session[] var2) {
         File var3 = new File(var1.getCacheDir(), "tabs_cache");
         ArrayList var7 = new ArrayList();
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Session var6 = var2[var5];
            if (var6 != null && var6.getEngineSession() != null && var6.getEngineSession().getWebViewState() != null) {
               FileUtils.writeBundleToStorage(var3, var6.getId(), var6.getEngineSession().getWebViewState());
               var7.add(new File(var3, var6.getId()));
            }
         }

         File[] var9 = var3.listFiles();
         if (var9 != null) {
            ArrayList var10 = new ArrayList(Arrays.asList(var9));
            var10.removeAll(var7);
            Iterator var8 = var10.iterator();

            while(var8.hasNext()) {
               ((File)var8.next()).delete();
            }
         }

      }

      protected Void doInBackground(Session... var1) {
         if (var1 != null) {
            Context var2 = (Context)this.contextRef.get();
            if (var2 != null) {
               this.saveWebViewState(var2, var1);
            }

            if (this.tabsDatabase != null) {
               TabEntity[] var4 = new TabEntity[var1.length];

               for(int var3 = 0; var3 < var4.length; ++var3) {
                  var4[var3] = new TabEntity(var1[var3].getId(), var1[var3].getParentId());
                  var4[var3].setTitle(var1[var3].getTitle());
                  var4[var3].setUrl(var1[var3].getUrl());
               }

               this.tabsDatabase.tabDao().deleteAllTabsAndInsertTabsInTransaction(var4);
            }
         }

         return null;
      }

      protected void onPostExecute(Void var1) {
         TabModelStore.AsyncSaveListener var2 = (TabModelStore.AsyncSaveListener)this.listenerRef.get();
         if (var2 != null) {
            var2.onSaveComplete();
         }

      }
   }
}
