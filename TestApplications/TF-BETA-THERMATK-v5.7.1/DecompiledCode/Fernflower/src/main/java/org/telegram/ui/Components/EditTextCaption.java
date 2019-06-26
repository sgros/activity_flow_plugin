package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode.Callback;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class EditTextCaption extends EditTextBoldCursor {
   private String caption;
   private StaticLayout captionLayout;
   private boolean copyPasteShowed;
   private EditTextCaption.EditTextCaptionDelegate delegate;
   private int hintColor;
   private int selectionEnd = -1;
   private int selectionStart = -1;
   private int triesCount = 0;
   private int userNameLength;
   private int xOffset;
   private int yOffset;

   public EditTextCaption(Context var1) {
      super(var1);
   }

   private void applyTextStyleToSelection(TypefaceSpan var1) {
      int var2;
      int var3;
      label41: {
         var2 = this.selectionStart;
         if (var2 >= 0) {
            var3 = this.selectionEnd;
            if (var3 >= 0) {
               this.selectionEnd = -1;
               this.selectionStart = -1;
               break label41;
            }
         }

         var2 = this.getSelectionStart();
         var3 = this.getSelectionEnd();
      }

      Editable var4 = this.getText();
      CharacterStyle[] var5 = (CharacterStyle[])var4.getSpans(var2, var3, CharacterStyle.class);
      if (var5 != null && var5.length > 0) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            CharacterStyle var7 = var5[var6];
            int var8 = var4.getSpanStart(var7);
            int var9 = var4.getSpanEnd(var7);
            var4.removeSpan(var7);
            if (var8 < var2) {
               var4.setSpan(var7, var8, var2, 33);
            }

            if (var9 > var3) {
               var4.setSpan(var7, var3, var9, 33);
            }
         }
      }

      if (var1 != null) {
         var4.setSpan(var1, var2, var3, 33);
      }

      EditTextCaption.EditTextCaptionDelegate var10 = this.delegate;
      if (var10 != null) {
         var10.onSpansChanged();
      }

   }

   // $FF: synthetic method
   static void lambda$makeSelectedUrl$1(EditTextBoldCursor var0, DialogInterface var1) {
      var0.requestFocus();
      AndroidUtilities.showKeyboard(var0);
   }

   private Callback overrideCallback(final Callback var1) {
      return new Callback() {
         public boolean onActionItemClicked(ActionMode var1x, MenuItem var2) {
            if (var2.getItemId() == 2131230847) {
               EditTextCaption.this.makeSelectedRegular();
               var1x.finish();
               return true;
            } else if (var2.getItemId() == 2131230842) {
               EditTextCaption.this.makeSelectedBold();
               var1x.finish();
               return true;
            } else if (var2.getItemId() == 2131230844) {
               EditTextCaption.this.makeSelectedItalic();
               var1x.finish();
               return true;
            } else if (var2.getItemId() == 2131230846) {
               EditTextCaption.this.makeSelectedMono();
               var1x.finish();
               return true;
            } else if (var2.getItemId() == 2131230845) {
               EditTextCaption.this.makeSelectedUrl();
               var1x.finish();
               return true;
            } else {
               try {
                  boolean var3 = var1.onActionItemClicked(var1x, var2);
                  return var3;
               } catch (Exception var4) {
                  return true;
               }
            }
         }

         public boolean onCreateActionMode(ActionMode var1x, Menu var2) {
            EditTextCaption.this.copyPasteShowed = true;
            return var1.onCreateActionMode(var1x, var2);
         }

         public void onDestroyActionMode(ActionMode var1x) {
            EditTextCaption.this.copyPasteShowed = false;
            var1.onDestroyActionMode(var1x);
         }

         public boolean onPrepareActionMode(ActionMode var1x, Menu var2) {
            return var1.onPrepareActionMode(var1x, var2);
         }
      };
   }

   public String getCaption() {
      return this.caption;
   }

   // $FF: synthetic method
   public void lambda$makeSelectedUrl$0$EditTextCaption(int var1, int var2, EditTextBoldCursor var3, DialogInterface var4, int var5) {
      Editable var12 = this.getText();
      CharacterStyle[] var6 = (CharacterStyle[])var12.getSpans(var1, var2, CharacterStyle.class);
      if (var6 != null && var6.length > 0) {
         for(var5 = 0; var5 < var6.length; ++var5) {
            CharacterStyle var7 = var6[var5];
            int var8 = var12.getSpanStart(var7);
            int var9 = var12.getSpanEnd(var7);
            var12.removeSpan(var7);
            if (var8 < var1) {
               var12.setSpan(var7, var8, var1, 33);
            }

            if (var9 > var2) {
               var12.setSpan(var7, var2, var9, 33);
            }
         }
      }

      try {
         URLSpanReplacement var13 = new URLSpanReplacement(var3.getText().toString());
         var12.setSpan(var13, var1, var2, 33);
      } catch (Exception var10) {
      }

      EditTextCaption.EditTextCaptionDelegate var11 = this.delegate;
      if (var11 != null) {
         var11.onSpansChanged();
      }

   }

   public void makeSelectedBold() {
      this.applyTextStyleToSelection(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")));
   }

   public void makeSelectedItalic() {
      this.applyTextStyleToSelection(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")));
   }

   public void makeSelectedMono() {
      this.applyTextStyleToSelection(new TypefaceSpan(Typeface.MONOSPACE));
   }

   public void makeSelectedRegular() {
      this.applyTextStyleToSelection((TypefaceSpan)null);
   }

   public void makeSelectedUrl() {
      AlertDialog.Builder var1;
      EditTextBoldCursor var2;
      int var3;
      int var4;
      label20: {
         var1 = new AlertDialog.Builder(this.getContext());
         var1.setTitle(LocaleController.getString("CreateLink", 2131559169));
         var2 = new EditTextBoldCursor(this.getContext()) {
            protected void onMeasure(int var1, int var2) {
               super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F), 1073741824));
            }
         };
         var2.setTextSize(1, 18.0F);
         var2.setText("http://");
         var2.setTextColor(Theme.getColor("dialogTextBlack"));
         var2.setHintText(LocaleController.getString("URL", 2131560927));
         var2.setHeaderHintColor(Theme.getColor("windowBackgroundWhiteBlueHeader"));
         var2.setSingleLine(true);
         var2.setFocusable(true);
         var2.setTransformHintToHeader(true);
         var2.setLineColors(Theme.getColor("windowBackgroundWhiteInputField"), Theme.getColor("windowBackgroundWhiteInputFieldActivated"), Theme.getColor("windowBackgroundWhiteRedText3"));
         var2.setImeOptions(6);
         var2.setBackgroundDrawable((Drawable)null);
         var2.requestFocus();
         var2.setPadding(0, 0, 0, 0);
         var1.setView(var2);
         var3 = this.selectionStart;
         if (var3 >= 0) {
            var4 = this.selectionEnd;
            if (var4 >= 0) {
               this.selectionEnd = -1;
               this.selectionStart = -1;
               break label20;
            }
         }

         var3 = this.getSelectionStart();
         var4 = this.getSelectionEnd();
      }

      var1.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$EditTextCaption$BQIhHIR0EWfMGyyXmJJ_pkFKO1Y(this, var3, var4, var2));
      var1.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
      var1.show().setOnShowListener(new _$$Lambda$EditTextCaption$8tXURyNItaU0tMIyLqohmCvoG40(var2));
      MarginLayoutParams var5 = (MarginLayoutParams)var2.getLayoutParams();
      if (var5 != null) {
         if (var5 instanceof LayoutParams) {
            ((LayoutParams)var5).gravity = 1;
         }

         var4 = AndroidUtilities.dp(24.0F);
         var5.leftMargin = var4;
         var5.rightMargin = var4;
         var5.height = AndroidUtilities.dp(36.0F);
         var2.setLayoutParams(var5);
      }

      var2.setSelection(0, var2.getText().length());
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);

      try {
         if (this.captionLayout != null && this.userNameLength == this.length()) {
            TextPaint var2 = this.getPaint();
            int var3 = this.getPaint().getColor();
            var2.setColor(this.hintColor);
            var1.save();
            var1.translate((float)this.xOffset, (float)this.yOffset);
            this.captionLayout.draw(var1);
            var1.restore();
            var2.setColor(var3);
         }
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
      super.onInitializeAccessibilityNodeInfo(var1);
      if (!TextUtils.isEmpty(this.caption)) {
         if (VERSION.SDK_INT >= 26) {
            var1.setHintText(this.caption);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append(var1.getText());
            var2.append(", ");
            var2.append(this.caption);
            var1.setText(var2.toString());
         }
      }

   }

   @SuppressLint({"DrawAllocation"})
   protected void onMeasure(int var1, int var2) {
      try {
         super.onMeasure(var1, var2);
      } catch (Exception var9) {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(51.0F));
         FileLog.e((Throwable)var9);
      }

      this.captionLayout = null;
      String var3 = this.caption;
      if (var3 != null && var3.length() > 0) {
         Editable var4 = this.getText();
         if (var4.length() > 1 && var4.charAt(0) == '@') {
            var1 = TextUtils.indexOf(var4, ' ');
            if (var1 != -1) {
               TextPaint var10 = this.getPaint();
               ++var1;
               CharSequence var5 = var4.subSequence(0, var1);
               var1 = (int)Math.ceil((double)var10.measureText(var4, 0, var1));
               int var6 = this.getMeasuredWidth();
               int var7 = this.getPaddingLeft();
               var2 = this.getPaddingRight();
               this.userNameLength = var5.length();
               String var12 = this.caption;
               var2 = var6 - var7 - var2 - var1;
               CharSequence var13 = TextUtils.ellipsize(var12, var10, (float)var2, TruncateAt.END);
               this.xOffset = var1;

               try {
                  StaticLayout var11 = new StaticLayout(var13, this.getPaint(), var2, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
                  this.captionLayout = var11;
                  if (this.captionLayout.getLineCount() > 0) {
                     this.xOffset = (int)((float)this.xOffset + -this.captionLayout.getLineLeft(0));
                  }

                  this.yOffset = (this.getMeasuredHeight() - this.captionLayout.getLineBottom(0)) / 2 + AndroidUtilities.dp(0.5F);
               } catch (Exception var8) {
                  FileLog.e((Throwable)var8);
               }
            }
         }
      }

   }

   public void onWindowFocusChanged(boolean var1) {
      if (VERSION.SDK_INT >= 23 || var1 || !this.copyPasteShowed) {
         super.onWindowFocusChanged(var1);
      }
   }

   public void setCaption(String var1) {
      String var2 = this.caption;
      if (var2 != null && var2.length() != 0 || var1 != null && var1.length() != 0) {
         var2 = this.caption;
         if (var2 == null || var1 == null || !var2.equals(var1)) {
            this.caption = var1;
            var1 = this.caption;
            if (var1 != null) {
               this.caption = var1.replace('\n', ' ');
            }

            this.requestLayout();
         }
      }

   }

   public void setDelegate(EditTextCaption.EditTextCaptionDelegate var1) {
      this.delegate = var1;
   }

   public void setHintColor(int var1) {
      super.setHintColor(var1);
      this.hintColor = var1;
      this.invalidate();
   }

   public void setSelectionOverride(int var1, int var2) {
      this.selectionStart = var1;
      this.selectionEnd = var2;
   }

   public ActionMode startActionMode(Callback var1) {
      return super.startActionMode(this.overrideCallback(var1));
   }

   public ActionMode startActionMode(Callback var1, int var2) {
      return super.startActionMode(this.overrideCallback(var1), var2);
   }

   public interface EditTextCaptionDelegate {
      void onSpansChanged();
   }
}
