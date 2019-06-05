package org.mozilla.focus.urlinput;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import mozilla.components.browser.domains.DomainAutoCompleteProvider.Result;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$onFilter$1 extends Lambda implements Function1<String, String> {
    final /* synthetic */ Result $result;

    UrlInputFragment$onFilter$1(Result result) {
        this.$result = result;
        super(1);
    }

    public final String invoke(String str) {
        Intrinsics.checkParameterIsNotNull(str, "it");
        return this.$result.getUrl();
    }
}
