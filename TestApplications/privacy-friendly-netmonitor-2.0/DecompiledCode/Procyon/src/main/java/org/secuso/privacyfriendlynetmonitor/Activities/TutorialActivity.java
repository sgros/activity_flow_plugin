// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.Activities;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View$OnClickListener;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.os.Build$VERSION;
import android.view.View;
import android.text.Html;
import android.content.Context;
import android.support.v4.view.ViewPager;
import org.secuso.privacyfriendlynetmonitor.Assistant.PrefManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

public class TutorialActivity extends AppCompatActivity
{
    private static boolean tutorial_click = false;
    private Button btnNext;
    private Button btnSkip;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private PrefManager prefManager;
    private ViewPager viewPager;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener;
    
    public TutorialActivity() {
        this.viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int n) {
            }
            
            @Override
            public void onPageScrolled(final int n, final float n2, final int n3) {
            }
            
            @Override
            public void onPageSelected(final int n) {
                TutorialActivity.this.addBottomDots(n);
                if (n == TutorialActivity.this.layouts.length - 1) {
                    TutorialActivity.this.btnNext.setText((CharSequence)TutorialActivity.this.getString(2131624035));
                    TutorialActivity.this.btnSkip.setText((CharSequence)TutorialActivity.this.getString(2131623999));
                }
                else {
                    TutorialActivity.this.btnNext.setText((CharSequence)TutorialActivity.this.getString(2131624031));
                    TutorialActivity.this.btnSkip.setText((CharSequence)TutorialActivity.this.getString(2131624056));
                    TutorialActivity.this.btnSkip.setVisibility(0);
                }
            }
        };
    }
    
    private void addBottomDots(final int n) {
        final int[] layouts = this.layouts;
        int i = 0;
        this.dots = new TextView[layouts.length];
        final int[] intArray = this.getResources().getIntArray(2130903040);
        final int[] intArray2 = this.getResources().getIntArray(2130903041);
        this.dotsLayout.removeAllViews();
        while (i < this.dots.length) {
            (this.dots[i] = new TextView((Context)this)).setText((CharSequence)Html.fromHtml("&#8226;"));
            this.dots[i].setTextSize(35.0f);
            this.dots[i].setTextColor(intArray2[n]);
            this.dotsLayout.addView((View)this.dots[i]);
            ++i;
        }
        if (this.dots.length > 0) {
            this.dots[n].setTextColor(intArray[n]);
        }
    }
    
    private void changeStatusBarColor() {
        if (Build$VERSION.SDK_INT >= 21) {
            final Window window = this.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }
    
    private int getItem(final int n) {
        return this.viewPager.getCurrentItem() + n;
    }
    
    private void launchHelp() {
        final Intent intent = new Intent((Context)this, (Class)HelpActivity.class);
        intent.setFlags(67108864);
        final PrefManager prefManager = this.prefManager;
        PrefManager.setFirstTimeLaunch(false);
        this.startActivity(intent);
        this.finish();
    }
    
    private void launchHomeScreen() {
        final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
        final PrefManager prefManager = this.prefManager;
        PrefManager.setFirstTimeLaunch(false);
        this.startActivity(intent);
        this.finish();
    }
    
    public static void setTutorial_click(final boolean tutorial_click) {
        TutorialActivity.tutorial_click = tutorial_click;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (Build$VERSION.SDK_INT >= 21) {
            this.getWindow().getDecorView().setSystemUiVisibility(1280);
        }
        this.setContentView(2131427369);
        this.viewPager = this.findViewById(2131296536);
        this.dotsLayout = this.findViewById(2131296381);
        this.btnSkip = this.findViewById(2131296308);
        this.btnNext = this.findViewById(2131296307);
        this.layouts = new int[] { 2131427421, 2131427422, 2131427423 };
        this.addBottomDots(0);
        this.changeStatusBarColor();
        this.myViewPagerAdapter = new MyViewPagerAdapter();
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);
        this.btnSkip.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (TutorialActivity.this.getItem(1) < TutorialActivity.this.layouts.length) {
                    TutorialActivity.this.launchHomeScreen();
                }
                else {
                    TutorialActivity.this.launchHelp();
                }
            }
        });
        this.btnNext.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final int access$000 = TutorialActivity.this.getItem(1);
                if (access$000 < TutorialActivity.this.layouts.length) {
                    TutorialActivity.this.viewPager.setCurrentItem(access$000);
                }
                else {
                    TutorialActivity.this.launchHomeScreen();
                }
            }
        });
    }
    
    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        @Override
        public int getCount() {
            return TutorialActivity.this.layouts.length;
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            this.layoutInflater = (LayoutInflater)TutorialActivity.this.getSystemService("layout_inflater");
            final View inflate = this.layoutInflater.inflate(TutorialActivity.this.layouts[n], viewGroup, false);
            viewGroup.addView(inflate);
            return inflate;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object o) {
            return view == o;
        }
    }
}
