package mozilla.components.ui.autocomplete;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.NoCopySpan.Concrete;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView.BufferType;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class InlineAutocompleteEditText extends AppCompatEditText {
   private static final Concrete AUTOCOMPLETE_SPAN = new Concrete();
   public static final InlineAutocompleteEditText.Companion Companion = new InlineAutocompleteEditText.Companion((DefaultConstructorMarker)null);
   private static final int DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR = Color.parseColor("#ffb5007f");
   private final int autoCompleteBackgroundColor;
   private int autoCompletePrefixLength;
   private Object[] autoCompleteSpans;
   private InlineAutocompleteEditText.AutocompleteResult autocompleteResult;
   private Function0 commitListener;
   private final Context ctx;
   private boolean discardAutoCompleteResult;
   private Function2 filterListener;
   private Function3 keyPreImeListener;
   private final Function3 onKey;
   private final Function3 onKeyPreIme;
   private final Function2 onSelectionChanged;
   private Function1 searchStateChangeListener;
   private Function2 selectionChangedListener;
   private boolean settingAutoComplete;
   private Function2 textChangeListener;
   private Function1 windowFocusChangeListener;

   public InlineAutocompleteEditText(Context var1) {
      this(var1, (AttributeSet)null, 0, 6, (DefaultConstructorMarker)null);
   }

   public InlineAutocompleteEditText(Context var1, AttributeSet var2) {
      this(var1, var2, 0, 4, (DefaultConstructorMarker)null);
   }

   public InlineAutocompleteEditText(Context var1, final AttributeSet var2, int var3) {
      Intrinsics.checkParameterIsNotNull(var1, "ctx");
      super(var1, var2, var3);
      this.ctx = var1;
      this.autocompleteResult = InlineAutocompleteEditText.AutocompleteResult.Companion.emptyResult();
      this.autoCompleteBackgroundColor = ((Number)((Function0)(new Function0() {
         public final int invoke() {
            TypedArray var1 = InlineAutocompleteEditText.this.getContext().obtainStyledAttributes(var2, R.styleable.InlineAutocompleteEditText);
            int var2x = var1.getColor(R.styleable.InlineAutocompleteEditText_autocompleteBackgroundColor, InlineAutocompleteEditText.Companion.getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR());
            var1.recycle();
            return var2x;
         }
      })).invoke()).intValue();
      this.onKeyPreIme = (Function3)(new Function3() {
         public final boolean invoke(View var1, int var2, KeyEvent var3) {
            Intrinsics.checkParameterIsNotNull(var1, "<anonymous parameter 0>");
            Intrinsics.checkParameterIsNotNull(var3, "event");
            if (var3.getAction() != 0) {
               return false;
            } else {
               Editable var4;
               if (var2 == 66) {
                  var4 = InlineAutocompleteEditText.this.getText();
                  InlineAutocompleteEditText.Companion var7 = InlineAutocompleteEditText.Companion;
                  Intrinsics.checkExpressionValueIsNotNull(var4, "content");
                  if (!var7.hasCompositionString(var4)) {
                     Function0 var5 = InlineAutocompleteEditText.this.commitListener;
                     if (var5 != null) {
                        Unit var6 = (Unit)var5.invoke();
                     }

                     return true;
                  }
               }

               if (var2 == 4) {
                  InlineAutocompleteEditText var8 = InlineAutocompleteEditText.this;
                  var4 = InlineAutocompleteEditText.this.getText();
                  Intrinsics.checkExpressionValueIsNotNull(var4, "text");
                  var8.removeAutocomplete(var4);
                  return false;
               } else {
                  return false;
               }
            }
         }
      });
      this.onKey = (Function3)(new Function3() {
         public final boolean invoke(View var1, int var2, KeyEvent var3) {
            Intrinsics.checkParameterIsNotNull(var1, "<anonymous parameter 0>");
            Intrinsics.checkParameterIsNotNull(var3, "event");
            boolean var4 = true;
            if (var2 == 66) {
               if (var3.getAction() != 0) {
                  return true;
               } else {
                  Function0 var6 = InlineAutocompleteEditText.this.commitListener;
                  if (var6 != null) {
                     Unit var7 = (Unit)var6.invoke();
                  }

                  return true;
               }
            } else {
               if (var2 == 67 || var2 == 112) {
                  InlineAutocompleteEditText var8 = InlineAutocompleteEditText.this;
                  Editable var5 = InlineAutocompleteEditText.this.getText();
                  Intrinsics.checkExpressionValueIsNotNull(var5, "text");
                  if (var8.removeAutocomplete(var5)) {
                     return var4;
                  }
               }

               var4 = false;
               return var4;
            }
         }
      });
      this.onSelectionChanged = (Function2)(new Function2() {
         public final void invoke(int var1, int var2) {
            Editable var3 = InlineAutocompleteEditText.this.getText();
            int var4 = var3.getSpanStart(InlineAutocompleteEditText.Companion.getAUTOCOMPLETE_SPAN());
            if (!InlineAutocompleteEditText.this.settingAutoComplete && var4 >= 0 && (var4 != var1 || var4 != var2)) {
               InlineAutocompleteEditText var5;
               if (var1 <= var4 && var2 <= var4) {
                  var5 = InlineAutocompleteEditText.this;
                  Intrinsics.checkExpressionValueIsNotNull(var3, "text");
                  var5.removeAutocomplete(var3);
               } else {
                  var5 = InlineAutocompleteEditText.this;
                  Intrinsics.checkExpressionValueIsNotNull(var3, "text");
                  var5.commitAutocomplete(var3);
               }

            }
         }
      });
   }

   // $FF: synthetic method
   public InlineAutocompleteEditText(Context var1, AttributeSet var2, int var3, int var4, DefaultConstructorMarker var5) {
      if ((var4 & 2) != 0) {
         var2 = (AttributeSet)null;
      }

      if ((var4 & 4) != 0) {
         var3 = R.attr.editTextStyle;
      }

      this(var1, var2, var3);
   }

   private final void beginSettingAutocomplete() {
      this.beginBatchEdit();
      this.settingAutoComplete = true;
   }

   private final boolean commitAutocomplete(Editable var1) {
      int var2 = var1.getSpanStart(AUTOCOMPLETE_SPAN);
      int var3 = 0;
      if (var2 < 0) {
         return false;
      } else {
         this.beginSettingAutocomplete();
         Object[] var4 = this.autoCompleteSpans;
         if (var4 == null) {
            Intrinsics.throwNpe();
         }

         for(var2 = var4.length; var3 < var2; ++var3) {
            var1.removeSpan(var4[var3]);
         }

         this.autoCompletePrefixLength = var1.length();
         this.setCursorVisible(true);
         this.endSettingAutocomplete();
         Function2 var6 = this.filterListener;
         if (var6 != null) {
            Unit var5 = (Unit)var6.invoke(var1.toString(), (Object)null);
         }

         return true;
      }
   }

   private final void endSettingAutocomplete() {
      this.settingAutoComplete = false;
      this.endBatchEdit();
   }

   private final boolean removeAutocomplete(Editable var1) {
      int var2 = var1.getSpanStart(AUTOCOMPLETE_SPAN);
      if (var2 < 0) {
         return false;
      } else {
         this.beginSettingAutocomplete();
         var1.delete(var2, var1.length());
         this.autocompleteResult = InlineAutocompleteEditText.AutocompleteResult.Companion.emptyResult();
         this.setCursorVisible(true);
         this.endSettingAutocomplete();
         return true;
      }
   }

   private final void resetAutocompleteState() {
      this.autoCompleteSpans = new Object[]{AUTOCOMPLETE_SPAN, new BackgroundColorSpan(this.autoCompleteBackgroundColor)};
      this.autocompleteResult = InlineAutocompleteEditText.AutocompleteResult.Companion.emptyResult();
      this.autoCompletePrefixLength = this.getText().length();
      this.setCursorVisible(true);
   }

   private final void setAutocompleteResult(InlineAutocompleteEditText.AutocompleteResult var1) {
      this.autocompleteResult = var1;
   }

   public final void applyAutocompleteResult(InlineAutocompleteEditText.AutocompleteResult var1) {
      Intrinsics.checkParameterIsNotNull(var1, "result");
      if (!this.discardAutoCompleteResult) {
         if (this.isEnabled() && !var1.isEmpty()) {
            Editable var2 = this.getText();
            int var3 = var2.length();
            int var4 = var1.getLength();
            int var5 = var2.getSpanStart(AUTOCOMPLETE_SPAN);
            this.autocompleteResult = var1;
            if (var5 > -1) {
               if (!TextUtils.regionMatches((CharSequence)var1.getText(), 0, (CharSequence)var2, 0, var5)) {
                  return;
               }

               this.beginSettingAutocomplete();
               var2.replace(var5, var3, (CharSequence)var1.getText(), var5, var4);
               if (var5 == var4) {
                  this.setCursorVisible(true);
               }

               this.endSettingAutocomplete();
            } else {
               if (var4 <= var3 || !TextUtils.regionMatches((CharSequence)var1.getText(), 0, (CharSequence)var2, 0, var3)) {
                  return;
               }

               Object[] var6 = var2.getSpans(var3, var3, Object.class);
               int[] var7 = new int[var6.length];
               int[] var8 = new int[var6.length];
               int[] var9 = new int[var6.length];
               Intrinsics.checkExpressionValueIsNotNull(var6, "spans");
               int var10 = var6.length;

               int var12;
               for(var5 = 0; var5 < var10; ++var5) {
                  Object var11 = var6[var5];
                  var12 = var2.getSpanFlags(var11);
                  if ((var12 & 256) != 0 || var11 == Selection.SELECTION_START || var11 == Selection.SELECTION_END) {
                     var7[var5] = var2.getSpanStart(var11);
                     var8[var5] = var2.getSpanEnd(var11);
                     var9[var5] = var12;
                  }
               }

               this.beginSettingAutocomplete();
               var2.append((CharSequence)var1.getText(), var3, var4);
               var10 = var6.length;

               for(var5 = 0; var5 < var10; ++var5) {
                  var12 = var9[var5];
                  if (var12 != 0) {
                     var2.setSpan(var6[var5], var7[var5], var8[var5], var12);
                  }
               }

               Object[] var13 = this.autoCompleteSpans;
               if (var13 == null) {
                  Intrinsics.throwNpe();
               }

               var10 = var13.length;

               for(var5 = 0; var5 < var10; ++var5) {
                  var2.setSpan(var13[var5], var3, var4, 33);
               }

               this.setCursorVisible(false);
               this.bringPointIntoView(var4);
               this.endSettingAutocomplete();
            }

            this.announceForAccessibility((CharSequence)var2.toString());
         } else {
            this.autocompleteResult = InlineAutocompleteEditText.AutocompleteResult.Companion.emptyResult();
         }
      }
   }

   public final InlineAutocompleteEditText.AutocompleteResult getAutocompleteResult() {
      return this.autocompleteResult;
   }

   public final Context getCtx() {
      return this.ctx;
   }

   public final String getNonAutocompleteText() {
      InlineAutocompleteEditText.Companion var1 = Companion;
      Editable var2 = this.getText();
      Intrinsics.checkExpressionValueIsNotNull(var2, "text");
      return var1.getNonAutocompleteText(var2);
   }

   public final String getOriginalText() {
      return this.getText().subSequence(0, this.autoCompletePrefixLength).toString();
   }

   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      this.keyPreImeListener = this.onKeyPreIme;
      this.selectionChangedListener = this.onSelectionChanged;
      Function3 var1 = this.onKey;
      Object var2 = var1;
      if (var1 != null) {
         var2 = new InlineAutocompleteEditTextKt$sam$android_view_View_OnKeyListener$0(var1);
      }

      this.setOnKeyListener((OnKeyListener)var2);
      this.addTextChangedListener((TextWatcher)(new InlineAutocompleteEditText.TextChangeListener()));
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      Intrinsics.checkParameterIsNotNull(var1, "outAttrs");
      final InputConnection var2 = super.onCreateInputConnection(var1);
      return var2 != null ? (InputConnection)(new InputConnectionWrapper(var2, false) {
         private final boolean removeAutocompleteOnComposing(CharSequence var1) {
            Editable var2x = InlineAutocompleteEditText.this.getText();
            Spannable var3 = (Spannable)var2x;
            int var4 = BaseInputConnection.getComposingSpanStart(var3);
            int var5 = BaseInputConnection.getComposingSpanEnd(var3);
            if (var4 >= 0 && var5 >= 0 && var5 - var4 > var1.length()) {
               InlineAutocompleteEditText var6 = InlineAutocompleteEditText.this;
               Intrinsics.checkExpressionValueIsNotNull(var2x, "editable");
               if (var6.removeAutocomplete(var2x)) {
                  this.finishComposingText();
                  return true;
               }
            }

            return false;
         }

         public boolean commitText(CharSequence var1, int var2x) {
            Intrinsics.checkParameterIsNotNull(var1, "text");
            boolean var3;
            if (this.removeAutocompleteOnComposing(var1)) {
               var3 = false;
            } else {
               var3 = super.commitText(var1, var2x);
            }

            return var3;
         }

         public boolean deleteSurroundingText(int var1, int var2x) {
            InlineAutocompleteEditText var3 = InlineAutocompleteEditText.this;
            Editable var4 = InlineAutocompleteEditText.this.getText();
            Intrinsics.checkExpressionValueIsNotNull(var4, "text");
            return var3.removeAutocomplete(var4) ? false : super.deleteSurroundingText(var1, var2x);
         }

         public boolean setComposingText(CharSequence var1, int var2x) {
            Intrinsics.checkParameterIsNotNull(var1, "text");
            boolean var3;
            if (this.removeAutocompleteOnComposing(var1)) {
               var3 = false;
            } else {
               var3 = super.setComposingText(var1, var2x);
            }

            return var3;
         }
      }) : null;
   }

   public void onFocusChanged(boolean var1, int var2, Rect var3) {
      super.onFocusChanged(var1, var2, var3);
      boolean var4 = TextUtils.isEmpty((CharSequence)this.getText());
      Function1 var6 = this.searchStateChangeListener;
      if (var6 != null) {
         Unit var7 = (Unit)var6.invoke(var4 ^ true);
      }

      if (var1) {
         this.resetAutocompleteState();
      } else {
         Editable var8 = this.getText();
         Intrinsics.checkExpressionValueIsNotNull(var8, "text");
         this.removeAutocomplete(var8);
         Object var9 = this.ctx.getSystemService("input_method");
         if (var9 != null) {
            InputMethodManager var10 = (InputMethodManager)var9;

            try {
               var10.restartInput((View)this);
               var10.hideSoftInputFromWindow(this.getWindowToken(), 0);
            } catch (NullPointerException var5) {
            }

         } else {
            throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
         }
      }
   }

   public boolean onKeyPreIme(int var1, KeyEvent var2) {
      Intrinsics.checkParameterIsNotNull(var2, "event");
      Function3 var3 = this.keyPreImeListener;
      boolean var4;
      if (var3 != null) {
         Boolean var5 = (Boolean)var3.invoke(this, var1, var2);
         if (var5 != null) {
            var4 = var5;
            return var4;
         }
      }

      var4 = false;
      return var4;
   }

   public void onSelectionChanged(int var1, int var2) {
      Function2 var3 = this.selectionChangedListener;
      if (var3 != null) {
         Unit var4 = (Unit)var3.invoke(var1, var2);
      }

      super.onSelectionChanged(var1, var2);
   }

   public boolean onTouchEvent(MotionEvent var1) {
      Intrinsics.checkParameterIsNotNull(var1, "event");
      boolean var2;
      if (VERSION.SDK_INT == 23 && var1.getActionMasked() == 1) {
         try {
            var2 = super.onTouchEvent(var1);
         } catch (NullPointerException var3) {
            this.clearFocus();
            var2 = true;
         }
      } else {
         var2 = super.onTouchEvent(var1);
      }

      return var2;
   }

   public void onWindowFocusChanged(boolean var1) {
      super.onWindowFocusChanged(var1);
      Function1 var2 = this.windowFocusChangeListener;
      if (var2 != null) {
         Unit var3 = (Unit)var2.invoke(var1);
      }

   }

   public void sendAccessibilityEventUnchecked(AccessibilityEvent var1) {
      Intrinsics.checkParameterIsNotNull(var1, "event");
      if (var1.getEventType() == 8192 && this.getParent() != null && !this.isShown()) {
         this.onInitializeAccessibilityEvent(var1);
         this.dispatchPopulateAccessibilityEvent(var1);
         this.getParent().requestSendAccessibilityEvent((View)this, var1);
      } else {
         super.sendAccessibilityEventUnchecked(var1);
      }

   }

   public final void setOnCommitListener(Function0 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.commitListener = var1;
   }

   public final void setOnFilterListener(Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.filterListener = var1;
   }

   public final void setOnKeyPreImeListener(Function3 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.keyPreImeListener = var1;
   }

   public final void setOnSearchStateChangeListener(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.searchStateChangeListener = var1;
   }

   public final void setOnSelectionChangedListener(Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.selectionChangedListener = var1;
   }

   public final void setOnTextChangeListener(Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.textChangeListener = var1;
   }

   public final void setOnWindowsFocusChangeListener(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "l");
      this.windowFocusChangeListener = var1;
   }

   public void setText(CharSequence var1, BufferType var2) {
      String var3;
      label12: {
         Intrinsics.checkParameterIsNotNull(var2, "type");
         if (var1 != null) {
            var3 = var1.toString();
            if (var3 != null) {
               break label12;
            }
         }

         var3 = "";
      }

      super.setText((CharSequence)var3, var2);
      this.resetAutocompleteState();
   }

   public static final class AutocompleteResult {
      public static final InlineAutocompleteEditText.AutocompleteResult.Companion Companion = new InlineAutocompleteEditText.AutocompleteResult.Companion((DefaultConstructorMarker)null);
      private final boolean isEmpty;
      private final int length;
      private final String source;
      private final String text;
      private final Function1 textFormatter;
      private final int totalItems;

      public AutocompleteResult(String var1, String var2, int var3, Function1 var4) {
         Intrinsics.checkParameterIsNotNull(var1, "text");
         Intrinsics.checkParameterIsNotNull(var2, "source");
         super();
         this.text = var1;
         this.source = var2;
         this.totalItems = var3;
         this.textFormatter = var4;
         boolean var5;
         if (((CharSequence)this.text).length() == 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.isEmpty = var5;
         this.length = this.text.length();
      }

      // $FF: synthetic method
      public AutocompleteResult(String var1, String var2, int var3, Function1 var4, int var5, DefaultConstructorMarker var6) {
         if ((var5 & 8) != 0) {
            var4 = (Function1)null;
         }

         this(var1, var2, var3, var4);
      }

      public boolean equals(Object var1) {
         if (this != var1) {
            if (!(var1 instanceof InlineAutocompleteEditText.AutocompleteResult)) {
               return false;
            }

            InlineAutocompleteEditText.AutocompleteResult var3 = (InlineAutocompleteEditText.AutocompleteResult)var1;
            if (!Intrinsics.areEqual(this.text, var3.text) || !Intrinsics.areEqual(this.source, var3.source)) {
               return false;
            }

            boolean var2;
            if (this.totalItems == var3.totalItems) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (!var2 || !Intrinsics.areEqual(this.textFormatter, var3.textFormatter)) {
               return false;
            }
         }

         return true;
      }

      public final String getFormattedText() {
         Function1 var1 = this.textFormatter;
         String var2;
         if (var1 != null) {
            var2 = (String)var1.invoke(this.text);
            if (var2 != null) {
               return var2;
            }
         }

         var2 = this.text;
         return var2;
      }

      public final int getLength() {
         return this.length;
      }

      public final String getText() {
         return this.text;
      }

      public int hashCode() {
         String var1 = this.text;
         int var2 = 0;
         int var3;
         if (var1 != null) {
            var3 = var1.hashCode();
         } else {
            var3 = 0;
         }

         var1 = this.source;
         int var4;
         if (var1 != null) {
            var4 = var1.hashCode();
         } else {
            var4 = 0;
         }

         int var5 = this.totalItems;
         Function1 var6 = this.textFormatter;
         if (var6 != null) {
            var2 = var6.hashCode();
         }

         return ((var3 * 31 + var4) * 31 + var5) * 31 + var2;
      }

      public final boolean isEmpty() {
         return this.isEmpty;
      }

      public final boolean startsWith(String var1) {
         Intrinsics.checkParameterIsNotNull(var1, "text");
         return StringsKt.startsWith$default(this.text, var1, false, 2, (Object)null);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("AutocompleteResult(text=");
         var1.append(this.text);
         var1.append(", source=");
         var1.append(this.source);
         var1.append(", totalItems=");
         var1.append(this.totalItems);
         var1.append(", textFormatter=");
         var1.append(this.textFormatter);
         var1.append(")");
         return var1.toString();
      }

      public static final class Companion {
         private Companion() {
         }

         // $FF: synthetic method
         public Companion(DefaultConstructorMarker var1) {
            this();
         }

         public final InlineAutocompleteEditText.AutocompleteResult emptyResult() {
            return new InlineAutocompleteEditText.AutocompleteResult("", "", 0, (Function1)null, 8, (DefaultConstructorMarker)null);
         }
      }
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      private final String getNonAutocompleteText(Editable var1) {
         int var2 = var1.getSpanStart(((InlineAutocompleteEditText.Companion)this).getAUTOCOMPLETE_SPAN());
         String var3;
         if (var2 < 0) {
            var3 = var1.toString();
         } else {
            var3 = TextUtils.substring((CharSequence)var1, 0, var2);
            Intrinsics.checkExpressionValueIsNotNull(var3, "TextUtils.substring(text, 0, start)");
         }

         return var3;
      }

      private final boolean hasCompositionString(Editable var1) {
         int var2 = var1.length();
         boolean var3 = false;
         Object[] var4 = var1.getSpans(0, var2, Object.class);
         Intrinsics.checkExpressionValueIsNotNull(var4, "spans");
         int var5 = var4.length;
         var2 = 0;

         boolean var6;
         while(true) {
            var6 = var3;
            if (var2 >= var5) {
               break;
            }

            boolean var7;
            if ((var1.getSpanFlags(var4[var2]) & 256) != 0) {
               var7 = true;
            } else {
               var7 = false;
            }

            if (var7) {
               var6 = true;
               break;
            }

            ++var2;
         }

         return var6;
      }

      public final Concrete getAUTOCOMPLETE_SPAN() {
         return InlineAutocompleteEditText.AUTOCOMPLETE_SPAN;
      }

      public final int getDEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR() {
         return InlineAutocompleteEditText.DEFAULT_AUTOCOMPLETE_BACKGROUND_COLOR;
      }
   }

   private final class TextChangeListener implements TextWatcher {
      private int textLengthBeforeChange;

      public TextChangeListener() {
      }

      public void afterTextChanged(Editable var1) {
         Intrinsics.checkParameterIsNotNull(var1, "editable");
         if (InlineAutocompleteEditText.this.isEnabled() && !InlineAutocompleteEditText.this.settingAutoComplete) {
            String var2 = InlineAutocompleteEditText.Companion.getNonAutocompleteText(var1);
            int var3 = var2.length();
            CharSequence var4 = (CharSequence)var2;
            CharSequence var5 = (CharSequence)" ";
            Object var6 = null;
            boolean var7 = StringsKt.contains$default(var4, var5, false, 2, (Object)null);
            boolean var8 = true;
            boolean var9;
            if (!var7 && var3 != this.textLengthBeforeChange - 1 && var3 != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            InlineAutocompleteEditText.this.autoCompletePrefixLength = var3;
            InlineAutocompleteEditText.this.discardAutoCompleteResult = var9 ^ true;
            if (var9 && InlineAutocompleteEditText.this.getAutocompleteResult().startsWith(var2)) {
               InlineAutocompleteEditText.this.applyAutocompleteResult(InlineAutocompleteEditText.this.getAutocompleteResult());
               var9 = false;
            } else {
               InlineAutocompleteEditText.this.removeAutocomplete(var1);
            }

            Function1 var10 = InlineAutocompleteEditText.this.searchStateChangeListener;
            Unit var11;
            if (var10 != null) {
               if (var3 <= 0) {
                  var8 = false;
               }

               var11 = (Unit)var10.invoke(var8);
            }

            Function2 var14 = InlineAutocompleteEditText.this.filterListener;
            if (var14 != null) {
               InlineAutocompleteEditText var12 = (InlineAutocompleteEditText)var6;
               if (var9) {
                  var12 = InlineAutocompleteEditText.this;
               }

               var11 = (Unit)var14.invoke(var2, var12);
            }

            Function2 var13 = InlineAutocompleteEditText.this.textChangeListener;
            if (var13 != null) {
               var11 = (Unit)var13.invoke(var2, InlineAutocompleteEditText.this.getText().toString());
            }

         }
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         Intrinsics.checkParameterIsNotNull(var1, "s");
         this.textLengthBeforeChange = var1.length();
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
         Intrinsics.checkParameterIsNotNull(var1, "s");
      }
   }
}
