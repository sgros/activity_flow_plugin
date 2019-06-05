package mozilla.components.p006ui.autocomplete;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditTextKt$sam$android_view_View_OnKeyListener$0 */
final class C0420xb13fbaa8 implements OnKeyListener {
    private final /* synthetic */ Function3 function;

    C0420xb13fbaa8(Function3 function3) {
        this.function = function3;
    }

    public final /* synthetic */ boolean onKey(View view, int i, KeyEvent keyEvent) {
        Object invoke = this.function.invoke(view, Integer.valueOf(i), keyEvent);
        Intrinsics.checkExpressionValueIsNotNull(invoke, "invoke(...)");
        return ((Boolean) invoke).booleanValue();
    }
}
