package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class PollEditTextCell extends FrameLayout {
   private ImageView deleteImageView;
   private boolean needDivider;
   private boolean showNextButton;
   private EditTextBoldCursor textView;
   private SimpleTextView textView2;

   public PollEditTextCell(Context var1, OnClickListener var2) {
      super(var1);
      this.textView = new EditTextBoldCursor(var1) {
         public InputConnection onCreateInputConnection(EditorInfo var1) {
            InputConnection var2 = super.onCreateInputConnection(var1);
            if (PollEditTextCell.this.showNextButton) {
               var1.imeOptions &= -1073741825;
            }

            return var2;
         }
      };
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.textView.setTextSize(1, 16.0F);
      EditTextBoldCursor var3 = this.textView;
      boolean var4 = LocaleController.isRTL;
      byte var5 = 5;
      byte var6;
      if (var4) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      var3.setGravity(var6 | 16);
      this.textView.setBackgroundDrawable((Drawable)null);
      this.textView.setPadding(0, AndroidUtilities.dp(14.0F), 0, AndroidUtilities.dp(14.0F));
      var3 = this.textView;
      var3.setImeOptions(var3.getImeOptions() | 268435456);
      var3 = this.textView;
      var3.setInputType(var3.getInputType() | 16384);
      var3 = this.textView;
      if (LocaleController.isRTL) {
         var6 = 5;
      } else {
         var6 = 3;
      }

      float var7;
      if (LocaleController.isRTL && var2 != null) {
         var7 = 58.0F;
      } else {
         var7 = 21.0F;
      }

      float var8;
      if (!LocaleController.isRTL && var2 != null) {
         var8 = 58.0F;
      } else {
         var8 = 21.0F;
      }

      this.addView(var3, LayoutHelper.createFrame(-1, -2.0F, var6 | 16, var7, 0.0F, var8, 0.0F));
      if (var2 != null) {
         this.deleteImageView = new ImageView(var1);
         this.deleteImageView.setFocusable(false);
         this.deleteImageView.setScaleType(ScaleType.CENTER);
         this.deleteImageView.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("stickers_menuSelector")));
         this.deleteImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("stickers_menu"), Mode.MULTIPLY));
         this.deleteImageView.setImageResource(2131165652);
         this.deleteImageView.setOnClickListener(var2);
         this.deleteImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText"), Mode.MULTIPLY));
         this.deleteImageView.setContentDescription(LocaleController.getString("Delete", 2131559227));
         ImageView var9 = this.deleteImageView;
         if (LocaleController.isRTL) {
            var6 = 3;
         } else {
            var6 = 5;
         }

         if (LocaleController.isRTL) {
            var7 = 3.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = 3.0F;
         }

         this.addView(var9, LayoutHelper.createFrame(48, 50.0F, var6 | 48, var7, 0.0F, var8, 0.0F));
         this.textView2 = new SimpleTextView(this.getContext());
         this.textView2.setTextSize(13);
         SimpleTextView var10 = this.textView2;
         if (LocaleController.isRTL) {
            var6 = 3;
         } else {
            var6 = 5;
         }

         var10.setGravity(var6 | 48);
         var10 = this.textView2;
         var6 = var5;
         if (LocaleController.isRTL) {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 20.0F;
         } else {
            var7 = 0.0F;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = 20.0F;
         }

         this.addView(var10, LayoutHelper.createFrame(48, 24.0F, var6 | 48, var7, 43.0F, var8, 0.0F));
      }

   }

   public void addTextWatcher(TextWatcher var1) {
      this.textView.addTextChangedListener(var1);
   }

   public void callOnDelete() {
      ImageView var1 = this.deleteImageView;
      if (var1 != null) {
         var1.callOnClick();
      }
   }

   protected boolean drawDivider() {
      return true;
   }

   public String getText() {
      return this.textView.getText().toString();
   }

   public EditTextBoldCursor getTextView() {
      return this.textView;
   }

   public SimpleTextView getTextView2() {
      return this.textView2;
   }

   public int length() {
      return this.textView.length();
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider && this.drawDivider()) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(20.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         int var5;
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

   }

   protected void onMeasure(int var1, int var2) {
      var1 = MeasureSpec.getSize(var1);
      ImageView var3 = this.deleteImageView;
      if (var3 != null) {
         var3.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824));
         this.textView2.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0F), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0F), 1073741824));
      }

      EditTextBoldCursor var6 = this.textView;
      int var4 = this.getPaddingLeft();
      var2 = this.getPaddingRight();
      float var5;
      if (this.deleteImageView != null) {
         var5 = 79.0F;
      } else {
         var5 = 42.0F;
      }

      var6.measure(MeasureSpec.makeMeasureSpec(var1 - var4 - var2 - AndroidUtilities.dp(var5), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      var2 = this.textView.getMeasuredHeight();
      this.setMeasuredDimension(var1, Math.max(AndroidUtilities.dp(50.0F), this.textView.getMeasuredHeight()) + this.needDivider);
      SimpleTextView var7 = this.textView2;
      if (var7 != null) {
         if (var2 >= AndroidUtilities.dp(52.0F)) {
            var5 = 1.0F;
         } else {
            var5 = 0.0F;
         }

         var7.setAlpha(var5);
      }

   }

   public void setEnabled(boolean var1, ArrayList var2) {
      this.setEnabled(var1);
   }

   public void setShowNextButton(boolean var1) {
      this.showNextButton = var1;
   }

   public void setText(String var1, boolean var2) {
      this.textView.setText(var1);
      this.needDivider = var2;
      this.setWillNotDraw(var2 ^ true);
   }

   public void setText2(String var1) {
      this.textView2.setText(var1);
   }

   public void setTextAndHint(String var1, String var2, boolean var3) {
      ImageView var4 = this.deleteImageView;
      if (var4 != null) {
         var4.setTag((Object)null);
      }

      this.textView.setText(var1);
      this.textView.setHint(var2);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
