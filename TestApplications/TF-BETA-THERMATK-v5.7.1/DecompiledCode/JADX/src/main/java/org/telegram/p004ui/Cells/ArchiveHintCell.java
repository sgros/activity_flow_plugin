package org.telegram.p004ui.Cells;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.p004ui.ActionBar.Theme;
import org.telegram.p004ui.Components.BottomPagesView;
import org.telegram.p004ui.Components.LayoutHelper;

/* renamed from: org.telegram.ui.Cells.ArchiveHintCell */
public class ArchiveHintCell extends FrameLayout {
    private BottomPagesView bottomPages;
    private ViewPager viewPager;

    /* renamed from: org.telegram.ui.Cells.ArchiveHintCell$2 */
    class C23412 implements OnPageChangeListener {
        C23412() {
        }

        public void onPageScrolled(int i, float f, int i2) {
            ArchiveHintCell.this.bottomPages.setPageOffset(i, f);
        }

        public void onPageSelected(int i) {
            FileLog.m27d("test1");
        }

        public void onPageScrollStateChanged(int i) {
            FileLog.m27d("test1");
        }
    }

    /* renamed from: org.telegram.ui.Cells.ArchiveHintCell$Adapter */
    private class Adapter extends PagerAdapter {
        public int getCount() {
            return 3;
        }

        public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        }

        public Parcelable saveState() {
            return null;
        }

        private Adapter() {
        }

        /* synthetic */ Adapter(ArchiveHintCell archiveHintCell, C23401 c23401) {
            this();
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            ArchiveHintInnerCell archiveHintInnerCell = new ArchiveHintInnerCell(viewGroup.getContext(), i);
            if (archiveHintInnerCell.getParent() != null) {
                ((ViewGroup) archiveHintInnerCell.getParent()).removeView(archiveHintInnerCell);
            }
            viewGroup.addView(archiveHintInnerCell, 0);
            return archiveHintInnerCell;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }

        public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
            super.setPrimaryItem(viewGroup, i, obj);
            ArchiveHintCell.this.bottomPages.setCurrentPage(i);
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view.equals(obj);
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            if (dataSetObserver != null) {
                super.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }

    public ArchiveHintCell(Context context) {
        super(context);
        this.viewPager = new ViewPager(context) {
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            /* Access modifiers changed, original: protected */
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
                requestLayout();
            }
        };
        AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, Theme.getColor(Theme.key_actionBarDefaultArchived));
        this.viewPager.setAdapter(new Adapter(this, null));
        this.viewPager.setPageMargin(0);
        this.viewPager.setOffscreenPageLimit(1);
        addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0f));
        this.viewPager.addOnPageChangeListener(new C23412());
        this.bottomPages = new BottomPagesView(context, this.viewPager, 3);
        this.bottomPages.setColor(Theme.key_chats_unreadCounterMuted, Theme.key_chats_actionBackground);
        addView(this.bottomPages, LayoutHelper.createFrame(33, 5.0f, 81, 0.0f, 0.0f, 0.0f, 19.0f));
    }

    public void invalidate() {
        super.invalidate();
        this.bottomPages.invalidate();
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(i), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.m26dp(204.0f), 1073741824));
    }
}
