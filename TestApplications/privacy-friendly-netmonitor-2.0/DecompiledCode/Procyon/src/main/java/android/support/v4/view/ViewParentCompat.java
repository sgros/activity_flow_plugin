// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.view;

import android.util.Log;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.view.ViewParent;
import android.os.Build$VERSION;

public final class ViewParentCompat
{
    static final ViewParentCompatBaseImpl IMPL;
    private static final String TAG = "ViewParentCompat";
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (ViewParentCompatBaseImpl)new ViewParentCompatApi21Impl();
        }
        else if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (ViewParentCompatBaseImpl)new ViewParentCompatApi19Impl();
        }
        else {
            IMPL = new ViewParentCompatBaseImpl();
        }
    }
    
    private ViewParentCompat() {
    }
    
    public static void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        ViewParentCompat.IMPL.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, n);
    }
    
    public static boolean onNestedFling(final ViewParent viewParent, final View view, final float n, final float n2, final boolean b) {
        return ViewParentCompat.IMPL.onNestedFling(viewParent, view, n, n2, b);
    }
    
    public static boolean onNestedPreFling(final ViewParent viewParent, final View view, final float n, final float n2) {
        return ViewParentCompat.IMPL.onNestedPreFling(viewParent, view, n, n2);
    }
    
    public static void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
        onNestedPreScroll(viewParent, view, n, n2, array, 0);
    }
    
    public static void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array, final int n3) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedPreScroll(view, n, n2, array, n3);
        }
        else if (n3 == 0) {
            ViewParentCompat.IMPL.onNestedPreScroll(viewParent, view, n, n2, array);
        }
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
        onNestedScroll(viewParent, view, n, n2, n3, n4, 0);
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedScroll(view, n, n2, n3, n4, n5);
        }
        else if (n5 == 0) {
            ViewParentCompat.IMPL.onNestedScroll(viewParent, view, n, n2, n3, n4);
        }
    }
    
    public static void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
        onNestedScrollAccepted(viewParent, view, view2, n, 0);
    }
    
    public static void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n, final int n2) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedScrollAccepted(view, view2, n, n2);
        }
        else if (n2 == 0) {
            ViewParentCompat.IMPL.onNestedScrollAccepted(viewParent, view, view2, n);
        }
    }
    
    public static boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
        return onStartNestedScroll(viewParent, view, view2, n, 0);
    }
    
    public static boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n, final int n2) {
        if (viewParent instanceof NestedScrollingParent2) {
            return ((NestedScrollingParent2)viewParent).onStartNestedScroll(view, view2, n, n2);
        }
        return n2 == 0 && ViewParentCompat.IMPL.onStartNestedScroll(viewParent, view, view2, n);
    }
    
    public static void onStopNestedScroll(final ViewParent viewParent, final View view) {
        onStopNestedScroll(viewParent, view, 0);
    }
    
    public static void onStopNestedScroll(final ViewParent viewParent, final View view, final int n) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onStopNestedScroll(view, n);
        }
        else if (n == 0) {
            ViewParentCompat.IMPL.onStopNestedScroll(viewParent, view);
        }
    }
    
    @Deprecated
    public static boolean requestSendAccessibilityEvent(final ViewParent viewParent, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewParent.requestSendAccessibilityEvent(view, accessibilityEvent);
    }
    
    @RequiresApi(19)
    static class ViewParentCompatApi19Impl extends ViewParentCompatBaseImpl
    {
        @Override
        public void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, n);
        }
    }
    
    @RequiresApi(21)
    static class ViewParentCompatApi21Impl extends ViewParentCompatApi19Impl
    {
        @Override
        public boolean onNestedFling(final ViewParent obj, final View view, final float n, final float n2, final boolean b) {
            try {
                return obj.onNestedFling(view, n, n2, b);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedFling");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        
        @Override
        public boolean onNestedPreFling(final ViewParent obj, final View view, final float n, final float n2) {
            try {
                return obj.onNestedPreFling(view, n, n2);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedPreFling");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        
        @Override
        public void onNestedPreScroll(final ViewParent obj, final View view, final int n, final int n2, final int[] array) {
            try {
                obj.onNestedPreScroll(view, n, n2, array);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedPreScroll");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
            }
        }
        
        @Override
        public void onNestedScroll(final ViewParent obj, final View view, final int n, final int n2, final int n3, final int n4) {
            try {
                obj.onNestedScroll(view, n, n2, n3, n4);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedScroll");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
            }
        }
        
        @Override
        public void onNestedScrollAccepted(final ViewParent obj, final View view, final View view2, final int n) {
            try {
                obj.onNestedScrollAccepted(view, view2, n);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onNestedScrollAccepted");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
            }
        }
        
        @Override
        public boolean onStartNestedScroll(final ViewParent obj, final View view, final View view2, final int n) {
            try {
                return obj.onStartNestedScroll(view, view2, n);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onStartNestedScroll");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
                return false;
            }
        }
        
        @Override
        public void onStopNestedScroll(final ViewParent obj, final View view) {
            try {
                obj.onStopNestedScroll(view);
            }
            catch (AbstractMethodError abstractMethodError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ViewParent ");
                sb.append(obj);
                sb.append(" does not implement interface ");
                sb.append("method onStopNestedScroll");
                Log.e("ViewParentCompat", sb.toString(), (Throwable)abstractMethodError);
            }
        }
    }
    
    static class ViewParentCompatBaseImpl
    {
        public void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        }
        
        public boolean onNestedFling(final ViewParent viewParent, final View view, final float n, final float n2, final boolean b) {
            return viewParent instanceof NestedScrollingParent && ((NestedScrollingParent)viewParent).onNestedFling(view, n, n2, b);
        }
        
        public boolean onNestedPreFling(final ViewParent viewParent, final View view, final float n, final float n2) {
            return viewParent instanceof NestedScrollingParent && ((NestedScrollingParent)viewParent).onNestedPreFling(view, n, n2);
        }
        
        public void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedPreScroll(view, n, n2, array);
            }
        }
        
        public void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScroll(view, n, n2, n3, n4);
            }
        }
        
        public void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScrollAccepted(view, view2, n);
            }
        }
        
        public boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
            return viewParent instanceof NestedScrollingParent && ((NestedScrollingParent)viewParent).onStartNestedScroll(view, view2, n);
        }
        
        public void onStopNestedScroll(final ViewParent viewParent, final View view) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onStopNestedScroll(view);
            }
        }
    }
}
