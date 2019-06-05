package org.mozilla.rocket.download;

import android.support.constraint.ConstraintLayout;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.download.DownloadIndicatorIntroViewHelper.OnViewInflated;

/* compiled from: DownloadIndicatorIntroViewHelper.kt */
/* renamed from: org.mozilla.rocket.download.DownloadIndicatorIntroViewHelper$initDownloadIndicatorIntroView$1 */
final class C0595x1a8f49f3 implements Runnable {
    final /* synthetic */ Fragment $fragment;
    final /* synthetic */ OnViewInflated $listener;
    final /* synthetic */ ViewGroup $parentView;
    final /* synthetic */ View $targetView;

    C0595x1a8f49f3(Fragment fragment, ViewGroup viewGroup, View view, OnViewInflated onViewInflated) {
        this.$fragment = fragment;
        this.$parentView = viewGroup;
        this.$targetView = view;
        this.$listener = onViewInflated;
    }

    public final void run() {
        if (this.$fragment.isResumed() && this.$parentView != null) {
            int[] iArr = new int[2];
            this.$targetView.getLocationOnScreen(iArr);
            FragmentActivity activity = this.$fragment.getActivity();
            if (activity == null) {
                Intrinsics.throwNpe();
            }
            Object systemService = activity.getSystemService("window");
            if (systemService != null) {
                Display defaultDisplay = ((WindowManager) systemService).getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                defaultDisplay.getMetrics(displayMetrics);
                View inflate = LayoutInflater.from(this.$fragment.getActivity()).inflate(C0769R.layout.download_indicator_intro, this.$parentView);
                final View findViewById = inflate.findViewById(C0427R.C0426id.download_indicator_intro_root);
                OnViewInflated onViewInflated = this.$listener;
                Intrinsics.checkExpressionValueIsNotNull(findViewById, "rootView");
                onViewInflated.onInflated(findViewById);
                ImageView imageView = (ImageView) inflate.findViewById(C0427R.C0426id.download_indicator_intro_menu);
                Intrinsics.checkExpressionValueIsNotNull(imageView, "menu");
                LayoutParams layoutParams = imageView.getLayoutParams();
                if (layoutParams != null) {
                    ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) layoutParams;
                    layoutParams2.setMargins(0, 0, (displayMetrics.widthPixels - layoutParams2.width) - iArr[0], (displayMetrics.heightPixels - layoutParams2.height) - iArr[1]);
                    ImageView imageView2 = (ImageView) inflate.findViewById(C0427R.C0426id.download_indicator_intro_pointer);
                    Intrinsics.checkExpressionValueIsNotNull(imageView2, "pointer");
                    LayoutParams layoutParams3 = imageView2.getLayoutParams();
                    if (layoutParams3 != null) {
                        ConstraintLayout.LayoutParams layoutParams4 = (ConstraintLayout.LayoutParams) layoutParams3;
                        layoutParams4.setMargins(0, 0, ((displayMetrics.widthPixels - (layoutParams2.width / 2)) - (layoutParams4.width / 2)) - iArr[0], 0);
                        findViewById.setOnClickListener(new OnClickListener() {
                            public final void onClick(View view) {
                                findViewById.setVisibility(8);
                            }
                        });
                        imageView.setOnClickListener(new OnClickListener(this) {
                            final /* synthetic */ C0595x1a8f49f3 this$0;

                            public final void onClick(View view) {
                                this.this$0.$targetView.performClick();
                                view = findViewById;
                                if (view != null) {
                                    view.setVisibility(8);
                                }
                            }
                        });
                        imageView.setOnLongClickListener(new OnLongClickListener(this) {
                            final /* synthetic */ C0595x1a8f49f3 this$0;

                            public final boolean onLongClick(View view) {
                                this.this$0.$targetView.performLongClick();
                                view = findViewById;
                                if (view != null) {
                                    view.setVisibility(8);
                                }
                                return false;
                            }
                        });
                        return;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type android.support.constraint.ConstraintLayout.LayoutParams");
                }
                throw new TypeCastException("null cannot be cast to non-null type android.support.constraint.ConstraintLayout.LayoutParams");
            }
            throw new TypeCastException("null cannot be cast to non-null type android.view.WindowManager");
        }
    }
}
