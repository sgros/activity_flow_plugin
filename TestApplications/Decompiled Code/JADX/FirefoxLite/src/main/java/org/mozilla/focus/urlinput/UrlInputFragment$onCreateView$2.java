package org.mozilla.focus.urlinput;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReference;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$onCreateView$2 extends FunctionReference implements Function0<Unit> {
    UrlInputFragment$onCreateView$2(UrlInputFragment urlInputFragment) {
        super(0, urlInputFragment);
    }

    public final String getName() {
        return "onCommit";
    }

    public final KDeclarationContainer getOwner() {
        return Reflection.getOrCreateKotlinClass(UrlInputFragment.class);
    }

    public final String getSignature() {
        return "onCommit()V";
    }

    public final void invoke() {
        ((UrlInputFragment) this.receiver).onCommit();
    }
}
