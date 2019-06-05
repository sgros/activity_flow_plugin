package org.mozilla.focus.firstrun;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import java.util.ArrayList;

public class FirstrunPagerAdapter extends PagerAdapter {
   protected Context context;
   private OnClickListener listener;
   protected ArrayList pages = new ArrayList();

   public FirstrunPagerAdapter(Context var1, OnClickListener var2) {
      this.context = var1;
      this.listener = var2;
   }

   public void destroyItem(ViewGroup var1, int var2, Object var3) {
      var1.removeView((View)var3);
   }

   public int getCount() {
      return this.pages.size();
   }

   protected View getView(int var1, ViewPager var2) {
      View var6 = LayoutInflater.from(this.context).inflate(2131492948, var2, false);
      FirstrunPage var3 = (FirstrunPage)this.pages.get(var1);
      ((TextView)var6.findViewById(2131296697)).setText(var3.title);
      TextView var4 = (TextView)var6.findViewById(2131296685);
      var4.setText(var3.text);
      var4.setMovementMethod(LinkMovementMethod.getInstance());
      ImageView var8 = (ImageView)var6.findViewById(2131296477);
      if (var3.lottieString != null) {
         final LottieDrawable var5 = new LottieDrawable();
         LottieComposition.Factory.fromAssetFileName(this.context, var3.lottieString, new OnCompositionLoadedListener() {
            public void onCompositionLoaded(LottieComposition var1) {
               var5.setComposition(var1);
            }
         });
         var8.setImageDrawable(var5);
      } else {
         var8.setImageResource(var3.imageResource);
      }

      Button var7 = (Button)var6.findViewById(2131296358);
      var7.setOnClickListener(this.listener);
      if (var1 == this.pages.size() - 1) {
         var7.setText(2131755217);
         var7.setId(2131296443);
      } else {
         var7.setText(2131755218);
         var7.setId(2131296548);
      }

      return var6;
   }

   public Object instantiateItem(ViewGroup var1, int var2) {
      ViewPager var4 = (ViewPager)var1;
      View var3 = this.getView(var2, var4);
      var4.addView(var3);
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
