// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MediaBrowserCompatUtils
{
    public static boolean areSameOptions(final Bundle bundle, final Bundle bundle2) {
        final boolean b = true;
        final boolean b2 = true;
        boolean b3 = true;
        if (bundle == bundle2) {
            return true;
        }
        if (bundle == null) {
            if (bundle2.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
                b3 = false;
            }
            return b3;
        }
        if (bundle2 == null) {
            return bundle.getInt("android.media.browse.extra.PAGE", -1) == -1 && bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) == -1 && b;
        }
        return bundle.getInt("android.media.browse.extra.PAGE", -1) == bundle2.getInt("android.media.browse.extra.PAGE", -1) && bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) == bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) && b2;
    }
    
    public static boolean hasDuplicatedItems(final Bundle bundle, final Bundle bundle2) {
        int int1;
        if (bundle == null) {
            int1 = -1;
        }
        else {
            int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int2;
        if (bundle2 == null) {
            int2 = -1;
        }
        else {
            int2 = bundle2.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int3;
        if (bundle == null) {
            int3 = -1;
        }
        else {
            int3 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        int int4;
        if (bundle2 == null) {
            int4 = -1;
        }
        else {
            int4 = bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        final int n = Integer.MAX_VALUE;
        int n4;
        int n5;
        if (int1 != -1 && int3 != -1) {
            final int n2 = int1 * int3;
            final int n3 = int3 + n2 - 1;
            n4 = n2;
            n5 = n3;
        }
        else {
            n5 = Integer.MAX_VALUE;
            n4 = 0;
        }
        int n6;
        int n7;
        if (int2 != -1 && int4 != -1) {
            n6 = int4 * int2;
            n7 = int4 + n6 - 1;
        }
        else {
            n6 = 0;
            n7 = n;
        }
        return (n4 <= n6 && n6 <= n5) || (n4 <= n7 && n7 <= n5);
    }
}
