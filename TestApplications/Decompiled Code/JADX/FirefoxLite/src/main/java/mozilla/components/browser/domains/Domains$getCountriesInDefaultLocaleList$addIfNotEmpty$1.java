package mozilla.components.browser.domains;

import android.text.TextUtils;
import java.util.LinkedHashSet;
import java.util.Locale;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: Domains.kt */
final class Domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1 extends Lambda implements Function1<String, Unit> {
    final /* synthetic */ LinkedHashSet $countries;

    Domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1(LinkedHashSet linkedHashSet) {
        this.$countries = linkedHashSet;
        super(1);
    }

    public final void invoke(String str) {
        Intrinsics.checkParameterIsNotNull(str, "c");
        if (!TextUtils.isEmpty(str)) {
            LinkedHashSet linkedHashSet = this.$countries;
            Locale locale = Locale.US;
            Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
            str = str.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(str, "(this as java.lang.String).toLowerCase(locale)");
            linkedHashSet.add(str);
        }
    }
}
