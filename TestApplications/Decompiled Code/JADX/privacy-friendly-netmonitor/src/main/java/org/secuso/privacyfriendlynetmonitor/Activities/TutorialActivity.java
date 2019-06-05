package org.secuso.privacyfriendlynetmonitor.Activities;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p000v4.view.PagerAdapter;
import android.support.p000v4.view.ViewPager;
import android.support.p000v4.view.ViewPager.OnPageChangeListener;
import android.support.p003v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.secuso.privacyfriendlynetmonitor.Assistant.PrefManager;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class TutorialActivity extends AppCompatActivity {
    private static boolean tutorial_click = false;
    private Button btnNext;
    private Button btnSkip;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private PrefManager prefManager;
    private ViewPager viewPager;
    OnPageChangeListener viewPagerPageChangeListener = new C05533();

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.TutorialActivity$1 */
    class C04941 implements OnClickListener {
        C04941() {
        }

        public void onClick(View view) {
            if (TutorialActivity.this.getItem(1) < TutorialActivity.this.layouts.length) {
                TutorialActivity.this.launchHomeScreen();
            } else {
                TutorialActivity.this.launchHelp();
            }
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.TutorialActivity$2 */
    class C04952 implements OnClickListener {
        C04952() {
        }

        public void onClick(View view) {
            int access$000 = TutorialActivity.this.getItem(1);
            if (access$000 < TutorialActivity.this.layouts.length) {
                TutorialActivity.this.viewPager.setCurrentItem(access$000);
            } else {
                TutorialActivity.this.launchHomeScreen();
            }
        }
    }

    /* renamed from: org.secuso.privacyfriendlynetmonitor.Activities.TutorialActivity$3 */
    class C05533 implements OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        C05533() {
        }

        public void onPageSelected(int i) {
            TutorialActivity.this.addBottomDots(i);
            if (i == TutorialActivity.this.layouts.length - 1) {
                TutorialActivity.this.btnNext.setText(TutorialActivity.this.getString(C0501R.string.okay));
                TutorialActivity.this.btnSkip.setText(TutorialActivity.this.getString(C0501R.string.help_button));
                return;
            }
            TutorialActivity.this.btnNext.setText(TutorialActivity.this.getString(C0501R.string.next));
            TutorialActivity.this.btnSkip.setText(TutorialActivity.this.getString(C0501R.string.skip));
            TutorialActivity.this.btnSkip.setVisibility(0);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            this.layoutInflater = (LayoutInflater) TutorialActivity.this.getSystemService("layout_inflater");
            View inflate = this.layoutInflater.inflate(TutorialActivity.this.layouts[i], viewGroup, false);
            viewGroup.addView(inflate);
            return inflate;
        }

        public int getCount() {
            return TutorialActivity.this.layouts.length;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    public static void setTutorial_click(boolean z) {
        tutorial_click = z;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(1280);
        }
        setContentView((int) C0501R.layout.activity_tutorial);
        this.viewPager = (ViewPager) findViewById(C0501R.C0499id.view_pager);
        this.dotsLayout = (LinearLayout) findViewById(C0501R.C0499id.layoutDots);
        this.btnSkip = (Button) findViewById(C0501R.C0499id.btn_skip);
        this.btnNext = (Button) findViewById(C0501R.C0499id.btn_next);
        this.layouts = new int[]{C0501R.layout.tutorial_slide1, C0501R.layout.tutorial_slide2, C0501R.layout.tutorial_slide3};
        addBottomDots(0);
        changeStatusBarColor();
        this.myViewPagerAdapter = new MyViewPagerAdapter();
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);
        this.btnSkip.setOnClickListener(new C04941());
        this.btnNext.setOnClickListener(new C04952());
    }

    private void addBottomDots(int i) {
        this.dots = new TextView[this.layouts.length];
        int[] intArray = getResources().getIntArray(C0501R.array.array_dot_active);
        int[] intArray2 = getResources().getIntArray(C0501R.array.array_dot_inactive);
        this.dotsLayout.removeAllViews();
        for (int i2 = 0; i2 < this.dots.length; i2++) {
            this.dots[i2] = new TextView(this);
            this.dots[i2].setText(Html.fromHtml("&#8226;"));
            this.dots[i2].setTextSize(35.0f);
            this.dots[i2].setTextColor(intArray2[i]);
            this.dotsLayout.addView(this.dots[i2]);
        }
        if (this.dots.length > 0) {
            this.dots[i].setTextColor(intArray[i]);
        }
    }

    private int getItem(int i) {
        return this.viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        PrefManager prefManager = this.prefManager;
        PrefManager.setFirstTimeLaunch(false);
        startActivity(intent);
        finish();
    }

    private void launchHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.setFlags(67108864);
        PrefManager prefManager = this.prefManager;
        PrefManager.setFirstTimeLaunch(false);
        startActivity(intent);
        finish();
    }

    private void changeStatusBarColor() {
        if (VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }
}
