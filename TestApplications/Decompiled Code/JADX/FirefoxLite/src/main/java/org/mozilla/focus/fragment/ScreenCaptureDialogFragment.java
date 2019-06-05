package org.mozilla.focus.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.p001v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieComposition.Factory;
import com.airbnb.lottie.OnCompositionLoadedListener;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.Inject;
import org.mozilla.rocket.C0769R;

public class ScreenCaptureDialogFragment extends DialogFragment {
    private LottieAnimationView mLottieAnimationView;

    /* renamed from: org.mozilla.focus.fragment.ScreenCaptureDialogFragment$1 */
    class C04781 implements OnKeyListener {
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return i == 4;
        }

        C04781() {
        }
    }

    /* renamed from: org.mozilla.focus.fragment.ScreenCaptureDialogFragment$2 */
    class C06972 implements OnCompositionLoadedListener {

        /* renamed from: org.mozilla.focus.fragment.ScreenCaptureDialogFragment$2$1 */
        class C04791 extends AnimatorListenerAdapter {
            C04791() {
            }

            public void onAnimationCancel(Animator animator) {
                ScreenCaptureDialogFragment.this.dismissAllowingStateLoss();
            }

            public void onAnimationEnd(Animator animator) {
                ScreenCaptureDialogFragment.this.dismissAllowingStateLoss();
            }
        }

        C06972() {
        }

        public void onCompositionLoaded(LottieComposition lottieComposition) {
            ScreenCaptureDialogFragment.this.mLottieAnimationView.addAnimatorListener(new C04791());
            ScreenCaptureDialogFragment.this.mLottieAnimationView.loop(false);
            ScreenCaptureDialogFragment.this.mLottieAnimationView.setComposition(lottieComposition);
            ScreenCaptureDialogFragment.this.mLottieAnimationView.playAnimation();
        }
    }

    public static ScreenCaptureDialogFragment newInstance() {
        ScreenCaptureDialogFragment screenCaptureDialogFragment = new ScreenCaptureDialogFragment();
        screenCaptureDialogFragment.setStyle(2, C0769R.style.ScreenCaptureDialog);
        return screenCaptureDialogFragment;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.setOnKeyListener(new C04781());
        return onCreateDialog;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C0769R.layout.dialog_capture_screen, viewGroup, false);
        this.mLottieAnimationView = (LottieAnimationView) inflate.findViewById(C0427R.C0426id.animation_view);
        if (!Inject.isUnderEspressoTest()) {
            this.mLottieAnimationView.playAnimation();
        }
        return inflate;
    }

    public void dismiss(boolean z) {
        if (z) {
            Factory.fromAssetFileName(getContext(), "screenshots-done.json", new C06972());
        } else {
            dismissAllowingStateLoss();
        }
    }
}
