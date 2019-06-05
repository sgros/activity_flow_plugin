// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.urlinput;

import java.util.Collections;
import org.json.JSONException;
import java.util.ArrayList;
import org.json.JSONArray;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import org.mozilla.httptask.SimpleLoadUrlTask;
import org.mozilla.focus.utils.SupportUtils;
import java.util.List;
import org.mozilla.focus.search.SearchEngine;
import android.os.AsyncTask;

public class UrlInputPresenter implements Presenter
{
    private AsyncTask queryTask;
    private final SearchEngine searchEngine;
    private final String userAgent;
    private View view;
    
    UrlInputPresenter(final SearchEngine searchEngine, final String userAgent) {
        this.searchEngine = searchEngine;
        this.userAgent = userAgent;
    }
    
    @Override
    public void onInput(final CharSequence charSequence, final boolean b) {
        if (b && this.queryTask != null) {
            this.queryTask.cancel(true);
        }
        if (this.view == null) {
            return;
        }
        if (charSequence.length() == 0) {
            this.view.setSuggestions(null);
            this.view.setQuickSearchVisible(false);
            return;
        }
        this.view.setQuickSearchVisible(true);
        if (SupportUtils.isUrl(charSequence.toString())) {
            return;
        }
        if (this.queryTask != null) {
            this.queryTask.cancel(true);
            this.queryTask = null;
        }
        this.queryTask = new QueryTask(this.view).execute((Object[])new String[] { this.searchEngine.buildSearchSuggestionUrl(charSequence.toString()), this.userAgent, Integer.toString(10000) });
    }
    
    @Override
    public void setView(final View view) {
        this.view = view;
        if (view == null && this.queryTask != null) {
            this.queryTask.cancel(false);
        }
    }
    
    private static class QueryTask extends SimpleLoadUrlTask
    {
        private WeakReference<View> viewWeakReference;
        
        QueryTask(final View referent) {
            this.viewWeakReference = new WeakReference<View>(referent);
        }
        
        protected void onPostExecute(String s) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                return;
            }
            final List<CharSequence> list = null;
            List<CharSequence> emptyList = null;
            Label_0113: {
                List<CharSequence> list2;
                try {
                    final JSONArray jsonArray = new JSONArray(s).getJSONArray(1);
                    final int length = jsonArray.length();
                    s = (String)new ArrayList(length);
                    int n = 0;
                    while (true) {
                        try {
                            if (n < Math.min(length, 5)) {
                                ((List<String>)s).add(jsonArray.getString(n));
                                ++n;
                                continue;
                            }
                            break Label_0113;
                        }
                        catch (JSONException ex) {}
                    }
                }
                catch (JSONException ex2) {
                    list2 = null;
                }
                finally {
                    list2 = list;
                }
                emptyList = list2;
                if (list2 == null) {
                    emptyList = Collections.emptyList();
                }
            }
            final View view = this.viewWeakReference.get();
            if (view != null) {
                view.setSuggestions(emptyList);
            }
        }
    }
}
