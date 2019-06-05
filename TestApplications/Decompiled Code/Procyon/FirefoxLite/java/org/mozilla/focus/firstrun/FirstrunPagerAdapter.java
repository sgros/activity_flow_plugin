// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.firstrun;

import android.widget.Button;
import android.graphics.drawable.Drawable;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.airbnb.lottie.LottieDrawable;
import android.widget.ImageView;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.view.View$OnClickListener;
import android.content.Context;
import android.support.v4.view.PagerAdapter;

public class FirstrunPagerAdapter extends PagerAdapter
{
    protected Context context;
    private View$OnClickListener listener;
    protected ArrayList<FirstrunPage> pages;
    
    public FirstrunPagerAdapter(final Context context, final View$OnClickListener listener) {
        this.pages = new ArrayList<FirstrunPage>();
        this.context = context;
        this.listener = listener;
    }
    
    @Override
    public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
        viewGroup.removeView((View)o);
    }
    
    @Override
    public int getCount() {
        return this.pages.size();
    }
    
    protected View getView(final int index, final ViewPager viewPager) {
        final View inflate = LayoutInflater.from(this.context).inflate(2131492948, (ViewGroup)viewPager, false);
        final FirstrunPage firstrunPage = this.pages.get(index);
        ((TextView)inflate.findViewById(2131296697)).setText((CharSequence)firstrunPage.title);
        final TextView textView = (TextView)inflate.findViewById(2131296685);
        textView.setText(firstrunPage.text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        final ImageView imageView = (ImageView)inflate.findViewById(2131296477);
        if (firstrunPage.lottieString != null) {
            final LottieDrawable imageDrawable = new LottieDrawable();
            LottieComposition.Factory.fromAssetFileName(this.context, firstrunPage.lottieString, new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(final LottieComposition composition) {
                    imageDrawable.setComposition(composition);
                }
            });
            imageView.setImageDrawable((Drawable)imageDrawable);
        }
        else {
            imageView.setImageResource(firstrunPage.imageResource);
        }
        final Button button = (Button)inflate.findViewById(2131296358);
        button.setOnClickListener(this.listener);
        if (index == this.pages.size() - 1) {
            button.setText(2131755217);
            button.setId(2131296443);
        }
        else {
            button.setText(2131755218);
            button.setId(2131296548);
        }
        return inflate;
    }
    
    @Override
    public Object instantiateItem(final ViewGroup viewGroup, final int n) {
        final ViewPager viewPager = (ViewPager)viewGroup;
        final View view = this.getView(n, viewPager);
        viewPager.addView(view);
        return view;
    }
    
    @Override
    public boolean isViewFromObject(final View view, final Object o) {
        return view == o;
    }
}
