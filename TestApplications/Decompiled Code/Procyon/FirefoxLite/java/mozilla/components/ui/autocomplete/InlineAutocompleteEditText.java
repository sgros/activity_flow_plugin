// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.ui.autocomplete;

import android.widget.TextView$BufferType;
import android.view.accessibility.AccessibilityEvent;
import android.os.Build$VERSION;
import android.view.MotionEvent;
import kotlin.TypeCastException;
import android.view.inputmethod.InputMethodManager;
import android.graphics.Rect;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.text.TextWatcher;
import android.view.View$OnKeyListener;
import android.text.Selection;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.Editable;
import kotlin.jvm.internal.Intrinsics;
import android.util.AttributeSet;
import android.graphics.Color;
import kotlin.jvm.functions.Function1;
import android.view.KeyEvent;
import android.view.View;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import android.content.Context;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import android.text.NoCopySpan$Concrete;
import android.support.v7.widget.AppCompatEditText;

public class InlineAutocompleteEditText extends AppCompatEditText
{
    private static final NoCopySpan$Concrete AUTOCOMPLETE_SPAN;
    public static final Companion Companion;
    private static final int DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR;
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
    
    static {
        Companion = new Companion(null);
        AUTOCOMPLETE_SPAN = new NoCopySpan$Concrete();
        DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR = Color.parseColor("#ffb5007f");
    }
    
    public InlineAutocompleteEditText(final Context context) {
        this(context, null, 0, 6, null);
    }
    
    public InlineAutocompleteEditText(final Context context, final AttributeSet set) {
        this(context, set, 0, 4, null);
    }
    
    public InlineAutocompleteEditText(final Context ctx, final AttributeSet set, final int n) {
        Intrinsics.checkParameterIsNotNull(ctx, "ctx");
        super(ctx, set, n);
        this.ctx = ctx;
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
        this.autoCompleteBackgroundColor = ((Function0<Number>)new InlineAutocompleteEditText$autoCompleteBackgroundColor.InlineAutocompleteEditText$autoCompleteBackgroundColor$1(this, set)).invoke().intValue();
        this.onKeyPreIme = (Function3<View, Integer, KeyEvent, Boolean>)new InlineAutocompleteEditText$onKeyPreIme.InlineAutocompleteEditText$onKeyPreIme$1(this);
        this.onKey = (Function3<View, Integer, KeyEvent, Boolean>)new InlineAutocompleteEditText$onKey.InlineAutocompleteEditText$onKey$1(this);
        this.onSelectionChanged = (Function2<Integer, Integer, Unit>)new InlineAutocompleteEditText$onSelectionChanged.InlineAutocompleteEditText$onSelectionChanged$1(this);
    }
    
    public static final /* synthetic */ NoCopySpan$Concrete access$getAUTOCOMPLETE_SPAN$cp() {
        return InlineAutocompleteEditText.AUTOCOMPLETE_SPAN;
    }
    
    public static final /* synthetic */ int access$getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR$cp() {
        return InlineAutocompleteEditText.DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR;
    }
    
    public static final /* synthetic */ Function2 access$getFilterListener$p(final InlineAutocompleteEditText inlineAutocompleteEditText) {
        return inlineAutocompleteEditText.filterListener;
    }
    
    public static final /* synthetic */ Function1 access$getSearchStateChangeListener$p(final InlineAutocompleteEditText inlineAutocompleteEditText) {
        return inlineAutocompleteEditText.searchStateChangeListener;
    }
    
    public static final /* synthetic */ boolean access$getSettingAutoComplete$p(final InlineAutocompleteEditText inlineAutocompleteEditText) {
        return inlineAutocompleteEditText.settingAutoComplete;
    }
    
    public static final /* synthetic */ Function2 access$getTextChangeListener$p(final InlineAutocompleteEditText inlineAutocompleteEditText) {
        return inlineAutocompleteEditText.textChangeListener;
    }
    
