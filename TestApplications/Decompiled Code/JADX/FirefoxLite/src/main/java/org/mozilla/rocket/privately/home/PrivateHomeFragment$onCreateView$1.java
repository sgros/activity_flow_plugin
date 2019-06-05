package org.mozilla.rocket.privately.home;

import android.support.p001v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener.TYPE;

/* compiled from: PrivateHomeFragment.kt */
final class PrivateHomeFragment$onCreateView$1 implements OnClickListener {
    final /* synthetic */ PrivateHomeFragment this$0;

    PrivateHomeFragment$onCreateView$1(PrivateHomeFragment privateHomeFragment) {
        this.this$0 = privateHomeFragment;
    }

    public final void onClick(View view) {
        FragmentActivity activity = this.this$0.getActivity();
        if (activity instanceof FragmentListener) {
            ((FragmentListener) activity).onNotified(this.this$0, TYPE.TOGGLE_PRIVATE_MODE, null);
        }
    }
}
