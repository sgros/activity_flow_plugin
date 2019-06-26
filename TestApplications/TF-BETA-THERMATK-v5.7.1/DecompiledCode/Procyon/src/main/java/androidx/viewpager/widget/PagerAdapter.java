// 
// Decompiled by Procyon v0.5.34
// 

package androidx.viewpager.widget;

import android.os.Parcelable;
import android.view.ViewGroup;
import android.view.View;
import android.database.DataSetObserver;
import android.database.DataSetObservable;

public abstract class PagerAdapter
{
    public static final int POSITION_NONE = -2;
    public static final int POSITION_UNCHANGED = -1;
    private final DataSetObservable mObservable;
    private DataSetObserver mViewPagerObserver;
    
    public PagerAdapter() {
        this.mObservable = new DataSetObservable();
    }
    
    @Deprecated
    public void destroyItem(final View view, final int n, final Object o) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }
    
    public abstract void destroyItem(final ViewGroup p0, final int p1, final Object p2);
    
    @Deprecated
    public void finishUpdate(final View view) {
    }
    
    public void finishUpdate(final ViewGroup viewGroup) {
        this.finishUpdate((View)viewGroup);
    }
    
    public abstract int getCount();
    
    public int getItemPosition(final Object o) {
        return -1;
    }
    
    public CharSequence getPageTitle(final int n) {
        return null;
    }
    
    public float getPageWidth(final int n) {
        return 1.0f;
    }
    
    @Deprecated
    public Object instantiateItem(final View view, final int n) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }
    
    public abstract Object instantiateItem(final ViewGroup p0, final int p1);
    
    public abstract boolean isViewFromObject(final View p0, final Object p1);
    
    public void notifyDataSetChanged() {
        synchronized (this) {
            if (this.mViewPagerObserver != null) {
                this.mViewPagerObserver.onChanged();
            }
            // monitorexit(this)
            this.mObservable.notifyChanged();
        }
    }
    
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        this.mObservable.registerObserver((Object)dataSetObserver);
    }
    
    public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
    }
    
    public Parcelable saveState() {
        return null;
    }
    
    @Deprecated
    public void setPrimaryItem(final View view, final int n, final Object o) {
    }
    
    public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
        this.setPrimaryItem((View)viewGroup, n, o);
    }
    
    void setViewPagerObserver(final DataSetObserver mViewPagerObserver) {
        synchronized (this) {
            this.mViewPagerObserver = mViewPagerObserver;
        }
    }
    
    @Deprecated
    public void startUpdate(final View view) {
    }
    
    public void startUpdate(final ViewGroup viewGroup) {
        this.startUpdate((View)viewGroup);
    }
    
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
        this.mObservable.unregisterObserver((Object)dataSetObserver);
    }
}
