package mozilla.components.p006ui.autocomplete;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.p004v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.NoCopySpan.Concrete;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: InlineAutocompleteEditText.kt */
/* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText */
public class InlineAutocompleteEditText extends AppCompatEditText {
    private static final Concrete AUTOCOMPLETE_SPAN = new Concrete();
    public static final Companion Companion = new Companion();
    private static final int DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR = Color.parseColor("#ffb5007f");
    private final int autoCompleteBackgroundColor;
    private int autoCompletePrefixLength;
    private Object[] autoCompleteSpans;
    private AutocompleteResult autocompleteResult;
    private Function0<Unit> commitListener;
    private final Context ctx;
    private boolean discardAutoCompleteResult;
    private Function2<? super String, ? super InlineAutocompleteEditText, Unit> filterListener;
    private Function3<? super View, ? super Integer, ? super KeyEvent, Boolean> keyPreImeListener;
    private final Function3<View, Integer, KeyEvent, Boolean> onKey;
    private final Function3<View, Integer, KeyEvent, Boolean> onKeyPreIme;
    private final Function2<Integer, Integer, Unit> onSelectionChanged;
    private Function1<? super Boolean, Unit> searchStateChangeListener;
    private Function2<? super Integer, ? super Integer, Unit> selectionChangedListener;
    private boolean settingAutoComplete;
    private Function2<? super String, ? super String, Unit> textChangeListener;
    private Function1<? super Boolean, Unit> windowFocusChangeListener;

    /* compiled from: InlineAutocompleteEditText.kt */
    /* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$AutocompleteResult */
    public static final class AutocompleteResult {
        public static final Companion Companion = new Companion();
        private final boolean isEmpty;
        private final int length;
        private final String source;
        private final String text;
        private final Function1<String, String> textFormatter;
        private final int totalItems;