    public static final /* synthetic */ void access$setAutoCompletePrefixLength$p(final InlineAutocompleteEditText inlineAutocompleteEditText, final int autoCompletePrefixLength) {
        inlineAutocompleteEditText.autoCompletePrefixLength = autoCompletePrefixLength;
    }
    
    public static final /* synthetic */ void access$setDiscardAutoCompleteResult$p(final InlineAutocompleteEditText inlineAutocompleteEditText, final boolean discardAutoCompleteResult) {
        inlineAutocompleteEditText.discardAutoCompleteResult = discardAutoCompleteResult;
    }
    
    private final void beginSettingAutocomplete() {
        this.beginBatchEdit();
        this.settingAutoComplete = true;
    }
    
    private final boolean commitAutocomplete(final Editable editable) {
        final int spanStart = editable.getSpanStart((Object)InlineAutocompleteEditText.AUTOCOMPLETE_SPAN);
        int i = 0;
        if (spanStart < 0) {
            return false;
        }
        this.beginSettingAutocomplete();
        final Object[] autoCompleteSpans = this.autoCompleteSpans;
        if (autoCompleteSpans == null) {
            Intrinsics.throwNpe();
        }
        while (i < autoCompleteSpans.length) {
            editable.removeSpan(autoCompleteSpans[i]);
            ++i;
        }
        this.autoCompletePrefixLength = editable.length();
        this.setCursorVisible(true);
        this.endSettingAutocomplete();
        final Function2<? super String, ? super InlineAutocompleteEditText, Unit> filterListener = this.filterListener;
        if (filterListener != null) {
            final Unit unit = filterListener.invoke(editable.toString(), null);
        }
        return true;
    }
    
    private final void endSettingAutocomplete() {
        this.settingAutoComplete = false;
        this.endBatchEdit();
    }
    
    private final boolean removeAutocomplete(final Editable editable) {
        final int spanStart = editable.getSpanStart((Object)InlineAutocompleteEditText.AUTOCOMPLETE_SPAN);
        if (spanStart < 0) {
            return false;
        }
        this.beginSettingAutocomplete();
        editable.delete(spanStart, editable.length());
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
        this.setCursorVisible(true);
        this.endSettingAutocomplete();
        return true;
    }
    
    private final void resetAutocompleteState() {
        this.autoCompleteSpans = new Object[] { InlineAutocompleteEditText.AUTOCOMPLETE_SPAN, new BackgroundColorSpan(this.autoCompleteBackgroundColor) };
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
        this.autoCompletePrefixLength = this.getText().length();
        this.setCursorVisible(true);
    }
    
    private final void setAutocompleteResult(final AutocompleteResult autocompleteResult) {
        this.autocompleteResult = autocompleteResult;
    }
    
