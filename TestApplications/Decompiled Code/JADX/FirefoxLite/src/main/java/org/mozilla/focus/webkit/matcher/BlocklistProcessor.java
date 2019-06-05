package org.mozilla.focus.webkit.matcher;

import android.util.JsonReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mozilla.focus.webkit.matcher.util.FocusString;

public class BlocklistProcessor {
    private static final Set<String> DISCONNECT_MOVED;
    private static final Set<String> IGNORED_CATEGORIES;

    public enum ListType {
        BASE_LIST,
        OVERRIDE_LIST
    }

    private interface UrlListCallback {
        void put(String str, String str2);
    }

    private static class ListCallback implements UrlListCallback {
        final Set<String> desiredOwners;
        final List<String> list;

        ListCallback(List<String> list, Set<String> set) {
            this.list = list;
            this.desiredOwners = set;
        }

        public void put(String str, String str2) {
            if (this.desiredOwners.contains(str2)) {
                this.list.add(str);
            }
        }
    }

    private static class TrieCallback implements UrlListCallback {
        final Trie trie;

        TrieCallback(Trie trie) {
            this.trie = trie;
        }

        public void put(String str, String str2) {
            this.trie.put(FocusString.create(str).reverse());
        }
    }

    static {
        HashSet hashSet = new HashSet();
        hashSet.add("Legacy Disconnect");
        hashSet.add("Legacy Content");
        IGNORED_CATEGORIES = Collections.unmodifiableSet(hashSet);
        hashSet = new HashSet();
        hashSet.add("Facebook");
        hashSet.add("Twitter");
        DISCONNECT_MOVED = Collections.unmodifiableSet(hashSet);
    }

    public static Map<String, Trie> loadCategoryMap(JsonReader jsonReader, Map<String, Trie> map, ListType listType) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            if (jsonReader.nextName().equals("categories")) {
                extractCategories(jsonReader, map, listType);
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return map;
    }

    private static void extractCategories(JsonReader jsonReader, Map<String, Trie> map, ListType listType) throws IOException {
        jsonReader.beginObject();
        LinkedList<String> linkedList = new LinkedList();
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (IGNORED_CATEGORIES.contains(nextName)) {
                jsonReader.skipValue();
            } else if (nextName.equals("Disconnect")) {
                extractCategory(jsonReader, new ListCallback(linkedList, DISCONNECT_MOVED));
            } else {
                Trie trie;
                if (listType != ListType.BASE_LIST) {
                    trie = (Trie) map.get(nextName);
                    if (trie == null) {
                        throw new IllegalStateException("Cannot add override items to nonexistent category");
                    }
                } else if (map.containsKey(nextName)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot insert already loaded category: ");
                    stringBuilder.append(nextName);
                    throw new IllegalStateException(stringBuilder.toString());
                } else {
                    trie = Trie.createRootNode();
                    map.put(nextName, trie);
                }
                extractCategory(jsonReader, new TrieCallback(trie));
            }
        }
        Trie trie2 = (Trie) map.get("Social");
        if (trie2 == null && listType == ListType.BASE_LIST) {
            throw new IllegalStateException("Expected social list to exist. Can't copy FB/Twitter into non-existing list");
        }
        for (String create : linkedList) {
            trie2.put(FocusString.create(create).reverse());
        }
        jsonReader.endObject();
    }

    private static void extractCategory(JsonReader jsonReader, UrlListCallback urlListCallback) throws IOException {
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            extractSite(jsonReader, urlListCallback);
        }
        jsonReader.endArray();
    }

    private static void extractSite(JsonReader jsonReader, UrlListCallback urlListCallback) throws IOException {
        jsonReader.beginObject();
        String nextName = jsonReader.nextName();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
            if (jsonReader.peek().name().equals("STRING")) {
                jsonReader.skipValue();
            } else {
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
}
