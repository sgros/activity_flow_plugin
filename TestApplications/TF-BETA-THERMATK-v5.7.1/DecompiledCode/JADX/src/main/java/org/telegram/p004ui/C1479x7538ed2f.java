package org.telegram.p004ui;

import java.util.Comparator;
import org.telegram.p004ui.CountrySelectActivity.Country;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$CountrySelectActivity$CountryAdapter$GbbT4_eUqPD5K07oYJV3z7sG8q4 */
public final /* synthetic */ class C1479x7538ed2f implements Comparator {
    public static final /* synthetic */ C1479x7538ed2f INSTANCE = new C1479x7538ed2f();

    private /* synthetic */ C1479x7538ed2f() {
    }

    public final int compare(Object obj, Object obj2) {
        return ((Country) obj).name.compareTo(((Country) obj2).name);
    }
}
