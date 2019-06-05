// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit.matcher;

import org.mozilla.focus.webkit.matcher.util.FocusString;
import android.net.Uri;
import android.content.SharedPreferences;
import java.io.IOException;
import java.io.Reader;
import android.util.JsonReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Collections;
import android.support.v4.util.ArrayMap;
import java.util.Iterator;
import android.preference.PreferenceManager;
import android.content.Context;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;

public class UrlMatcher implements SharedPreferences$OnSharedPreferenceChangeListener
{
    private static final String[] WEBFONT_EXTENSIONS;
    private boolean blockWebfonts;
    private final Map<String, Trie> categories;
    private final Map<String, String> categoryPrefMap;
    private final Set<String> enabledCategories;
    private final EntityList entityList;
    private final HashSet<String> previouslyMatched;
    private final HashSet<String> previouslyUnmatched;
    
    static {
        WEBFONT_EXTENSIONS = new String[] { ".woff2", ".woff", ".eot", ".ttf", ".otf" };
    }
    
    UrlMatcher(final Context context, final Map<String, String> categoryPrefMap, final Map<String, Trie> categories, final EntityList entityList) {
        this.enabledCategories = new HashSet<String>();
        this.previouslyMatched = new HashSet<String>();
        this.previouslyUnmatched = new HashSet<String>();
        this.blockWebfonts = true;
        this.categoryPrefMap = categoryPrefMap;
        this.entityList = entityList;
        this.categories = categories;
        for (final Map.Entry<String, Trie> entry : categories.entrySet()) {
            if (!categoryPrefMap.values().contains(entry.getKey())) {
                throw new IllegalArgumentException("categoryMap contains undeclared category");
            }
            this.enabledCategories.add(entry.getKey());
        }
        this.loadPrefs(context);
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
    }
    
