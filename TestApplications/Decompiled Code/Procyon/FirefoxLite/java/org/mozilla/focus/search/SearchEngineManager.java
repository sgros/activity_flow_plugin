// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.search;

import android.content.Intent;
import android.content.IntentFilter;
import java.util.Iterator;
import org.mozilla.focus.utils.Settings;
import org.json.JSONObject;
import org.mozilla.focus.utils.IOUtils;
import org.json.JSONArray;
import android.content.res.AssetManager;
import java.io.IOException;
import org.json.JSONException;
import android.util.Log;
import java.util.Arrays;
import org.mozilla.focus.locale.Locales;
import java.util.ArrayList;
import java.util.Locale;
import android.content.Context;
import java.util.List;
import android.content.BroadcastReceiver;

public class SearchEngineManager extends BroadcastReceiver
{
    private static final String LOG_TAG = "SearchEngineManager";
    private static SearchEngineManager instance;
    private boolean loadHasBeenTriggered;
    private List<SearchEngine> searchEngines;
    
    static {
        SearchEngineManager.instance = new SearchEngineManager();
    }
    
    private SearchEngineManager() {
        this.loadHasBeenTriggered = false;
    }
    
    public static SearchEngineManager getInstance() {
        return SearchEngineManager.instance;
    }
    
    private void loadFromDisk(final Context context) {
        synchronized (this) {
            this.loadHasBeenTriggered = true;
            final AssetManager assets = context.getAssets();
            final Locale default1 = Locale.getDefault();
            final ArrayList<SearchEngine> searchEngines = new ArrayList<SearchEngine>();
            while (true) {
                try {
                    try {
                        final JSONArray loadSearchEngineListForLocale = this.loadSearchEngineListForLocale(context);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("search/");
                        sb.append(Locales.getLanguageTag(default1));
                        final String string = sb.toString();
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("search/");
                        sb2.append(Locales.getLanguage(default1));
                        final String string2 = sb2.toString();
                        final List<String> list = Arrays.asList(assets.list(string));
                        final List<String> list2 = Arrays.asList(assets.list(string2));
                        final List<String> list3 = Arrays.asList(assets.list("search/default"));
                        for (int i = 0; i < loadSearchEngineListForLocale.length(); ++i) {
                            final String string3 = loadSearchEngineListForLocale.getString(i);
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append(string3);
                            sb3.append(".xml");
                            final String string4 = sb3.toString();
                            if (list.contains(string4)) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append(string);
                                sb4.append("/");
                                sb4.append(string4);
                                searchEngines.add(SearchEngineParser.load(assets, string3, sb4.toString()));
                            }
                            else if (list2.contains(string4)) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append(string2);
                                sb5.append("/");
                                sb5.append(string4);
                                searchEngines.add(SearchEngineParser.load(assets, string3, sb5.toString()));
                            }
                            else if (list3.contains(string4)) {
                                final StringBuilder sb6 = new StringBuilder();
                                sb6.append("search/default/");
                                sb6.append(string4);
                                searchEngines.add(SearchEngineParser.load(assets, string3, sb6.toString()));
                            }
                            else {
                                final String log_TAG = SearchEngineManager.LOG_TAG;
                                final StringBuilder sb7 = new StringBuilder();
                                sb7.append("Couldn't find configuration for engine: ");
                                sb7.append(string3);
                                Log.e(log_TAG, sb7.toString());
                            }
                        }
                        this.searchEngines = searchEngines;
                        this.notifyAll();
                    }
                    finally {}
                }
                catch (JSONException cause) {
                    throw new AssertionError("Reading search engine failed: ", (Throwable)cause);
                }
                catch (IOException ex) {
                    Log.e(SearchEngineManager.LOG_TAG, "IOException while loading search engines", (Throwable)ex);
                    this.searchEngines = searchEngines;
                    continue;
                }
                break;
            }
            return;
            this.searchEngines = searchEngines;
            this.notifyAll();
        }
    }
    
    private JSONArray loadSearchEngineListForLocale(final Context context) throws IOException {
        try {
            final Locale default1 = Locale.getDefault();
            final JSONObject asset = IOUtils.readAsset(context, "search/search_configuration.json");
            final String languageTag = Locales.getLanguageTag(default1);
            if (asset.has(languageTag)) {
                return asset.getJSONArray(languageTag);
            }
            final String language = Locales.getLanguage(default1);
            if (asset.has(language)) {
                return asset.getJSONArray(language);
            }
            return asset.getJSONArray("default");
        }
        catch (JSONException cause) {
            throw new AssertionError("Reading search configuration failed", (Throwable)cause);
        }
    }
    
    public void awaitLoadingSearchEnginesLocked() {
        if (this.loadHasBeenTriggered) {
            while (this.searchEngines == null) {
                try {
                    this.wait();
                }
                catch (InterruptedException ex) {}
            }
            return;
        }
        throw new IllegalStateException("Attempting to retrieve search engines without a corresponding init()");
    }
    
    public SearchEngine getDefaultSearchEngine(final Context context) {
        synchronized (this) {
            this.awaitLoadingSearchEnginesLocked();
            final String defaultSearchEngineName = Settings.getInstance(context).getDefaultSearchEngineName();
            if (defaultSearchEngineName != null) {
                for (final SearchEngine searchEngine : this.searchEngines) {
                    if (defaultSearchEngineName.equals(searchEngine.getName())) {
                        return searchEngine;
                    }
                }
            }
            return this.searchEngines.get(0);
        }
    }
    
    public List<SearchEngine> getSearchEngines() {
        synchronized (this) {
            this.awaitLoadingSearchEnginesLocked();
            return this.searchEngines;
        }
    }
    
    public void init(final Context context) {
        context.registerReceiver((BroadcastReceiver)this, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.loadSearchEngines(context);
    }
    
    public void loadSearchEngines(final Context context) {
        new Thread("SearchEngines-Load") {
            @Override
            public void run() {
                SearchEngineManager.this.loadFromDisk(context);
            }
        }.start();
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (!"android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
            return;
        }
        this.loadSearchEngines(context.getApplicationContext());
    }
}
