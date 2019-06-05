package org.mozilla.focus.utils;

import android.content.Context;
import org.mozilla.focus.search.SearchEngineManager;

public class SearchUtils {
   public static String createSearchUrl(Context var0, String var1) {
      return SearchEngineManager.getInstance().getDefaultSearchEngine(var0).buildSearchUrl(var1);
   }
}
