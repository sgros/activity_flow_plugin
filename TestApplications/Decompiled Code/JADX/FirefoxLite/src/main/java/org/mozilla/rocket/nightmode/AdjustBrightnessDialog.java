package org.mozilla.rocket.nightmode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.SeekBar;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.BaseActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.rocket.C0769R;

/* compiled from: AdjustBrightnessDialog.kt */
public final class AdjustBrightnessDialog extends BaseActivity {
    private final AdjustBrightnessDialog$mSeekListener$1 mSeekListener = new AdjustBrightnessDialog$mSeekListener$1(this);
    private SeekBar seekBar;

    /* compiled from: AdjustBrightnessDialog.kt */
    public static final class Constants {
        public static final Constants INSTANCE = new Constants();

        public final float progressToValue(int i) {
            return (float) ((((double) i) * 0.5d) / ((double) 100.0f));
        }

        public final int valueToProgress(float f) {
            return (int) (((double) (f * 100.0f)) / 0.5d);
        }

        private Constants() {
        }
    }

    /* compiled from: AdjustBrightnessDialog.kt */
    public static final class Intents {
        public static final Intents INSTANCE = new Intents();

        private Intents() {
        }

        public final Intent getStartIntentFromMenu(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return getStartIntent(context, "menu");
        }

        public final Intent getStartIntentFromSetting(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return getStartIntent(context, "setting");
        }

        private final Intent getStartIntent(Context context, String str) {
            Intent intent = new Intent(context, AdjustBrightnessDialog.class);
            int hashCode = str.hashCode();
            if (hashCode != 3347807) {
                if (hashCode == 1985941072 && str.equals("setting")) {
                    intent.putExtra("extra_source", "setting");
                }
            } else if (str.equals("menu")) {
                intent.putExtra("extra_source", "menu");
            }
            return intent;
        }
    }

    public void applyLocale() {
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0769R.layout.adjust_briteness_view);
        View findViewById = findViewById(C0427R.C0426id.brightness_slider);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "findViewById(R.id.brightness_slider)");
        this.seekBar = (SeekBar) findViewById;
        SeekBar seekBar = this.seekBar;
        if (seekBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("seekBar");
        }
        seekBar.setOnSeekBarChangeListener(this.mSeekListener);
        findViewById(C0427R.C0426id.brightness_root).setOnClickListener(new AdjustBrightnessDialog$onCreate$1(this));
        ViewUtils.updateStatusBarStyle(false, getWindow());
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        Settings instance = Settings.getInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(this)");
        int valueToProgress = Constants.INSTANCE.valueToProgress(instance.getNightModeBrightnessValue());
        SeekBar seekBar = this.seekBar;
        if (seekBar == null) {
            Intrinsics.throwUninitializedPropertyAccessException("seekBar");
        }
        seekBar.setProgress(valueToProgress);
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        boolean areEqual = Intrinsics.areEqual(getIntent().getStringExtra("extra_source"), "setting");
        Window window = getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window, "window");
        LayoutParams attributes = window.getAttributes();
        Settings instance = Settings.getInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Settings.getInstance(this)");
        instance.setNightModeBrightnessValue(attributes.screenBrightness);
        TelemetryWrapper.nightModeBrightnessChangeTo(Constants.INSTANCE.valueToProgress(attributes.screenBrightness), areEqual);
    }
}
