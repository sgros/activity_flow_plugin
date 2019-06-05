package org.mozilla.focus.urlinput;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$onCreateView$1 extends FunctionReference implements Function2<String, String, Unit> {
    UrlInputFragment$onCreateView$1(UrlInputFragment urlInputFragment) {
        super(2, urlInputFragment);
    }

    public final String getName() {
        return "onTextChange";
    }

    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(UrlInputFragment.class);
    }

    public final String getSignature() {
        return "onTextChange(Ljava/lang/String;Ljava/lang/String;)V";
    }

    public final void invoke(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "p1");
        Intrinsics.checkParameterIsNotNull(str2, "p2");
        ((UrlInputFragment) this.receiver).onTextChange(str, str2);
    }
}
