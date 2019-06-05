package org.mozilla.focus.webkit.matcher;

import android.util.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.mozilla.focus.webkit.matcher.util.FocusString;

class EntityListProcessor {
    private final EntityList entityMap = new EntityList();

    public static EntityList getEntityMapFromJSON(JsonReader jsonReader) throws IOException {
        return new EntityListProcessor(jsonReader).entityMap;
    }

    private EntityListProcessor(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
            handleSite(jsonReader);
        }
        jsonReader.endObject();
    }

    private void handleSite(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        Trie createRootNode = Trie.createRootNode();
        ArrayList arrayList = new ArrayList();
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("properties")) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    arrayList.add(jsonReader.nextString());
                }
                jsonReader.endArray();
            } else if (nextName.equals("resources")) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    createRootNode.put(FocusString.create(jsonReader.nextString()).reverse());
                }
                jsonReader.endArray();
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.entityMap.putWhiteList(FocusString.create((String) it.next()).reverse(), createRootNode);
        }
        jsonReader.endObject();
    }
}
