// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.focus.search.SearchEngineManager;
import android.content.Context;

public class SearchUtils
{
    public static String createSearchUrl(final Context context, final String s) {
        return SearchEngineManager.getInstance().getDefaultSearchEngine(context).buildSearchUrl(s);
    }
}
