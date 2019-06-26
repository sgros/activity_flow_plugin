// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import java.lang.reflect.InvocationTargetException;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import java.lang.reflect.Constructor;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class ParceledListSliceAdapterApi21
{
    private static Constructor sConstructor;
    
    static {
        try {
            ParceledListSliceAdapterApi21.sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    static Object newInstance(final List<MediaBrowser$MediaItem> list) {
        Object instance;
        try {
            instance = ParceledListSliceAdapterApi21.sConstructor.newInstance(list);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            t.printStackTrace();
            instance = null;
        }
        return instance;
    }
}
