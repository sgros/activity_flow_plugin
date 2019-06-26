package android.support.design.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SnackbarContentLayout extends LinearLayout implements BaseTransientBottomBar.ContentViewCallback {
   private Button mActionView;
   private int mMaxInlineActionWidth;
   private int mMaxWidth;
   private TextView mMessageView;

   public SnackbarContentLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SnackbarContentLayout(Context var1, AttributeSet var2) {
      super(var1, var2);
      TypedArray var3 = var1.obtainStyledAttributes(var2, R.styleable.SnackbarLayout);
      this.mMaxWidth = var3.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
      this.mMaxInlineActionWidth = var3.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
      var3.recycle();
   }

   private static void updateTopBottomPadding(View var0, int var1, int var2) {
      if (ViewCompat.isPaddingRelative(var0)) {
         ViewCompat.setPaddingRelative(var0, ViewCompat.getPaddingStart(var0), var1, ViewCompat.getPaddingEnd(var0), var2);
      } else {
         var0.setPadding(var0.getPaddingLeft(), var1, var0.getPaddingRight(), var2);
      }

   }

   private boolean updateViewsWithinLayout(int var1, int var2, int var3) {
      boolean var4;
      if (var1 != this.getOrientation()) {
         this.setOrientation(var1);
         var4 = true;
      } else {
         var4 = false;
      }

      if (this.mMessageView.getPaddingTop() != var2 || this.mMessageView.getPaddingBottom() != var3) {
         updateTopBottomPadding(this.mMessageView, var2, var3);
         var4 = true;
      }

      return var4;
   }

   public void animateContentIn(int var1, int var2) {
      this.mMessageView.setAlpha(0.0F);
      ViewPropertyAnimator var3 = this.mMessageView.animate().alpha(1.0F);
      long var4 = (long)var2;
      var3 = var3.setDuration(var4);
      long var6 = (long)var1;
      var3.setStartDelay(var6).start();
      if (this.mActionView.getVisibility() == 0) {
         this.mActionView.setAlpha(0.0F);
         this.mActionView.animate().alpha(1.0F).setDuration(var4).setStartDelay(var6).start();
      }

   }

   public void animateContentOut(int var1, int var2) {
      this.mMessageView.setAlpha(1.0F);
      ViewPropertyAnimator var3 = this.mMessageView.animate().alpha(0.0F);
      long var4 = (long)var2;
      var3 = var3.setDuration(var4);
      long var6 = (long)var1;
      var3.setStartDelay(var6).start();
      if (this.mActionView.getVisibility() == 0) {
         this.mActionView.setAlpha(1.0F);
         this.mActionView.animate().alpha(0.0F).setDuration(var4).setStartDelay(var6).start();
      }

   }

   public Button getActionView() {
      return this.mActionView;
   }

   public TextView getMessageView() {
      return this.mMessageView;
   }

   protected void onFinishInflate() {
      super.onFinishInflate();
      this.mMessageView = (TextView)this.findViewById(R.id.snackbar_text);
      this.mActionView = (Button)this.findViewById(R.id.snackbar_action);
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      int var3 = var1;
      if (this.mMaxWidth > 0) {
         var3 = var1;
         if (this.getMeasuredWidth() > this.mMaxWidth) {
            var3 = MeasureSpec.makeMeasureSpec(this.mMaxWidth, 1073741824);
            super.onMeasure(var3, var2);
         }
      }

      int var4 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical_2lines);
      int var5 = this.getResources().getDimensionPixelSize(R.dimen.design_snackbar_padding_vertical);
      var1 = this.mMessageView.getLayout().getLineCount();
      boolean var6 = true;
      boolean var7;
      if (var1 > 1) {
         var7 = true;
      } else {
         var7 = false;
      }

      label46: {
         if (var7 && this.mMaxInlineActionWidth > 0 && this.mActionView.getMeasuredWidth() > this.mMaxInlineActionWidth) {
            if (this.updateViewsWithinLayout(1, var4, var4 - var5)) {
               var7 = var6;
               break label46;
            }
         } else {
            if (var7) {
               var1 = var4;
            } else {
               var1 = var5;
            }

            if (this.updateViewsWithinLayout(0, var1, var1)) {
               var7 = var6;
               break label46;
            }
         }

         var7 = false;
      }

      if (var7) {
         super.onMeasure(var3, var2);
      }

   }
}
