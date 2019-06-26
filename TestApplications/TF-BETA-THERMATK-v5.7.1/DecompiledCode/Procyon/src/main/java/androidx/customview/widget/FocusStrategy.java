// 
// Decompiled by Procyon v0.5.34
// 

package androidx.customview.widget;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import android.graphics.Rect;

class FocusStrategy
{
    private static boolean beamBeats(final int n, final Rect rect, final Rect rect2, final Rect rect3) {
        final boolean beamsOverlap = beamsOverlap(n, rect, rect2);
        if (beamsOverlap(n, rect, rect3) || !beamsOverlap) {
            return false;
        }
        final boolean toDirection = isToDirectionOf(n, rect, rect3);
        final boolean b = true;
        if (!toDirection) {
            return true;
        }
        boolean b2 = b;
        if (n != 17) {
            if (n == 66) {
                b2 = b;
            }
            else {
                b2 = (majorAxisDistance(n, rect, rect2) < majorAxisDistanceToFarEdge(n, rect, rect3) && b);
            }
        }
        return b2;
    }
    
    private static boolean beamsOverlap(final int n, final Rect rect, final Rect rect2) {
        final boolean b = true;
        boolean b2 = true;
        if (n != 17) {
            if (n != 33) {
                if (n == 66) {
                    return rect2.bottom >= rect.top && rect2.top <= rect.bottom && b;
                }
                if (n != 130) {
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
            }
            if (rect2.right < rect.left || rect2.left > rect.right) {
                b2 = false;
            }
            return b2;
        }
        return rect2.bottom >= rect.top && rect2.top <= rect.bottom && b;
    }
    
    public static <L, T> T findNextFocusInAbsoluteDirection(final L l, final CollectionAdapter<L, T> collectionAdapter, final BoundsAdapter<T> boundsAdapter, final T t, final Rect rect, final int n) {
        final Rect rect2 = new Rect(rect);
        int i = 0;
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                    rect2.offset(0, -(rect.height() + 1));
                }
                else {
                    rect2.offset(-(rect.width() + 1), 0);
                }
            }
            else {
                rect2.offset(0, rect.height() + 1);
            }
        }
        else {
            rect2.offset(rect.width() + 1, 0);
        }
        T t2 = null;
        final int size = collectionAdapter.size(l);
        final Rect rect3 = new Rect();
        while (i < size) {
            final T value = collectionAdapter.get(l, i);
            if (value != t) {
                boundsAdapter.obtainBounds(value, rect3);
                if (isBetterCandidate(n, rect, rect3, rect2)) {
                    rect2.set(rect3);
                    t2 = value;
                }
            }
            ++i;
        }
        return t2;
    }
    
    public static <L, T> T findNextFocusInRelativeDirection(final L l, final CollectionAdapter<L, T> collectionAdapter, final BoundsAdapter<T> boundsAdapter, final T t, final int n, final boolean b, final boolean b2) {
        final int size = collectionAdapter.size(l);
        final ArrayList list = new ArrayList<T>(size);
        for (int i = 0; i < size; ++i) {
            list.add(collectionAdapter.get(l, i));
        }
        Collections.sort((List<T>)list, new SequentialComparator<Object>(b, (BoundsAdapter<? super T>)boundsAdapter));
        if (n == 1) {
            return getPreviousFocusable(t, (ArrayList<T>)list, b2);
        }
        if (n == 2) {
            return getNextFocusable(t, (ArrayList<T>)list, b2);
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
    }
    
    private static <T> T getNextFocusable(final T o, final ArrayList<T> list, final boolean b) {
        final int size = list.size();
        int lastIndex;
        if (o == null) {
            lastIndex = -1;
        }
        else {
            lastIndex = list.lastIndexOf(o);
        }
        if (++lastIndex < size) {
            return list.get(lastIndex);
        }
        if (b && size > 0) {
            return list.get(0);
        }
        return null;
    }
    
    private static <T> T getPreviousFocusable(final T o, final ArrayList<T> list, final boolean b) {
        final int size = list.size();
        int index;
        if (o == null) {
            index = size;
        }
        else {
            index = list.indexOf(o);
        }
        if (--index >= 0) {
            return list.get(index);
        }
        if (b && size > 0) {
            return list.get(size - 1);
        }
        return null;
    }
    
    private static int getWeightedDistanceFor(final int n, final int n2) {
        return n * 13 * n + n2 * n2;
    }
    
    private static boolean isBetterCandidate(final int n, final Rect rect, final Rect rect2, final Rect rect3) {
        final boolean candidate = isCandidate(rect, rect2, n);
        boolean b = false;
        if (!candidate) {
            return false;
        }
        if (!isCandidate(rect, rect3, n)) {
            return true;
        }
        if (beamBeats(n, rect, rect2, rect3)) {
            return true;
        }
        if (beamBeats(n, rect, rect3, rect2)) {
            return false;
        }
        if (getWeightedDistanceFor(majorAxisDistance(n, rect, rect2), minorAxisDistance(n, rect, rect2)) < getWeightedDistanceFor(majorAxisDistance(n, rect, rect3), minorAxisDistance(n, rect, rect3))) {
            b = true;
        }
        return b;
    }
    
    private static boolean isCandidate(final Rect rect, final Rect rect2, int n) {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        boolean b4 = true;
        if (n == 17) {
            final int right = rect.right;
            n = rect2.right;
            return (right > n || rect.left >= n) && rect.left > rect2.left && b3;
        }
        if (n == 33) {
            n = rect.bottom;
            final int bottom = rect2.bottom;
            return (n > bottom || rect.top >= bottom) && rect.top > rect2.top && b2;
        }
        if (n == 66) {
            n = rect.left;
            final int left = rect2.left;
            return (n < left || rect.right <= left) && rect.right < rect2.right && b;
        }
        if (n == 130) {
            n = rect.top;
            final int top = rect2.top;
            if ((n >= top && rect.bottom > top) || rect.bottom >= rect2.bottom) {
                b4 = false;
            }
            return b4;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }
    
    private static boolean isToDirectionOf(final int n, final Rect rect, final Rect rect2) {
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        boolean b4 = true;
        if (n == 17) {
            return rect.left >= rect2.right && b3;
        }
        if (n == 33) {
            return rect.top >= rect2.bottom && b2;
        }
        if (n == 66) {
            return rect.right <= rect2.left && b;
        }
        if (n == 130) {
            if (rect.bottom > rect2.top) {
                b4 = false;
            }
            return b4;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }
    
    private static int majorAxisDistance(final int n, final Rect rect, final Rect rect2) {
        return Math.max(0, majorAxisDistanceRaw(n, rect, rect2));
    }
    
    private static int majorAxisDistanceRaw(int n, final Rect rect, final Rect rect2) {
        int n2;
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                    n = rect2.top;
                    n2 = rect.bottom;
                }
                else {
                    n = rect2.left;
                    n2 = rect.right;
                }
            }
            else {
                n = rect.top;
                n2 = rect2.bottom;
            }
        }
        else {
            n = rect.left;
            n2 = rect2.right;
        }
        return n - n2;
    }
    
    private static int majorAxisDistanceToFarEdge(final int n, final Rect rect, final Rect rect2) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(n, rect, rect2));
    }
    
    private static int majorAxisDistanceToFarEdgeRaw(int n, final Rect rect, final Rect rect2) {
        int n2;
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                    n = rect2.bottom;
                    n2 = rect.bottom;
                }
                else {
                    n = rect2.right;
                    n2 = rect.right;
                }
            }
            else {
                n = rect.top;
                n2 = rect2.top;
            }
        }
        else {
            n = rect.left;
            n2 = rect2.left;
        }
        return n - n2;
    }
    
    private static int minorAxisDistance(final int n, final Rect rect, final Rect rect2) {
        if (n != 17) {
            if (n != 33) {
                if (n == 66) {
                    return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
                }
                if (n != 130) {
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
            }
            return Math.abs(rect.left + rect.width() / 2 - (rect2.left + rect2.width() / 2));
        }
        return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
    }
    
    public interface BoundsAdapter<T>
    {
        void obtainBounds(final T p0, final Rect p1);
    }
    
    public interface CollectionAdapter<T, V>
    {
        V get(final T p0, final int p1);
        
        int size(final T p0);
    }
    
    private static class SequentialComparator<T> implements Comparator<T>
    {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1;
        private final Rect mTemp2;
        
        SequentialComparator(final boolean mIsLayoutRtl, final BoundsAdapter<T> mAdapter) {
            this.mTemp1 = new Rect();
            this.mTemp2 = new Rect();
            this.mIsLayoutRtl = mIsLayoutRtl;
            this.mAdapter = mAdapter;
        }
        
        @Override
        public int compare(final T t, final T t2) {
            final Rect mTemp1 = this.mTemp1;
            final Rect mTemp2 = this.mTemp2;
            this.mAdapter.obtainBounds(t, mTemp1);
            this.mAdapter.obtainBounds(t2, mTemp2);
            final int top = mTemp1.top;
            final int top2 = mTemp2.top;
            int n = -1;
            if (top < top2) {
                return -1;
            }
            if (top > top2) {
                return 1;
            }
            final int left = mTemp1.left;
            final int left2 = mTemp2.left;
            if (left < left2) {
                if (this.mIsLayoutRtl) {
                    n = 1;
                }
                return n;
            }
            if (left > left2) {
                if (!this.mIsLayoutRtl) {
                    n = 1;
                }
                return n;
            }
            final int bottom = mTemp1.bottom;
            final int bottom2 = mTemp2.bottom;
            if (bottom < bottom2) {
                return -1;
            }
            if (bottom > bottom2) {
                return 1;
            }
            final int right = mTemp1.right;
            final int right2 = mTemp2.right;
            if (right < right2) {
                if (this.mIsLayoutRtl) {
                    n = 1;
                }
                return n;
            }
            if (right > right2) {
                if (!this.mIsLayoutRtl) {
                    n = 1;
                }
                return n;
            }
            return 0;
        }
    }
}
