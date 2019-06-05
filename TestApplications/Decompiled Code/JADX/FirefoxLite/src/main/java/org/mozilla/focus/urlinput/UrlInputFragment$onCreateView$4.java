package org.mozilla.focus.urlinput;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;
import mozilla.components.p006ui.autocomplete.InlineAutocompleteEditText;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$onCreateView$4 extends FunctionReference implements Function2<String, InlineAutocompleteEditText, Unit> {
    UrlInputFragment$onCreateView$4(UrlInputFragment urlInputFragment) {
        super(2, urlInputFragment);
    }

    public final String getName() {
        return "onFilter";
    }

    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(UrlInputFragment.class);
    }

    public final String getSignature() {
        return "onFilter(Ljava/lang/String;Lmozilla/components/ui/autocomplete/InlineAutocompleteEditText;)V";
    }

    public final void invoke(String str, InlineAutocompleteEditText inlineAutocompleteEditText) {
        Intrinsics.checkParameterIsNotNull(str, "p1");
        ((UrlInputFragment) this.receiver).onFilter(str, inlineAutocompleteEditText);
    }
}
