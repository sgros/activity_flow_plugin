package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.secuso.privacyfriendlynetmonitor.Assistant.PrefManager;

public class TutorialActivity extends AppCompatActivity {
   private static boolean tutorial_click;
   private Button btnNext;
   private Button btnSkip;
   private TextView[] dots;
   private LinearLayout dotsLayout;
   private int[] layouts;
   private TutorialActivity.MyViewPagerAdapter myViewPagerAdapter;
   private PrefManager prefManager;
   private ViewPager viewPager;
   ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
      public void onPageScrollStateChanged(int var1) {
      }

      public void onPageScrolled(int var1, float var2, int var3) {
      }

      public void onPageSelected(int var1) {
         TutorialActivity.this.addBottomDots(var1);
         if (var1 == TutorialActivity.this.layouts.length - 1) {
            TutorialActivity.this.btnNext.setText(TutorialActivity.this.getString(2131624035));
            TutorialActivity.this.btnSkip.setText(TutorialActivity.this.getString(2131623999));
         } else {
            TutorialActivity.this.btnNext.setText(TutorialActivity.this.getString(2131624031));
            TutorialActivity.this.btnSkip.setText(TutorialActivity.this.getString(2131624056));
            TutorialActivity.this.btnSkip.setVisibility(0);
         }

      }
   };

   private void addBottomDots(int var1) {
      int[] var2 = this.layouts;
      int var3 = 0;
      this.dots = new TextView[var2.length];
      var2 = this.getResources().getIntArray(2130903040);
      int[] var4 = this.getResources().getIntArray(2130903041);
      this.dotsLayout.removeAllViews();

      while(var3 < this.dots.length) {
         this.dots[var3] = new TextView(this);
         this.dots[var3].setText(Html.fromHtml("&#8226;"));
         this.dots[var3].setTextSize(35.0F);
         this.dots[var3].setTextColor(var4[var1]);
         this.dotsLayout.addView(this.dots[var3]);
         ++var3;
      }

      if (this.dots.length > 0) {
         this.dots[var1].setTextColor(var2[var1]);
      }

   }

   private void changeStatusBarColor() {
      if (VERSION.SDK_INT >= 21) {
         Window var1 = this.getWindow();
         var1.addFlags(Integer.MIN_VALUE);
         var1.setStatusBarColor(0);
      }

   }

   private int getItem(int var1) {
      return this.viewPager.getCurrentItem() + var1;
   }

   private void launchHelp() {
      Intent var1 = new Intent(this, HelpActivity.class);
      var1.setFlags(67108864);
      PrefManager var2 = this.prefManager;
      PrefManager.setFirstTimeLaunch(false);
      this.startActivity(var1);
      this.finish();
   }

   private void launchHomeScreen() {
      Intent var1 = new Intent(this, MainActivity.class);
      PrefManager var2 = this.prefManager;
      PrefManager.setFirstTimeLaunch(false);
      this.startActivity(var1);
      this.finish();
   }

   public static void setTutorial_click(boolean var0) {
      tutorial_click = var0;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (VERSION.SDK_INT >= 21) {
         this.getWindow().getDecorView().setSystemUiVisibility(1280);
      }

      this.setContentView(2131427369);
      this.viewPager = (ViewPager)this.findViewById(2131296536);
      this.dotsLayout = (LinearLayout)this.findViewById(2131296381);
      this.btnSkip = (Button)this.findViewById(2131296308);
      this.btnNext = (Button)this.findViewById(2131296307);
      this.layouts = new int[]{2131427421, 2131427422, 2131427423};
      this.addBottomDots(0);
      this.changeStatusBarColor();
      this.myViewPagerAdapter = new TutorialActivity.MyViewPagerAdapter();
      this.viewPager.setAdapter(this.myViewPagerAdapter);
      this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);
      this.btnSkip.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (TutorialActivity.this.getItem(1) < TutorialActivity.this.layouts.length) {
               TutorialActivity.this.launchHomeScreen();
            } else {
               TutorialActivity.this.launchHelp();
            }

         }
      });
      this.btnNext.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            int var2 = TutorialActivity.this.getItem(1);
            if (var2 < TutorialActivity.this.layouts.length) {
               TutorialActivity.this.viewPager.setCurrentItem(var2);
            } else {
               TutorialActivity.this.launchHomeScreen();
            }

         }
      });
   }

   public class MyViewPagerAdapter extends PagerAdapter {
      private LayoutInflater layoutInflater;

      public void destroyItem(ViewGroup var1, int var2, Object var3) {
         var1.removeView((View)var3);
      }

      public int getCount() {
         return TutorialActivity.this.layouts.length;
      }

      public Object instantiateItem(ViewGroup var1, int var2) {
         this.layoutInflater = (LayoutInflater)TutorialActivity.this.getSystemService("layout_inflater");
         View var3 = this.layoutInflater.inflate(TutorialActivity.this.layouts[var2], var1, false);
         var1.addView(var3);
         return var3;
      }

      public boolean isViewFromObject(View var1, Object var2) {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }
   }
}
