// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.provider;

import kotlin.TypeCastException;
import android.os.Bundle;
import android.database.Cursor;
import android.net.Uri;
import kotlin.jvm.internal.Intrinsics;
import android.content.ContentResolver;

public final class SettingPreferenceWrapper
{
    private final ContentResolver resolver;
    
    public SettingPreferenceWrapper(final ContentResolver resolver) {
        Intrinsics.checkParameterIsNotNull(resolver, "resolver");
        this.resolver = resolver;
    }
    
    private final Object getValue(final String s, String query, final Object obj) {
        final Uri withAppendedPath = Uri.withAppendedPath(SettingContract.INSTANCE.getAUTHORITY_URI(), s);
        final String value = String.valueOf(obj);
        final Object o = null;
        Object extras = null;
        Bundle bundle = null;
        Label_0119: {
            try {
                query = (String)this.resolver.query(withAppendedPath, (String[])null, (String)null, new String[] { query, value }, (String)null);
                final Object o2 = o;
                if (query != null) {
                    try {
                        extras = ((Cursor)query).getExtras();
                        if (extras != null) {
                            ((Bundle)extras).get("key");
                            ((Bundle)extras).clear();
                        }
                    }
                    finally {
                        break Label_0119;
                    }
                }
                if (query != null) {
                    ((Cursor)query).close();
                }
                if (o2 != null) {}
                return o2;
            }
            finally {
                bundle = (Bundle)extras;
            }
        }
        if (bundle != null) {
            ((Cursor)bundle).close();
        }
    }
    
    public final boolean getBoolean(final String s, final boolean b) {
        Intrinsics.checkParameterIsNotNull(s, "key");
        final Object value = this.getValue("getBoolean", s, b);
        if (value != null) {
            return (boolean)value;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
    }
    
    public final float getFloat(final String s, final float f) {
        Intrinsics.checkParameterIsNotNull(s, "key");
        final Object value = this.getValue("getFloat", s, f);
        if (value != null) {
            return (float)value;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Float");
    }
}
