package org.mozilla.focus.utils;

public interface OnSwipeListener {

    /* renamed from: org.mozilla.focus.utils.OnSwipeListener$-CC */
    public final /* synthetic */ class C0538-CC {
        public static boolean $default$onDoubleTap(OnSwipeListener onSwipeListener) {
            return false;
        }

        public static void $default$onLongPress(OnSwipeListener onSwipeListener) {
        }

        public static boolean $default$onSingleTapConfirmed(OnSwipeListener onSwipeListener) {
            return false;
        }

        public static void $default$onSwipeDown(OnSwipeListener onSwipeListener) {
        }

        public static void $default$onSwipeLeft(OnSwipeListener onSwipeListener) {
        }

        public static void $default$onSwipeRight(OnSwipeListener onSwipeListener) {
        }

        public static void $default$onSwipeUp(OnSwipeListener onSwipeListener) {
        }
    }

    boolean onDoubleTap();

    void onLongPress();

    boolean onSingleTapConfirmed();

    void onSwipeDown();

    void onSwipeLeft();

    void onSwipeRight();

    void onSwipeUp();
}
