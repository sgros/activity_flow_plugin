package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;

public class EditTextSettingsCell extends FrameLayout {
   private boolean needDivider;
   private EditTextBoldCursor textView;

   public EditTextSettingsCell(Context var1) {
      super(var1);
      this.textView = new EditTextBoldCursor(var1);
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
      this.textView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
      this.textView.setTextSize(1, 16.0F);
      this.textView.setLines(1);
      this.textView.setMaxLines(1);
      this.textView.setSingleLine(true);
      this.textView.setEllipsize(TruncateAt.END);
      EditTextBoldCursor var5 = this.textView;
      boolean var2 = LocaleController.isRTL;
      byte var3 = 5;
      byte var4;
      if (var2) {
         var4 = 5;
      } else {
         var4 = 3;
      }

      var5.setGravity(var4 | 16);
      this.textView.setBackgroundDrawable((Drawable)null);
      this.textView.setPadding(0, 0, 0, 0);
      var5 = this.textView;
      var5.setInputType(var5.getInputType() | 16384);
      var5 = this.textView;
      if (LocaleController.isRTL) {
         var4 = var3;
      } else {
         var4 = 3;
      }

      this.addView(var5, LayoutHelper.createFrame(-1, -1.0F, var4 | 48, 21.0F, 0.0F, 21.0F, 0.0F));
   }

   public void addTextWatcher(TextWatcher var1) {
      this.textView.addTextChangedListener(var1);
   }

   public String getText() {
      return this.textView.getText().toString();
   }

   public EditTextBoldCursor getTextView() {
      return this.textView;
   }

   public int length() {
      return this.textView.length();
   }

   protected void onDraw(Canvas var1) {
      if (this.needDivider) {
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
      this.setMeasuredDimension(MeasureSpec.getSize(var1), AndroidUtilities.dp(50.0F) + this.needDivider);
      var1 = this.getMeasuredWidth();
      int var3 = this.getPaddingLeft();
      int var4 = this.getPaddingRight();
      var2 = AndroidUtilities.dp(42.0F);
      this.textView.measure(MeasureSpec.makeMeasureSpec(var1 - var3 - var4 - var2, 1073741824), MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      this.setEnabled(var1);
   }

   public void setText(String var1, boolean var2) {
      this.textView.setText(var1);
      this.needDivider = var2;
      this.setWillNotDraw(var2 ^ true);
   }

   public void setTextAndHint(String var1, String var2, boolean var3) {
      this.textView.setText(var1);
      this.textView.setHint(var2);
      this.needDivider = var3;
      this.setWillNotDraw(var3 ^ true);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }
}
