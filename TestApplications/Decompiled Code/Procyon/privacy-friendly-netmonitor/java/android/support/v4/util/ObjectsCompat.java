// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.util;

import java.util.Objects;
import android.support.annotation.RequiresApi;
import android.os.Build$VERSION;

public class ObjectsCompat
{
    private static final ImplBase IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (ImplBase)new ImplApi19();
        }
        else {
            IMPL = new ImplBase();
        }
    }
    
    private ObjectsCompat() {
    }
    
    public static boolean equals(final Object o, final Object o2) {
        return ObjectsCompat.IMPL.equals(o, o2);
    }
    
    @RequiresApi(19)
    private static class ImplApi19 extends ImplBase
    {
        @Override
        public boolean equals(final Object a, final Object b) {
            return Objects.equals(a, b);
        }
    }
    
    private static class ImplBase
    {
        public boolean equals(final Object o, final Object obj) {
            return o == obj || (o != null && o.equals(obj));
        }
    }
}
