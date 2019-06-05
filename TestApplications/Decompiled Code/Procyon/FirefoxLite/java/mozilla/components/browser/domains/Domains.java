// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.browser.domains;

import java.util.Iterator;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.List;
import kotlin.io.TextStreamsKt;
import java.io.Reader;
import java.io.BufferedReader;
import kotlin.text.Charsets;
import java.io.InputStreamReader;
import java.util.Locale;
import android.os.LocaleList;
import android.os.Build$VERSION;
import kotlin.jvm.functions.Function1;
import android.content.res.AssetManager;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import android.content.Context;

public final class Domains
{
    public static final Domains INSTANCE;
    
    static {
        INSTANCE = new Domains();
    }
    
    private Domains() {
    }
    
    private final Set<String> getAvailableDomainLists(final Context context) {
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        final AssetManager assets = context.getAssets();
        String[] list;
        try {
            list = assets.list("domains");
        }
        catch (IOException ex) {
            list = new String[0];
        }
        final LinkedHashSet<String> set2 = set;
        Intrinsics.checkExpressionValueIsNotNull(list, "domains");
        CollectionsKt__MutableCollectionsKt.addAll(set2, list);
        return set;
    }
    
    private final Set<String> getCountriesInDefaultLocaleList() {
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        final Function1<String, Object> function1 = (Function1<String, Object>)new Domains$getCountriesInDefaultLocaleList$addIfNotEmpty.Domains$getCountriesInDefaultLocaleList$addIfNotEmpty$1((LinkedHashSet)set);
        if (Build$VERSION.SDK_INT >= 24) {
            final LocaleList default1 = LocaleList.getDefault();
            for (int i = 0; i < default1.size(); ++i) {
                final Locale value = default1.get(i);
                Intrinsics.checkExpressionValueIsNotNull(value, "list.get(i)");
                final String country = value.getCountry();
                Intrinsics.checkExpressionValueIsNotNull(country, "list.get(i).country");
                function1.invoke(country);
            }
        }
        else {
            final Locale default2 = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(default2, "Locale.getDefault()");
            final String country2 = default2.getCountry();
            Intrinsics.checkExpressionValueIsNotNull(country2, "Locale.getDefault().country");
            function1.invoke(country2);
        }
        return set;
    }
    
    private final void loadDomainsForLanguage(final Context context, final Set<String> set, final String str) {
        final AssetManager assets = context.getAssets();
        List<? extends String> list;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("domains/");
            sb.append(str);
            final InputStream open = assets.open(sb.toString());
            Intrinsics.checkExpressionValueIsNotNull(open, "assetManager.open(\"domains/\" + country)");
            final InputStreamReader in = new InputStreamReader(open, Charsets.UTF_8);
            BufferedReader bufferedReader;
            if (in instanceof BufferedReader) {
                bufferedReader = (BufferedReader)in;
            }
            else {
                bufferedReader = new BufferedReader(in, 8192);
            }
            list = TextStreamsKt.readLines(bufferedReader);
        }
        catch (IOException ex) {
            list = CollectionsKt__CollectionsKt.emptyList();
        }
        set.addAll(list);
    }
    
    public final List<String> load(final Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return this.load$domains_release(context, this.getCountriesInDefaultLocaleList());
    }
    
    public final List<String> load$domains_release(final Context context, final Set<String> set) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(set, "countries");
        final LinkedHashSet<String> set2 = new LinkedHashSet<String>();
        final Set<String> availableDomainLists = this.getAvailableDomainLists(context);
        final Set<String> set3 = set;
        final ArrayList<String> list = new ArrayList<String>();
        for (final String next : set3) {
            if (availableDomainLists.contains(next)) {
                list.add(next);
            }
        }
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            Domains.INSTANCE.loadDomainsForLanguage(context, set2, iterator2.next());
        }
        this.loadDomainsForLanguage(context, set2, "global");
        return (List<String>)CollectionsKt___CollectionsKt.toList((Iterable<?>)set2);
    }
}
