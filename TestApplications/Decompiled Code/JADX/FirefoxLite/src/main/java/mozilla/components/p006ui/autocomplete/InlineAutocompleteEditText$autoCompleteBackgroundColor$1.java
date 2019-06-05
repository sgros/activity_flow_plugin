package mozilla.components.p006ui.autocomplete;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$autoCompleteBackgroundColor$1 */
final class InlineAutocompleteEditText$autoCompleteBackgroundColor$1 extends Lambda implements Function0<Integer> {
    final /* synthetic */ AttributeSet $attrs;
    final /* synthetic */ InlineAutocompleteEditText this$0;

    InlineAutocompleteEditText$autoCompleteBackgroundColor$1(InlineAutocompleteEditText inlineAutocompleteEditText, AttributeSet attributeSet) {
        this.this$0 = inlineAutocompleteEditText;
        this.$attrs = attributeSet;
        super(0);
    }

    public final int invoke() {
        TypedArray obtainStyledAttributes = this.this$0.getContext().obtainStyledAttributes(this.$attrs, C0421R.styleable.InlineAutocompleteEditText);
        int color = obtainStyledAttributes.getColor(C0421R.styleable.InlineAutocompleteEditText_autocompleteBackgroundColor, InlineAutocompleteEditText.Companion.getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR());
        obtainStyledAttributes.recycle();
        return color;
    }
}
