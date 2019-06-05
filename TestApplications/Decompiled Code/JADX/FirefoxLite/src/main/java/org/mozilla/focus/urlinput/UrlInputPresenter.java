package org.mozilla.focus.urlinput;

import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import org.mozilla.focus.search.SearchEngine;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.httptask.SimpleLoadUrlTask;

public class UrlInputPresenter implements Presenter {
    private AsyncTask queryTask;
    private final SearchEngine searchEngine;
    private final String userAgent;
    private View view;

    private static class QueryTask extends SimpleLoadUrlTask {
        private WeakReference<View> viewWeakReference;

        QueryTask(View view) {
            this.viewWeakReference = new WeakReference(view);
        }

        /* Access modifiers changed, original: protected */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x004a  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x003c  */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x004a  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0035  */
        public void onPostExecute(java.lang.String r5) {
            /*
            r4 = this;
            r0 = android.text.TextUtils.isEmpty(r5);
            if (r0 == 0) goto L_0x0007;
        L_0x0006:
            return;
        L_0x0007:
            r0 = 0;
            r1 = new org.json.JSONArray;	 Catch:{ JSONException -> 0x0039, all -> 0x0032 }
            r1.<init>(r5);	 Catch:{ JSONException -> 0x0039, all -> 0x0032 }
            r5 = 1;
            r5 = r1.getJSONArray(r5);	 Catch:{ JSONException -> 0x0039, all -> 0x0032 }
            r1 = r5.length();	 Catch:{ JSONException -> 0x0039, all -> 0x0032 }
            r2 = new java.util.ArrayList;	 Catch:{ JSONException -> 0x0039, all -> 0x0032 }
            r2.<init>(r1);	 Catch:{ JSONException -> 0x0039, all -> 0x0032 }
            r0 = 0;
        L_0x001c:
            r3 = 5;
            r3 = java.lang.Math.min(r1, r3);	 Catch:{ JSONException -> 0x0030, all -> 0x002d }
            if (r0 >= r3) goto L_0x0040;
        L_0x0023:
            r3 = r5.getString(r0);	 Catch:{ JSONException -> 0x0030, all -> 0x002d }
            r2.add(r3);	 Catch:{ JSONException -> 0x0030, all -> 0x002d }
            r0 = r0 + 1;
            goto L_0x001c;
        L_0x002d:
            r5 = move-exception;
            r0 = r2;
            goto L_0x0033;
            goto L_0x003a;
        L_0x0032:
            r5 = move-exception;
        L_0x0033:
            if (r0 != 0) goto L_0x0038;
        L_0x0035:
            java.util.Collections.emptyList();
        L_0x0038:
            throw r5;
        L_0x0039:
            r2 = r0;
        L_0x003a:
            if (r2 != 0) goto L_0x0040;
        L_0x003c:
            r2 = java.util.Collections.emptyList();
        L_0x0040:
            r5 = r4.viewWeakReference;
            r5 = r5.get();
            r5 = (org.mozilla.focus.urlinput.UrlInputContract.View) r5;
            if (r5 == 0) goto L_0x004d;
        L_0x004a:
            r5.setSuggestions(r2);
        L_0x004d:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.urlinput.UrlInputPresenter$QueryTask.onPostExecute(java.lang.String):void");
        }
    }

    UrlInputPresenter(SearchEngine searchEngine, String str) {
        this.searchEngine = searchEngine;
        this.userAgent = str;
    }

    public void setView(View view) {
        this.view = view;
        if (view == null && this.queryTask != null) {
            this.queryTask.cancel(false);
        }
    }

    public void onInput(CharSequence charSequence, boolean z) {
        if (z && this.queryTask != null) {
            this.queryTask.cancel(true);
        }
        if (this.view != null) {
            if (charSequence.length() == 0) {
                this.view.setSuggestions(null);
                this.view.setQuickSearchVisible(false);
                return;
            }
            this.view.setQuickSearchVisible(true);
            if (!SupportUtils.isUrl(charSequence.toString())) {
                if (this.queryTask != null) {
                    this.queryTask.cancel(true);
                    this.queryTask = null;
                }
                this.queryTask = new QueryTask(this.view).execute(new String[]{this.searchEngine.buildSearchSuggestionUrl(charSequence.toString()), this.userAgent, Integer.toString(10000)});
            }
        }
    }
}
