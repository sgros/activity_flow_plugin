// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit.matcher;

import java.io.IOException;
import java.util.Iterator;
import org.mozilla.focus.webkit.matcher.util.FocusString;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import android.util.JsonReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlocklistProcessor
{
    private static final Set<String> DISCONNECT_MOVED;
    private static final Set<String> IGNORED_CATEGORIES;
    
    static {
        final HashSet<String> s = new HashSet<String>();
        s.add("Legacy Disconnect");
        s.add("Legacy Content");
        IGNORED_CATEGORIES = Collections.unmodifiableSet((Set<?>)s);
        final HashSet<String> s2 = new HashSet<String>();
        s2.add("Facebook");
        s2.add("Twitter");
        DISCONNECT_MOVED = Collections.unmodifiableSet((Set<?>)s2);
    }
    
    private static void extractCategories(final JsonReader jsonReader, final Map<String, Trie> map, final ListType listType) throws IOException {
        jsonReader.beginObject();
        final LinkedList<String> list = new LinkedList<String>();
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            if (BlocklistProcessor.IGNORED_CATEGORIES.contains(nextName)) {
                jsonReader.skipValue();
            }
            else if (nextName.equals("Disconnect")) {
                extractCategory(jsonReader, (UrlListCallback)new ListCallback(list, BlocklistProcessor.DISCONNECT_MOVED));
            }
            else {
                Trie rootNode;
                if (listType == ListType.BASE_LIST) {
                    if (map.containsKey(nextName)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Cannot insert already loaded category: ");
                        sb.append(nextName);
                        throw new IllegalStateException(sb.toString());
                    }
                    rootNode = Trie.createRootNode();
                    map.put(nextName, rootNode);
                }
                else {
                    rootNode = map.get(nextName);
                    if (rootNode == null) {
                        throw new IllegalStateException("Cannot add override items to nonexistent category");
                    }
                }
                extractCategory(jsonReader, (UrlListCallback)new TrieCallback(rootNode));
            }
        }
        final Trie trie = map.get("Social");
        if (trie == null && listType == ListType.BASE_LIST) {
            throw new IllegalStateException("Expected social list to exist. Can't copy FB/Twitter into non-existing list");
        }
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            trie.put(FocusString.create(iterator.next()).reverse());
        }
        jsonReader.endObject();
    }
    
    private static void extractCategory(final JsonReader jsonReader, final UrlListCallback urlListCallback) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            extractSite(jsonReader, urlListCallback);
        }
        jsonReader.endArray();
    }
    
    private static void extractSite(final JsonReader jsonReader, final UrlListCallback urlListCallback) throws IOException {
        jsonReader.beginObject();
        final String nextName = jsonReader.nextName();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
            if (jsonReader.peek().name().equals("STRING")) {
                jsonReader.skipValue();
            }
            else {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    urlListCallback.put(jsonReader.nextString(), nextName);
                }
                jsonReader.endArray();
            }
        }
        jsonReader.endObject();
        jsonReader.endObject();
    }
    
    public static Map<String, Trie> loadCategoryMap(final JsonReader jsonReader, final Map<String, Trie> map, final ListType listType) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            if (jsonReader.nextName().equals("categories")) {
                extractCategories(jsonReader, map, listType);
            }
            else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return map;
    }
    
    private static class ListCallback implements UrlListCallback
    {
        final Set<String> desiredOwners;
        final List<String> list;
        
        ListCallback(final List<String> list, final Set<String> desiredOwners) {
            this.list = list;
            this.desiredOwners = desiredOwners;
        }
        
        @Override
        public void put(final String s, final String s2) {
            if (this.desiredOwners.contains(s2)) {
                this.list.add(s);
            }
        }
    }
    
    public enum ListType
    {
        BASE_LIST, 
        OVERRIDE_LIST;
    }
    
    private static class TrieCallback implements UrlListCallback
    {
        final Trie trie;
        
        TrieCallback(final Trie trie) {
            this.trie = trie;
        }
        
        @Override
        public void put(final String s, final String s2) {
            this.trie.put(FocusString.create(s).reverse());
        }
    }
    
    private interface UrlListCallback
    {
        void put(final String p0, final String p1);
    }
}
