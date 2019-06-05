package org.mozilla.focus.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.theme.ThemeManager;

public class HomeScreenBackground extends ThemedImageView implements ThemeManager.Themeable {
   private boolean isNight;
   private Paint paint;

   public HomeScreenBackground(Context var1) {
      super(var1, (AttributeSet)null);
      this.init();
   }

   public HomeScreenBackground(Context var1, AttributeSet var2) {
      super(var1, var2, 0);
      this.init();
   }

   public HomeScreenBackground(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.init();
   }

   void init() {
      Rect var1 = new Rect();
      ((Activity)this.getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(var1);
      Bitmap var2 = BitmapFactory.decodeResource(this.getResources(), 2131230871);
      this.paint = new Paint();
      BitmapShader var11 = new BitmapShader(var2, TileMode.REPEAT, TileMode.REPEAT);
      int var3 = Color.parseColor("#99FFFFFF");
      int var4 = Color.parseColor("#4dFFFFFF");
      int var5 = Color.parseColor("#1aFFFFFF");
      int var6 = Color.parseColor("#00FFFFFF");
      float var7 = (float)var1.top;
      float var8 = (float)var1.bottom;
      TileMode var9 = TileMode.CLAMP;
      LinearGradient var10 = new LinearGradient(0.0F, var7, 0.0F, var8, new int[]{var3, var4, var5, var6}, new float[]{0.0F, 0.4F, 0.7F, 1.0F}, var9);
      this.paint.setShader(new ComposeShader(var10, var11, Mode.MULTIPLY));
   }

   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.isNight) {
         this.setPadding(0, ViewUtils.getStatusBarHeight((Activity)this.getContext()), 0, 0);
      } else {
         this.setPadding(0, 0, 0, 0);
      }

   }

   public void onThemeChanged() {
      this.setBackground(this.getContext().getTheme().getDrawable(2131230837));
   }

   public void setNightMode(boolean var1) {
      super.setNightMode(var1);
      this.isNight = var1;
      if (this.isNight) {
         this.setImageResource(2131230968);
      } else {
         this.setImageResource(2131230866);
      }

   }
}
