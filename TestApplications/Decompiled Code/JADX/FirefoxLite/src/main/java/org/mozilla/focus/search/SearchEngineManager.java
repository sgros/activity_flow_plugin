package org.mozilla.focus.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.locale.Locales;
import org.mozilla.focus.utils.IOUtils;
import org.mozilla.focus.utils.Settings;

public class SearchEngineManager extends BroadcastReceiver {
    private static final String LOG_TAG = "SearchEngineManager";
    private static SearchEngineManager instance = new SearchEngineManager();
    private boolean loadHasBeenTriggered = false;
    private List<SearchEngine> searchEngines;

    public static SearchEngineManager getInstance() {
        return instance;
    }

    private SearchEngineManager() {
    }

    public void init(Context context) {
        context.registerReceiver(this, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        loadSearchEngines(context);
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
            loadSearchEngines(context.getApplicationContext());
        }
    }

    public void loadSearchEngines(final Context context) {
        new Thread("SearchEngines-Load") {
            public void run() {
                SearchEngineManager.this.loadFromDisk(context);
            }
        }.start();
    }

    private synchronized void loadFromDisk(Context context) {
        this.loadHasBeenTriggered = true;
        AssetManager assets = context.getAssets();
        Locale locale = Locale.getDefault();
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray loadSearchEngineListForLocale = loadSearchEngineListForLocale(context);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("search/");
            stringBuilder.append(Locales.getLanguageTag(locale));
            String stringBuilder2 = stringBuilder.toString();
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("search/");
            stringBuilder3.append(Locales.getLanguage(locale));
            String stringBuilder4 = stringBuilder3.toString();
            List asList = Arrays.asList(assets.list(stringBuilder2));
            List asList2 = Arrays.asList(assets.list(stringBuilder4));
            List asList3 = Arrays.asList(assets.list("search/default"));
            for (int i = 0; i < loadSearchEngineListForLocale.length(); i++) {
                String string = loadSearchEngineListForLocale.getString(i);
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(string);
                stringBuilder5.append(".xml");
                String stringBuilder6 = stringBuilder5.toString();
                StringBuilder stringBuilder7;
                if (asList.contains(stringBuilder6)) {
                    stringBuilder7 = new StringBuilder();
                    stringBuilder7.append(stringBuilder2);
                    stringBuilder7.append("/");
                    stringBuilder7.append(stringBuilder6);
                    arrayList.add(SearchEngineParser.load(assets, string, stringBuilder7.toString()));
                } else if (asList2.contains(stringBuilder6)) {
                    stringBuilder7 = new StringBuilder();
                    stringBuilder7.append(stringBuilder4);
                    stringBuilder7.append("/");
                    stringBuilder7.append(stringBuilder6);
                    arrayList.add(SearchEngineParser.load(assets, string, stringBuilder7.toString()));
                } else if (asList3.contains(stringBuilder6)) {
                    stringBuilder7 = new StringBuilder();
                    stringBuilder7.append("search/default/");
                    stringBuilder7.append(stringBuilder6);
                    arrayList.add(SearchEngineParser.load(assets, string, stringBuilder7.toString()));
                } else {
                    stringBuilder6 = LOG_TAG;
                    stringBuilder7 = new StringBuilder();
                    stringBuilder7.append("Couldn't find configuration for engine: ");
                    stringBuilder7.append(string);
                    Log.e(stringBuilder6, stringBuilder7.toString());
                }
            }
            this.searchEngines = arrayList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException while loading search engines", e);
            this.searchEngines = arrayList;
        } catch (JSONException e2) {
            throw new AssertionError("Reading search engine failed: ", e2);
        } catch (Throwable th) {
            this.searchEngines = arrayList;
            notifyAll();
        }
        notifyAll();
        return;
    }

    private JSONArray loadSearchEngineListForLocale(Context context) throws IOException {
        try {
            Locale locale = Locale.getDefault();
            JSONObject readAsset = IOUtils.readAsset(context, "search/search_configuration.json");
            String languageTag = Locales.getLanguageTag(locale);
            if (readAsset.has(languageTag)) {
                return readAsset.getJSONArray(languageTag);
            }
            String language = Locales.getLanguage(locale);
            if (readAsset.has(language)) {
                return readAsset.getJSONArray(language);
            }
            return readAsset.getJSONArray("default");
        } catch (JSONException e) {
            throw new AssertionError("Reading search configuration failed", e);
        }
    }

    public synchronized List<SearchEngine> getSearchEngines() {
        awaitLoadingSearchEnginesLocked();
        return this.searchEngines;
    }

    public synchronized SearchEngine getDefaultSearchEngine(Context context) {
        awaitLoadingSearchEnginesLocked();
        String defaultSearchEngineName = Settings.getInstance(context).getDefaultSearchEngineName();
        if (defaultSearchEngineName != null) {
            for (SearchEngine searchEngine : this.searchEngines) {
                if (defaultSearchEngineName.equals(searchEngine.getName())) {
                    return searchEngine;
                }
            }
        }
        return (SearchEngine) this.searchEngines.get(0);
    }

    public void awaitLoadingSearchEnginesLocked() {
        if (this.loadHasBeenTriggered) {
            while (this.searchEngines == null) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
            return;
        }
        throw new IllegalStateException("Attempting to retrieve search engines without a corresponding init()");
    }
}
