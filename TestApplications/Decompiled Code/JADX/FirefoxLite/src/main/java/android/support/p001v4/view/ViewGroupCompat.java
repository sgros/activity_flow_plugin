package android.support.p001v4.view;

import android.os.Build.VERSION;
import android.support.compat.C0017R;
import android.view.ViewGroup;

/* renamed from: android.support.v4.view.ViewGroupCompat */
public final class ViewGroupCompat {
    public static boolean isTransitionGroup(ViewGroup viewGroup) {
        if (VERSION.SDK_INT >= 21) {
            return viewGroup.isTransitionGroup();
        }
        Boolean bool = (Boolean) viewGroup.getTag(C0017R.C0016id.tag_transition_group);
        boolean z = ((bool == null || !bool.booleanValue()) && viewGroup.getBackground() == null && ViewCompat.getTransitionName(viewGroup) == null) ? false : true;
        return z;
    }
}
