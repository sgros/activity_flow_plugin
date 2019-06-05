package org.mozilla.focus.webkit.matcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.p001v4.util.ArrayMap;
import android.util.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.mozilla.focus.webkit.matcher.BlocklistProcessor.ListType;
import org.mozilla.focus.webkit.matcher.util.FocusString;
import org.mozilla.rocket.C0769R;

public class UrlMatcher implements OnSharedPreferenceChangeListener {
    private static final String[] WEBFONT_EXTENSIONS = new String[]{".woff2", ".woff", ".eot", ".ttf", ".otf"};
    private boolean blockWebfonts = true;
    private final Map<String, Trie> categories;
    private final Map<String, String> categoryPrefMap;
    private final Set<String> enabledCategories = new HashSet();
    private final EntityList entityList;
    private final HashSet<String> previouslyMatched = new HashSet();
    private final HashSet<String> previouslyUnmatched = new HashSet();

    private static Map<String, String> loadDefaultPrefMap(Context context) {
        ArrayMap arrayMap = new ArrayMap();
        arrayMap.put(context.getString(C0769R.string.pref_key_privacy_block_ads), "Advertising");
        arrayMap.put(context.getString(C0769R.string.pref_key_privacy_block_analytics), "Analytics");
        arrayMap.put(context.getString(C0769R.string.pref_key_privacy_block_social), "Social");
        arrayMap.put(context.getString(C0769R.string.pref_key_privacy_block_other), "Content");
        arrayMap.put(context.getString(C0769R.string.pref_key_privacy_block_abpindo), "ABPIndo");
        arrayMap.put(context.getString(C0769R.string.pref_key_performance_block_webfonts), "Webfonts");
        return Collections.unmodifiableMap(arrayMap);
    }

    public static UrlMatcher loadMatcher(Context context, int i, int[] iArr, int i2, int i3) {
        Throwable th;
        Map loadDefaultPrefMap = loadDefaultPrefMap(context);
        HashMap hashMap = new HashMap(5);
        JsonReader jsonReader;
        try {
            jsonReader = new JsonReader(new InputStreamReader(context.getResources().openRawResource(i), StandardCharsets.UTF_8));
            BlocklistProcessor.loadCategoryMap(jsonReader, hashMap, ListType.BASE_LIST);
            jsonReader.close();
            if (iArr != null) {
                int i4 = 0;
                while (i4 < iArr.length) {
                    JsonReader jsonReader2;
                    try {
                        jsonReader2 = new JsonReader(new InputStreamReader(context.getResources().openRawResource(iArr[i4]), StandardCharsets.UTF_8));
                        BlocklistProcessor.loadCategoryMap(jsonReader2, hashMap, ListType.OVERRIDE_LIST);
                        jsonReader2.close();
                        i4++;
                    } catch (IOException unused) {
                        throw new IllegalStateException("Unable to parse override blacklist");
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
            }
            JsonReader jsonReader3;
            try {
                jsonReader3 = new JsonReader(new InputStreamReader(context.getResources().openRawResource(i2), StandardCharsets.UTF_8));
                EntityList entityMapFromJSON = EntityListProcessor.getEntityMapFromJSON(jsonReader3);
                jsonReader3.close();
                try {
                    jsonReader3 = new JsonReader(new InputStreamReader(context.getResources().openRawResource(i3), StandardCharsets.UTF_8));
                    BlocklistProcessor.loadCategoryMap(jsonReader3, hashMap, ListType.BASE_LIST);
                    jsonReader3.close();
                    return new UrlMatcher(context, loadDefaultPrefMap, hashMap, entityMapFromJSON);
                } catch (IOException unused2) {
                    throw new IllegalStateException("Unable to parse abpindo list");
                } catch (Throwable th22) {
                    th.addSuppressed(th22);
                }
            } catch (IOException unused3) {
                throw new IllegalStateException("Unable to parse entity list");
            } catch (Throwable th222) {
                th.addSuppressed(th222);
            }
        } catch (IOException unused4) {
            throw new IllegalStateException("Unable to parse blacklist");
        } catch (Throwable th2222) {
            th.addSuppressed(th2222);
        }
    }

    UrlMatcher(Context context, Map<String, String> map, Map<String, Trie> map2, EntityList entityList) {
        this.categoryPrefMap = map;
        this.entityList = entityList;
        this.categories = map2;
        for (Entry entry : map2.entrySet()) {
            if (map.values().contains(entry.getKey())) {
                this.enabledCategories.add(entry.getKey());
            } else {
                throw new IllegalArgumentException("categoryMap contains undeclared category");
            }
        }
        loadPrefs(context);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        String str2 = (String) this.categoryPrefMap.get(str);
        if (str2 != null) {
            setCategoryEnabled(str2, sharedPreferences.getBoolean(str, false));
        }
    }

    private void loadPrefs(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (Entry entry : this.categoryPrefMap.entrySet()) {
            setCategoryEnabled((String) entry.getValue(), defaultSharedPreferences.getBoolean((String) entry.getKey(), shouldDefaultBlock(context, (String) entry.getKey())));
        }
    }

    private boolean shouldDefaultBlock(Context context, String str) {
        return context.getString(C0769R.string.pref_key_privacy_block_ads).equals(str) || context.getString(C0769R.string.pref_key_privacy_block_analytics).equals(str) || context.getString(C0769R.string.pref_key_privacy_block_social).equals(str) || context.getString(C0769R.string.pref_key_privacy_block_abpindo).equals(str);
    }

    public Set<String> getCategories() {
        return this.categories.keySet();
    }

    public void setCategoryEnabled(String str, boolean z) {
        if ("Webfonts".equals(str)) {
            this.blockWebfonts = z;
        } else if (getCategories().contains(str)) {
            if (z) {
                if (!this.enabledCategories.contains(str)) {
                    this.enabledCategories.add(str);
                    this.previouslyUnmatched.clear();
                }
            } else if (this.enabledCategories.contains(str)) {
                this.enabledCategories.remove(str);
                this.previouslyMatched.clear();
            }
        } else {
            throw new IllegalArgumentException("Can't enable/disable inexistant category");
        }
    }

    public boolean matches(Uri uri, Uri uri2) {
        String path = uri.getPath();
        if (path == null) {
            return false;
        }
        if (this.blockWebfonts) {
            for (String endsWith : WEBFONT_EXTENSIONS) {
                if (path.endsWith(endsWith)) {
                    return true;
                }
            }
        }
        path = uri.toString();
        if (this.previouslyUnmatched.contains(path)) {
            return false;
        }
        if (this.entityList != null && this.entityList.isWhiteListed(uri2, uri)) {
            return false;
        }
        String host = uri.getHost();
        String host2 = uri2.getHost();
        if (host2 != null && host2.equals(host)) {
            return false;
        }
        if (this.previouslyMatched.contains(path)) {
            return true;
        }
        FocusString reverse = FocusString.create(host).reverse();
        for (Entry entry : this.categories.entrySet()) {
            if (this.enabledCategories.contains(entry.getKey()) && ((Trie) entry.getValue()).findNode(reverse) != null) {
                this.previouslyMatched.add(path);
                return true;
            }
        }
        this.previouslyUnmatched.add(path);
        return false;
    }
}
