package org.mozilla.rocket.download;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public final class DownloadIndicatorIntroViewHelper {
   public static final DownloadIndicatorIntroViewHelper INSTANCE = new DownloadIndicatorIntroViewHelper();

   private DownloadIndicatorIntroViewHelper() {
   }

   public final void initDownloadIndicatorIntroView(final Fragment var1, final View var2, final ViewGroup var3, final DownloadIndicatorIntroViewHelper.OnViewInflated var4) {
      Intrinsics.checkParameterIsNotNull(var1, "fragment");
      Intrinsics.checkParameterIsNotNull(var4, "listener");
      if (var2 != null) {
         var2.postDelayed((Runnable)(new Runnable() {
            public final void run() {
               if (var1.isResumed() && var3 != null) {
                  int[] var1x = new int[2];
                  var2.getLocationOnScreen(var1x);
                  FragmentActivity var2x = var1.getActivity();
                  if (var2x == null) {
                     Intrinsics.throwNpe();
                  }

                  Object var7 = var2x.getSystemService("window");
                  if (var7 == null) {
                     throw new TypeCastException("null cannot be cast to non-null type android.view.WindowManager");
                  }

                  Display var3x = ((WindowManager)var7).getDefaultDisplay();
                  DisplayMetrics var8 = new DisplayMetrics();
                  var3x.getMetrics(var8);
                  View var4x = LayoutInflater.from((Context)var1.getActivity()).inflate(2131492945, var3);
                  final View var9 = var4x.findViewById(2131296416);
                  DownloadIndicatorIntroViewHelper.OnViewInflated var5 = var4;
                  Intrinsics.checkExpressionValueIsNotNull(var9, "rootView");
                  var5.onInflated(var9);
                  ImageView var13 = (ImageView)var4x.findViewById(2131296414);
                  Intrinsics.checkExpressionValueIsNotNull(var13, "menu");
                  LayoutParams var6 = var13.getLayoutParams();
                  if (var6 == null) {
                     throw new TypeCastException("null cannot be cast to non-null type android.support.constraint.ConstraintLayout.LayoutParams");
                  }

                  ConstraintLayout.LayoutParams var14 = (ConstraintLayout.LayoutParams)var6;
                  var14.setMargins(0, 0, var8.widthPixels - var14.width - var1x[0], var8.heightPixels - var14.height - var1x[1]);
                  ImageView var10 = (ImageView)var4x.findViewById(2131296415);
                  Intrinsics.checkExpressionValueIsNotNull(var10, "pointer");
                  LayoutParams var11 = var10.getLayoutParams();
                  if (var11 == null) {
                     throw new TypeCastException("null cannot be cast to non-null type android.support.constraint.ConstraintLayout.LayoutParams");
                  }

                  ConstraintLayout.LayoutParams var12 = (ConstraintLayout.LayoutParams)var11;
                  var12.setMargins(0, 0, var8.widthPixels - var14.width / 2 - var12.width / 2 - var1x[0], 0);
                  var9.setOnClickListener((OnClickListener)(new OnClickListener() {
                     public final void onClick(View var1x) {
                        var9.setVisibility(8);
                     }
                  }));
                  var13.setOnClickListener((OnClickListener)(new OnClickListener() {
                     public final void onClick(View var1x) {
                        var2.performClick();
                        var1x = var9;
                        if (var1x != null) {
                           var1x.setVisibility(8);
                        }

                     }
                  }));
                  var13.setOnLongClickListener((OnLongClickListener)(new OnLongClickListener() {
                     public final boolean onLongClick(View var1x) {
                        var2.performLongClick();
                        var1x = var9;
                        if (var1x != null) {
                           var1x.setVisibility(8);
                        }

                        return false;
                     }
                  }));
               }

            }
         }), 3500L);
      }

   }

   public interface OnViewInflated {
      void onInflated(View var1);
   }
}
