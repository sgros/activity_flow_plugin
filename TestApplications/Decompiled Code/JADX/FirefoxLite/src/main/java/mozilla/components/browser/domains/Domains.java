package mozilla.components.browser.domains;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build.VERSION;
import android.os.LocaleList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p005io.TextStreamsKt;
import kotlin.text.Charsets;

/* compiled from: Domains.kt */
public final class Domains {
    public static final Domains INSTANCE = new Domains();

    private Domains() {
    }

    public final List<String> load(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return load$domains_release(context, getCountriesInDefaultLocaleList());
    }

    public final List<String> load$domains_release(Context context, Set<String> set) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(set, "countries");
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Set availableDomainLists = getAvailableDomainLists(context);
        Collection arrayList = new ArrayList();
        for (Object next : set) {
            if (availableDomainLists.contains((String) next)) {
                arrayList.add(next);
            }
        }
        for (String loadDomainsForLanguage : (List) arrayList) {
            INSTANCE.loadDomainsForLanguage(context, linkedHashSet, loadDomainsForLanguage);
        }
        loadDomainsForLanguage(context, linkedHashSet, "global");
        return CollectionsKt___CollectionsKt.toList(linkedHashSet);
    }

    private final Set<String> getAvailableDomainLists(Context context) {
        Object list;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        try {
            list = context.getAssets().list("domains");
        } catch (IOException unused) {
            list = new String[0];
        }
        Collection collection = linkedHashSet;
        Intrinsics.checkExpressionValueIsNotNull(list, "domains");
        CollectionsKt__MutableCollectionsKt.addAll(collection, list);
        return linkedHashSet;
    }

    private final void loadDomainsForLanguage(Context context, Set<String> set, String str) {
        List readLines;
        AssetManager assets = context.getAssets();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("domains/");
            stringBuilder.append(str);
            InputStream open = assets.open(stringBuilder.toString());
            Intrinsics.checkExpressionValueIsNotNull(open, "assetManager.open(\"domains/\" + country)");
            Reader inputStreamReader = new InputStreamReader(open, Charsets.UTF_8);
            readLines = TextStreamsKt.readLines(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192));
        } catch (IOException unused) {
            readLines = CollectionsKt__CollectionsKt.emptyList();
        }
        set.addAll(readLines);
    }

    private final Set<String> getCountriesInDefaultLocaleList() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        Function1 domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1 = new Domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1(linkedHashSet);
        if (VERSION.SDK_INT >= 24) {
            LocaleList localeList = LocaleList.getDefault();
            int size = localeList.size();
            for (int i = 0; i < size; i++) {
                Locale locale = localeList.get(i);
                Intrinsics.checkExpressionValueIsNotNull(locale, "list.get(i)");
                String country = locale.getCountry();
                Intrinsics.checkExpressionValueIsNotNull(country, "list.get(i).country");
                domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1.invoke(country);
            }
        } else {
            Locale locale2 = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(locale2, "Locale.getDefault()");
            String country2 = locale2.getCountry();
            Intrinsics.checkExpressionValueIsNotNull(country2, "Locale.getDefault().country");
            domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1.invoke(country2);
        }
        return linkedHashSet;
    }
}
