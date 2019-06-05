package org.mozilla.rocket.content;

/* compiled from: HomeFragmentViewState.kt */
public final class HomeFragmentViewState {
    public static final HomeFragmentViewState INSTANCE = new HomeFragmentViewState();
    private static Integer lastScrollPos;
    private static int state;

    private HomeFragmentViewState() {
    }

    public final Integer getLastScrollPos() {
        return lastScrollPos;
    }

    public final void setLastScrollPos(Integer num) {
        lastScrollPos = num;
    }

    public static final void reset() {
        state = 0;
        lastScrollPos = (Integer) null;
    }

    public static final void lastOpenNews() {
        state = 1;
    }

    public final boolean isLastOpenNews() {
        return state == 1;
    }
}
