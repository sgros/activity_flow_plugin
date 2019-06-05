// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.webkit.matcher;

import java.util.Iterator;
import org.mozilla.focus.webkit.matcher.util.FocusString;
import java.util.ArrayList;
import java.io.IOException;
import android.util.JsonReader;

class EntityListProcessor
{
    private final EntityList entityMap;
    
    private EntityListProcessor(final JsonReader jsonReader) throws IOException {
        this.entityMap = new EntityList();
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            jsonReader.skipValue();
            this.handleSite(jsonReader);
        }
        jsonReader.endObject();
    }
    
    public static EntityList getEntityMapFromJSON(final JsonReader jsonReader) throws IOException {
        return new EntityListProcessor(jsonReader).entityMap;
    }
    
    private void handleSite(final JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        final Trie rootNode = Trie.createRootNode();
        final ArrayList<String> list = new ArrayList<String>();
        while (jsonReader.hasNext()) {
            final String nextName = jsonReader.nextName();
            if (nextName.equals("properties")) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    list.add(jsonReader.nextString());
                }
                jsonReader.endArray();
            }
            else {
                if (!nextName.equals("resources")) {
                    continue;
                }
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    rootNode.put(FocusString.create(jsonReader.nextString()).reverse());
                }
                jsonReader.endArray();
            }
        }
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.entityMap.putWhiteList(FocusString.create(iterator.next()).reverse(), rootNode);
        }
        jsonReader.endObject();
    }
}
