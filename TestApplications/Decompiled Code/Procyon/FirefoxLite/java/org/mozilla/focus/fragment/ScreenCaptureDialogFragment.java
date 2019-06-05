// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.fragment;

import org.mozilla.focus.Inject;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.content.DialogInterface$OnKeyListener;
import android.app.Dialog;
import android.os.Bundle;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.airbnb.lottie.LottieAnimationView;
import android.support.v4.app.DialogFragment;

public class ScreenCaptureDialogFragment extends DialogFragment
{
    private LottieAnimationView mLottieAnimationView;
    
    public static ScreenCaptureDialogFragment newInstance() {
        final ScreenCaptureDialogFragment screenCaptureDialogFragment = new ScreenCaptureDialogFragment();
        screenCaptureDialogFragment.setStyle(2, 2131820825);
        return screenCaptureDialogFragment;
    }
    
    public void dismiss(final boolean b) {
        if (b) {
            LottieComposition.Factory.fromAssetFileName(this.getContext(), "screenshots-done.json", new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(final LottieComposition composition) {
                    ScreenCaptureDialogFragment.this.mLottieAnimationView.addAnimatorListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationCancel(final Animator animator) {
                            ScreenCaptureDialogFragment.this.dismissAllowingStateLoss();
                        }
                        
                        public void onAnimationEnd(final Animator animator) {
                            ScreenCaptureDialogFragment.this.dismissAllowingStateLoss();
                        }
                    });
                    ScreenCaptureDialogFragment.this.mLottieAnimationView.loop(false);
                    ScreenCaptureDialogFragment.this.mLottieAnimationView.setComposition(composition);
                    ScreenCaptureDialogFragment.this.mLottieAnimationView.playAnimation();
                }
            });
        }
        else {
            this.dismissAllowingStateLoss();
        }
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle bundle) {
        final Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.setOnKeyListener((DialogInterface$OnKeyListener)new DialogInterface$OnKeyListener() {
            public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
                return n == 4;
            }
        });
        return onCreateDialog;
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2131492941, viewGroup, false);
        this.mLottieAnimationView = (LottieAnimationView)inflate.findViewById(2131296294);
        if (!Inject.isUnderEspressoTest()) {
            this.mLottieAnimationView.playAnimation();
        }
        return inflate;
    }
}
