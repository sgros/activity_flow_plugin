package android.support.p001v4.view;

import android.view.MotionEvent;

/* renamed from: android.support.v4.view.MotionEventCompat */
public final class MotionEventCompat {
    @Deprecated
    public static int getActionMasked(MotionEvent motionEvent) {
        return motionEvent.getActionMasked();
    }

    public static boolean isFromSource(MotionEvent motionEvent, int i) {
        return (motionEvent.getSource() & i) == i;
    }
}
