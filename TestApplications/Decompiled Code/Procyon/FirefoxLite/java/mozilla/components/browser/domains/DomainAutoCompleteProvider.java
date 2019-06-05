// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.browser.domains;

import kotlin.text.MatchGroup;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import java.util.ArrayList;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.experimental.Job;
import kotlinx.coroutines.experimental.CoroutineStart;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlinx.coroutines.experimental.BuildersKt;
import kotlin.coroutines.experimental.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.experimental.android.HandlerContextKt;
import kotlinx.coroutines.experimental.android.HandlerContext;
import java.util.Iterator;
import java.util.Locale;
import android.content.Context;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;

public final class DomainAutoCompleteProvider
{
    private List<Domain> customDomains;
    private List<Domain> shippedDomains;
    private boolean useCustomDomains;
    private boolean useShippedDomains;
    
    public DomainAutoCompleteProvider() {
        this.customDomains = CollectionsKt__CollectionsKt.emptyList();
        this.shippedDomains = CollectionsKt__CollectionsKt.emptyList();
        this.useShippedDomains = true;
    }
    
    private final String getResultText(String substring, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(substring);
        final int length = substring.length();
        if (s != null) {
            substring = s.substring(length);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
            sb.append(substring);
            return sb.toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
    
    private final Result tryToAutocomplete(final String s, final List<Domain> list, final String s2) {
        final Locale us = Locale.US;
        Intrinsics.checkExpressionValueIsNotNull(us, "Locale.US");
        if (s != null) {
            final String lowerCase = s.toLowerCase(us);
            Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase(locale)");
            for (final Domain domain : list) {
                final StringBuilder sb = new StringBuilder();
                sb.append("www.");
                sb.append(domain.getHost());
                final String string = sb.toString();
                if (StringsKt__StringsJVMKt.startsWith$default(string, lowerCase, false, 2, null)) {
                    return new Result(this.getResultText(s, string), domain.getUrl$domains_release(), s2, list.size());
                }
                if (StringsKt__StringsJVMKt.startsWith$default(domain.getHost(), lowerCase, false, 2, null)) {
                    return new Result(this.getResultText(s, domain.getHost()), domain.getUrl$domains_release(), s2, list.size());
                }
            }
            return null;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
    
    public final Result autocomplete(final String s) {
        Intrinsics.checkParameterIsNotNull(s, "rawText");
        if (this.useCustomDomains) {
            final Result tryToAutocomplete = this.tryToAutocomplete(s, this.customDomains, "custom");
            if (tryToAutocomplete != null) {
                return tryToAutocomplete;
            }
        }
        if (this.useShippedDomains) {
            final Result tryToAutocomplete2 = this.tryToAutocomplete(s, this.shippedDomains, "default");
            if (tryToAutocomplete2 != null) {
                return tryToAutocomplete2;
            }
        }
        return new Result("", "", "", 0);
    }
    
    public final void initialize(final Context context, final boolean useShippedDomains, final boolean useCustomDomains, final boolean b) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.useCustomDomains = useCustomDomains;
        this.useShippedDomains = useShippedDomains;
        if (b) {
            BuildersKt.launch$default((CoroutineContext)HandlerContextKt.getUI(), (CoroutineStart)null, (Job)null, (Function1)null, (Function2)new DomainAutoCompleteProvider$initialize.DomainAutoCompleteProvider$initialize$1(this, context, (Continuation)null), 14, (Object)null);
        }
    }
    
    public final void onDomainsLoaded$domains_release(final List<String> list, final List<String> list2) {
        Intrinsics.checkParameterIsNotNull(list, "domains");
        Intrinsics.checkParameterIsNotNull(list2, "customDomains");
        final List<String> list3 = list;
        final ArrayList<Domain> list4 = new ArrayList<Domain>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)list3, 10));
        final Iterator<Object> iterator = list3.iterator();
        while (iterator.hasNext()) {
            list4.add(Domain.Companion.create$domains_release(iterator.next()));
        }
        this.shippedDomains = list4;
        final List<String> list5 = list2;
        final ArrayList<Domain> list6 = new ArrayList<Domain>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)list5, 10));
        final Iterator<Object> iterator2 = list5.iterator();
        while (iterator2.hasNext()) {
            list6.add(Domain.Companion.create$domains_release(iterator2.next()));
        }
        this.customDomains = list6;
    }
    
    public static final class Domain
    {
        public static final Companion Companion;
        private static final Regex urlMatcher;
        private final boolean hasWww;
        private final String host;
        private final String protocol;
        
        static {
            Companion = new Companion(null);
            urlMatcher = new Regex("(https?://)?(www.)?(.+)?");
        }
        
        public Domain(final String protocol, final boolean hasWww, final String host) {
            Intrinsics.checkParameterIsNotNull(protocol, "protocol");
            Intrinsics.checkParameterIsNotNull(host, "host");
            this.protocol = protocol;
            this.hasWww = hasWww;
            this.host = host;
        }
        
        public static final /* synthetic */ Regex access$getUrlMatcher$cp() {
            return Domain.urlMatcher;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o instanceof Domain) {
                    final Domain domain = (Domain)o;
                    if (Intrinsics.areEqual(this.protocol, domain.protocol) && this.hasWww == domain.hasWww && Intrinsics.areEqual(this.host, domain.host)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public final String getHost() {
            return this.host;
        }
        
        public final String getUrl$domains_release() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.protocol);
            String str;
            if (this.hasWww) {
                str = "www.";
            }
            else {
                str = "";
            }
            sb.append(str);
            sb.append(this.host);
            return sb.toString();
        }
        
        @Override
        public int hashCode() {
            final String protocol = this.protocol;
            int hashCode = 0;
            int hashCode2;
            if (protocol != null) {
                hashCode2 = protocol.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            int hasWww;
            if ((hasWww = (this.hasWww ? 1 : 0)) != 0) {
                hasWww = 1;
            }
            final String host = this.host;
            if (host != null) {
                hashCode = host.hashCode();
            }
            return (hashCode2 * 31 + hasWww) * 31 + hashCode;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Domain(protocol=");
            sb.append(this.protocol);
            sb.append(", hasWww=");
            sb.append(this.hasWww);
            sb.append(", host=");
            sb.append(this.host);
            sb.append(")");
            return sb.toString();
        }
        
        public static final class Companion
        {
            private Companion() {
            }
            
            public final Domain create$domains_release(String value) {
                Intrinsics.checkParameterIsNotNull(value, "url");
                final Regex access$getUrlMatcher$cp = Domain.access$getUrlMatcher$cp();
                final String s = value;
                Object value2 = null;
                final MatchResult find$default = Regex.find$default(access$getUrlMatcher$cp, s, 0, 2, null);
                if (find$default != null) {
                    final MatchGroup value3 = find$default.getGroups().get(1);
                    Label_0062: {
                        if (value3 != null) {
                            value = value3.getValue();
                            if (value != null) {
                                break Label_0062;
                            }
                        }
                        value = "http://";
                    }
                    final MatchGroup value4 = find$default.getGroups().get(2);
                    if (value4 != null) {
                        value2 = value4.getValue();
                    }
                    final boolean equal = Intrinsics.areEqual(value2, "www.");
                    final MatchGroup value5 = find$default.getGroups().get(3);
                    if (value5 != null) {
                        final String value6 = value5.getValue();
                        if (value6 != null) {
                            return new Domain(value, equal, value6);
                        }
                    }
                    throw new IllegalStateException();
                }
                throw new IllegalStateException();
            }
        }
    }
    
    public static final class Result
    {
        private final int size;
        private final String source;
        private final String text;
        private final String url;
        
        public Result(final String text, final String url, final String source, final int size) {
            Intrinsics.checkParameterIsNotNull(text, "text");
            Intrinsics.checkParameterIsNotNull(url, "url");
            Intrinsics.checkParameterIsNotNull(source, "source");
            this.text = text;
            this.url = url;
            this.source = source;
            this.size = size;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o instanceof Result) {
                    final Result result = (Result)o;
                    if (Intrinsics.areEqual(this.text, result.text) && Intrinsics.areEqual(this.url, result.url) && Intrinsics.areEqual(this.source, result.source) && this.size == result.size) {
                        return true;
                    }
                }
                return false;
            }
            return true;
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
        
        @Override
        public int hashCode() {
            final String text = this.text;
            int hashCode = 0;
            int hashCode2;
            if (text != null) {
                hashCode2 = text.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            final String url = this.url;
            int hashCode3;
            if (url != null) {
                hashCode3 = url.hashCode();
            }
            else {
                hashCode3 = 0;
            }
            final String source = this.source;
            if (source != null) {
                hashCode = source.hashCode();
            }
            return ((hashCode2 * 31 + hashCode3) * 31 + hashCode) * 31 + this.size;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Result(text=");
            sb.append(this.text);
            sb.append(", url=");
            sb.append(this.url);
            sb.append(", source=");
            sb.append(this.source);
            sb.append(", size=");
            sb.append(this.size);
            sb.append(")");
            return sb.toString();
        }
    }
}
