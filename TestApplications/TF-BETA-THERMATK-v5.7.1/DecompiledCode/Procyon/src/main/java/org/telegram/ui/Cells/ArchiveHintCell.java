// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Cells;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.ViewGroup;
import android.view.View$MeasureSpec;
import org.telegram.messenger.FileLog;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import androidx.viewpager.widget.PagerAdapter;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import android.view.MotionEvent;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import org.telegram.ui.Components.BottomPagesView;
import android.widget.FrameLayout;

public class ArchiveHintCell extends FrameLayout
{
    private BottomPagesView bottomPages;
    private ViewPager viewPager;
    
    public ArchiveHintCell(final Context context) {
        super(context);
        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager = new ViewPager(context) {
            @Override
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                this.requestLayout();
            }
            
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                if (this.getParent() != null) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onInterceptTouchEvent(motionEvent);
            }
        }, Theme.getColor("actionBarDefaultArchived"));
        this.viewPager.setAdapter(new Adapter());
        this.viewPager.setPageMargin(0);
        this.viewPager.setOffscreenPageLimit(1);
        this.addView((View)this.viewPager, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
                FileLog.d("test1");
            }
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
                ArchiveHintCell.this.bottomPages.setPageOffset(n, n2);
            }
            
            @Override
            public void onPageSelected(final int n) {
                FileLog.d("test1");
            }
        });
        (this.bottomPages = new BottomPagesView(context, this.viewPager, 3)).setColor("chats_unreadCounterMuted", "chats_actionBackground");
        this.addView((View)this.bottomPages, (ViewGroup$LayoutParams)LayoutHelper.createFrame(33, 5.0f, 81, 0.0f, 0.0f, 0.0f, 19.0f));
    }
    
    public ViewPager getViewPager() {
        return this.viewPager;
    }
    
    public void invalidate() {
        super.invalidate();
        this.bottomPages.invalidate();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n), 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(204.0f), 1073741824));
    }
    
    private class Adapter extends PagerAdapter
    {
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        @Override
        public int getCount() {
            return 3;
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            final ArchiveHintInnerCell archiveHintInnerCell = new ArchiveHintInnerCell(viewGroup.getContext(), n);
            if (archiveHintInnerCell.getParent() != null) {
                ((ViewGroup)archiveHintInnerCell.getParent()).removeView((View)archiveHintInnerCell);
            }
            viewGroup.addView((View)archiveHintInnerCell, 0);
            return archiveHintInnerCell;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object obj) {
            return view.equals(obj);
        }
        
        @Override
        public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
        }
        
        @Override
        public Parcelable saveState() {
            return null;
        }
        
        @Override
        public void setPrimaryItem(final ViewGroup viewGroup, final int currentPage, final Object o) {
            super.setPrimaryItem(viewGroup, currentPage, o);
            ArchiveHintCell.this.bottomPages.setCurrentPage(currentPage);
        }
        
        @Override
        public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
            if (dataSetObserver != null) {
                super.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }
}
