package mozilla.components.p006ui.autocomplete;

import android.view.KeyEvent;
import android.view.View;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Lambda;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$onKey$1 */
final class InlineAutocompleteEditText$onKey$1 extends Lambda implements Function3<View, Integer, KeyEvent, Boolean> {
    final /* synthetic */ InlineAutocompleteEditText this$0;

    InlineAutocompleteEditText$onKey$1(InlineAutocompleteEditText inlineAutocompleteEditText) {
        this.this$0 = inlineAutocompleteEditText;
        super(3);
    }

    /* JADX WARNING: Missing block: B:14:0x003e, code skipped:
            if (r3.removeAutocomplete(r4) != false) goto L_0x0042;
     */
    public final boolean invoke(android.view.View r2, int r3, android.view.KeyEvent r4) {
        /*
        r1 = this;
        r0 = "<anonymous parameter 0>";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r2, r0);
        r2 = "event";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r4, r2);
        r2 = 1;
        r0 = 66;
        if (r3 != r0) goto L_0x0025;
    L_0x000f:
        r3 = r4.getAction();
        if (r3 == 0) goto L_0x0016;
    L_0x0015:
        return r2;
    L_0x0016:
        r3 = r1.this$0;
        r3 = r3.commitListener;
        if (r3 == 0) goto L_0x0024;
    L_0x001e:
        r3 = r3.invoke();
        r3 = (kotlin.Unit) r3;
    L_0x0024:
        return r2;
    L_0x0025:
        r4 = 67;
        if (r3 == r4) goto L_0x002d;
    L_0x0029:
        r4 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        if (r3 != r4) goto L_0x0041;
    L_0x002d:
        r3 = r1.this$0;
        r4 = r1.this$0;
        r4 = r4.getText();
        r0 = "text";
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r4, r0);
        r3 = r3.removeAutocomplete(r4);
        if (r3 == 0) goto L_0x0041;
    L_0x0040:
        goto L_0x0042;
    L_0x0041:
        r2 = 0;
    L_0x0042:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: mozilla.components.p006ui.autocomplete.InlineAutocompleteEditText$onKey$1.invoke(android.view.View, int, android.view.KeyEvent):boolean");
    }
}
