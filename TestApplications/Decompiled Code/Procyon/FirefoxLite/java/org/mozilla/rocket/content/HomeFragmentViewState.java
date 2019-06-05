// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.content;

public final class HomeFragmentViewState
{
    public static final HomeFragmentViewState INSTANCE;
    private static Integer lastScrollPos;
    private static int state;
    
    static {
        INSTANCE = new HomeFragmentViewState();
    }
    
    private HomeFragmentViewState() {
    }
    
    public static final void lastOpenNews() {
        HomeFragmentViewState.state = 1;
    }
    
    public static final void reset() {
        HomeFragmentViewState.state = 0;
        HomeFragmentViewState.lastScrollPos = null;
    }
    
    public final Integer getLastScrollPos() {
        return HomeFragmentViewState.lastScrollPos;
    }
    
    public final boolean isLastOpenNews() {
        final int state = HomeFragmentViewState.state;
        boolean b = true;
        if (state != 1) {
            b = false;
        }
        return b;
    }
    
    public final void setLastScrollPos(final Integer lastScrollPos) {
        HomeFragmentViewState.lastScrollPos = lastScrollPos;
    }
}
