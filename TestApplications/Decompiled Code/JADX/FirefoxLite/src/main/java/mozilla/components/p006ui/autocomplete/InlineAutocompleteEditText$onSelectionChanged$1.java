package mozilla.components.p006ui.autocomplete;

import android.text.Editable;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$onSelectionChanged$1 */
final class InlineAutocompleteEditText$onSelectionChanged$1 extends Lambda implements Function2<Integer, Integer, Unit> {
    final /* synthetic */ InlineAutocompleteEditText this$0;

    InlineAutocompleteEditText$onSelectionChanged$1(InlineAutocompleteEditText inlineAutocompleteEditText) {
        this.this$0 = inlineAutocompleteEditText;
        super(2);
    }

    public final void invoke(int i, int i2) {
        Editable text = this.this$0.getText();
        int spanStart = text.getSpanStart(InlineAutocompleteEditText.Companion.getAUTOCOMPLETE_SPAN());
        if (!this.this$0.settingAutoComplete && spanStart >= 0 && (spanStart != i || spanStart != i2)) {
            InlineAutocompleteEditText inlineAutocompleteEditText;
            if (i > spanStart || i2 > spanStart) {
                inlineAutocompleteEditText = this.this$0;
                Intrinsics.checkExpressionValueIsNotNull(text, "text");
                inlineAutocompleteEditText.commitAutocomplete(text);
            } else {
                inlineAutocompleteEditText = this.this$0;
                Intrinsics.checkExpressionValueIsNotNull(text, "text");
                inlineAutocompleteEditText.removeAutocomplete(text);
            }
        }
    }
}
