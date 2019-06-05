package mozilla.components.p006ui.autocomplete;

import android.text.Editable;
import android.text.Spannable;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$onCreateInputConnection$1 */
public final class InlineAutocompleteEditText$onCreateInputConnection$1 extends InputConnectionWrapper {
    final /* synthetic */ InputConnection $ic;
    final /* synthetic */ InlineAutocompleteEditText this$0;

    InlineAutocompleteEditText$onCreateInputConnection$1(InlineAutocompleteEditText inlineAutocompleteEditText, InputConnection inputConnection, InputConnection inputConnection2, boolean z) {
        this.this$0 = inlineAutocompleteEditText;
        this.$ic = inputConnection;
        super(inputConnection2, z);
    }

    public boolean deleteSurroundingText(int i, int i2) {
        InlineAutocompleteEditText inlineAutocompleteEditText = this.this$0;
        Editable text = this.this$0.getText();
        Intrinsics.checkExpressionValueIsNotNull(text, "text");
        if (inlineAutocompleteEditText.removeAutocomplete(text)) {
            return false;
        }
        return super.deleteSurroundingText(i, i2);
    }

    private final boolean removeAutocompleteOnComposing(CharSequence charSequence) {
        Editable text = this.this$0.getText();
        Spannable spannable = text;
        int composingSpanStart = BaseInputConnection.getComposingSpanStart(spannable);
        int composingSpanEnd = BaseInputConnection.getComposingSpanEnd(spannable);
        if (composingSpanStart >= 0 && composingSpanEnd >= 0 && composingSpanEnd - composingSpanStart > charSequence.length()) {
            InlineAutocompleteEditText inlineAutocompleteEditText = this.this$0;
            Intrinsics.checkExpressionValueIsNotNull(text, "editable");
            if (inlineAutocompleteEditText.removeAutocomplete(text)) {
                finishComposingText();
                return true;
            }
        }
        return false;
    }

    public boolean commitText(CharSequence charSequence, int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "text");
        if (removeAutocompleteOnComposing(charSequence)) {
            return false;
        }
        return super.commitText(charSequence, i);
    }

    public boolean setComposingText(CharSequence charSequence, int i) {
        Intrinsics.checkParameterIsNotNull(charSequence, "text");
        if (removeAutocompleteOnComposing(charSequence)) {
            return false;
        }
        return super.setComposingText(charSequence, i);
    }
}
