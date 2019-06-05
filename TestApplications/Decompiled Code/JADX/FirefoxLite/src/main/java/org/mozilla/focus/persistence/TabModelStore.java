package org.mozilla.focus.persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.Inject;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager.SessionWithState;
import org.mozilla.rocket.tabs.TabViewEngineSession;

public class TabModelStore {
    private static volatile TabModelStore instance;
    private TabsDatabase tabsDatabase;

    public interface AsyncQueryListener {
        void onQueryComplete(List<SessionWithState> list, String str);
    }

    public interface AsyncSaveListener {
        void onSaveComplete();
    }

    private static class QueryTabsTask extends AsyncTask<Void, Void, List<SessionWithState>> {
        private WeakReference<Context> contextRef;
        private WeakReference<AsyncQueryListener> listenerRef;
        private TabsDatabase tabsDatabase;

        public QueryTabsTask(Context context, TabsDatabase tabsDatabase, AsyncQueryListener asyncQueryListener) {
            this.contextRef = new WeakReference(context);
            this.tabsDatabase = tabsDatabase;
            this.listenerRef = new WeakReference(asyncQueryListener);
        }

        /* Access modifiers changed, original: protected|varargs */
        public List<SessionWithState> doInBackground(Void... voidArr) {
            Context context = (Context) this.contextRef.get();
            if (context == null || this.tabsDatabase == null) {
                return null;
            }
            List<TabEntity> tabs = this.tabsDatabase.tabDao().getTabs();
            ArrayList arrayList = new ArrayList();
            for (TabEntity tabEntity : tabs) {
                Session session = new Session(tabEntity.getId(), tabEntity.getParentId(), tabEntity.getUrl());
                session.setTitle(tabEntity.getTitle() == null ? "" : tabEntity.getTitle());
                arrayList.add(session);
            }
            return restoreWebViewState(context, arrayList);
        }

        private List<SessionWithState> restoreWebViewState(Context context, List<Session> list) {
            ArrayList arrayList = new ArrayList();
            File file = new File(context.getCacheDir(), "tabs_cache");
            for (Session session : list) {
                TabViewEngineSession tabViewEngineSession = new TabViewEngineSession();
                tabViewEngineSession.setWebViewState(FileUtils.readBundleFromStorage(file, session.getId()));
                arrayList.add(new SessionWithState(session, tabViewEngineSession));
            }
            return arrayList;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(List<SessionWithState> list) {
            Context context = (Context) this.contextRef.get();
            AsyncQueryListener asyncQueryListener = (AsyncQueryListener) this.listenerRef.get();
            if (asyncQueryListener != null && context != null) {
                asyncQueryListener.onQueryComplete(list, PreferenceManager.getDefaultSharedPreferences(context).getString(context.getResources().getString(C0769R.string.pref_key_focus_tab_id), ""));
            }
        }
    }

    private static class SaveTabsTask extends AsyncTask<Session, Void, Void> {
        private WeakReference<Context> contextRef;
        private WeakReference<AsyncSaveListener> listenerRef;
        private TabsDatabase tabsDatabase;

        public SaveTabsTask(Context context, TabsDatabase tabsDatabase, AsyncSaveListener asyncSaveListener) {
            this.contextRef = new WeakReference(context);
            this.tabsDatabase = tabsDatabase;
            this.listenerRef = new WeakReference(asyncSaveListener);
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Session... sessionArr) {
            if (sessionArr != null) {
                Context context = (Context) this.contextRef.get();
                if (context != null) {
                    saveWebViewState(context, sessionArr);
                }
                if (this.tabsDatabase != null) {
                    TabEntity[] tabEntityArr = new TabEntity[sessionArr.length];
                    for (int i = 0; i < tabEntityArr.length; i++) {
                        tabEntityArr[i] = new TabEntity(sessionArr[i].getId(), sessionArr[i].getParentId());
                        tabEntityArr[i].setTitle(sessionArr[i].getTitle());
                        tabEntityArr[i].setUrl(sessionArr[i].getUrl());
                    }
                    this.tabsDatabase.tabDao().deleteAllTabsAndInsertTabsInTransaction(tabEntityArr);
                }
            }
            return null;
        }

        private void saveWebViewState(Context context, Session[] sessionArr) {
            File file = new File(context.getCacheDir(), "tabs_cache");
            ArrayList arrayList = new ArrayList();
            for (Session session : sessionArr) {
                if (!(session == null || session.getEngineSession() == null || session.getEngineSession().getWebViewState() == null)) {
                    FileUtils.writeBundleToStorage(file, session.getId(), session.getEngineSession().getWebViewState());
                    arrayList.add(new File(file, session.getId()));
                }
            }
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                ArrayList<File> arrayList2 = new ArrayList(Arrays.asList(listFiles));
                arrayList2.removeAll(arrayList);
                for (File delete : arrayList2) {
                    delete.delete();
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void voidR) {
            AsyncSaveListener asyncSaveListener = (AsyncSaveListener) this.listenerRef.get();
            if (asyncSaveListener != null) {
                asyncSaveListener.onSaveComplete();
            }
        }
    }

    private TabModelStore(Context context) {
        this.tabsDatabase = Inject.getTabsDatabase(context);
    }

    public static TabModelStore getInstance(Context context) {
        if (instance == null) {
            synchronized (TabModelStore.class) {
                if (instance == null) {
                    instance = new TabModelStore(context);
                }
            }
        }
        return instance;
    }

    public void getSavedTabs(Context context, AsyncQueryListener asyncQueryListener) {
        new QueryTabsTask(context, this.tabsDatabase, asyncQueryListener).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    public void saveTabs(Context context, List<Session> list, String str, AsyncSaveListener asyncSaveListener) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getResources().getString(C0769R.string.pref_key_focus_tab_id), str).apply();
        new SaveTabsTask(context, this.tabsDatabase, asyncSaveListener).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, list.toArray(new Session[0]));
    }
}