        /* compiled from: InlineAutocompleteEditText.kt */
        /* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$AutocompleteResult$Companion */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public final AutocompleteResult emptyResult() {
                return new AutocompleteResult("", "", 0, null, 8, null);
            }
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof AutocompleteResult) {
                    AutocompleteResult autocompleteResult = (AutocompleteResult) obj;
                    if (Intrinsics.areEqual(this.text, autocompleteResult.text) && Intrinsics.areEqual(this.source, autocompleteResult.source)) {
                        if ((this.totalItems == autocompleteResult.totalItems ? 1 : null) == null || !Intrinsics.areEqual(this.textFormatter, autocompleteResult.textFormatter)) {
                            return false;
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            String str = this.text;
            int i = 0;
            int hashCode = (str != null ? str.hashCode() : 0) * 31;
            String str2 = this.source;
            hashCode = (((hashCode + (str2 != null ? str2.hashCode() : 0)) * 31) + this.totalItems) * 31;
            Function1 function1 = this.textFormatter;
            if (function1 != null) {
                i = function1.hashCode();
            }
            return hashCode + i;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AutocompleteResult(text=");
            stringBuilder.append(this.text);
            stringBuilder.append(", source=");
            stringBuilder.append(this.source);
            stringBuilder.append(", totalItems=");
            stringBuilder.append(this.totalItems);
            stringBuilder.append(", textFormatter=");
            stringBuilder.append(this.textFormatter);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        public AutocompleteResult(String str, String str2, int i, Function1<? super String, String> function1) {
            Intrinsics.checkParameterIsNotNull(str, "text");
            Intrinsics.checkParameterIsNotNull(str2, "source");
            this.text = str;
            this.source = str2;
            this.totalItems = i;
            this.textFormatter = function1;
            this.isEmpty = ((CharSequence) this.text).length() == 0;
            this.length = this.text.length();
        }

        public final String getText() {
            return this.text;
        }

        public /* synthetic */ AutocompleteResult(String str, String str2, int i, Function1 function1, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            if ((i2 & 8) != 0) {
                function1 = (Function1) null;
            }
            this(str, str2, i, function1);
        }

        public final boolean isEmpty() {
            return this.isEmpty;
        }

        public final int getLength() {
            return this.length;
        }

        public final String getFormattedText() {
            Function1 function1 = this.textFormatter;
            if (function1 != null) {
                String str = (String) function1.invoke(this.text);
                if (str != null) {
                    return str;
                }
            }
            return this.text;
        }

        public final boolean startsWith(String str) {
            Intrinsics.checkParameterIsNotNull(str, "text");
            return StringsKt__StringsJVMKt.startsWith$default(this.text, str, false, 2, null);
        }
    }

    /* compiled from: InlineAutocompleteEditText.kt */
    /* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$Companion */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Concrete getAUTOCOMPLETE_SPAN() {
            return InlineAutocompleteEditText.AUTOCOMPLETE_SPAN;
        }

        public final int getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR() {
            return InlineAutocompleteEditText.DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR;
        }

        private final String getNonAutocompleteText(Editable editable) {
            int spanStart = editable.getSpanStart(getAUTOCOMPLETE_SPAN());
            if (spanStart < 0) {
                return editable.toString();
            }
            String substring = TextUtils.substring(editable, 0, spanStart);
            Intrinsics.checkExpressionValueIsNotNull(substring, "TextUtils.substring(text, 0, start)");
            return substring;
        }

        private final boolean hasCompositionString(Editable editable) {
            Object[] spans = editable.getSpans(0, editable.length(), Object.class);
            Intrinsics.checkExpressionValueIsNotNull(spans, "spans");
            for (Object spanFlags : spans) {
                if (((editable.getSpanFlags(spanFlags) & 256) != 0 ? 1 : null) != null) {
                    return true;
                }
            }
            return false;
        }
    }

    /* compiled from: InlineAutocompleteEditText.kt */
    /* renamed from: mozilla.components.ui.autocomplete.InlineAutocompleteEditText$TextChangeListener */
    private final class TextChangeListener implements TextWatcher {
        private int textLengthBeforeChange;

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "s");
        }

        public void afterTextChanged(Editable editable) {
            Intrinsics.checkParameterIsNotNull(editable, "editable");
            if (InlineAutocompleteEditText.this.isEnabled() && !InlineAutocompleteEditText.this.settingAutoComplete) {
                int i;
                Unit unit;
                String access$getNonAutocompleteText = InlineAutocompleteEditText.Companion.getNonAutocompleteText(editable);
                int length = access$getNonAutocompleteText.length();
                Object obj = null;
                boolean z = true;
                if (StringsKt__StringsKt.contains$default(access$getNonAutocompleteText, " ", false, 2, null) || length == this.textLengthBeforeChange - 1 || length == 0) {
                    i = 0;
                } else {
                    i = 1;
                }
                InlineAutocompleteEditText.this.autoCompletePrefixLength = length;
                InlineAutocompleteEditText.this.discardAutoCompleteResult = i ^ 1;
                if (i == 0 || !InlineAutocompleteEditText.this.getAutocompleteResult().startsWith(access$getNonAutocompleteText)) {
                    InlineAutocompleteEditText.this.removeAutocomplete(editable);
                } else {
                    InlineAutocompleteEditText.this.applyAutocompleteResult(InlineAutocompleteEditText.this.getAutocompleteResult());
                    i = 0;
                }
                Function1 access$getSearchStateChangeListener$p = InlineAutocompleteEditText.this.searchStateChangeListener;
                if (access$getSearchStateChangeListener$p != null) {
                    if (length <= 0) {
                        z = false;
                    }
                    unit = (Unit) access$getSearchStateChangeListener$p.invoke(Boolean.valueOf(z));
                }
                Function2 access$getFilterListener$p = InlineAutocompleteEditText.this.filterListener;
                if (access$getFilterListener$p != null) {
                    if (i != 0) {
                        obj = InlineAutocompleteEditText.this;
                    }
                    unit = (Unit) access$getFilterListener$p.invoke(access$getNonAutocompleteText, obj);
                }
                access$getFilterListener$p = InlineAutocompleteEditText.this.textChangeListener;
                if (access$getFilterListener$p != null) {
                    unit = (Unit) access$getFilterListener$p.invoke(access$getNonAutocompleteText, InlineAutocompleteEditText.this.getText().toString());
                }
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "s");
            this.textLengthBeforeChange = charSequence.length();
        }
    }

