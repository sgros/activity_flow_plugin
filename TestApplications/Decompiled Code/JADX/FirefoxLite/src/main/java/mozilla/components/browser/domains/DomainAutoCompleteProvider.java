package mozilla.components.browser.domains;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlinx.coroutines.experimental.android.HandlerContextKt;

/* compiled from: DomainAutoCompleteProvider.kt */
public final class DomainAutoCompleteProvider {
    private List<Domain> customDomains = CollectionsKt__CollectionsKt.emptyList();
    private List<Domain> shippedDomains = CollectionsKt__CollectionsKt.emptyList();
    private boolean useCustomDomains;
    private boolean useShippedDomains = true;

    /* compiled from: DomainAutoCompleteProvider.kt */
    public static final class Domain {
        public static final Companion Companion = new Companion();
        private static final Regex urlMatcher = new Regex("(https?://)?(www.)?(.+)?");
        private final boolean hasWww;
        private final String host;
        private final String protocol;

        /* compiled from: DomainAutoCompleteProvider.kt */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            /* JADX WARNING: Missing block: B:5:0x0023, code skipped:
            if (r0 != null) goto L_0x0028;
     */
            public final mozilla.components.browser.domains.DomainAutoCompleteProvider.Domain create$domains_release(java.lang.String r5) {
                /*
                r4 = this;
                r0 = "url";
                kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r5, r0);
                r0 = mozilla.components.browser.domains.DomainAutoCompleteProvider.Domain.urlMatcher;
                r5 = (java.lang.CharSequence) r5;
                r1 = 0;
                r2 = 2;
                r3 = 0;
                r5 = kotlin.text.Regex.find$default(r0, r5, r3, r2, r1);
                if (r5 == 0) goto L_0x005b;
            L_0x0014:
                r0 = r5.getGroups();
                r3 = 1;
                r0 = r0.get(r3);
                if (r0 == 0) goto L_0x0026;
            L_0x001f:
                r0 = r0.getValue();
                if (r0 == 0) goto L_0x0026;
            L_0x0025:
                goto L_0x0028;
            L_0x0026:
                r0 = "http://";
            L_0x0028:
                r3 = r5.getGroups();
                r2 = r3.get(r2);
                if (r2 == 0) goto L_0x0036;
            L_0x0032:
                r1 = r2.getValue();
            L_0x0036:
                r2 = "www.";
                r1 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r2);
                r5 = r5.getGroups();
                r2 = 3;
                r5 = r5.get(r2);
                if (r5 == 0) goto L_0x0053;
            L_0x0047:
                r5 = r5.getValue();
                if (r5 == 0) goto L_0x0053;
            L_0x004d:
                r2 = new mozilla.components.browser.domains.DomainAutoCompleteProvider$Domain;
                r2.<init>(r0, r1, r5);
                return r2;
            L_0x0053:
                r5 = new java.lang.IllegalStateException;
                r5.<init>();
                r5 = (java.lang.Throwable) r5;
                throw r5;
            L_0x005b:
                r5 = new java.lang.IllegalStateException;
                r5.<init>();
                r5 = (java.lang.Throwable) r5;
                throw r5;
                */
                throw new UnsupportedOperationException("Method not decompiled: mozilla.components.browser.domains.DomainAutoCompleteProvider$Domain$Companion.create$domains_release(java.lang.String):mozilla.components.browser.domains.DomainAutoCompleteProvider$Domain");
            }
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof Domain) {
                    Domain domain = (Domain) obj;
                    if (Intrinsics.areEqual(this.protocol, domain.protocol)) {
                        if ((this.hasWww == domain.hasWww ? 1 : null) == null || !Intrinsics.areEqual(this.host, domain.host)) {
                            return false;
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            String str = this.protocol;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            int i2 = this.hasWww;
            if (i2 != 0) {
                i2 = 1;
            }
            hashCode = (hashCode + i2) * 31;
            String str2 = this.host;
            if (str2 != null) {
                i = str2.hashCode();
            }
            return hashCode + i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Domain(protocol=");
            stringBuilder.append(this.protocol);
            stringBuilder.append(", hasWww=");
            stringBuilder.append(this.hasWww);
            stringBuilder.append(", host=");
            stringBuilder.append(this.host);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public Domain(String str, boolean z, String str2) {
            Intrinsics.checkParameterIsNotNull(str, "protocol");
            Intrinsics.checkParameterIsNotNull(str2, "host");
            this.protocol = str;
            this.hasWww = z;
            this.host = str2;
        }

        public final String getHost() {
            return this.host;
        }

        public final String getUrl$domains_release() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.protocol);
            stringBuilder.append(this.hasWww ? "www." : "");
            stringBuilder.append(this.host);
            return stringBuilder.toString();
        }
    }

    /* compiled from: DomainAutoCompleteProvider.kt */
    public static final class Result {
        private final int size;
        private final String source;
        private final String text;
        private final String url;

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof Result) {
                    Result result = (Result) obj;
                    if (Intrinsics.areEqual(this.text, result.text) && Intrinsics.areEqual(this.url, result.url) && Intrinsics.areEqual(this.source, result.source)) {
                        if ((this.size == result.size ? 1 : null) != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            String str = this.text;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            String str2 = this.url;
            hashCode = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
            str2 = this.source;
            if (str2 != null) {
                i = str2.hashCode();
            }
            return ((hashCode + i) * 31) + this.size;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Result(text=");
            stringBuilder.append(this.text);
            stringBuilder.append(", url=");
            stringBuilder.append(this.url);
            stringBuilder.append(", source=");
            stringBuilder.append(this.source);
            stringBuilder.append(", size=");
            stringBuilder.append(this.size);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public Result(String str, String str2, String str3, int i) {
            Intrinsics.checkParameterIsNotNull(str, "text");
            Intrinsics.checkParameterIsNotNull(str2, "url");
            Intrinsics.checkParameterIsNotNull(str3, "source");
            this.text = str;
            this.url = str2;
            this.source = str3;
            this.size = i;
        }

        public final int getSize() {
            return this.size;
        }

        public final String getSource() {
            return this.source;
        }

        public final String getText() {
            return this.text;
        }

        public final String getUrl() {
            return this.url;
        }
    }

    public final Result autocomplete(String str) {
        Intrinsics.checkParameterIsNotNull(str, "rawText");
        if (this.useCustomDomains) {
            Result tryToAutocomplete = tryToAutocomplete(str, this.customDomains, "custom");
            if (tryToAutocomplete != null) {
                return tryToAutocomplete;
            }
        }
        if (this.useShippedDomains) {
            Result tryToAutocomplete2 = tryToAutocomplete(str, this.shippedDomains, "default");
            if (tryToAutocomplete2 != null) {
                return tryToAutocomplete2;
            }
        }
        return new Result("", "", "", 0);
    }

    public static /* bridge */ /* synthetic */ void initialize$default(DomainAutoCompleteProvider domainAutoCompleteProvider, Context context, boolean z, boolean z2, boolean z3, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        if ((i & 4) != 0) {
            z2 = false;
        }
        if ((i & 8) != 0) {
            z3 = true;
        }
        domainAutoCompleteProvider.initialize(context, z, z2, z3);
    }

    public final void initialize(Context context, boolean z, boolean z2, boolean z3) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.useCustomDomains = z2;
        this.useShippedDomains = z;
        if (z3) {
            BuildersKt__Builders_commonKt.launch$default(HandlerContextKt.getUI(), null, null, null, new DomainAutoCompleteProvider$initialize$1(this, context, null), 14, null);
        }
    }

    public final void onDomainsLoaded$domains_release(List<String> list, List<String> list2) {
        Intrinsics.checkParameterIsNotNull(list, "domains");
        Intrinsics.checkParameterIsNotNull(list2, "customDomains");
        Iterable<String> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable, 10));
        for (String create$domains_release : iterable) {
            arrayList.add(Domain.Companion.create$domains_release(create$domains_release));
        }
        this.shippedDomains = (List) arrayList;
        Iterable<String> iterable2 = list2;
        Collection arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(iterable2, 10));
        for (String create$domains_release2 : iterable2) {
            arrayList2.add(Domain.Companion.create$domains_release(create$domains_release2));
        }
        this.customDomains = (List) arrayList2;
    }

    private final Result tryToAutocomplete(String str, List<Domain> list, String str2) {
        Locale locale = Locale.US;
        Intrinsics.checkExpressionValueIsNotNull(locale, "Locale.US");
        if (str != null) {
            String toLowerCase = str.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(toLowerCase, "(this as java.lang.String).toLowerCase(locale)");
            for (Domain domain : list) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("www.");
                stringBuilder.append(domain.getHost());
                String stringBuilder2 = stringBuilder.toString();
                if (StringsKt__StringsJVMKt.startsWith$default(stringBuilder2, toLowerCase, false, 2, null)) {
                    return new Result(getResultText(str, stringBuilder2), domain.getUrl$domains_release(), str2, list.size());
                }
                if (StringsKt__StringsJVMKt.startsWith$default(domain.getHost(), toLowerCase, false, 2, null)) {
                    return new Result(getResultText(str, domain.getHost()), domain.getUrl$domains_release(), str2, list.size());
                }
            }
            return null;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    private final String getResultText(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        int length = str.length();
        if (str2 != null) {
            str = str2.substring(length);
            Intrinsics.checkExpressionValueIsNotNull(str, "(this as java.lang.String).substring(startIndex)");
            stringBuilder.append(str);
            return stringBuilder.toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
}
