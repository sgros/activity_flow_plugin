// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.persistence;

import java.util.Collection;
import java.util.Arrays;
import java.util.Iterator;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import java.io.File;
import java.util.ArrayList;
import java.lang.ref.WeakReference;
import org.mozilla.rocket.tabs.SessionManager;
import android.preference.PreferenceManager;
import org.mozilla.rocket.tabs.Session;
import java.util.List;
import android.os.AsyncTask;
import org.mozilla.focus.Inject;
import android.content.Context;

public class TabModelStore
{
    private static volatile TabModelStore instance;
    private TabsDatabase tabsDatabase;
    
    private TabModelStore(final Context context) {
        this.tabsDatabase = Inject.getTabsDatabase(context);
    }
    
    public static TabModelStore getInstance(final Context context) {
        if (TabModelStore.instance == null) {
            synchronized (TabModelStore.class) {
                if (TabModelStore.instance == null) {
                    TabModelStore.instance = new TabModelStore(context);
                }
            }
        }
        return TabModelStore.instance;
    }
    
    public void getSavedTabs(final Context context, final AsyncQueryListener asyncQueryListener) {
        new QueryTabsTask(context, this.tabsDatabase, asyncQueryListener).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (Object[])new Void[0]);
    }
    
    public void saveTabs(final Context context, final List<Session> list, final String s, final AsyncSaveListener asyncSaveListener) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getResources().getString(2131755309), s).apply();
        new SaveTabsTask(context, this.tabsDatabase, asyncSaveListener).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (Object[])list.toArray(new Session[0]));
    }
    
    public interface AsyncQueryListener
    {
        void onQueryComplete(final List<SessionManager.SessionWithState> p0, final String p1);
    }
    
    public interface AsyncSaveListener
    {
        void onSaveComplete();
    }
    
    private static class QueryTabsTask extends AsyncTask<Void, Void, List<SessionManager.SessionWithState>>
    {
        private WeakReference<Context> contextRef;
        private WeakReference<AsyncQueryListener> listenerRef;
        private TabsDatabase tabsDatabase;
        
        public QueryTabsTask(final Context referent, final TabsDatabase tabsDatabase, final AsyncQueryListener referent2) {
            this.contextRef = new WeakReference<Context>(referent);
            this.tabsDatabase = tabsDatabase;
            this.listenerRef = new WeakReference<AsyncQueryListener>(referent2);
        }
        
        private List<SessionManager.SessionWithState> restoreWebViewState(final Context context, final List<Session> list) {
            final ArrayList<SessionManager.SessionWithState> list2 = new ArrayList<SessionManager.SessionWithState>();
            final File file = new File(context.getCacheDir(), "tabs_cache");
            for (final Session session : list) {
                final TabViewEngineSession tabViewEngineSession = new TabViewEngineSession();
                tabViewEngineSession.setWebViewState(FileUtils.readBundleFromStorage(file, session.getId()));
                list2.add(new SessionManager.SessionWithState(session, tabViewEngineSession));
            }
            return list2;
        }
        
        protected List<SessionManager.SessionWithState> doInBackground(final Void... array) {
            final Context context = this.contextRef.get();
            if (context != null && this.tabsDatabase != null) {
                final List<TabEntity> tabs = this.tabsDatabase.tabDao().getTabs();
                final ArrayList<Session> list = new ArrayList<Session>();
                for (final TabEntity tabEntity : tabs) {
                    final Session session = new Session(tabEntity.getId(), tabEntity.getParentId(), tabEntity.getUrl());
                    String title;
                    if (tabEntity.getTitle() == null) {
                        title = "";
                    }
                    else {
                        title = tabEntity.getTitle();
                    }
                    session.setTitle(title);
                    list.add(session);
                }
                return this.restoreWebViewState(context, list);
            }
            return null;
        }
        
        protected void onPostExecute(final List<SessionManager.SessionWithState> list) {
            final Context context = this.contextRef.get();
            final AsyncQueryListener asyncQueryListener = this.listenerRef.get();
            if (asyncQueryListener != null && context != null) {
                asyncQueryListener.onQueryComplete(list, PreferenceManager.getDefaultSharedPreferences(context).getString(context.getResources().getString(2131755309), ""));
            }
        }
    }
    
    private static class SaveTabsTask extends AsyncTask<Session, Void, Void>
    {
        private WeakReference<Context> contextRef;
        private WeakReference<AsyncSaveListener> listenerRef;
        private TabsDatabase tabsDatabase;
        
        public SaveTabsTask(final Context referent, final TabsDatabase tabsDatabase, final AsyncSaveListener referent2) {
            this.contextRef = new WeakReference<Context>(referent);
            this.tabsDatabase = tabsDatabase;
            this.listenerRef = new WeakReference<AsyncSaveListener>(referent2);
        }
        
        private void saveWebViewState(final Context context, final Session[] array) {
            final File parent = new File(context.getCacheDir(), "tabs_cache");
            final ArrayList<File> list = new ArrayList<File>();
            for (final Session session : array) {
                if (session != null && session.getEngineSession() != null && session.getEngineSession().getWebViewState() != null) {
                    FileUtils.writeBundleToStorage(parent, session.getId(), session.getEngineSession().getWebViewState());
                    list.add(new File(parent, session.getId()));
                }
            }
            final File[] listFiles = parent.listFiles();
            if (listFiles != null) {
                final ArrayList list2 = new ArrayList<Object>(Arrays.asList(listFiles));
                list2.removeAll(list);
                final Iterator<File> iterator = (Iterator<File>)list2.iterator();
                while (iterator.hasNext()) {
                    iterator.next().delete();
                }
            }
        }
        
        protected Void doInBackground(final Session... array) {
            if (array != null) {
                final Context context = this.contextRef.get();
                if (context != null) {
                    this.saveWebViewState(context, array);
                }
                if (this.tabsDatabase != null) {
                    final TabEntity[] array2 = new TabEntity[array.length];
                    for (int i = 0; i < array2.length; ++i) {
                        (array2[i] = new TabEntity(array[i].getId(), array[i].getParentId())).setTitle(array[i].getTitle());
                        array2[i].setUrl(array[i].getUrl());
                    }
                    this.tabsDatabase.tabDao().deleteAllTabsAndInsertTabsInTransaction(array2);
                }
            }
            return null;
        }
        
        protected void onPostExecute(final Void void1) {
            final AsyncSaveListener asyncSaveListener = this.listenerRef.get();
            if (asyncSaveListener != null) {
                asyncSaveListener.onSaveComplete();
            }
        }
    }
}