    public final void applyAutocompleteResult(final AutocompleteResult autocompleteResult) {
        Intrinsics.checkParameterIsNotNull(autocompleteResult, "result");
        if (this.discardAutoCompleteResult) {
            return;
        }
        if (this.isEnabled() && !autocompleteResult.isEmpty()) {
            final Editable text = this.getText();
            final int length = text.length();
            final int length2 = autocompleteResult.getLength();
            final int spanStart = text.getSpanStart((Object)InlineAutocompleteEditText.AUTOCOMPLETE_SPAN);
            this.autocompleteResult = autocompleteResult;
            if (spanStart > -1) {
                if (!TextUtils.regionMatches((CharSequence)autocompleteResult.getText(), 0, (CharSequence)text, 0, spanStart)) {
                    return;
                }
                this.beginSettingAutocomplete();
                text.replace(spanStart, length, (CharSequence)autocompleteResult.getText(), spanStart, length2);
                if (spanStart == length2) {
                    this.setCursorVisible(true);
                }
                this.endSettingAutocomplete();
            }
            else {
                if (length2 <= length || !TextUtils.regionMatches((CharSequence)autocompleteResult.getText(), 0, (CharSequence)text, 0, length)) {
                    return;
                }
                final Object[] spans = text.getSpans(length, length, (Class)Object.class);
                final int[] array = new int[spans.length];
                final int[] array2 = new int[spans.length];
                final int[] array3 = new int[spans.length];
                Intrinsics.checkExpressionValueIsNotNull(spans, "spans");
                for (int length3 = spans.length, i = 0; i < length3; ++i) {
                    final Object o = spans[i];
                    final int spanFlags = text.getSpanFlags(o);
                    if ((spanFlags & 0x100) != 0x0 || o == Selection.SELECTION_START || o == Selection.SELECTION_END) {
                        array[i] = text.getSpanStart(o);
                        array2[i] = text.getSpanEnd(o);
                        array3[i] = spanFlags;
                    }
                }
                this.beginSettingAutocomplete();
                text.append((CharSequence)autocompleteResult.getText(), length, length2);
                for (int length4 = spans.length, j = 0; j < length4; ++j) {
                    final int n = array3[j];
                    if (n != 0) {
                        text.setSpan(spans[j], array[j], array2[j], n);
                    }
                }
                final Object[] autoCompleteSpans = this.autoCompleteSpans;
                if (autoCompleteSpans == null) {
                    Intrinsics.throwNpe();
                }
                for (int length5 = autoCompleteSpans.length, k = 0; k < length5; ++k) {
                    text.setSpan(autoCompleteSpans[k], length, length2, 33);
                }
                this.setCursorVisible(false);
                this.bringPointIntoView(length2);
                this.endSettingAutocomplete();
            }
            this.announceForAccessibility((CharSequence)text.toString());
            return;
        }
        this.autocompleteResult = AutocompleteResult.Companion.emptyResult();
    }
    
    public final AutocompleteResult getAutocompleteResult() {
        return this.autocompleteResult;
    }
    
    public final Context getCtx() {
        return this.ctx;
    }
    
    public final String getNonAutocompleteText() {
        final Companion companion = InlineAutocompleteEditText.Companion;
        final Editable text = this.getText();
        Intrinsics.checkExpressionValueIsNotNull(text, "text");
        return companion.getNonAutocompleteText(text);
    }
    
    public final String getOriginalText() {
        return this.getText().subSequence(0, this.autoCompletePrefixLength).toString();
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.keyPreImeListener = this.onKeyPreIme;
        this.selectionChangedListener = this.onSelectionChanged;
        Object onKey;
        final Function3<View, Integer, KeyEvent, Boolean> function3 = (Function3<View, Integer, KeyEvent, Boolean>)(onKey = this.onKey);
        if (function3 != null) {
            onKey = new InlineAutocompleteEditTextKt$sam$android_view_View_OnKeyListener$0(function3);
        }
        this.setOnKeyListener((View$OnKeyListener)onKey);
        this.addTextChangedListener((TextWatcher)new TextChangeListener());
    }
    
