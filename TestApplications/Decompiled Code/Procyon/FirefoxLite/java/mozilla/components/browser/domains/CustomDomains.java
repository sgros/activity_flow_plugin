// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.browser.domains;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import android.content.SharedPreferences;
import android.content.Context;

public final class CustomDomains
{
    public static final CustomDomains INSTANCE;
    
    static {
        INSTANCE = new CustomDomains();
    }
    
    private CustomDomains() {
    }
    
    private final SharedPreferences preferences(final Context context) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences("custom_autocomplete", 0);
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "context.getSharedPrefere\u2026ME, Context.MODE_PRIVATE)");
        return sharedPreferences;
    }
    
    public final List<String> load(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        final String string = this.preferences(context).getString("custom_domains", "");
        Intrinsics.checkExpressionValueIsNotNull(string, "preferences(context).getString(KEY_DOMAINS, \"\")");
        final List list = StringsKt__StringsKt.split$default(string, new String[] { "@<;>@" }, false, 0, 6, null);
        final ArrayList<String> list2 = new ArrayList<String>();
        for (final String next : list) {
            if (next.length() == 0 ^ true) {
                list2.add(next);
            }
        }
        return list2;
    }
}
