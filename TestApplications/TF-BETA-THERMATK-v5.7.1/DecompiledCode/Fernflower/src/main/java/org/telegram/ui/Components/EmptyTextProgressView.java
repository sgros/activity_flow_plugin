package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;

public class EmptyTextProgressView extends FrameLayout {
   private boolean inLayout;
   private RadialProgressView progressBar;
   private boolean showAtCenter;
   private TextView textView;

   public EmptyTextProgressView(Context var1) {
      super(var1);
      this.progressBar = new RadialProgressView(var1);
      this.progressBar.setVisibility(4);
      this.addView(this.progressBar, LayoutHelper.createFrame(-2, -2.0F));
      this.textView = new TextView(var1);
      this.textView.setTextSize(1, 20.0F);
      this.textView.setTextColor(Theme.getColor("emptyListPlaceholder"));
      this.textView.setGravity(17);
      this.textView.setVisibility(4);
      this.textView.setPadding(AndroidUtilities.dp(20.0F), 0, AndroidUtilities.dp(20.0F), 0);
      this.textView.setText(LocaleController.getString("NoResult", 2131559943));
      this.addView(this.textView, LayoutHelper.createFrame(-2, -2.0F));
      this.setOnTouchListener(_$$Lambda$EmptyTextProgressView$AeVTSCBshpCl6wf4siSABV33AKw.INSTANCE);
   }

   // $FF: synthetic method
   static boolean lambda$new$0(View var0, MotionEvent var1) {
      return true;
   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      this.inLayout = true;
      int var6 = var5 - var3;
      int var7 = this.getChildCount();

      for(var3 = 0; var3 < var7; ++var3) {
         View var8 = this.getChildAt(var3);
         if (var8.getVisibility() != 8) {
            int var9 = (var4 - var2 - var8.getMeasuredWidth()) / 2;
            if (this.showAtCenter) {
               var5 = (var6 / 2 - var8.getMeasuredHeight()) / 2;
            } else {
               var5 = (var6 - var8.getMeasuredHeight()) / 2;
            }

            var8.layout(var9, var5, var8.getMeasuredWidth() + var9, var8.getMeasuredHeight() + var5);
         }
      }

      this.inLayout = false;
   }

   public void requestLayout() {
      if (!this.inLayout) {
         super.requestLayout();
      }

   }

   public void setProgressBarColor(int var1) {
      this.progressBar.setProgressColor(var1);
   }

   public void setShowAtCenter(boolean var1) {
      this.showAtCenter = var1;
   }

   public void setText(String var1) {
      this.textView.setText(var1);
   }

   public void setTextColor(int var1) {
      this.textView.setTextColor(var1);
   }

   public void setTextSize(int var1) {
      this.textView.setTextSize(1, (float)var1);
   }

   public void setTopImage(int var1) {
      if (var1 == 0) {
         this.textView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
      } else {
         Drawable var2 = this.getContext().getResources().getDrawable(var1).mutate();
         if (var2 != null) {
            var2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("emptyListPlaceholder"), Mode.MULTIPLY));
         }

         this.textView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, var2, (Drawable)null, (Drawable)null);
         this.textView.setCompoundDrawablePadding(AndroidUtilities.dp(1.0F));
      }

   }

   public void showProgress() {
      this.textView.setVisibility(4);
      this.progressBar.setVisibility(0);
   }

   public void showTextView() {
      this.textView.setVisibility(0);
      this.progressBar.setVisibility(4);
   }
}
