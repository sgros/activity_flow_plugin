package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class SlidingTabView extends LinearLayout {
   private float animateTabXTo = 0.0F;
   private SlidingTabView.SlidingTabViewDelegate delegate;
   private DecelerateInterpolator interpolator;
   private Paint paint = new Paint();
   private int selectedTab = 0;
   private long startAnimationTime = 0L;
   private float startAnimationX = 0.0F;
   private int tabCount = 0;
   private float tabWidth = 0.0F;
   private float tabX = 0.0F;
   private long totalAnimationDiff = 0L;

   public SlidingTabView(Context var1) {
      super(var1);
      this.setOrientation(0);
      this.setWeightSum(100.0F);
      this.paint.setColor(-1);
      this.setWillNotDraw(false);
      this.interpolator = new DecelerateInterpolator();
   }

   private void animateToTab(int var1) {
      this.animateTabXTo = (float)var1 * this.tabWidth;
      this.startAnimationX = this.tabX;
      this.totalAnimationDiff = 0L;
      this.startAnimationTime = System.currentTimeMillis();
      this.invalidate();
   }

   private void didSelectTab(int var1) {
      if (this.selectedTab != var1) {
         this.selectedTab = var1;
         this.animateToTab(var1);
         SlidingTabView.SlidingTabViewDelegate var2 = this.delegate;
         if (var2 != null) {
            var2.didSelectTab(var1);
         }

      }
   }

   public void addTextTab(final int var1, String var2) {
      TextView var3 = new TextView(this.getContext());
      var3.setText(var2);
      var3.setFocusable(true);
      var3.setGravity(17);
      var3.setSingleLine();
      var3.setTextColor(-1);
      var3.setTextSize(1, 14.0F);
      var3.setTypeface(Typeface.DEFAULT_BOLD);
      var3.setBackgroundDrawable(Theme.createSelectorDrawable(-12763843, 0));
      var3.setOnClickListener(new OnClickListener() {
         public void onClick(View var1x) {
            SlidingTabView.this.didSelectTab(var1);
         }
      });
      this.addView(var3);
      LayoutParams var4 = (LayoutParams)var3.getLayoutParams();
      var4.height = -1;
      var4.width = 0;
      var4.weight = 50.0F;
      var3.setLayoutParams(var4);
      ++this.tabCount;
   }

   public int getSeletedTab() {
      return this.selectedTab;
   }

   protected void onDraw(Canvas var1) {
      if (this.tabX != this.animateTabXTo) {
         long var2 = System.currentTimeMillis();
         long var4 = this.startAnimationTime;
         this.startAnimationTime = System.currentTimeMillis();
         this.totalAnimationDiff += var2 - var4;
         var4 = this.totalAnimationDiff;
         if (var4 > 200L) {
            this.totalAnimationDiff = 200L;
            this.tabX = this.animateTabXTo;
         } else {
            this.tabX = this.startAnimationX + this.interpolator.getInterpolation((float)var4 / 200.0F) * (this.animateTabXTo - this.startAnimationX);
            this.invalidate();
         }
      }

      var1.drawRect(this.tabX, (float)(this.getHeight() - AndroidUtilities.dp(2.0F)), this.tabX + this.tabWidth, (float)this.getHeight(), this.paint);
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.tabWidth = (float)(var4 - var2) / (float)this.tabCount;
      float var6 = this.tabWidth * (float)this.selectedTab;
      this.tabX = var6;
      this.animateTabXTo = var6;
   }

   public void setDelegate(SlidingTabView.SlidingTabViewDelegate var1) {
      this.delegate = var1;
   }

   public interface SlidingTabViewDelegate {
      void didSelectTab(int var1);
   }
}