    private static Map<String, String> loadDefaultPrefMap(final Context context) {
        final ArrayMap<String, String> m = new ArrayMap<String, String>();
        m.put(context.getString(2131755319), "Advertising");
        m.put(context.getString(2131755320), "Analytics");
        m.put(context.getString(2131755322), "Social");
        m.put(context.getString(2131755321), "Content");
        m.put(context.getString(2131755318), "ABPIndo");
        m.put(context.getString(2131755317), "Webfonts");
        return (Map<String, String>)Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    public static UrlMatcher loadMatcher(final Context context, int i, int[] array, final int n, final int n2) {
        final Map<String, String> loadDefaultPrefMap = loadDefaultPrefMap(context);
        final HashMap<String, Trie> hashMap = new HashMap<String, Trie>(5);
        try {
            JsonReader jsonReader = new JsonReader((Reader)new InputStreamReader(context.getResources().openRawResource(i), StandardCharsets.UTF_8));
            final int[] array2 = null;
            final int[] array3 = null;
            Object o = null;
            final JsonReader jsonReader2 = null;
            try {
                try {
                    BlocklistProcessor.loadCategoryMap(jsonReader, hashMap, BlocklistProcessor.ListType.BASE_LIST);
                    jsonReader.close();
                    if (array != null) {
                        i = 0;
                        while (i < array.length) {
                            try {
                                o = new InputStreamReader(context.getResources().openRawResource(array[i]), StandardCharsets.UTF_8);
                                jsonReader = new JsonReader((Reader)o);
                                o = jsonReader2;
                                try {
                                    try {
                                        BlocklistProcessor.loadCategoryMap(jsonReader, hashMap, BlocklistProcessor.ListType.OVERRIDE_LIST);
                                        jsonReader.close();
                                        ++i;
                                    }
                                    finally {
                                        if (o != null) {
                                            final JsonReader jsonReader3 = jsonReader;
                                            jsonReader3.close();
                                        }
                                        else {
                                            jsonReader.close();
                                        }
                                    }
                                }
                                catch (Throwable t) {}
                                try {
                                    final JsonReader jsonReader3 = jsonReader;
                                    jsonReader3.close();
                                }
                                catch (Throwable t2) {}
                            }
                            catch (IOException ex) {
                                throw new IllegalStateException("Unable to parse override blacklist");
                            }
                            break;
                        }
                    }
                    try {
                        o = new JsonReader((Reader)new InputStreamReader(context.getResources().openRawResource(n), StandardCharsets.UTF_8));
                        array = array3;
                        try {
                            try {
                                final EntityList entityMapFromJSON = EntityListProcessor.getEntityMapFromJSON((JsonReader)o);
                                ((JsonReader)o).close();
                                try {
                                    array = (int[])(Object)new InputStreamReader(context.getResources().openRawResource(n2), StandardCharsets.UTF_8);
                                    o = new JsonReader((Reader)(Object)array);
                                    array = array2;
                                    try {
                                        try {
                                            BlocklistProcessor.loadCategoryMap((JsonReader)o, hashMap, BlocklistProcessor.ListType.BASE_LIST);
                                            ((JsonReader)o).close();
                                            return new UrlMatcher(context, loadDefaultPrefMap, hashMap, entityMapFromJSON);
                                        }
                                        finally {
                                            if (array != null) {
                                                final JsonReader jsonReader4 = (JsonReader)o;
                                                jsonReader4.close();
                                            }
                                            else {
                                                ((JsonReader)o).close();
                                            }
                                        }
                                    }
                                    catch (Throwable t3) {}
                                    try {
                                        final JsonReader jsonReader4 = (JsonReader)o;
                                        jsonReader4.close();
                                    }
                                    catch (Throwable t4) {}
                                }
                                catch (IOException ex2) {
                                    throw new IllegalStateException("Unable to parse abpindo list");
                                }
                            }
                            finally {
                                if (array != null) {
                                    final JsonReader jsonReader5 = (JsonReader)o;
                                    jsonReader5.close();
                                }
                                else {
                                    ((JsonReader)o).close();
                                }
                            }
                        }
                        catch (Throwable t5) {}
                        try {
                            final JsonReader jsonReader5 = (JsonReader)o;
                            jsonReader5.close();
                        }
                        catch (Throwable t6) {}
                    }
                    catch (IOException ex3) {
                        throw new IllegalStateException("Unable to parse entity list");
                    }
                }
                finally {
                    if (o != null) {
                        final JsonReader jsonReader6 = jsonReader;
                        jsonReader6.close();
                    }
                    else {
                        jsonReader.close();
                    }
                }
            }
            catch (Throwable t7) {}
            try {
                final JsonReader jsonReader6 = jsonReader;
                jsonReader6.close();
            }
            catch (Throwable t8) {}
        }
        catch (IOException ex4) {
            throw new IllegalStateException("Unable to parse blacklist");
        }
    }
    
    private void loadPrefs(final Context context) {
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (final Map.Entry<String, String> entry : this.categoryPrefMap.entrySet()) {
            this.setCategoryEnabled(entry.getValue(), defaultSharedPreferences.getBoolean((String)entry.getKey(), this.shouldDefaultBlock(context, entry.getKey())));
        }
    }
    
    private boolean shouldDefaultBlock(final Context context, final String s) {
        final String string = context.getString(2131755319);
        final String string2 = context.getString(2131755320);
        final String string3 = context.getString(2131755322);
        final String string4 = context.getString(2131755318);
        return string.equals(s) || string2.equals(s) || string3.equals(s) || string4.equals(s);
    }
    
    public Set<String> getCategories() {
        return this.categories.keySet();
    }
    
    public boolean matches(final Uri uri, final Uri uri2) {
        final String path = uri.getPath();
        if (path == null) {
            return false;
        }
        if (this.blockWebfonts) {
            final String[] webfont_EXTENSIONS = UrlMatcher.WEBFONT_EXTENSIONS;
            for (int length = webfont_EXTENSIONS.length, i = 0; i < length; ++i) {
                if (path.endsWith(webfont_EXTENSIONS[i])) {
                    return true;
                }
            }
        }
        final String string = uri.toString();
        if (this.previouslyUnmatched.contains(string)) {
            return false;
        }
        if (this.entityList != null && this.entityList.isWhiteListed(uri2, uri)) {
            return false;
        }
        final String host = uri.getHost();
        final String host2 = uri2.getHost();
        if (host2 != null && host2.equals(host)) {
            return false;
        }
        if (this.previouslyMatched.contains(string)) {
            return true;
        }
        final FocusString reverse = FocusString.create(host).reverse();
        for (final Map.Entry<String, Trie> entry : this.categories.entrySet()) {
            if (this.enabledCategories.contains(entry.getKey()) && entry.getValue().findNode(reverse) != null) {
                this.previouslyMatched.add(string);
                return true;
            }
        }
        this.previouslyUnmatched.add(string);
        return false;
    }
    
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        final String s2 = this.categoryPrefMap.get(s);
        if (s2 != null) {
            this.setCategoryEnabled(s2, sharedPreferences.getBoolean(s, false));
        }
    }
    
    public void setCategoryEnabled(final String anObject, final boolean blockWebfonts) {
        if ("Webfonts".equals(anObject)) {
            this.blockWebfonts = blockWebfonts;
            return;
        }
        if (this.getCategories().contains(anObject)) {
            if (blockWebfonts) {
                if (this.enabledCategories.contains(anObject)) {
                    return;
                }
                this.enabledCategories.add(anObject);
                this.previouslyUnmatched.clear();
            }
            else {
                if (!this.enabledCategories.contains(anObject)) {
                    return;
                }
                this.enabledCategories.remove(anObject);
                this.previouslyMatched.clear();
            }
            return;
        }
        throw new IllegalArgumentException("Can't enable/disable inexistant category");
    }
}
