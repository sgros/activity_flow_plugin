package org.mozilla.focus.utils;

import android.content.Context;
import org.mozilla.focus.search.SearchEngineManager;

public class SearchUtils {
    public static String createSearchUrl(Context context, String str) {
        return SearchEngineManager.getInstance().getDefaultSearchEngine(context).buildSearchUrl(str);
    }
}
