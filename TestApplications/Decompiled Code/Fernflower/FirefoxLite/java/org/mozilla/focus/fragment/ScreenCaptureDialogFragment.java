package org.mozilla.focus.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import org.mozilla.focus.Inject;

public class ScreenCaptureDialogFragment extends DialogFragment {
   private LottieAnimationView mLottieAnimationView;

   public static ScreenCaptureDialogFragment newInstance() {
      ScreenCaptureDialogFragment var0 = new ScreenCaptureDialogFragment();
      var0.setStyle(2, 2131820825);
      return var0;
   }

   public void dismiss(boolean var1) {
      if (var1) {
         LottieComposition.Factory.fromAssetFileName(this.getContext(), "screenshots-done.json", new OnCompositionLoadedListener() {
            public void onCompositionLoaded(LottieComposition var1) {
               ScreenCaptureDialogFragment.this.mLottieAnimationView.addAnimatorListener(new AnimatorListenerAdapter() {
                  public void onAnimationCancel(Animator var1) {
                     ScreenCaptureDialogFragment.this.dismissAllowingStateLoss();
                  }

                  public void onAnimationEnd(Animator var1) {
                     ScreenCaptureDialogFragment.this.dismissAllowingStateLoss();
                  }
               });
               ScreenCaptureDialogFragment.this.mLottieAnimationView.loop(false);
               ScreenCaptureDialogFragment.this.mLottieAnimationView.setComposition(var1);
               ScreenCaptureDialogFragment.this.mLottieAnimationView.playAnimation();
            }
         });
      } else {
         this.dismissAllowingStateLoss();
      }

   }

   public Dialog onCreateDialog(Bundle var1) {
      Dialog var2 = super.onCreateDialog(var1);
      var2.setOnKeyListener(new OnKeyListener() {
         public boolean onKey(DialogInterface var1, int var2, KeyEvent var3) {
            return var2 == 4;
         }
      });
      return var2;
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(2131492941, var2, false);
      this.mLottieAnimationView = (LottieAnimationView)var4.findViewById(2131296294);
      if (!Inject.isUnderEspressoTest()) {
         this.mLottieAnimationView.playAnimation();
      }

      return var4;
   }
}
