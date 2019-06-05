package org.mozilla.focus.fragment;

import android.content.Intent;
import android.support.p001v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.periodic.FirstLaunchWorker;
import org.mozilla.rocket.periodic.PeriodicReceiver;

/* compiled from: FirstrunFragment.kt */
final class FirstrunFragment$wrapButtonClickListener$1 implements OnClickListener {
    final /* synthetic */ OnClickListener $onClickListener;
    final /* synthetic */ FirstrunFragment this$0;

    FirstrunFragment$wrapButtonClickListener$1(FirstrunFragment firstrunFragment, OnClickListener onClickListener) {
        this.this$0 = firstrunFragment;
        this.$onClickListener = onClickListener;
    }

    public final void onClick(View view) {
        Intrinsics.checkExpressionValueIsNotNull(view, "view");
        if (view.getId() == C0427R.C0426id.finish) {
            FragmentActivity activity = this.this$0.getActivity();
            if (activity != null) {
                Intent intent = new Intent(this.this$0.getActivity(), PeriodicReceiver.class);
                intent.setAction(FirstLaunchWorker.Companion.getACTION());
                activity.sendBroadcast(intent);
            }
        }
        this.$onClickListener.onClick(view);
    }
}