    @Override
    public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
        Intrinsics.checkParameterIsNotNull(editorInfo, "outAttrs");
        final InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        if (onCreateInputConnection != null) {
            return (InputConnection)new InlineAutocompleteEditText$onCreateInputConnection.InlineAutocompleteEditText$onCreateInputConnection$1(this, onCreateInputConnection, onCreateInputConnection, false);
        }
        return null;
    }
    
    public void onFocusChanged(final boolean b, final int n, final Rect rect) {
        super.onFocusChanged(b, n, rect);
        final boolean empty = TextUtils.isEmpty((CharSequence)this.getText());
        final Function1<? super Boolean, Unit> searchStateChangeListener = this.searchStateChangeListener;
        if (searchStateChangeListener != null) {
            final Unit unit = searchStateChangeListener.invoke(empty ^ true);
        }
        if (b) {
            this.resetAutocompleteState();
            return;
        }
        final Editable text = this.getText();
        Intrinsics.checkExpressionValueIsNotNull(text, "text");
        this.removeAutocomplete(text);
        final Object systemService = this.ctx.getSystemService("input_method");
        Label_0111: {
            if (systemService == null) {
                break Label_0111;
            }
            final InputMethodManager inputMethodManager = (InputMethodManager)systemService;
            try {
                inputMethodManager.restartInput((View)this);
                inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
                return;
                throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
            }
            catch (NullPointerException ex) {}
        }
    }
    
    public boolean onKeyPreIme(final int i, final KeyEvent keyEvent) {
        Intrinsics.checkParameterIsNotNull(keyEvent, "event");
        final Function3<? super View, ? super Integer, ? super KeyEvent, Boolean> keyPreImeListener = this.keyPreImeListener;
        if (keyPreImeListener != null) {
            final Boolean b = keyPreImeListener.invoke(this, Integer.valueOf(i), keyEvent);
            if (b != null) {
                return b;
            }
        }
        return false;
    }
    
    public void onSelectionChanged(final int i, final int j) {
        final Function2<? super Integer, ? super Integer, Unit> selectionChangedListener = this.selectionChangedListener;
        if (selectionChangedListener != null) {
            final Unit unit = selectionChangedListener.invoke(i, j);
        }
        super.onSelectionChanged(i, j);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        Intrinsics.checkParameterIsNotNull(motionEvent, "event");
        boolean b;
        if (Build$VERSION.SDK_INT == 23 && motionEvent.getActionMasked() == 1) {
            try {
                b = super.onTouchEvent(motionEvent);
            }
            catch (NullPointerException ex) {
                this.clearFocus();
                b = true;
            }
        }
        else {
            b = super.onTouchEvent(motionEvent);
        }
        return b;
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        final Function1<? super Boolean, Unit> windowFocusChangeListener = this.windowFocusChangeListener;
        if (windowFocusChangeListener != null) {
            final Unit unit = windowFocusChangeListener.invoke(b);
        }
    }
    
    public void sendAccessibilityEventUnchecked(final AccessibilityEvent accessibilityEvent) {
        Intrinsics.checkParameterIsNotNull(accessibilityEvent, "event");
        if (accessibilityEvent.getEventType() == 8192 && this.getParent() != null && !this.isShown()) {
            this.onInitializeAccessibilityEvent(accessibilityEvent);
            this.dispatchPopulateAccessibilityEvent(accessibilityEvent);
            this.getParent().requestSendAccessibilityEvent((View)this, accessibilityEvent);
        }
        else {
            super.sendAccessibilityEventUnchecked(accessibilityEvent);
        }
    }
    
    public final void setOnCommitListener(final Function0<Unit> commitListener) {
        Intrinsics.checkParameterIsNotNull(commitListener, "l");
        this.commitListener = commitListener;
    }
    
    public final void setOnFilterListener(final Function2<? super String, ? super InlineAutocompleteEditText, Unit> filterListener) {
        Intrinsics.checkParameterIsNotNull(filterListener, "l");
        this.filterListener = filterListener;
    }
    
    public final void setOnKeyPreImeListener(final Function3<? super View, ? super Integer, ? super KeyEvent, Boolean> keyPreImeListener) {
        Intrinsics.checkParameterIsNotNull(keyPreImeListener, "l");
        this.keyPreImeListener = keyPreImeListener;
    }
    
    public final void setOnSearchStateChangeListener(final Function1<? super Boolean, Unit> searchStateChangeListener) {
        Intrinsics.checkParameterIsNotNull(searchStateChangeListener, "l");
        this.searchStateChangeListener = searchStateChangeListener;
    }
    
    public final void setOnSelectionChangedListener(final Function2<? super Integer, ? super Integer, Unit> selectionChangedListener) {
        Intrinsics.checkParameterIsNotNull(selectionChangedListener, "l");
        this.selectionChangedListener = selectionChangedListener;
    }
    
    public final void setOnTextChangeListener(final Function2<? super String, ? super String, Unit> textChangeListener) {
        Intrinsics.checkParameterIsNotNull(textChangeListener, "l");
        this.textChangeListener = textChangeListener;
    }
    
    public final void setOnWindowsFocusChangeListener(final Function1<? super Boolean, Unit> windowFocusChangeListener) {
        Intrinsics.checkParameterIsNotNull(windowFocusChangeListener, "l");
        this.windowFocusChangeListener = windowFocusChangeListener;
    }
    
    public void setText(final CharSequence charSequence, final TextView$BufferType textView$BufferType) {
        Intrinsics.checkParameterIsNotNull(textView$BufferType, "type");
        String string = null;
        Label_0027: {
            if (charSequence != null) {
                string = charSequence.toString();
                if (string != null) {
                    break Label_0027;
                }
            }
            string = "";
        }
        super.setText((CharSequence)string, textView$BufferType);
        this.resetAutocompleteState();
    }
    
    public static final class AutocompleteResult
    {
        public static final Companion Companion;
        private final boolean isEmpty;
        private final int length;
        private final String source;
        private final String text;
        private final Function1<String, String> textFormatter;
        private final int totalItems;
        
        static {
            Companion = new Companion(null);
        }
        
        public AutocompleteResult(final String text, final String source, final int totalItems, final Function1<? super String, String> textFormatter) {
            Intrinsics.checkParameterIsNotNull(text, "text");
            Intrinsics.checkParameterIsNotNull(source, "source");
            this.text = text;
            this.source = source;
            this.totalItems = totalItems;
            this.textFormatter = (Function1<String, String>)textFormatter;
            this.isEmpty = (this.text.length() == 0);
            this.length = this.text.length();
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o instanceof AutocompleteResult) {
                    final AutocompleteResult autocompleteResult = (AutocompleteResult)o;
                    if (Intrinsics.areEqual(this.text, autocompleteResult.text) && Intrinsics.areEqual(this.source, autocompleteResult.source) && this.totalItems == autocompleteResult.totalItems && Intrinsics.areEqual(this.textFormatter, autocompleteResult.textFormatter)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        
        public final String getFormattedText() {
            final Function1<String, String> textFormatter = this.textFormatter;
            if (textFormatter != null) {
                final String text = textFormatter.invoke(this.text);
                if (text != null) {
                    return text;
                }
            }
            return this.text;
        }
        
        public final int getLength() {
            return this.length;
        }
        
        public final String getText() {
            return this.text;
        }
        
        @Override
        public int hashCode() {
            final String text = this.text;
            int hashCode = 0;
            int hashCode2;
            if (text != null) {
                hashCode2 = text.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            final String source = this.source;
            int hashCode3;
            if (source != null) {
                hashCode3 = source.hashCode();
            }
            else {
                hashCode3 = 0;
            }
            final int totalItems = this.totalItems;
            final Function1<String, String> textFormatter = this.textFormatter;
            if (textFormatter != null) {
                hashCode = textFormatter.hashCode();
            }
            return ((hashCode2 * 31 + hashCode3) * 31 + totalItems) * 31 + hashCode;
        }
        
        public final boolean isEmpty() {
            return this.isEmpty;
        }
        
        public final boolean startsWith(final String s) {
            Intrinsics.checkParameterIsNotNull(s, "text");
            return StringsKt__StringsJVMKt.startsWith$default(this.text, s, false, 2, null);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("AutocompleteResult(text=");
            sb.append(this.text);
            sb.append(", source=");
            sb.append(this.source);
            sb.append(", totalItems=");
            sb.append(this.totalItems);
            sb.append(", textFormatter=");
            sb.append(this.textFormatter);
            sb.append(")");
            return sb.toString();
        }
        
        public static final class Companion
        {
            private Companion() {
            }
            
            public final AutocompleteResult emptyResult() {
                return new AutocompleteResult("", "", 0, null, 8, null);
            }
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        private final String getNonAutocompleteText(final Editable editable) {
            final int spanStart = editable.getSpanStart((Object)this.getAUTOCOMPLETE_SPAN());
            String s;
            if (spanStart < 0) {
                s = editable.toString();
            }
            else {
                s = TextUtils.substring((CharSequence)editable, 0, spanStart);
                Intrinsics.checkExpressionValueIsNotNull(s, "TextUtils.substring(text, 0, start)");
            }
            return s;
        }
        
        private final boolean hasCompositionString(final Editable editable) {
            final int length = editable.length();
            final boolean b = false;
            final Object[] spans = editable.getSpans(0, length, (Class)Object.class);
            Intrinsics.checkExpressionValueIsNotNull(spans, "spans");
            final int length2 = spans.length;
            int n = 0;
            boolean b2;
            while (true) {
                b2 = b;
                if (n >= length2) {
                    break;
                }
                if ((editable.getSpanFlags(spans[n]) & 0x100) != 0x0) {
                    b2 = true;
                    break;
                }
                ++n;
            }
            return b2;
        }
        
        public final NoCopySpan$Concrete getAUTOCOMPLETE_SPAN() {
            return InlineAutocompleteEditText.access$getAUTOCOMPLETE_SPAN$cp();
        }
        
        public final int getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR() {
            return InlineAutocompleteEditText.access$getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR$cp();
        }
    }
    
    private final class TextChangeListener implements TextWatcher
    {
        private int textLengthBeforeChange;
        
        public TextChangeListener() {
        }
        
        public void afterTextChanged(final Editable editable) {
            Intrinsics.checkParameterIsNotNull(editable, "editable");
            if (InlineAutocompleteEditText.this.isEnabled() && !InlineAutocompleteEditText.access$getSettingAutoComplete$p(InlineAutocompleteEditText.this)) {
                final String access$getNonAutocompleteText = InlineAutocompleteEditText.Companion.getNonAutocompleteText(editable);
                final int length = access$getNonAutocompleteText.length();
                final String s = access$getNonAutocompleteText;
                final String s2 = " ";
                final InlineAutocompleteEditText inlineAutocompleteEditText = null;
                final boolean contains$default = StringsKt__StringsKt.contains$default(s, s2, false, 2, null);
                boolean b = true;
                int n;
                if (!contains$default && length != this.textLengthBeforeChange - 1 && length != 0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                InlineAutocompleteEditText.access$setAutoCompletePrefixLength$p(InlineAutocompleteEditText.this, length);
                InlineAutocompleteEditText.access$setDiscardAutoCompleteResult$p(InlineAutocompleteEditText.this, (boolean)((n ^ 0x1) != 0x0));
                if (n != 0 && InlineAutocompleteEditText.this.getAutocompleteResult().startsWith(access$getNonAutocompleteText)) {
                    InlineAutocompleteEditText.this.applyAutocompleteResult(InlineAutocompleteEditText.this.getAutocompleteResult());
                    n = 0;
                }
                else {
                    InlineAutocompleteEditText.this.removeAutocomplete(editable);
                }
                final Function1 access$getSearchStateChangeListener$p = InlineAutocompleteEditText.access$getSearchStateChangeListener$p(InlineAutocompleteEditText.this);
                if (access$getSearchStateChangeListener$p != null) {
                    if (length <= 0) {
                        b = false;
                    }
                    final Unit unit = access$getSearchStateChangeListener$p.invoke(b);
                }
                final Function2 access$getFilterListener$p = InlineAutocompleteEditText.access$getFilterListener$p(InlineAutocompleteEditText.this);
                if (access$getFilterListener$p != null) {
                    InlineAutocompleteEditText this$0 = inlineAutocompleteEditText;
                    if (n != 0) {
                        this$0 = InlineAutocompleteEditText.this;
                    }
                    final Unit unit2 = access$getFilterListener$p.invoke(access$getNonAutocompleteText, this$0);
                }
                final Function2 access$getTextChangeListener$p = InlineAutocompleteEditText.access$getTextChangeListener$p(InlineAutocompleteEditText.this);
                if (access$getTextChangeListener$p != null) {
                    final Unit unit3 = access$getTextChangeListener$p.invoke(access$getNonAutocompleteText, InlineAutocompleteEditText.this.getText().toString());
                }
            }
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "s");
            this.textLengthBeforeChange = charSequence.length();
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "s");
        }
    }
}
