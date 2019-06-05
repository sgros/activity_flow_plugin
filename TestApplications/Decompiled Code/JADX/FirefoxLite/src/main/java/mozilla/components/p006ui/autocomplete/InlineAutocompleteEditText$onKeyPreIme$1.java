package mozilla.components.p006ui.autocomplete;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import mozilla.components.p006ui.autocomplete.InlineAutocompleteEditText.Companion;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$onKeyPreIme$1 */
final class InlineAutocompleteEditText$onKeyPreIme$1 extends Lambda implements Function3<View, Integer, KeyEvent, Boolean> {
    final /* synthetic */ InlineAutocompleteEditText this$0;

    InlineAutocompleteEditText$onKeyPreIme$1(InlineAutocompleteEditText inlineAutocompleteEditText) {
        this.this$0 = inlineAutocompleteEditText;
        super(3);
    }

    public final boolean invoke(View view, int i, KeyEvent keyEvent) {
        Intrinsics.checkParameterIsNotNull(view, "<anonymous parameter 0>");
        Intrinsics.checkParameterIsNotNull(keyEvent, "event");
        if (keyEvent.getAction() != 0) {
            return false;
        }
        if (i == 66) {
            Editable text = this.this$0.getText();
            Companion companion = InlineAutocompleteEditText.Companion;
            Intrinsics.checkExpressionValueIsNotNull(text, "content");
            if (!companion.hasCompositionString(text)) {
                Function0 access$getCommitListener$p = this.this$0.commitListener;
                if (access$getCommitListener$p != null) {
                    Unit unit = (Unit) access$getCommitListener$p.invoke();
                }
                return true;
            }
        }
        if (i != 4) {
            return false;
        }
        InlineAutocompleteEditText inlineAutocompleteEditText = this.this$0;
        Editable text2 = this.this$0.getText();
        Intrinsics.checkExpressionValueIsNotNull(text2, "text");
        inlineAutocompleteEditText.removeAutocomplete(text2);
        return false;
    }
}
