package org.telegram.ui.Cells;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BottomPagesView;
import org.telegram.ui.Components.LayoutHelper;

public class ArchiveHintCell extends FrameLayout {
   private BottomPagesView bottomPages;
   private ViewPager viewPager;

   public ArchiveHintCell(Context var1) {
      super(var1);
      this.viewPager = new ViewPager(var1) {
         protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.requestLayout();
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (this.getParent() != null) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            return super.onInterceptTouchEvent(var1);
         }
      };
      AndroidUtilities.setViewPagerEdgeEffectColor(this.viewPager, Theme.getColor("actionBarDefaultArchived"));
      this.viewPager.setAdapter(new ArchiveHintCell.Adapter());
      this.viewPager.setPageMargin(0);
      this.viewPager.setOffscreenPageLimit(1);
      this.addView(this.viewPager, LayoutHelper.createFrame(-1, -1.0F));
      this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         public void onPageScrollStateChanged(int var1) {
            FileLog.d("test1");
         }

         public void onPageScrolled(int var1, float var2, int var3) {
            ArchiveHintCell.this.bottomPages.setPageOffset(var1, var2);
         }

         public void onPageSelected(int var1) {
            FileLog.d("test1");
         }
      });
      this.bottomPages = new BottomPagesView(var1, this.viewPager, 3);
      this.bottomPages.setColor("chats_unreadCounterMuted", "chats_actionBackground");
      this.addView(this.bottomPages, LayoutHelper.createFrame(33, 5.0F, 81, 0.0F, 0.0F, 0.0F, 19.0F));
   }

   public ViewPager getViewPager() {
      return this.viewPager;
   }

   public void invalidate() {
      super.invalidate();
      this.bottomPages.invalidate();
   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(204.0F), 1073741824));
   }

   private class Adapter extends PagerAdapter {
      private Adapter() {
      }

      // $FF: synthetic method
      Adapter(Object var2) {
         this();
      }

      public void destroyItem(ViewGroup var1, int var2, Object var3) {
         var1.removeView((View)var3);
      }

      public int getCount() {
         return 3;
      }

      public Object instantiateItem(ViewGroup var1, int var2) {
         ArchiveHintInnerCell var3 = new ArchiveHintInnerCell(var1.getContext(), var2);
         if (var3.getParent() != null) {
            ((ViewGroup)var3.getParent()).removeView(var3);
         }

         var1.addView(var3, 0);
         return var3;
      }

      public boolean isViewFromObject(View var1, Object var2) {
         return var1.equals(var2);
      }

      public void restoreState(Parcelable var1, ClassLoader var2) {
      }

      public Parcelable saveState() {
         return null;
      }

      public void setPrimaryItem(ViewGroup var1, int var2, Object var3) {
         super.setPrimaryItem(var1, var2, var3);
         ArchiveHintCell.this.bottomPages.setCurrentPage(var2);
      }

      public void unregisterDataSetObserver(DataSetObserver var1) {
         if (var1 != null) {
            super.unregisterDataSetObserver(var1);
         }

      }
   }
}
