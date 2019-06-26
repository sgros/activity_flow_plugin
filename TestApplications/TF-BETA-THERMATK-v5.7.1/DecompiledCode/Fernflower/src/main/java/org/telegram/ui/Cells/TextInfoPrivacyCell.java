package org.telegram.ui.Cells;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class TextInfoPrivacyCell extends FrameLayout {
   private int bottomPadding;
   private int fixedSize;
   private String linkTextColorKey;
   private TextView textView;

   public TextInfoPrivacyCell(Context var1) {
      this(var1, 21);
   }

   public TextInfoPrivacyCell(Context var1, int var2) {
      super(var1);
      this.linkTextColorKey = "windowBackgroundWhiteLinkText";
      this.bottomPadding = 17;
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 14.0F);
      TextView var7 = this.textView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 5;
      byte var5;
      if (var3) {
         var5 = 5;
      } else {
         var5 = 3;
      }

      var7.setGravity(var5);
      this.textView.setPadding(0, AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(17.0F));
      this.textView.setMovementMethod(LinkMovementMethod.getInstance());
      this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText4"));
      this.textView.setLinkTextColor(Theme.getColor(this.linkTextColorKey));
      var7 = this.textView;
      if (LocaleController.isRTL) {
         var5 = var4;
      } else {
         var5 = 3;
      }

      float var6 = (float)var2;
      this.addView(var7, LayoutHelper.createFrame(-1, -2.0F, var5 | 48, var6, 0.0F, var6, 0.0F));
   }

   public TextView getTextView() {
      return this.textView;
   }

   public int length() {
      return this.textView.length();
   }

   protected void onMeasure(int var1, int var2) {
      if (this.fixedSize != 0) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((float)this.fixedSize), 1073741824));
      } else {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(0, 0));
      }

   }

   public void setBottomPadding(int var1) {
      this.bottomPadding = var1;
   }

   public void setEnabled(boolean var1, ArrayList var2) {
      float var3 = 1.0F;
      if (var2 != null) {
         TextView var4 = this.textView;
         if (!var1) {
            var3 = 0.5F;
         }

         var2.add(ObjectAnimator.ofFloat(var4, "alpha", new float[]{var3}));
      } else {
         TextView var5 = this.textView;
         if (!var1) {
            var3 = 0.5F;
         }

         var5.setAlpha(var3);
      }

   }

   public void setFixedSize(int var1) {
      this.fixedSize = var1;
   }

   public void setLinkTextColorKey(String var1) {
      this.linkTextColorKey = var1;
   }

   public void setText(CharSequence var1) {
      if (var1 == null) {
         this.textView.setPadding(0, AndroidUtilities.dp(2.0F), 0, 0);
      } else {
         this.textView.setPadding(0, AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp((float)this.bottomPadding));
      }

      this.textView.setText(var1);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }

   public void setTextColor(String var1) {
      this.textView.setTextColor(Theme.getColor(var1));
      this.textView.setTag(var1);
   }
}
