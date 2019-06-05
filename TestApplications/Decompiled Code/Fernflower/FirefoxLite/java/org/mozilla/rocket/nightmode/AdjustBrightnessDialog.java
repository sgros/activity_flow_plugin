package org.mozilla.rocket.nightmode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.activity.BaseActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.ViewUtils;

public final class AdjustBrightnessDialog extends BaseActivity {
   private final <undefinedtype> mSeekListener = new OnSeekBarChangeListener() {
      public void onProgressChanged(SeekBar var1, int var2, boolean var3) {
         Intrinsics.checkParameterIsNotNull(var1, "seekBar");
         if (var3) {
            Window var7 = AdjustBrightnessDialog.this.getWindow();
            Intrinsics.checkExpressionValueIsNotNull(var7, "window");
            LayoutParams var8 = var7.getAttributes();
            float var4 = AdjustBrightnessDialog.Constants.INSTANCE.progressToValue(var2);
            float var5 = var4;
            if ((double)var4 < 0.01D) {
               var5 = 0.01F;
            }

            var8.screenBrightness = var5;
            Window var6 = AdjustBrightnessDialog.this.getWindow();
            Intrinsics.checkExpressionValueIsNotNull(var6, "window");
            var6.setAttributes(var8);
         }

      }

      public void onStartTrackingTouch(SeekBar var1) {
         Intrinsics.checkParameterIsNotNull(var1, "seekBar");
      }

      public void onStopTrackingTouch(SeekBar var1) {
         Intrinsics.checkParameterIsNotNull(var1, "seekBar");
      }
   };
   private SeekBar seekBar;

   public void applyLocale() {
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2131492898);
      View var2 = this.findViewById(2131296330);
      Intrinsics.checkExpressionValueIsNotNull(var2, "findViewById(R.id.brightness_slider)");
      this.seekBar = (SeekBar)var2;
      SeekBar var3 = this.seekBar;
      if (var3 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("seekBar");
      }

      var3.setOnSeekBarChangeListener((OnSeekBarChangeListener)this.mSeekListener);
      this.findViewById(2131296329).setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            AdjustBrightnessDialog.this.finish();
         }
      }));
      ViewUtils.updateStatusBarStyle(false, this.getWindow());
   }

   protected void onPause() {
      super.onPause();
      boolean var1 = Intrinsics.areEqual(this.getIntent().getStringExtra("extra_source"), "setting");
      Window var2 = this.getWindow();
      Intrinsics.checkExpressionValueIsNotNull(var2, "window");
      LayoutParams var4 = var2.getAttributes();
      Settings var3 = Settings.getInstance((Context)this);
      Intrinsics.checkExpressionValueIsNotNull(var3, "Settings.getInstance(this)");
      var3.setNightModeBrightnessValue(var4.screenBrightness);
      TelemetryWrapper.nightModeBrightnessChangeTo(AdjustBrightnessDialog.Constants.INSTANCE.valueToProgress(var4.screenBrightness), var1);
   }

   protected void onResume() {
      super.onResume();
      Settings var1 = Settings.getInstance((Context)this);
      Intrinsics.checkExpressionValueIsNotNull(var1, "Settings.getInstance(this)");
      float var2 = var1.getNightModeBrightnessValue();
      int var3 = AdjustBrightnessDialog.Constants.INSTANCE.valueToProgress(var2);
      SeekBar var4 = this.seekBar;
      if (var4 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("seekBar");
      }

      var4.setProgress(var3);
   }

   public static final class Constants {
      public static final AdjustBrightnessDialog.Constants INSTANCE = new AdjustBrightnessDialog.Constants();

      private Constants() {
      }

      public final float progressToValue(int var1) {
         return (float)((double)var1 * 0.5D / (double)100.0F);
      }

      public final int valueToProgress(float var1) {
         return (int)((double)(var1 * 100.0F) / 0.5D);
      }
   }

   public static final class Intents {
      public static final AdjustBrightnessDialog.Intents INSTANCE = new AdjustBrightnessDialog.Intents();

      private Intents() {
      }

      private final Intent getStartIntent(Context var1, String var2) {
         Intent var4 = new Intent(var1, AdjustBrightnessDialog.class);
         int var3 = var2.hashCode();
         if (var3 != 3347807) {
            if (var3 == 1985941072 && var2.equals("setting")) {
               var4.putExtra("extra_source", "setting");
            }
         } else if (var2.equals("menu")) {
            var4.putExtra("extra_source", "menu");
         }

         return var4;
      }

      public final Intent getStartIntentFromMenu(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         return this.getStartIntent(var1, "menu");
      }

      public final Intent getStartIntentFromSetting(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         return this.getStartIntent(var1, "setting");
      }
   }
}
