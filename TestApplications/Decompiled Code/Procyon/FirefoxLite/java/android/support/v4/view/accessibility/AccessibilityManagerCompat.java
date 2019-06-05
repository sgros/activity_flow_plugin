// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityManager$TouchExplorationStateChangeListener;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityManager;

public final class AccessibilityManagerCompat
{
    public static boolean addTouchExplorationStateChangeListener(final AccessibilityManager accessibilityManager, final TouchExplorationStateChangeListener touchExplorationStateChangeListener) {
        return Build$VERSION.SDK_INT >= 19 && touchExplorationStateChangeListener != null && accessibilityManager.addTouchExplorationStateChangeListener((AccessibilityManager$TouchExplorationStateChangeListener)new TouchExplorationStateChangeListenerWrapper(touchExplorationStateChangeListener));
    }
    
    public static boolean removeTouchExplorationStateChangeListener(final AccessibilityManager accessibilityManager, final TouchExplorationStateChangeListener touchExplorationStateChangeListener) {
        return Build$VERSION.SDK_INT >= 19 && touchExplorationStateChangeListener != null && accessibilityManager.removeTouchExplorationStateChangeListener((AccessibilityManager$TouchExplorationStateChangeListener)new TouchExplorationStateChangeListenerWrapper(touchExplorationStateChangeListener));
    }
    
    public interface TouchExplorationStateChangeListener
    {
        void onTouchExplorationStateChanged(final boolean p0);
    }
    
    private static class TouchExplorationStateChangeListenerWrapper implements AccessibilityManager$TouchExplorationStateChangeListener
    {
        final TouchExplorationStateChangeListener mListener;
        
        TouchExplorationStateChangeListenerWrapper(final TouchExplorationStateChangeListener mListener) {
            this.mListener = mListener;
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && this.mListener.equals(((TouchExplorationStateChangeListenerWrapper)o).mListener));
        }
        
        @Override
        public int hashCode() {
            return this.mListener.hashCode();
        }
        
        public void onTouchExplorationStateChanged(final boolean b) {
            this.mListener.onTouchExplorationStateChanged(b);
        }
    }
}
