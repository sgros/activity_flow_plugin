package org.mozilla.focus.firstrun;

import android.content.Context;
import android.support.p001v4.view.PagerAdapter;
import android.support.p001v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieComposition.Factory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import java.util.ArrayList;
import org.mozilla.focus.C0427R;
import org.mozilla.rocket.C0769R;

public class FirstrunPagerAdapter extends PagerAdapter {
    protected Context context;
    private OnClickListener listener;
    protected ArrayList<FirstrunPage> pages = new ArrayList();

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public FirstrunPagerAdapter(Context context, OnClickListener onClickListener) {
        this.context = context;
        this.listener = onClickListener;
    }

    /* Access modifiers changed, original: protected */
    public View getView(int i, ViewPager viewPager) {
        View inflate = LayoutInflater.from(this.context).inflate(C0769R.layout.firstrun_page, viewPager, false);
        FirstrunPage firstrunPage = (FirstrunPage) this.pages.get(i);
        ((TextView) inflate.findViewById(2131296697)).setText(firstrunPage.title);
        TextView textView = (TextView) inflate.findViewById(2131296685);
        textView.setText(firstrunPage.text);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        ImageView imageView = (ImageView) inflate.findViewById(2131296477);
        if (firstrunPage.lottieString != null) {
            final LottieDrawable lottieDrawable = new LottieDrawable();
            Factory.fromAssetFileName(this.context, firstrunPage.lottieString, new OnCompositionLoadedListener() {
                public void onCompositionLoaded(LottieComposition lottieComposition) {
                    lottieDrawable.setComposition(lottieComposition);
                }
            });
            imageView.setImageDrawable(lottieDrawable);
        } else {
            imageView.setImageResource(firstrunPage.imageResource);
        }
        Button button = (Button) inflate.findViewById(C0427R.C0426id.button);
        button.setOnClickListener(this.listener);
        if (i == this.pages.size() - 1) {
            button.setText(C0769R.string.firstrun_close_button);
            button.setId(C0427R.C0426id.finish);
        } else {
            button.setText(C0769R.string.firstrun_next_button);
            button.setId(C0427R.C0426id.next);
        }
        return inflate;
    }

    public int getCount() {
        return this.pages.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        ViewPager viewPager = (ViewPager) viewGroup;
        View view = getView(i, viewPager);
        viewPager.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }
}
