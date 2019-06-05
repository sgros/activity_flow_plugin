package mozilla.components.browser.domains;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CustomDomains.kt */
public final class CustomDomains {
    public static final CustomDomains INSTANCE = new CustomDomains();

    private CustomDomains() {
    }

    public final List<String> load(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String string = preferences(context).getString("custom_domains", "");
        Intrinsics.checkExpressionValueIsNotNull(string, "preferences(context).getString(KEY_DOMAINS, \"\")");
        Collection arrayList = new ArrayList();
        for (Object next : StringsKt__StringsKt.split$default(string, new String[]{"@<;>@"}, false, 0, 6, null)) {
            if (((((CharSequence) ((String) next)).length() == 0 ? 1 : 0) ^ 1) != 0) {
                arrayList.add(next);
            }
        }
        return (List) arrayList;
    }

    private final SharedPreferences preferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("custom_autocomplete", 0);
        Intrinsics.checkExpressionValueIsNotNull(sharedPreferences, "context.getSharedPrefere…ME, Context.MODE_PRIVATE)");
        return sharedPreferences;
    }
}
