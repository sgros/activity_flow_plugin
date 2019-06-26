package android.support.v7.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class TooltipPopup {
   private static final String TAG = "TooltipPopup";
   private final View mContentView;
   private final Context mContext;
   private final LayoutParams mLayoutParams = new LayoutParams();
   private final TextView mMessageView;
   private final int[] mTmpAnchorPos = new int[2];
   private final int[] mTmpAppPos = new int[2];
   private final Rect mTmpDisplayFrame = new Rect();

   TooltipPopup(Context var1) {
      this.mContext = var1;
      this.mContentView = LayoutInflater.from(this.mContext).inflate(R.layout.tooltip, (ViewGroup)null);
      this.mMessageView = (TextView)this.mContentView.findViewById(R.id.message);
      this.mLayoutParams.setTitle(this.getClass().getSimpleName());
      this.mLayoutParams.packageName = this.mContext.getPackageName();
      this.mLayoutParams.type = 1002;
      this.mLayoutParams.width = -2;
      this.mLayoutParams.height = -2;
      this.mLayoutParams.format = -3;
      this.mLayoutParams.windowAnimations = R.style.Animation_AppCompat_Tooltip;
      this.mLayoutParams.flags = 24;
   }

   private void computePosition(View var1, int var2, int var3, boolean var4, LayoutParams var5) {
      int var6 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.tooltip_precise_anchor_threshold);
      if (var1.getWidth() < var6) {
         var2 = var1.getWidth() / 2;
      }

      int var7;
      if (var1.getHeight() >= var6) {
         var7 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.tooltip_precise_anchor_extra_offset);
         var6 = var3 + var7;
         var7 = var3 - var7;
         var3 = var6;
         var6 = var7;
      } else {
         var3 = var1.getHeight();
         var6 = 0;
      }

      var5.gravity = 49;
      Resources var8 = this.mContext.getResources();
      if (var4) {
         var7 = R.dimen.tooltip_y_offset_touch;
      } else {
         var7 = R.dimen.tooltip_y_offset_non_touch;
      }

      int var9 = var8.getDimensionPixelOffset(var7);
      View var12 = getAppRootView(var1);
      if (var12 == null) {
         Log.e("TooltipPopup", "Cannot find app view");
      } else {
         var12.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
         if (this.mTmpDisplayFrame.left < 0 && this.mTmpDisplayFrame.top < 0) {
            Resources var10 = this.mContext.getResources();
            var7 = var10.getIdentifier("status_bar_height", "dimen", "android");
            if (var7 != 0) {
               var7 = var10.getDimensionPixelSize(var7);
            } else {
               var7 = 0;
            }

            DisplayMetrics var13 = var10.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, var7, var13.widthPixels, var13.heightPixels);
         }

         var12.getLocationOnScreen(this.mTmpAppPos);
         var1.getLocationOnScreen(this.mTmpAnchorPos);
         int[] var11 = this.mTmpAnchorPos;
         var11[0] -= this.mTmpAppPos[0];
         var11 = this.mTmpAnchorPos;
         var11[1] -= this.mTmpAppPos[1];
         var5.x = this.mTmpAnchorPos[0] + var2 - this.mTmpDisplayFrame.width() / 2;
         var2 = MeasureSpec.makeMeasureSpec(0, 0);
         this.mContentView.measure(var2, var2);
         var2 = this.mContentView.getMeasuredHeight();
         var6 = this.mTmpAnchorPos[1] + var6 - var9 - var2;
         var3 = this.mTmpAnchorPos[1] + var3 + var9;
         if (var4) {
            if (var6 >= 0) {
               var5.y = var6;
            } else {
               var5.y = var3;
            }
         } else if (var2 + var3 <= this.mTmpDisplayFrame.height()) {
            var5.y = var3;
         } else {
            var5.y = var6;
         }

      }
   }

   private static View getAppRootView(View var0) {
      for(Context var1 = var0.getContext(); var1 instanceof ContextWrapper; var1 = ((ContextWrapper)var1).getBaseContext()) {
         if (var1 instanceof Activity) {
            return ((Activity)var1).getWindow().getDecorView();
         }
      }

      return var0.getRootView();
   }

   void hide() {
      if (this.isShowing()) {
         ((WindowManager)this.mContext.getSystemService("window")).removeView(this.mContentView);
      }
   }

   boolean isShowing() {
      boolean var1;
      if (this.mContentView.getParent() != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   void show(View var1, int var2, int var3, boolean var4, CharSequence var5) {
      if (this.isShowing()) {
         this.hide();
      }

      this.mMessageView.setText(var5);
      this.computePosition(var1, var2, var3, var4, this.mLayoutParams);
      ((WindowManager)this.mContext.getSystemService("window")).addView(this.mContentView, this.mLayoutParams);
   }
}
