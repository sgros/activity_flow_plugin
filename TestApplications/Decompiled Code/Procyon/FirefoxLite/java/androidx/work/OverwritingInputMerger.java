// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public final class OverwritingInputMerger extends InputMerger
{
    @Override
    public Data merge(final List<Data> list) {
        final Data.Builder builder = new Data.Builder();
        final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        final Iterator<Data> iterator = list.iterator();
        while (iterator.hasNext()) {
            hashMap.putAll(iterator.next().getKeyValueMap());
        }
        builder.putAll((Map<String, Object>)hashMap);
        return builder.build();
    }
}