    public InlineAutocompleteEditText(Context context) {
        this(context, null, 0, 6, null);
    }

    public InlineAutocompleteEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public final Context getCtx() {
        return this.ctx;
    }

    public /* synthetic */ InlineAutocompleteEditText(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 2) != 0) {
            attributeSet = (AttributeSet) null;
        }
        if ((i2 & 4) != 0) {
            i = C0421R.attr.editTextStyle;
        }
        this(context, attributeSet, i);
    }

    public InlineAutocompleteEditText(Context context, AttributeSet attributeSet, int i) {
        Intrinsics.checkParameterIsNotNull(context, "ctx");
        super(context, attributeSet, i);
        this.ctx = context;
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
        this.autoCompleteBackgroundColor = ((Number) new InlineAutocompleteEditText$autoCompleteBackgroundColor$1(this, attributeSet).invoke()).intValue();
        this.onKeyPreIme = new InlineAutocompleteEditText$onKeyPreIme$1(this);
        this.onKey = new InlineAutocompleteEditText$onKey$1(this);
        this.onSelectionChanged = new InlineAutocompleteEditText$onSelectionChanged$1(this);
    }

    public final void setOnCommitListener(Function0<Unit> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "l");
        this.commitListener = function0;
    }

    public final void setOnFilterListener(Function2<? super String, ? super InlineAutocompleteEditText, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "l");
        this.filterListener = function2;
    }

    public final void setOnSearchStateChangeListener(Function1<? super Boolean, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "l");
        this.searchStateChangeListener = function1;
    }

    public final void setOnTextChangeListener(Function2<? super String, ? super String, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "l");
        this.textChangeListener = function2;
    }

    public final void setOnKeyPreImeListener(Function3<? super View, ? super Integer, ? super KeyEvent, Boolean> function3) {
        Intrinsics.checkParameterIsNotNull(function3, "l");
        this.keyPreImeListener = function3;
    }

    public final void setOnSelectionChangedListener(Function2<? super Integer, ? super Integer, Unit> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "l");
        this.selectionChangedListener = function2;
    }

    public final void setOnWindowsFocusChangeListener(Function1<? super Boolean, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "l");
        this.windowFocusChangeListener = function1;
    }

    private final void setAutocompleteResult(AutocompleteResult autocompleteResult) {
        this.autocompleteResult = autocompleteResult;
    }

    public final AutocompleteResult getAutocompleteResult() {
        return this.autocompleteResult;
    }

    public final String getNonAutocompleteText() {
        Companion companion = Companion;
        Editable text = getText();
        Intrinsics.checkExpressionValueIsNotNull(text, "text");
        return companion.getNonAutocompleteText(text);
    }

    public final String getOriginalText() {
        return getText().subSequence(0, this.autoCompletePrefixLength).toString();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.keyPreImeListener = this.onKeyPreIme;
        this.selectionChangedListener = this.onSelectionChanged;
        Function3 function3 = this.onKey;
        if (function3 != null) {
            function3 = new C0420xb13fbaa8(function3);
        }
        setOnKeyListener((OnKeyListener) function3);
        addTextChangedListener(new TextChangeListener());
    }

    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        i = TextUtils.isEmpty(getText()) ^ 1;
        Function1 function1 = this.searchStateChangeListener;
        if (function1 != null) {
            Unit unit = (Unit) function1.invoke(Boolean.valueOf(i));
        }
        if (z) {
            resetAutocompleteState();
            return;
        }
        Editable text = getText();
        Intrinsics.checkExpressionValueIsNotNull(text, "text");
        removeAutocomplete(text);
        Object systemService = this.ctx.getSystemService("input_method");
        if (systemService != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) systemService;
            try {
                inputMethodManager.restartInput(this);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } catch (NullPointerException unused) {
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
    }

    /* JADX WARNING: Missing block: B:3:0x000b, code skipped:
            if (r2 != null) goto L_0x0010;
     */
    public void setText(java.lang.CharSequence r2, android.widget.TextView.BufferType r3) {
        /*
        r1 = this;
        r0 = "type";
        kotlin.jvm.internal.Intrinsics.checkParameterIsNotNull(r3, r0);
        if (r2 == 0) goto L_0x000e;
    L_0x0007:
        r2 = r2.toString();
        if (r2 == 0) goto L_0x000e;
    L_0x000d:
        goto L_0x0010;
    L_0x000e:
        r2 = "";
    L_0x0010:
        r2 = (java.lang.CharSequence) r2;
        super.setText(r2, r3);
        r1.resetAutocompleteState();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: mozilla.components.p006ui.autocomplete.InlineAutocompleteEditText.setText(java.lang.CharSequence, android.widget.TextView$BufferType):void");
    }

    public void sendAccessibilityEventUnchecked(AccessibilityEvent accessibilityEvent) {
        Intrinsics.checkParameterIsNotNull(accessibilityEvent, "event");
        if (accessibilityEvent.getEventType() != 8192 || getParent() == null || isShown()) {
            super.sendAccessibilityEventUnchecked(accessibilityEvent);
            return;
        }
        onInitializeAccessibilityEvent(accessibilityEvent);
        dispatchPopulateAccessibilityEvent(accessibilityEvent);
        getParent().requestSendAccessibilityEvent(this, accessibilityEvent);
    }

    private final void beginSettingAutocomplete() {
        beginBatchEdit();
        this.settingAutoComplete = true;
    }

    private final void endSettingAutocomplete() {
        this.settingAutoComplete = false;
        endBatchEdit();
    }

    private final void resetAutocompleteState() {
        this.autoCompleteSpans = new Object[]{AUTOCOMPLETE_SPAN, new BackgroundColorSpan(this.autoCompleteBackgroundColor)};
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
        this.autoCompletePrefixLength = getText().length();
        setCursorVisible(true);
    }

    private final boolean removeAutocomplete(Editable editable) {
        int spanStart = editable.getSpanStart(AUTOCOMPLETE_SPAN);
        if (spanStart < 0) {
            return false;
        }
        beginSettingAutocomplete();
        editable.delete(spanStart, editable.length());
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
        setCursorVisible(true);
        endSettingAutocomplete();
        return true;
    }

    private final boolean commitAutocomplete(Editable editable) {
        int i = 0;
        if (editable.getSpanStart(AUTOCOMPLETE_SPAN) < 0) {
            return false;
        }
        beginSettingAutocomplete();
        Object[] objArr = this.autoCompleteSpans;
        if (objArr == null) {
            Intrinsics.throwNpe();
        }
        int length = objArr.length;
        while (i < length) {
            editable.removeSpan(objArr[i]);
            i++;
        }
        this.autoCompletePrefixLength = editable.length();
        setCursorVisible(true);
        endSettingAutocomplete();
        Function2 function2 = this.filterListener;
        if (function2 != null) {
            Unit unit = (Unit) function2.invoke(editable.toString(), null);
        }
        return true;
    }

    public final void applyAutocompleteResult(AutocompleteResult autocompleteResult) {
        Intrinsics.checkParameterIsNotNull(autocompleteResult, "result");
        if (!this.discardAutoCompleteResult) {
            if (!isEnabled() || autocompleteResult.isEmpty()) {
                this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
                return;
            }
            Editable text = getText();
            int length = text.length();
            int length2 = autocompleteResult.getLength();
            int spanStart = text.getSpanStart(AUTOCOMPLETE_SPAN);
            this.autocompleteResult = autocompleteResult;
            if (spanStart > -1) {
                if (TextUtils.regionMatches(autocompleteResult.getText(), 0, text, 0, spanStart)) {
                    beginSettingAutocomplete();
                    text.replace(spanStart, length, autocompleteResult.getText(), spanStart, length2);
                    if (spanStart == length2) {
                        setCursorVisible(true);
                    }
                    endSettingAutocomplete();
                } else {
                    return;
                }
            } else if (length2 > length && TextUtils.regionMatches(autocompleteResult.getText(), 0, text, 0, length)) {
                int i;
                Object[] spans = text.getSpans(length, length, Object.class);
                int[] iArr = new int[spans.length];
                int[] iArr2 = new int[spans.length];
                int[] iArr3 = new int[spans.length];
                Intrinsics.checkExpressionValueIsNotNull(spans, "spans");
                spanStart = spans.length;
                for (i = 0; i < spanStart; i++) {
                    Object obj = spans[i];
                    int spanFlags = text.getSpanFlags(obj);
                    if ((spanFlags & 256) != 0 || obj == Selection.SELECTION_START || obj == Selection.SELECTION_END) {
                        iArr[i] = text.getSpanStart(obj);
                        iArr2[i] = text.getSpanEnd(obj);
                        iArr3[i] = spanFlags;
                    }
                }
                beginSettingAutocomplete();
                text.append(autocompleteResult.getText(), length, length2);
                int length3 = spans.length;
                for (spanStart = 0; spanStart < length3; spanStart++) {
                    i = iArr3[spanStart];
                    if (i != 0) {
                        text.setSpan(spans[spanStart], iArr[spanStart], iArr2[spanStart], i);
                    }
                }
                Object[] objArr = this.autoCompleteSpans;
                if (objArr == null) {
                    Intrinsics.throwNpe();
                }
                for (Object span : objArr) {
                    text.setSpan(span, length, length2, 33);
                }
                setCursorVisible(false);
                bringPointIntoView(length2);
                endSettingAutocomplete();
            } else {
                return;
            }
            announceForAccessibility(text.toString());
        }
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        Intrinsics.checkParameterIsNotNull(editorInfo, "outAttrs");
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        return onCreateInputConnection != null ? new InlineAutocompleteEditText$onCreateInputConnection$1(this, onCreateInputConnection, onCreateInputConnection, false) : null;
    }

    public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
        Intrinsics.checkParameterIsNotNull(keyEvent, "event");
        Function3 function3 = this.keyPreImeListener;
        if (function3 != null) {
            Boolean bool = (Boolean) function3.invoke(this, Integer.valueOf(i), keyEvent);
            if (bool != null) {
                return bool.booleanValue();
            }
        }
        return false;
    }

    public void onSelectionChanged(int i, int i2) {
        Function2 function2 = this.selectionChangedListener;
        if (function2 != null) {
            Unit unit = (Unit) function2.invoke(Integer.valueOf(i), Integer.valueOf(i2));
        }
        super.onSelectionChanged(i, i2);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Function1 function1 = this.windowFocusChangeListener;
        if (function1 != null) {
            Unit unit = (Unit) function1.invoke(Boolean.valueOf(z));
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkParameterIsNotNull(motionEvent, "event");
        if (VERSION.SDK_INT != 23 || motionEvent.getActionMasked() != 1) {
            return super.onTouchEvent(motionEvent);
        }
        try {
            return super.onTouchEvent(motionEvent);
        } catch (NullPointerException unused) {
            clearFocus();
            return true;
        }
    }
}
